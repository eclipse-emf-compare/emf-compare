/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.facet;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.papyrus.uml.tools.providers.SemanticUMLContentProvider;

/**
 * Wraps the Papyrus Facet content provider in an EMF {@link ItemProviderAdapter}.
 * 
 * @author Stefan Dirix
 */
public class PapyrusFacetContentProviderWrapper extends ItemProviderAdapter implements ITreeItemContentProvider {

	/**
	 * The Papyrus Facet Content Provider.
	 */
	private SemanticUMLContentProvider facetContentProvider;

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @param resourceSet
	 *            the {@ResourceSet} for Papyrus Facet.
	 */
	public PapyrusFacetContentProviderWrapper(AdapterFactory adapterFactory, ResourceSet resourceSet) {
		super(adapterFactory);
		facetContentProvider = new SemanticUMLContentProvider(resourceSet);

	}

	@Override
	public Collection<?> getElements(Object object) {
		return Arrays.asList(facetContentProvider.getElements(object));
	}

	@Override
	public Collection<?> getChildren(Object object) {
		return Arrays.asList(facetContentProvider.getChildren(object));
	}

	@Override
	public boolean hasChildren(Object object) {
		return facetContentProvider.hasChildren(object);
	}

	@Override
	public Object getParent(Object object) {
		return facetContentProvider.getParent(object);
	}

}
