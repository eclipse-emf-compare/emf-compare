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
import org.eclipse.emf.compare.ide.ui.internal.IEMFCompareConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MergeWayAction extends Action {

	private boolean fRightToLeft;

	private final CompareConfiguration fCompareConfiguration;

	/**
	 * 
	 */
	public MergeWayAction(CompareConfiguration compareConfiguration) {
		fCompareConfiguration = compareConfiguration;
		fRightToLeft = IEMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT_DEFAULT;
		fCompareConfiguration.setProperty(IEMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT, Boolean
				.valueOf(fRightToLeft));

		setId(MergeWayAction.class.getName());
		setToolTipText(doGetToolTipText());
		setImageDescriptor(doGetImageDescriptor());
	}

	private String doGetToolTipText() {
		if (fRightToLeft) {
			return "Consider merging from right to left";
		} else {
			return "Consider merging from left to right";
		}
	}

	public ImageDescriptor doGetImageDescriptor() {
		if (fRightToLeft) {
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
		fRightToLeft = !fRightToLeft;
		fCompareConfiguration.setProperty(IEMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT, Boolean
				.valueOf(fRightToLeft));
		setToolTipText(doGetToolTipText());
		setImageDescriptor(doGetImageDescriptor());
	}
}
