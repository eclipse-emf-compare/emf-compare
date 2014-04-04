/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import com.google.common.eventbus.Subscribe;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.CollapseAllModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.DropDownMergeMenuAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.ExpandAllModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAllNonConflictingAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SaveComparisonModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SelectNextDiffAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SelectPreviousDiffAction;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IComparisonAndScopeChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.FilterActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.GroupActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilterChange;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareToolBar implements ISelectionChangedListener {

	private final GroupActionMenu groupActionMenu;

	private final FilterActionMenu filterActionMenu;

	private final List<MergeAction> mergeActions;

	private final List<MergeAllNonConflictingAction> mergeAllNonConflictingActions;

	private final EMFCompareConfiguration compareConfiguration;

	private final ToolBarManager toolbarManager;

	/**
	 * Allows us to know if the {@link #initToolbar(AbstractTreeViewer, INavigatable)} step has been executed.
	 */
	private boolean doOnce;

	/**
	 * 
	 */
	public CompareToolBar(ToolBarManager toolbarManager, StructureMergeViewerGrouper viewerGrouper,
			StructureMergeViewerFilter viewerFilter, EMFCompareConfiguration compareConfiguration) {
		this.toolbarManager = toolbarManager;
		this.compareConfiguration = compareConfiguration;
		this.doOnce = false;
		mergeActions = newArrayListWithCapacity(2);
		mergeAllNonConflictingActions = newArrayListWithCapacity(2);

		groupActionMenu = new GroupActionMenu(viewerGrouper, EMFCompareRCPUIPlugin.getDefault()
				.getDifferenceGroupProviderRegistry());

		filterActionMenu = new FilterActionMenu(viewerFilter, EMFCompareRCPUIPlugin.getDefault()
				.getDifferenceFilterRegistry());
	}

	public final void initToolbar(AbstractTreeViewer viewer, INavigatable nav) {
		if (!this.doOnce) {
			compareConfiguration.getEventBus().register(this);

			// Add extension point contributions to the structure merge viewer toolbar
			IServiceLocator workbench = PlatformUI.getWorkbench();

			IMenuService menuService = (IMenuService)workbench.getService(IMenuService.class);
			if (menuService != null) {
				menuService.populateContributionManager(toolbarManager,
						"toolbar:org.eclipse.emf.compare.structuremergeviewer.toolbar"); //$NON-NLS-1$
			}

			boolean leftEditable = compareConfiguration.isLeftEditable();
			boolean rightEditable = compareConfiguration.isRightEditable();

			final EnumSet<MergeMode> modes;
			if (rightEditable && leftEditable) {
				modes = EnumSet.of(MergeMode.RIGHT_TO_LEFT, MergeMode.LEFT_TO_RIGHT);
			} else {
				modes = EnumSet.of(MergeMode.ACCEPT, MergeMode.REJECT);
			}

			if (rightEditable || leftEditable) {
				toolbarManager.add(new DropDownMergeMenuAction(compareConfiguration, modes));
				for (MergeMode mode : modes) {
					toolbarManager.add(createMergeAction(mode, compareConfiguration, nav));
				}
				for (MergeMode mode : modes) {
					toolbarManager.add(createMergeAllNonConflictingAction(mode, compareConfiguration));
				}
			}

			toolbarManager.add(new Separator());
			toolbarManager.add(new SelectNextDiffAction(nav));
			toolbarManager.add(new SelectPreviousDiffAction(nav));
			toolbarManager.add(new Separator());
			toolbarManager.add(new ExpandAllModelAction(viewer));
			toolbarManager.add(new CollapseAllModelAction(viewer));
			toolbarManager.add(new Separator());
			toolbarManager.add(groupActionMenu);
			groupActionMenu.updateMenu(compareConfiguration.getComparisonScope(), compareConfiguration
					.getComparison());
			toolbarManager.add(filterActionMenu);
			filterActionMenu.updateMenu(compareConfiguration.getComparisonScope(), compareConfiguration
					.getComparison());
			toolbarManager.add(new Separator());
			toolbarManager.add(new SaveComparisonModelAction(compareConfiguration));

			toolbarManager.update(true);

			this.doOnce = true;
		}
	}

	private MergeAction createMergeAction(MergeMode mergeMode, EMFCompareConfiguration cc, INavigatable nav) {
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAction mergeAction = new MergeAction(cc.getEditingDomain(), mergerRegistry, mergeMode, cc
				.isLeftEditable(), cc.isRightEditable(), nav);
		mergeActions.add(mergeAction);
		return mergeAction;
	}

	private MergeAction createMergeAllNonConflictingAction(MergeMode mergeMode, IEMFCompareConfiguration cc) {
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAllNonConflictingAction mergeAction = new MergeAllNonConflictingAction(cc.getEditingDomain(), cc
				.getComparison(), mergerRegistry, mergeMode, cc.isLeftEditable(), cc.isRightEditable());
		mergeAllNonConflictingActions.add(mergeAction);
		return mergeAction;
	}

	public void dispose() {
		toolbarManager.removeAll();
		compareConfiguration.getEventBus().unregister(this);
		this.doOnce = false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		// forward event to merge actions

		for (MergeAction mergeAction : mergeActions) {
			mergeAction.selectionChanged(event);
		}
		for (MergeAllNonConflictingAction mergeAllNonConflictingAction : mergeAllNonConflictingActions) {
			mergeAllNonConflictingAction.selectionChanged(event);
		}
	}

	@Subscribe
	public void editingDomainChange(ICompareEditingDomainChange event) {
		for (MergeAction mergeAction : mergeActions) {
			mergeAction.setEditingDomain(event.getNewValue());
		}
		for (MergeAction mergeAction : mergeAllNonConflictingActions) {
			mergeAction.setEditingDomain(event.getNewValue());
		}
	}

	@Subscribe
	public void selectedDifferenceFiltersChange(IDifferenceFilterChange event) {
		final boolean enabled = any(event.getSelectedDifferenceFilters(),
				instanceOf(CascadingDifferencesFilter.class));
		for (MergeAction mergeAction : mergeActions) {
			mergeAction.setCascadingDifferencesFilterEnabled(enabled);
		}
	}

	@Subscribe
	public void comparisonAndScopeChange(final IComparisonAndScopeChange event) {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				filterActionMenu.updateMenu(event.getNewScope(), event.getNewComparison());
				groupActionMenu.updateMenu(event.getNewScope(), event.getNewComparison());
			}
		});
		for (MergeAllNonConflictingAction mergeAction : mergeAllNonConflictingActions) {
			mergeAction.setComparison(event.getNewComparison());
		}
	}

}
