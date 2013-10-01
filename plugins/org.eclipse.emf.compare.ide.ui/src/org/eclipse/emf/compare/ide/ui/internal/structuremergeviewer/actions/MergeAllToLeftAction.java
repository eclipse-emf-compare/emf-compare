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
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.util.EMFCompareUIActionUtil;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages a merge from left to right of all non-conflicting differences in case of both sides of
 * the comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergeAllToLeftAction extends AbstractMergeAllAction {

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public MergeAllToLeftAction(CompareConfiguration configuration) {
		super(configuration);
		setToolTipText(EMFCompareIDEUIMessages.getString("merged.all.to.left.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/merge_all_to_left.gif")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.AbstractMergeAllAction#copyAllDiffs()
	 */
	@Override
	protected void copyAllDiffs() {
		EMFCompareUIActionUtil.copyAllDiffs(false, getConfiguration());
	}
}
