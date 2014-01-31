/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.indexOf;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * The default implementation of the {@link IDifferenceGroupProvider.Descriptor.Registry}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 */
public class DifferenceGroupRegistryImpl implements IDifferenceGroupProvider.Descriptor.Registry {

	/** A map that associates the class name to theirs {@link IDifferenceGroupProvider.Descriptor}s. */
	private final Map<String, IDifferenceGroupProvider.Descriptor> map;

	/**
	 * Constructs the registry.
	 */
	public DifferenceGroupRegistryImpl() {
		map = new ConcurrentHashMap<String, IDifferenceGroupProvider.Descriptor>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#getGroupProviders(IComparisonScope,
	 *      Comparison)
	 */
	public List<IDifferenceGroupProvider> getGroupProviders(IComparisonScope scope, Comparison comparison) {
		List<IDifferenceGroupProvider> providers = newArrayList();
		for (IDifferenceGroupProvider.Descriptor desc : map.values()) {
			IDifferenceGroupProvider groupProvider = desc.createGroupProvider();
			if (isGroupProviderActivable(groupProvider, scope, comparison)) {
				providers.add(groupProvider);
			}
		}
		int indexOfDefault = indexOf(providers, instanceOf(DefaultGroupProvider.class));
		if (indexOfDefault >= 0) {
			IDifferenceGroupProvider defaultGroupProvider = providers.remove(indexOfDefault);
			providers.add(0, defaultGroupProvider);
		}
		return ImmutableList.copyOf(providers);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#getDefaultGroupProvider(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public IDifferenceGroupProvider getDefaultGroupProvider(IComparisonScope scope, Comparison comparison) {
		IDifferenceGroupProvider selectedGroupProvider = null;
		for (IDifferenceGroupProvider dgp : getGroupProviders(scope, comparison)) {
			if (dgp.defaultSelected()) {
				if (selectedGroupProvider == null || !(dgp instanceof DefaultGroupProvider)) {
					selectedGroupProvider = dgp;
				}
			}
		}
		return selectedGroupProvider;
	}

	/**
	 * Checks if the given IDifferenceGroupProvider is activable based on the scope and comparison objects.
	 * 
	 * @param dgp
	 *            the given IDifferenceGroupProvider.
	 * @param scope
	 *            The scope on which the group provider will be applied.
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view.
	 * @return true, if it is activable, false otherwise.
	 */
	static final boolean isGroupProviderActivable(final IDifferenceGroupProvider dgp,
			final IComparisonScope scope, final Comparison comparison) {
		return dgp.isEnabled(scope, comparison);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#add
	 *      (org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor)
	 */
	public IDifferenceGroupProvider.Descriptor add(IDifferenceGroupProvider.Descriptor providerDescriptor,
			String className) {
		Preconditions.checkNotNull(providerDescriptor);
		return map.put(className, providerDescriptor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#remove(java.lang.String)
	 */
	public IDifferenceGroupProvider.Descriptor remove(String className) {
		return map.remove(className);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#clear()
	 */
	public void clear() {
		map.clear();
	}
}
