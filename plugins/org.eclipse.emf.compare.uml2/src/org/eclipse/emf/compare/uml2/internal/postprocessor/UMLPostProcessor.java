/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.UMLCompareMessages;
import org.eclipse.emf.compare.uml2.internal.UMLComparePlugin;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.UMLExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.ProfileApplication;

/**
 * Post-processor to create the UML difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLPostProcessor implements IPostProcessor {

	/**
	 * Predicate to find the match of an annotation referencing a profile definition, within a list a matches.
	 */
	private static final Predicate<Match> ANNOTATION_REFERENCING_PROFILE_DEFINITION = new Predicate<Match>() {

		private Pattern umlNsPattern = Pattern.compile("http://www\\.eclipse\\.org/uml2/.*/UML"); //$NON-NLS-1$

		public boolean apply(Match input) {
			return input.getLeft() instanceof EAnnotation
					&& umlNsPattern.matcher(((EAnnotation)input.getLeft()).getSource()).matches();
		}
	};

	/** UML2 extensions factories. */
	private Set<IChangeFactory> uml2ExtensionFactories;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {
		// Check the version of the applied profile on each matched profile application.
		boolean isSameProfileVersion = true;

		Iterator<Match> matchesRoot = comparison.getMatches().iterator();
		while (matchesRoot.hasNext() && isSameProfileVersion) {
			Match matchRoot = matchesRoot.next();

			isSameProfileVersion = checkProfileVersion(matchRoot);

			Iterator<Match> matches = matchRoot.getAllSubmatches().iterator();
			while (matches.hasNext() && isSameProfileVersion) {
				Match match = matches.next();

				isSameProfileVersion = checkProfileVersion(match);
			}
		}
	}

	/**
	 * It checks the profile applications, matched by the given match, are based on the same profile version.<br>
	 * It adds a diagnostic (error) to the comparison as soon as a difference is met.
	 * 
	 * @param match
	 *            A match of profile applications.
	 * @return False if a couple of profile applications at least is not based on the same profile version,
	 *         True otherwise.
	 */
	private boolean checkProfileVersion(Match match) {
		EObject left = match.getLeft();
		EObject right = match.getRight();
		if (left instanceof ProfileApplication && right != null) {
			Collection<Match> annotationsMatches = Collections2.filter(match.getSubmatches(),
					ANNOTATION_REFERENCING_PROFILE_DEFINITION);
			for (Match annotationMatch : annotationsMatches) {
				EAnnotation leftAnnot = (EAnnotation)annotationMatch.getLeft();
				EAnnotation rightAnnot = (EAnnotation)annotationMatch.getRight();
				if (!checkProfileVersion(match.getComparison(), (ProfileApplication)left, leftAnnot,
						rightAnnot)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * It checks the profile annotations reference the same profile version.<br>
	 * It adds a diagnostic (error) to the comparison as soon as an annotation does not reference the same
	 * profile version as the annotation from the other side.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param profileApplication
	 *            The profile application to compare (on the left or right side)
	 * @param leftAnnot
	 *            The annotation referencing the profile on the left side
	 * @param rightAnnot
	 *            The annotation referencing the profile on the right side
	 * @return False if the version of the referenced profile is different, True otherwise.
	 */
	private boolean checkProfileVersion(Comparison comparison, ProfileApplication profileApplication,
			EAnnotation leftAnnot, EAnnotation rightAnnot) {
		Collection<URI> leftUris = getURIs(ReferenceUtil.getAsList(leftAnnot,
				EcorePackage.Literals.EANNOTATION__REFERENCES));
		Collection<URI> rightUris = getURIs(ReferenceUtil.getAsList(rightAnnot,
				EcorePackage.Literals.EANNOTATION__REFERENCES));
		if (leftUris.size() != rightUris.size() || !leftUris.containsAll(rightUris)) {
			org.eclipse.uml2.uml.Package impactedPackage = profileApplication.getApplyingPackage();
			String message;
			if (impactedPackage != null) {
				message = UMLCompareMessages.getString("profile.definition.changed.on", "<" //$NON-NLS-1$//$NON-NLS-2$
						+ impactedPackage.eClass().getName() + "> " + impactedPackage.getName()); //$NON-NLS-1$
			} else {
				message = UMLCompareMessages.getString("profile.definition.changed"); //$NON-NLS-1$
			}

			addDiagnostic(comparison, new BasicDiagnostic(Diagnostic.ERROR, UMLComparePlugin.PLUGIN_ID, 0,
					message, new Object[] {}));

			return false;
		}
		return true;
	}

	/**
	 * It adds a diagnostic to the given comparison.
	 * 
	 * @param comparison
	 *            The comparison
	 * @param diagnostic
	 *            The diagnostic
	 */
	private void addDiagnostic(Comparison comparison, Diagnostic diagnostic) {
		Diagnostic currentDiag = comparison.getDiagnostic();
		if (currentDiag == null) {
			comparison.setDiagnostic(new BasicDiagnostic(UMLComparePlugin.PLUGIN_ID, 0, null, new Object[0]));
		}
		((BasicDiagnostic)comparison.getDiagnostic()).add(diagnostic);
	}

	/**
	 * Get the URI of the given ecore objects.
	 * 
	 * @param eObjects
	 *            The ecore objects.
	 * @return the list of the URI.
	 */
	private Collection<URI> getURIs(List<Object> eObjects) {
		Function<Object, URI> eObjectToURI = new Function<Object, URI>() {
			public URI apply(Object input) {
				if (input instanceof EObject) {
					return EcoreUtil.getURI((EObject)input);
				}
				return null;
			}
		};
		return Collections2.transform(eObjects, eObjectToURI);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		// Not needed here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postComparison(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {

		final Map<Class<? extends Diff>, IChangeFactory> mapUml2ExtensionFactories = UMLExtensionFactoryRegistry
				.createExtensionFactories();
		uml2ExtensionFactories = new LinkedHashSet<IChangeFactory>(mapUml2ExtensionFactories.values());

		// Creation of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff);
		}

		// Filling of the requirements link of the UML difference extensions
		for (Diff umlDiff : comparison.getDifferences()) {
			if (umlDiff instanceof UMLDiff) {
				final Class<?> classDiffElement = umlDiff.eClass().getInstanceClass();
				final IChangeFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, umlDiff);
				}
			}
		}

		// Filling implications with subsets
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof ReferenceChange) {
				fillImplicationsWithUMLSubsets((ReferenceChange)diff);
			}
		}

	}

	/**
	 * Creates the difference extensions in relation to the existing {@link DiffElement}s.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 */
	private void applyManagedTypes(Diff element) {
		for (IChangeFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				Diff extension = factory.create(element);
				// FIXME: Instantiation of UML extensions (intersections of predicates)
				if (!extension.getRefinedBy().isEmpty()) {
					final Match match = factory.getParentMatch(element);
					// FIXME: why the match may be null ? (see AddAssociation2Test.testMergeLtRA30UseCase)
					if (match != null) {
						match.getDifferences().add(extension);
					}
				} else {
					extension = null;
				}

			}
		}
	}

	/**
	 * Fill the implication links ({@link Diff#getImplies()}, {@link Diff#getImpliedBy()}) on the given
	 * reference change.
	 * 
	 * @param diff
	 *            The reference change.
	 */
	private void fillImplicationsWithUMLSubsets(ReferenceChange diff) {
		EReference reference = diff.getReference();
		// ADD implies ADD on non union supersets
		// DELETE is implied by DEL on non union supersets
		for (EReference superSet : UMLCompareUtil.getNonUnionSupersetReferences(reference)) {
			Comparison comparison = diff.getMatch().getComparison();
			for (Diff superSetDiff : comparison.getDifferences(superSet)) {
				// Only keep diffs on the same ref and value where parent matches
				if (superSetDiff instanceof ReferenceChange
						&& ((ReferenceChange)superSetDiff).getReference() == superSet
						&& ((ReferenceChange)superSetDiff).getValue() == diff.getValue()
						&& ((ReferenceChange)superSetDiff).getMatch() == diff.getMatch()) {
					if (isAddOrSetDiff(diff) && isAddOrSetDiff(superSetDiff)) {
						diff.getImplies().add(superSetDiff);
					} else if (isDeleteOrUnsetDiff(diff) && isDeleteOrUnsetDiff(superSetDiff)) {
						diff.getImpliedBy().add(superSetDiff);
					}
				}
			}
		}
	}
}
