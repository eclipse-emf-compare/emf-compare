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
package org.eclipse.emf.compare.rcp.internal.tracer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;

/**
 * Store constant used to display or not a trace.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public final class TracingConstant {

	/**
	 * If true trace stuff about module wrapping.
	 */
	public static final boolean CONFIGURATION_TRACING_ACTIVATED = EMFCompareRCPPlugin.getDefault()
			.isDebugging()
			&& "true".equalsIgnoreCase(Platform //$NON-NLS-1$ // Not in a constant to avoid checkstyke error.
					.getDebugOption("org.eclipse.emf.compare.rcp/debug/comparisonConfiguration")); //$NON-NLS-1$

	/**
	 * Private constructor. Not to be call.
	 */
	private TracingConstant() {
		throw new AssertionError("Not to be called"); //$NON-NLS-1$
	}

}
