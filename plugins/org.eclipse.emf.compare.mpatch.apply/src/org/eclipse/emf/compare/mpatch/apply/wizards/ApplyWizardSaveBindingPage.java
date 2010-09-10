/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
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
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;


/**
 * Wizard page asking the user for storing the binding between the {@link MPatchModel} and the changed target model.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ApplyWizardSaveBindingPage extends WizardNewFileCreationPage {

	private static final String DIFFBINDING_EXTENSION = MPatchConstants.FILE_EXTENSION_MPATCH_BINDING;

	public ApplyWizardSaveBindingPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle(pageName);
		setDescription("Please select a file to store the binding between the " + MPatchConstants.MPATCH_SHORT_NAME
				+ " and the model.");
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
			final URI uri = ((ApplyWizard) getWizard()).getModelResource().getURI();
			// setFileName(uri.lastSegment());
			setFileName(uri.trimFileExtension().lastSegment() + "." + DIFFBINDING_EXTENSION);
		}
	}

	@Override
	protected boolean validatePage() {
		if (super.validatePage()) {
			final String actualExtension = new Path(getFileName()).getFileExtension();
			if (actualExtension == null || !actualExtension.equals(DIFFBINDING_EXTENSION)) {
				setErrorMessage("Wrong file extension! '" + DIFFBINDING_EXTENSION + "' expected.");
				return false;
			}
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
					getContainerFullPath().append(getFileName()));
			((ApplyWizard) getWizard()).setBindingFile(file);
			return true;
		}
		return false;
	}
}
