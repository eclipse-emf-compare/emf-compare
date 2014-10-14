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

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Handle, for the Logical Model View, the editors activations and the selections of items.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public interface ILogicalModelViewHandler {

	/**
	 * This will be called to determine whether an editor must be listened by the logical model view.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor to test.
	 * @return true if the editor must be listened, false otherwise.
	 */
	boolean canHandle(IWorkbenchPart part);

	/**
	 * This will be called to determine whether a selection must be listened by the logical model view.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the {@link ISelection} to test.
	 * @return true if the selection must be listened, false otherwise.
	 */
	boolean canHandle(IWorkbenchPart part, ISelection selection);

	/**
	 * Retrieve the files associated with the given editor (via its {@link IWorkbenchPart}).
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor.
	 * @return the files associated with the given editor (via its {@link IWorkbenchPart}).
	 */
	Collection<IFile> getFilesFromWorkbenchPart(IWorkbenchPart part);

	/**
	 * Retrieve the files associated with the given selection.
	 * 
	 * @param selection
	 *            the {@link ISelection}.
	 * @return the files associated with the given selection.
	 */
	Collection<IFile> getFilesFromSelection(ISelection selection);

	/**
	 * Get the resources computed by the logical model.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor.
	 * @param monitor
	 *            to monitor the process.
	 * @return the resources computed by the logical model.
	 */
	Collection<IResource> getLogicalModelResources(IWorkbenchPart part, IProgressMonitor monitor);

	/**
	 * Get the resources computed by the logical model.
	 * 
	 * @param selection
	 *            the {@link ISelection}.
	 * @param monitor
	 *            to monitor the process.
	 * @return the resources computed by the logical model.
	 */
	Collection<IResource> getLogicalModelResources(ISelection selection, IProgressMonitor monitor);
}
