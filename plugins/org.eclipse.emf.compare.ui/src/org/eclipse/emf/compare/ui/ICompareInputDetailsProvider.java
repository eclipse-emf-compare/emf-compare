/*******************************************************************************
 * Copyright (c) 2010, 2012 Geensys and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stephan Eberle - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This interface is responsible for providing compared resources to the EMF Compare core.
 * 
 * @author <a href="mailto:stephan.eberle@geensys.com">Stephan Eberle</a>
 * @since 1.1
 */
public interface ICompareInputDetailsProvider {

	/**
	 * Returns the left compared resource.
	 * 
	 * @return The left compared resource.
	 */
	Resource getLeftResource();

	/**
	 * Returns the right compared resource.
	 * 
	 * @return The right compared resource.
	 */
	Resource getRightResource();

	/**
	 * Returns the compared resources ancestor.
	 * 
	 * @return The compared resources ancestor.
	 */
	Resource getAncestorResource();

	/**
	 * Returns the compare input we've initially been fed.
	 * 
	 * @return The initial compare input.
	 */
	ICompareInput getCompareInput();
}
