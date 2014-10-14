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

import static org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil.findFile;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Handle, for the Logical Model View, the EMF Compare editor activations.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EMFCompareEditorLMVHandler extends AbstractLogicalModelViewHandler {

	/**
	 * Retrieve the files associated with the given editor (via its {@link IWorkbenchPart}).
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor.
	 * @return the files associated with the given editor (via its {@link IWorkbenchPart}).
	 */
	@Override
	public Collection<IFile> getFilesFromWorkbenchPart(IWorkbenchPart part) {
		final Set<IFile> files = Sets.newLinkedHashSet();
		if (part instanceof IEditorPart) {
			final IEditorInput editorInput = ((IEditorPart)part).getEditorInput();
			if (editorInput instanceof CompareEditorInput) {
				final Object compareResult = ((CompareEditorInput)editorInput).getCompareResult();
				if (compareResult instanceof ICompareInput) {
					final IFile left = findFile(((ICompareInput)compareResult).getLeft());
					final IFile right = findFile(((ICompareInput)compareResult).getRight());
					final IFile ancestor = findFile(((ICompareInput)compareResult).getAncestor());
					if (left != null) {
						files.add(left);
					}
					if (right != null) {
						files.add(right);
					}
					if (ancestor != null) {
						files.add(ancestor);
					}
				}
			}
		}
		return files;
	}
}
