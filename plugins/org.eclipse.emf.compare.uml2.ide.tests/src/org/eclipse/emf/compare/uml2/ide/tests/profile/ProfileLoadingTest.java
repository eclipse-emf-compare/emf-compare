/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - adapting tests for changes in bug 460780
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.ide.tests.profile;

import static org.eclipse.emf.compare.uml2.ide.tests.util.ProfileTestUtil.BASE_URI;
import static org.eclipse.emf.compare.uml2.ide.tests.util.ProfileTestUtil.assertLoadedResources;
import static org.eclipse.emf.compare.uml2.ide.tests.util.ProfileTestUtil.createNotLoadingResourceSet;
import static org.eclipse.emf.compare.uml2.ide.tests.util.ProfileTestUtil.getStereotype;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.Test;

/**
 * Tests the different way to load a UML profile in a {@link NotLoadingResourceSet}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ProfileLoadingTest {

	/**
	 * Holds the pathmap string to the profile.
	 */
	public static final String PROFILE_PATHMAP_URI = "pathmap://ProfileLoadingTest/model.profile.uml"; //$NON-NLS-1$

	/**
	 * Qualified name of the profile St1.
	 */
	private static final String ST1_PROFILE_QUALIFIED_NAME = "profile::St1"; //$NON-NLS-1$

	/**
	 * URI of the profile used in most tests.
	 */
	private static final String PROFILE_URI = BASE_URI + "model.profile.uml"; //$NON-NLS-1$

	/**
	 * URI of the pathmap to the UML metamodel.
	 */
	private static final String METAMODEL_PATHMAP_URI = "pathmap://UML_METAMODELS/UML.metamodel.uml"; //$NON-NLS-1$

	/**
	 * URI of the pathmap to the Ecore profile model.
	 */
	private static final String ECORE_PROFILE_PATHMAP_URI = "pathmap://UML_PROFILES/Ecore.profile.uml"; //$NON-NLS-1$

	/**
	 * URI of the pathmap to the Standard profile model.
	 */
	private static final String STANDARD_PROFILE_PATHMAP_URI = "pathmap://UML_PROFILES/Standard.profile.uml"; //$NON-NLS-1$

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
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI,
				METAMODEL_PATHMAP_URI, ECORE_PROFILE_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI);
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
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
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI,
				METAMODEL_PATHMAP_URI, ECORE_PROFILE_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI);
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
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
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI,
				METAMODEL_PATHMAP_URI, ECORE_PROFILE_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI);
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
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
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_URI, umlResourceURI,
				METAMODEL_PATHMAP_URI, ECORE_PROFILE_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI);
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
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
		Set<String> expectedLoadedResource = Sets.newHashSet(
				BASE_URI + "registered/model_profile.uml", //$NON-NLS-1$
				umlResourceURI, METAMODEL_PATHMAP_URI, ECORE_PROFILE_PATHMAP_URI,
				STANDARD_PROFILE_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI);
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
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
		Set<String> expectedLoadedResource = Sets.newHashSet(PROFILE_PATHMAP_URI, umlResourceURI,
				METAMODEL_PATHMAP_URI, ECORE_PROFILE_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI);
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_XzoewEIyEeSXpd1NJW5urA", //$NON-NLS-1$
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
				.newHashSet(
						"pathmap://UML_PROFILES/Ecore.profile.uml", //$NON-NLS-1$
						umlResourceURI,
						"pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml", ECORE_PROFILE_PATHMAP_URI, METAMODEL_PATHMAP_URI, STANDARD_PROFILE_PATHMAP_URI); //$NON-NLS-1$
		assertLoadedResources(resourceSet, expectedLoadedResource);
		EObject stereotypeApplication = getStereotype(resourceSet, umlResourceURI
				+ "#_mg1YkEJqEeSsE-f8QjqLJA", //$NON-NLS-1$
				"UML2CompareTestProfile::ACliche"); //$NON-NLS-1$
		assertTrue(stereotypeApplication instanceof ACliche);
	}
}
