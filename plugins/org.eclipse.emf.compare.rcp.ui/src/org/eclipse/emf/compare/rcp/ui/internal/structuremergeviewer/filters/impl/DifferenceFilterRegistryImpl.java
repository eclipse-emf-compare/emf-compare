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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter.Registry;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * The default implementation of the {@link Registry}.
 */
public class DifferenceFilterRegistryImpl implements Registry {

	/** A map that associates the class name to theirs {@link IDifferenceFilter}s. */
	private final Map<String, IDifferenceFilter> map;

	/**
	 * Constructs the registry.
	 */
	public DifferenceFilterRegistryImpl() {
		map = new ConcurrentHashMap<String, IDifferenceFilter>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter.Registry#getFilters(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public List<IDifferenceFilter> getFilters(IComparisonScope scope, Comparison comparison) {
		return newArrayList(filter(map.values(), isFilterActivable(scope, comparison)));
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
	static final Predicate<IDifferenceFilter> isFilterActivable(final IComparisonScope scope,
			final Comparison comparison) {
		return new Predicate<IDifferenceFilter>() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see com.google.common.base.Predicate#apply(java.lang.Object)
			 */
			public boolean apply(IDifferenceFilter d) {
				return d.isEnabled(scope, comparison);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter.Registry#add(org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter)
	 */
	public IDifferenceFilter add(IDifferenceFilter filter) {
		Preconditions.checkNotNull(filter);
		return map.put(filter.getClass().getName(), filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter.Registry#remove(java.lang.String)
	 */
	public IDifferenceFilter remove(String className) {
		return map.remove(className);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter.Registry#clear()
	 */
	public void clear() {
		map.clear();
	}
}