/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.export.xslt.action;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.examples.export.xslt.Messages;
import org.eclipse.emf.compare.examples.export.xslt.XSLTExportPlugin;
import org.eclipse.emf.compare.examples.export.xslt.wizard.ExportAsHTMLWizard;
import org.eclipse.emf.compare.ui.export.IExportAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * This action export a given emfdiff file as HTML.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ExportAsHTMLAction implements IExportAction {
	/** Text that will be displayed for the action. */
	private final String text;

	/** Text that will be used as tool tip for this action. */
	private final String toolTipText;

	/** Image used as this action's icon when enabled. */
	private final Image enabledImage;

	/** Image used as this action's icon when disabled. */
	private final Image disabledImage;

	/**
	 * Default constructor. Instantiates this action by setting its text, tooltip and image.
	 */
	public ExportAsHTMLAction() {
		text = Messages.getString("ExportAsHTMLAction.action.label"); //$NON-NLS-1$
		toolTipText = Messages.getString("ExportAsHTMLAction.action.tooltip"); //$NON-NLS-1$

		final String imagePath = Messages.getString("ExportAsHTMLAction.action.image"); //$NON-NLS-1$

		ImageDescriptor disabledImageDescriptor = null;
		ImageDescriptor enabledImageDescriptor = null;
		final URL imageURL = XSLTExportPlugin.getDefault().getBundle().getEntry("icons/full/" + imagePath); //$NON-NLS-1$
		try {
			disabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.toFileURL(imageURL));
			enabledImageDescriptor = ImageDescriptor.createFromURL(FileLocator.toFileURL(imageURL));
		} catch (final IOException e) {
			EMFComparePlugin.log(e.getMessage(), false);
		}

		if (disabledImageDescriptor != null) {
			disabledImage = disabledImageDescriptor.createImage();
		} else {
			disabledImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		if (enabledImageDescriptor != null) {
			enabledImage = enabledImageDescriptor.createImage();
		} else {
			enabledImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IExportAction#getText()
	 */
	public String getText() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IExportAction#getToolTipText()
	 */
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IExportAction#getDisabledImage()
	 */
	public Image getDisabledImage() {
		return disabledImage;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IExportAction#getEnabledImage()
	 */
	public Image getEnabledImage() {
		return enabledImage;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IExportAction#exportSnapshot(ModelInputSnapshot)
	 */
	public void exportSnapshot(ComparisonSnapshot snapshot) {
		final ExportAsHTMLWizard wizard = new ExportAsHTMLWizard();
		final IWorkbench workbench = PlatformUI.getWorkbench();

		wizard.init(workbench, snapshot);
		final WizardDialog dialog = new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
	}
}
