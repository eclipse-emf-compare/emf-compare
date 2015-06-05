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

import org.eclipse.emf.compare.ide.internal.utils.IProxyCreationListener;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;

/**
 * Asbtract super-class of {@link IProxyCreationListener}s to use for computing the logical model.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractMonitoredProxyCreationListener implements IProxyCreationListener {

	/** The progress monitor. */
	protected final ThreadSafeProgressMonitor tspm;

	/** The diagnostic. */
	protected final DiagnosticSupport diagnostic;

	/**
	 * Constructor.
	 * 
	 * @param monitor
	 *            The monitor to use, must not be {@code null}
	 * @param diagnostic
	 *            The diagnostic, must not be {@code null}
	 */
	public AbstractMonitoredProxyCreationListener(ThreadSafeProgressMonitor monitor,
			DiagnosticSupport diagnostic) {
		this.tspm = checkNotNull(monitor);
		this.diagnostic = checkNotNull(diagnostic);
	}

}
