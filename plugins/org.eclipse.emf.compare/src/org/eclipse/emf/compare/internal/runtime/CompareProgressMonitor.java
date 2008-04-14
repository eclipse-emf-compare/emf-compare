/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.runtime;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Encapsulates an {@link org.eclipse.core.runtime.IProgressMonitor IProgressMonitor} to allow us to run out
 * of eclipse where no progress monitors are accessible.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class CompareProgressMonitor {
	/** Progress monitor wrapped within this instance. Can be <code>null</code>. */
	private Object progressMonitor;

	/**
	 * Allows the default constructor to be used. This will have no progress monitor set.
	 */
	public CompareProgressMonitor() {
		super();
	}

	/**
	 * Encapsulates the given progress monitor.
	 * 
	 * @param theProgressMonitor
	 *            Progress monitor to wrap within this instance. Can be <code>null</code>.
	 */
	public CompareProgressMonitor(Object theProgressMonitor) {
		progressMonitor = theProgressMonitor;
	}

	/**
	 * Notifies the wrapped progress monitor that a task begins.
	 * 
	 * @param name
	 *            Name of the beginning task.
	 * @param totalWork
	 *            Total number of work units.
	 */
	public void beginTask(String name, int totalWork) {
		if (progressMonitor != null)
			((IProgressMonitor)progressMonitor).beginTask(name, totalWork);
	}

	/**
	 * Notifies the wrapped progress monitor that a task has been canceled.
	 * 
	 * @return <code>True</code> if cancellation has been requested, and <code>False</code> otherwise
	 */
	public boolean isCanceled() {
		if (progressMonitor != null)
			return ((IProgressMonitor)progressMonitor).isCanceled();
		return false;
	}

	/**
	 * Notifies the wrapped progress monitor that a subtask begins.
	 * 
	 * @param name
	 *            Name of the beginning subtask.
	 */
	public void subTask(String name) {
		if (progressMonitor != null)
			((IProgressMonitor)progressMonitor).subTask(name);
	}

	/**
	 * Notifies the wrapped progress monitor that a task has advanced.
	 * 
	 * @param work
	 *            Number of work units just completed.
	 */
	public void worked(int work) {
		if (progressMonitor != null)
			((IProgressMonitor)progressMonitor).worked(work);
	}
}
