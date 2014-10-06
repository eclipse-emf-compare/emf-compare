/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.ide.tests.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.internal.utils.URIStorage;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

/**
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ProfileTestUtil {

	/**
	 * Base URI for used referencing data.
	 */
	public static final String BASE_URI = "platform:/plugin/org.eclipse.emf.compare.uml2.ide.tests/src/org/eclipse/emf/compare/uml2/ide/tests/profile/data/loading/"; //$NON-NLS-1$

	/**
	 * Transforms a {@link Resource} to String using its {@link URI}.
	 */
	private static final Function<Resource, String> TO_STRING = new Function<Resource, String>() {
		public String apply(Resource input) {
			return input.getURI().toString();
		}
	};

	private ProfileTestUtil() {
		// Should not be instanciated
	}

	/**
	 * Asserts that all (and only) expected resources are loaded. It normalizes the URI in the resource set in
	 * order to resolve pathmaps.
	 * 
	 * @param expectedLoadedResource
	 * @param resourceSet
	 */
	public static void assertLoadeResources(ResourceSet resourceSet, Set<String> expectedLoadedResource) {
		assertEquals(expectedLoadedResource.size(), resourceSet.getResources().size());
		Set<String> actualURIs = Sets.newHashSet(Collections2
				.transform(resourceSet.getResources(), TO_STRING));
		assertEquals(expectedLoadedResource, actualURIs);

	}

	/**
	 * Creates a {@link NotLoadingResourceSet}.
	 * 
	 * @param stringURIs
	 *            Uris of the resources to load.
	 * @return {@link NotLoadingResourceSet} with loaded resources.
	 */
	public static NotLoadingResourceSet createNotLoadingResourceSet(String... stringURIs) {
		StorageTraversal storageTraversal = createStorageTraversal(stringURIs);
		NotLoadingResourceSet newResourceSet = NotLoadingResourceSet.create(storageTraversal,
				new NullProgressMonitor(), null);

		return newResourceSet;

	}

	/**
	 * Creates a {@link StorageTraversal} holding the {@link IStorage}s that are pointed by the parameter.
	 * 
	 * @param stringURIs
	 *            String uri of the resouce that need to be wrapped in {@link IStorage}s.
	 * @return {@link StorageTraversal}.
	 */
	public static StorageTraversal createStorageTraversal(String... stringURIs) {
		Collection<IStorage> storages = Collections2.transform(Lists.newArrayList(stringURIs),
				toStorage(new ExtensibleURIConverterImpl()));
		StorageTraversal storageTraversal = new StorageTraversal(Sets.newHashSet(storages));
		return storageTraversal;
	}

	/**
	 * Returns the stereoptype application on the eObject.
	 * <p>
	 * This method assert that the Element exists and that it has the matching stereotyped applied on it
	 * </p>
	 * 
	 * @param eObjectURI
	 *            uri pointing to a {@link NamedElement} in the resource set. (NamedElement has been choosen
	 *            for printing convenience).
	 * @param stereotypeName
	 *            Qualified name of the seteotype.
	 */
	public static EObject getStereotype(ResourceSet resourceSet, String eObjectURI, String stereotypeName) {
		EObject eObject = resourceSet.getEObject(URI.createURI(eObjectURI), false);
		assertTrue(eObject instanceof NamedElement);
		NamedElement elem = (NamedElement)eObject;
		StringBuilder errorMessage = new StringBuilder();
		errorMessage.append("The stereotype ").append(stereotypeName).append(" is not applied on ").append( //$NON-NLS-1$ //$NON-NLS-2$
				elem.getName());
		Stereotype stereotype = elem.getAppliedStereotype(stereotypeName);
		assertNotNull(errorMessage.toString(), stereotype);
		EObject stereotypeApplication = elem.getStereotypeApplication(stereotype);
		assertNotNull(stereotypeApplication);
		return stereotypeApplication;
	}

	/**
	 * Creates a {@link Function} that convert a string into a {@link IStorage} using a {@link URIConverter}.
	 * 
	 * @param uriConverter
	 *            {@link URIConverter} used to erretreive the {@link URIHandler}.
	 * @return Function<String, IStorage>
	 */
	public static Function<String, IStorage> toStorage(final URIConverter uriConverter) {
		return new Function<String, IStorage>() {
			public IStorage apply(String input) {
				URI uri = URI.createURI(input, false);
				return new URIStorage(uri, uriConverter.getURIHandler(uri), uriConverter);
			}
		};
	}

}
