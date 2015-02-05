/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
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

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		resolver = new ThreadedModelResolver();
		resolver.initialize();
		monitor = new NullProgressMonitor();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		resolver.dispose();
		super.tearDown();
	}

	@Test
	public void test() throws Exception {

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
		copyFile(leftFile3, rightFile3);

		iProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);

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

	private static void copyFile(File source, File dest) throws IOException {
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
