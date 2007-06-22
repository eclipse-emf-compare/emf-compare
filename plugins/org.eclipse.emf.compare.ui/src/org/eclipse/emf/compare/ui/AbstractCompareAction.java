/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import java.io.IOException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Implements an action that will be initialized given a {@link ResourceBundle} and a 
 * key prefix.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public abstract class AbstractCompareAction extends Action {
	/* DIRTY 
	 * should use Utilities.getIconPath(null) or 
	 * CompareUIPlugin.getImageDescriptor(String) yet neither of these is accessible.
	 * Another option would be to copy the icons in our own /icons/full.
	 */
	private static final IPath ICONS_PATH = new Path("$nl$/icons/full/"); //$NON-NLS-1$
	
	/* DIRTY 
	 * should use CompareUIPlugin.getDefault or CompareUIPlugin.PLUGIN_ID yet neither of 
	 * these is accessible.
	 * Again, another option would be to copy the icons in our own /icons/full as this
	 * ID is used to resolve the icon path.
	 */
	private static final String COMPARE_UI_PLUGIN_ID = "org.eclipse.compare"; //$NON-NLS-1$
	
	/**
	 * Creates and initializes and action given its {@link ResourceBundle} and its 
	 * keys prefix.
	 * 
	 * @param bundle
	 * 			The bundle from which to read the keys.
	 * @param keyPrefix
	 * 			Prefix for this action's keys.
	 */
	public AbstractCompareAction(ResourceBundle bundle, String keyPrefix) {
		if (keyPrefix == null && keyPrefix.length() <= 0) {
			throw new IllegalArgumentException("keyPrefix must be specified."); //$NON-NLS-1$
		} else if (bundle == null) {
			throw new IllegalArgumentException("bundle cannot be null."); //$NON-NLS-1$
		}
		final String labelKey = keyPrefix + "label"; //$NON-NLS-1$
		final String tooltipKey = keyPrefix + "tooltip"; //$NON-NLS-1$
		final String imageKey = keyPrefix + "image"; //$NON-NLS-1$
		
		setText(bundle.getString(labelKey));
		setToolTipText(bundle.getString(tooltipKey));
		
		/* DIRTY
		 * We are here using the native icons of org.eclipse.compare for actions, yet
		 * some needed information is not open API. 
		 * see #ICONS_PATH
		 * see #COMPARE_UI_PLUGIN_ID
		 */
		final String imagePath = bundle.getString(imageKey);
		if (imagePath != null && imagePath.trim().length() > 0) {
			String disabledImagePath;
			String enabledImagePath;
			
			if (imagePath.indexOf("/") >= 0) { //$NON-NLS-1$
				final String path = imagePath.substring(1);
				disabledImagePath = 'd' + path;
				enabledImagePath = 'e' + path;
			} else {
				disabledImagePath = "dlcl16/" + imagePath; //$NON-NLS-1$
				enabledImagePath = "elcl16/" + imagePath; //$NON-NLS-1$
			}
			
			ImageDescriptor disabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.find(
					Platform.getBundle(COMPARE_UI_PLUGIN_ID), 
					ICONS_PATH.append(disabledImagePath),
					null));
			ImageDescriptor enabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.find(
					Platform.getBundle(COMPARE_UI_PLUGIN_ID), 
					ICONS_PATH.append(enabledImagePath),
					null));
			
			// if the image cannot be found in CompareUIPlugin's icon path, we seek in ours.
			if (disabledImageDescriptor.equals(ImageDescriptor.getMissingImageDescriptor())) {
				try {
					disabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.toFileURL(
							Platform.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/" + imagePath))); //$NON-NLS-1$
					enabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.toFileURL(
							Platform.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/" + imagePath))); //$NON-NLS-1$
				} catch (IOException e) {
					EMFComparePlugin.getDefault().log(e.getMessage(), false);
				}
			}
			
			// Sets the images.
			if (disabledImageDescriptor != null) {
				setDisabledImageDescriptor(disabledImageDescriptor);
			}
			if (enabledImageDescriptor != null) {
				setImageDescriptor(enabledImageDescriptor);
				setHoverImageDescriptor(enabledImageDescriptor);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see Action#run()
	 */
	@Override
	public abstract void run();
}
