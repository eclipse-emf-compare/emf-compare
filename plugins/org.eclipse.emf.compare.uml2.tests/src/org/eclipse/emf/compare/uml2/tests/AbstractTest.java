package org.eclipse.emf.compare.uml2.tests;

import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.diff.UMLDiffExtensionPostProcessor;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.junit.Before;
import org.junit.BeforeClass;

@SuppressWarnings("nls")
public abstract class AbstractTest {

	private EMFCompare emfCompare;

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", //$NON-NLS-1$
				new UMLResourceFactoryImpl());
	}

	@Before
	public void before() {
		PostProcessorRegistry registry = new PostProcessorRegistry();
		registry.addPostProcessor(new PostProcessorDescriptor("http://www.eclipse.org/uml2/\\d\\.0\\.0/UML",
				null, "org.eclipse.emf.compare.uml2.diff.UMLDiffExtensionPostProcessor",
				new UMLDiffExtensionPostProcessor()));
		emfCompare = EMFCompare.builder().setPostProcessorRegistry(registry).build();
	}

	protected EMFCompare getCompare() {
		return emfCompare;
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

	protected void testMergeLeftToRight(Notifier left, Notifier right, Notifier origin) {
		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		for (Diff diff : differences) {
			diff.copyLeftToRight();
		}
		final Comparison comparisonAfter = getCompare().compare(scope);
		assertTrue("Comparison#getDifferences() must be empty after copyAllLeftToRight", comparisonAfter
				.getDifferences().isEmpty());
	}

	protected void testMergeRightToLeft(Notifier left, Notifier right, Notifier origin) {
		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		for (Diff diff : differences) {
			diff.copyRightToLeft();
		}
		final Comparison comparisonAfter = getCompare().compare(scope);
		assertTrue("Comparison#getDifferences() must be empty after copyAllRightToLeft", comparisonAfter
				.getDifferences().isEmpty());
	}
}
