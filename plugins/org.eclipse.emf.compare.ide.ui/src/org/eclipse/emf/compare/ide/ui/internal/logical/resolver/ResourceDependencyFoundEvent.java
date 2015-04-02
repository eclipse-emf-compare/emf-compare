/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * An event indicating that a resource dependency is found.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ResourceDependencyFoundEvent extends DependencyFoundEvent<URI> {

	/**
	 * Constructor.
	 * 
	 * @param from
	 *            The source of the dependency
	 * @param to
	 *            The target of the dependency
	 * @param parent
	 *            The object in the source causing the dependency
	 * @param feature
	 *            The feature through which the parent causes the dependency
	 */
	public ResourceDependencyFoundEvent(URI from, URI to, EObject parent, EStructuralFeature feature) {
		super(from, to, getParentUriIfContainmentReference(checkNotNull(parent), feature));
	}

	/**
	 * Returns the URI of the {@code parent} object causing this dependency, if it is the actual owner of the
	 * dependency.
	 * <p>
	 * The {@code parent} object is the actual owner if the {@code feature} through which the {@code parent}
	 * object refers to the dependency is a {@link EReference#isContainment() containment reference}.
	 * </p>
	 * 
	 * @param parent
	 *            The object in the source causing the dependency
	 * @param feature
	 *            The feature through which the parent causes the dependency
	 * @return the URI of the parent if it is the owner of the dependency, otherwise {@link Optional#absent()}
	 */
	private static Optional<URI> getParentUriIfContainmentReference(EObject parent, EStructuralFeature feature) {
		if (feature instanceof EReference && ((EReference)feature).isContainment()) {
			return Optional.of(getUri(parent));
		}
		return Optional.absent();
	}

	/**
	 * Returns the URI of the given {@code eObject}.
	 * 
	 * @param eObject
	 *            The object to get the URI of
	 * @return The URI of the given {@code eObject}
	 */
	public static URI getUri(EObject eObject) {
		return EcoreUtil.getURI(eObject);
	}

}
