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
package org.eclipse.emf.compare.uml2.ide.tests.profile;

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
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.internal.utils.URIStorage;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the different way to load a UML profile in a {@link NotLoadingResourceSet}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ProfileLoadingTest {

	/**
	 * Qualified name of the profile St1.
	 */
	private static final String ST1_PROFILE_QUALIFIED_NAME = "profile::St1"; //$NON-NLS-1$

	/**
	 * Base URI for used referencing data.
	 */
	private static final String BASE_URI = "platform:/plugin/org.eclipse.emf.compare.uml2.ide.tests/src/org/eclipse/emf/compare/uml2/ide/tests/profile/data/loading/"; //$NON-NLS-1$

	/**
	 * URI of the profile used in most tests.
	 */
	private static final String PROFILE_URI = BASE_URI + "model.profile.uml"; //$NON-NLS-1$

	private static final Function<Resource, String> TO_STRING = new Function<Resource, String>() {
		public String apply(Resource input) {
			return input.getURI().toString();
		}
	};

	@SuppressWarnings("restriction")
	@BeforeClass
	public static void fillRegistries() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore",
					new EcoreResourceFactoryImpl());
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", //$NON-NLS-1$
					new UMLResourceFactoryImpl());
		}
	}

	@AfterClass
	public static void resetRegistries() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().remove("uml");
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().remove("ecore");

			EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(ComparePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(EcorePackage.eNS_URI);
		}
	}

	@After
	public void after() {
		if (resourceSet != null) {
			for (Resource resource : resourceSet.getResources()) {
				resource.unload();
			}
		}
	}

	private NotLoadingResourceSet resourceSet;

	/**
	 * Tests loading a dynamic profile referenced using a relative path. This profile is not registered in the
	 * platform but uses the xxx.profile.uml convention.
	 */
	@Test
	public void loadDynamicRelativeProfile() {
		String umlResourceURI = BASE_URI + "relative/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(umlResourceURI);
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI);
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
				ST1_PROFILE_QUALIFIED_NAME);
		assertTrue(stereotypeApplication instanceof DynamicEObjectImpl);

	}

	/**
	 * Tests loading a dynamic profile referenced using a platform:/plugin URI. It is not registered in the
	 * platform but uses the xxx.profile.uml convention.
	 */
	@Test
	public void loadDynamicPlatformProfile() {
		String umlResourceURI = BASE_URI + "platform/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(umlResourceURI);
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI);
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
				ST1_PROFILE_QUALIFIED_NAME);
		assertTrue(stereotypeApplication instanceof DynamicEObjectImpl);

	}

	/**
	 * Tests loading a dynamic profile referenced using a platform:/plugin URI. This profile is registered in
	 * the platform against org.eclipse.uml2.uml.dynamic_package UML extension point. It does not respect the
	 * convention xxx.profile.uml.
	 */
	@Test
	public void loadDynamicRegisteredProfile() {
		String umlResourceURI = BASE_URI + "registered/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(umlResourceURI);
		Set<String> expectedLoadedResource = Sets.newHashSet(BASE_URI + "registered/model_profile.uml", //$NON-NLS-1$
				umlResourceURI);
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
				ST1_PROFILE_QUALIFIED_NAME);
		assertTrue(stereotypeApplication instanceof DynamicEObjectImpl);

	}

	/**
	 * Tests loading a dynamic profile referenced using a pathmap URI. This profile is not registered in
	 * platform but uses the xxx.profile.uml convention.
	 */
	@Test
	public void loadDynamicPathmapProfile() {
		String umlResourceURI = BASE_URI + "pathmap/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(umlResourceURI);
		Set<String> expectedLoadedResource = Sets.newHashSet(
				"pathmap://ProfileLoadingTest/model.profile.uml", umlResourceURI); //$NON-NLS-1$
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
				ST1_PROFILE_QUALIFIED_NAME);
		assertTrue(stereotypeApplication instanceof DynamicEObjectImpl);

	}

	/**
	 * Tests loading a dynamic profile referenced using a relative path. This profile is not registered in the
	 * platform but uses the xxx.profile.uml convention. This time the profile model belong the list of
	 * IStorage that the resource set will load.
	 */
	@Test
	public void loadDynamicRelativeProfile2() {
		String umlResourceURI = BASE_URI + "relative/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(umlResourceURI, PROFILE_URI);
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI);
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
				ST1_PROFILE_QUALIFIED_NAME);
		assertTrue(stereotypeApplication instanceof DynamicEObjectImpl);

	}

	/**
	 * Same as {@link #loadDynamicRelativeProfile2()} but changes the order in wich the IStorages are loaded
	 * in the resource set.
	 */
	@Test
	public void loadDynamicRelativeProfile3() {
		String umlResourceURI = BASE_URI + "relative/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(PROFILE_URI, umlResourceURI);
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI);
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
				ST1_PROFILE_QUALIFIED_NAME);
		assertTrue(stereotypeApplication instanceof DynamicEObjectImpl);

	}

	/**
	 * Tests loading a static profile referenced using a pathmap URI. This profile is registered in the
	 * platform against org.eclipse.uml2.uml.generated_package UML extension point.
	 */
	@Test
	public void loadStaticProfile() {
		String umlResourceURI = BASE_URI + "static_/model.uml"; //$NON-NLS-1$
		resourceSet = createNotLoadingResourceSet(umlResourceURI);
		Set<String> expectedLoadedResource = Sets
				.newHashSet("pathmap://UML_PROFILES/Ecore.profile.uml", //$NON-NLS-1$
						umlResourceURI, BASE_URI + "static_/model.uml", //$NON-NLS-1$
						"platform:/plugin/org.eclipse.emf.compare.uml2.tests/model/uml2.compare.testprofile.profile.uml"); //$NON-NLS-1$
		assertLoadeResources(expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(umlResourceURI + "#_mg1YkEJqEeSsE-f8QjqLJA", //$NON-NLS-1$
				"UML2CompareTestProfile::ACliche"); //$NON-NLS-1$
		assertTrue(stereotypeApplication instanceof ACliche);
	}

	/**
	 * Asserts that all (and only) expected resources are loaded. It normalizes the URI in the resource set in
	 * order to resolve pathmaps.
	 * 
	 * @param expectedLoadedResource
	 * @param resourceSet
	 */
	private void assertLoadeResources(Set<String> expectedLoadedResource) {
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
		Collection<IStorage> storages = Collections2.transform(Lists.newArrayList(stringURIs),
				toStorage(new ExtensibleURIConverterImpl()));
		StorageTraversal storageTraversal = new StorageTraversal(Sets.newHashSet(storages));
		NotLoadingResourceSet newResourceSet = NotLoadingResourceSet.create(storageTraversal,
				new NullProgressMonitor());

		return newResourceSet;

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
	private EObject getStereotype(String eObjectURI, String stereotypeName) {
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

	public static final Function<String, IStorage> toStorage(final URIConverter uriConverter) {
		return new Function<String, IStorage>() {
			public IStorage apply(String input) {
				URI uri = URI.createURI(input, true);
				return new URIStorage(uri, uriConverter.getURIHandler(uri), uriConverter);
			}
		};

	}

}
