/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.internal;

import java.io.IOException;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This abstract base class will be used to implement team handlers.
 * <p>
 * A Team Handler basically provides a way to load resources given a compare input created by a given team
 * plug-in.
 * </p>
 * <p>
 * An example where such an handler is required is the comparison of models issued from XSD schemas by the
 * subversive plug-in : if no specific handler is used, merging differences from those models will not be
 * possible.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractTeamHandler {
	/** This will hold the ancestor resource. */
	protected Resource ancestorResource;

	/** This will hold the left resource. */
	protected Resource leftResource;

	/** This will hold the right resource. */
	protected Resource rightResource;

	/**
	 * This will be used by external classes when access to the ancestor resource is needed.
	 * 
	 * @return The ancestor compared resource.
	 */
	public Resource getAncestorResource() {
		return ancestorResource;
	}

	/**
	 * This will be used by external classes when access to the left resource is needed.
	 * 
	 * @return The left compared resource.
	 */
	public Resource getLeftResource() {
		return leftResource;
	}

	/**
	 * This will be used by external classes when access to the right resource is needed.
	 * 
	 * @return The right compared resource.
	 */
	public Resource getRightResource() {
		return rightResource;
	}

	/**
	 * This should be overriden to return <code>false</code> if the left loaded resource is local in order
	 * to enable the "copy right to left" merging actions.
	 * 
	 * @return <code>true</code> if the left loaded resource is remote, <code>false</code> if is is a
	 *         local resource.
	 */
	public boolean isLeftRemote() {
		return true;
	}

	/**
	 * This should be overriden to return <code>false</code> if the right loaded resource is local in order
	 * to enable the "copy left to right" merging actions.
	 * 
	 * @return <code>true</code> if the right loaded resource is remote, <code>false</code> if is is a
	 *         local resource.
	 */
	public boolean isRightRemote() {
		return true;
	}

	/**
	 * This will load the resources this <code>input</code> holds if possible.
	 * <p>
	 * Implementing classes should load all three resources using the fields (left|right|ancestor)Resource.
	 * Resources need not be manually unloaded.
	 * </p>
	 * <p>
	 * If the returned value is <code>false</code>, we'll simply try another team handler.
	 * </p>
	 * 
	 * @param input
	 *            CompareInput which holds the resources to be loaded.
	 * @return <code>true</code> if the given models have been successfully loaded, <code>false</code>
	 *         otherwise.
	 * @throws IOException
	 *             Can be thrown if resource loading fails.
	 * @throws CoreException
	 *             Can be thrown if resource loading fails.
	 */
	public abstract boolean loadResources(ICompareInput input) throws IOException,
			CoreException;
}
