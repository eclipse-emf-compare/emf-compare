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
package org.eclipse.emf.compare.diagram.ecoretools.tests;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.After;
import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

@SuppressWarnings("nls")
public abstract class AbstractTest {

	private EMFCompare emfCompare;
	
	private PostProcessorDescriptorRegistryImpl<String> postProcessorRegistry;

	@Before
	public void before() {
		postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<String>();
		postProcessorRegistry.put(CompareDiagramPostProcessor.class.getName(), new TestPostProcessor.TestPostProcessorDescriptor(
				Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation"), null, new CompareDiagramPostProcessor(), 30));
		emfCompare = EMFCompare.builder().setPostProcessorRegistry(postProcessorRegistry).build();
	}

	protected EMFCompare getCompare() {
		return emfCompare;
	}
	
	/**
	 * @return the postProcessorRegistry
	 */
	protected IPostProcessor.Descriptor.Registry<?> getPostProcessorRegistry() {
		return postProcessorRegistry;
	}
	
	@After
	public void after() {
		if (getInput() != null && getInput().getSets() != null) {
			for (ResourceSet set : getInput().getSets()) {
				cleanup(set);
			}
		}
	}

	private void cleanup(ResourceSet resourceSet) {
		for (Resource res : resourceSet.getResources()) {
			res.unload();
		}
		resourceSet.getResources().clear();
	}

	protected enum TestKind {
		ADD, DELETE;
	}

	protected static Integer count(List<Diff> differences, Predicate<? super Diff> p) {
		int count = 0;
		final Iterator<Diff> result = Iterators.filter(differences.iterator(), p);
		while (result.hasNext()) {
			count++;
			result.next();
		}
		return Integer.valueOf(count);
	}
	
	protected abstract DiagramInputData getInput();

}
