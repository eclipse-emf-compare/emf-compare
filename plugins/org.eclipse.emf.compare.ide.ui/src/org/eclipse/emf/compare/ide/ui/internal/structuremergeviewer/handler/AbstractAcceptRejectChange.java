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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler;

import com.google.common.collect.ImmutableSet;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler.util.EMFCompareUIHandlerUtil;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.ui.ISources;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Abstract handler that manages the accept and reject actions (when one side of a diff is not editable).
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class AbstractAcceptRejectChange extends AbstractHandler {

	/** The compare configuration object used to get the compare model. */
	private CompareConfiguration configuration;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object editorInput = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (editorInput instanceof CompareEditorInput) {
			configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
			Object diffNode = ((CompareEditorInput)editorInput).getSelectedEdition();
			if (diffNode instanceof Adapter) {
				Notifier target = ((Adapter)diffNode).getTarget();
				if (target instanceof TreeNode) {
					EObject data = ((TreeNode)target).getData();
					if (data instanceof Diff) {
						boolean rightEditableOnly = !configuration.isLeftEditable()
								&& configuration.isRightEditable();
						boolean leftEditableOnly = configuration.isLeftEditable()
								&& !configuration.isRightEditable();
						if (leftEditableOnly) {
							if (isCopyDiffCase((Diff)data, false)) {
								EMFCompareUIHandlerUtil.copyDiff((Diff)data, false, configuration);
							} else {
								changeStateFromUnresolvedToMerged((Diff)data, true);
							}
						} else if (rightEditableOnly) {
							if (isCopyDiffCase((Diff)data, true)) {
								EMFCompareUIHandlerUtil.copyDiff((Diff)data, true, configuration);
							} else {
								changeStateFromUnresolvedToMerged((Diff)data, false);
							}
						}
						// Select next diff
						EMFCompareUIHandlerUtil.navigate(true, configuration);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Check if the way of merge of the given diff correspond to a copy or a simple change state (unresolved
	 * to merged).
	 * 
	 * @param diff
	 *            the given diff.
	 * @param leftToRight
	 *            the way of merge.
	 * @return true if the way of merge of the given diff correspond to a copy, false if it corresponds to a
	 *         simple change state.
	 */
	protected abstract boolean isCopyDiffCase(Diff diff, boolean leftToRight);

	/**
	 * Execute a command that change the state of the given diff from {@link DifferenceState#UNRESOLVED} to
	 * {@link DifferenceState#MERGED}.
	 * 
	 * @param diffToChangeState
	 *            the given diff.
	 * @param leftToRight
	 *            the way of merge.
	 */
	private void changeStateFromUnresolvedToMerged(Diff diffToChangeState, boolean leftToRight) {
		if (diffToChangeState != null) {
			ICompareEditingDomain compareEditingDomain = (ICompareEditingDomain)configuration
					.getProperty(EMFCompareConstants.EDITING_DOMAIN);
			Command changeStateCommand = new AcceptRejectChangeCommand(compareEditingDomain
					.getChangeRecorder(), diffToChangeState, leftToRight, configuration);
			compareEditingDomain.getCommandStack().execute(changeStateCommand);
		}
	}

	/**
	 * A specific {@link ChangeCommand} that change the state of the given diff and all its required diffs
	 * from {@link DifferenceState#UNRESOLVED} to {@link DifferenceState#MERGED}.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private static class AcceptRejectChangeCommand extends ChangeCommand implements ICompareCopyCommand {

		/** The difference concerned by the command. */
		private Diff difference;

		/** The way of merge. */
		private boolean leftToRight;

		/** The compare configuration object. */
		private CompareConfiguration configuration;

		/**
		 * Constructor.
		 * 
		 * @param changeRecorder
		 *            the change recorder used by the command.
		 * @param difference
		 *            the difference concerned by the command.
		 * @param leftToRight
		 *            the way of merge.
		 * @param configuration
		 *            the compare configuration object.
		 */
		public AcceptRejectChangeCommand(ChangeRecorder changeRecorder, Diff difference, boolean leftToRight,
				CompareConfiguration configuration) {
			super(changeRecorder, ImmutableSet.<Notifier> builder().add(difference).addAll(
					DiffUtil.getRequires(difference, leftToRight, difference.getSource())).build());
			this.difference = difference;
			this.leftToRight = leftToRight;
			this.configuration = configuration;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
		 */
		@Override
		public void doExecute() {
			for (Diff require : DiffUtil.getRequires(difference, leftToRight, difference.getSource())) {
				EMFCompareUIHandlerUtil.setMergeDataForDiff(require, leftToRight, configuration
						.isLeftEditable(), configuration.isRightEditable());
				require.setState(DifferenceState.MERGED);
			}
			EMFCompareUIHandlerUtil.setMergeDataForDiff(difference, leftToRight, configuration
					.isLeftEditable(), configuration.isRightEditable());
			difference.setState(DifferenceState.MERGED);

		}

		/**
		 * Returns true if the command will be applied from left to right side, false otherwise.
		 * 
		 * @return true if the command will be applied from left to right side, false otherwise.
		 */
		public boolean isLeftToRight() {
			return leftToRight;
		}
	}

}
