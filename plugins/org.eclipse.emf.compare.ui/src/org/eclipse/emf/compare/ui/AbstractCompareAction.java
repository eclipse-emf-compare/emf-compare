/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
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

import org.eclipse.compare.CompareUI;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.export.IExportAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Implements an action that will be initialized given a {@link ResourceBundle} and a key prefix.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractCompareAction extends Action {
	/** Fullpath for our action icons. */
	private static final IPath ICONS_PATH = new Path("$nl$/icons/full/"); //$NON-NLS-1$

	/**
	 * Creates and initializes an action given a {@link IExportAction descriptor}.
	 * 
	 * @param descriptor
	 *            Descriptor from which we'll get the needed parameters.
	 */
	public AbstractCompareAction(IExportAction descriptor) {
		setText(descriptor.getText());
		setToolTipText(descriptor.getToolTipText());
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor.getEnabledImage()));
		setHoverImageDescriptor(ImageDescriptor.createFromImage(descriptor.getEnabledImage()));
		setDisabledImageDescriptor(ImageDescriptor.createFromImage(descriptor.getDisabledImage()));
	}

	/**
	 * Creates and initializes an action given its {@link ResourceBundle} and its keys prefix.
	 * <p>
	 * Expects keys are of the form
	 * <ul>
	 * <li>displayed label : <code>keyPrefix</code>label (ex: compare.label)</li>
	 * <li>action tooltip : <code>keyPrefix</code>tooltip (ex: compare.tooltip)</li>
	 * <li>action icon : <code>keyPrefix</code>image (ex: compare.image)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param bundle
	 *            The bundle from which to read the keys.
	 * @param keyPrefix
	 *            Prefix for this action's keys.
	 */
	public AbstractCompareAction(ResourceBundle bundle, String keyPrefix) {
		if (keyPrefix == null || keyPrefix.length() <= 0) {
			throw new IllegalArgumentException(EMFCompareUIMessages
					.getString("AbstractCompareAction.IllegalKey")); //$NON-NLS-1$
		} else if (bundle == null) {
			throw new IllegalArgumentException(EMFCompareUIMessages
					.getString("AbstractCompareAction.IllegalBundle")); //$NON-NLS-1$
		}
		final String labelKey = keyPrefix + "label"; //$NON-NLS-1$
		final String tooltipKey = keyPrefix + "tooltip"; //$NON-NLS-1$
		final String imageKey = keyPrefix + "image"; //$NON-NLS-1$

		setText(bundle.getString(labelKey));
		setToolTipText(bundle.getString(tooltipKey));

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

			ImageDescriptor disabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.find(Platform
					.getBundle(CompareUI.PLUGIN_ID), ICONS_PATH.append(disabledImagePath), null));
			ImageDescriptor enabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.find(Platform
					.getBundle(CompareUI.PLUGIN_ID), ICONS_PATH.append(enabledImagePath), null));

			// if the image cannot be found in CompareUIPlugin's icon path, we seek in ours.
			if (disabledImageDescriptor.equals(ImageDescriptor.getMissingImageDescriptor())) {
				try {
					disabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.toFileURL(Platform
							.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/" + imagePath))); //$NON-NLS-1$
					enabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.toFileURL(Platform
							.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/" + imagePath))); //$NON-NLS-1$
				} catch (IOException e) {
					EMFComparePlugin.log(e.getMessage(), false);
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
	 * Subclasses should override this method to define their action's behavior.
	 * 
	 * @see Action#run()
	 */
	@Override
	public abstract void run();
}
