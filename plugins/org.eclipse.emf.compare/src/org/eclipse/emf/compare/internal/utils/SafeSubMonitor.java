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
package org.eclipse.emf.compare.internal.utils;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;

/**
 * An EMF {@link Monitor} that can be safely passed to clients because it will ignore calls to
 * {@code worked(int)} and {@code done()} to allow the caller to master the number of ticks consumed, whatever
 * the clients do with the monitor. Such a monitor allows clients to cancel an operation while preventing them
 * to negatively (or positively alas) impact the progress report.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("unused")
// implements Monitor required for Juno and older
public class SafeSubMonitor extends BasicMonitor.Delegating implements Monitor {
	/**
	 * Constructor.
	 * 
	 * @param monitor
	 *            The wrapped monitor.
	 */
	public SafeSubMonitor(Monitor monitor) {
		super(monitor);
	}

	@Override
	public void worked(int work) {
		// Does nothing on purpose
	}

	@Override
	public void done() {
		// Does nothing on purpose
	}
}
