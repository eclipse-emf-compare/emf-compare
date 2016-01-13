/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.wrapper;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 * Wraps a JGit ProgressMonitor in an object conforming to IProgressMonitor.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class JGitProgressMonitorWrapper implements IProgressMonitor {

	/** The instance of ProgressMonitor. */
	private ProgressMonitor jgitMonitor;

	/** A boolean to know if cancel has been requested by user. */
	private boolean isCanceled;

	/**
	 * The constructor.
	 * 
	 * @param monitor
	 *            An instance of JGit ProgressMonitor
	 */
	public JGitProgressMonitorWrapper(ProgressMonitor monitor) {
		Assert.isNotNull(monitor);
		jgitMonitor = monitor;
	}

	/**
	 * Bind IProgressMonitor beginTask method with ProgressMonitor equivalent.
	 * 
	 * @param name
	 *            The name of the task
	 * @param totalWork
	 *            The total amount of work for this task
	 * @see IProgressMonitor#beginTask(String, int)
	 */
	public void beginTask(String name, int totalWork) {
		jgitMonitor.beginTask(name, totalWork);
	}

	/**
	 * Bind IProgressMonitor done method with ProgressMonitor equivalent.
	 * 
	 * @see IProgressMonitor#done()
	 */
	public void done() {
		jgitMonitor.endTask();
	}

	/**
	 * Bind IProgressMonitor internalWorked method with ProgressMonitor equivalent.
	 * 
	 * @param work
	 *            The amount of work done
	 * @see IProgressMonitor#internalWorked(double)
	 */
	public void internalWorked(double work) {
		jgitMonitor.update((int)work);
	}

	/**
	 * Bind IProgressMonitor isCanceled method with ProgressMonitor equivalent.
	 * 
	 * @see IProgressMonitor#isCanceled()
	 * @return true if the action is canceled
	 */
	public boolean isCanceled() {
		if (isCanceled) {
			return isCanceled;
		}
		return jgitMonitor.isCancelled();
	}

	/**
	 * Bind IProgressMonitor setCanceled method with ProgressMonitor equivalent.
	 * 
	 * @param value
	 *            <code>true</code> if the task has been canceled
	 * @see IProgressMonitor#setCanceled(boolean)
	 */
	public void setCanceled(boolean value) {
		isCanceled = value;
	}

	/**
	 * Bind IProgressMonitor setTaskName method with ProgressMonitor equivalent.
	 * 
	 * @param name
	 *            The name of the task
	 * @see IProgressMonitor#setTaskName(String)
	 */
	public void setTaskName(String name) {
		// This value cannot be transmitted to JGit ProgressMonitor
	}

	/**
	 * Bind IProgressMonitor subTask method with ProgressMonitor equivalent.
	 * 
	 * @param name
	 *            The name of the subtask
	 * @see IProgressMonitor#subTask(String)
	 */
	public void subTask(String name) {
		// This value cannot be transmitted to JGit ProgressMonitor
	}

	/**
	 * Bind IProgressMonitor worked method with ProgressMonitor equivalent.
	 * 
	 * @param work
	 *            The amount of work done
	 * @see IProgressMonitor#worked(int)
	 */
	public void worked(int work) {
		internalWorked(work);
	}

}
