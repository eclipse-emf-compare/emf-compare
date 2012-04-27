/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.logical.extension;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implementations of this interface can be used in order to tell EMF Compare how to resolve all of the
 * Logical model corresponding to a given {@link IFile}.
 * <p>
 * The main reason for this is that there is no generic way to find the parent resource of a controlled
 * fragment if this fragment does not reference its parent explicitely. If "a.ecore" contains "b.ecore", we
 * cannot find "a.ecore" from "b.ecore" without loading every single model of the workspace and trying to
 * determine whether they contains links to "b.ecore".
 * </p>
 * <p>
 * The generic algorithm used by EMF Compare is a simple call to
 * {@link org.eclipse.emf.ecore.util.EcoreUtil#resolveAll(Resource)} on the resource containing the selected
 * IFile. This only allows us to find the children fragments of the selected file, not its parents. If clients
 * of the API need a better search for the parent, they should implement this and extend the
 * <code>org.eclipse.emf.compare.modelResolver</code> extension point.
 * </p>
 * <p>
 * This extension point will only be used in order to resolve the <b>local</b> resource set. When this is
 * done, we will determine which of the resolved resources is the root of the containment tree, and use it to
 * resolve the remote resource sets. We assume that a model spanning three resources in the workspace won't
 * span more than those three in the remote.
 * </p>
 * <p>
 * Take note that the very first model resolver which enablement is <code>true</code> for a given resource
 * will be used, and the framework will discard others.
 * </p>
 * <p>
 * See also {@link ScopedModelResolver} for an example implementation that seeks through all the base
 * resource's containing project for cross referencing resources that would be part of its logical model.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see org.eclipse.emf.compare.ide.logical.extension.ScopedModelResolver
 */
public interface IModelResolver {
	/**
	 * This will be called by EMF Compare in order to resolve the whole logical model containing the given
	 * Resource. <em>eResource</em> can be either the logical model root or a leaf of its containment
	 * hierarchy.
	 * 
	 * @param iFile
	 *            The file for which we seek the full logical model.
	 * @param eResource
	 *            The EMF {@link Resource} contained by the given <em>file</em>.
	 */
	void resolve(IFile iFile, Resource eResource);
}
