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

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages the click on the menu item "show consequences of merge from left to right" in the
 * dropdown menu of the toolbar of the structure merge viewer when both sides are writeable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class DropDownLeftToRightAction extends Action {

	/** The compare configuration object used to get the compare model. */
	private EMFCompareConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public DropDownLeftToRightAction(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
		setText(EMFCompareIDEUIMessages.getString("dropdown.left.to.right.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		configuration.setPreviewMergeMode(true);
	}
}
