package org.eclipse.emf.compare.diagram.ecoretools.tests;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.diff.DiagramDiffExtensionPostProcessor;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.After;
import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

@SuppressWarnings("nls")
public abstract class AbstractTest {

	private PostProcessorRegistry postProcessorRegistry;

	@Before
	public void before() {
		postProcessorRegistry = new PostProcessorRegistry();
		postProcessorRegistry.addPostProcessor(new PostProcessorDescriptor(
				"http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation", null,
				"org.eclipse.emf.compare.diagram.diff.DiagramDiffExtensionPostProcessor",
				new DiagramDiffExtensionPostProcessor()));
	}

	/**
	 * @return the postProcessorRegistry
	 */
	protected PostProcessorRegistry getPostProcessorRegistry() {
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
