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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.actions;

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.Emfdiff2mpatchActivator;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.WizardLauncher;
import org.eclipse.emf.compare.ui.export.IExportAction;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * This action is a new export action in the EMF Compare GUI (compare editor) and allows transforming the entire emfdiff
 * into an {@link MPatchModel} by calling the wizard.
 * 
 * If only a subset of {@link DiffElement}s should be transformed, {@link DiffElementTransformAction} is more
 * appropriate.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class EmfdiffExportAction implements IExportAction {

	private static final String ACTION_TEXT = MPatchConstants.MPATCH_LONG_NAME;
	private static final String TOOL_TIP_TEXT = "Transform this emfdiff into " + MPatchConstants.MPATCH_LONG_NAME;
	
	private static final String DEFAULT_FILE_NAME = "result." + MPatchConstants.FILE_EXTENSION_MPATCH;

	private static final String DIFF_ICON = "icons/diff_16.gif";
	private static final Image IMAGE;

	static {
		final URL imageURL = Emfdiff2mpatchActivator.getDefault().getBundle().getEntry(DIFF_ICON);
		IMAGE = ImageDescriptor.createFromURL(imageURL).createImage();
	}

	@Override
	public void exportSnapshot(ComparisonSnapshot snapshot) {
		final EObject leftModel = CommonUtils.getModelFromEmfdiff(snapshot, true);
		if (leftModel != null && leftModel.eResource() != null) {
			final IFile mPatchFile = CommonUtils.createNewIFile(leftModel.eResource().getURI(), DEFAULT_FILE_NAME);
			WizardLauncher.openWizard(snapshot, mPatchFile);
		} else {
			WizardLauncher.openWizard(snapshot);
		}
	}

	@Override
	public Image getDisabledImage() {
		return IMAGE;
	}

	@Override
	public Image getEnabledImage() {
		return IMAGE;
	}

	@Override
	public String getText() {
		return ACTION_TEXT;
	}

	@Override
	public String getToolTipText() {
		return TOOL_TIP_TEXT;
	}

}
