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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ForwardingListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor;

/**
 * The default implementation of the {@link Descriptor.Registry}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class RankedAdapterFactoryDescriptorRegistryImpl extends ForwardingListMultimap<Collection<?>, ComposedAdapterFactory.Descriptor> implements RankedAdapterFactoryDescriptor.Registry {

	/** The delegate registry. */
	private final ComposedAdapterFactory.Descriptor.Registry delegateRegistry;

	/** The store of registered adapter factory descriptors. */
	private final ListMultimap<Collection<?>, ComposedAdapterFactory.Descriptor> map;

	/**
	 * Creates an instance.
	 * 
	 * @param delegateRegistry
	 *            <code>null</code> or a registration that should act as the delegate.
	 */
	public RankedAdapterFactoryDescriptorRegistryImpl(
			ComposedAdapterFactory.Descriptor.Registry delegateRegistry) {
		this.delegateRegistry = delegateRegistry;
		map = Multimaps.synchronizedListMultimap(ArrayListMultimap
				.<Collection<?>, ComposedAdapterFactory.Descriptor> create());
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
		ComposedAdapterFactory.Descriptor ret = null;

		List<Object> stringTypes = new ArrayList<Object>(types.size());
		for (Object key : types) {
			if (key instanceof EPackage) {
				stringTypes.add(((EPackage)key).getNsURI());
			} else if (key instanceof Package) {
				stringTypes.add(((Package)key).getName());
			} else if (key instanceof Class<?>) {
				stringTypes.add(((Class<?>)key).getName());
			}
		}
		Iterator<ComposedAdapterFactory.Descriptor> descriptors = get(stringTypes).iterator();

		if (descriptors.hasNext()) {
			ComposedAdapterFactory.Descriptor highestRanking = descriptors.next();
			while (descriptors.hasNext()) {
				ComposedAdapterFactory.Descriptor descriptor = descriptors.next();
				if (descriptor instanceof RankedAdapterFactoryDescriptor
						&& highestRanking instanceof RankedAdapterFactoryDescriptor) {
					if (((RankedAdapterFactoryDescriptor)descriptor).getRanking() > ((RankedAdapterFactoryDescriptor)highestRanking)
							.getRanking()) {
						highestRanking = descriptor;
					}
				} else if (descriptor instanceof RankedAdapterFactoryDescriptor) {
					// previous highestRanking was not ranked, so it is overriden by any
					// RankedAdapterFactoryDescriptor.
					highestRanking = descriptor;
				}
			}
			ret = highestRanking;
		}

		if (ret != null) {
			put(types, ret);
		} else {
			ret = delegatedGetDescriptor(types);
		}

		return ret;
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
	 * @see com.google.common.collect.ForwardingListMultimap#delegate()
	 */
	@Override
	protected ListMultimap<Collection<?>, ComposedAdapterFactory.Descriptor> delegate() {
		return map;
	}

}
