/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - refactorings
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=459131">459131</a>
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("nls")
public class TestBug459131 extends CompareTestCase {

	private IModelResolver resolver;

	private NullProgressMonitor monitor;

	@SuppressWarnings("restriction")
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry().getBestResolverFor(null);
		monitor = new NullProgressMonitor();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test models:
	 * <p>
	 * On left side: R1 -> R2 -> R3
	 * </p>
	 * <p>
	 * On right side: R1 -> R2 -> R3
	 * </p>
	 * <p>
	 * 1 change in R2 fragment and 1 change in R3 fragment. Then update R3 right model to have 1 change in R2
	 * fragment and no change in R3 fragment.
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateResource() throws Exception {

		final IProject iProject = project.getProject();

		final File leftFile1 = project.getOrCreateFile(iProject, "left" + File.separator + "R1.nodes");
		final File leftFile2 = project.getOrCreateFile(iProject, "left" + File.separator + "R2.nodes");
		final File leftFile3 = project.getOrCreateFile(iProject, "left" + File.separator + "R3.nodes");

		final File rightFile1 = project.getOrCreateFile(iProject, "right" + File.separator + "R1.nodes");
		final File rightFile2 = project.getOrCreateFile(iProject, "right" + File.separator + "R2.nodes");
		final File rightFile3 = project.getOrCreateFile(iProject, "right" + File.separator + "R3.nodes");

		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.ui.tests");
		URL entryLeftFile1 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/left/R1.nodes");
		URL entryLeftFile2 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/left/R2.nodes");
		URL entryLeftFile3 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/left/R3.nodes");
		URL entryRightFile1 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R1.nodes");
		URL entryRightFile2 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R2.nodes");
		URL entryRightFile3 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R3.nodes");
		URL leftFile1URL = FileLocator.toFileURL(entryLeftFile1);
		URL leftFile2URL = FileLocator.toFileURL(entryLeftFile2);
		URL leftFile3URL = FileLocator.toFileURL(entryLeftFile3);
		URL rightFile1URL = FileLocator.toFileURL(entryRightFile1);
		URL rightFile2URL = FileLocator.toFileURL(entryRightFile2);
		URL rightFile3URL = FileLocator.toFileURL(entryRightFile3);
		copyFile(new File(leftFile1URL.toURI()), leftFile1);
		copyFile(new File(leftFile2URL.toURI()), leftFile2);
		copyFile(new File(leftFile3URL.toURI()), leftFile3);
		copyFile(new File(rightFile1URL.toURI()), rightFile1);
		copyFile(new File(rightFile2URL.toURI()), rightFile2);
		copyFile(new File(rightFile3URL.toURI()), rightFile3);

		IFile iLeftFile1 = project.getIFile(iProject, leftFile1);
		IFile iLeftFile2 = project.getIFile(iProject, leftFile2);
		IFile iLeftFile3 = project.getIFile(iProject, leftFile3);

		IFile iRightFile1 = project.getIFile(iProject, rightFile1);
		IFile iRightFile2 = project.getIFile(iProject, rightFile2);
		IFile iRightFile3 = project.getIFile(iProject, rightFile3);

		// 1 change in R2 fragment and 1 change in R3 fragment
		SynchronizationModel syncModel = resolver.resolveLocalModels(iLeftFile1, iRightFile1, null, monitor);

		StorageTraversal leftTraversal = syncModel.getLeftTraversal();
		assertEquals(3, leftTraversal.getStorages().size());
		assertTrue(leftTraversal.getStorages().contains(iLeftFile1));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile2));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile3));

		StorageTraversal rightTraversal = syncModel.getRightTraversal();
		assertEquals(3, rightTraversal.getStorages().size());
		assertTrue(rightTraversal.getStorages().contains(iRightFile1));
		assertTrue(rightTraversal.getStorages().contains(iRightFile2));
		assertTrue(rightTraversal.getStorages().contains(iRightFile3));

		// Update R3 right model
		// 1 change in R2 fragment and no change in R3 fragment
		iRightFile3.setContents(iLeftFile3.getContents(), IResource.FORCE, monitor);

		syncModel = resolver.resolveLocalModels(iLeftFile1, iRightFile1, null, monitor);

		leftTraversal = syncModel.getLeftTraversal();
		assertEquals(3, leftTraversal.getStorages().size());
		assertTrue(leftTraversal.getStorages().contains(iLeftFile1));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile2));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile3));

		rightTraversal = syncModel.getRightTraversal();
		assertEquals(3, rightTraversal.getStorages().size());
		assertTrue(rightTraversal.getStorages().contains(iRightFile1));
		assertTrue(rightTraversal.getStorages().contains(iRightFile2));
		assertTrue(rightTraversal.getStorages().contains(iRightFile3));

	}

	/**
	 * Test models:
	 * <p>
	 * On left side: R4 -> R5 -> R6
	 * </p>
	 * <p>
	 * On right side: R4 -> R5 -> R6
	 * </p>
	 * <p>
	 * 1 change in R5 fragment and 1 change in R6 fragment. Then brake dependency between R5 & R6 right models
	 * to delete R3 right model from the logical model.
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBrakeResourceDependency() throws Exception {

		final IProject iProject = project.getProject();

		final File leftFile4 = project.getOrCreateFile(iProject, "left" + File.separator + "R4.nodes");
		final File leftFile5 = project.getOrCreateFile(iProject, "left" + File.separator + "R5.nodes");
		final File leftFile6 = project.getOrCreateFile(iProject, "left" + File.separator + "R6.nodes");

		final File rightFile4 = project.getOrCreateFile(iProject, "right" + File.separator + "R4.nodes");
		final File rightFile5 = project.getOrCreateFile(iProject, "right" + File.separator + "R5.nodes");
		final File newRightFile5 = project.getOrCreateFile(iProject,
				"right" + File.separator + "R5NEW.nodes");
		final File rightFile6 = project.getOrCreateFile(iProject, "right" + File.separator + "R6.nodes");

		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.ui.tests");
		URL entryLeftFile4 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/left/R4.nodes");
		URL entryLeftFile5 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/left/R5.nodes");
		URL entryLeftFile6 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/left/R6.nodes");
		URL entryRightFile4 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R4.nodes");
		URL entryRightFile5 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R5.nodes");
		URL newEntryRightFile5 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R5NEW.nodes");
		URL entryRightFile6 = bundle
				.getEntry("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/_459131/right/R6.nodes");
		URL leftFile4URL = FileLocator.toFileURL(entryLeftFile4);
		URL leftFile5URL = FileLocator.toFileURL(entryLeftFile5);
		URL leftFile6URL = FileLocator.toFileURL(entryLeftFile6);
		URL rightFile4URL = FileLocator.toFileURL(entryRightFile4);
		URL rightFile5URL = FileLocator.toFileURL(entryRightFile5);
		URL newRightFile5URL = FileLocator.toFileURL(newEntryRightFile5);
		URL rightFile6URL = FileLocator.toFileURL(entryRightFile6);
		copyFile(new File(leftFile4URL.toURI()), leftFile4);
		copyFile(new File(leftFile5URL.toURI()), leftFile5);
		copyFile(new File(leftFile6URL.toURI()), leftFile6);
		copyFile(new File(rightFile4URL.toURI()), rightFile4);
		copyFile(new File(rightFile5URL.toURI()), rightFile5);
		copyFile(new File(newRightFile5URL.toURI()), newRightFile5);
		copyFile(new File(rightFile6URL.toURI()), rightFile6);

		IFile iLeftFile4 = project.getIFile(iProject, leftFile4);
		IFile iLeftFile5 = project.getIFile(iProject, leftFile5);
		IFile iLeftFile6 = project.getIFile(iProject, leftFile6);

		IFile iRightFile4 = project.getIFile(iProject, rightFile4);
		IFile iRightFile5 = project.getIFile(iProject, rightFile5);
		IFile newIRightFile5 = project.getIFile(iProject, newRightFile5);
		IFile iRightFile6 = project.getIFile(iProject, rightFile6);

		// 1 change in R2 fragment and 1 change in R3 fragment
		SynchronizationModel syncModel = resolver.resolveLocalModels(iLeftFile4, iRightFile4, null, monitor);

		StorageTraversal leftTraversal = syncModel.getLeftTraversal();
		assertEquals(3, leftTraversal.getStorages().size());
		assertTrue(leftTraversal.getStorages().contains(iLeftFile4));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile5));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile6));

		StorageTraversal rightTraversal = syncModel.getRightTraversal();
		assertEquals(3, rightTraversal.getStorages().size());
		assertTrue(rightTraversal.getStorages().contains(iRightFile4));
		assertTrue(rightTraversal.getStorages().contains(iRightFile5));
		assertTrue(rightTraversal.getStorages().contains(iRightFile6));

		// Brake dependency between R5 & R6 right models.
		iRightFile5.setContents(newIRightFile5.getContents(), IResource.FORCE, monitor);

		syncModel = resolver.resolveLocalModels(iLeftFile4, iRightFile4, null, monitor);

		leftTraversal = syncModel.getLeftTraversal();
		assertEquals(3, leftTraversal.getStorages().size());
		assertTrue(leftTraversal.getStorages().contains(iLeftFile4));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile5));
		assertTrue(leftTraversal.getStorages().contains(iLeftFile6));

		rightTraversal = syncModel.getRightTraversal();
		assertEquals(2, rightTraversal.getStorages().size());
		assertTrue(rightTraversal.getStorages().contains(iRightFile4));
		assertTrue(rightTraversal.getStorages().contains(iRightFile5));
		assertFalse(rightTraversal.getStorages().contains(iRightFile6));

	}
}
