/*******************************************************************************
 * Copyright (c) 2006, Obeo.
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
 * Factory Exception.
 * @author www.obeo.fr
 *
 */
public class FactoryException extends Exception {

	private static final long serialVersionUID = 1;

	/**
	 * Constructor.
	 * @param message is the message
	 */
	public FactoryException(String message){
		super(message);
	}

}
