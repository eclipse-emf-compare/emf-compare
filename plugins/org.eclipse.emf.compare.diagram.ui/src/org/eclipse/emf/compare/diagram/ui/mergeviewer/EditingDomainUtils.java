/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ui.mergeviewer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.DiagramEditingDomainFactory;

/**
 * Utility Class to retrieve an editing domain.
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public final class EditingDomainUtils {

	/**
	 * Constructor.
	 */
	private EditingDomainUtils() {
		// no-op
	}
	
	
	/**
	 * Get or create an editing domain to handle the given object.
	 * @param object The object.
	 * @return The editing domain.
	 */
	public static TransactionalEditingDomain getOrCreateEditingDomain(Object object) {
		EditingDomain editingDomain = getEditingDomainFor(object);
		if (editingDomain == null || !(editingDomain instanceof TransactionalEditingDomain)) {
			if (object instanceof EObject) {
				editingDomain = registerEditingDomain((EObject)object);
			} else if (object instanceof List<?>) {
				final List<?> list = (List<?>)object;
				for (Object object2 : list) {
					editingDomain = getOrCreateEditingDomain(object2);
					if (editingDomain != null) {
						break;
					}
				}
			} else if (object instanceof Resource) {
				editingDomain = registerEditingDomain((Resource)object);
			} else if (object instanceof ResourceSet) {
				editingDomain = registerEditingDomain((ResourceSet)object);
			} else {
				// XXX: fix this case
			}
		}
		return (TransactionalEditingDomain)editingDomain;
	}

	/**
	 * Retrieve the editing domain for the given object.
	 * @param object the object.
	 * @return the editing domain.
	 */
	private static EditingDomain getEditingDomainFor(Object object) {
		EditingDomain editingDomain = null;
		if (object instanceof IEditingDomainProvider) {
			editingDomain = ((IEditingDomainProvider)object)
					.getEditingDomain();
		} else if (object instanceof EObject) {
			editingDomain = getEditingDomainFor((EObject)object);
		} else if (object instanceof FeatureMap.Entry) {
			editingDomain = getEditingDomainFor(((FeatureMap.Entry)object).getValue());
		} else if (object instanceof IWrapperItemProvider) {
			editingDomain = getEditingDomainFor(((IWrapperItemProvider)object)
					.getValue());
		}
		return editingDomain;

	}

	/**
	 * Retrieve the editing domain for the given {@link EObject}.
	 * @param object The {@link EObject}
	 * @return The editing domain.
	 */
	private static EditingDomain getEditingDomainFor(EObject object) {
		final Resource resource = object.eResource();
		if (resource != null) {
			return getEditingDomainFor(resource);
		}

		return null;
	}

	/**
	 * Retrieve the editing domain for the given {@link Resource}.
	 * @param resource The {@link Resource}
	 * @return The editing domain.
	 */
	private static EditingDomain getEditingDomainFor(Resource resource) {
		EditingDomain editingDomain = null;
		final IEditingDomainProvider editingDomainProvider = (IEditingDomainProvider)EcoreUtil
				.getExistingAdapter(resource, IEditingDomainProvider.class);
		if (editingDomainProvider != null) {
			editingDomain = editingDomainProvider.getEditingDomain();
		} else {
			final ResourceSet resourceSet = resource.getResourceSet();
			editingDomain = getEditingDomainFor(resourceSet);
		}
		return editingDomain;
	}

	/**
	 * Retrieve the editing domain for the given {@link ResourceSet}.
	 * @param resourceSet The {@link ResourceSet}
	 * @return The editing domain.
	 */
	private static EditingDomain getEditingDomainFor(ResourceSet resourceSet) {
		EditingDomain editingDomain = null;
		if (resourceSet instanceof IEditingDomainProvider) {
			editingDomain = ((IEditingDomainProvider)resourceSet)
					.getEditingDomain();
		} else if (resourceSet != null) {
			final IEditingDomainProvider editingDomainProvider = (IEditingDomainProvider)EcoreUtil
					.getExistingAdapter(resourceSet,
							IEditingDomainProvider.class);
			if (editingDomainProvider != null) {
				editingDomain = editingDomainProvider.getEditingDomain();
			}
		}
		return editingDomain;
	}

	/** Register the editing domain from the given {@link EObject}.
	 * @param eObject The {@link EObject}
	 * @return The editing domain.
	 */
	private static TransactionalEditingDomain registerEditingDomain(EObject eObject) {
		TransactionalEditingDomain editingDomain = null;
		final Resource eResource = eObject.eResource();
		if (eResource != null) {
			editingDomain = registerEditingDomain(eResource);
		} else {
			// XXX: fix this case
		}
		return editingDomain;
	}

	/** Register the editing domain from the given {@link Resource}.
	 * @param eResource The {@link Resource}
	 * @return The editing domain.
	 */
	private static TransactionalEditingDomain registerEditingDomain(Resource eResource) {
		TransactionalEditingDomain editingDomain = null;
		final ResourceSet resourceSet = eResource.getResourceSet();
		if (resourceSet != null) {
			editingDomain = registerEditingDomain(resourceSet);
		} else {
			// XXX: fix this case
		}
		return editingDomain;
	}

	/** Register the editing domain from the given {@link ResourceSet}.
	 * @param resourceSet The {@link ResourceSet}
	 * @return The editing domain.
	 */
	private static TransactionalEditingDomain registerEditingDomain(ResourceSet resourceSet) {
		return DiagramEditingDomainFactory.getInstance().createEditingDomain(resourceSet);
	}
}
