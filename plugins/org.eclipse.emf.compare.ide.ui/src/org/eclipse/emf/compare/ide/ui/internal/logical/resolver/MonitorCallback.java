/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.util.concurrent.FutureCallback;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;

/**
 * Callback that updates a progress monitor when called. Also owns a diagnostic that gets update if this is
 * called upon failure.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
final class MonitorCallback implements FutureCallback<Object> {

	/** The progress monitor. */
	private final ThreadSafeProgressMonitor monitor;

	/** The diagnostic. */
	private final DiagnosticSupport diagnostic;

	/**
	 * Constructor.
	 * 
	 * @param diagnostic
	 *            The diagnostic.
	 * @param monitor
	 *            The monitor.
	 */
	MonitorCallback(DiagnosticSupport diagnostic, ThreadSafeProgressMonitor monitor) {
		this.diagnostic = checkNotNull(diagnostic);
		this.monitor = checkNotNull(monitor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSuccess(Object o) {
		if (!ResolutionUtil.isInterruptedOrCanceled(monitor)) {
			// do not report progress anymore when the task has been interrupted of canceled.
			// It speeds up the cancellation.
			monitor.worked(1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onFailure(final Throwable t) {
		if (!ResolutionUtil.isInterruptedOrCanceled(monitor)) {
			// do not report progress or errors anymore when the task has been interrupted of
			// canceled. It speeds up the cancellation.
			monitor.worked(1);
			diagnostic.merge(BasicDiagnostic.toDiagnostic(t));
		}
	}
}
