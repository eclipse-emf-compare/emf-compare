/*******************************************************************************
 * Copyright (c) 2016, 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *     Philip Langer - bug 516484
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.papyrus.internal.hook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.hook.AbstractResourceSetHooks;
import org.eclipse.emf.compare.uml2.papyrus.internal.hook.migration.StereotypeApplicationRepair;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.uml.modelrepair.internal.stereotypes.IRepairAction;
import org.eclipse.papyrus.uml.modelrepair.internal.stereotypes.ZombieStereotypesDescriptor;
import org.eclipse.papyrus.uml.tools.model.UmlModel;

/**
 * This class migrates missing UML stereotype applications before the comparison, if possible. For any missing
 * stereotype application, we aim to find an available profile definition (EPackage) that provides the
 * stereotype. If such a definition can be found, we migrate to the respective profile and stereotype
 * applications. If no definition can be found, the model is left unchanged. Profile definitions are searched
 * based on the URI of the missing stereotypes package URI.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class ProfileMigrationHook extends AbstractResourceSetHooks {

	@Override
	public void postLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
		final List<Resource> umlResources = getUMLResources(resourceSet);
		if (umlResources.isEmpty()) {
			return; // we are not responsible
		}

		for (final Resource umlResource : umlResources) {
			repairProfileApplications(umlResource);
		}
	}

	@Override
	public boolean isHookFor(Collection<? extends URI> uris) {
		for (final URI uri : uris) {
			if (isUMLResource(uri)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given URI represents a UML resource.
	 * 
	 * @param uri
	 *            URI to check
	 * @return true if the given URI represents a UML resource, false otherwise
	 */
	private boolean isUMLResource(final URI uri) {
		return uri != null && UmlModel.UML_FILE_EXTENSION.equals(uri.fileExtension());
	}

	/**
	 * Checks if the given resource is a UML resource based on its URI. If the given resource is null, false
	 * is returned.
	 * 
	 * @param resource
	 *            resource to check
	 * @return true if the given resource represents a UML resource, false otherwise.
	 * @see #isUMLResource(URI)
	 */
	private boolean isUMLResource(final Resource resource) {
		return resource != null && isUMLResource(resource.getURI());
	}

	/**
	 * Filters all UML resources from the given resource set. If no UML resources can be found, an empty list
	 * is returned.
	 * 
	 * @param resourceSet
	 *            loaded resource set
	 * @return all UML resources from the given resource set
	 * @see #isUMLResource(Resource)
	 */
	private List<Resource> getUMLResources(final ResourceSet resourceSet) {
		final List<Resource> umlResources = new ArrayList<Resource>();
		for (final Resource resource : resourceSet.getResources()) {
			if (isUMLResource(resource)) {
				umlResources.add(resource);
			}
		}
		return umlResources;
	}

	/**
	 * Repairs the profile applications of missing stereotypes, if possible, by first analyzing the resource
	 * for missing stereotypes and then delegating to the model repair mechanism provided by Papyrus.
	 * 
	 * @param resource
	 *            resource to be repaired
	 */
	protected void repairProfileApplications(final Resource resource) {
		if (resource == null) {
			return; // nothing to repair
		}

		final StereotypeApplicationRepair repair = new StereotypeApplicationRepair(resource);
		try {
			final ZombieStereotypesDescriptor stereotypesDescriptor = repair.repair();
			if (stereotypesDescriptor == null || !stereotypesDescriptor.hasZombies()) {
				return; // nothing to repair
			}

			// for each schema (missing EPackages) try to repair the respective stereotype applications
			for (final IAdaptable schema : stereotypesDescriptor.getZombieSchemas()) {
				// the stereotype descriptor already provides the most suitable repair action
				// deletion for orphans (stereotypes whose base element is missing)
				// profile migration for zombies (stereotypes whose defining package can not be found)
				final IRepairAction repairAction = stereotypesDescriptor.getSuggestedRepairAction(schema);
				if (repairAction != null) {
					// execute any suggested action
					stereotypesDescriptor.repair(schema, repairAction, new BasicDiagnostic(),
							new NullProgressMonitor());
				}
			}
		} finally {
			repair.dispose();
		}
	}
}
