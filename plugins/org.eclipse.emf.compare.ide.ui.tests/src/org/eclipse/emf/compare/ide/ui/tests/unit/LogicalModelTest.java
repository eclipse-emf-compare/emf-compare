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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.LogicalModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
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
import org.eclipse.swt.graphics.Image;
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

	private Resource resource1;

	private Resource resource2;

	private String[] masterTipSynonyms;

	private String[] branchTipSynonyms;

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

		masterTipSynonyms = new String[] {MASTER, commits[4].getName(), };
		branchTipSynonyms = new String[] {BRANCH, commits[6].getName(), Constants.HEAD, };
	}

	@Test
	public void testCompareTipsFile1() throws Exception {
		// 1 conflict (deleted third class)
		// 6 diffs total (renamed both classes in both versions)

		for (String masterTip : masterTipSynonyms) {
			for (String branchTip : branchTipSynonyms) {
				compareBothDirectionsAndCheck(iFile1, masterTip, branchTip, 1, 3, 3);
			}
		}
	}

	@Test
	public void testCompareMasterTipWithBranchingPoint() throws Exception {
		final String branchingPoint = commits[2].getName();
		for (String masterTip : masterTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 3 diffs : renamed class in both files, deleted the third

			compareBothDirectionsAndCheck(iFile1, masterTip, branchingPoint, 0, 3, 0);
		}
	}

	@Test
	public void testCompareBranchTipWithBranchingPoint() throws Exception {
		final String branchingPoint = commits[2].getName();
		for (String branchTip : branchTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 3 diffs : renamed class in both files, deleted the third

			compareBothDirectionsAndCheck(iFile1, branchTip, branchingPoint, 0, 3, 0);
		}
	}

	@Test
	public void testCompareMasterTipWithInitial() throws Exception {
		final String initialCommit = commits[0].getName();
		for (String masterTip : masterTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 2 diffs : renamed class in both files

			compareBothDirectionsAndCheck(iFile1, masterTip, initialCommit, 0, 2, 0);
		}
	}

	@Test
	public void testCompareBranchTipWithInitial() throws Exception {
		final String initialCommit = commits[0].getName();
		for (String branchTip : branchTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 2 diffs : renamed class in both files

			compareBothDirectionsAndCheck(iFile1, branchTip, initialCommit, 0, 2, 0);
		}
	}

	@Test
	public void testCompareSecondToLastCommitBothBranch() throws Exception {
		// no conflict
		// 3 diffs : renamed two class on branch, removed the third on master
		final String masterCommit = commits[3].getName();
		final String branchCommit = commits[5].getName();

		compareBothDirectionsAndCheck(iFile1, masterCommit, branchCommit, 0, 1, 2);
	}

	private void compareBothDirectionsAndCheck(IFile file, String source, String destination,
			int expectedConflicts, int diffsInSource, int diffsInDestination) throws Exception {
		Comparison compareResult = compare(source, destination, file);

		assertEquals(expectedConflicts, compareResult.getConflicts().size());
		assertDiffCount(compareResult.getDifferences(), diffsInSource, diffsInDestination);

		compareResult = compare(destination, source, file);

		assertEquals(expectedConflicts, compareResult.getConflicts().size());
		assertDiffCount(compareResult.getDifferences(), diffsInDestination, diffsInSource);
	}

	private void assertDiffCount(List<Diff> differences, int expectedOutgoing, int expectedIncoming) {
		assertEquals(expectedOutgoing + expectedIncoming, differences.size());

		int outgoingCount = 0;
		int incomingCount = 0;
		for (Diff diff : differences) {
			switch (diff.getSource()) {
				case LEFT:
					outgoingCount++;
					break;
				case RIGHT:
					incomingCount++;
					break;
				default:
					break;
			}
		}

		assertEquals(expectedOutgoing, outgoingCount);
		assertEquals(expectedIncoming, incomingCount);
	}

	private Comparison compare(String sourceRev, String targetRev, IFile file) throws Exception {
		final String fullPath = file.getFullPath().toString();
		final Subscriber subscriber = repository.createSubscriberForComparison(sourceRev, targetRev, file);
		final IStorageProviderAccessor accessor = new SubscriberStorageAccessor(subscriber);
		final IStorageProvider sourceProvider = accessor.getStorageProvider(iFile1,
				IStorageProviderAccessor.DiffSide.SOURCE);
		final IStorageProvider remoteProvider = accessor.getStorageProvider(iFile1,
				IStorageProviderAccessor.DiffSide.REMOTE);
		final IStorageProvider ancestorProvider = accessor.getStorageProvider(iFile1,
				IStorageProviderAccessor.DiffSide.ORIGIN);
		assertNotNull(sourceProvider);
		assertNotNull(remoteProvider);
		assertNotNull(ancestorProvider);

		final IProgressMonitor monitor = new NullProgressMonitor();
		final IStorageProviderAccessor storageAccessor = new SubscriberStorageAccessor(subscriber);
		final ITypedElement left = new StorageTypedElement(sourceProvider.getStorage(monitor), fullPath);
		final ITypedElement right = new StorageTypedElement(remoteProvider.getStorage(monitor), fullPath);
		final ITypedElement origin = new StorageTypedElement(ancestorProvider.getStorage(monitor), fullPath);
		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(new LogicalModelResolver(),
				new IdenticalResourceMinimizer(), storageAccessor);
		final IComparisonScope scope = scopeBuilder.build(left, right, origin, monitor);

		final ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
		final ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
		final ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

		assertEquals(2, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(2, originResourceSet.getResources().size());

		return EMFCompare.builder().build().compare(scope, new BasicMonitor());
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

	/** Mostly copied from org.eclipse.team.internal.ui.StorageTypedElement. */
	private class StorageTypedElement implements ITypedElement, IEncodedStreamContentAccessor, IAdaptable {
		private final IStorage storage;

		private final String fullPath;

		public StorageTypedElement(IStorage storage, String fullPath) {
			this.storage = storage;
			this.fullPath = fullPath;
		}

		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			if (adapter == IStorage.class) {
				return storage;
			}
			return storage.getAdapter(adapter);
		}

		public String getCharset() throws CoreException {
			if (storage instanceof IEncodedStreamContentAccessor) {
				return ((IEncodedStreamContentAccessor)storage).getCharset();
			}
			return null;
		}

		public InputStream getContents() throws CoreException {
			return storage.getContents();
		}

		public Image getImage() {
			return CompareUI.getImage(getType());
		}

		public String getName() {
			return fullPath;
		}

		public String getType() {
			String name = getName();
			if (name != null) {
				int index = name.lastIndexOf('.');
				if (index == -1) {
					return ""; //$NON-NLS-1$
				}
				if (index == (name.length() - 1)) {
					return ""; //$NON-NLS-1$
				}
				return name.substring(index + 1);
			}
			return ITypedElement.FOLDER_TYPE;
		}
	}
}
