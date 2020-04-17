/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.util;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeRunnableImpl;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.CachingDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;

/**
 * Abstract class for undo and redo tests. This class provides methods to initialize a command stack to merge
 * differences and undo/redo the last executed command.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public abstract class AbstractReverseActionTest {

	/**
	 * Used to initialize a default merger registry for merge action.
	 */
	private IMerger.Registry mergerRegistry;

	/**
	 * Used to merge differences.
	 */
	private IMergeRunnable mergeRunnable;

	/**
	 * Used to initialize a command stack.
	 */
	private ICompareEditingDomain editingDomain;

	/**
	 * Getter for the scope.
	 * 
	 * @return scope.
	 */
	public abstract DefaultComparisonScope getScope();

	/**
	 * Getter for the editingDomain.
	 * 
	 * @return editingDomain.
	 */
	public ICompareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * Getter for the merger registry.
	 * 
	 * @return mergerRegistry.
	 */
	public IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}

	/**
	 * Getter for the merge runnable.
	 * 
	 * @return mergeRunnable.
	 */
	public IMergeRunnable getMergeRunnable() {
		return mergeRunnable;
	}

	/**
	 * Initializes attributes for tests.
	 */
	@Before
	public void setUp() throws IOException {
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		editingDomain = null;
	}

	/**
	 * Provides access to the list of Nodes of a NodesResource.
	 * 
	 * @param resource
	 *            the resource that contains the nodes.
	 * @return the list of nodes.
	 */
	protected EList<Node> getNodes(Resource resource) {
		EObject container = resource.getContents().get(0);
		if (container instanceof Node) {
			return ((Node)container).getContainmentRef1();
		}
		return null;
	}

	/**
	 * Executes a right to left merge of a list of differences.
	 * 
	 * @param differences
	 *            the list of differences.
	 */
	protected void mergeDiffsRightToLeft(List<? extends Diff> differences) {
		mergeDiffs(differences, false);
	}

	/**
	 * Executes a left to right merge of a list of differences.
	 * 
	 * @param differences
	 *            the list of differences.
	 */
	protected void mergeDiffsLeftToRight(List<? extends Diff> differences) {
		mergeDiffs(differences, true);
	}

	/**
	 * Executes a right to left or left to right merge of a list of differences. The command is necessary to
	 * perform the execute(), undo, and redo() actions. The merge command is executed and stacked on the
	 * command stack for a potential undo or redo action.
	 * 
	 * @param differences
	 *            the list of differences.
	 * @param leftToRight
	 *            the merge direction.
	 */
	protected void mergeDiffs(List<? extends Diff> differences, boolean leftToRight) {
		if (editingDomain == null) {
			editingDomain = EMFCompareEditingDomain.create(getScope().getLeft(), getScope().getRight(),
					getScope().getOrigin());
		}

		initMergeRunnable(leftToRight);

		ICompareCopyCommand command = getEditingDomain().createCopyCommand(differences, leftToRight,
				getMergerRegistry(), getMergeRunnable());

		getEditingDomain().getCommandStack().execute(command);
	}

	/**
	 * Initialize the merge runnable used to merge differences.
	 * 
	 * @param leftToRight
	 *            the merge direction.
	 */
	protected void initMergeRunnable(boolean leftToRight) {
		IDiffRelationshipComputer computer = new CachingDiffRelationshipComputer(getMergerRegistry());
		final MergeMode mergeMode;
		if (leftToRight) {
			mergeMode = MergeMode.LEFT_TO_RIGHT;
		} else {
			mergeMode = MergeMode.RIGHT_TO_LEFT;
		}
		mergeRunnable = new MergeRunnableImpl(true, true, mergeMode, computer);
	}

	/**
	 * Restores the previous state after a merge, using an UndoAction object.
	 */
	protected void undo() {
		UndoAction undoAction = new UndoAction(getEditingDomain());
		undoAction.run();
	}

	/**
	 * Restores the previous state after an undo, using an RedoAction object.
	 */
	protected void redo() {
		RedoAction redoAction = new RedoAction(getEditingDomain());
		redoAction.run();
	}
}
