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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.compare.scope.AbstractComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This implementation of a comparison scope can be supplied to EMF Compare if there was an error beyond
 * repair during the creation of the scope.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EmptyComparisonScope extends AbstractComparisonScope {
	/** Default constructor. */
	public EmptyComparisonScope() {
		super(new ResourceSetImpl(), new ResourceSetImpl(), new ResourceSetImpl());
	}

	/** {@inheritDoc} */
	public Iterator<? extends Resource> getCoveredResources(ResourceSet resourceSet) {
		return Iterators.emptyIterator();
	}

	/** {@inheritDoc} */
	public Iterator<? extends EObject> getCoveredEObjects(Resource resource) {
		return Iterators.emptyIterator();
	}

	/** {@inheritDoc} */
	public Iterator<? extends EObject> getChildren(EObject eObject) {
		return Iterators.emptyIterator();
	}
}
