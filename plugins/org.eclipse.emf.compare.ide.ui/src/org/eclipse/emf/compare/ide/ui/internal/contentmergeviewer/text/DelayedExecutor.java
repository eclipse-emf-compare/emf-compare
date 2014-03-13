/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.text;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
class DelayedExecutor {

	private static final int DELAY = 500;

	private final ScheduledExecutorService executor;

	private Future<?> currentFuture;

	DelayedExecutor(ScheduledExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void schedule(Runnable run) {
		if (currentFuture != null && !currentFuture.isDone()) {
			currentFuture.cancel(false);
		}
		final ScheduledFuture<?> future = executor.schedule(run, DELAY, TimeUnit.MILLISECONDS);
		currentFuture = future;
	}
}
