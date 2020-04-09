/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.sirius.tests;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.sirius.internal.SiriusDiffPostProcessor;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeRunnableImpl;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.CachingDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.BasicPostProcessorDescriptorImpl;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;

/**
 * This class helps to initialize and use the registers for Sirius tests.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public abstract class AbstractSiriusTest {

	/**
	 * The PostProcessors registry.
	 */
	private IPostProcessor.Descriptor.Registry<String> postProcessorRegistry;

	/**
	 * Used to initialize a default merger registry for merge action.
	 */
	private IMerger.Registry mergerRegistry;

	/**
	 * Used to merge differences.
	 */
	private IMergeRunnable mergeRunnable;

	/**
	 * Used to initialize the registers.
	 */
	protected void fillRegistries() {
		postProcessorRegistry = registerSiriusPostProcessors();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
	}

	/**
	 * Used to register new post processors.
	 * 
	 * @see org.eclipse.emf.compare.diagram.sirius.internal.SiriusDiffPostProcessor
	 * @see org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor
	 * @return the register composed of SiriusDiffPostProcessor and CompareDiagramPostProcessor.
	 */
	protected IPostProcessor.Descriptor.Registry<String> registerSiriusPostProcessors() {
		final IPostProcessor.Descriptor.Registry<String> registry = new PostProcessorDescriptorRegistryImpl<String>();

		registry.put(SiriusDiffPostProcessor.class.getName(),
				new BasicPostProcessorDescriptorImpl(new SiriusDiffPostProcessor(),
						Pattern.compile("http://www.eclipse.org/sirius/1.1.0"), //$NON-NLS-1$
						Pattern.compile(""))); //$NON-NLS-1$
		registry.put(CompareDiagramPostProcessor.class.getName(),
				new BasicPostProcessorDescriptorImpl(new CompareDiagramPostProcessor(),
						Pattern.compile("http://www.eclipse.org/gmf/runtime/1.0.2/notation"), //$NON-NLS-1$
						Pattern.compile(""))); //$NON-NLS-1$
		return registry;
	}

	/**
	 * Executes a right to left merge of a list of differences.
	 * 
	 * @param differences
	 *            the list of differences.
	 */
	protected void mergeDiffsRightToLeft(List<? extends Diff> differences) {
		mergeDiffs(differences, false);
	}

	/**
	 * Executes a left to right merge of a list of differences.
	 * 
	 * @param differences
	 *            the list of differences.
	 */
	protected void mergeDiffsLeftToRight(List<? extends Diff> differences) {
		mergeDiffs(differences, true);
	}

	/**
	 * Executes a right to left or left to right merge of a list of differences.
	 * 
	 * @param differences
	 *            the list of differences.
	 * @param leftToRight
	 *            the merge direction.
	 */
	private void mergeDiffs(List<? extends Diff> differences, boolean leftToRight) {
		IDiffRelationshipComputer computer = new CachingDiffRelationshipComputer(mergerRegistry);
		final MergeMode mergeMode;
		if (leftToRight) {
			mergeMode = MergeMode.LEFT_TO_RIGHT;
		} else {
			mergeMode = MergeMode.RIGHT_TO_LEFT;
		}
		mergeRunnable = new MergeRunnableImpl(true, true, mergeMode, computer);
		mergeRunnable.merge(differences, leftToRight, mergerRegistry);
	}

	/**
	 * Returns the post processor registry.
	 * 
	 * @return postProcessorRegistry.
	 */
	protected IPostProcessor.Descriptor.Registry<String> getPostProcessorRegistry() {
		return postProcessorRegistry;
	}

	/**
	 * Returns the merger registry.
	 * 
	 * @return mergerRegistry.
	 */
	protected IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}

	/**
	 * Returns the merge runnable.
	 * 
	 * @return mergeRunnable.
	 */
	protected IMergeRunnable getMergeRunnable() {
		return mergeRunnable;
	}
}
