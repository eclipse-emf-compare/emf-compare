package org.eclipse.emf.compare.uml2.diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.extension.IPostProcessor;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;

public class UMLDiffExtensionPostProcessor implements IPostProcessor {

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
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison)
	 * @see #postMatch(Comparison, Monitor)
	 */
	@Deprecated
	public void postMatch(Comparison comparison) {
		postMatch(comparison, new BasicMonitor());
	}

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the match step,
	 * from a <code>comparison</code>.
	 * <p>
	 * This method should be pull-up to the interface in the next major version.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison after the match step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison)
	 * @se {@link #postDiff(Comparison, BasicMonitor)}
	 */
	@Deprecated
	public void postDiff(Comparison comparison) {
		postDiff(comparison, new BasicMonitor());
	}

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the difference
	 * step, from a <code>comparison</code>.
	 * <p>
	 * This method should be pull-up to the interface in the next major version.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison after the difference step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison)
	 * @see #postRequirements(Comparison, Monitor)
	 */
	@Deprecated
	public void postRequirements(Comparison comparison) {
		postRequirements(comparison, new BasicMonitor());
	}

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the requirements
	 * step, from a <code>comparison</code>.
	 * <p>
	 * This method should be pull-up to the interface in the next major version.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison after the requirements step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
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
		for (Diff UMLDiff : comparison.getDifferences()) {
			if (UMLDiff instanceof UMLDiff) {
				final Class<?> classDiffElement = UMLDiff.eClass().getInstanceClass();
				final IDiffExtensionFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, (UMLDiff)UMLDiff);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison)
	 * @see #postEquivalences(Comparison, Monitor)
	 */
	@Deprecated
	public void postEquivalences(Comparison comparison) {
		postEquivalences(comparison, new BasicMonitor());
	}

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the equivalences
	 * step, from a <code>comparison</code>.
	 * <p>
	 * This method should be pull-up to the interface in the next major version.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison after the equivalences step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison)
	 * @see #postConflicts(Comparison, Monitor)
	 */
	@Deprecated
	public void postConflicts(Comparison comparison) {
		postConflicts(comparison, new BasicMonitor());
	}

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the conflicts step,
	 * from a <code>comparison</code>.
	 * <p>
	 * This method should be pull-up to the interface in the next major version.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison after the conflicts step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
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

}
