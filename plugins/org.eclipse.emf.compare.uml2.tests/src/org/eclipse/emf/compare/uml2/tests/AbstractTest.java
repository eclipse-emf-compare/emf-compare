package org.eclipse.emf.compare.uml2.tests;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.diff.UMLDiffExtensionPostProcessor;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
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

	public static Predicate<? super Diff> onRealFeature(final EStructuralFeature feature) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final EStructuralFeature affectedFeature;
				if (input instanceof AttributeChange) {
					affectedFeature = ((AttributeChange)input).getAttribute();
				} else if (input instanceof ReferenceChange) {
					affectedFeature = ((ReferenceChange)input).getReference();
				} else {
					return false;
				}
				return feature == affectedFeature;
			}
		};
	}

	public static Predicate<? super Diff> isChangeAdd() {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof ReferenceChange) {
					return ReferenceUtil.getAsList(input.getMatch().getLeft(),
							((ReferenceChange)input).getReference()).contains(
							((ReferenceChange)input).getValue());
				} else if (input instanceof AttributeChange) {
					return ReferenceUtil.getAsList(input.getMatch().getLeft(),
							((AttributeChange)input).getAttribute()).contains(
							((AttributeChange)input).getValue());
				}
				return false;
			}
		};
	}

	protected abstract AbstractInputData getInput();

}
