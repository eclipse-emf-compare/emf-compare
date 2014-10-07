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

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.domain.IMergeAllNonConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract Action that manages a merge of a all non-conflicting difference in case of both sides of the
 * comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergeAllNonConflictingAction extends MergeAction {

	private Comparison comparison;

	private IMergeAllNonConflictingRunnable runnable;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public MergeAllNonConflictingAction(ICompareEditingDomain editingDomain, Comparison comparison,
			IMerger.Registry mergerRegistry, MergeMode mode, boolean isLeftEditable, boolean isRightEditable) {
		super(editingDomain, mergerRegistry, mode, isLeftEditable, isRightEditable, null);

		Preconditions.checkNotNull(mode);
		Preconditions.checkState(isLeftEditable || isRightEditable);
		// if left and right editable, the only accepted mode are LtR or RtL
		if (isLeftEditable && isRightEditable) {
			Preconditions.checkState(mode == MergeMode.LEFT_TO_RIGHT || mode == MergeMode.RIGHT_TO_LEFT);
		}
		// if mode is accept or reject, left and right can't be both read only (no action should be created in
		// this case) and can't be both editable.
		if (isLeftEditable != isRightEditable) {
			Preconditions.checkState(mode == MergeMode.ACCEPT || mode == MergeMode.REJECT);
		}

		this.comparison = comparison;
		this.runnable = new MergeAllNonConflictingRunnable(isLeftEditable, isRightEditable, mode);
	}

	@Override
	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.all.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_all_to_right.gif")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.all.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_all_to_left.gif")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.all.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept_all_changes.gif")); //$NON-NLS-1$
				break;
			case REJECT:
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.all.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject_all_changes.gif")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	public void setComparison(Comparison comparison) {
		this.comparison = comparison;
		clearCache();
		// update the enablement of this action by simulating a selection change.
		setEnabled(comparison != null);
	}

	@Override
	public void run() {
		if (editingDomain instanceof EMFCompareEditingDomain) {
			ICompareCopyCommand mergeCommand = ((EMFCompareEditingDomain)editingDomain)
					.createCopyAllNonConflictingCommand(comparison, isLeftToRight(), mergerRegistry, runnable);
			editingDomain.getCommandStack().execute(mergeCommand);
		} else {
			// FIXME remove this once we have pulled "createCopyAllNonConflictingCommand" up as API.
			EMFCompareIDEUIPlugin.getDefault().getLog().log(
					new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID,
							"Couldn't create the copy all command.")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction#getDifferencesToMerge()
	 */
	@Override
	protected List<Diff> getDifferencesToMerge() {
		// We're overriding #run(), so this has no effect
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		// this subclass does not care about the selection change event.
		return true;
	}

}
