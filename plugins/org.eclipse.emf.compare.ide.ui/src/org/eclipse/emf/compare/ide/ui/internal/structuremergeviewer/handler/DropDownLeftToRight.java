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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler that manages the click on the menu item left to right in the dropdown menu of the toolbar of the
 * structure merge viewer.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DropDownLeftToRight extends AbstractHandler {

	/** The compare configuration object. */
	private CompareConfiguration configuration;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object editorInput = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (editorInput instanceof CompareEditorInput) {
			configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
			configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(true));
		}

		ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(
				ICommandService.class);
		commandService.refreshElements("org.eclipse.emf.compare.ide.ui.dropdown", null); //$NON-NLS-1$

		return null;
	}
}
