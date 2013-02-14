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
package org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;

/**
 * Instances of this class will be used by EMF Compare in order to provide difference filter facilities to the
 * structural differences view.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public interface IDifferenceFilter {

	/**
	 * The predicate that will filter out objects in the structural differences view.
	 * 
	 * @return The predicate that will filter out objects in the structural differences view.
	 */
	Predicate<? super EObject> getPredicate();

	/**
	 * A human-readable label for this filter. This will be displayed in the EMF Compare UI.
	 * 
	 * @return The label for this filter.
	 */
	String getLabel();

	/**
	 * Set the label for this filter. This will be displayed in the EMF Compare UI.
	 * 
	 * @param label
	 *            A human-readable label for this filter.
	 */
	void setLabel(String label);

	/**
	 * Returns the initial activation state that the filter should have.
	 * 
	 * @return The initial activation state that the filter should have.
	 */
	boolean defaultSelected();

	/**
	 * Set the initial activation state that the filter should have.
	 * 
	 * @param defaultSelected
	 *            The initial activation state that the filter should have (true if the filter should be
	 *            active by default).
	 */
	void setDefaultSelected(boolean defaultSelected);

	/**
	 * Returns the activation condition based on the scope and comparison objects.
	 * 
	 * @param scope
	 *            The scope on which the filter will be applied.
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view.
	 * @return The activation condition based on the scope and comparison objects.
	 */
	boolean isEnabled(IComparisonScope scope, Comparison comparison);

	/**
	 * A registry of {@link IDifferenceFilter}.
	 */
	interface Registry {

		/**
		 * Returns the list of {@link IDifferenceFilter} contained in the registry.
		 * 
		 * @param scope
		 *            The scope on which the filters will be applied.
		 * @param comparison
		 *            The comparison which is to be displayed in the structural view.
		 * @return The list of {@link IDifferenceFilter} contained in the registry.
		 */
		Collection<IDifferenceFilter> getFilters(IComparisonScope scope, Comparison comparison);

		/**
		 * Add to the registry the given {@link IDifferenceFilter}.
		 * 
		 * @param filter
		 *            The given {@link IDifferenceFilter}.
		 * @return The previous value associated with the class name of the given {@link IDifferenceFilter},
		 *         or null if there was no entry in the registry for the class name.
		 */
		IDifferenceFilter add(IDifferenceFilter filter);

		/**
		 * Remove from the registry the {@link IDifferenceFilter} designated by the given {@link String} .
		 * 
		 * @param className
		 *            The given {@link String} representing a {@link IDifferenceFilter}.
		 * @return The {@link IDifferenceFilter} designated by the given {@link String}.
		 */
		IDifferenceFilter remove(String className);

		/**
		 * Clear the registry.
		 */
		void clear();
	}

	/**
	 * The default implementation of the {@link Registry}.
	 */
	public class RegistryImpl implements Registry {

		/** A map that associates the class name to theirs {@link IDifferenceFilter}s. */
		private final Map<String, IDifferenceFilter> map;

		/**
		 * Constructs the registry.
		 */
		public RegistryImpl() {
			map = new ConcurrentHashMap<String, IDifferenceFilter>();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter.Registry#getFilters(org.eclipse.emf.compare.scope.IComparisonScope,
		 *      org.eclipse.emf.compare.Comparison)
		 */
		public List<IDifferenceFilter> getFilters(IComparisonScope scope, Comparison comparison) {
			Iterable<IDifferenceFilter> filters = filter(map.values(), isFilterActivable(scope, comparison));
			List<IDifferenceFilter> ret = newArrayList();
			for (IDifferenceFilter filter : filters) {
				ret.add(filter);
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
		 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter.Registry#add(org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter)
		 */
		public IDifferenceFilter add(IDifferenceFilter filter) {
			Preconditions.checkNotNull(filter);
			return map.put(filter.getClass().getName(), filter);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter.Registry#remove(java.lang.String)
		 */
		public IDifferenceFilter remove(String className) {
			return map.remove(className);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter.Registry#clear()
		 */
		public void clear() {
			map.clear();
		}
	}

}
