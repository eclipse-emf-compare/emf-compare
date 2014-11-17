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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Abstract implementation of the handling of editors' activations, for the Logical Model View.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class AbstractLogicalModelViewHandler implements ILogicalModelViewHandler {

	/**
	 * This will be called to determine whether the given editor or the given selection must be listened by
	 * the logical model view.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the {@link ISelection} to test.
	 * @return true if the editor or the selection must be listened, false otherwise.
	 */
	public boolean canHandle(IWorkbenchPart part, ISelection selection) {
		Collection<IFile> files = getFiles(part, selection);
		for (IFile file : files) {
			if (LogicalModelViewHandlerUtil.isEMFCompareCompliantFile(file)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve the files associated with the given editor (via its {@link IWorkbenchPart}) or the given
	 * selection.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the {@link ISelection}.
	 * @return the files associated with the given editor or the given selection.
	 */
	public Collection<IFile> getFiles(IWorkbenchPart part, ISelection selection) {
		return Collections.emptySet();
	}

	/**
	 * Get the logical models associated with the given editor or selection.
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the {@link ISelection}.
	 * @param monitor
	 *            to monitor the process.
	 * @return the logical models associated with the given editor or selection.
	 */
	public Collection<SynchronizationModel> getSynchronizationModels(IWorkbenchPart part,
			ISelection selection, IProgressMonitor monitor) {
		final Collection<SynchronizationModel> models = Sets.newLinkedHashSet();
		final Collection<IFile> files = getFiles(part, selection);

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100).setWorkRemaining(files.size());

		for (IFile file : files) {
			models.addAll(LogicalModelViewHandlerUtil.getSynchronizationModels(file, subMonitor.newChild(1)));
		}
		return models;
	}

}
