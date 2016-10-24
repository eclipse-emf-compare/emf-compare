/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.registry;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.adapterfactory.context.AbstractContextTester;
import org.eclipse.emf.compare.adapterfactory.context.IContextTester;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptorRegistryImpl;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.provider.spec.ComparisonItemProviderSpec;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Test;

@SuppressWarnings({"restriction", "nls" })
public class RankedAdapterFactoryRegistryTest {

	/** registry map */
	private Multimap<Collection<?>, RankedAdapterFactoryDescriptor> registry;

	/** ranked registry */
	private RankedAdapterFactoryDescriptor.Registry rankedRegistry;

	private void setUpAdapterFactoryRegistry(Comparison comparison) {
		registry = ArrayListMultimap.create();
		HashMap<Object, Object> context = Maps.newLinkedHashMap();
		context.put(IContextTester.CTX_COMPARISON, comparison);
		rankedRegistry = new RankedAdapterFactoryDescriptorRegistryImpl(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE, registry, context);
	}

	private Comparison createNullComparison() {
		return null;
	}

	private Comparison createComparison() {
		final Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		comparison.setThreeWay(true);
		return comparison;
	}

	private IContextTester createMatchingContextTester() {
		return new AbstractContextTester() {
			public boolean apply(Map<Object, Object> context) {
				Comparison comparison = getComparison(context);
				return comparison != null && comparison.isThreeWay();
			}
		};
	}

	private IContextTester createNonMatchingContextTester() {
		return new AbstractContextTester() {
			public boolean apply(Map<Object, Object> context) {
				Comparison comparison = getComparison(context);
				return comparison == null || !comparison.isThreeWay();
			}
		};
	}

	/**
	 * Tests that the registry returns the adapter factory with the highest rank when no context tester is
	 * present.
	 */
	@Test
	public void testNoContextTesterRankConsidered() {
		final Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		setUpAdapterFactoryRegistry(createNullComparison());

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank50-NoContextTester", 50, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-NoContextTester", 70, null));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank70-NoContextTester", styledText.getString());
	}

	/**
	 * Tests that adapter factories with matching and non-matching context testers are ignored if no context
	 * is provided.
	 */
	@Test
	public void testNoContextContextTesterIgnored() {
		final Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		setUpAdapterFactoryRegistry(createNullComparison());

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank50-MatchingContextTester", 50,
				createMatchingContextTester()));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-NonMatchingContextTester", 70,
				createNonMatchingContextTester()));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank20-NoContextTester", styledText.getString());
	}

	/**
	 * Tests that the registry returns the adapter factory with a lower rank if the context tester for a
	 * higher-ranking factory does not match.
	 */
	@Test
	public void testNonMatchingContextTesterSkipped() {
		final Comparison comparison = createComparison();
		setUpAdapterFactoryRegistry(comparison);

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank50-NoContextTester", 50, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-NonMatchingContextTester", 70,
				createNonMatchingContextTester()));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank50-NoContextTester", styledText.getString());
	}

	/**
	 * Tests that the registry returns the adapter factory with the highest rank if the context tester for the
	 * highest-ranking factory does match.
	 */
	@Test
	public void testMatchingContextTesterConsidered() {
		final Comparison comparison = createComparison();
		setUpAdapterFactoryRegistry(comparison);

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank50-NoContextTester", 50, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-MatchingContextTester", 70,
				createMatchingContextTester()));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank70-MatchingContextTester", styledText.getString());
	}

	/**
	 * Tests that the registry returns the adapter factory with the highest rank and context tester if
	 * multiple factories with context testers exist.
	 */
	@Test
	public void testContextTesterRanksConsidered() {
		final Comparison comparison = createComparison();
		setUpAdapterFactoryRegistry(comparison);

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank50-MatchingContextTester", 50,
				createMatchingContextTester()));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-MatchingContextTester", 70,
				createMatchingContextTester()));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank70-MatchingContextTester", styledText.getString());
	}

	/**
	 * Tests that the registry favors higher rank over matching context tester.
	 */
	@Test
	public void testHigherRankBeatsContextTester() {
		final Comparison comparison = createComparison();
		setUpAdapterFactoryRegistry(comparison);

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank50-MatchingContextTester", 50,
				createMatchingContextTester()));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-NoContextTester", 70, null));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank70-NoContextTester", styledText.getString());
	}

	/**
	 * Tests that the registry favors a factory with context tester when factories have an equal rank.
	 */
	@Test
	public void testEqualRankContextTesterPreferred() {
		final Comparison comparison = createComparison();
		setUpAdapterFactoryRegistry(comparison);

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key, new TestAdapterFactoryDescriptor("Rank20-NoContextTester", 20, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-NoContextTester", 70, null));
		registry.put(key, new TestAdapterFactoryDescriptor("Rank70-MatchingContextTester", 70,
				createMatchingContextTester()));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);
		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("Rank70-MatchingContextTester", styledText.getString());
	}

	/**
	 * Descriptor used for test ranking.
	 */
	public class TestAdapterFactoryDescriptor implements RankedAdapterFactoryDescriptor {

		AdapterFactory adapterFactory;

		int ranking;

		IContextTester contextTester;

		public TestAdapterFactoryDescriptor(String text, int ranking, IContextTester contextTester) {
			this.adapterFactory = new TestCompareItemProviderAdapterFactorySpec(text);
			this.ranking = ranking;
			this.contextTester = contextTester;
		}

		public AdapterFactory createAdapterFactory() {
			return adapterFactory;
		}

		public int getRanking() {
			return ranking;
		}

		public IContextTester getContextTester() {
			return contextTester;
		}

		public String getId() {
			return this.adapterFactory.getClass().getName();
		}

		public String getLabel() {
			return "";
		}

		public String getDescription() {
			return null;
		}

		public boolean isOptional() {
			return false;
		}

	}

	/**
	 * Specialized CompareItemProviderAdapterFactorySpec used for testing.
	 */
	public class TestCompareItemProviderAdapterFactorySpec extends CompareItemProviderAdapterFactorySpec {

		private String text;

		public TestCompareItemProviderAdapterFactorySpec(String text) {
			this.text = text;
		}

		@Override
		public Adapter createComparisonAdapter() {
			return new TestComparisonItemProviderSpec(this, text);
		}
	}

	/**
	 * Specialized ComparisonItemProviderSpec used for testing.
	 */
	public class TestComparisonItemProviderSpec extends ComparisonItemProviderSpec {

		private String text;

		public TestComparisonItemProviderSpec(AdapterFactory adapterFactory, String text) {
			super(adapterFactory);
			this.text = text;
		}

		@Override
		public IComposedStyledString getStyledText(Object object) {
			return new ComposedStyledString(text);
		}
	}
}
