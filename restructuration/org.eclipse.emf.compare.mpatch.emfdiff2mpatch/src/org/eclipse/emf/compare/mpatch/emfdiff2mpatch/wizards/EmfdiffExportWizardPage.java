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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.wizards;

import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;


/**
 * Page for asking the user where to store the resulting {@link MPatchModel}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class EmfdiffExportWizardPage extends WizardNewFileCreationPage {

	private static final String FILE_EXTENSION = "." + MPatchConstants.FILE_EXTENSION_MPATCH;

	/**
	 * Create new file page.
	 */
	public EmfdiffExportWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	@Override
	protected boolean validatePage() {
		final boolean result = super.validatePage();
		if (result) {
			final String fileName = getFileName();
			if (fileName.endsWith(FILE_EXTENSION)) {
				setErrorMessage(null);
				return true;
			}
			setErrorMessage("File name must end in '" + FILE_EXTENSION + "'");
			return false;
		}
		return result;
	}
}
