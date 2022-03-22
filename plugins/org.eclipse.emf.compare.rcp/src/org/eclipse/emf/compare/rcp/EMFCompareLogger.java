/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - add scope of a comparison to its adapters, progress reporting
 *******************************************************************************/
package org.eclipse.emf.compare.rcp;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Mimics the Log4J 1 logger interface but sends all logging to the Eclipse log. This allows us to remove
 * Log4J 1 with limited impacts.
 * 
 * @author lgoubet
 */
public class EMFCompareLogger {

	private static Level level;

	private Class<?> caller;

	public EMFCompareLogger(Class<?> caller) {
		this.caller = caller;
	}

	public boolean isInfoEnabled() {
		return level.compareTo(Level.INFO) >= 0;
	}

	public boolean isDebugEnabled() {
		return level.compareTo(Level.DEBUG) >= 0;
	}

	public static void setLevel(String newLevel) {
		if (newLevel == null) {
			level = Level.OFF;
			return;
		}
		String upper = newLevel.toUpperCase();
		switch (upper) {
			case "INFO": //$NON-NLS-1$
				level = Level.INFO;
				break;
			case "DEBUG": //$NON-NLS-1$
				level = Level.DEBUG;
				break;
			case "OFF": //$NON-NLS-1$
				// Fall-through
			default:
				level = Level.OFF;
		}
	}

	public void info(String message) {
		EMFCompareRCPPlugin.getDefault().getLog().log(
				new Status(IStatus.INFO, EMFCompareRCPPlugin.PLUGIN_ID, caller.getName() + " - " + message)); //$NON-NLS-1$
	}

	public void debug(String message) {
		// Eclipse has no "debug" level.
		info(message);
	}

	public enum Level {
		OFF, INFO, DEBUG;
	}

}
