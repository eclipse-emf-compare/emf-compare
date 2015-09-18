/******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent Goubet <laurent.goubet@obeo.fr> - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.ui.internal.actions;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.UIPreferences;
import org.eclipse.egit.ui.internal.actions.MergeToolActionHandler;
import org.eclipse.egit.ui.internal.merge.MergeModeDialog;
import org.eclipse.emf.compare.egit.ui.internal.merge.ModelGitMergeEditorInput;
import org.eclipse.jface.window.Window;

/**
 * This class extend EGit ModelMergeToolActionHandler in order to give our own editor input.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public class ModelMergeToolActionHandler extends MergeToolActionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		int mergeMode = Activator.getDefault().getPreferenceStore().getInt(UIPreferences.MERGE_MODE);
		IPath[] locations = getSelectedLocations(event);
		CompareEditorInput input;
		if (mergeMode == 0) {
			MergeModeDialog dlg = new MergeModeDialog(getShell(event));
			if (dlg.open() != Window.OK) {
				return null;
			}
			input = new ModelGitMergeEditorInput(dlg.useWorkspace(), locations);
		} else {
			boolean useWorkspace = mergeMode == 1;
			input = new ModelGitMergeEditorInput(useWorkspace, locations);
		}
		CompareUI.openCompareEditor(input);
		return null;
	}

}
