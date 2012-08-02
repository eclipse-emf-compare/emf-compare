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
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * This class will be used by the EMF Compare UI to group differences together in the structural differences
 * tree viewer.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class DifferenceGrouper {
	/**
	 * The currently selected group provider. This is what will compute and give us the groups we need to
	 * display.
	 */
	private DifferenceGroupProvider provider;

	/** List of all TreeViewers on which this filter is applied. */
	private List<TreeViewer> viewers = Lists.newArrayList();

	/**
	 * This will be called internally by the ComparisonNode in order to know how it should group its
	 * differences.
	 * 
	 * @param comparison
	 *            The comparison which differences need to be grouped.
	 * @param factory
	 *            The adapter factory that is currently in use to adapt EMF Compare's Diff elements into
	 *            {@link IDiffElement}s.
	 * @return The collection of groups we could retrieve from the currently selected group provider.
	 *         {@code null} if we have no group provider set.
	 */
	public Iterable<? extends IDiffElement> getGroups(final Comparison comparison,
			final AdapterFactory factory) {
		if (provider == null) {
			return null;
		}

		final Predicate<DifferenceGroup> nonEmptyGroups = new Predicate<DifferenceGroup>() {
			public boolean apply(DifferenceGroup input) {
				return input != null && !Iterables.isEmpty(input.getDifferences());
			}
		};
		final Function<DifferenceGroup, IDiffElement> groupWrapper = new Function<DifferenceGroup, IDiffElement>() {
			public IDiffElement apply(DifferenceGroup input) {
				return new DifferenceGroupNode(comparison, factory, input);
			}
		};

		final Iterable<? extends DifferenceGroup> groups = provider.getGroups(comparison);
		final Iterable<? extends DifferenceGroup> filteredGroups = Iterables.filter(groups, nonEmptyGroups);
		return Iterables.transform(filteredGroups, groupWrapper);
	}

	/**
	 * Sets the instance that will provide the groups to be displayed in the structural differences view.
	 * 
	 * @param provider
	 *            The provider that will be use to compute the groups that are to be displayed in the UI.
	 */
	public void setProvider(DifferenceGroupProvider provider) {
		if (this.provider != provider) {
			this.provider = provider;
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
		viewers.remove(viewer);
	}
}
