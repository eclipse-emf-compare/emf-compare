/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.logical.EMFSynchronizationModel;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.team.core.subscribers.Subscriber;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class LogicalModelTest extends CompareGitTestCase {
	private static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	private static final String BRANCH = Constants.R_HEADS + "branch";

	private static final String PACKAGE_NAME_PREFIX = "package";

	private static final String CLASS1_NAME_PREFIX = "Class_A";

	private static final String CLASS2_NAME_PREFIX = "Class_B";

	private static final String CLASS3_NAME_PREFIX = "Class_C";

	private static final String CLASS4_NAME_PREFIX = "Class_D";

	private static final String FILE1_SUFFIX = "_file1";

	private static final String FILE2_SUFFIX = "_file2";

	private IFile iFile1;

	private IFile iFile2;

	private Resource resource1;

	private Resource resource2;

	/**
	 * We'll have seven commits during this test, roughly looking like this:
	 * 
	 * <pre>
	 * o--o--o--o--o
	 *        \--o--o
	 * </pre>
	 * 
	 * In order within this array:
	 * 
	 * <pre>
	 * 0--1--2--3--4
	 *        \--5--6
	 * </pre>
	 * 
	 * There will be two ecore files within this repository. file1 (F1) references file2 (F2) and conversely
	 * so that the logical model, whatever the starting point, comprises both files. The following shows which
	 * files were modified with which commits:
	 * 
	 * <pre>
	 * (F1+F2)--(F2)--(F2)--(F2)--(F1+F2)
	 *                    \--(F1+F2)--(F2)
	 * </pre>
	 */
	private RevCommit[] commits = new RevCommit[7];

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = repository.getOrCreateFile(iProject, "file1.ecore");
		final File file2 = repository.getOrCreateFile(iProject, "file2.ecore");
		iFile1 = repository.getIFile(iProject, file1);
		iFile2 = repository.getIFile(iProject, file2);

		resource1 = repository.connectResource(iProject, file1, resourceSet);
		resource2 = repository.connectResource(iProject, file2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		makeCrossReference(resource1, resource2, CLASS1_NAME_PREFIX, CLASS1_NAME_PREFIX);
		makeCrossReference(resource2, resource1, CLASS2_NAME_PREFIX, CLASS2_NAME_PREFIX);

		save(resource1, resource2);
		commits[0] = repository.addAndCommit(iProject, "master-commit-1", file1, file2);

		// Second commit: add class in second resource
		EPackage packFile2 = (EPackage)findObject(resource2, PACKAGE_NAME_PREFIX);
		final EClass newClassFile2 = createClass(packFile2, CLASS3_NAME_PREFIX + FILE2_SUFFIX);
		save(resource2);
		commits[1] = repository.addAndCommit(iProject, "master-commit-2", file2);

		// Third: rename that new class
		newClassFile2.setName(CLASS4_NAME_PREFIX + FILE2_SUFFIX);
		save(resource2);
		commits[2] = repository.addAndCommit(iProject, "master-commit-3", file2);

		// Branching point, though stay on master for now
		repository.createBranch(MASTER, BRANCH);

		// fourth commit: remove the new class
		packFile2.getEClassifiers().remove(newClassFile2);
		save(resource2);
		commits[3] = repository.addAndCommit(iProject, "master-commit-4", file2);

		// fifth and last commit on master: rename second class in both files
		final EClass classBFile1 = (EClass)findObject(resource1, CLASS2_NAME_PREFIX);
		final EClass classBFile2 = (EClass)findObject(resource2, CLASS2_NAME_PREFIX);
		classBFile1.setName(CLASS3_NAME_PREFIX + FILE1_SUFFIX);
		classBFile2.setName(CLASS3_NAME_PREFIX + FILE2_SUFFIX);
		save(resource1, resource2);
		commits[4] = repository.addAndCommit(iProject, "master-commit-5", file1, file2);

		// checkout the branch now
		repository.checkoutBranch(BRANCH);
		// Note that we have to reload our two resources now
		reload(resource1, resource2);

		// first commit of the branch : rename first class in both files
		final EClass classAFile1 = (EClass)findObject(resource1, CLASS1_NAME_PREFIX);
		final EClass classAFile2 = (EClass)findObject(resource2, CLASS1_NAME_PREFIX);
		classAFile1.setName(CLASS3_NAME_PREFIX + FILE1_SUFFIX);
		classAFile2.setName(CLASS3_NAME_PREFIX + FILE2_SUFFIX);
		save(resource1, resource2);
		commits[5] = repository.addAndCommit(iProject, "branch-commit-1", file1, file2);

		// second commit of the branch : delete the new (third) class
		// We've reloaded the resource, our reference to the class must be reinitialized
		EcoreUtil.remove(findObject(resource2, CLASS4_NAME_PREFIX));
		save(resource2);
		commits[6] = repository.addAndCommit(iProject, "branch-commit-2", file2);
	}

	@Test
	public void testCompareTipsFile1() throws Exception {
		final Subscriber subscriber = repository.createSubscriberForComparison(MASTER, BRANCH, iFile1);

		final EMFSynchronizationModel syncModel = EMFSynchronizationModel.createSynchronizationModel(
				subscriber, iFile1, null, null);
		final IComparisonScope scope = syncModel.createMinimizedScope();

		final ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
		final ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
		final ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

		assertEquals(2, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(2, originResourceSet.getResources().size());

		final Comparison compareResult = EMFCompare.builder().build().compare(scope, new BasicMonitor());
		/*
		 * Between the tips, we should have one pseudo conflict (removed the third class) and four differences
		 * (renamed first and second class in both files).
		 */
		assertEquals(6, compareResult.getDifferences().size());
		assertEquals(1, compareResult.getConflicts().size());
	}

	/**
	 * The base model for both our files will be one package containing two classes. There are no references
	 * and no attributes set, save for the name of these objects.
	 * 
	 * @param nameSuffix
	 *            Suffix that will be appended to all names for this model.
	 * @return A basic model to be used by these tests.
	 */
	private EPackage createBasicModel(String nameSuffix) {
		EPackage root = createPackage(null, PACKAGE_NAME_PREFIX + nameSuffix);
		createClass(root, CLASS1_NAME_PREFIX + nameSuffix);
		createClass(root, CLASS2_NAME_PREFIX + nameSuffix);
		return root;
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
	private void makeCrossReference(Resource source, Resource target, String sourceNamePrefix,
			String targetNamePrefix) {
		final EObject sourceObject = findObject(source, sourceNamePrefix);
		final EObject targetObject = findObject(target, targetNamePrefix);

		assertTrue(sourceObject instanceof EClass);
		assertTrue(targetObject instanceof EClass);

		((EClass)sourceObject).getESuperTypes().add((EClass)targetObject);
	}
}
