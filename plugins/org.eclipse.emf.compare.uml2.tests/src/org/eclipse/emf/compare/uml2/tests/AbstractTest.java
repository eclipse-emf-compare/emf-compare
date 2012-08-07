package org.eclipse.emf.compare.uml2.tests;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.uml2.diff.UMLDiffExtensionPostProcessor;
import org.junit.After;
import org.junit.Before;

@SuppressWarnings("nls")
public abstract class AbstractTest {

	@Before
	public void before() {
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(
				"http://www.eclipse.org/uml2/4.0.0/UML", null,
				"org.eclipse.emf.compare.uml2.diff.UMLDiffExtensionPostProcessor",
				new UMLDiffExtensionPostProcessor()));
	}

	@After
	public void after() {
		EMFCompareExtensionRegistry.clearRegistry();
	}

	protected enum TestKind {
		ADD, DELETE;
	}

	protected static Integer count(List<Diff> differences, Predicate<Object> p) {
		int count = 0;
		final Iterator<Diff> result = Iterators.filter(differences.iterator(), p);
		while (result.hasNext()) {
			count++;
			result.next();
		}
		return Integer.valueOf(count);
	}

}
