package org.eclipse.emf.compare.uml2.diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

}
