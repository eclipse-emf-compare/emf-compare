/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.extension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of an {@link IModelResolver} can be used to search within a given scope (IProject,
 * IFolder...) containing our base resource for its potential parents.
 * <p>
 * Take note that this implementation is generic, and thus extremely costly.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class ScopedModelResolver implements IModelResolver {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.extension.IModelResolver#resolve(org.eclipse.core.resources.IFile,
	 *      org.eclipse.emf.ecore.resource.Resource)
	 */
	public void resolve(IFile iFile, Resource eResource) {
		// We'll load every "model" resource we find in this temporary resource set.
		ResourceSet temporaryResourceSet = new ResourceSetImpl();

		IContainer container = getScope(iFile);
		try {
			container.accept(new ModelResourceVisitor(temporaryResourceSet));
		} catch (CoreException e) {
			// FIXME log
		}

		/*
		 * We now have a temporary resource set containing all EMF resources of this project. We'll now try
		 * and find all Resources that reference "eResource".
		 */
		URI resourceURI = eResource.getURI();
		Resource baseResource = temporaryResourceSet.getResource(resourceURI, false);
		if (baseResource != null) {
			EcoreUtil.CrossReferencer crossReferencer = new EcoreUtil.CrossReferencer(temporaryResourceSet) {
				private static final long serialVersionUID = 1L;

				{
					crossReference();
				}
			};
			Set<Resource> crossReferencingResources = findCrossReferencingResources(crossReferencer,
					baseResource);

			/*
			 * We now know all resources that reference "baseResource", close or far. These compose the whole
			 * logical model which "baseResource" is a part of. Resolve them all in the base resource set.
			 */
			ResourceSet baseResourceSet = eResource.getResourceSet();
			for (Resource crossReferencing : crossReferencingResources) {
				baseResourceSet.getResource(crossReferencing.getURI(), true);
			}

			// Then, unload every temporary resource we've loaded
			for (Resource temporaryResource : temporaryResourceSet.getResources()) {
				temporaryResource.unload();
			}
			temporaryResourceSet.getResources().clear();
		}

		// Ensure that the resource set is fully resolved
		EcoreUtil.resolveAll(eResource.getResourceSet());
	}

	/**
	 * This will be used to retrieve the "scope" in which this resolver should seek for referencing Resources.
	 * 
	 * @param iFile
	 *            The file containing the "selected Resource" of
	 * @return
	 */
	protected IContainer getScope(IFile iFile) {
		return iFile.getParent();
	}

	/**
	 * This will try and find all Resources that reference <em>baseResource</em> by walking its cross
	 * referencing tree.
	 * 
	 * @param crossReferencer
	 *            Cross referencer initialized on <em>baseResource</em>'s resource set.
	 * @param baseResource
	 *            Resource we seek all cross references of.
	 * @return All Resources that reference <em>baseResource</em>.
	 */
	protected Set<Resource> findCrossReferencingResources(EcoreUtil.CrossReferencer crossReferencer,
			Resource baseResource) {
		Set<Resource> crossReferencingResources = new HashSet<Resource>();

		for (EObject root : baseResource.getContents()) {
			crossReferencingResources.addAll(findCrossReferencingResources(crossReferencer, root));

			Iterator<EObject> rootContent = root.eAllContents();
			while (rootContent.hasNext()) {
				crossReferencingResources.addAll(findCrossReferencingResources(crossReferencer,
						rootContent.next()));
			}
		}

		// We also need to walk up the cross referencing tree of those cross referencing resources
		for (Resource crossReferencing : crossReferencingResources) {
			crossReferencingResources
					.addAll(findCrossReferencingResources(crossReferencer, crossReferencing));
		}

		return crossReferencingResources;
	}

	/**
	 * This will try and find all Resources that reference <em>object</em> by walking its cross referencing
	 * tree.
	 * 
	 * @param crossReferencer
	 *            Cross referencer initialized on <em>object</em>'s resource set.
	 * @param object
	 *            The object we seek all cross references of.
	 * @return All Resources that reference <em>object</em>.
	 */
	protected Set<Resource> findCrossReferencingResources(EcoreUtil.CrossReferencer crossReferencer,
			EObject object) {
		Set<Resource> crossReferencingResources = new HashSet<Resource>();

		Resource baseResource = object.eResource();
		Collection<EStructuralFeature.Setting> crossReferences = crossReferencer.get(object);
		if (crossReferences != null) {
			Iterator<EStructuralFeature.Setting> crossReferenceIterator = crossReferences.iterator();
			while (crossReferenceIterator.hasNext()) {
				EObject nextObject = crossReferenceIterator.next().getEObject();
				Resource nextResource = nextObject.eResource();
				if (baseResource != nextResource) {
					crossReferencingResources.add(nextResource);
				}
			}
		}

		return crossReferencingResources;
	}
}
