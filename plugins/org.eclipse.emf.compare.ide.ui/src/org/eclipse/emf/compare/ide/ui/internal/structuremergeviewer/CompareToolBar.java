/*******************************************************************************
 * Copyright (c) 2013, 2018 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 473985
 *     Conor O'Mahony - bug 507465
 *     Martin Fleck - bug 483798
 *     Philip Langer - bug 514079
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.INavigatable;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.CollapseAllModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.DropDownMergeMenuAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.ExpandAllModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAllNonConflictingAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SaveComparisonModelAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.SelectDiffAction;
import org.eclipse.emf.compare.ide.ui.internal.util.CompareHandlerService;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IAdapterFactoryChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IComparisonAndScopeChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.FilterActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions.GroupActionMenu;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeyBinding;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.menus.IMenuService;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareToolBar implements ISelectionChangedListener {

	private static final String COMPARE_EDITOR_SCOPE_ID = "org.eclipse.compare.compareEditorScope"; //$NON-NLS-1$

	private static final String DEFAULT_ACCELERATOR_SCHEME_ID = "org.eclipse.ui.defaultAcceleratorConfiguration"; //$NON-NLS-1$

	private static final Map<String, String> COMPARE_BINDINGS;

	static {
		Map<String, String> bindings = Maps.newHashMap();
		bindings.put("org.eclipse.compare.selectNextChange", "M3+ARROW_DOWN"); //$NON-NLS-1$//$NON-NLS-2$
		bindings.put("org.eclipse.compare.selectPreviousChange", "M3+ARROW_UP"); //$NON-NLS-1$//$NON-NLS-2$
		bindings.put("org.eclipse.compare.copyLeftToRight", "M3+ARROW_RIGHT"); //$NON-NLS-1$//$NON-NLS-2$
		bindings.put("org.eclipse.compare.copyRightToLeft", "M3+ARROW_LEFT"); //$NON-NLS-1$//$NON-NLS-2$
		bindings.put("org.eclipse.compare.copyAllLeftToRight", "M2+M3+ARROW_RIGHT"); //$NON-NLS-1$//$NON-NLS-2$
		bindings.put("org.eclipse.compare.copyAllRightToLeft", "M2+M3+ARROW_LEFT"); //$NON-NLS-1$//$NON-NLS-2$
		COMPARE_BINDINGS = Collections.unmodifiableMap(bindings);
	}

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
	 * {@link IMenuCreator} that needs to be disposed
	 */
	private DropDownMergeMenuAction dropDownMergeMenuAction;

	/**
	 * The {@link SelectDiffAction SelectDiffActions} that we added and that need to be disposed.
	 */
	private List<SelectDiffAction> selectDiffActions = new ArrayList<>();

	/**
	 * 
	 */
	public CompareToolBar(ToolBarManager toolbarManager, StructureMergeViewerGrouper viewerGrouper,
			StructureMergeViewerFilter viewerFilter, EMFCompareConfiguration compareConfiguration) {
		this.toolbarManager = checkNotNull(toolbarManager);
		this.compareConfiguration = checkNotNull(compareConfiguration);
		mergeActions = newArrayListWithCapacity(2);
		mergeAllNonConflictingActions = newArrayListWithCapacity(2);

		// check configuration if group provider action menu is to be displayed
		if (compareConfiguration.getBooleanProperty(EMFCompareConfiguration.DISPLAY_GROUP_PROVIDERS, true)) {
			groupActionMenu = new GroupActionMenu(checkNotNull(viewerGrouper),
					EMFCompareRCPUIPlugin.getDefault().getDifferenceGroupProviderRegistry());
		} else {
			groupActionMenu = null;
		}

		// check configuration if filter action menu is to be displayed
		if (compareConfiguration.getBooleanProperty(EMFCompareConfiguration.DISPLAY_FILTERS, true)) {
			filterActionMenu = new FilterActionMenu(checkNotNull(viewerFilter),
					EMFCompareRCPUIPlugin.getDefault().getDifferenceFilterRegistry());
		} else {
			filterActionMenu = null;
		}
	}

	public final void initToolbar(final AbstractTreeViewer viewer, INavigatable nav,
			CompareHandlerService handlerService) {
		if (!this.doOnce) {
			compareConfiguration.getEventBus().register(this);

			// Add extension point contributions to the structure merge viewer toolbar
			if (PlatformUI.isWorkbenchRunning()) {
				final IMenuService menuService = getService(IMenuService.class);
				if (menuService != null) {
					menuService.populateContributionManager(toolbarManager,
							"toolbar:org.eclipse.emf.compare.structuremergeviewer.toolbar"); //$NON-NLS-1$
					ToolBar toolbar = toolbarManager.getControl();
					if (toolbar != null) {
						toolbar.addDisposeListener(new DisposeListener() {
							public void widgetDisposed(DisposeEvent e) {
								menuService.releaseContributions(toolbarManager);
							}
						});
					}
				}

				registerCompareBindings();
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
				// Keep a reference to be able to dispose it
				this.dropDownMergeMenuAction = new DropDownMergeMenuAction(compareConfiguration, modes);
				toolbarManager.add(dropDownMergeMenuAction);
				for (MergeMode mode : modes) {
					toolbarManager.add(createMergeAction(mode, compareConfiguration, nav));
				}
				for (MergeMode mode : modes) {
					toolbarManager.add(createMergeAllNonConflictingAction(mode, compareConfiguration));
				}
			}

			toolbarManager.add(new Separator());

			selectDiffActions.add(new SelectDiffAction(nav, INavigatable.NEXT_CHANGE));
			selectDiffActions.add(new SelectDiffAction(nav, INavigatable.PREVIOUS_CHANGE));

			if (compareConfiguration.getBooleanProperty(
					EMFCompareConfiguration.DISPLAY_SELECT_UNRESOLVED_DIFF_ACTIONS, true)) {
				selectDiffActions.add(new SelectDiffAction(nav, Navigatable.NEXT_UNRESOLVED_CHANGE));
				selectDiffActions.add(new SelectDiffAction(nav, Navigatable.PREVIOUS_UNRESOLVED_CHANGE));
			}

			for (SelectDiffAction selectDiffAction : selectDiffActions) {
				toolbarManager.add(selectDiffAction);
			}

			toolbarManager.add(new Separator());
			toolbarManager.add(new ExpandAllModelAction(viewer));
			toolbarManager.add(new CollapseAllModelAction(viewer));
			toolbarManager.add(new Separator());

			// only add/update menus if they have been created
			if (groupActionMenu != null) {
				toolbarManager.add(groupActionMenu);
				groupActionMenu.updateMenu(compareConfiguration.getComparisonScope(),
						compareConfiguration.getComparison());
			}

			if (filterActionMenu != null) {
				toolbarManager.add(filterActionMenu);
				filterActionMenu.updateMenu(compareConfiguration.getComparisonScope(),
						compareConfiguration.getComparison());
			}

			toolbarManager.add(new Separator());

			// check configuration if we want to display save button in the toolbar
			if (compareConfiguration.getBooleanProperty(EMFCompareConfiguration.DISPLAY_SAVE_ACTION, true)) {
				toolbarManager.add(new SaveComparisonModelAction(compareConfiguration));
			}

			Arrays.stream(toolbarManager.getItems())
					.filter(item -> item instanceof ActionContributionItem
							&& ((ActionContributionItem)item).getAction().getActionDefinitionId() != null)
					.map(ActionContributionItem.class::cast)
					.forEach(item -> registerBinding(item, viewer, handlerService));
			toolbarManager.update(true);

			this.doOnce = true;
		}
	}

	/**
	 * Returns the toolbar manager for this tool bar.
	 * 
	 * @return the {@link ToolBarManager}.
	 */
	public ToolBarManager getToolBarManager() {
		return toolbarManager;
	}

	private void registerCompareBindings() {
		// Dynamically register the key bindings, but only if the active scheme is the default scheme.
		final IBindingService bindingService = getService(IBindingService.class);
		if (isDefaultAcceleratorSchemeActive()) {
			// We need the command service to work with commands.
			ICommandService commandService = getService(ICommandService.class);
			List<KeyBinding> newBindings = Lists.newArrayList();
			// Iterate over our static bindings map.
			for (Map.Entry<String, String> entry : COMPARE_BINDINGS.entrySet()) {
				String id = entry.getKey();
				// If there is no active binding for this command ID...
				if (bindingService.getActiveBindingsFor(id).length == 0) {
					// Fetch the command and create a parameterized command for it.
					Command command = commandService.getCommand(id);
					ParameterizedCommand parameterizedCommand = ParameterizedCommand.generateCommand(command,
							Collections.emptyMap());
					// Parse our key sequence.
					KeySequence keySequence;
					try {
						keySequence = KeySequence.getInstance(entry.getValue());
					} catch (ParseException e) {
						// Can't happen because our static constants are definitely valid.
						continue;
					}
					// Create the binding for the default scheme and with compare editor scope.
					// Note that we create it with USER type so that it will have precedence and will
					// not result in key binding warnings in the log when the compare editor is
					// opened.
					KeyBinding binding = new KeyBinding(keySequence, parameterizedCommand,
							DEFAULT_ACCELERATOR_SCHEME_ID, COMPARE_EDITOR_SCOPE_ID, null, null, null,
							Binding.USER);
					newBindings.add(binding);
				}
			}

			// If we've generated new bindings...
			if (!newBindings.isEmpty()) {
				// Add them to the existing bindings.
				ArrayList<Binding> bindings = Lists.newArrayList(bindingService.getBindings());
				bindings.addAll(newBindings);
				try {
					// Save them all to the preference store to make them available.
					bindingService.savePreferences(bindingService.getActiveScheme(),
							bindings.toArray(new Binding[bindings.size()]));
				} catch (IOException e) {
					EMFCompareIDEUIPlugin.getDefault().log(e);
				}
			}
		}
	}

	private boolean isDefaultAcceleratorSchemeActive() {
		final IBindingService bindingService = getService(IBindingService.class);
		return bindingService.getActiveScheme() != null
				&& DEFAULT_ACCELERATOR_SCHEME_ID.equals(bindingService.getActiveScheme().getId());
	}

	@SuppressWarnings("cast")
	private <T> T getService(Class<T> serviceClass) {
		return (T)PlatformUI.getWorkbench().getService(serviceClass);
	}

	private void registerBinding(ActionContributionItem item, AbstractTreeViewer viewer,
			CompareHandlerService handlerService) {
		final IAction action = item.getAction();
		final String actionDefinitionId = action.getActionDefinitionId();
		final Action actionWrapper = new ActionWrapper(action, viewer);
		// Set the action definition ID so that it's known during registration.
		actionWrapper.setActionDefinitionId(actionDefinitionId);
		// Register it globally, because if we just register it as an action, then we'll get
		// logged errors about conflicting bindings when the bottom viewers also register this
		// same action definition ID.
		handlerService.setGlobalActionHandler(actionDefinitionId, actionWrapper);

		// If we have a binding service...
		final IBindingService bindingService = getService(IBindingService.class);
		if (bindingService != null) {
			// Determine the trigger sequences associated with this action definition ID.
			for (TriggerSequence triggerSequence : bindingService.getActiveBindingsFor(actionDefinitionId)) {
				// Look up the binding for that key sequence.
				Binding binding = bindingService.getPerfectMatch(triggerSequence);
				if (binding != null) {
					// Extract the command for that binding.
					ParameterizedCommand parameterizedCommand = binding.getParameterizedCommand();
					// If that command ID is different from this action definition ID,
					// then the key sequence will not invoke our registered action.
					// This seems like a bug in the platform's lookup mechanism.
					String id = parameterizedCommand.getId();
					if (!actionDefinitionId.equals(id)) {
						// To work around that misbehavior, register our action with that ID.
						actionWrapper.setActionDefinitionId(id);
						handlerService.setGlobalActionHandler(id, actionWrapper);
					}
				}
			}
		}
	}

	private MergeAction createMergeAction(MergeMode mergeMode, EMFCompareConfiguration cc, INavigatable nav) {
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAction mergeAction = new MergeAction(cc, mergerRegistry, mergeMode, nav);
		mergeActions.add(mergeAction);
		if (mergeMode == MergeMode.RIGHT_TO_LEFT || mergeMode == MergeMode.ACCEPT) {
			mergeAction.setActionDefinitionId("org.eclipse.compare.copyRightToLeft"); //$NON-NLS-1$
		} else {
			mergeAction.setActionDefinitionId("org.eclipse.compare.copyLeftToRight"); //$NON-NLS-1$
		}
		return mergeAction;
	}

	private MergeAction createMergeAllNonConflictingAction(MergeMode mergeMode, IEMFCompareConfiguration cc) {
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		MergeAllNonConflictingAction mergeAction = new MergeAllNonConflictingAction(cc, cc.getComparison(),
				mergerRegistry, mergeMode);
		mergeAllNonConflictingActions.add(mergeAction);
		if (mergeMode == MergeMode.RIGHT_TO_LEFT || mergeMode == MergeMode.ACCEPT) {
			mergeAction.setActionDefinitionId("org.eclipse.compare.copyAllRightToLeft"); //$NON-NLS-1$
		} else {
			mergeAction.setActionDefinitionId("org.eclipse.compare.copyAllLeftToRight"); //$NON-NLS-1$
		}
		return mergeAction;
	}

	public void dispose() {
		// We need to explicitly dispose this IMenuCreator because the items in the toolbar are never
		// disposed. They are simply removed. See
		// org.eclipse.compare.CompareViewerPane.clearToolBar(Composite).
		if (dropDownMergeMenuAction != null) {
			dropDownMergeMenuAction.dispose();
			dropDownMergeMenuAction = null;
		}
		for (SelectDiffAction selectDiffAction : selectDiffActions) {
			selectDiffAction.dispose();
		}
		selectDiffActions.clear();
		toolbarManager.removeAll();
		if (doOnce) {
			// doOnce makes sure we have been registered with the eventBus
			compareConfiguration.getEventBus().unregister(this);
		}
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
	public void adapterFactoryChange(IAdapterFactoryChange event) {
		for (MergeAction mergeAction : mergeActions) {
			mergeAction.setAdapterFactory(event.getNewValue());
		}
		for (MergeAction mergeAction : mergeAllNonConflictingActions) {
			mergeAction.setAdapterFactory(event.getNewValue());
		}
	}

	@Subscribe
	public void comparisonAndScopeChange(final IComparisonAndScopeChange event) {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				if (filterActionMenu != null) {
					filterActionMenu.updateMenu(event.getNewScope(), event.getNewComparison());
				}
				if (groupActionMenu != null) {
					groupActionMenu.updateMenu(event.getNewScope(), event.getNewComparison());
				}
			}
		});
		for (MergeAllNonConflictingAction mergeAction : mergeAllNonConflictingActions) {
			mergeAction.setComparison(event.getNewComparison());
		}
	}

	/**
	 * Enables or disables the toolbar.
	 * 
	 * @param enable
	 *            Set to <code>true</code> to enable, <code>false</code> otherwise.
	 */
	public void setEnabled(boolean enable) {
		ToolBar toolbar = toolbarManager.getControl();
		if (toolbar != null) {
			toolbar.setEnabled(enable);
		}
	}

	private final class ActionWrapper extends Action {

		private final AbstractTreeViewer viewer;

		private final IAction action;

		private ActionWrapper(IAction action, AbstractTreeViewer viewer) {
			super(action.getText());
			this.viewer = viewer;
			this.action = action;
		}

		@Override
		public void run() {
			Composite composite = viewer.getControl().getParent();
			Control focusControl = composite.getDisplay().getFocusControl();
			if (focusControl != null) {
				// Traverse the parents of the focus control.
				Composite parent = focusControl.getParent();
				while (parent != null && parent != composite) {
					// If there is a tool bar manager in the bottom viewer...
					ToolBarManager toolBarManager = CompareViewerPane.getToolBarManager(parent);
					if (toolBarManager != null) {
						// Iterate over action contribution items of that tool bar manager.
						for (IContributionItem otherContributionItem : toolBarManager.getItems()) {
							if (otherContributionItem instanceof ActionContributionItem) {
								ActionContributionItem otherActionContributionItem = (ActionContributionItem)otherContributionItem;
								IAction contributedAction = otherActionContributionItem.getAction();
								String actionDefinitionId = contributedAction.getActionDefinitionId();
								// If the contributed action's action definition ID is equal to this action
								// definition ID...
								if (action.getActionDefinitionId().equals(actionDefinitionId)) {
									// Run that action, if it is enabled, and then we're done.
									if (contributedAction.isEnabled()) {
										contributedAction.run();
									}
									return;
								}
							}
						}
					}
					parent = parent.getParent();
				}
			}
			// We didn't find a contributed item, so run the action for this viewer's tool bar, if it is
			// enabled.
			if (action.isEnabled()) {
				action.run();
			}
		}
	}

	/**
	 * Returns whether the toolbar is currently enabled.
	 * 
	 * @return whether the toolbar is currently enabled.
	 */
	public boolean isEnabled() {
		ToolBar toolbar = toolbarManager.getControl();
		return toolbar != null && toolbar.isEnabled();
	}
}
