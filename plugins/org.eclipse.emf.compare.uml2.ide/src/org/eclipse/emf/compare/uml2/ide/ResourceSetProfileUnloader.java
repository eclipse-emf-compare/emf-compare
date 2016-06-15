/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 460780
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.ide;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.hook.IResourceSetHook;
import org.eclipse.emf.compare.uml2.rcp.internal.policy.UMLLoadOnDemandPolicy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.uml2.common.util.CacheAdapter;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;

/**
 * The {@link UMLLoadOnDemandPolicy} will load profiles in the resource set used by EMF Compare. These will be
 * referenced in package registries and extended meta-data for their resource set. We want them to be properly
 * unloaded and will do so from here.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceSetProfileUnloader extends UMLLoadOnDemandPolicy implements IResourceSetHook {

	/**
	 * Returns true if this hook should be used.
	 * 
	 * @param uris
	 *            list of {@link URI}s about to be loaded in the {@link ResourceSet}.
	 * @return <code>true</code> if this hook should be used, <code>false</code> otherwise.
	 */
	public boolean isHookFor(Collection<? extends URI> uris) {
		for (URI uri : uris) {
			if (UML_EXTENSION.equals(uri.fileExtension())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This will be called before the final resource set is populated, in unspecified order. Resource set
	 * hooks can load resource in this resource set and thus an individual hook is not guaranteed to be
	 * provided an empty resource set here.
	 * 
	 * @param resourceSet
	 *            about to be filled.
	 * @param uris
	 *            {@link URI}s that the resource set has been requested to load. The {@link Collection} of
	 *            {@link URI} is not modifiable.
	 */
	public void preLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
		// We're not interested in this event
	}

	/**
	 * This will be called after the resource set is populated in an unspecified order.
	 * 
	 * @param resourceSet
	 *            that has been filled with {@link org.eclipse.emf.ecore.resource.Resource}s.
	 * @param uris
	 *            {@link URI}s that the resource set has been requested to load.The {@link Collection} of
	 *            {@link URI} is not modifiable.
	 */
	public void postLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
		// We're not interested in this event
	}

	/**
	 * This will be called when the resource set is disposed (if it is).
	 * <p>
	 * By default, EMF Compare will not unload any resource. Still some resources might need to be unloaded.
	 * This method could be a good way to do it. Hooks are called in unspecified order, so resources may
	 * already have been unloaded by other hooks when yours is called.
	 * </p>
	 * 
	 * @param resources
	 *            List of {@link Resource}s currently in the resource set.
	 */
	public void onDispose(Iterable<Resource> resources) {
		URIConverter uriConverter = new ExtensibleURIConverterImpl();
		for (Resource resource : resources) {
			if (resource.isLoaded()) {
				URI uri = resource.getURI();
				URI normalizedURI = uriConverter.normalize(uri);
				if (isConventionalURIForUMLProfile(normalizedURI) || isUMLMetaModel(normalizedURI)
						|| isRegisteredUMLProfile(normalizedURI, uriConverter)) {
					for (EObject child : resource.getContents()) {
						// TODO Are profiles always at the root?
						if (child instanceof Profile) {
							disposeProfileApplications((Profile)child);
							break;
						}
					}
					resource.unload();
				}
			}
		}
	}

	/**
	 * UML will hold onto the profiles in memory even if we unload them completely unless we do the same for
	 * the resources they're referenced from.
	 * 
	 * @param profile
	 *            The profile which applications we need to dispose of.
	 */
	private void disposeProfileApplications(Profile profile) {
		final Set<Resource> unloadMe = new LinkedHashSet<Resource>();
		Collection<EStructuralFeature.Setting> settings = CacheAdapter.getInstance()
				.getInverseReferences(profile, false);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEObject() instanceof ProfileApplication) {
				unloadMe.add(setting.getEObject().eResource());
			}
		}
		for (Resource unload : unloadMe) {
			if (unload.isLoaded()) {
				unload.unload();
			}
		}
	}
}
