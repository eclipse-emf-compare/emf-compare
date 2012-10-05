package org.eclipse.emf.compare.diagram.tests;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.diff.DiagramDiffExtensionPostProcessor;
import org.eclipse.emf.compare.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.After;
import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

@SuppressWarnings("nls")
public abstract class AbstractTest {

	@Before
	public void before() {
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(
				"http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation", null,
				"org.eclipse.emf.compare.diagram.diff.DiagramDiffExtensionPostProcessor",
				new DiagramDiffExtensionPostProcessor()));
	}

	@After
	public void after() {
		EMFCompareExtensionRegistry.clearRegistry();
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
