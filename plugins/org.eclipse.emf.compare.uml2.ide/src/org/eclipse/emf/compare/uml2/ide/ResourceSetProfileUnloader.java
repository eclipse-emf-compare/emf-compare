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
	public boolean isHookFor(Collection<? extends URI> uris) {
		for (URI uri : uris) {
			if (UML_EXTENSION.equals(uri.fileExtension())) {
				return true;
			}
		}
		return false;
	}

	public void preLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
		// We're not interested in this event
	}

	public void postLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
		// We're not interested in this event
	}

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
		Collection<EStructuralFeature.Setting> settings = CacheAdapter.getInstance().getInverseReferences(
				profile, false);
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
