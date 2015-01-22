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
package org.eclipse.emf.compare;

/**
 * Exception used to manage cancellation of comparison operations.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.2
 */
public class ComparisonCanceledException extends RuntimeException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -3584412451390015285L;

	/**
	 * Default constructor.
	 */
	public ComparisonCanceledException() {
		super();
	}
}
