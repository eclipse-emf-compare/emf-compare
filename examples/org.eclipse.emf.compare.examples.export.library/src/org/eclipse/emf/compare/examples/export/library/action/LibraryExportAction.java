/*******************************************************************************
 * Copyright (c) 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.export.library.action;

import java.net.URL;

import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.examples.export.library.provider.LibraryEditPlugin;
import org.eclipse.emf.compare.examples.export.library.wizard.LibraryExportWizard;
import org.eclipse.emf.compare.ui.export.IExportAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * This action will launch a wizard allowing the user to export the result of a comparison on library files as
 * a human readable report.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class LibraryExportAction implements IExportAction {
	/** Text that will be displayed for the action. */
	private final String text;
	
	/** Text that will be used as tool tip for this action. */
	private final String toolTipText;
	
	/** Image used as this action's icon. We'll use the same icon for enabled and disabled state. */
	private final Image image;
	
	/**
	 * Default constructor.
	 */
	public LibraryExportAction() {
		text = "Library report";
		toolTipText = "Exports library comparison result as a report";
		final URL imageURL = LibraryEditPlugin.getPlugin().getBundle().getEntry("icons/libraryexport.gif"); //$NON-NLS-1$
		image = ImageDescriptor.createFromURL(imageURL).createImage();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.export.IExportAction#getText()
	 */
	public String getText() {
		return text; 
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.export.IExportAction#getToolTipText()
	 */
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.ui.export.IExportAction#getDisabledImage()
	 */
	public Image getDisabledImage() {
		return image;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.ui.export.IExportAction#getEnabledImage()
	 */
	public Image getEnabledImage() {
		return image;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.ui.export.IExportAction#exportSnapshot(org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot)
	 */
	public void exportSnapshot(ModelInputSnapshot snapshot) {
		final LibraryExportWizard wizard = new LibraryExportWizard();
		final IWorkbench workbench = PlatformUI.getWorkbench();
		
		wizard.init(workbench, snapshot);
		final WizardDialog dialog = new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
	}
}
