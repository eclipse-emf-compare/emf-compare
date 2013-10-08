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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.util.EMFCompareUIActionUtil;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.jface.action.Action;

/**
 * Abstract action that manages the accept all and reject all actions (when one side of a diff is not
 * editable).
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class AbstractAcceptRejectAllAction extends Action {

	/** The compare configuration object used to get the compare model. */
	protected EMFCompareConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public AbstractAcceptRejectAllAction(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		boolean rightEditableOnly = !configuration.isLeftEditable() && configuration.isRightEditable();
		boolean leftEditableOnly = configuration.isLeftEditable() && !configuration.isRightEditable();
		if (leftEditableOnly) {
			manageChanges(false);
		} else if (rightEditableOnly) {
			manageChanges(true);
		}
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
	 * Manage changes (copy or change state) for all non-conflicting diffs.
	 * 
	 * @param leftToRight
	 *            the way of merge.
	 */
	private void manageChanges(final boolean leftToRight) {
		final List<Diff> differences;
		Comparison comparison = configuration.getComparison();
		if (comparison.isThreeWay()) {
			differences = ImmutableList.copyOf(filter(comparison.getDifferences(), new Predicate<Diff>() {
				public boolean apply(Diff diff) {
					final boolean unresolved = diff.getState() == DifferenceState.UNRESOLVED;
					final boolean nonConflictual = diff.getConflict() == null;
					final boolean fromLeftToRight = leftToRight && diff.getSource() == DifferenceSource.LEFT;
					final boolean fromRightToLeft = !leftToRight
							&& diff.getSource() == DifferenceSource.RIGHT;
					return unresolved && nonConflictual && (fromLeftToRight || fromRightToLeft);
				}
			}));
		} else {
			differences = ImmutableList.copyOf(filter(comparison.getDifferences(), EMFComparePredicates
					.hasState(DifferenceState.UNRESOLVED)));
		}

		if (differences.size() > 0) {

			ICompareEditingDomain editingDomain = configuration.getEditingDomain();

			AcceptRejectAllChangesCompoundCommand compoundCommand = new AcceptRejectAllChangesCompoundCommand(
					leftToRight);

			for (Diff diff : differences) {
				if (DifferenceState.UNRESOLVED == diff.getState()) {
					if (isCopyDiffCase(diff, leftToRight)) {
						EMFCompareUIActionUtil.setMergeDataForDiff(diff, leftToRight, configuration
								.isLeftEditable(), configuration.isRightEditable());
						compoundCommand.append(editingDomain.createCopyCommand(Lists.newArrayList(diff),
								leftToRight, EMFCompareRCPPlugin.getDefault().getMergerRegistry()));
					} else {
						compoundCommand.append(createChangeStateFromUnresolvedToMergedCommand(diff,
								!leftToRight));
					}
				}
			}

			editingDomain.getCommandStack().execute(compoundCommand);
		}
	}

	/**
	 * Execute a command that change the state of the given diff from {@link DifferenceState#UNRESOLVED} to
	 * {@link DifferenceState#MERGED}.
	 * 
	 * @param diffToChangeState
	 *            the given diff.
	 * @param leftToRight
	 *            the way of merge.
	 * @return A command that change the state of the given diff from {@link DifferenceState#UNRESOLVED} to
	 *         {@link DifferenceState#MERGED}.
	 */
	protected Command createChangeStateFromUnresolvedToMergedCommand(Diff diffToChangeState,
			boolean leftToRight) {
		if (diffToChangeState != null) {
			ICompareEditingDomain compareEditingDomain = configuration.getEditingDomain();
			Command changeStateCommand = new AcceptRejectAllChangesCommand(compareEditingDomain
					.getChangeRecorder(), diffToChangeState, leftToRight, configuration);
			return changeStateCommand;
		}
		return null;
	}

	/**
	 * A specific {@link ChangeCommand} that change the state of the given diff from
	 * {@link DifferenceState#UNRESOLVED} to {@link DifferenceState#MERGED}.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private static class AcceptRejectAllChangesCommand extends ChangeCommand implements ICompareCopyCommand {

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
		public AcceptRejectAllChangesCommand(ChangeRecorder changeRecorder, Diff difference,
				boolean leftToRight, CompareConfiguration configuration) {
			super(changeRecorder, difference);
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
			EMFCompareUIActionUtil.setMergeDataForDiff(difference, leftToRight, configuration
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

	/**
	 * A specific {@link CompoundCommand} that implements {@link ICompareCopyCommand}.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private static class AcceptRejectAllChangesCompoundCommand extends CompoundCommand implements ICompareCopyCommand {

		/** The way of merge. */
		private boolean leftToRight;

		/**
		 * Constructor.
		 * 
		 * @param leftToRight
		 *            the way of merge.
		 */
		public AcceptRejectAllChangesCompoundCommand(boolean leftToRight) {
			this.leftToRight = leftToRight;
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
