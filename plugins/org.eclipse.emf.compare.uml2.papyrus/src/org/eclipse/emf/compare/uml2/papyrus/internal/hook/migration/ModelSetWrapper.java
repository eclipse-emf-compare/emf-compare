/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.papyrus.internal.hook.migration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.papyrus.infra.core.resource.ModelSet;

/**
 * This class wraps a resource set into a ModelSet with minimal changes to be compliant to the expected
 * Profile Migration mechanism of Eclipse Luna.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class ModelSetWrapper extends ModelSet {

	/**
	 * Mapping of resources to their read-only setting. Needed when a new editing domain is created.
	 */
	private Map<Resource, Boolean> resourceToReadOnlyMap = new HashMap<Resource, Boolean>();

	/**
	 * The resource set being wrapped.
	 */
	private ResourceSet resourceSet;

	/**
	 * Constructor.
	 * 
	 * @param resourceSet
	 *            resource set to be wrapped.
	 */
	public ModelSetWrapper(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}

	/**
	 * Ensure that the given resource has the specified readOnly setting in the
	 * {@link #getTransactionalEditingDomain() editing domain} of this model set.
	 * 
	 * @param resource
	 *            resource within this resource set
	 * @param readOnly
	 *            true if resource should be readOnly, false otherwise
	 */
	public void setReadOnly(Resource resource, Boolean readOnly) {
		resourceToReadOnlyMap.put(resource, readOnly);
	}

	@Override
	public synchronized TransactionalEditingDomain getTransactionalEditingDomain() {
		final TransactionalEditingDomainImpl domain = new TransactionalEditingDomainImpl(
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		domain.setResourceToReadOnlyMap(resourceToReadOnlyMap);
		return domain;
	}

	@Override
	public Registry getPackageRegistry() {
		return resourceSet.getPackageRegistry(); // delegate to wrapped resourceSet
	}

}
