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
package org.eclipse.emf.compare.ide.ui.internal.logical.view;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Handle, for the Logical Model View, the item(s) selected in Project Explorer View or Package Explorer View.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class ExplorersViewSelectionLMVHandler extends AbstractLogicalModelViewHandler {

	/**
	 * This will be called to determine whether a selection must be listened by the logical model view.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the {@link ISelection} to test.
	 * @return true if the selection must be listened, false otherwise.
	 */
	@Override
	public boolean canHandle(IWorkbenchPart part, ISelection selection) {
		if (part != null
				&& ("Project Explorer".equals(part.getTitle()) || "Package Explorer".equals(part.getTitle()))) { //$NON-NLS-1$ //$NON-NLS-2$
			return super.canHandle(part, selection);
		}
		return false;
	}

	/**
	 * Retrieve the files associated with the given selection.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor.
	 * @param selection
	 *            the {@link ISelection}.
	 * @return the files associated with the given selection.
	 */
	@Override
	public Collection<IFile> getFiles(IWorkbenchPart part, ISelection selection) {
		final Set<IFile> files = Sets.newLinkedHashSet();
		if (selection instanceof TreeSelection) {
			Object element = ((TreeSelection)selection).getFirstElement();
			if (element instanceof IFile) {
				files.add((IFile)element);
			}
		}
		return files;
	}

}
