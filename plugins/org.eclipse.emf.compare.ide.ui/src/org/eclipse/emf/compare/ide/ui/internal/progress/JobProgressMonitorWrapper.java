/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.progress;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ProgressMonitorWrapper;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class JobProgressMonitorWrapper extends ProgressMonitorWrapper {

	private final JobProgressInfoComposite progressInfoItem;

	private int totalWork;

	private double worked;

	/**
	 * @param monitor
	 */
	public JobProgressMonitorWrapper(IProgressMonitor monitor, JobProgressInfoComposite progressInfoItem) {
		super(monitor);
		this.progressInfoItem = progressInfoItem;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(String name, int tota) {
		this.totalWork = tota;
		this.worked = 0;
		this.progressInfoItem.init();
		this.progressInfoItem.setTaskName(name);
		super.beginTask(name, tota);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#setTaskName(java.lang.String)
	 */
	@Override
	public void setTaskName(String name) {
		this.progressInfoItem.setPercentDone(getPercentDone());
		super.setTaskName(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#subTask(java.lang.String)
	 */
	@Override
	public void subTask(String name) {
		this.progressInfoItem.setTaskName(name);
		super.subTask(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ProgressMonitorWrapper#worked(int)
	 */
	@Override
	public void worked(int work) {
		this.worked += work;
		int percentDone = getPercentDone();
		this.progressInfoItem.setPercentDone(percentDone);
		super.worked(work);
	}

	private int getPercentDone() {
		return Math.min((int)(worked * 100 / totalWork), 100);
	}
}
