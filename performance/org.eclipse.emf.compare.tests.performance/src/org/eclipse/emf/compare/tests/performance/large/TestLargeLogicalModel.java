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
package org.eclipse.emf.compare.tests.performance.large;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.osgi.framework.Bundle;

import data.models.Data;
import data.models.LargeInputData;
import data.models.StorageTypedElement;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLargeLogicalModel extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestLargeLogicalModel.class.getSimpleName());
	}
	
	@Test
	public void e_logicalModelUMLLarge() {
		try {
			PerformanceMonitor monitor = getPerformance().createMonitor("logicalModelUMLLarge");
			final Data data = new LargeInputData();
			
			Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.tests.performance");
			URL entry = bundle.getEntry("src/data/models/model_size_large/.project");
			URL fileURL = FileLocator.toFileURL(entry);
			IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(fileURL.getPath()));
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
			final IFile leftFile = project.getFile(new Path("original/model.uml"));
			final IFile rightFile = project.getFile(new Path("modified/model.uml"));
			final ITypedElement leftTypedElement = new StorageTypedElement(leftFile, leftFile.getFullPath().toOSString());
			final ITypedElement rightTypedElement = new StorageTypedElement(rightFile, rightFile.getFullPath().toOSString());
			
			monitor.measure(false, getStepsNumber(), new Runnable() {
				public void run() {
					data.logicalModel(leftTypedElement, rightTypedElement);
				}
			});
			data.dispose();
			project.close(new NullProgressMonitor());
			project.delete(false, new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	private IFile createProjects(Bundle bundle, final ResourceSet rightResourceSet,
			String sourceProjectName, String projectName, final List<IProject> projects) throws IOException,
			CoreException {
		IFile file = null;
		for (Resource right : rightResourceSet.getResources()) {
			//URIs pattern : bundleresource://149.fwk766258359/data/models/model_size_small_split/model_size_small_original_model/model.uml
			//We have to retrieve the second to last segment
			String projectPartName = right.getURI().segment(3);
			URL entry = bundle.getEntry("src/data/models/" + sourceProjectName + "/" + projectPartName + "/.project");
			URL fileURL = FileLocator.toFileURL(entry);
			IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(fileURL.getPath()));
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
			projects.add(project);
			if (file == null && projectPartName.equals(projectName)){
				file = project.getFile(new Path("model.uml"));
			}
		}
		return file;
	}
}
