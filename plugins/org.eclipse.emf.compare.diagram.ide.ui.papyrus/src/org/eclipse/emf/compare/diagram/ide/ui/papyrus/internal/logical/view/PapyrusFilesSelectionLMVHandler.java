/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.logical.view;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.ide.ui.internal.logical.view.AbstractLogicalModelViewHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.papyrus.infra.onefile.model.IPapyrusFile;
import org.eclipse.papyrus.infra.onefile.model.ISubResourceFile;

/**
 * Handle, for the Logical Model View, the Papyrus files selection.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class PapyrusFilesSelectionLMVHandler extends AbstractLogicalModelViewHandler {

	/**
	 * Retrieve the files associated with the given selection.
	 * 
	 * @param selection
	 *            the {@link ISelection}.
	 * @return the files associated with the given selection.
	 */
	@Override
	public Collection<IFile> getFilesFromSelection(ISelection selection) {
		final Set<IFile> files = Sets.newLinkedHashSet();
		if (selection instanceof TreeSelection) {
			Object element = ((TreeSelection)selection).getFirstElement();
			if (element instanceof IPapyrusFile) {
				files.add(((IPapyrusFile)element).getMainFile());
			} else if (element instanceof ISubResourceFile) {
				files.add(((ISubResourceFile)element).getFile());
			}
		}
		return files;
	}
}
