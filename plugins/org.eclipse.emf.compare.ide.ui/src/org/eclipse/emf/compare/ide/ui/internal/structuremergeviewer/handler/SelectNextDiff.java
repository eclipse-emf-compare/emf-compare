/*******************************************************************************
 * Copyright (c) 2013 Obeo.
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
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler.util.EMFCompareUIHandlerUtil;
import org.eclipse.ui.ISources;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler that manages the select next diff button.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class SelectNextDiff extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object editorInput = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (editorInput instanceof CompareEditorInput) {
			CompareConfiguration configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
			// Select next diff
			EMFCompareUIHandlerUtil.navigate(true, configuration);
		}
		return null;
	}

}
