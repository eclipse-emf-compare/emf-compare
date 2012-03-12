/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;


/**
 * Wizard page asking the user for storing an EMF Compare emfdiff file.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 *
 */
public class ApplyWizardNewEmfdiffPage extends WizardNewFileCreationPage {

	private static final String FILE_EXTENSION = MPatchConstants.FILE_EXTENSION_EMF_COMPARE;

	public ApplyWizardNewEmfdiffPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle(pageName);
		setDescription("Please select a file to store the emfdiff.");
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			initialize();
		}
	}
	
	private void initialize() {
		if (getFileName() == null || getFileName().length() == 0) {
			final URI uri = ((ApplyWizard)getWizard()).getMPatch().eResource().getURI();
			final String filename = uri.lastSegment();
			setFileName(filename.substring(0, filename.lastIndexOf(".") + 1) + FILE_EXTENSION);
		}
	}

	@Override
	protected boolean validatePage() {
		if (super.validatePage()) {
			final String extension = new Path(getFileName()).getFileExtension();
			if (extension == null || !FILE_EXTENSION.equals(extension)) {
				setErrorMessage("Wrong file extension! '" + FILE_EXTENSION + "' expected.");
				return false;
			}
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
			((ApplyWizard)getWizard()).setEmfdiff(file);
			return true;
		}
		return false;
	}
}
