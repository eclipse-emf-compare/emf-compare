/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.RevisionedURIConverter;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Constants;
import org.junit.After;
import org.junit.Before;

public class AbstractURITest extends CompareGitTestCase {
	protected static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	protected static final String BRANCH = Constants.R_HEADS + "branch";

	protected File file1;

	protected IFile iFile1;

	protected Resource resource1;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		file1 = project.getOrCreateFile(iProject, "file1.ecore");
		iFile1 = project.getIFile(iProject, file1);

		resource1 = connectResource(iFile1, resourceSet);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider) ModelProvider
				.getModelProviderDescriptor(EMFModelProvider.PROVIDER_ID)
				.getModelProvider();
		emfModelProvider.clear();
		super.tearDown();
	}

	/**
	 * We'll create a repo with a single file and three commits such as there's
	 * an initial commit, one more commit on master, then one commit on a branch
	 * starting from the initial one.
	 * 
	 * <pre>
	 * initial commit -> master
	 *                \-> branch
	 * </pre>
	 */
	protected void setupStraightRepo() throws Exception {
		EPackage root1 = createPackage(null, "P1");
		EClass class1 = createClass(root1, "C1");
		EClass class2 = createClass(root1, "C2");
		resource1.getContents().add(root1);

		save(resource1);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root1.getEClassifiers().remove(class2);
		class1.setName("C1_renamed");
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1);

		root1 = (EPackage) findObject(resource1, "P1");
		class1 = (EClass) findObject(resource1, "C1");
		class2 = (EClass) findObject(resource1, "C2");
		class1.getESuperTypes().add(class2);
		createClass(root1, "C3");

		save(resource1);
		repository.addAndCommit(project, "branch-commit", file1);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
	}

	/**
	 * We'll create a repo with a single file and multiple commits such as there
	 * are multiple commits in-between the compared one and the expected common
	 * ancestor.
	 * 
	 * <pre>
	 * initial commit -> commit-master-1 -> commit-master-2 -> commit-master-3
	 *                                   \-> commit-branch1-1 -> commit-branch1-2
	 *                                                        \-> commit-branch2-1
	 * </pre>
	 */
	protected void setupMultipleCommitsRepo(String branch1Name,
			String branch2Name) throws Exception {
		EPackage root1 = createPackage(null, "P1");
		EClass class1 = createClass(root1, "C1");
		resource1.getContents().add(root1);
		save(resource1);
		repository.addAllAndCommit("initial-commit");

		EClass class2 = createClass(root1, "C2");
		save(resource1);
		repository.addAndCommit(project, "commit-master-1", file1);

		// Branch, but stay on master
		repository.createBranch(MASTER, branch1Name);

		root1.getEClassifiers().remove(class2);
		save(resource1);
		repository.addAndCommit(project, "commit-master-2", file1);

		class1.setName("C1_renamed");
		save(resource1);
		repository.addAndCommit(project, "commit-master-3", file1);

		repository.checkoutBranch(branch1Name);
		reload(resource1);

		class1 = (EClass) findObject(resource1, "C1");
		class2 = (EClass) findObject(resource1, "C2");
		class1.getESuperTypes().add(class2);
		save(resource1);
		repository.addAndCommit(project, "commit-branch1-1", file1);

		// Create the second branch, but stay on the first for one more commit
		repository.createBranch(branch1Name, branch2Name);

		class2.setName("C2_renamed");
		save(resource1);
		repository.addAndCommit(project, "commit-branch1-2", file1);

		repository.checkoutBranch(branch2Name);
		reload(resource1);

		root1 = (EPackage) findObject(resource1, "P1");
		createClass(root1, "C3");
		save(resource1);
		repository.addAndCommit(project, "commit-branch2-1", file1);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
	}

	protected void assertStateInitial(URI fileURI,
			IStorageProviderAccessor accessor, DiffSide side) throws Exception {
		URIConverter delegate = new ExtensibleURIConverterImpl();
		RevisionedURIConverter converter = new RevisionedURIConverter(delegate,
				accessor, side);
		InputStream stream = converter.createInputStream(fileURI,
				Collections.emptyMap());
		ResourceSet tmpSet = new ResourceSetImpl();
		Resource tmpResource = tmpSet.createResource(fileURI);
		tmpResource.load(stream, Collections.emptyMap());

		assertEquals(1, tmpResource.getContents().size());
		assertTrue(tmpResource.getContents().get(0) instanceof EPackage);
		EPackage tmpPack = (EPackage) tmpResource.getContents().get(0);
		assertEquals("P1", tmpPack.getName());
		assertEquals(2, tmpPack.eContents().size());
		assertTrue(tmpPack.eContents().get(0) instanceof EClass);
		assertTrue(tmpPack.eContents().get(1) instanceof EClass);
		EClass tmpClass = (EClass) tmpPack.eContents().get(0);
		assertEquals("C1", tmpClass.getName());
		assertTrue(tmpClass.eContents().isEmpty());
		tmpClass = (EClass) tmpPack.eContents().get(1);
		assertEquals("C2", tmpClass.getName());
		assertTrue(tmpClass.eContents().isEmpty());
	}

	protected void assertStateMaster(URI fileURI,
			IStorageProviderAccessor accessor, DiffSide side) throws Exception {
		URIConverter delegate = new ExtensibleURIConverterImpl();
		RevisionedURIConverter converter = new RevisionedURIConverter(delegate,
				accessor, side);
		InputStream stream = converter.createInputStream(fileURI,
				Collections.emptyMap());
		ResourceSet tmpSet = new ResourceSetImpl();
		Resource tmpResource = tmpSet.createResource(fileURI);
		tmpResource.load(stream, Collections.emptyMap());

		assertEquals(1, tmpResource.getContents().size());
		assertTrue(tmpResource.getContents().get(0) instanceof EPackage);
		EPackage tmpPack = (EPackage) tmpResource.getContents().get(0);
		assertEquals("P1", tmpPack.getName());
		assertEquals(1, tmpPack.eContents().size());
		assertTrue(tmpPack.eContents().get(0) instanceof EClass);
		EClass tmpClass = (EClass) tmpPack.eContents().get(0);
		assertEquals("C1_renamed", tmpClass.getName());
		assertTrue(tmpClass.eContents().isEmpty());
	}

	protected void assertStateBranch(URI fileURI,
			IStorageProviderAccessor accessor, DiffSide side) throws Exception {
		URIConverter delegate = new ExtensibleURIConverterImpl();
		RevisionedURIConverter converter = new RevisionedURIConverter(delegate,
				accessor, side);
		InputStream stream = converter.createInputStream(fileURI,
				Collections.emptyMap());
		ResourceSet tmpSet = new ResourceSetImpl();
		Resource tmpResource = tmpSet.createResource(fileURI);
		tmpResource.load(stream, Collections.emptyMap());

		assertEquals(1, tmpResource.getContents().size());
		assertTrue(tmpResource.getContents().get(0) instanceof EPackage);
		EPackage tmpPack = (EPackage) tmpResource.getContents().get(0);
		assertEquals("P1", tmpPack.getName());
		assertEquals(3, tmpPack.eContents().size());
		assertTrue(tmpPack.eContents().get(0) instanceof EClass);
		assertTrue(tmpPack.eContents().get(1) instanceof EClass);
		assertTrue(tmpPack.eContents().get(2) instanceof EClass);
		EClass tmpClass1 = (EClass) tmpPack.eContents().get(0);
		EClass tmpClass2 = (EClass) tmpPack.eContents().get(1);
		EClass tmpClass3 = (EClass) tmpPack.eContents().get(2);
		assertEquals("C1", tmpClass1.getName());
		assertEquals(1, tmpClass1.eContents().size());
		assertTrue(tmpClass1.eContents().get(0) instanceof EGenericType);
		assertTrue(tmpClass1.getESuperTypes().contains(tmpClass2));
		assertEquals("C2", tmpClass2.getName());
		assertTrue(tmpClass2.eContents().isEmpty());
		assertEquals("C3", tmpClass3.getName());
		assertTrue(tmpClass3.eContents().isEmpty());
	}
}
