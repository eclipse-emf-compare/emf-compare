/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.tests;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterators.all;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.StereotypedElementChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
@SuppressWarnings("nls")
public abstract class AbstractUMLTest {

	private EMFCompare emfCompare;

	private IMerger.Registry mergerRegistry;

	@BeforeClass
	public static void fillRegistries() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore",
					new EcoreResourceFactoryImpl());
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", //$NON-NLS-1$
					new UMLResourceFactoryImpl());
		}
	}

	@AfterClass
	public static void resetRegistries() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().remove("uml");
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().remove("ecore");

			EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(ComparePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(EcorePackage.eNS_URI);
		}
	}

	@Before
	public void before() {
		Builder builder = EMFCompare.builder();
		// post-processor and merger registry is not filled in runtime (org.eclipse.emf.compare.rcp not
		// loaded)
		final IPostProcessor.Descriptor.Registry<String> postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<String>();
		registerPostProcessors(postProcessorRegistry);
		builder.setPostProcessorRegistry(postProcessorRegistry);
		mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger umlMerger = new UMLMerger();
		umlMerger.setRanking(11);
		mergerRegistry.add(umlMerger);
		emfCompare = builder.build();
	}

	/**
	 * Used to register new post processors.
	 * 
	 * @param postProcessorRegistry
	 */
	protected void registerPostProcessors(
			final IPostProcessor.Descriptor.Registry<String> postProcessorRegistry) {
		postProcessorRegistry.put(UMLPostProcessor.class.getName(),
				new TestPostProcessor.TestPostProcessorDescriptor(Pattern
						.compile("http://www.eclipse.org/uml2/\\d\\.0\\.0/UML"), null,
						new UMLPostProcessor(), 20));
	}

	@After
	public void cleanup() {
		for (ResourceSet set : getInput().getSets()) {
			for (Resource res : set.getResources()) {
				res.unload();
			}
			set.getResources().clear();
		}
		getInput().getSets().clear();

	}

	protected EMFCompare getCompare() {
		return emfCompare;
	}

	protected Comparison compare(Notifier left, Notifier right) {
		return compare(left, right, null);
	}

	protected Comparison compare(Notifier left, Notifier right, Notifier origin) {
		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		return getCompare().compare(scope);
	}

	protected IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}

	protected enum TestKind {
		ADD, DELETE;
	}

	protected static int count(List<Diff> differences, Predicate<Object> p) {
		int count = 0;
		final Iterator<Diff> result = Iterators.filter(differences.iterator(), p);
		while (result.hasNext()) {
			count++;
			result.next();
		}
		return count;
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

	protected static Predicate<Diff> discriminantInstanceOf(final EClass clazz) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input instanceof UMLDiff && clazz.isInstance(((UMLDiff)input).getDiscriminant());
			}

		};
	}

	protected abstract AbstractUMLInputData getInput();

	protected void testMergeLeftToRight(Notifier left, Notifier right, Notifier origin) {
		testMergeLeftToRight(left, right, origin, false);
	}

	protected void testMergeRightToLeft(Notifier left, Notifier right, Notifier origin) {
		testMergeRightToLeft(left, right, origin, false);
	}

	protected void testMergeLeftToRight(Notifier left, Notifier right, Notifier origin, boolean pseudoAllowed) {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differencesBefore = comparisonBefore.getDifferences();
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(differencesBefore, new BasicMonitor());
		final Comparison comparisonAfter = getCompare().compare(scope);
		EList<Diff> differencesAfter = comparisonAfter.getDifferences();
		final boolean diffs;
		if (pseudoAllowed) {
			diffs = all(differencesAfter.iterator(), hasConflict(ConflictKind.PSEUDO));
		} else {
			diffs = differencesAfter.isEmpty();
		}
		assertTrue("Comparison#getDifferences() must be empty after copyAllLeftToRight", diffs);
	}

	protected void testMergeRightToLeft(Notifier left, Notifier right, Notifier origin, boolean pseudoAllowed) {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differencesBefore = comparisonBefore.getDifferences();
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(differencesBefore, new BasicMonitor());
		final Comparison comparisonAfter = getCompare().compare(scope);
		EList<Diff> differencesAfter = comparisonAfter.getDifferences();
		final boolean diffs;
		if (pseudoAllowed) {
			diffs = all(differencesAfter.iterator(), hasConflict(ConflictKind.PSEUDO));
		} else {
			diffs = differencesAfter.isEmpty();
		}
		assertTrue("Comparison#getDifferences() must be empty after copyAllRightToLeft", diffs);
	}

	protected void testIntersections(Comparison comparison) {
		for (Diff diff : comparison.getDifferences()) {
			int realRefinesSize = Iterables.size(Iterables.filter(diff.getRefines(),
					not(instanceOf(StereotypedElementChange.class))));
			assertFalse("Wrong number of refines (without StereotypedElementChange) on" + diff,
					realRefinesSize > 1);
			int stereotypedElementChangeRefines = Iterables.size(Iterables.filter(diff.getRefines(),
					instanceOf(StereotypedElementChange.class)));
			assertFalse("Wrong number of refines (of type StereotypedElementChange) on " + diff,
					stereotypedElementChangeRefines > 1);

		}
	}
}
