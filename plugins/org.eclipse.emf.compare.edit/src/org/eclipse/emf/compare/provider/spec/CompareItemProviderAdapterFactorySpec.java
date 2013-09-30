/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;

/**
 * Specialized {@link CompareItemProviderAdapterFactory} returning ItemProviderAdapter with nice
 * {@link org.eclipse.emf.edit.provider.IItemLabelProvider#getText(Object)} and
 * {@link org.eclipse.emf.edit.provider.IItemLabelProvider#getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareItemProviderAdapterFactorySpec extends CompareItemProviderAdapterFactory {

	/**
	 * Constructor calling super {@link #CompareItemProviderAdapterFactory()}.
	 */
	public CompareItemProviderAdapterFactorySpec() {
		super();
		supportedTypes.add(IItemStyledLabelProvider.class);
		supportedTypes.add(IItemDescriptionProvider.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createMatchAdapter()
	 */
	@Override
	public Adapter createMatchAdapter() {
		return new MatchItemProviderSpec(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createReferenceChangeAdapter()
	 */
	@Override
	public Adapter createReferenceChangeAdapter() {
		return new ReferenceChangeItemProviderSpec(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createAttributeChangeAdapter()
	 */
	@Override
	public Adapter createAttributeChangeAdapter() {
		return new AttributeChangeItemProviderSpec(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createMatchResourceAdapter()
	 */
	@Override
	public Adapter createMatchResourceAdapter() {
		return new MatchResourceItemProviderSpec(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createComparisonAdapter()
	 */
	@Override
	public Adapter createComparisonAdapter() {
		return new ComparisonItemProviderSpec(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createConflictAdapter()
	 */
	@Override
	public Adapter createConflictAdapter() {
		return new ConflictItemProviderSpec(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createResourceAttachmentChangeAdapter()
	 */
	@Override
	public Adapter createResourceAttachmentChangeAdapter() {
		return new ResourceAttachmentChangeItemProviderSpec(this);
	}
}
