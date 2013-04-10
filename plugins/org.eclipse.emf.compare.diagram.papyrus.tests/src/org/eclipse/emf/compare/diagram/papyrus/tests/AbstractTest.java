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
package org.eclipse.emf.compare.diagram.papyrus.tests;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.internal.merge.CompareDiagramMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
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
	
	private IMerger.Registry mergerRegistry;

	@Before
	public void before() {
		postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<String>();
		postProcessorRegistry.put(UMLPostProcessor.class.getName(), new TestPostProcessor.TestPostProcessorDescriptor(
				Pattern.compile("http://www.eclipse.org/uml2/\\d.0.0/UML"), null, new UMLPostProcessor(), 20));
		postProcessorRegistry.put(CompareDiagramPostProcessor.class.getName(), new TestPostProcessor.TestPostProcessorDescriptor(
				Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation"), null, new CompareDiagramPostProcessor(), 30));
		emfCompare = EMFCompare.builder().setPostProcessorRegistry(postProcessorRegistry).build();
		mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger umlMerger = new UMLMerger();
		umlMerger.setRanking(11);
		final IMerger diagramMerger = new CompareDiagramMerger();
		diagramMerger.setRanking(11);
		mergerRegistry.add(umlMerger);
		mergerRegistry.add(diagramMerger);
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
	
	protected IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}
	
	protected Comparison buildComparison(Resource left, Resource right) {
		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(),
				right.getResourceSet());
		Comparison comparison = EMFCompare.builder()
				.setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		return comparison;
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
