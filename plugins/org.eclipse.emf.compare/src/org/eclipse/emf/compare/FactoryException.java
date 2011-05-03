/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
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
 * Factory Exception, indicates that an error occured during an {@link EFactory} operation.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class FactoryException extends Exception {
	/** Serial used for unserialization checks. */
	private static final long serialVersionUID = 51499898584684757L;

	/**
	 * Instantiates a FactoryException given its error message.
	 * 
	 * @param message
	 *            Message associated to the exception.
	 */
	public FactoryException(String message) {
		super(message);
	}

	/**
	 * Instantiates a FactoryException wrapped around another exception.
	 * 
	 * @param exception
	 *            Exception to wrap within this new one.
	 */
	public FactoryException(Throwable exception) {
		super(exception);
	}
}
