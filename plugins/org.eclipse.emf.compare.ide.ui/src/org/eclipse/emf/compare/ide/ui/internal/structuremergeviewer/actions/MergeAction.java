/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - bug 462237
 *     Martin Fleck - bug 483798
 *     Philip Langer - bug 521948
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.internal.merge.MergeMode.ACCEPT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.LEFT_TO_RIGHT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.REJECT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.RIGHT_TO_LEFT;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Streams;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.command.impl.AbstractCopyCommand;
import org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.text.EMFCompareTextMergeViewer.EditCommand;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract Action that manages a merge of a difference in case of both sides of the comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergeAction extends BaseSelectionListenerAction {

	private static final Predicate<Diff> IS_IN_TERMINAL_STATE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return AbstractMerger.isInTerminalState(diff);
		}
	};

	private static final Predicate<Diff> IS_NOT_IN_TERMINAL_STATE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return !AbstractMerger.isInTerminalState(diff);
		}
	};

	protected static final Function<? super Adapter, ? extends Notifier> ADAPTER__TARGET = new Function<Adapter, Notifier>() {
		public Notifier apply(Adapter adapter) {
			return adapter.getTarget();
		}
	};

	protected final Registry mergerRegistry;

	protected ICompareEditingDomain editingDomain;

	private final List<Diff> selectedDifferences;

	private final INavigatable navigatable;

	/**
	 * The merge mode used for the comparison.
	 */
	private final MergeMode selectedMode;

	/**
	 * The adapter factory for the comparison.
	 */
	private AdapterFactory adapterFactory;

	private IDiffRelationshipComputer diffRelationshipComputer;

	private IEMFCompareConfiguration compareConfiguration;

	private boolean isMirrored;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public MergeAction(IEMFCompareConfiguration compareConfiguration, IMerger.Registry mergerRegistry,
			MergeMode mode, INavigatable navigatable) {
		super(""); //$NON-NLS-1$

		this.compareConfiguration = compareConfiguration;
		adapterFactory = compareConfiguration.getAdapterFactory();
		diffRelationshipComputer = compareConfiguration.getDiffRelationshipComputer();
		boolean isLeftEditable = compareConfiguration.isLeftEditable();
		boolean isRightEditable = compareConfiguration.isRightEditable();

		this.navigatable = navigatable;
		Preconditions.checkNotNull(mode);
		// at least should be editable
		Preconditions.checkState(isLeftEditable || isRightEditable);
		// if left and right editable, the only accepted mode are LtR or RtL
		if (isLeftEditable && isRightEditable) {
			Preconditions.checkState(mode == LEFT_TO_RIGHT || mode == RIGHT_TO_LEFT);
		}
		// if mode is accept or reject, left and right can't be both read only (no action should be created in
		// this case) and can't be both editable.
		if (isLeftEditable != isRightEditable) {
			Preconditions.checkState(mode == ACCEPT || mode == REJECT);
		}

		this.editingDomain = compareConfiguration.getEditingDomain();
		this.mergerRegistry = mergerRegistry;
		this.selectedDifferences = newArrayList();
		this.selectedMode = mode;

		initToolTipAndImage(mode);
	}

	public MergeAction(IEMFCompareConfiguration compareConfiguration, IMerger.Registry mergerRegistry,
			MergeMode mode, INavigatable navigatable, IStructuredSelection selection) {
		this(compareConfiguration, mergerRegistry, mode, navigatable);
		setEnabled(updateSelection(selection));
	}

	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean leftEditable, boolean rightEditable,
			IDiffRelationshipComputer relationshipComputer) {
		return new MergeRunnableImpl(leftEditable, rightEditable, mode, relationshipComputer);
	}

	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setText(EMFCompareIDEUIMessages.getString("merged.to.right.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/merge_to_right.gif")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setText(EMFCompareIDEUIMessages.getString("merged.to.left.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/merge_to_left.gif")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setText(EMFCompareIDEUIMessages.getString("accept.change.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.change.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/accept_change.gif")); //$NON-NLS-1$
				break;
			case REJECT:
				setText(EMFCompareIDEUIMessages.getString("reject.change.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.change.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/reject_change.gif")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	/**
	 * This method is used to created contextual tooltips.
	 */
	protected void contextualizeTooltip() {
		if (this.selectedDifferences.size() > 1) {
			// multiple selection
			setMultipleTooltip(this.selectedMode);
		} else if (this.selectedDifferences.isEmpty()) {
			// no selection
			initToolTipAndImage(this.selectedMode);
		} else {
			Diff diff = this.selectedDifferences.get(0);
			Object adapter = adapterFactory.adapt(diff, ITooltipLabelProvider.class);
			if (adapter instanceof ITooltipLabelProvider) {
				String tooltip = ((ITooltipLabelProvider)adapter).getTooltip(this.selectedMode);
				setToolTipText(tooltip);
			} else {
				initToolTipAndImage(this.selectedMode);
			}
		}
	}

	/**
	 * Set the tooltips for multiple selection.
	 *
	 * @param mode
	 *            The comparison mode
	 */
	private void setMultipleTooltip(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.multiple.to.right.tooltip")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.multiple.to.left.tooltip")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.multiple.changes.tooltip")); //$NON-NLS-1$
				break;
			case REJECT:
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.multiple.changes.tooltip")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		MergeMode mode = getSelectedMode();
		ICompareCommandStack commandStack = editingDomain.getCommandStack();
		if (commandStack instanceof TransactionalDualCompareCommandStack) {
			TransactionalDualCompareCommandStack transactionalDualCompareCommandStack = (TransactionalDualCompareCommandStack)commandStack;
			boolean oldDeliver = transactionalDualCompareCommandStack.isDeliver();

			try {
				// Disable notifications during all the processing
				transactionalDualCompareCommandStack.setDeliver(false);

				ManagedCommandStack managedCmdStack = new ManagedCommandStack(commandStack);
				managedCmdStack.undoUntilDiffsAreInTerminalState(selectedDifferences);

				// If selected diffs are still in the terminal state we seem to be unable to process them.
				// This should really never happen, but if some command doesn't support undo, it's possible to
				// get in this situation.
				if (any(selectedDifferences, IS_IN_TERMINAL_STATE)) {
					managedCmdStack.restoreCommandStack();
					return;
				}

				// There might be commands that can't be repeated, so they would be lost when we redo all
				// undone commands. We should better ask the user if that's what she wants.
				if (managedCmdStack.hasUnrepeatableCommands()) {
					if (!MessageDialog.openQuestion(
							PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(),
							EMFCompareIDEUIMessages.getString("MergeAction.redoProblem.title"), //$NON-NLS-1$
							EMFCompareIDEUIMessages.getString("MergeAction.redoProblem.message", //$NON-NLS-1$
									Integer.valueOf(managedCmdStack.getNonRepeatableCommandsCount())))) {
						managedCmdStack.restoreCommandStack();
						return;
					}
				}

				// Execute the command to process the selected diffs using the selected mode.
				execute(commandStack, mode, selectedDifferences);

				boolean haveUndoneSelectedDifferences = managedCmdStack.redoExcept(selectedDifferences, mode);

				// If we have undone the command...
				if (haveUndoneSelectedDifferences) {
					// Process the selected diffs again, but now at the top of the stack.
					execute(commandStack, mode, selectedDifferences);
				}
			} finally {
				// Restore old delivery state. This will send notifications to the command stack listeners.
				if (oldDeliver) {
					transactionalDualCompareCommandStack.setDeliver(true);
				}
			}
		} else {
			// Execute the command to process using the selected mode the selected diffs.
			execute(commandStack, mode, selectedDifferences);
		}

		if (navigatable != null && EMFCompareIDEUIPlugin.getDefault().getPreferenceStore()
				.getBoolean(EMFCompareUIPreferences.SELECT_NEXT_UNRESOLVED_DIFF)) {
			// navigator is null in MergeAllNonConflictingAction
			navigatable.selectChange(Navigatable.NEXT_UNRESOLVED_CHANGE);
		}
	}

	protected void execute(ICompareCommandStack commandStack, MergeMode mode, List<Diff> diffs) {
		IMergeRunnable runnable = createMergeRunnable(mode, isLeftEditable(), isRightEditable(),
				diffRelationshipComputer);
		ICompareCopyCommand command = editingDomain.createCopyCommand(diffs,
				mode.isLeftToRight(isLeftEditable(), isRightEditable()), mergerRegistry, runnable);
		commandStack.execute(command);
	}

	/**
	 * A facade to get a more manageable command stack when undoing and redoing certain diffs.
	 * <p>
	 * Maintains a list of diff changes and a map from each of those to a possible associated edit command.
	 * This allows to properly detect cases that we can't support and redo undone diffs.
	 * </p>
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class ManagedCommandStack {

		private List<Multimap<DifferenceState, Diff>> diffChangesList = Lists.newArrayList();

		private Map<Multimap<DifferenceState, Diff>, EditCommand> editCommands = new IdentityHashMap<Multimap<DifferenceState, Diff>, EditCommand>();

		private int nonRepeatableCommandCount = 0;

		private ICompareCommandStack commandStack;

		private boolean isChangeUndoneYet;

		public ManagedCommandStack(ICompareCommandStack commandStack) {
			this.commandStack = commandStack;
		}

		private void addChangedDiffs(Multimap<DifferenceState, Diff> changedDiffs) {
			diffChangesList.add(changedDiffs);
		}

		private void addChangedDiffs(Multimap<DifferenceState, Diff> changedDiffs, EditCommand editCommand) {
			addChangedDiffs(changedDiffs);
			editCommands.put(changedDiffs, editCommand);
		}

		private int getChangedDiffsSize() {
			return diffChangesList.size();
		}

		private void reverseDiffChanges() {
			Collections.reverse(diffChangesList);
		}

		private List<Multimap<DifferenceState, Diff>> getDiffChanges() {
			return diffChangesList;
		}

		private EditCommand getEditCommand(Multimap<DifferenceState, Diff> diffChanges) {
			return editCommands.get(diffChanges);
		}

		private void increaseNonRepeatableCommandCount() {
			nonRepeatableCommandCount = nonRepeatableCommandCount + 1;
		}

		public int getNonRepeatableCommandsCount() {
			return nonRepeatableCommandCount;
		}

		public int getUndoneCommandsCount() {
			return getNonRepeatableCommandsCount() + getChangedDiffsSize();
		}

		public boolean hasUnrepeatableCommands() {
			return getNonRepeatableCommandsCount() > 0;
		}

		public void undoUntilDiffsAreInTerminalState(List<Diff> diffs) {
			while (commandStack.canUndo() && any(diffs, IS_IN_TERMINAL_STATE)) {
				Command undoCommand = commandStack.getUndoCommand();

				// Keep track of undone changes
				if (undoCommand instanceof AbstractCopyCommand) {
					AbstractCopyCommand copyCommand = (AbstractCopyCommand)undoCommand;
					addChangedDiffs(copyCommand.getChangedDiffs());
				} else if (undoCommand instanceof EditCommand) {
					EditCommand editCommand = (EditCommand)undoCommand;
					addChangedDiffs(editCommand.getChangedDiffs(), editCommand);
				} else if (isCompoundCommandContainingAbstractCopyCommand(undoCommand)) {
					Command firstCommand = getFirstCommandFromCompoundCommand(undoCommand);
					AbstractCopyCommand copyCmd = (AbstractCopyCommand)firstCommand;
					addChangedDiffs(copyCmd.getChangedDiffs());
				} else {
					increaseNonRepeatableCommandCount();
				}

				commandStack.undo();
			}
		}

		public void restoreCommandStack() {
			for (int i = getUndoneCommandsCount(); i > 0; --i) {
				commandStack.redo();
			}
		}

		/**
		 * Performs a redo of all commands that have previously been undone with this managed command stack,
		 * except for the specified <code>diffsToExclude</code>.
		 * 
		 * @param diffsToExclude
		 *            Diffs to exclude from redoing.
		 * @param mode
		 *            The merge mode.
		 * @return whether we've needed to undo any of the <code>diffsToExclude</code>.
		 */
		public boolean redoExcept(List<Diff> diffsToExclude, MergeMode mode) {
			isChangeUndoneYet = false;
			reverseDiffChanges();

			for (Multimap<DifferenceState, Diff> diffsToBeRestored : getDiffChanges()) {
				redoExcept(diffsToBeRestored, diffsToExclude, mode);
			}

			return isChangeUndoneYet;
		}

		private void redoExcept(Multimap<DifferenceState, Diff> diffsToRestore, List<Diff> diffsToExclude,
				MergeMode mode) {
			// If there is an edit command associated with these diff changes...
			EditCommand editCommand = getEditCommand(diffsToRestore);
			if (editCommand != null) {
				// If any of the diffs changed by this edit command is the diff we are currently
				// processing, then ignore this edit command and its associated diff.
				Collection<Diff> discardedDiffs = diffsToRestore.get(DISCARDED);
				for (Diff diff : discardedDiffs) {
					if (diffsToExclude.contains(diff)) {
						return;
					}
				}

				undoIfNotUndoneYet();
				commandStack.execute(editCommand.recreate());
				return;
			}

			// Remove any diffs that have changed state because of other command execution.
			removeTerminalStateDiffs(diffsToRestore.values().iterator());

			// If there are diff changes that need to be restored...
			if (!diffsToRestore.values().isEmpty()) {
				undoIfNotUndoneYet();

				List<Diff> diffsToMerge = Lists.newArrayList(diffsToRestore.get(MERGED));
				List<Diff> diffsToDiscard = Lists.newArrayList(diffsToRestore.get(DISCARDED));

				if (mode == ACCEPT || mode == REJECT) {
					redoDiffs(diffsToMerge, diffsToDiscard, ACCEPT, REJECT);
				} else {
					List<Diff> diffsToBeCopiedLTR = Streams
							.concat(diffsToMerge.stream().filter(fromSource(LEFT)),
									diffsToDiscard.stream().filter(fromSource(RIGHT)))
							.collect(Collectors.toList());
					List<Diff> diffsToBeCopiedRTL = Stream
							.concat(diffsToMerge.stream().filter(fromSource(RIGHT)),
									diffsToDiscard.stream().filter(fromSource(LEFT)))
							.collect(Collectors.toList());
					redoDiffs(diffsToBeCopiedLTR, diffsToBeCopiedRTL, LEFT_TO_RIGHT, RIGHT_TO_LEFT);
				}
			}
		}

		private void redoDiffs(List<Diff> diffsToMerge, List<Diff> diffsToDiscarded, MergeMode modeForMerged,
				MergeMode modeForDiscarded) {
			// If there are any diffs that need to be in the merged state...
			if (!diffsToMerge.isEmpty()) {
				// Processed those diffs.
				executeCompareCopyCommand(commandStack, modeForMerged, diffsToMerge);
				// Clean up any discarded diffs that are already in the terminal state.
				removeTerminalStateDiffs(diffsToDiscarded.iterator());
			}

			// If there are any diffs that need to be in the discarded state...
			if (!diffsToDiscarded.isEmpty()) {
				// Process those diffs.
				executeCompareCopyCommand(commandStack, modeForDiscarded, diffsToDiscarded);
			}
		}

		private void undoIfNotUndoneYet() {
			if (!isChangeUndoneYet) {
				commandStack.undo();
				isChangeUndoneYet = true;
			}
		}

		private java.util.function.Predicate<? super Diff> fromSource(DifferenceSource source) {
			return (diff) -> diff.getSource() == source;
		}

		private boolean isCompoundCommandContainingAbstractCopyCommand(Command command) {
			Command firstCommand = getFirstCommandFromCompoundCommand(command);
			return firstCommand instanceof AbstractCopyCommand;
		}

		private Command getFirstCommandFromCompoundCommand(Command possiblyCompoundCommand) {
			Command command = null;
			if (possiblyCompoundCommand instanceof CompoundCommand) {
				final CompoundCommand compoundCommand = (CompoundCommand)possiblyCompoundCommand;
				if (!compoundCommand.getCommandList().isEmpty()) {
					command = compoundCommand.getCommandList().get(0);
				}
			}
			return command;
		}

		private void removeTerminalStateDiffs(Iterator<Diff> diffs) {
			while (diffs.hasNext()) {
				if (AbstractMerger.isInTerminalState(diffs.next())) {
					diffs.remove();
				}
			}
		}

	}

	protected void executeCompareCopyCommand(ICompareCommandStack commandStack, MergeMode mode,
			List<Diff> diffs) {
		IMergeRunnable runnable = new MergeRunnableImpl(isLeftEditable(), isRightEditable(), mode,
				diffRelationshipComputer);
		ICompareCopyCommand command = editingDomain.createCopyCommand(diffs,
				mode.isLeftToRight(isLeftEditable(), isRightEditable()), mergerRegistry, runnable);
		commandStack.execute(command);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.actions.BaseSelectionListenerAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		addAll(selectedDifferences, getSelectedDifferences(selection));
		if (this.adapterFactory != null) {
			contextualizeTooltip();
		}
		// The action is enabled only if all the elements in the selection are diffs that will change state
		// when this action is applied.
		return !selectedDifferences.isEmpty() && selection.toList().size() == selectedDifferences.size();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.actions.BaseSelectionListenerAction#clearCache()
	 */
	@Override
	protected void clearCache() {
		selectedDifferences.clear();
	}

	protected Iterable<Diff> getSelectedDifferences(IStructuredSelection selection) {
		List<?> selectedObjects = selection.toList();
		Iterable<Adapter> selectedAdapters = filter(selectedObjects, Adapter.class);
		Iterable<Notifier> selectedNotifiers = transform(selectedAdapters, ADAPTER__TARGET);
		Iterable<TreeNode> selectedTreeNode = filter(selectedNotifiers, TreeNode.class);
		Iterable<EObject> selectedEObjects = transform(selectedTreeNode, IDifferenceGroup.TREE_NODE_DATA);
		Iterable<Diff> diffs = filter(selectedEObjects, Diff.class);
		return getSelectedDifferences(diffs);
	}

	protected Predicate<Diff> getStatePredicate() {
		return new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				switch (diff.getState()) {
					case DISCARDED:
						switch (getSelectedMode()) {
							case ACCEPT:
								return true;
							case LEFT_TO_RIGHT:
								return diff.getSource() == LEFT;
							case RIGHT_TO_LEFT:
								return diff.getSource() == RIGHT;
							default:
								return false;
						}

					case MERGED:
						switch (getSelectedMode()) {
							case REJECT:
								return true;
							case RIGHT_TO_LEFT:
								return diff.getSource() == LEFT;
							case LEFT_TO_RIGHT:
								return diff.getSource() == RIGHT;
							default:
								return false;
						}
					default:
						return true;
				}
			}
		};
	}

	protected Iterable<Diff> getSelectedDifferences(Iterable<Diff> diffs) {
		ICompareCommandStack commandStack = editingDomain.getCommandStack();

		// We can only re-process diffs in the terminal state if we have a command stack that supports
		// suspending the delivery of notifications. So filter out diffs that are already in the terminal
		// state.
		if (!(commandStack instanceof TransactionalDualCompareCommandStack)) {
			return filter(diffs, IS_NOT_IN_TERMINAL_STATE);
		}

		// Filter out diffs whose state would not be changed by this actions's selected mode.
		return filter(diffs, getStatePredicate());
	}

	/**
	 * @param newValue
	 */
	public final void setEditingDomain(ICompareEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
		clearCache();
		setEnabled(editingDomain != null && updateSelection(getStructuredSelection()));
	}

	/**
	 * Set the adapter factory used by this action.
	 * 
	 * @param adapterFactory
	 *            adapter factory
	 */
	public final void setAdapterFactory(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		if (adapterFactory != null) {
			contextualizeTooltip();
		}
	}

	/**
	 * Refreshes the merge action by re-creating the necessary elements based on the current compare
	 * configuration.
	 */
	public void setMirrored(boolean mirrored) {
		if (selectedMode == ACCEPT || selectedMode == REJECT) {
			return;
		}

		this.isMirrored = mirrored;
	}

	/**
	 * @return the leftToRight
	 */
	protected final boolean isLeftToRight() {
		return getSelectedMode().isLeftToRight(isLeftEditable(), isRightEditable());
	}

	/**
	 * Returns the cached selected differences.
	 * 
	 * @return The cached selected differences.
	 */
	public List<Diff> getSelectedDifferences() {
		return selectedDifferences;
	}

	protected IDiffRelationshipComputer getDiffRelationshipComputer() {
		return diffRelationshipComputer;
	}

	protected MergeMode getSelectedMode() {
		if (isMirrored() && (selectedMode == LEFT_TO_RIGHT || selectedMode == RIGHT_TO_LEFT)) {
			return selectedMode.inverse();
		} else {
			return selectedMode;
		}
	}

	protected boolean isLeftEditable() {
		return compareConfiguration.isLeftEditable();
	}

	protected boolean isRightEditable() {
		return compareConfiguration.isRightEditable();
	}

	protected boolean isMirrored() {
		return isMirrored;
	}
}
