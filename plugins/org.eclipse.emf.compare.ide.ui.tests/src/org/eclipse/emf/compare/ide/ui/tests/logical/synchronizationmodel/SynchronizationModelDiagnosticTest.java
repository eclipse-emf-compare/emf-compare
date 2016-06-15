/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.synchronizationmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.core.internal.resources.ResourceStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Tests that diagnostics not belonging to resources that are part of any storage traversal are not contained
 * in the diagnostic of the synchronization model.
 * 
 * @author Alexandra Buzila <abuzila@eclipsesource.com>
 */
@SuppressWarnings({"nls", "restriction" })
public class SynchronizationModelDiagnosticTest extends CompareTestCase {
	private static final String BUNDLE = "org.eclipse.emf.compare.ide.ui.tests";

	private static final String DATA_PATH = "src/org/eclipse/emf/compare/ide/ui/tests/logical/synchronizationmodel/diagnostic/data/";

	private static final String LEFT = "left";

	private static final String RIGHT = "right";

	private static final String ORIGIN = "origin";

	private static final String ROOT_CHILD_DIAGNOSTIC_MESSAGE_KEY = "root";

	private static final String DIAGNOSTIC_MESSAGE_KEY = "diagnosticMesg";

	/**
	 * Tests the diagnostic of a synchronization model returned by a {@link IModelResolver}.
	 * <p>
	 * The synchronization model contains a two valid ecores that form of a logical model (model1 and model2)
	 * and a third valid model (model3) that is outside of the logical model. Additionally, the project that
	 * is the root of these files, also contains a broken xmi file unreferenced by any other model, that the
	 * resolver will however try to analyze (resolution scope is
	 * {@link CrossReferenceResolutionScope#CONTAINER} by default). The resolver will use model1 and model3 as
	 * a starting points for the left and right side, respectively.<br>
	 * We expect that no diagnostic for the broken xmi file is included in the synchronization model, as the
	 * file is not part of our comparison scope.
	 */
	@Test
	public void testRealExampleSyncModelDiagnosticWithXMIExceptionOutsideLogicalModel()
			throws IOException, URISyntaxException, CoreException, InterruptedException {
		TestProject testProject = new TestProject("TEST_PROJECT");
		/* setup */
		IFile left = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile right = addModelFile(testProject, "model3.ecore");
		// incorrectly formatted XMI file
		addModelFile(testProject, "invalidXMI.ecore");

		IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(left);
		SynchronizationModel synchronizationModel = resolver.resolveLocalModels(left, right, null,
				new NullProgressMonitor());

		/* sanity checks */
		assertEquals(2, synchronizationModel.getLeftTraversal().getStorages().size());
		assertTrue(synchronizationModel.getLeftTraversal().getStorages().contains(left));
		assertTrue(synchronizationModel.getLeftTraversal().getStorages().contains(m2));

		assertEquals(1, synchronizationModel.getRightTraversal().getStorages().size());
		assertTrue(synchronizationModel.getRightTraversal().getStorages().contains(right));

		/* verify */
		Diagnostic diagnostic = synchronizationModel.getDiagnostic();
		assertEquals(Diagnostic.OK, synchronizationModel.getDiagnostic().getSeverity());
		assertEquals(getDiagnosticMessage(DIAGNOSTIC_MESSAGE_KEY), diagnostic.getMessage());
		assertEquals(4, diagnostic.getChildren().size());

		// sync model root diagnostic
		Diagnostic syncRootDiagnostic = diagnostic.getChildren().get(0);
		assertEquals(Diagnostic.OK, syncRootDiagnostic.getSeverity());

		verifyTraversalDiagnostics(diagnostic);
	}

	/**
	 * Tests the diagnostic of a synchronization model returned by a {@link IModelResolver}.
	 * <p>
	 * The synchronization model contains a two valid ecores that form of a logical model (model1 and model2)
	 * and a third invalid model (broken xmi file).<br>
	 * We expect that an error diagnostic for the broken xmi file is included in the synchronization model, as
	 * the file is part of our comparison scope.
	 */
	@Test
	public void testRealExampleSyncModelDiagnosticWithXMIExceptionInLogicalModel()
			throws IOException, URISyntaxException, CoreException, InterruptedException {
		TestProject testProject = new TestProject("TEST_PROJECT");
		/* setup */
		IFile left = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile right = addModelFile(testProject, "invalidXMI.ecore");

		IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(left);
		SynchronizationModel synchronizationModel = resolver.resolveLocalModels(left, right, null,
				new NullProgressMonitor());

		/* sanity checks */
		assertEquals(2, synchronizationModel.getLeftTraversal().getStorages().size());
		assertTrue(synchronizationModel.getLeftTraversal().getStorages().contains(left));
		assertTrue(synchronizationModel.getLeftTraversal().getStorages().contains(m2));

		assertEquals(1, synchronizationModel.getRightTraversal().getStorages().size());
		assertTrue(synchronizationModel.getRightTraversal().getStorages().contains(right));

		/* verify */
		Diagnostic diagnostic = synchronizationModel.getDiagnostic();
		assertEquals(Diagnostic.ERROR, synchronizationModel.getDiagnostic().getSeverity());
		assertEquals(getDiagnosticMessage(DIAGNOSTIC_MESSAGE_KEY), diagnostic.getMessage());
		assertEquals(4, diagnostic.getChildren().size());

		// sync model root diagnostic
		Diagnostic syncRootDiagnostic = diagnostic.getChildren().get(0);
		assertEquals(Diagnostic.ERROR, syncRootDiagnostic.getSeverity());

		verifyTraversalDiagnostics(diagnostic);
	}

	/**
	 * Tests the diagnostic of a synchronization model returned by a {@link IModelResolver}.
	 * <p>
	 * The synchronization model contains an ecore model1 (left resolution starting point) with a broken
	 * reference and an unrelated ecore model3 (right resolution starting point).<br>
	 * We expect that an error diagnostic for the broken reference is included in the synchronization model,
	 * as the logical model of model1 is incomplete.
	 */
	@Test
	public void testRealExampleSyncModelDiagnosticWithDanglingReferenceInLogicalModel()
			throws IOException, URISyntaxException, CoreException, InterruptedException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		/* setup */
		// m1 will have a broken reference to m2 (m2 missing from project)
		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m3 = addModelFile(testProject, "model3.ecore");

		IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(m1);
		SynchronizationModel synchronizationModel = resolver.resolveLocalModels(m1, m3, null,
				new NullProgressMonitor());

		/* sanity checks */
		assertEquals(2, synchronizationModel.getLeftTraversal().getStorages().size());
		IStorage[] storages = Iterators
				.toArray(synchronizationModel.getLeftTraversal().getStorages().iterator(), IStorage.class);
		if (storages[0].equals(m1)) {
			assertTrue(storages[1].getFullPath().toString().contains("model2.ecore"));
		} else {
			assertTrue(storages[1].equals(m1));
			assertTrue(storages[0].getFullPath().toString().contains("model2.ecore"));
		}

		assertEquals(1, synchronizationModel.getRightTraversal().getStorages().size());
		assertTrue(synchronizationModel.getRightTraversal().getStorages().contains(m3));

		/* verify */
		Diagnostic diagnostic = synchronizationModel.getDiagnostic();

		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
		assertEquals(1, diagnostic.getData().size());
		assertEquals(synchronizationModel, diagnostic.getData().get(0));

		// sync model root diagnostic
		Diagnostic syncRootDiagnostic = diagnostic.getChildren().get(0);
		assertEquals(Diagnostic.ERROR, syncRootDiagnostic.getSeverity());

		verifyTraversalDiagnostics(diagnostic);
	}

	/**
	 * Tests the diagnostic of a synchronization model returned by a {@link IModelResolver}.
	 * <p>
	 * The synchronization model contains an ecore model1 (left resolution starting point) with a reference to
	 * another ecore model2 and an unrelated ecore model3 (right resolution starting point). Additionally, the
	 * project that is the root of these files, also contains a model unreferenced by any other model, with a
	 * dangling reference(dangling_ref.ecore), that the resolver will however try to analyze (resolution scope
	 * is {@link CrossReferenceResolutionScope#CONTAINER} by default). <br>
	 * We expect that an error diagnostic for the broken reference is included in the synchronization model,
	 * as the logical model of model1 is incomplete.
	 */
	@Test
	public void testSyncModelDiagnosticWithDanglingReferenceOutsideLogicalModel()
			throws IOException, URISyntaxException, CoreException, InterruptedException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		/* setup */
		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile m3 = addModelFile(testProject, "model3.ecore");
		// file with dangling ref
		addModelFile(testProject, "dangling_ref.ecore");

		IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(m1);
		SynchronizationModel synchronizationModel = resolver.resolveLocalModels(m1, m3, null,
				new NullProgressMonitor());

		/* sanity checks */
		assertEquals(2, synchronizationModel.getLeftTraversal().getStorages().size());
		assertTrue(synchronizationModel.getLeftTraversal().getStorages().contains(m1));
		assertTrue(synchronizationModel.getLeftTraversal().getStorages().contains(m2));

		assertEquals(1, synchronizationModel.getRightTraversal().getStorages().size());
		assertTrue(synchronizationModel.getRightTraversal().getStorages().contains(m3));

		/* verify */
		Diagnostic diagnostic = synchronizationModel.getDiagnostic();

		assertEquals(Diagnostic.OK, diagnostic.getSeverity());

		// sync model root diagnostic
		Diagnostic syncRootDiagnostic = diagnostic.getChildren().get(0);
		assertEquals(Diagnostic.OK, syncRootDiagnostic.getSeverity());

		verifyTraversalDiagnostics(diagnostic);
	}

	/**
	 * Tests that diagnostics not belonging to resources that are part of any storage traversal are not
	 * contained in the diagnostic of the synchronization model.
	 */
	@Test
	public void testSynchronizationModelDiagnosticsWithBasicDiagnosticOutsideTraversal()
			throws IOException, CoreException, URISyntaxException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile m3 = addModelFile(testProject, "model3.ecore");

		StorageTraversal leftTraversal = getStorageTraversal(m1, m2);
		StorageTraversal rightTraversal = getStorageTraversal(m1);

		BasicDiagnostic diagnostic = new BasicDiagnostic();
		/* add diagnostic for object that doesn't belong to the resource traversals */
		diagnostic.add(new BasicDiagnostic(Diagnostic.ERROR, BUNDLE, 0, "", new Object[] {m3 }));

		SynchronizationModel synchronizationModel = new SynchronizationModel(leftTraversal, rightTraversal,
				null, diagnostic);

		assertEquals(Diagnostic.OK, synchronizationModel.getDiagnostic().getSeverity());
		verifyTraversalDiagnostics(synchronizationModel.getDiagnostic());
	}

	/**
	 * Tests that diagnostics not belonging to resources that are part of any storage traversal are not
	 * contained in the diagnostic of the synchronization model.
	 */
	@Test
	public void testSynchronizationModelDiagnosticsWithResourceStatusOutsideTraversal()
			throws IOException, CoreException, URISyntaxException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");

		StorageTraversal leftTraversal = getStorageTraversal(m1, m2);
		StorageTraversal rightTraversal = getStorageTraversal(m1);

		BasicDiagnostic diagnostic = new BasicDiagnostic();
		/* add diagnostics for fictional objects that don't belong to the resource traversals */
		diagnostic.add(new BasicDiagnostic(Diagnostic.WARNING, BUNDLE, 0, "",
				new Object[] {new ResourceStatus(0, new Path("virtualPath1"), "") }));
		diagnostic.add(new BasicDiagnostic(Diagnostic.WARNING, BUNDLE, 0, "",
				new Object[] {new ResourceStatus(0, m1.getFullPath().append("virtualPathSegment"), "") }));

		SynchronizationModel synchronizationModel = new SynchronizationModel(leftTraversal, rightTraversal,
				null, diagnostic);

		assertEquals(Diagnostic.OK, synchronizationModel.getDiagnostic().getSeverity());
		verifyTraversalDiagnostics(synchronizationModel.getDiagnostic());
	}

	/**
	 * Tests that diagnostics not belonging to resources that are part of any storage traversal are not
	 * contained in the diagnostic of the synchronization model.
	 */
	@Test
	public void testSynchronizationModelDiagnosticsWithResourceDiagnosticOutsideTraversal()
			throws IOException, CoreException, URISyntaxException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");

		StorageTraversal leftTraversal = getStorageTraversal(m1, m2);
		StorageTraversal rightTraversal = getStorageTraversal(m1);

		BasicDiagnostic diagnostic = new BasicDiagnostic();
		diagnostic.add(new BasicDiagnostic(Diagnostic.WARNING, BUNDLE, 0, "",
				new Object[] {new XMIException("", "virtualLocation", 0, 0) }));

		SynchronizationModel synchronizationModel = new SynchronizationModel(leftTraversal, rightTraversal,
				null, diagnostic);

		assertEquals(Diagnostic.OK, synchronizationModel.getDiagnostic().getSeverity());
		verifyTraversalDiagnostics(synchronizationModel.getDiagnostic());
	}

	/**
	 * Tests that diagnostics belonging to resources that are part of any storage traversal are contained in
	 * the diagnostic of the synchronization model.
	 */
	@Test
	public void testSynchronizationModelDiagnosticsWithBasicDiagnosticInTraversal()
			throws IOException, CoreException, URISyntaxException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile m3 = addModelFile(testProject, "model3.ecore");

		StorageTraversal leftTraversal = getStorageTraversal(m1, m2);
		StorageTraversal rightTraversal = getStorageTraversal(m1, m3);

		BasicDiagnostic diagnostic = new BasicDiagnostic();
		diagnostic.add(new BasicDiagnostic(Diagnostic.WARNING, BUNDLE, 0, "", new Object[] {m3 }));

		SynchronizationModel synchronizationModel = new SynchronizationModel(leftTraversal, rightTraversal,
				null, diagnostic);

		assertEquals(Diagnostic.WARNING, synchronizationModel.getDiagnostic().getSeverity());
		verifyTraversalDiagnostics(synchronizationModel.getDiagnostic());
	}

	/**
	 * Tests that diagnostics belonging to resources that are part of any storage traversal are contained in
	 * the diagnostic of the synchronization model.
	 */
	@Test
	public void testSynchronizationModelDiagnosticsWithResourceStatusInTraversal()
			throws IOException, CoreException, URISyntaxException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile m3 = addModelFile(testProject, "model3.ecore");

		StorageTraversal leftTraversal = getStorageTraversal(m1, m2);
		StorageTraversal rightTraversal = getStorageTraversal(m1, m3);
		BasicDiagnostic diagnostic = new BasicDiagnostic();
		diagnostic.add(new BasicDiagnostic(Diagnostic.WARNING, BUNDLE, 0, "",
				new Object[] {new ResourceStatus(0, m3.getFullPath(), "") }));

		SynchronizationModel synchronizationModel = new SynchronizationModel(leftTraversal, rightTraversal,
				null, diagnostic);

		assertEquals(Diagnostic.WARNING, synchronizationModel.getDiagnostic().getSeverity());
		verifyTraversalDiagnostics(synchronizationModel.getDiagnostic());
	}

	/**
	 * Tests that diagnostics belonging to resources that are part of any storage traversal are contained in
	 * the diagnostic of the synchronization model.
	 */
	@Test
	public void testSynchronizationModelDiagnosticsWithResourceDiagnosticInTraversal()
			throws IOException, CoreException, URISyntaxException {
		TestProject testProject = new TestProject("TEST_PROJECT");

		IFile m1 = addModelFile(testProject, "model1.ecore");
		IFile m2 = addModelFile(testProject, "model2.ecore");
		IFile m3 = addModelFile(testProject, "model3.ecore");

		StorageTraversal leftTraversal = getStorageTraversal(m1, m2);
		StorageTraversal rightTraversal = getStorageTraversal(m1, m3);

		BasicDiagnostic diagnostic = new BasicDiagnostic();
		diagnostic.add(new BasicDiagnostic(Diagnostic.ERROR, BUNDLE, 0, "",
				new Object[] {new XMIException("", m3.getFullPath().toString(), 0, 0) }));

		SynchronizationModel synchronizationModel = new SynchronizationModel(leftTraversal, rightTraversal,
				null, diagnostic);

		assertEquals(Diagnostic.ERROR, synchronizationModel.getDiagnostic().getSeverity());
		verifyTraversalDiagnostics(synchronizationModel.getDiagnostic());
	}

	private void verifyTraversalDiagnostics(Diagnostic diagnostic) {
		Diagnostic rootDiagnostic = diagnostic.getChildren().get(0);
		assertEquals(getDiagnosticMessage(ROOT_CHILD_DIAGNOSTIC_MESSAGE_KEY), rootDiagnostic.getMessage());

		Diagnostic leftTraversalDiagnostic = diagnostic.getChildren().get(1);
		assertEquals(getDiagnosticMessage(LEFT), leftTraversalDiagnostic.getMessage());
		assertEquals(Diagnostic.OK, leftTraversalDiagnostic.getSeverity());

		Diagnostic originTraversalDiagnostic = diagnostic.getChildren().get(2);
		assertEquals(getDiagnosticMessage(ORIGIN), originTraversalDiagnostic.getMessage());
		assertEquals(Diagnostic.OK, originTraversalDiagnostic.getSeverity());

		Diagnostic rightTraversalDiagnostic = diagnostic.getChildren().get(3);
		assertEquals(getDiagnosticMessage(RIGHT), rightTraversalDiagnostic.getMessage());
		assertEquals(Diagnostic.OK, rightTraversalDiagnostic.getSeverity());
	}

	private String getDiagnosticMessage(String key) {
		return EMFCompareIDEUIMessages.getString("SynchronizationModel." + key);
	}

	private StorageTraversal getStorageTraversal(IStorage... storages) {
		HashSet<IStorage> set = Sets.newHashSet();
		Collections.addAll(set, storages);
		return new StorageTraversal(set);
	}

	private IFile addModelFile(TestProject testProject, String filePath)
			throws IOException, URISyntaxException, CoreException {
		// source
		final Bundle bundle = Platform.getBundle(BUNDLE);
		URL fileURL = FileLocator.toFileURL(bundle.getEntry(DATA_PATH + filePath));
		File dataSource = new File(fileURL.toURI());

		// destination
		File destination = testProject.getOrCreateFile(testProject.getProject(), filePath);
		copyFile(dataSource, destination);
		return testProject.getIFile(testProject.getProject(), destination);
	}

}
