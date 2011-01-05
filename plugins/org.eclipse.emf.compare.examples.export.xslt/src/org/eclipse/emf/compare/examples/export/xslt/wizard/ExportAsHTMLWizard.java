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
package org.eclipse.emf.compare.examples.export.xslt.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.examples.export.xslt.XSLTExportPlugin;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

/**
 * Wizard used by the "export as html" action. It will transform the emfdiff result of a comparison to an HTML
 * file according to xslt/EMF-DIFF2HTML.xslt.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ExportAsHTMLWizard extends BasicNewFileResourceWizard {
	/** Result of the comparison this wizard is meant to export. */
	private ComparisonSnapshot input;

	/**
	 * initalizes the wizard.
	 * 
	 * @param workbench
	 *            Current workbench.
	 * @param inputSnapshot
	 *            The {@link ModelInputSnapshot} to export.
	 */
	public void init(IWorkbench workbench, ComparisonSnapshot inputSnapshot) {
		super.init(workbench, new StructuredSelection());
		// ensures no modification will be made to the input
		input = (ComparisonResourceSnapshot)EcoreUtil.copy(inputSnapshot);
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

		final String fileName = ((WizardNewFileCreationPage)getPage(page)).getFileName();
		if (!fileName.endsWith(".html")) { //$NON-NLS-1$
			((WizardNewFileCreationPage)getPage(page)).setFileName(fileName + ".html"); //$NON-NLS-1$
		}

		final IFile createdFile = ((WizardNewFileCreationPage)getPage(page)).createNewFile();
		if (createdFile != null) {
			try {
				final File xslTransform = new File(FileLocator.toFileURL(
						XSLTExportPlugin.getDefault().getBundle().getEntry(
								"templates/xslt/EMF-DIFF2HTML.xslt")).getPath()); //$NON-NLS-1$
				final File htmlResult = new File(createdFile.getLocationURI());
				final BufferedReader emfdiffReader = new BufferedReader(new StringReader(ModelUtils
						.serialize(input)));

				final TransformerFactory factory = TransformerFactory.newInstance();
				final Transformer htmlTransformer = factory.newTransformer(new StreamSource(xslTransform));
				htmlTransformer.transform(new StreamSource(emfdiffReader), new StreamResult(htmlResult));
				emfdiffReader.close();
			} catch (final IOException e) {
				EMFComparePlugin.log(e, false);
			} catch (final TransformerConfigurationException e) {
				EMFComparePlugin.log(e, false);
			} catch (final TransformerException e) {
				EMFComparePlugin.log(e, false);
			}
			result = true;
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
				.setFileName("result.html"); //$NON-NLS-1$
	}
}
