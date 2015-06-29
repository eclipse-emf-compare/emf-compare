/*******************************************************************************
 * Copyright (C) 2013, 2015 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterators.filter;
import static org.eclipse.emf.ecore.util.EcoreUtil.getAllProperContents;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.After;
import org.junit.Before;

@SuppressWarnings("nls")
public class CompareTestCase {
	protected static final String PROJECT_NAME = "Project-1";

	protected static final String PACKAGE_NAME_PREFIX = "package";

	protected static final String CLASS1_NAME_PREFIX = "Class_A";

	protected static final String CLASS2_NAME_PREFIX = "Class_B";

	protected static final String CLASS3_NAME_PREFIX = "Class_C";

	protected static final String CLASS4_NAME_PREFIX = "Class_D";

	protected TestProject project;

	@Before
	public void setUp() throws Exception {
		project = new TestProject(PROJECT_NAME);
	}

	@After
	public void tearDown() throws Exception {
		project.dispose();
	}

	/**
	 * The base model for both our files will be one package containing two classes. There are no references
	 * and no attributes set, save for the name of these objects.
	 * 
	 * @param nameSuffix
	 *            Suffix that will be appended to all names for this model.
	 * @return A basic model to be used by these tests.
	 */
	protected EPackage createBasicModel(String nameSuffix) {
		EPackage root = createPackage(null, PACKAGE_NAME_PREFIX + nameSuffix);
		createClass(root, CLASS1_NAME_PREFIX + nameSuffix);
		createClass(root, CLASS2_NAME_PREFIX + nameSuffix);
		return root;
	}

	protected EPackage createPackage(EPackage parent, String name) {
		final EPackage newPackage = EcoreFactory.eINSTANCE.createEPackage();
		newPackage.setName(name);
		if (parent != null) {
			parent.getESubpackages().add(newPackage);
		}
		return newPackage;
	}

	protected EClass createClass(EPackage parent, String name) {
		final EClass newClass = EcoreFactory.eINSTANCE.createEClass();
		newClass.setName(name);
		if (parent != null) {
			parent.getEClassifiers().add(newClass);
		}
		return newClass;
	}

	protected EAttribute createAttribute(EClass parent, String name, EClassifier type) {
		final EAttribute newAttribute = EcoreFactory.eINSTANCE.createEAttribute();
		newAttribute.setName(name);
		newAttribute.setEType(type);
		if (parent != null) {
			parent.getEAttributes().add(newAttribute);
		}
		return newAttribute;
	}

	protected EObject findObject(Resource resource, String namePrefix) {
		Iterator<EObject> children = EcoreUtil.getAllProperContents(resource, false);
		while (children.hasNext()) {
			final EObject child = children.next();
			if (child instanceof ENamedElement && ((ENamedElement)child).getName().startsWith(namePrefix)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Connects an EMF resource to the given File within the given project. The resource will be created with
	 * a workspace-relative "{@code platform:/resource}" URI.
	 * 
	 * @param file
	 *            The file we're attaching an EMF Resource on.
	 * @param resourceSet
	 *            The resource set in which the new Resource will be created.
	 * @return The created EMF Resource.
	 */
	protected Resource connectResource(IFile file, ResourceSet resourceSet) throws CoreException {
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);

		return createResource(uri, resourceSet);
	}

	private static Resource createResource(URI modelURI, ResourceSet resourceSet) {
		final Resource resource = new XMIResourceImpl(modelURI) {
			@Override
			protected boolean useUUIDs() {
				return true;
			}
		};
		resourceSet.getResources().add(resource);
		return resource;
	}

	/**
	 * This will seek for a random EClass in both given resources, then use the "eSuperTypes" reference of the
	 * source one to create a reference towards the target.
	 * 
	 * @param source
	 *            Resource within which we'll search for our source EClass (the class which will have a
	 *            superType).
	 * @param target
	 *            Resource within which we'll search for our target EClass (the superType).
	 */
	protected void makeCrossReference(Resource source, Resource target) {
		final Iterator<EClass> sourceChildren = filter(getAllProperContents(source, false), EClass.class);
		final Iterator<EClass> targetChildren = filter(getAllProperContents(target, false), EClass.class);
		assertTrue(sourceChildren.hasNext());
		assertTrue(targetChildren.hasNext());
		final EClass sourceClass = sourceChildren.next();
		final EClass targetClass = targetChildren.next();

		sourceClass.getESuperTypes().add(targetClass);
	}

	/**
	 * This will seek and break all cross-references from <code>source</code> to <code>target</code>.
	 * 
	 * @param source
	 *            Resource within which we'll search for our cross-references.
	 * @param target
	 *            Target of the cross-references to break.
	 */
	protected void breakCrossReferences(Resource source, Resource target) {
		final Iterator<EObject> sourceChildren = getAllProperContents(source, false);

		while (sourceChildren.hasNext()) {
			final EObject child = sourceChildren.next();
			breakCrossReferences(child, target);
		}
	}

	private void breakCrossReferences(EObject source, Resource target) {
		for (EReference ref : source.eClass().getEAllReferences()) {
			if (ref.isDerived()) {
				continue;
			}
			final Object value = source.eGet(ref);
			if (!ref.isMany()) {
				if (value instanceof EObject && ((EObject)value).eResource() == target) {
					source.eSet(ref, null);
				}
			} else if (value instanceof Collection<?>) {
				final Collection<?> valueList = (Collection<?>)value;
				final Iterable<EObject> copy = filter(new ArrayList<Object>(valueList), EObject.class);
				for (EObject targetEObject : copy) {
					if (targetEObject.eResource() == target) {
						valueList.remove(targetEObject);
					}
				}
			}
		}
	}

	/**
	 * Create a cross-resource reference through the "superType" reference of a given EClass.
	 * <p>
	 * The source EClass will be searched within the {@code source} Resource and its name should have a set
	 * prefix. Similarly, the target EClass will be searched withi the {@code target} Resource.
	 * </p>
	 * 
	 * @param source
	 *            Resource within which we'll search for our source EClass (the class which will have a
	 *            superType).
	 * @param target
	 *            Resource within which we'll search for our target EClass (the superType).
	 * @param sourceNamePrefix
	 *            Prefix (or exact name) of the source EClass.
	 * @param targetNamePrefix
	 *            Prefix (or exact name) of the target EClass.
	 */
	protected void makeCrossReference(Resource source, Resource target, String sourceNamePrefix,
			String targetNamePrefix) {
		final EObject sourceObject = findObject(source, sourceNamePrefix);
		final EObject targetObject = findObject(target, targetNamePrefix);

		assertTrue(sourceObject instanceof EClass);
		assertTrue(targetObject instanceof EClass);

		((EClass)sourceObject).getESuperTypes().add((EClass)targetObject);
	}

	protected void unload(Resource... resources) {
		for (Resource resource : resources) {
			resource.getContents().clear();
			resource.unload();
		}
	}

	protected void reload(Resource... resources) throws IOException {
		unload(resources);
		// separate loop to reload so that we are sure everything has been unloaded
		for (Resource resource : resources) {
			resource.load(Collections.emptyMap());
		}
		// And a third loop to re-resolve every cross-references between the reloaded resources
		for (Resource resource : resources) {
			EcoreUtil.resolveAll(resource);
		}
	}

	protected void save(ResourceSet resourceSet) throws IOException, CoreException {
		for (Resource resource : resourceSet.getResources()) {
			final HashMap<String, Object> options = new HashMap<String, Object>();
			options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
					Resource.OPTION_SAVE_ONLY_IF_CHANGED_FILE_BUFFER);
			resource.save(options);
		}
		if (project != null) {
			IProject prj = project.getProject();
			if (prj != null) {
				prj.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
		}
	}

	protected void save(Resource... resources) throws IOException, CoreException {
		for (Resource resource : resources) {
			final HashMap<String, Object> options = new HashMap<String, Object>();
			options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
					Resource.OPTION_SAVE_ONLY_IF_CHANGED_FILE_BUFFER);
			resource.save(options);
		}
		if (project != null) {
			IProject prj = project.getProject();
			if (prj != null) {
				prj.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
		}
	}

	protected static void copyFile(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		FileInputStream fileInputStream = new FileInputStream(source);
		sourceChannel = fileInputStream.getChannel();
		FileOutputStream fileOutputStream = new FileOutputStream(dest);
		destChannel = fileOutputStream.getChannel();
		destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		sourceChannel.close();
		destChannel.close();
		fileInputStream.close();
		fileOutputStream.close();
	}
}
