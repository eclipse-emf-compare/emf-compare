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

import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.sirius.internal.SiriusDiffPostProcessor;
import org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.util.AbstractReverseActionTest;
import org.eclipse.emf.compare.postprocessor.BasicPostProcessorDescriptorImpl;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;

/**
 * This class helps to initialize and use the registers for Sirius tests.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public abstract class AbstractSiriusTest extends AbstractReverseActionTest {

	/**
	 * The PostProcessors registry.
	 */
	private IPostProcessor.Descriptor.Registry<String> postProcessorRegistry;

	/**
	 * Used to initialize the registers.
	 */
	@Override
	public void setUp() throws IOException {
		super.setUp();
		postProcessorRegistry = registerSiriusPostProcessors();
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
						Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d+\\.\\d+\\.\\d+/notation"), //$NON-NLS-1$
						Pattern.compile(""))); //$NON-NLS-1$
		return registry;
	}

	/**
	 * Returns the post processor registry.
	 * 
	 * @return postProcessorRegistry.
	 */
	protected IPostProcessor.Descriptor.Registry<String> getPostProcessorRegistry() {
		return postProcessorRegistry;
	}
}
