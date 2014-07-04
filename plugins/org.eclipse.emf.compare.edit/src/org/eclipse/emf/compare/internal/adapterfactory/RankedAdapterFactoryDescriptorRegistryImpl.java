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
package org.eclipse.emf.compare.internal.adapterfactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor;

/**
 * The default implementation of the {@link Descriptor.Registry}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class RankedAdapterFactoryDescriptorRegistryImpl implements RankedAdapterFactoryDescriptor.Registry {

	/** The delegate registry. */
	private final ComposedAdapterFactory.Descriptor.Registry delegateRegistry;

	/** The store of registered adapter factory descriptors. */
	private final Multimap<Collection<?>, RankedAdapterFactoryDescriptor> emfCompareAdapterFactoryRegistry;

	/**
	 * Creates an instance.
	 * 
	 * @param delegateRegistry
	 *            <code>null</code> or a registration that should act as the delegate.
	 * @param adapterFactoryRegistryBackingMultimap
	 *            Multimap backing all {@link RankedAdapterFactoryDescriptor} registered into EMF Compare.
	 */
	public RankedAdapterFactoryDescriptorRegistryImpl(
			ComposedAdapterFactory.Descriptor.Registry delegateRegistry,
			Multimap<Collection<?>, RankedAdapterFactoryDescriptor> adapterFactoryRegistryBackingMultimap) {
		this.delegateRegistry = delegateRegistry;
		this.emfCompareAdapterFactoryRegistry = adapterFactoryRegistryBackingMultimap;
	}

	/**
	 * Returns the appropriate Descriptor for the given types. Returns uppermost the ranked adapter factory
	 * descriptor with the highest ranking. If no ranked adapter factory descriptor found, delegates to the
	 * delegate registry.
	 * 
	 * @param types
	 *            the given types.
	 * @return the appropriate Descriptor for the given types.
	 */
	public Descriptor getDescriptor(Collection<?> types) {

		ComposedAdapterFactory.Descriptor ret = getRankedDescriptor(types);

		if (ret == null) {
			ret = delegatedGetDescriptor(types);
		}
		return ret;
	}

	/**
	 * Gets the {@link ComposedAdapterFactory.Descriptor} that handles the given types.
	 * 
	 * @param types
	 *            Types that the {@link ComposedAdapterFactory.Descriptor} should handle.
	 * @return {@link ComposedAdapterFactory.Descriptor}
	 */
	private RankedAdapterFactoryDescriptor getRankedDescriptor(Collection<?> types) {
		List<String> stringTypes = new ArrayList<String>(types.size());
		for (Object key : types) {
			if (key instanceof EPackage) {
				stringTypes.add(((EPackage)key).getNsURI());
			} else if (key instanceof Package) {
				stringTypes.add(((Package)key).getName());
			} else if (key instanceof Class<?>) {
				stringTypes.add(((Class<?>)key).getName());
			}
		}
		return getHighestRankedDescriptor(stringTypes);
	}

	/**
	 * Gets the highest ranked {@link ComposedAdapterFactory.Descriptor} registered in
	 * emfCompareAdapterFactoryRegistry.
	 * 
	 * @param stringTypes
	 *            Types that the {@link ComposedAdapterFactory.Descriptor} should handle.
	 * @return {@link RankedAdapterFactoryDescriptor}
	 */
	private RankedAdapterFactoryDescriptor getHighestRankedDescriptor(List<String> stringTypes) {
		final RankedAdapterFactoryDescriptor result;

		Iterator<? extends RankedAdapterFactoryDescriptor> descriptors = emfCompareAdapterFactoryRegistry
				.get(stringTypes).iterator();

		if (descriptors.hasNext()) {
			RankedAdapterFactoryDescriptor highestRanking = descriptors.next();
			while (descriptors.hasNext()) {
				RankedAdapterFactoryDescriptor descriptor = descriptors.next();
				if (descriptor.getRanking() > highestRanking.getRanking()) {
					highestRanking = descriptor;
				}
			}
			result = highestRanking;
		} else {
			result = null;
		}
		return result;

	}

	/**
	 * This is called when local lookup fails.
	 * 
	 * @param types
	 *            the given types.
	 * @return the appropriate Descriptor for the given types.
	 */
	protected Descriptor delegatedGetDescriptor(Collection<?> types) {
		if (delegateRegistry != null) {
			return delegateRegistry.getDescriptor(types);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see {org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor.Registry.Registry#
	 *      getDescriptors()}
	 */
	public Set<RankedAdapterFactoryDescriptor> getDescriptors() {
		return ImmutableSet.copyOf(emfCompareAdapterFactoryRegistry.values());
	}

}
