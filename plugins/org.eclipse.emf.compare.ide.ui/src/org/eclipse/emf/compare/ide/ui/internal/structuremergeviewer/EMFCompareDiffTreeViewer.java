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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newLinkedHashSet;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
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
import org.eclipse.emf.compare.ide.ui.internal.util.JFaceUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.FilterActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.GroupActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareDiffTreeViewer extends DiffTreeViewer {

	public static final String REQUIRED_DIFF_COLOR = "RequiredDiffColor"; //$NON-NLS-1$

	public static final String REQUIRED_DIFF_BORDER_COLOR = "RequiredDiffBorderColor"; //$NON-NLS-1$

	public static final String UNMERGEABLE_DIFF_COLOR = "UnmergeableDiffColor"; //$NON-NLS-1$

	public static final String UNMERGEABLE_DIFF_BORDER_COLOR = "UnmergeableDiffBorderColor"; //$NON-NLS-1$

	private final Color requiredDiffColor;

	private final Color unmergeableDiffColor;

	private final CompareViewerSwitchingPane fParent;

	private ToolBarManager toolbarManager;

	private Object fRoot;

	/**
	 * The difference filter that will be applied to the structure viewer. Note that this will be initialized
	 * from {@link #createToolItems(ToolBarManager)} since that method is called from the super-constructor
	 * and we cannot init ourselves beforehand.
	 */
	private StructureMergeViewerFilter structureMergeViewerFilter;

	/**
	 * This will be used by our adapter factory in order to group together the differences located under the
	 * Comparison. Note that this will be initialized from {@link #createToolItems(ToolBarManager)} since that
	 * method is called from the super-constructor and we cannot init ourselves beforehand.
	 */
	private StructureMergeViewerGrouper structureMergeViewerGrouper;

	private MenuManager groupsMenuManager;

	private MenuManager filtersMenuManager;

	private GroupActionMenu groupActionMenu;

	private FilterActionMenu filterActionMenu;

	private EventBus eventBus;

	private Listener fEraseItemListener;

	private AdapterFactory adapterFactory;

	private List<MergeAction> mergeActions = newArrayListWithCapacity(4);

	private List<MergeAllNonConflictingAction> mergeAllNonConflictingActions = newArrayListWithCapacity(2);

	/**
	 * @param parent
	 * @param adapterFactory
	 * @param configuration
	 */
	public EMFCompareDiffTreeViewer(Composite parent, final AdapterFactory adapterFactory,
			EMFCompareConfiguration configuration) {
		super(parent, configuration);
		this.adapterFactory = adapterFactory;

		ToolBarManager tbm = CompareViewerPane.getToolBarManager(parent.getParent());
		if (tbm != null) {
			tbm.removeAll();

			tbm.add(new Separator("merge")); //$NON-NLS-1$
			tbm.add(new Separator("modes")); //$NON-NLS-1$
			tbm.add(new Separator("navigation")); //$NON-NLS-1$

			createToolItems(tbm);
			// updateActions();

			tbm.update(true);
		}

		setLabelProvider(new DelegatingStyledCellLabelProvider(
				new EMFCompareStructureMergeViewerLabelProvider(adapterFactory, this)));
		setContentProvider(new EMFCompareStructureMergeViewerContentProvider(adapterFactory));

		if (parent.getParent() instanceof CompareViewerSwitchingPane) {
			fParent = (CompareViewerSwitchingPane)parent.getParent();
		} else {
			fParent = null;
		}

		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				EMFCompareDiffTreeViewer.this.handleEraseItemEvent(event);
			}
		};
		getControl().addListener(SWT.EraseItem, fEraseItemListener);

		if (eventBus == null) {
			eventBus = new EventBus();
			eventBus.register(this);
		}

		JFaceResources.getColorRegistry().put(REQUIRED_DIFF_COLOR, new RGB(215, 255, 200));
		JFaceResources.getColorRegistry().put(REQUIRED_DIFF_BORDER_COLOR, new RGB(195, 235, 180));
		JFaceResources.getColorRegistry().put(UNMERGEABLE_DIFF_COLOR, new RGB(255, 205, 180));
		JFaceResources.getColorRegistry().put(UNMERGEABLE_DIFF_BORDER_COLOR, new RGB(235, 185, 160));

		requiredDiffColor = JFaceResources.getColorRegistry().get(REQUIRED_DIFF_COLOR);
		unmergeableDiffColor = JFaceResources.getColorRegistry().get(UNMERGEABLE_DIFF_COLOR);

		configurationChangeListener = new EMFCompareConfigurationChangeListener() {
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
				getTree().redraw();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener#mergePreviewModeChange(org.eclipse.emf.compare.internal.merge.MergeMode,
			 *      org.eclipse.emf.compare.internal.merge.MergeMode)
			 */
			@Override
			public void mergePreviewModeChange(MergeMode oldValue, MergeMode newValue) {
				getTree().redraw();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener#comparisonChange(org.eclipse.emf.compare.Comparison,
			 *      org.eclipse.emf.compare.Comparison)
			 */
			@Override
			public void comparisonChange(Comparison oldValue, Comparison newValue) {
				for (MergeAllNonConflictingAction mergeAction : mergeAllNonConflictingActions) {
					mergeAction.setComparison(newValue);
				}
			}
		};
		configuration.addChangeListener(configurationChangeListener);
		setUseHashlookup(true);
	}

	/**
	 * A predicate that checks if the given input is a TreeNode that contains a diff.
	 * 
	 * @return true, if the given input is a TreeNode that contains a diff, false otherwise.
	 */
	static Predicate<EObject> IS_DIFF_TREE_NODE = new Predicate<EObject>() {
		public boolean apply(EObject t) {
			return t instanceof TreeNode && ((TreeNode)t).getData() instanceof Diff;
		}
	};

	private final EMFCompareConfigurationChangeListener configurationChangeListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getComparator()
	 */
	@Override
	public ViewerComparator getComparator() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void recordFilterSelectionChange(IDifferenceFilterSelectionChangeEvent event) {
		final Set<IDifferenceFilter> property = getCompareConfiguration().getSelectedDifferenceFilters();

		final Set<IDifferenceFilter> selectedFilters = newLinkedHashSet(property);
		switch (event.getAction()) {
			case SELECTED:
				selectedFilters.add(event.getFilter());
				break;
			case DESELECTED:
				selectedFilters.remove(event.getFilter());
				break;
			default:
				throw new IllegalStateException();
		}

		getCompareConfiguration().setSelectedDifferenceFilters(ImmutableSet.copyOf(selectedFilters));
		getCompareConfiguration().setAggregatedViewerPredicate(event.getAggregatedPredicate());

		refreshTitle();
	}

	@Subscribe
	public void recordGroupProviderSelectionChange(IDifferenceGroupProvider differenceGroupProvider) {
		EList<Adapter> eAdapters = ((Adapter)fRoot).getTarget().eAdapters();
		IDifferenceGroupProvider oldDifferenceGroupProvider = (IDifferenceGroupProvider)EcoreUtil.getAdapter(
				eAdapters, IDifferenceGroupProvider.class);
		if (oldDifferenceGroupProvider != null) {
			eAdapters.remove(oldDifferenceGroupProvider);
		}
		eAdapters.add(differenceGroupProvider);
		getCompareConfiguration().setSelectedDifferenceGroupProvider(differenceGroupProvider);

		refreshTitle();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRoot()
	 */
	@Override
	public Object getRoot() {
		return fRoot;
	}

	public void setRoot(Object root) {
		fRoot = root;
	}

	public void createChildrenSilently(Object o) {
		if (o instanceof Tree) {
			createChildren((Widget)o);
			for (TreeItem item : ((Tree)o).getItems()) {
				createChildrenSilently(item);
			}
		} else if (o instanceof TreeItem) {
			createChildren((Widget)o);
			for (TreeItem item : ((TreeItem)o).getItems()) {
				createChildrenSilently(item);
			}
		}
	}

	@Override
	public void initialSelection() {
		super.initialSelection();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#isExpandable(java.lang.Object)
	 */
	@Override
	public boolean isExpandable(Object parent) {
		if (hasFilters()) {
			// workaround for 65762
			return getFilteredChildren(parent).length > 0;
		}
		return super.isExpandable(parent);
	}

	public void refreshAfterDiff(Object root) {
		if (getControl().isDisposed()) {
			return;
		}

		refreshTitle();
		refresh(root);
	}

	protected void refreshTitle() {
		if (fParent != null) {
			ITreeContentProvider contentProvider = (ITreeContentProvider)getContentProvider();
			int displayedDiff = getMatchCount(contentProvider, contentProvider.getElements(getRoot()));
			Comparison comparison = getCompareConfiguration().getComparison();
			int computedDiff = comparison.getDifferences().size();
			int filteredDiff = computedDiff - displayedDiff;
			fParent.setTitleArgument(computedDiff + " differences – " + filteredDiff
					+ " differences filtered from view");
		}
	}

	private int getMatchCount(ITreeContentProvider cp, Object[] elements) {
		Set<Diff> diffs = newHashSet();
		getMatchCount(cp, elements, diffs);
		return diffs.size();
	}

	/**
	 * @param cp
	 * @param children
	 * @param diffs
	 * @return
	 */
	private void getMatchCount(ITreeContentProvider cp, Object[] elements, Set<Diff> diffs) {
		for (int j = 0; j < elements.length; j++) {
			Object element = elements[j];
			if (!JFaceUtil.isFiltered(this, element, null) && element instanceof Adapter) {
				Notifier target = ((Adapter)element).getTarget();
				if (target instanceof TreeNode) {
					TreeNode treeNode = (TreeNode)target;
					EObject data = treeNode.getData();
					if (data instanceof Diff) {
						diffs.add((Diff)data);
					}
				}
			}
			Object[] children = cp.getChildren(element);
			getMatchCount(cp, children, diffs);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager toolbarManager) {

		this.toolbarManager = toolbarManager;

		super.createToolItems(toolbarManager);

		// Add extension point contributions to the structure merge viewer toolbar
		IServiceLocator workbench = PlatformUI.getWorkbench();
		IMenuService menuService = (IMenuService)workbench.getService(IMenuService.class);
		if (menuService != null) {
			menuService.populateContributionManager(toolbarManager,
					"toolbar:org.eclipse.emf.compare.structuremergeviewer.toolbar");
		}

		groupActionMenu = new GroupActionMenu(getStructureMergeViewerGrouper(), getGroupsMenuManager(),
				getDefaultGroupProvider());
		filterActionMenu = new FilterActionMenu(getStructureMergeViewerFilter(), getFiltersMenuManager());

		boolean leftEditable = getCompareConfiguration().isLeftEditable();
		boolean rightEditable = getCompareConfiguration().isRightEditable();

		final EnumSet<MergeMode> modes;
		if (rightEditable && leftEditable) {
			modes = EnumSet.of(MergeMode.RIGHT_TO_LEFT, MergeMode.LEFT_TO_RIGHT);
		} else {
			modes = EnumSet.of(MergeMode.ACCEPT, MergeMode.REJECT);
		}

		toolbarManager.add(new DropDownMergeMenuAction(getCompareConfiguration(), modes));
		for (MergeMode mode : modes) {
			toolbarManager.add(createMergeAction(mode));
		}
		for (MergeMode mode : modes) {
			toolbarManager.add(createMergeAllNonConflictingAction(mode));
		}

		toolbarManager.add(new Separator());
		toolbarManager.add(new SelectNextDiffAction(getCompareConfiguration()));
		toolbarManager.add(new SelectPreviousDiffAction(getCompareConfiguration()));
		toolbarManager.add(new Separator());
		toolbarManager.add(new ExpandAllModelAction(this));
		toolbarManager.add(new CollapseAllModelAction(this));
		toolbarManager.add(new Separator());
		toolbarManager.add(groupActionMenu);
		toolbarManager.add(filterActionMenu);
		toolbarManager.add(new Separator());
		toolbarManager.add(new SaveComparisonModelAction(getCompareConfiguration()));
	}

	private MergeAction createMergeAction(MergeMode mergeMode) {
		EMFCompareConfiguration cc = getCompareConfiguration();
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAction mergeAction = new MergeAction(cc.getEditingDomain(), mergerRegistry, mergeMode, cc
				.isLeftEditable(), cc.isRightEditable());
		addSelectionChangedListener(mergeAction);
		mergeActions.add(mergeAction);
		return mergeAction;
	}

	private MergeAction createMergeAllNonConflictingAction(MergeMode mergeMode) {
		EMFCompareConfiguration cc = getCompareConfiguration();
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAllNonConflictingAction mergeAction = new MergeAllNonConflictingAction(cc.getEditingDomain(), cc
				.getComparison(), mergerRegistry, mergeMode, cc.isLeftEditable(), cc.isRightEditable());
		addSelectionChangedListener(mergeAction);
		mergeActions.add(mergeAction);
		mergeAllNonConflictingActions.add(mergeAction);
		return mergeAction;
	}

	/**
	 * Returns the viewer filter that is to be applied on the structure viewer.
	 * <p>
	 * Note that this will be called from {@link #createToolItems(ToolBarManager)}, which is called from the
	 * super-constructor, when we have had no time to initialize the {@link #structureMergeViewerFilter}
	 * field.
	 * </p>
	 * 
	 * @return The difference filter that is to be applied on the structure viewer.
	 */
	protected StructureMergeViewerFilter getStructureMergeViewerFilter() {
		if (structureMergeViewerFilter == null) {
			if (eventBus == null) {
				eventBus = new EventBus();
				eventBus.register(this);
			}
			structureMergeViewerFilter = new StructureMergeViewerFilter(eventBus);
			structureMergeViewerFilter.install(this);
		}
		return structureMergeViewerFilter;
	}

	/**
	 * Returns the viewer grouper that is to be applied on the structure viewer.
	 * <p>
	 * Note that this will be called from {@link #createToolItems(ToolBarManager)}, which is called from the
	 * super-constructor, when we have had no time to initialize the {@link #structureMergeViewerGrouper}
	 * field.
	 * </p>
	 * 
	 * @return The viewer grouper grouper that is to be applied on the structure viewer.
	 */
	protected StructureMergeViewerGrouper getStructureMergeViewerGrouper() {
		if (structureMergeViewerGrouper == null) {
			if (eventBus == null) {
				eventBus = new EventBus();
				eventBus.register(this);
			}
			structureMergeViewerGrouper = new StructureMergeViewerGrouper(eventBus);
			structureMergeViewerGrouper.install(this);
		}
		return structureMergeViewerGrouper;
	}

	/**
	 * Returns the menu manager that is to be applied to groups on the structure viewer.
	 * 
	 * @return The menu manager that is to be applied to groups on the structure viewer.
	 */
	public MenuManager getGroupsMenuManager() {
		if (groupsMenuManager == null) {
			groupsMenuManager = new MenuManager();
		}
		return groupsMenuManager;
	}

	/**
	 * @return the groupActionMenu
	 */
	public GroupActionMenu getGroupActionMenu() {
		return groupActionMenu;
	}

	/**
	 * @param groupActionMenu
	 *            the groupActionMenu to set
	 */
	public void setGroupActionMenu(GroupActionMenu groupActionMenu) {
		this.groupActionMenu = groupActionMenu;
	}

	/**
	 * Returns the menu manager that is to be applied to filters on the structure viewer.
	 * 
	 * @return The menu manager that is to be applied to filters on the structure viewer.
	 */
	public MenuManager getFiltersMenuManager() {
		if (filtersMenuManager == null) {
			filtersMenuManager = new MenuManager();
		}
		return filtersMenuManager;
	}

	/**
	 * @return the filterActionMenu
	 */
	public FilterActionMenu getFilterActionMenu() {
		return filterActionMenu;
	}

	/**
	 * @param filterActionMenu
	 *            the filterActionMenu to set
	 */
	public void setFilterActionMenu(FilterActionMenu filterActionMenu) {
		this.filterActionMenu = filterActionMenu;
	}

	/**
	 * Returns the default group provider that is to be applied on the structure viewer.
	 * 
	 * @return The default group provider that is to be applied on the structure viewer.
	 */
	public DefaultGroupProvider getDefaultGroupProvider() {
		return new DefaultGroupProvider();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		getControl().removeListener(SWT.EraseItem, fEraseItemListener);
		for (MergeAction mergeAction : mergeActions) {
			removeSelectionChangedListener(mergeAction);
		}
		getCompareConfiguration().removeChangeListener(configurationChangeListener);
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getSortedChildren(java.lang.Object)
	 */
	@Override
	protected Object[] getSortedChildren(Object parentElementOrTreePath) {
		Object[] result = super.getSortedChildren(parentElementOrTreePath);
		if (parentElementOrTreePath instanceof Adapter) {
			Notifier target = ((Adapter)parentElementOrTreePath).getTarget();
			if (target instanceof TreeNode) {
				EObject data = ((TreeNode)target).getData();
				if (data instanceof Conflict) {
					Collections.sort(Arrays.asList(result), new Comparator<Object>() {
						public int compare(Object o1, Object o2) {
							return getValue(o1) - getValue(o2);
						}

						public int getValue(Object o) {
							int value = 0;
							if (o instanceof Adapter) {
								Notifier n = ((Adapter)o).getTarget();
								if (n instanceof TreeNode) {
									EObject d = ((TreeNode)n).getData();
									if (d instanceof Diff) {
										if (((Diff)d).getSource() == DifferenceSource.LEFT) {
											value = 1;
										} else {
											value = 2;
										}
									}
								}
							}
							return value;
						}
					});
				}
			}
		}

		return result;
	}

	/**
	 * Handle the erase item event. When select a difference in the structure merge viewer, highlight required
	 * differences with a specific color, and highlight unmergeable differences with another color.
	 * 
	 * @param event
	 *            the erase item event.
	 */
	protected void handleEraseItemEvent(Event event) {
		ISelection selection = getSelection();
		Object firstElement = ((IStructuredSelection)selection).getFirstElement();
		if (firstElement instanceof Adapter) {
			Notifier target = ((Adapter)firstElement).getTarget();
			if (target instanceof TreeNode) {
				EObject selectionData = ((TreeNode)target).getData();
				if (selectionData instanceof Diff) {
					TreeItem item = (TreeItem)event.item;
					Object dataTreeItem = item.getData();
					if (dataTreeItem instanceof Adapter) {
						Notifier targetItem = ((Adapter)dataTreeItem).getTarget();
						if (targetItem instanceof TreeNode) {
							EObject dataItem = ((TreeNode)targetItem).getData();
							MergeMode mergePreviewMode = getCompareConfiguration().getMergePreviewMode();
							boolean leftEditable = getCompareConfiguration().isLeftEditable();
							boolean rightEditable = getCompareConfiguration().isRightEditable();
							Diff selectedDiff = (Diff)selectionData;
							boolean leftToRight = mergePreviewMode.isLeftToRight(leftEditable, rightEditable);
							final Set<Diff> requires = DiffUtil.getRequires(selectedDiff, leftToRight);
							final Set<Diff> unmergeables = DiffUtil
									.getUnmergeables(selectedDiff, leftToRight);
							final GC g = event.gc;
							if (requires.contains(dataItem)) {
								paintItemBackground(g, item, requiredDiffColor);
							} else if (unmergeables.contains(dataItem)) {
								paintItemBackground(g, item, unmergeableDiffColor);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Paint the background of the given item with the given color.
	 * 
	 * @param g
	 *            the GC associated to the item.
	 * @param item
	 *            the given item.
	 * @param color
	 *            the given color.
	 */
	private void paintItemBackground(GC g, TreeItem item, Color color) {
		Rectangle itemBounds = item.getBounds();
		Tree tree = item.getParent();
		Rectangle areaBounds = tree.getClientArea();
		g.setClipping(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
		g.setBackground(color);
		g.fillRectangle(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#getCompareConfiguration()
	 */
	@Override
	public EMFCompareConfiguration getCompareConfiguration() {
		return (EMFCompareConfiguration)super.getCompareConfiguration();
	}
}
