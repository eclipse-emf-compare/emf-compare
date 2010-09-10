/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: BindingPlugin.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.mpatch.provider.MPatchEditPlugin;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;


/**
 * This is the central singleton for the Binding edit plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class BindingPlugin extends EMFPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.mpatch.binding";
	
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final BindingPlugin INSTANCE = new BindingPlugin();

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * Create the instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingPlugin() {
		super
		  (new ResourceLocator [] {
		     EcoreEditPlugin.INSTANCE,
		     MPatchEditPlugin.INSTANCE,
		   });
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static class Implementation extends EclipsePlugin {
		/**
		 * Creates an instance.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}
		
		////////////// SOME LOGGING
		
		public void logError(String error) {
			logError(error, null);
		}

		public void logError(String error, Throwable throwable) {
			if (error == null && throwable != null) {
				error = throwable.getMessage();
			}
			getLog().log(
					new Status(IStatus.ERROR, PLUGIN_ID,
							IStatus.OK, error, throwable));
			debug(error, throwable);
		}

		public void logInfo(String message) {
			logInfo(message, null);
		}

		public void logInfo(String message, Throwable throwable) {
			if (message == null && throwable != null) {
				message = throwable.getMessage();
			}
			getLog().log(
					new Status(IStatus.INFO, PLUGIN_ID,
							IStatus.OK, message, throwable));
			debug(message, throwable);
		}
		
		public void logWarning(String message) {
			logWarning(message, null);
		}

		public void logWarning(String message, Throwable throwable) {
			if (message == null && throwable != null) {
				message = throwable.getMessage();
			}
			getLog().log(
					new Status(IStatus.WARNING, PLUGIN_ID,
							IStatus.OK, message, throwable));
			debug(message, throwable);
		}
		
		private void debug(String message, Throwable throwable) {
			if (!isDebugging()) {
				return;
			}
			if (message != null) {
				System.err.println(message);
			}
			if (throwable != null) {
				throwable.printStackTrace();
			}
		}

	}

}
