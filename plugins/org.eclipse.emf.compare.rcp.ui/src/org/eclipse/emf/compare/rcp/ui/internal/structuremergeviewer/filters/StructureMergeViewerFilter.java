/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.GroupItemProviderAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * This will be used by the structure viewer to filter out its list of differences according to a number of
 * provided predicates.
 * <p>
 * <b>Note</b> that this filter acts as an "OR" predicate between all provided ones, and that filters are
 * "exclude" filters. Basically, that means if the user selects two filters, any difference that applies for
 * any of these two filters will be <i>hidden</i> from the view, contrarily to "classic" {@link ViewerFilter}
 * that act as "AND" predicates for "include" filters, forcing any displayed element to meet the criterion of
 * all provided filters.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public class StructureMergeViewerFilter extends ViewerFilter {

	public static final Predicate<? super EObject> DEFAULT_PREDICATE = alwaysFalse();

	/** The set of predicates known by this filter. */
	private final Set<Predicate<? super EObject>> predicates;

	private final Set<IDifferenceFilter> selectedDifferenceFilters;

	private final Set<IDifferenceFilter> unselectedDifferenceFilters;

	/** List of all TreeViewers on which this filter is applied. */
	private final List<TreeViewer> viewers;

	/** The {@link EventBus} associated with this filter. */
	private final EventBus eventBus;

	private final Predicate<? super EObject> viewerPredicate = new Predicate<EObject>() {
		public boolean apply(EObject eObject) {
			if (aggregatedPredicate.apply(eObject)) {
				Collection<EObject> eContents = eObject.eContents();
				if (!eContents.isEmpty() && eObject instanceof TreeNode) {
					EObject data = ((TreeNode)eObject).getData();
					if (data instanceof Match || data instanceof Conflict || data instanceof MatchResource) {
						return any(eContents, viewerPredicate);
					}
				}
				return true;
			} else {
				return false;
			}
		}
	};

	private Predicate<? super EObject> aggregatedPredicate;

	/**
	 * Constructs the difference filter.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} which will be associated with this filter.
	 */
	public StructureMergeViewerFilter(EventBus eventBus) {
		this.eventBus = eventBus;
		this.predicates = newLinkedHashSet();
		this.selectedDifferenceFilters = newLinkedHashSet();
		this.unselectedDifferenceFilters = newLinkedHashSet();
		this.viewers = newArrayList();
		this.aggregatedPredicate = DEFAULT_PREDICATE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (predicates.isEmpty()) {
			return true;
		}

		final boolean result;
		if (element instanceof GroupItemProviderAdapter) {
			Collection<?> children = ((GroupItemProviderAdapter)element).getChildren(element);
			result = any(Iterables.filter(children, EObject.class), viewerPredicate);
		} else if (element instanceof Adapter) {
			Notifier notifier = ((Adapter)element).getTarget();
			if (notifier instanceof EObject) {
				EObject eObject = (EObject)notifier;
				result = viewerPredicate.apply(eObject);
			} else {
				result = true;
			}
		} else {
			result = true;
		}

		return result;
	}

	/**
	 * Add the predicate of the given {@link IDifferenceFilter}.
	 * 
	 * @param filter
	 *            The given {@link IDifferenceFilter}.
	 */
	public void addFilter(IDifferenceFilter filter) {
		boolean changed = predicates.remove(filter.getPredicateWhenUnselected());
		changed |= predicates.add(filter.getPredicateWhenSelected());

		changed |= selectedDifferenceFilters.add(filter);
		changed |= unselectedDifferenceFilters.remove(filter);

		if (changed) {
			aggregatedPredicate = computeAggregatedPredicate();
			refreshViewers();
			eventBus.post(new DifferenceFilterChange(aggregatedPredicate, selectedDifferenceFilters,
					unselectedDifferenceFilters));
		}
	}

	/**
	 * @return
	 */
	private Predicate<? super EObject> computeAggregatedPredicate() {
		return not(or(predicates));
	}

	/**
	 * Remove the predicate of the given {@link IDifferenceFilter}.
	 * 
	 * @param filter
	 *            The given {@link IDifferenceFilter}.
	 */
	public void removeFilter(IDifferenceFilter filter) {
		boolean changed = predicates.add(filter.getPredicateWhenUnselected());
		changed |= predicates.remove(filter.getPredicateWhenSelected());

		changed |= unselectedDifferenceFilters.add(filter);
		changed |= selectedDifferenceFilters.remove(filter);

		if (changed) {
			aggregatedPredicate = computeAggregatedPredicate();
			refreshViewers();
			eventBus.post(new DifferenceFilterChange(aggregatedPredicate, selectedDifferenceFilters,
					unselectedDifferenceFilters));
		}
	}

	public void init(Collection<IDifferenceFilter> selectedFilters,
			Collection<IDifferenceFilter> unselectedFilters) {
		boolean changed = false;

		if (!predicates.isEmpty()) {
			predicates.clear();
			changed = true;
		}

		for (IDifferenceFilter filter : selectedFilters) {
			changed |= predicates.add(filter.getPredicateWhenSelected());
			changed |= selectedDifferenceFilters.add(filter);
		}
		for (IDifferenceFilter filter : unselectedFilters) {
			changed |= predicates.add(filter.getPredicateWhenUnselected());
			changed |= unselectedDifferenceFilters.add(filter);
		}

		if (changed) {
			aggregatedPredicate = computeAggregatedPredicate();
			refreshViewers();
			eventBus.post(new DifferenceFilterChange(aggregatedPredicate, selectedDifferenceFilters,
					unselectedDifferenceFilters));
		}
	}

	/**
	 * @return the selectedDifferenceFilters
	 */
	public Set<IDifferenceFilter> getSelectedDifferenceFilters() {
		return selectedDifferenceFilters;
	}

	/**
	 * @return the aggregatedPredicate
	 */
	public Predicate<? super EObject> getAggregatedPredicate() {
		return aggregatedPredicate;
	}

	/**
	 * Refreshes the viewers registered with this filter. Will try and conserve the expanded tree paths when
	 * possible.
	 */
	private void refreshViewers() {
		for (final TreeViewer viewer : viewers) {
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					final TreePath[] paths = viewer.getExpandedTreePaths();
					viewer.refresh();
					viewer.setExpandedTreePaths(paths);
				}
			});
		}
	}

	/**
	 * Install this filter on the given viewer.
	 * <p>
	 * Note that this will also install a dispose listener on that viewer in order to remove the filter
	 * whenever the viewer is disposed.
	 * </p>
	 * 
	 * @param viewer
	 *            the viewer on which the filter will be installed
	 */
	public void install(final TreeViewer viewer) {
		viewer.addFilter(this);
		viewers.add(viewer);
		viewer.getTree().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				uninstall(viewer);
			}
		});
	}

	/**
	 * Uninstall this filter from the given viewer.
	 * 
	 * @param viewer
	 *            The viewer from which the filter should be removed.
	 */
	public void uninstall(TreeViewer viewer) {
		viewers.remove(viewer);
		viewer.removeFilter(this);
	}

}
