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

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.util.EMFCompareUIActionUtil;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Abstract Action that manages a merge of a difference in case of both sides of the comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public abstract class AbstractMergeAction extends Action {

	/** The compare configuration object used to get the compare model. */
	private EMFCompareConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public AbstractMergeAction(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		ISelection selection = (ISelection)configuration.getProperty(EMFCompareConstants.SMV_SELECTION);
		if (selection instanceof IStructuredSelection) {
			Object diffNode = ((IStructuredSelection)selection).getFirstElement();
			if (diffNode instanceof Adapter) {
				Notifier target = ((Adapter)diffNode).getTarget();
				if (target instanceof TreeNode) {
					EObject data = ((TreeNode)target).getData();
					if (data instanceof Diff) {
						copyDiff((Diff)data);
						// Select next diff
						EMFCompareUIActionUtil.navigate(true, configuration);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		ISelection selection = (ISelection)configuration.getProperty(EMFCompareConstants.SMV_SELECTION);
		if (selection instanceof IStructuredSelection) {
			Object diffNode = ((IStructuredSelection)selection).getFirstElement();
			if (diffNode instanceof Adapter) {
				Notifier target = ((Adapter)diffNode).getTarget();
				if (target instanceof TreeNode) {
					EObject data = ((TreeNode)target).getData();
					if (data instanceof Diff) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Get the compare configuration object.
	 * 
	 * @return the configuration
	 */
	public EMFCompareConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Set the compare configuration object.
	 * 
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Copy the diff.
	 * 
	 * @param diff
	 *            the given diff.
	 */
	protected abstract void copyDiff(Diff diff);

}
