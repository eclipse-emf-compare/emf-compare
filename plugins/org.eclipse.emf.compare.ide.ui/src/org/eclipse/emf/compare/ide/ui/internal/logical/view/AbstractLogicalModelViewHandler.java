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
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Abstract implementation of the handling of editors' activations, for the Logical Model View.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class AbstractLogicalModelViewHandler implements ILogicalModelViewHandler {

	/**
	 * This will be called to determine whether an editor must be listened by the logical model view.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor to test.
	 * @return true if the editor must be listened, false otherwise.
	 */
	public boolean canHandle(IWorkbenchPart part) {
		Collection<IFile> files = getFilesFromWorkbenchPart(part);
		for (IFile file : files) {
			if (LogicalModelViewHandlerUtil.isEMFCompareCompliantFile(file)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve the files associated with the given editor (via its {@link IWorkbenchPart}).
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor.
	 * @return the files associated with the given editor (via its {@link IWorkbenchPart}).
	 */
	public Collection<IFile> getFilesFromWorkbenchPart(IWorkbenchPart part) {
		return Collections.emptySet();
	}

	/**
	 * Get the resources computed by the logical model.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor.
	 * @param monitor
	 *            to monitor the process.
	 * @return the resources computed by the logical model.
	 */
	public Collection<IResource> getLogicalModelResources(IWorkbenchPart part, IProgressMonitor monitor) {
		final Collection<IResource> resources = Sets.newLinkedHashSet();
		final Collection<IFile> files = getFilesFromWorkbenchPart(part);

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100).setWorkRemaining(files.size());

		for (IFile file : files) {
			resources.addAll(LogicalModelViewHandlerUtil.getLogicalModelResources(file, subMonitor
					.newChild(1)));
		}
		return resources;
	}

	/**
	 * This will be called to determine whether a selection must be listened by the logical model view.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the {@link ISelection} to test.
	 * @return true if the selection must be listened, false otherwise.
	 */
	public boolean canHandle(IWorkbenchPart part, ISelection selection) {
		Collection<IFile> files = getFilesFromSelection(selection);
		for (IFile file : files) {
			if (LogicalModelViewHandlerUtil.isEMFCompareCompliantFile(file)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve the files associated with the given selection.
	 * 
	 * @param selection
	 *            the {@link ISelection}.
	 * @return the files associated with the given selection.
	 */
	public Collection<IFile> getFilesFromSelection(ISelection selection) {
		return Collections.emptySet();
	}

	/**
	 * Get the resources computed by the logical model.
	 * 
	 * @param selection
	 *            the {@link ISelection}.
	 * @param monitor
	 *            to monitor the process.
	 * @return the resources computed by the logical model.
	 */
	public Collection<IResource> getLogicalModelResources(ISelection selection, IProgressMonitor monitor) {
		final Collection<IResource> resources = Sets.newLinkedHashSet();
		final Collection<IFile> files = getFilesFromSelection(selection);

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100).setWorkRemaining(files.size());

		for (IFile file : files) {
			resources.addAll(LogicalModelViewHandlerUtil.getLogicalModelResources(file, subMonitor
					.newChild(1)));
		}
		return resources;
	}

}
