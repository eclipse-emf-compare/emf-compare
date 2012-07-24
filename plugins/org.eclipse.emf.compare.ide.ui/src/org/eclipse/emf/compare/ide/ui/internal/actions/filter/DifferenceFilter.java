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
package org.eclipse.emf.compare.ide.ui.internal.actions.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.MatchNode;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * An instance of this class intends to filter differences between elements.
 * 
 * @author <a href="mailto:maher.bouanani@obeo.fr">Bouanani Maher</a>
 */
public class DifferenceFilter extends ViewerFilter {
	/** The kinds of differences accepted by this filter. */
	private Set<DifferenceKind> differenceKind = Sets.newHashSet();

	/** List of all TreeViewers on which this filter is applied. */
	private List<TreeViewer> viewers = Lists.newArrayList();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean result = false;

		if (differenceKind.isEmpty()) {
			result = true;
		} else if (element instanceof DiffNode) {
			final Diff diff = ((DiffNode)element).getTarget();
			result = diff.getState() == DifferenceState.UNRESOLVED && differenceKind.contains(diff.getKind());
		} else if (element instanceof MatchNode) {
			final Iterator<Diff> differences = ((MatchNode)element).getTarget().getAllDifferences()
					.iterator();
			while (!result && differences.hasNext()) {
				result = differenceKind.contains(differences.next().getKind());
			}
		}

		return result;
	}

	/**
	 * Add a difference kind to those that are accepted by this filter.
	 * 
	 * @param diffKind
	 *            The new kind of difference to be accepted by this viewer. No effect if already accepted.
	 */
	public void addFilter(DifferenceKind diffKind) {
		final boolean changed = differenceKind.add(diffKind);
		if (changed) {
			refreshViewers();
		}
	}

	/**
	 * Removes a difference kind from those accepted by this filter.
	 * 
	 * @param diffKind
	 *            The difference kind that should no longer by accepted by this filter. No effect if it was
	 *            not one of the accepted kinds.
	 */
	public void removeFilter(DifferenceKind diffKind) {
		final boolean changed = differenceKind.remove(diffKind);
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
}
