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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract Action that manages a merge of a difference in case of both sides of the comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergeAction extends BaseSelectionListenerAction {

	private static final Function<? super Adapter, ? extends Notifier> ADAPTER__TARGET = new Function<Adapter, Notifier>() {
		public Notifier apply(Adapter adapter) {
			return adapter.getTarget();
		}
	};

	private final Registry mergerRegistry;

	private final boolean leftToRight;

	private final IMergeRunnable mergeRunnable;

	private ICompareEditingDomain editingDomain;

	private boolean cascadingDifferencesFilterEnabled;

	private final List<Diff> selectedDifferences;

	private final INavigatable navigatable;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public MergeAction(ICompareEditingDomain editingDomain, IMerger.Registry mergerRegistry, MergeMode mode,
			boolean isLeftEditable, boolean isRightEditable, INavigatable navigatable) {
		super(""); //$NON-NLS-1$
		this.navigatable = navigatable;

		Preconditions.checkNotNull(mode);
		// at least should be editable
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

		this.editingDomain = editingDomain;
		this.mergerRegistry = mergerRegistry;
		this.leftToRight = mode.isLeftToRight(isLeftEditable, isRightEditable);
		this.mergeRunnable = createMergeRunnable(mode, isLeftEditable, isRightEditable);
		this.selectedDifferences = newArrayList();

		initToolTipAndImage(mode);
	}

	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable) {
		return new MergeRunnableImpl(isLeftEditable, isRightEditable, mode);
	}

	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_to_right.gif")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_to_left.gif")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.change.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept_change.gif")); //$NON-NLS-1$
				break;
			case REJECT:
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.change.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject_change.gif")); //$NON-NLS-1$
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
		List<Diff> differences = getDifferencesToMerge();
		ICompareCopyCommand mergeCommand = editingDomain.createCopyCommand(differences, leftToRight,
				mergerRegistry, mergeRunnable);
		editingDomain.getCommandStack().execute(mergeCommand);

		if (navigatable != null) {
			// navigator is null in MergeAllNonConflictingAction
			navigatable.selectChange(INavigatable.NEXT_CHANGE);
		}
	}

	protected List<Diff> getDifferencesToMerge() {
		List<Diff> differencesToMerge = newArrayList(selectedDifferences);

		if (cascadingDifferencesFilterEnabled) {
			Iterable<Diff> cascadingDifferences = concat(transform(selectedDifferences, ComparisonUtil
					.getSubDiffs(leftToRight)));
			addAll(differencesToMerge, cascadingDifferences);
		}

		return differencesToMerge;
	}

	public final void setCascadingDifferencesFilterEnabled(boolean enable) {
		this.cascadingDifferencesFilterEnabled = enable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.actions.BaseSelectionListenerAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		addAll(selectedDifferences, getSelectedDifferences(selection));
		return selection.toList().size() == selectedDifferences.size();
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
		return filter(selectedEObjects, Diff.class);
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
	 * @return the leftToRight
	 */
	protected final boolean isLeftToRight() {
		return leftToRight;
	}

}
