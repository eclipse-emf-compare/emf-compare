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
package org.eclipse.emf.compare.util;

/**
 * Factory Exception, indicates that an error occured during an {@link EFactory} operation.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class FactoryException extends Exception {
	/** Serial used for unserialization checks. */
	private static final long serialVersionUID = 51499898584684757L;

	/**
	 * Instantiates a {@link FactoryException} given its error message.
	 * 
	 * @param message
	 *            Message associated to the exception.
	 */
	public FactoryException(String message) {
		super(message);
	}
}
