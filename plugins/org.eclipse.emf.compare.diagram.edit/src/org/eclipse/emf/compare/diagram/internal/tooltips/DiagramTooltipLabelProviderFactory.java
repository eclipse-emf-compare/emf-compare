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
package org.eclipse.emf.compare.diagram.internal.tooltips;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.diagram.internal.extensions.util.ExtensionsAdapterFactory;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This factory return for each UML change a class implementing the {@link ITooltipLabelProvider} interface.
 * This allow to compute the tooltips used in the ui for each change.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 4.2
 */
public class DiagramTooltipLabelProviderFactory extends ExtensionsAdapterFactory implements ComposeableAdapterFactory {

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
	public DiagramTooltipLabelProviderFactory() {
		supportedTypes.add(ITooltipLabelProvider.class);
	}

	@Override
	public Adapter createCoordinatesChangeAdapter() {
		return new DefaultDiagramTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createDiagramChangeAdapter() {
		return new DefaultDiagramTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createEdgeChangeAdapter() {
		return new DefaultDiagramTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createNodeChangeAdapter() {
		return new DefaultDiagramTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createShowAdapter() {
		return new DefaultDiagramTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createHideAdapter() {
		return new DefaultDiagramTooltipProvider(parentAdapterFactory);
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
