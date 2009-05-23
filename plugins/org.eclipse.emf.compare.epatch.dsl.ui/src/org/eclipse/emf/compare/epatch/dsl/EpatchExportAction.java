/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.epatch.dsl.wizard.EportEpatchWizard;
import org.eclipse.emf.compare.ui.export.IExportAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchExportAction implements IExportAction {

	public void exportSnapshot(ComparisonSnapshot snapshot) {
		EportEpatchWizard w = new EportEpatchWizard(snapshot);
		WizardDialog wd = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), w);
		wd.create();
		wd.open();
	}

	public Image getDisabledImage() {
		return Activator.getImageDescriptor("icons/dtool16/export_epatch_obj.gif").createImage();
	}

	public Image getEnabledImage() {
		return Activator.getImageDescriptor("icons/etool16/export_epatch_obj.gif").createImage();
	}

	public String getText() {
		return "Export as Epatch";
	}

	public String getToolTipText() {
		return "Export the model differences as self-containing, textual Epatch";
	}

}
