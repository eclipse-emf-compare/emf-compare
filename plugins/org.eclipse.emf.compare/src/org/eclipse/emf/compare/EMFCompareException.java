/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
 * Exception thrown when a comparison launched from the GUI fails.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class EMFCompareException extends RuntimeException {
	/** Serial used for unserialization checks. */
	private static final long serialVersionUID = 5727212239745736313L;

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * 
	 * @param message
	 *            The detail message. The detail message is saved for later retrieval by the
	 *            {@link #getMessage()} method.
	 */
	public EMFCompareException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception wrapped around another exception.
	 * 
	 * @param exception
	 *            Exception to wrap within this new one.
	 */
	public EMFCompareException(Throwable exception) {
		super(exception);
	}
}
