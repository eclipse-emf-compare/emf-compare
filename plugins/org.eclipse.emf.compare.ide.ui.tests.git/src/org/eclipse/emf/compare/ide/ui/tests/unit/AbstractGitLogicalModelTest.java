/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - extract super class AbstractGitLogicalModelTest
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.lib.Constants;
import org.junit.After;
import org.junit.Before;

public class AbstractGitLogicalModelTest extends CompareGitTestCase {

	protected static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	protected static final String BRANCH = Constants.R_HEADS + "branch";

	protected File file1;

	protected File file2;

	protected IFile iFile1;

	protected IFile iFile2;

	protected Resource resource1;

	protected Resource resource2;

	private ResourceSet resourceSet;

	protected IProject iProject;
	
	public AbstractGitLogicalModelTest() {
		super();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		iProject = project.getProject();
		resourceSet = new ResourceSetImpl();
		file1 = project.getOrCreateFile(iProject, "file1.ecore");
		file2 = project.getOrCreateFile(iProject, "file2.ecore");
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		resource1 = connectResource(iFile1, resourceSet);
		resource2 = connectResource(iFile2, resourceSet);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider)ModelProvider.getModelProviderDescriptor(
				EMFModelProvider.PROVIDER_ID).getModelProvider();
		emfModelProvider.clear();
		super.tearDown();
	}
	
	protected Resource createAndConnectResource(String fileName) throws Exception {
		final File file = project.getOrCreateFile(iProject, fileName);
		final IFile iFile = project.getIFile(iProject, file);
		return connectResource(iFile, resourceSet);
	}
	
	protected File getFile(String fileName) throws Exception{
		return project.getOrCreateFile(iProject, fileName);
	}

}
