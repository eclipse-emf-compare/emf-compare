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
package org.eclipse.emf.compare.examples.export.library.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Overrides the default behavior of a wizard page to specify validation rules.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class LibraryExportWizardPage extends WizardNewFileCreationPage {
	/**
	 * Instantiates a wizard page given its name and the current selection.
	 * 
	 * @param pageName
	 *            Name of the page that is to be created.
	 * @param selection
	 *            Current resource selection.
	 */
	public LibraryExportWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	@Override
	protected boolean validatePage() {
		boolean result = super.validatePage();
		if (result) {
			final String fileName = getFileName();
			if (fileName.endsWith(".html")) { //$NON-NLS-1$
				setErrorMessage(null);
				return true;
			}
			setErrorMessage("File name must end in '.html'");
			return false;
		}
		// This will return false
		return result;
	}
}
