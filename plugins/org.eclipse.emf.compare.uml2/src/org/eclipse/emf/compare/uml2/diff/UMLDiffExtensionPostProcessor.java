package org.eclipse.emf.compare.uml2.diff;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.extension.IPostProcessor;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLDiffExtensionPostProcessor implements IPostProcessor {
	/** Some references will be ignored as they are derived, but not marked as such. */
	private static final Set<EReference> IGNORED_REFERENCES = ignoredReference();

	/**
	 * UML2 extensions factories.
	 */
	private Set<IDiffExtensionFactory> uml2ExtensionFactories;

	public UMLDiffExtensionPostProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		for (Diff ignore : filter(comparison.getDifferences(), diffOnIgnoredReference())) {
			EcoreUtil.delete(ignore);
		}
	}

	private static Predicate<? super Diff> diffOnIgnoredReference() {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof ReferenceChange) {
					return IGNORED_REFERENCES.contains(((ReferenceChange)input).getReference());
				}
				return false;
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
		final Map<Class<? extends Diff>, IDiffExtensionFactory> mapUml2ExtensionFactories = DiffExtensionFactoryRegistry
				.createExtensionFactories();
		uml2ExtensionFactories = new HashSet<IDiffExtensionFactory>(mapUml2ExtensionFactories.values());

		// Creation of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff);
		}

		// Filling of the requirements link of the UML difference extensions
		for (Diff umlDiff : comparison.getDifferences()) {
			if (umlDiff instanceof UMLDiff) {
				final Class<?> classDiffElement = umlDiff.eClass().getInstanceClass();
				final IDiffExtensionFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, (UMLDiff)umlDiff);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {

	}

	/**
	 * Creates the difference extensions in relation to the existing {@link DiffElement}s.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 * @param diffModelCrossReferencer
	 *            The cross referencer.
	 */
	private void applyManagedTypes(Diff element) {
		for (IDiffExtensionFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				final Diff extension = factory.create(element);
				final Match match = factory.getParentMatch(element);
				match.getDifferences().add(extension);
			}
		}
	}

	private static Set<EReference> ignoredReference() {
		// @formatter:off
		// FIXME test each of the following thoroughly to
		// 1 - check that they indeed cause trouble with merging
		// 2 - check that they can indeed be ignored without breaking anything
		return ImmutableSet.of(
				UMLPackage.Literals.ASSOCIATION__MEMBER_END,
				UMLPackage.Literals.DEPENDENCY__CLIENT
//				UMLPackage.Literals.ACTION__INPUT,
//				UMLPackage.Literals.ACTION__OUTPUT,
//				UMLPackage.Literals.ACTIVITY_GROUP__CONTAINED_EDGE,
//				UMLPackage.Literals.ACTIVITY_GROUP__CONTAINED_NODE,
//				UMLPackage.Literals.ACTIVITY_GROUP__SUBGROUP,
//				UMLPackage.Literals.CLASSIFIER__ATTRIBUTE,
//				UMLPackage.Literals.CLASSIFIER__FEATURE,
//				UMLPackage.Literals.ELEMENT__OWNED_ELEMENT,
//				UMLPackage.Literals.NAMESPACE__MEMBER,
//				UMLPackage.Literals.NAMESPACE__OWNED_MEMBER,
//				UMLPackage.Literals.STRUCTURED_CLASSIFIER__ROLE,
//				UMLPackage.Literals.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL,
//				UMLPackage.Literals.TEMPLATE_PARAMETER__PARAMETERED_ELEMENT,
//				UMLPackage.Literals.TEMPLATE_PARAMETER__DEFAULT,
//				UMLPackage.Literals.TEMPLATE_SIGNATURE__PARAMETER
				);
		// @formatter:on
	}
}
