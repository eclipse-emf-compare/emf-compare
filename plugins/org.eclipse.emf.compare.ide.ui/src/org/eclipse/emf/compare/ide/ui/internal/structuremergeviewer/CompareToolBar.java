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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.CollapseAllModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.DropDownMergeMenuAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.ExpandAllModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAllNonConflictingAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SaveComparisonModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SelectNextDiffAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SelectPreviousDiffAction;
import org.eclipse.emf.compare.ide.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.FilterActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.GroupActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareToolBar {

	private final GroupActionMenu groupActionMenu;

	private final FilterActionMenu filterActionMenu;

	private final List<MergeAction> mergeActions = newArrayListWithCapacity(2);

	private final List<MergeAllNonConflictingAction> mergeAllNonConflictingActions = newArrayListWithCapacity(2);

	private final CompareConfigurationListener configurationChangeListener;

	private final EMFCompareConfiguration compareConfiguration;

	private final AbstractTreeViewer viewer;

	/**
	 * 
	 */
	public CompareToolBar(AbstractTreeViewer viewer, ToolBarManager toolbarManager,
			StructureMergeViewerGrouper viewerGrouper, StructureMergeViewerFilter viewerFilter,
			EMFCompareConfiguration compareConfiguration) {
		this.viewer = viewer;
		this.compareConfiguration = compareConfiguration;
		// Add extension point contributions to the structure merge viewer toolbar
		IServiceLocator workbench = PlatformUI.getWorkbench();
		IMenuService menuService = (IMenuService)workbench.getService(IMenuService.class);
		if (menuService != null) {
			menuService.populateContributionManager(toolbarManager,
					"toolbar:org.eclipse.emf.compare.structuremergeviewer.toolbar");
		}

		configurationChangeListener = new CompareConfigurationListener();
		compareConfiguration.addChangeListener(configurationChangeListener);

		groupActionMenu = new GroupActionMenu(viewerGrouper, new DefaultGroupProvider());
		filterActionMenu = new FilterActionMenu(viewerFilter);

		boolean leftEditable = compareConfiguration.isLeftEditable();
		boolean rightEditable = compareConfiguration.isRightEditable();

		final EnumSet<MergeMode> modes;
		if (rightEditable && leftEditable) {
			modes = EnumSet.of(MergeMode.RIGHT_TO_LEFT, MergeMode.LEFT_TO_RIGHT);
		} else {
			modes = EnumSet.of(MergeMode.ACCEPT, MergeMode.REJECT);
		}

		toolbarManager.add(new DropDownMergeMenuAction(compareConfiguration, modes));
		for (MergeMode mode : modes) {
			toolbarManager.add(createMergeAction(mode));
		}
		for (MergeMode mode : modes) {
			toolbarManager.add(createMergeAllNonConflictingAction(mode));
		}

		toolbarManager.add(new Separator());
		toolbarManager.add(new SelectNextDiffAction(compareConfiguration));
		toolbarManager.add(new SelectPreviousDiffAction(compareConfiguration));
		toolbarManager.add(new Separator());
		toolbarManager.add(new ExpandAllModelAction(viewer));
		toolbarManager.add(new CollapseAllModelAction(viewer));
		toolbarManager.add(new Separator());
		toolbarManager.add(groupActionMenu);
		toolbarManager.add(filterActionMenu);
		toolbarManager.add(new Separator());
		toolbarManager.add(new SaveComparisonModelAction(compareConfiguration));

		toolbarManager.update(true);
	}

	private MergeAction createMergeAction(MergeMode mergeMode) {
		EMFCompareConfiguration cc = compareConfiguration;
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAction mergeAction = new MergeAction(cc.getEditingDomain(), mergerRegistry, mergeMode, cc
				.isLeftEditable(), cc.isRightEditable());
		viewer.addSelectionChangedListener(mergeAction);
		mergeActions.add(mergeAction);
		return mergeAction;
	}

	private MergeAction createMergeAllNonConflictingAction(MergeMode mergeMode) {
		EMFCompareConfiguration cc = compareConfiguration;
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAllNonConflictingAction mergeAction = new MergeAllNonConflictingAction(cc.getEditingDomain(), cc
				.getComparison(), mergerRegistry, mergeMode, cc.isLeftEditable(), cc.isRightEditable());
		viewer.addSelectionChangedListener(mergeAction);
		mergeAllNonConflictingActions.add(mergeAction);
		return mergeAction;
	}

	public void dispose() {
		compareConfiguration.addChangeListener(configurationChangeListener);
		for (MergeAction mergeAction : mergeActions) {
			viewer.removeSelectionChangedListener(mergeAction);
		}
		for (MergeAllNonConflictingAction mergeAllNonConflictingAction : mergeAllNonConflictingActions) {
			viewer.removeSelectionChangedListener(mergeAllNonConflictingAction);
		}
	}

	private class CompareConfigurationListener extends EMFCompareConfigurationChangeListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener#editingDomainChange(org.eclipse.emf.compare.domain.ICompareEditingDomain,
		 *      org.eclipse.emf.compare.domain.ICompareEditingDomain)
		 */
		@Override
		public void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
			for (MergeAction mergeAction : mergeActions) {
				mergeAction.setEditingDomain(newValue);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener#selectedDifferenceFiltersChange(java.util.Set,
		 *      java.util.Set)
		 */
		@Override
		public void selectedDifferenceFiltersChange(Set<IDifferenceFilter> oldValue,
				Set<IDifferenceFilter> newValue) {
			final boolean enabled = any(newValue, instanceOf(CascadingDifferencesFilter.class));
			for (MergeAction mergeAction : mergeActions) {
				mergeAction.setCascadingDifferencesFilterEnabled(enabled);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener#comparisonChange(org.eclipse.emf.compare.Comparison,
		 *      org.eclipse.emf.compare.Comparison)
		 */
		@Override
		public void comparisonChange(Comparison oldValue, final Comparison newValue) {
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					groupActionMenu.createActions(compareConfiguration.getComparisonScope(), newValue);
					filterActionMenu.createActions(compareConfiguration.getComparisonScope(), newValue);
				}
			});
			for (MergeAllNonConflictingAction mergeAction : mergeAllNonConflictingActions) {
				mergeAction.setComparison(newValue);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener#comparisonScopeChange(org.eclipse.emf.compare.scope.IComparisonScope,
		 *      org.eclipse.emf.compare.scope.IComparisonScope)
		 */
		@Override
		public void comparisonScopeChange(IComparisonScope oldValue, final IComparisonScope newValue) {
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					groupActionMenu.createActions(newValue, compareConfiguration.getComparison());
					filterActionMenu.createActions(newValue, compareConfiguration.getComparison());
				}
			});
		}
	}
}
