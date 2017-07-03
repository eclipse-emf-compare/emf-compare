/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - Consider profile definition changes on origin (bug 495259)
 *     Philip Langer - bug 508665
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.delete;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.uml2.uml.UMLPackage.Literals.INSTANCE_SPECIFICATION__CLASSIFIER;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
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
	 * It checks the profile applications, matched by the given match, are based on the same profile version.
	 * <br>
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
				EAnnotation originAnnot = (EAnnotation)annotationMatch.getOrigin();
				EAnnotation leftAnnot = (EAnnotation)annotationMatch.getLeft();
				EAnnotation rightAnnot = (EAnnotation)annotationMatch.getRight();
				if (!checkProfileVersion(match.getComparison(), (ProfileApplication)left, originAnnot,
						leftAnnot, rightAnnot)) {
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
	 * @param originAnnot
	 *            The annotation referencing the profile on the origin side
	 * @param leftAnnot
	 *            The annotation referencing the profile on the left side
	 * @param rightAnnot
	 *            The annotation referencing the profile on the right side
	 * @return False if the version of the referenced profile is different, True otherwise.
	 */
	private boolean checkProfileVersion(Comparison comparison, ProfileApplication profileApplication,
			EAnnotation originAnnot, EAnnotation leftAnnot, EAnnotation rightAnnot) {
		Collection<URI> originUris = Lists.newArrayList();
		if (comparison.isThreeWay()) {
			originUris = getNormalizedURIs(
					ReferenceUtil.getAsList(originAnnot, EcorePackage.Literals.EANNOTATION__REFERENCES));
		}
		Collection<URI> leftUris = getNormalizedURIs(
				ReferenceUtil.getAsList(leftAnnot, EcorePackage.Literals.EANNOTATION__REFERENCES));
		Collection<URI> rightUris = getNormalizedURIs(
				ReferenceUtil.getAsList(rightAnnot, EcorePackage.Literals.EANNOTATION__REFERENCES));

		boolean notEqualSize = leftUris.size() != rightUris.size()
				|| (comparison.isThreeWay() && leftUris.size() != originUris.size());

		if (notEqualSize || !leftUris.containsAll(rightUris)
				|| (comparison.isThreeWay() && !leftUris.containsAll(originUris))) {
			// different uri on one of the sides
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
		((BasicDiagnostic)comparison.getDiagnostic()).merge(diagnostic);
	}

	/**
	 * Get the normalized URI of the given ecore objects.
	 * 
	 * @param eObjects
	 *            The ecore objects.
	 * @return the list of the URI.
	 */
	private Collection<URI> getNormalizedURIs(List<Object> eObjects) {
		Function<Object, URI> eObjectToURI = new Function<Object, URI>() {
			public URI apply(Object input) {
				if (input instanceof EObject) {
					URI uri = EcoreUtil.getURI((EObject)input);
					URIConverter uriConverter = getURIConverter((EObject)input);
					if (uriConverter != null) {
						uri = uriConverter.normalize(uri);
					}
					return uri;
				}
				return null;
			}

			private URIConverter getURIConverter(EObject eObject) {
				Resource resource = eObject.eResource();
				if (resource != null) {
					ResourceSet resourceSet = resource.getResourceSet();
					if (resourceSet != null) {
						return resourceSet.getURIConverter();
					}
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
		// And delete enumeration literal classifier changes, as it is actually a derived feature
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof ReferenceChange) {
				final ReferenceChange referenceChange = (ReferenceChange)diff;
				fillImplicationsWithUMLSubsets(referenceChange);
				deleteEnumerationLiteralClassifierChanges(referenceChange);
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
				if (superSetDiff instanceof ReferenceChange && superSetDiff.getSource() == diff.getSource()
						&& ((ReferenceChange)superSetDiff).getReference() == superSet
						&& ((ReferenceChange)superSetDiff).getValue() == diff.getValue()
						&& superSetDiff.getMatch() == diff.getMatch()) {
					if (isAddOrSetDiff(diff) && isAddOrSetDiff(superSetDiff)) {
						diff.getImplies().add(superSetDiff);
					} else if (isDeleteOrUnsetDiff(diff) && isDeleteOrUnsetDiff(superSetDiff)) {
						diff.getImpliedBy().add(superSetDiff);
					}
				}
			}
		}
		// If we have an eOpposite and we are a containment reference, then we also imply/are implied by the
		// changes to this opposite's subset-superset reference.
		// "interfaceRealization" is an eOpposite of "implementingClassifier" which in turn is a super set of
		// "InterfaceRealization#client". Adding the InterfaceRealization into a Class#interfaceRealizations
		// reference implies the setting of this particular realization's "client" reference.
		if (reference.isContainment() && reference.getEOpposite() != null) {
			for (EReference superSet : UMLCompareUtil
					.getNonUnionSupersetReferences(reference.getEOpposite())) {
				Comparison comparison = diff.getMatch().getComparison();
				for (Diff superSetDiff : comparison.getDifferences(superSet)) {
					if (superSetDiff instanceof ReferenceChange
							&& superSetDiff.getSource() == diff.getSource()
							&& ((ReferenceChange)superSetDiff).getReference() == superSet
							&& superSetDiff.getMatch() == comparison.getMatch(diff.getValue())) {
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

	/**
	 * Deletes the given <code>referenceChange</code>, if it is an {@link EnumerationLiteral#getClassifier()
	 * enumeration literal classifier} change.
	 * 
	 * @param referenceChange
	 *            The reference change to check and delete.
	 */
	private void deleteEnumerationLiteralClassifierChanges(ReferenceChange referenceChange) {
		if (isEnumerationLiteralClassifierChange(referenceChange)) {
			delete(referenceChange);
		}
	}

	/**
	 * Specifies whether the given <code>referenceChange</code> is a change of an
	 * {@link EnumerationLiteral#getClassifier() enumeration literal classifier}, whereas the type of the
	 * reference change's value must be Enumeration.
	 * 
	 * @param referenceChange
	 *            The reference change to check.
	 * @return <code>true</code> if it is a EnumerationLiteral classifier change, <code>false</code>
	 *         otherwise.
	 */
	private boolean isEnumerationLiteralClassifierChange(ReferenceChange referenceChange) {
		return INSTANCE_SPECIFICATION__CLASSIFIER.equals(referenceChange.getReference())
				&& getAnyMatchedEObject(referenceChange) instanceof EnumerationLiteral
				&& referenceChange.getValue() instanceof Enumeration;
	}

	/**
	 * Returns the left-, right-, or origin object of the given <code>diff</code>'s match.
	 * 
	 * @param diff
	 *            The diff to get the matched object for.
	 * @return The matched object of any side.
	 */
	private EObject getAnyMatchedEObject(Diff diff) {
		final Match match = diff.getMatch();
		final EObject eObject;
		if (match.getLeft() != null) {
			eObject = match.getLeft();
		} else if (match.getRight() != null) {
			eObject = match.getRight();
		} else {
			eObject = match.getOrigin();
		}
		return eObject;
	}
}
