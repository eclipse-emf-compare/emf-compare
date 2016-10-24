/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 483798
 *******************************************************************************/
package org.eclipse.emf.compare.tests.adapterfactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
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

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"restriction", "nls" })
public class EMFCompareAdapterFactoryTest {

	@Test
	public void testEMFCompareAdapterFactory() throws IOException {

		Multimap<Collection<?>, RankedAdapterFactoryDescriptor> registry = ArrayListMultimap.create();

		final RankedAdapterFactoryDescriptorRegistryImpl rankedRegistry = new RankedAdapterFactoryDescriptorRegistryImpl(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE, registry);

		final Collection<String> key = new ArrayList<String>();
		key.add("http://www.eclipse.org/emf/compare");
		key.add("org.eclipse.emf.compare.provider.IItemStyledLabelProvider");

		registry.put(key,
				new TestEMFCompareAdapterFactoryDescriptor(new CompareItemProviderAdapterFactorySpec2(), 10));
		registry.put(key,
				new TestEMFCompareAdapterFactoryDescriptor(new CompareItemProviderAdapterFactorySpec3(), 20));

		final Collection<String> keyIItemLabelProvider = new ArrayList<String>();
		keyIItemLabelProvider.add("http://www.eclipse.org/emf/compare");
		keyIItemLabelProvider.add("org.eclipse.emf.edit.provider.IItemLabelProvider");
		registry.put(keyIItemLabelProvider,
				new TestEMFCompareAdapterFactoryDescriptor(new CompareItemProviderAdapterFactorySpec2(), 30));

		final AdapterFactory fAdapterFactory = new ComposedAdapterFactory(rankedRegistry);

		final Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		Adapter adapter = fAdapterFactory.adapt(comparison, IItemStyledLabelProvider.class);

		assertTrue(adapter instanceof ComparisonItemProviderSpec3);

		IComposedStyledString styledText = ((IItemStyledLabelProvider)adapter).getStyledText(comparison);

		assertEquals("ComparisonItemProviderSpecRanking20", styledText.getString());

	}

	/**
	 * EMFCompareAdapterFactory.Descriptor used for test ranking.
	 */
	public class TestEMFCompareAdapterFactoryDescriptor implements RankedAdapterFactoryDescriptor {

		AdapterFactory adapterFactory;

		int ranking;

		public TestEMFCompareAdapterFactoryDescriptor(AdapterFactory adapterFactory, int ranking) {
			this.adapterFactory = adapterFactory;
			this.ranking = ranking;
		}

		public AdapterFactory createAdapterFactory() {
			return adapterFactory;
		}

		public int getRanking() {
			return ranking;
		}

		public IContextTester getContextTester() {
			return null;
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
	 * Specialized CompareItemProviderAdapterFactorySpec, used for test ranking.
	 */
	public class CompareItemProviderAdapterFactorySpec2 extends CompareItemProviderAdapterFactorySpec {

		@Override
		public Adapter createComparisonAdapter() {
			return new ComparisonItemProviderSpec2(this);
		}
	}

	/**
	 * Specialized ComparisonItemProviderSpec, used for test ranking.
	 */
	public class ComparisonItemProviderSpec2 extends ComparisonItemProviderSpec {

		public ComparisonItemProviderSpec2(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public IComposedStyledString getStyledText(Object object) {
			return new ComposedStyledString("ComparisonItemProviderSpecRanking10");
		}
	}

	/**
	 * Specialized CompareItemProviderAdapterFactorySpec, used for test ranking.
	 */
	public class CompareItemProviderAdapterFactorySpec3 extends CompareItemProviderAdapterFactorySpec {

		@Override
		public Adapter createComparisonAdapter() {
			return new ComparisonItemProviderSpec3(this);
		}
	}

	/**
	 * Specialized ComparisonItemProviderSpec, used for test ranking.
	 */
	public class ComparisonItemProviderSpec3 extends ComparisonItemProviderSpec {

		public ComparisonItemProviderSpec3(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public IComposedStyledString getStyledText(Object object) {
			return new ComposedStyledString("ComparisonItemProviderSpecRanking20");
		}
	}

}
