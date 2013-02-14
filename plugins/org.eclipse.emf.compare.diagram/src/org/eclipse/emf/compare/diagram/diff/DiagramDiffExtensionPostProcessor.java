/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.compare.extension.IPostProcessor;

/**
 * Post-processor to create the diagram difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramDiffExtensionPostProcessor implements IPostProcessor {

	/** Registry of diagram difference extension factories. */
	private Set<IDiffExtensionFactory> diagramExtensionFactories;

	/** Diagram comparison configuration. */
	private DiagramComparisonConfiguration configuration;

	/**
	 * Constructor.
	 */
	public DiagramDiffExtensionPostProcessor() {

	}

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The diagram comparison configuration.
	 */
	public DiagramDiffExtensionPostProcessor(DiagramComparisonConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Set the diagram comparison configuration.
	 * 
	 * @param configuration
	 *            The configuration.
	 */
	public void setConfiguration(DiagramComparisonConfiguration configuration) {
		this.configuration = configuration;
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
		final Map<Class<? extends Diff>, IDiffExtensionFactory> mapDiagramExtensionFactories = DiffExtensionFactoryRegistry
				.createExtensionFactories(configuration);
		diagramExtensionFactories = new HashSet<IDiffExtensionFactory>(mapDiagramExtensionFactories.values());

		// Creation of the diagram difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff);
		}

		// Filling of the requirements link of the difference extensions
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof DiagramDiff) {
				final Class<?> classDiffElement = diff.eClass().getInstanceClass();
				final IDiffExtensionFactory diffFactory = mapDiagramExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, diff);
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
	 * Creates the difference extensions in relation to the existing difference.
	 * 
	 * @param element
	 *            The current candidate difference for the build of the diagram extension.
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
