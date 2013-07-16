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

import static com.google.common.base.Predicates.or;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent.Action;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.GroupItemProviderAdapter;
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
	/** The set of predicates known by this filter. */
	private final Set<Predicate<? super EObject>> predicates;

	/** List of all TreeViewers on which this filter is applied. */
	private final List<TreeViewer> viewers;

	/** The {@link EventBus} associated with this filter. */
	private final EventBus eventBus;

	/**
	 * Constructs the difference filter.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} which will be associated with this filter.
	 */
	public StructureMergeViewerFilter(EventBus eventBus) {
		this.eventBus = eventBus;
		this.predicates = Sets.newLinkedHashSet();
		this.viewers = Lists.newArrayList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (getPredicates().isEmpty()) {
			return true;
		}

		boolean result = true;
		final Predicate<? super EObject> predicate = or(getPredicates());

		if (element instanceof GroupItemProviderAdapter) {
			result = ((GroupItemProviderAdapter)element).hasChildren(element);
		} else if (element instanceof Adapter) {
			Notifier notifier = ((Adapter)element).getTarget();
			if (notifier instanceof EObject) {
				EObject eObject = (EObject)notifier;

				// Keep node only if it is not filtered or if it is a Match with only filtered children.
				result = keepNode(eObject, predicate);
			}
		}

		return result;
	}

	/**
	 * Keep node only if it is not filtered or if it is a Match with only filtered children.
	 * 
	 * @param eObject
	 *            the node we want to keep.
	 * @param predicate
	 *            the predicate used to keep the node or not.
	 * @return true if the node has to be keeped, false otherwise.
	 */
	private boolean keepNode(EObject eObject, final Predicate<? super EObject> predicate) {
		boolean result = !predicate.apply(eObject);
		Collection<EObject> eContents = eObject.eContents();
		if (result && !eContents.isEmpty() && eObject instanceof TreeNode) {
			EObject data = ((TreeNode)eObject).getData();
			if ((data instanceof Match || data instanceof Conflict)) {
				result = false;
				for (EObject child : eContents) {
					if (keepNode(child, predicate)) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Add the predicates of the given {@link IDifferenceFilter}s.
	 * 
	 * @param filters
	 *            The given {@link IDifferenceFilter}s.
	 */
	public void addFilters(Collection<IDifferenceFilter> filters) {
		for (IDifferenceFilter filter : filters) {
			getPredicates().remove(filter.getPredicateWhenUnselected());
			getPredicates().add(filter.getPredicateWhenSelected());
		}
		refreshViewers();
		for (IDifferenceFilter filter : filters) {
			eventBus.post(new IDifferenceFilterSelectionChangeEvent.DefaultFilterSelectionChangeEvent(filter,
					Action.ACTIVATE));
		}
	}

	/**
	 * Add the predicate of the given {@link IDifferenceFilter}.
	 * 
	 * @param filter
	 *            The given {@link IDifferenceFilter}.
	 */
	public void addFilter(IDifferenceFilter filter) {
		getPredicates().remove(filter.getPredicateWhenUnselected());
		addPredicate(filter.getPredicateWhenSelected());
		eventBus.post(new IDifferenceFilterSelectionChangeEvent.DefaultFilterSelectionChangeEvent(filter,
				Action.ACTIVATE));
	}

	/**
	 * Remove the predicate of the given {@link IDifferenceFilter}.
	 * 
	 * @param filter
	 *            The given {@link IDifferenceFilter}.
	 */
	public void removeFilter(IDifferenceFilter filter) {
		getPredicates().add(filter.getPredicateWhenUnselected());
		removePredicate(filter.getPredicateWhenSelected());
		eventBus.post(new IDifferenceFilterSelectionChangeEvent.DefaultFilterSelectionChangeEvent(filter,
				Action.DEACTIVATE));
	}

	/**
	 * Add a predicate to the set known by this filter.
	 * 
	 * @param predicate
	 *            The new predicate for differences to be accepted by this viewer. No effect if already
	 *            accepted.
	 */
	public void addPredicate(Predicate<? super EObject> predicate) {
		final boolean changed = getPredicates().add(predicate);
		if (changed) {
			refreshViewers();
		}
	}

	/**
	 * Removes a predicate from those accepted by this filter.
	 * 
	 * @param predicate
	 *            The predicate that should no longer by accepted by this filter. No effect if it was not one
	 *            of the accepted ones.
	 */
	public void removePredicate(Predicate<? super EObject> predicate) {
		final boolean changed = getPredicates().remove(predicate);
		if (changed) {
			refreshViewers();
		}
	}

	/**
	 * Refreshes the viewers registered with this filter. Will try and conserve the expanded tree paths when
	 * possible.
	 */
	private void refreshViewers() {
		for (TreeViewer viewer : viewers) {
			final TreePath[] paths = viewer.getExpandedTreePaths();
			viewer.refresh();
			viewer.setExpandedTreePaths(paths);
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
		viewer.getTree().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				uninstall(viewer);
			}
		});
		viewers.add(viewer);
	}

	/**
	 * Uninstall this filter from the given viewer.
	 * 
	 * @param viewer
	 *            The viewer from which the filter should be removed.
	 */
	public void uninstall(TreeViewer viewer) {
		viewer.removeFilter(this);
		viewers.remove(viewer);
	}

	/**
	 * Get the predicates associated with this viewer.
	 * 
	 * @return the predicates
	 */
	public Set<Predicate<? super EObject>> getPredicates() {
		return predicates;
	}

}
