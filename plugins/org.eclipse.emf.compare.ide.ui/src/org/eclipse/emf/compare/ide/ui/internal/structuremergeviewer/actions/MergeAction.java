/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - bug 462237
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
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

	protected static final Function<? super Adapter, ? extends Notifier> ADAPTER__TARGET = new Function<Adapter, Notifier>() {
		public Notifier apply(Adapter adapter) {
			return adapter.getTarget();
		}
	};

	protected final Registry mergerRegistry;

	protected ICompareEditingDomain editingDomain;

	private final boolean leftToRight;

	private final IMergeRunnable mergeRunnable;

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

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public MergeAction(IEMFCompareConfiguration compareConfiguration, IMerger.Registry mergerRegistry,
			MergeMode mode, INavigatable navigatable) {
		super(""); //$NON-NLS-1$

		adapterFactory = compareConfiguration.getAdapterFactory();
		boolean isLeftEditable = compareConfiguration.isLeftEditable();
		boolean isRightEditable = compareConfiguration.isRightEditable();

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

		this.editingDomain = compareConfiguration.getEditingDomain();
		this.mergerRegistry = mergerRegistry;
		this.leftToRight = mode.isLeftToRight(isLeftEditable, isRightEditable);
		this.mergeRunnable = createMergeRunnable(mode, isLeftEditable, isRightEditable);
		this.selectedDifferences = newArrayList();
		this.selectedMode = mode;

		initToolTipAndImage(mode);
	}

	public MergeAction(IEMFCompareConfiguration compareConfiguration, IMerger.Registry mergerRegistry,
			MergeMode mode, INavigatable navigatable, IStructuredSelection selection) {
		this(compareConfiguration, mergerRegistry, mode, navigatable);
		updateSelection(selection);
	}

	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable) {
		return new MergeRunnableImpl(isLeftEditable, isRightEditable, mode);
	}

	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setText(EMFCompareIDEUIMessages.getString("merged.to.right.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_to_right.gif")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setText(EMFCompareIDEUIMessages.getString("merged.to.left.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_to_left.gif")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setText(EMFCompareIDEUIMessages.getString("accept.change.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.change.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept_change.gif")); //$NON-NLS-1$
				break;
			case REJECT:
				setText(EMFCompareIDEUIMessages.getString("reject.change.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.change.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject_change.gif")); //$NON-NLS-1$
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
		ICompareCopyCommand mergeCommand = editingDomain.createCopyCommand(selectedDifferences, leftToRight,
				mergerRegistry, mergeRunnable);
		editingDomain.getCommandStack().execute(mergeCommand);

		if (navigatable != null) {
			// navigator is null in MergeAllNonConflictingAction
			navigatable.selectChange(Navigatable.NEXT_UNRESOLVED_CHANGE);
		}
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
