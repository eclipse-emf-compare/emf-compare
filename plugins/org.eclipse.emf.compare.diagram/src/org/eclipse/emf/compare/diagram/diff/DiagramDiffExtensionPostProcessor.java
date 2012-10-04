package org.eclipse.emf.compare.diagram.diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.compare.extension.IPostProcessor;

public class DiagramDiffExtensionPostProcessor implements IPostProcessor {

	private Set<IDiffExtensionFactory> diagramExtensionFactories;

	public DiagramDiffExtensionPostProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void postMatch(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	public void postDiff(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	public void postRequirements(Comparison comparison) {
		final Map<Class<? extends Diff>, IDiffExtensionFactory> mapUml2ExtensionFactories = DiffExtensionFactoryRegistry
				.createExtensionFactories();
		diagramExtensionFactories = new HashSet<IDiffExtensionFactory>(mapUml2ExtensionFactories.values());

		// Creation of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff);
		}

		// Filling of the requirements link of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof DiagramDiff) {
				final Class<?> classDiffElement = diff.eClass().getInstanceClass();
				final IDiffExtensionFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, (DiagramDiff)diff);
				}
			}
		}
	}

	public void postEquivalences(Comparison comparison) {
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
	private void applyManagedTypes(Diff element) {
		for (IDiffExtensionFactory factory : diagramExtensionFactories) {
			if (factory.handles(element)) {
				final Diff extension = factory.create(element);
				final Match match = factory.getParentMatch(element);
				match.getDifferences().add(extension);
			}
		}
	}

}
