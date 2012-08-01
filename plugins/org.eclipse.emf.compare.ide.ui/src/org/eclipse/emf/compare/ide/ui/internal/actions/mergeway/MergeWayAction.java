/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.actions.mergeway;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ide.ui.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.CompareConfigurationExtension;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MergeWayAction extends Action {

	private final CompareConfiguration fCompareConfiguration;

	/**
	 * 
	 */
	public MergeWayAction(CompareConfiguration compareConfiguration) {
		fCompareConfiguration = compareConfiguration;
		setId(MergeWayAction.class.getName());

		update();
	}

	private void update() {
		boolean rightToLeft = getRightToLeft();
		setRightToLeft(rightToLeft);
		setToolTipText(doGetToolTipText(rightToLeft));
		setImageDescriptor(doGetImageDescriptor(rightToLeft));
	}

	private boolean getRightToLeft() {
		return CompareConfigurationExtension.getBoolean(fCompareConfiguration,
				EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT,
				EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT_DEFAULT);
	}

	private void setRightToLeft(boolean rightToLeft) {
		fCompareConfiguration.setProperty(EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT, Boolean
				.valueOf(rightToLeft));
	}

	private static String doGetToolTipText(boolean rightToLeft) {
		if (rightToLeft) {
			return "Consider merging from right to left";
		} else {
			return "Consider merging from left to right";
		}
	}

	public static ImageDescriptor doGetImageDescriptor(boolean rightToLeft) {
		if (rightToLeft) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/merge_rl.gif");
		} else {
			return AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/merge_lr.gif");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		update();
	}
}
