/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.wizard;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.ModelInputSnapshot;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

/**
 * Wizard used by the "save" action of the {@link ModelStructureMergeViewer}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class SaveDeltaWizard extends BasicNewFileResourceWizard {
	private ModelInputSnapshot input;
	private String fileExtension;
	
	/**
	 * Creates a new file wizard given the file extension to use.
	 * 
	 * @param extension
	 * 			Extension of the file(s) to generate.
	 */
	public SaveDeltaWizard(String extension) {
		super();
		fileExtension = extension;
	}
	
	/**
	 * initalizes the wizard.
	 * 
	 * @param workbench
	 * 			Current workbench.
	 * @param inputSnapshot
	 * 			The current {@link ModelCompareInput}.	
	 */
	public void init(IWorkbench workbench, ModelInputSnapshot inputSnapshot) {
		super.init(workbench, new StructuredSelection());
		input = inputSnapshot;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see BasicNewFileResourceWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		boolean result = false;
		final String page = "newFilePage1"; //$NON-NLS-1$
		if (((WizardNewFileCreationPage)getPage(page)).getFileName().endsWith(fileExtension)) { 
			final IFile createdFile = ((WizardNewFileCreationPage)getPage(page)).createNewFile();
	        if (createdFile != null) {
		        try {
		        	final ModelInputSnapshot modelInputSnapshot = DiffFactory.eINSTANCE.createModelInputSnapshot();
		        	modelInputSnapshot.setDiff(input.getDiff());
		        	modelInputSnapshot.setMatch(input.getMatch());
		        	modelInputSnapshot.setDate(Calendar.getInstance(Locale.getDefault()).getTime());
					ModelUtils.save(modelInputSnapshot, createdFile.getFullPath().toString());
				} catch (IOException e) {
					EMFComparePlugin.getDefault().log(e, false);
				}
				result = true;
	        }
		} else {
			((WizardNewFileCreationPage)getPage(page)).setErrorMessage("The file name must end in " + fileExtension); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see BasicNewFileResourceWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		((WizardNewFileCreationPage)getPage("newFilePage1")) //$NON-NLS-1$
				.setFileName("result." + fileExtension); //$NON-NLS-1$
	}
}
