/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class, controls the plug-in life cycle.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EMFComparePlugin extends Plugin {
	/** The plugin ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare"; //$NON-NLS-1$

	// The shared instance
	private static EMFComparePlugin plugin;

	/**
	 * Default constructor.
	 */
	public EMFComparePlugin() {
		plugin = this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Plugin#start(BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Plugin#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the plugin's shared instance.
	 * 
	 * @return The plugin's shared instance.
	 */
	public static EMFComparePlugin getDefault() {
		return plugin;
	}

	/**
	 * Trace an Exception in the error log.
	 * 
	 * @param e
	 *            Exception to log.
	 * @param blocker
	 *            <code>True</code> if the exception must be logged as error, <code>False</code> to log it
	 *            as a warning.
	 */
	public static void log(Exception e, boolean blocker) {
		e.printStackTrace();
		if (e instanceof CoreException) {
			final IStatus status = ((CoreException)e).getStatus();
			log(new Status(status.getSeverity(), PLUGIN_ID, status.getCode(), status.getMessage(), status
					.getException()));
		} else if (e instanceof NullPointerException) {
			int severity = IStatus.WARNING;
			if (blocker)
				severity = IStatus.ERROR;
			log(new Status(severity, PLUGIN_ID, severity, "Required element not found", e)); //$NON-NLS-1$
		} else {
			int severity = IStatus.WARNING;
			if (blocker)
				severity = IStatus.ERROR;
			log(new Status(severity, PLUGIN_ID, severity, "A java exception has been thrown", e)); //$NON-NLS-1$
		}
	}

	/**
	 * Puts the given message in the error log view, as error or warning.
	 * 
	 * @param message
	 *            The message to put in the error log view.
	 * @param blocker
	 *            <code>True</code> if the exception must be logged as error, <code>False</code> to log it
	 *            as a warning.
	 */
	public static void log(String message, boolean blocker) {
		int severity = IStatus.WARNING;
		if (blocker)
			severity = IStatus.ERROR;
		String errorMessage = "Unknown EMF Compare problem"; //$NON-NLS-1$
		if (message != null)
			errorMessage = message.trim().replaceFirst("\n", ";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		log(new Status(severity, PLUGIN_ID, severity, errorMessage, null));
	}

	/**
	 * Puts the given status in the error log view.
	 * 
	 * @param status
	 *            Error Status.
	 */
	public static void log(IStatus status) {
		if (getDefault() != null) {
			getDefault().getLog().log(status);
		} else {
			throw new EMFCompareException(status.getException());
		}
	}
}
