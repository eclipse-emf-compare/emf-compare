/*******************************************************************************
 * Copyright (C) 2013 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.egit;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.GitTestRepository;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.MockSystemReader;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.TestProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * The set up and tear down of this class were mostly copied from org.eclipse.egit.core.test.GitTestCase.
 */
public class CompareGitTestCase {
	protected TestProject project;

	protected GitTestRepository repository;

	// The ".git" folder of the test repository
	private File gitDir;

	@BeforeClass
	public static void setUpClass() {
		// suppress auto-ignoring and auto-sharing to avoid interference
		IEclipsePreferences eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		eGitPreferences.putBoolean(GitCorePreferences.core_autoIgnoreDerivedResources, false);
		eGitPreferences.putBoolean(GitCorePreferences.core_autoShareProjects, false);
	}

	@Before
	public void setUp() throws Exception {
		// ensure there are no shared Repository instances left
		// when starting a new test
		Activator.getDefault().getRepositoryCache().clear();
		final MockSystemReader mockSystemReader = new MockSystemReader();
		SystemReader.setInstance(mockSystemReader);
		mockSystemReader.setProperty(Constants.GIT_CEILING_DIRECTORIES_KEY, ResourcesPlugin.getWorkspace()
				.getRoot().getLocation().toFile().getAbsoluteFile().toString());
		project = new TestProject();
		gitDir = new File(project.getProject().getWorkspace().getRoot().getRawLocation().toFile(),
				Constants.DOT_GIT);
		repository = new GitTestRepository(gitDir);
		repository.connect(project.getProject());
	}

	@After
	public void tearDown() throws Exception {
		repository.dispose();
		project.dispose();
		Activator.getDefault().getRepositoryCache().clear();
		if (gitDir.exists()) {
			FileUtils.delete(gitDir, FileUtils.RECURSIVE | FileUtils.RETRY);
		}
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

	protected void reload(Resource... resources) throws IOException {
		for (Resource resource : resources) {
			resource.getContents().clear();
			resource.unload();
		}
		// separate loop to reload so that we are sure everything has been unloaded
		for (Resource resource : resources) {
			resource.load(Collections.emptyMap());
		}
		// And a third loop to re-resolve every cross-references between the reloaded resources
		for (Resource resource : resources) {
			EcoreUtil.resolveAll(resource);
		}
	}

	protected void save(Resource... resources) throws IOException {
		for (Resource resource : resources) {
			resource.save(Collections.emptyMap());
		}
	}
}
