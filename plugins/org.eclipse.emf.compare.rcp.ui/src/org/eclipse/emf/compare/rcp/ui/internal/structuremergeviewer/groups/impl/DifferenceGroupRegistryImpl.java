/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider.Registry;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * The default implementation of the {@link Registry}.
 */
public class DifferenceGroupRegistryImpl implements Registry {

	/** A map that associates the class name to theirs {@link IDifferenceGroupProvider}s. */
	private final Map<String, IDifferenceGroupProvider> map;

	/**
	 * Constructs the registry.
	 */
	public DifferenceGroupRegistryImpl() {
		map = new ConcurrentHashMap<String, IDifferenceGroupProvider>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider.Registry#getGroupProviders(IComparisonScope,
	 *      Comparison)
	 */
	public List<IDifferenceGroupProvider> getGroupProviders(IComparisonScope scope, Comparison comparison) {
		Iterable<IDifferenceGroupProvider> providers = filter(map.values(), isGroupProviderActivable(
				scope, comparison));
		List<IDifferenceGroupProvider> ret = newArrayList();
		for (IDifferenceGroupProvider provider : providers) {
			ret.add(provider);
		}
		return ret;
	}

	/**
	 * Returns a predicate that represents the activation condition based on the scope and comparison
	 * objects.
	 * 
	 * @param scope
	 *            The scope on which the group provider will be applied.
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view.
	 * @return A predicate that represents the activation condition based on the scope and comparison
	 *         objects.
	 */
	static final Predicate<IDifferenceGroupProvider> isGroupProviderActivable(
			final IComparisonScope scope, final Comparison comparison) {
		return new Predicate<IDifferenceGroupProvider>() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see com.google.common.base.Predicate#apply(java.lang.Object)
			 */
			public boolean apply(IDifferenceGroupProvider d) {
				return d.isEnabled(scope, comparison);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider.Registry#add
	 *      (org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider)
	 */
	public IDifferenceGroupProvider add(IDifferenceGroupProvider provider) {
		Preconditions.checkNotNull(provider);
		return map.put(provider.getClass().getName(), provider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider.Registry#remove(java.lang.String)
	 *      )
	 */
	public IDifferenceGroupProvider remove(String className) {
		return map.remove(className);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider.Registry#clear()
	 */
	public void clear() {
		map.clear();
	}
}