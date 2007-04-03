/**
 * <copyright> 
 *
 * Copyright (c) 2006-2007 Obeo.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Obeo - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: EMFComparePlugin.java,v 1.1 2007/04/03 06:43:23 cbrun Exp $
 */
package org.eclipse.emf.compare;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EMFComparePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.emf.compare";

	// The shared instance
	private static EMFComparePlugin plugin;
	
	/**
	 * The constructor
	 */
	public EMFComparePlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EMFComparePlugin getDefault() {
		return plugin;
	}
	
	public void log(Exception e,boolean blocker)
	{
		if (e instanceof CoreException) {
			IStatus status = ((CoreException) e).getStatus();
			log(new Status(status.getSeverity(), PLUGIN_ID, status.getCode(), status.getMessage(), status.getException()));
		}  else if (e instanceof NullPointerException) {
			int severity = (blocker) ? IStatus.ERROR : IStatus.WARNING;
			log(new Status(severity, PLUGIN_ID, severity, "Required element not found", e));
		} else {
			int severity = (blocker) ? IStatus.ERROR : IStatus.WARNING;
			log(new Status(severity, PLUGIN_ID, severity, "A java exception has been thrown", e));
		}
	}
	
	/**
	 * Puts the given message in the error log view, as error or warning.
	 * 
	 * @param message
	 *            is the message to put in the error log view
	 * @param blocker
	 *            is the severity : (blocker)? IStatus.ERROR : IStatus.WARNING
	 */
	public void log(String message, boolean blocker) {
		int severity = (blocker) ? IStatus.ERROR : IStatus.WARNING;
		log(new Status(severity, PLUGIN_ID, severity, ((message != null) ? message.trim().replaceFirst("\n", ";\n") : "Unknown EMF Compare problem"), null));
	}

	/**
	 * Puts the given status in the error log view.
	 * 
	 * @param status
	 *            is the status
	 */
	public void log(IStatus status) {
		getLog().log(status);
	}
	

}
