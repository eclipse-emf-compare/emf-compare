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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Wizard page asking the user for storing a copy of the unchanged target model.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 *
 */
public class ApplyWizardNewModelPage extends WizardNewFileCreationPage {

	public ApplyWizardNewModelPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle(pageName);
		setDescription("Please select a file to store a copy of the model, e.g. for reviewing the changes made to the model.");
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
			final URI uri = ((ApplyWizard)getWizard()).getModelResource().getURI();
//			setFileName(uri.lastSegment());
			setFileName(uri.trimFileExtension().lastSegment() + "_copy." + uri.fileExtension());
		}
	}
	
	@Override
	protected boolean validatePage() {
		if (super.validatePage()) {
			final String expectedExtension = ((ApplyWizard)getWizard()).getModelResource().getURI().fileExtension();
			final String actualExtension = new Path(getFileName()).getFileExtension();
			if (actualExtension == null || !actualExtension.equals(expectedExtension)) {
				setErrorMessage("Wrong file extension! '" + expectedExtension + "' expected (same as input model).");
				return false;
			}
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
			((ApplyWizard)getWizard()).setNewModelFile(file);
			return true;
		}
		return false;
	}
}
