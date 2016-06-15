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
package org.eclipse.emf.compare.ide.ui.internal.logical.view;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFResourceMapping;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;

/**
 * Util methods, for the Logical Model View handlers.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public final class LogicalModelViewHandlerUtil {

	/**
	 * Number of ticks consumed by the
	 * {@link EMFResourceMapping#getTraversals(ResourceMappingContext, IProgressMonitor)} method.
	 */
	private static final int GET_TRAVERSALS_TICKS = 15;

	/** Number of ticks consumed by the {@link #getResourceMappings(IFile, IProgressMonitor)} method. */
	private static final int GET_RESOURCES_MAPPING_TICKS = 85;

	/** The model descriptor of the EMF Model Provider. Used to check EMF Compare compliance on files. */
	private static IModelProviderDescriptor modelDescriptor = ModelProvider
			.getModelProviderDescriptor(EMFModelProvider.PROVIDER_ID);

	/**
	 * Constructor.
	 */
	private LogicalModelViewHandlerUtil() {
	}

	/**
	 * Get the logical model associated with the given file.
	 * 
	 * @param file
	 *            the given file to compute the logical model.
	 * @param monitor
	 *            to monitor the process.
	 * @return the synchronization model associated with the given file.
	 */
	public static Collection<SynchronizationModel> getSynchronizationModels(IFile file,
			IProgressMonitor monitor) {
		final Collection<SynchronizationModel> logicalModels = Sets.newHashSet();
		if (file == null) {
			return null;
		}
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		Collection<EMFResourceMapping> resourceMappings = getResourceMappings(file,
				subMonitor.newChild(GET_RESOURCES_MAPPING_TICKS));
		subMonitor.worked(GET_RESOURCES_MAPPING_TICKS);
		SubMonitor subMonitorLoop = subMonitor.newChild(GET_TRAVERSALS_TICKS)
				.setWorkRemaining(resourceMappings.size());
		for (ResourceMapping resourceMapping : resourceMappings) {
			if (resourceMapping instanceof EMFResourceMapping) {
				try {
					((EMFResourceMapping)resourceMapping).getTraversals(ResourceMappingContext.LOCAL_CONTEXT,
							subMonitorLoop.newChild(1));
					logicalModels.add(((EMFResourceMapping)resourceMapping).getLatestModel());
				} catch (CoreException e) {
					EMFCompareIDEUIPlugin.getDefault().log(e);
				}
			}
		}
		return logicalModels;
	}

	/**
	 * Get the resources of the given logical models.
	 * 
	 * @param logicalModels
	 *            the logical models.
	 * @param monitor
	 *            to monitor the process.
	 * @return the resources of the given logical models.
	 */
	public static Collection<IResource> getLogicalModelResources(
			Collection<SynchronizationModel> logicalModels, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		SubMonitor subMonitorLoop = subMonitor.newChild(100).setWorkRemaining(logicalModels.size());
		final Collection<IResource> resources = Sets.newHashSet();
		for (Iterator<SynchronizationModel> it = logicalModels.iterator(); it.hasNext();) {
			SynchronizationModel logicalModel = it.next();
			resources.addAll(logicalModel.getResources());
			subMonitorLoop.newChild(1);
		}
		return resources;
	}

	/**
	 * Check if the given file is a model compliant with EMF Compare.
	 * 
	 * @param file
	 *            the file to test.
	 * @return true if the file is compliant, false otherwise.
	 */
	public static boolean isEMFCompareCompliantFile(IFile file) {
		try {
			IResource[] resources = modelDescriptor.getMatchingResources(new IResource[] {file, });
			if (resources.length > 0) {
				return true;
			}
		} catch (CoreException e) {
			EMFCompareIDEUIPlugin.getDefault().log(e);
		}
		return false;
	}

	/**
	 * This will query the EMF model provider on the given file and list all EMF mappings available for that
	 * file.
	 * 
	 * @param file
	 *            The file for which we need the associated resource mappings.
	 * @param monitor
	 *            to monitor the process.
	 * @return All EMF mappings available for that file.
	 */
	private static Collection<EMFResourceMapping> getResourceMappings(IFile file, IProgressMonitor monitor) {
		final Set<EMFResourceMapping> mappings = Sets.newLinkedHashSet();
		if (isEMFCompareCompliantFile(file)) {
			try {
				// get mappings from model provider if there are matching resources
				ModelProvider model = modelDescriptor.getModelProvider();
				ResourceMapping[] modelMappings = model.getMappings(file,
						ResourceMappingContext.LOCAL_CONTEXT, monitor);
				for (ResourceMapping mapping : modelMappings) {
					if (mapping instanceof EMFResourceMapping) {
						mappings.add((EMFResourceMapping)mapping);
					}
				}
			} catch (CoreException e) {
				EMFCompareIDEUIPlugin.getDefault().log(e);
			}
		}
		return mappings;
	}
}
