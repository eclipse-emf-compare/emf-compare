/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - progress reporting
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.factories.DiagramExtensionFactoryRegistry;
import org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;

/**
 * Post-processor to create the diagram difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CompareDiagramPostProcessor implements IPostProcessor {

	/** Registry of diagram difference extension factories. */
	private Set<IChangeFactory> diagramExtensionFactories;

	/** Diagram comparison configuration. */
	private CompareDiagramConfiguration configuration;

	/**
	 * Set the diagram comparison configuration.
	 * 
	 * @param configuration
	 *            The configuration.
	 */
	public void setConfiguration(CompareDiagramConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
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
		final Map<Class<? extends Diff>, IChangeFactory> mapDiagramExtensionFactories = DiagramExtensionFactoryRegistry
				.createExtensionFactories(configuration);
		diagramExtensionFactories = new LinkedHashSet<IChangeFactory>(mapDiagramExtensionFactories.values());

		// Creation of the diagram difference extensions
		List<Diff> differences = comparison.getDifferences();
		int diffCount = differences.size();
		for (int i = 0; i < diffCount; i++) {
			applyManagedTypes(differences.get(i));
			reportProgress(monitor, "CompareDiagramPostProcessor.monitor.applyManagedTypes", i + 1, //$NON-NLS-1$
					diffCount);
		}

		// Filling of the requirements link of the difference extensions
		// we must not reuse the variable "differences" as applyManagedTypes(Diff) may have added diffs
		differences = comparison.getDifferences();
		diffCount = differences.size();
		for (int i = 0; i < diffCount; i++) {
			final Diff diff = differences.get(i);
			if (diff instanceof DiagramDiff) {
				final Class<?> classDiffElement = diff.eClass().getInstanceClass();
				final IChangeFactory diffFactory = mapDiagramExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, diff);
				}
			}
			reportProgress(monitor, "CompareDiagramPostProcessor.monitor.fillRequiredDifferences", i + 1, //$NON-NLS-1$
					diffCount);
		}
	}

	/**
	 * Reports the progress to the given <code>monitor</code> for the given <code>msgKey</code> in
	 * {@link CompareDiagramUIMessages} with the <code>currentDiffIndex</code> of the total
	 * <code>diffCount</code>. This method also checks for cancellation.
	 * 
	 * @param monitor
	 *            The monitor to report progress.
	 * @param msgKey
	 *            The message key in {@link UMLCompareMessages}.
	 * @param currentDiffIndex
	 *            The current diff index.
	 * @param diffCount
	 *            The total diff count.
	 */
	private void reportProgress(Monitor monitor, String msgKey, int currentDiffIndex, int diffCount) {
		if (currentDiffIndex % 100 == 1) {
			monitor.subTask(CompareDiagramUIMessages.getString(msgKey, Integer.valueOf(currentDiffIndex),
					Integer.valueOf(diffCount)));
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}
		}
	}

	/**
	 * Creates the difference extensions in relation to the existing difference.
	 * 
	 * @param element
	 *            The current candidate difference for the build of the diagram extension.
	 */
	private void applyManagedTypes(Diff element) {
		for (IChangeFactory factory : diagramExtensionFactories) {
			if (factory.handles(element)) {
				final Diff extension = factory.create(element);
				final Match match = factory.getParentMatch(element);
				match.getDifferences().add(extension);
			}
		}
	}

}
