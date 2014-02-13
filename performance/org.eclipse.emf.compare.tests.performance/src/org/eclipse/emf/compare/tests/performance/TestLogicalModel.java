/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance;

import java.net.URL;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.osgi.framework.Bundle;

import data.models.Data;
import data.models.SmallInputData;
import data.models.StorageTypedElement;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLogicalModel extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestLogicalModel.class.getSimpleName());
	}

	@Test
	public void a_logicalModelUMLSmall() {
		try {
			PerformanceMonitor monitor = getPerformance().createMonitor("logicalModelUMLSmall");
			final Data data = new SmallInputData();
			
			Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.tests.performance");
			URL entry = bundle.getEntry("src/data/models/model_size_small/.project");
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void b_logicalModelUMLNominal() {
		try {
			PerformanceMonitor monitor = getPerformance().createMonitor("logicalModelUMLNominal");
			final Data data = new SmallInputData();
			
			Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.tests.performance");
			URL entry = bundle.getEntry("src/data/models/model_size_nominal/.project");
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
