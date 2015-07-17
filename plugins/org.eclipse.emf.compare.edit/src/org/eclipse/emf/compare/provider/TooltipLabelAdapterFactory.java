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
package org.eclipse.emf.compare.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.tooltip.AttributeChangeTooltipProvider;
import org.eclipse.emf.compare.tooltip.FeatureMapChangeTooltipProvider;
import org.eclipse.emf.compare.tooltip.ReferenceChangeTooltipProvider;
import org.eclipse.emf.compare.tooltip.ResourceAttachmentChangeTooltipProvider;
import org.eclipse.emf.compare.tooltip.ResourceLocationChangeTooltipProvider;
import org.eclipse.emf.compare.util.CompareAdapterFactory;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * SuperClass of factories that need to access tooltips labels.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 4.2
 */
public class TooltipLabelAdapterFactory extends CompareAdapterFactory implements ComposeableAdapterFactory {

	/**
	 * The composed adapter factory.
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * The constructor.
	 */
	public TooltipLabelAdapterFactory() {
		super();
		supportedTypes.add(ITooltipLabelProvider.class);
	}

	@Override
	public Adapter createAttributeChangeAdapter() {
		return new AttributeChangeTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createFeatureMapChangeAdapter() {
		return new FeatureMapChangeTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createReferenceChangeAdapter() {
		return new ReferenceChangeTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createResourceAttachmentChangeAdapter() {
		return new ResourceAttachmentChangeTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createResourceLocationChangeAdapter() {
		return new ResourceLocationChangeTooltipProvider(parentAdapterFactory);
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * 
	 * @return the composed adapter factory
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		if (parentAdapterFactory == null) {
			return this;
		} else {
			return parentAdapterFactory.getRootAdapterFactory();
		}
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * 
	 * @param parentAdapterFactory
	 *            The composed adapter factory
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

}
