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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.merge.CompareDiagramMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.After;
import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
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
	
	protected Comparison buildComparison(Resource left, Resource right, Resource origin) {
		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(),
				right.getResourceSet(), origin.getResourceSet());
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
	
	protected void diffsChecking(Comparison comparison, int totalDiffsNb, ExpectedStat ... expectedStats) {
		List<Diff> differences = comparison.getDifferences();
		assertEquals(totalDiffsNb, differences.size());
		for (ExpectedStat expectedStat : expectedStats) {
			Predicate<Diff> p = expectedStat.p;
			int nb = expectedStat.nb;
			int result = Collections2.filter(differences, p).size();
			
			String message = buildAssertMessage(differences, p);
			
			assertEquals(message, nb, result);
		}
		testIntersections(comparison);
	}

	private String buildAssertMessage(List<Diff> differences, Predicate<Diff> p) {
		Diff diff = Iterables.find(differences, p, null);
		String message = "";
		if (diff != null) {
			EClass clazz = getElementClass(diff);
			if (clazz != null) {
				message = clazz.getName() + " from " + diff.eClass().getName();
			}
		}
		return message;
	}
	
	protected class ExpectedStat {
		public Predicate<Diff> p;
		public int nb;
		public ExpectedStat(Predicate<Diff> p, int nb) {
			this.p = p;
			this.nb = nb;
		}
	}
	
	protected static Predicate<Diff> nameIs(final String name) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof UMLDiff) {
					EObject element = ((UMLDiff)input).getDiscriminant();
					if (element instanceof NamedElement) {
						String eltName = ((NamedElement)element).getName();
						return name.equals(eltName);
					}
				}
				return false;
			}
			
		};
	}
	
	protected static Predicate<Diff> elementNameIs(final String name) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof DiagramDiff) {
					EObject obj = ((DiagramDiff)input).getView();
					if (obj instanceof View) {
						EObject element = ((View)((DiagramDiff)input).getView()).getElement();
						if (element instanceof NamedElement) {
							String eltName = ((NamedElement)element).getName();
							return name.equals(eltName);
						}
					}
				}
				return false;
			}
			
		};
	}
	
	protected static Predicate<Diff> valueIs(final EClass clazz) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {	
				return getValue(input) != null && getValue(input).eClass() == clazz;
			}
		};
	}
	
	protected static Predicate<Diff> elementClassIs(final EClass clazz) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return getElementClass(input) == clazz;
			}
			
		};
	}
	
	protected static EObject getValue(Diff diff) {
		if (diff instanceof DiagramDiff) {
			return ((DiagramDiff)diff).getView();
		} else if (diff instanceof UMLDiff) {
			return ((UMLDiff)diff).getDiscriminant();
		} else if (diff instanceof ReferenceChange) {
			return ((ReferenceChange)diff).getValue();
		}
		return null;
	}
	
	protected static EClass getElementClass(Diff diff) {
		EObject obj = getValue(diff);
		if (obj instanceof View) {
			EObject element = ((View)obj).getElement();
			if (element != null) {
				return element.eClass();
			}
		}
		if (obj != null) {
			return obj.eClass();
		}
		return null;
	}
	
	protected abstract DiagramInputData getInput();
	
	protected void testIntersections(Comparison comparison) {
		assertFalse(Iterables.any(comparison.getDifferences(), new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input.getRefines().size() > 1;
			}
		}));
	}

}
