package org.eclipse.emf.compare.uml2.diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.logical.extension.IPostProcessor;
import org.eclipse.emf.compare.uml2.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

public class UMLDiffExtensionPostProcessor implements IPostProcessor {

	/**
	 * UML2 extensions factories.
	 */
	private Set<IDiffExtensionFactory> uml2ExtensionFactories;

	public UMLDiffExtensionPostProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void postMatch(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	public void postDiff(Comparison comparison) {

	}

	public void postRequirements(Comparison comparison, CrossReferencer crossReferencer) {
		final Map<Class<? extends Diff>, IDiffExtensionFactory> mapUml2ExtensionFactories = DiffExtensionFactoryRegistry
				.createExtensionFactories();
		uml2ExtensionFactories = new HashSet<IDiffExtensionFactory>(mapUml2ExtensionFactories.values());

		// Creation of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff, crossReferencer);
		}

		// Filling of the requirements link of the UML difference extensions
		for (Diff umlExtension : comparison.getDifferences()) {
			if (umlExtension instanceof UMLExtension) {
				final Class<?> classDiffElement = umlExtension.eClass().getInstanceClass();
				final IDiffExtensionFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences((UMLExtension)umlExtension, crossReferencer);
				}
			}
		}
	}

	public void postEquivalences(Comparison comparison, CrossReferencer crossReferencer) {
		// TODO Auto-generated method stub

	}

	public void postConflicts(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates the difference extensions in relation to the existing {@link DiffElement}s.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 * @param diffModelCrossReferencer
	 *            The cross referencer.
	 */
	private void applyManagedTypes(Diff element, EcoreUtil.CrossReferencer diffModelCrossReferencer) {
		for (IDiffExtensionFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				final Diff extension = factory.create(element, diffModelCrossReferencer);
				final Match match = factory.getParentMatch(element, diffModelCrossReferencer);
				match.getDifferences().add(extension);
			}
		}
	}

}
