/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.handler;

import static com.google.common.collect.Maps.newHashMap;

import com.google.common.collect.ImmutableList;

import java.io.File;
import java.io.IOException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler that manages the save of the comparison model.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class SaveComparisonModel extends AbstractHandler {

	/** The dialog buttons. */
	private static final ImmutableList<String> DIALOG_BUTTON_LABELS = ImmutableList.of("Replace", "Cancel");

	/** The compare configuration object used to get the compare model. */
	private CompareConfiguration configuration;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorInput editorInput = HandlerUtil.getActiveEditorInput(event);
		if (editorInput instanceof CompareEditorInput) {
			configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
			Shell parent = configuration.getContainer().getWorkbenchPart().getSite().getShell();

			FileDialog fileDialog = new FileDialog(parent, SWT.SAVE);
			String filePath = fileDialog.open();
			if (filePath != null) {
				File file = new File(filePath);
				if (file.exists()) {
					MessageDialog messageDialog = new MessageDialog(parent, "File already exists", null,
							"File \"" + file.toString()
									+ "\" already exists. Do you want to replace the existing one?",
							MessageDialog.WARNING, DIALOG_BUTTON_LABELS.toArray(new String[0]), 1);
					int open = messageDialog.open();
					if (open == DIALOG_BUTTON_LABELS.indexOf("Replace")) {
						saveComparison(file);
					} // else do nothing
				} else {
					saveComparison(file);
				}
			}
		}

		return null;
	}

	/**
	 * Save the compare model in the given result {@link File}.
	 * 
	 * @param file
	 *            the result {@link File}.
	 */
	private void saveComparison(File file) {
		Comparison comparison = (Comparison)configuration.getProperty(EMFCompareConstants.COMPARE_RESULT);
		Resource resource = new XMIResourceImpl(URI.createFileURI(file.getAbsolutePath()));
		Copier copier = new Copier(false);
		EObject comparisonCopy = copier.copy(comparison);
		copier.copyReferences();

		resource.getContents().add(comparisonCopy);
		try {
			resource.save(newHashMap());
		} catch (IOException e) {
			EMFCompareRCPUIPlugin.getDefault().log(e);
		}
	}

}
