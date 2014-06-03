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
package org.eclipse.emf.compare.ide.ui.internal.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.SubMonitor;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ThreadSafeProgressMonitor extends ProgressMonitorWrapper {

	/**
	 * @param monitor
	 */
	public ThreadSafeProgressMonitor(IProgressMonitor monitor) {
		super(monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(String name, int totalWork) {
		synchronized(getWrappedProgressMonitor()) {
			super.beginTask(name, totalWork);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#clearBlocked()
	 */
	@Override
	public void clearBlocked() {
		synchronized(getWrappedProgressMonitor()) {
			super.clearBlocked();
		}
	}

	public void setWorkRemaining(int remaining) {
		synchronized(getWrappedProgressMonitor()) {
			if (getWrappedProgressMonitor() instanceof SubMonitor) {
				((SubMonitor)getWrappedProgressMonitor()).setWorkRemaining(remaining);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#done()
	 */
	@Override
	public void done() {
		synchronized(getWrappedProgressMonitor()) {
			super.done();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#internalWorked(double)
	 */
	@Override
	public void internalWorked(double work) {
		synchronized(getWrappedProgressMonitor()) {
			super.internalWorked(work);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		synchronized(getWrappedProgressMonitor()) {
			return super.isCanceled();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#setBlocked(org.eclipse.core.runtime.IStatus)
	 */
	@Override
	public void setBlocked(IStatus reason) {
		synchronized(getWrappedProgressMonitor()) {
			super.setBlocked(reason);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#setCanceled(boolean)
	 */
	@Override
	public void setCanceled(boolean b) {
		synchronized(getWrappedProgressMonitor()) {
			super.setCanceled(b);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#setTaskName(java.lang.String)
	 */
	@Override
	public void setTaskName(String name) {
		synchronized(getWrappedProgressMonitor()) {
			super.setTaskName(name);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#subTask(java.lang.String)
	 */
	@Override
	public void subTask(String name) {
		synchronized(getWrappedProgressMonitor()) {
			super.subTask(name);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#worked(int)
	 */
	@Override
	public void worked(int work) {
		synchronized(getWrappedProgressMonitor()) {
			super.worked(work);
		}
	}

}
