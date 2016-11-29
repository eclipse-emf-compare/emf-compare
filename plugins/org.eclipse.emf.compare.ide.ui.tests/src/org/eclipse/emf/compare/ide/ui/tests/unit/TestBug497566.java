/**
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.StreamAccessorStorage;
import org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"restriction", "nls" })
public class TestBug497566 extends CompareTestCase {

	/**
	 * Uncontrol an element. The element uncontrolled is in a fragment in the right and origin resources. The
	 * element is in the root resource in the left resource (no fragment on the left side). After the merge,
	 * the right fragment is empty.
	 * 
	 * @throws IOException
	 * @throws CoreException
	 * @throws URISyntaxException
	 */
	@Test
	public void testUncontrolElementCausesEmptyFragment()
			throws IOException, CoreException, URISyntaxException {

		IComparisonScope scope = initComparison("_497566/empty");
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		BatchMerger batchMerger = new BatchMerger(EMFCompareRCPPlugin.getDefault().getMergerRegistry());

		ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
		ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
		ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);
		EcoreUtil.resolveAll(originResourceSet);

		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(rightResourceSet.getResources().get(0).getURI().lastSegment(), "right.nodes");
		assertEquals(rightResourceSet.getResources().get(1).getURI().lastSegment(), "fragment.nodes");
		assertEquals(2, originResourceSet.getResources().size());

		batchMerger.copyAllLeftToRight(comparison.getDifferences(), null);

		ResourceUtil.saveAllResources(leftResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(rightResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(originResourceSet, Collections.emptyMap());

		// After merge, the right fragment is empty so it has been deleted in the right resourceSet
		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(1, rightResourceSet.getResources().size());
		assertEquals(rightResourceSet.getResources().get(0).getURI().lastSegment(), "right.nodes");
		assertEquals(2, originResourceSet.getResources().size());

		// The right fragment (file) must have also been deleted from the file system
		IResource fragmentFile = ResourcesPlugin.getWorkspace().getRoot()
				.findMember("Project-1/right/fragment.nodes");
		assertNull(fragmentFile);
	}

	/**
	 * Uncontrol an element. The element uncontrolled is in a fragment in the right and origin resources. The
	 * element is in the root resource in the left resource (no fragment on the left side). After the merge,
	 * the right fragment is not empty, because it is not the only root element in the fragment.
	 * 
	 * @throws IOException
	 * @throws CoreException
	 * @throws URISyntaxException
	 */
	@Test
	public void testUncontrolElementCausesNotEmptyFragment()
			throws IOException, CoreException, URISyntaxException {

		IComparisonScope scope = initComparison("_497566/notempty");
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		BatchMerger batchMerger = new BatchMerger(EMFCompareRCPPlugin.getDefault().getMergerRegistry());

		ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
		ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
		ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);
		EcoreUtil.resolveAll(originResourceSet);

		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(rightResourceSet.getResources().get(0).getURI().lastSegment(), "right.nodes");
		assertEquals(rightResourceSet.getResources().get(1).getURI().lastSegment(), "fragment.nodes");
		assertEquals(2, originResourceSet.getResources().size());

		EList<Diff> differences = comparison.getDifferences();
		Iterable<Diff> filter2 = filter(differences, onEObject("root.fragmented"));
		batchMerger.copyAllLeftToRight(filter2, null);

		ResourceUtil.saveAllResources(leftResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(rightResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(originResourceSet, Collections.emptyMap());

		// After merge, the right fragment is not empty so it has not been deleted in the right resourceSet
		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(2, originResourceSet.getResources().size());

		// The right fragment (file) must have also been deleted from the file system
		IResource fragmentFile = ResourcesPlugin.getWorkspace().getRoot()
				.findMember("Project-1/right/fragment.nodes");
		assertNotNull(fragmentFile);
	}

	/**
	 * Uncontrol elements. The elements uncontrolled are in a fragment in the right and origin resources. The
	 * elements are in the root resource in the left resource (no fragment on the left side). After the merge
	 * of the first root fragment element, the right fragment is not empty, because it is not the only root
	 * element in the fragment. After the second merge, the right fragment becomes empty.
	 * 
	 * @throws IOException
	 * @throws CoreException
	 * @throws URISyntaxException
	 */
	@Test
	public void testUncontrolElementsCausesEmptyFragmentAfterMultipleMerge()
			throws IOException, CoreException, URISyntaxException {

		IComparisonScope scope = initComparison("_497566/empty2");
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		BatchMerger batchMerger = new BatchMerger(EMFCompareRCPPlugin.getDefault().getMergerRegistry());

		ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
		ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
		ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);
		EcoreUtil.resolveAll(originResourceSet);

		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(rightResourceSet.getResources().get(0).getURI().lastSegment(), "right.nodes");
		assertEquals(rightResourceSet.getResources().get(1).getURI().lastSegment(), "fragment.nodes");
		assertEquals(2, originResourceSet.getResources().size());

		EList<Diff> differences = comparison.getDifferences();
		Iterable<Diff> filter2 = filter(differences, onEObject("root.fragmented"));
		batchMerger.copyAllLeftToRight(filter2, null);

		ResourceUtil.saveAllResources(leftResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(rightResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(originResourceSet, Collections.emptyMap());

		// After the first merge , the right fragment is not empty so it has not been deleted in the right
		// resourceSet
		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(2, originResourceSet.getResources().size());

		// The right fragment (file) must have also been deleted from the file system
		IResource fragmentFile = ResourcesPlugin.getWorkspace().getRoot()
				.findMember("Project-1/right/fragment.nodes");
		assertNotNull(fragmentFile);

		// 2nd merge
		comparison = EMFCompare.builder().build().compare(scope);

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);
		EcoreUtil.resolveAll(originResourceSet);

		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(2, rightResourceSet.getResources().size());
		assertEquals(rightResourceSet.getResources().get(0).getURI().lastSegment(), "right.nodes");
		assertEquals(rightResourceSet.getResources().get(1).getURI().lastSegment(), "fragment.nodes");
		assertEquals(2, originResourceSet.getResources().size());

		batchMerger.copyAllLeftToRight(comparison.getDifferences(), null);

		ResourceUtil.saveAllResources(leftResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(rightResourceSet, Collections.emptyMap());
		ResourceUtil.saveAllResources(originResourceSet, Collections.emptyMap());

		// After the last merge, the right fragment is empty so it has been deleted in the right resourceSet
		assertEquals(1, leftResourceSet.getResources().size());
		assertEquals(1, rightResourceSet.getResources().size());
		assertEquals(rightResourceSet.getResources().get(0).getURI().lastSegment(), "right.nodes");
		assertEquals(2, originResourceSet.getResources().size());

		// The right fragment (file) must have also been deleted from the file system
		fragmentFile = ResourcesPlugin.getWorkspace().getRoot().findMember("Project-1/right/fragment.nodes");
		assertNull(fragmentFile);

	}

	private IComparisonScope initComparison(String projectName)
			throws IOException, CoreException, URISyntaxException {

		final IProject iProject = project.getProject();

		final File leftFile1 = project.getOrCreateFile(iProject, "left/left.nodes");
		final File rightFile1 = project.getOrCreateFile(iProject, "right/right.nodes");
		final File rightFragment1 = project.getOrCreateFile(iProject, "right/fragment.nodes");
		final File originFile1 = project.getOrCreateFile(iProject, "origin/origin.nodes");
		final File originFragment1 = project.getOrCreateFile(iProject, "origin/fragment.nodes");

		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.ui.tests");

		IFile iLeftFile1 = project.getIFile(iProject, leftFile1);
		copy(testPath(projectName + "/left/left.nodes"), iLeftFile1, bundle);

		IFile iRightFile1 = project.getIFile(iProject, rightFile1);
		copy(testPath(projectName + "/right/right.nodes"), iRightFile1, bundle);
		IFile iRightFragmentFile1 = project.getIFile(iProject, rightFragment1);
		copy(testPath(projectName + "/right/fragment.nodes"), iRightFragmentFile1, bundle);

		IFile iOriginFile1 = project.getIFile(iProject, originFile1);
		copy(testPath(projectName + "/origin/origin.nodes"), iOriginFile1, bundle);
		IFile iOriginFragmentFile1 = project.getIFile(iProject, originFragment1);
		copy(testPath(projectName + "/origin/fragment.nodes"), iOriginFragmentFile1, bundle);

		final ITypedElement left = new StorageTypedElement(iLeftFile1, iLeftFile1.getFullPath().toString());
		final ITypedElement right = new StorageTypedElement(iRightFile1,
				iRightFile1.getFullPath().toString());
		final ITypedElement origin = new StorageTypedElement(iOriginFile1,
				iOriginFile1.getFullPath().toString());

		IStorage leftStorage = PlatformElementUtil.findFile(left);
		if (leftStorage == null) {
			leftStorage = StreamAccessorStorage.fromTypedElement(left);
		}
		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftStorage);
		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), null);
		final IComparisonScope scope = scopeBuilder.build(left, right, origin, new NullProgressMonitor());

		return scope;
	}

	protected void copy(IPath from, IFile target, Bundle bundle) throws CoreException, IOException {
		target.setContents(FileLocator.openStream(bundle, from, false), true, false, null);
	}

	protected Path testPath(String dataRelativePath) {
		return new Path("src/org/eclipse/emf/compare/ide/ui/tests/unit/data/" + dataRelativePath);
	}
}
