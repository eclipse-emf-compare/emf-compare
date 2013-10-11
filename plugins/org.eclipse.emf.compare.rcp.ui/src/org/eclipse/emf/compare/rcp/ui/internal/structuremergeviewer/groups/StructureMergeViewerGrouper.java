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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.List;

import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupProviderChange;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * This class will be used by the EMF Compare UI to group differences together in the structural differences
 * tree viewer.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public final class StructureMergeViewerGrouper {
	/**
	 * The currently selected group provider. This is what will compute and give us the groups we need to
	 * display.
	 */
	private IDifferenceGroupProvider provider;

	/** List of all TreeViewers on which this grouper is applied. */
	private List<TreeViewer> viewers = Lists.newArrayList();

	/** The {@link EventBus} associated with this grouper. */
	private EventBus eventBus;

	/**
	 * Constructs the difference grouper.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} which will be associated with this difference grouper.
	 */
	public StructureMergeViewerGrouper(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Sets the instance that will provide the groups to be displayed in the structural differences view.
	 * 
	 * @param provider
	 *            The provider that will be use to compute the groups that are to be displayed in the UI.
	 */
	public void setProvider(IDifferenceGroupProvider provider) {
		if (this.provider != provider) {
			this.provider = provider;
			refreshViewers();
			eventBus.post(new DifferenceGroupProviderChange(provider));
		}
	}

	/**
	 * @return the provider
	 */
	public IDifferenceGroupProvider getProvider() {
		return provider;
	}

	/**
	 * Refreshes the viewers registered with this grouper. Will try and conserve the expanded tree paths when
	 * possible.
	 */
	private void refreshViewers() {
		for (TreeViewer viewer : viewers) {
			SWTUtil.safeRefresh(viewer, false);
		}
	}

	/**
	 * Install this grouper on the given viewer.
	 * <p>
	 * Note that this will also install a dispose listener on that viewer in order to remove the grouper
	 * whenever the viewer is disposed.
	 * </p>
	 * 
	 * @param viewer
	 *            The viewer on which the grouper will be installed.
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
	 * Uninstall this grouper from the given viewer.
	 * 
	 * @param viewer
	 *            The viewer from which the grouper should be removed.
	 */
	public void uninstall(TreeViewer viewer) {
		viewers.remove(viewer);
	}
}
