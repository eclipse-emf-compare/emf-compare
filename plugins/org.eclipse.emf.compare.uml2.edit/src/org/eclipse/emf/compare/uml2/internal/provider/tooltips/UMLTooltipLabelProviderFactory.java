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
package org.eclipse.emf.compare.uml2.internal.provider.tooltips;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This factory return for each UML change a class implementing the {@link ITooltipLabelProvider} interface.
 * This allow to compute the tooltips used in the ui for each change.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 4.2
 */
public class UMLTooltipLabelProviderFactory extends UMLCompareAdapterFactory implements ComposeableAdapterFactory {

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
	public UMLTooltipLabelProviderFactory() {
		supportedTypes.add(ITooltipLabelProvider.class);
	}

	@Override
	public Adapter createStereotypeApplicationChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createStereotypeAttributeChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createStereotypedElementChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createStereotypeReferenceChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createIntervalConstraintChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createDirectedRelationshipChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createGeneralizationSetChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createOpaqueElementBodyChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createMessageChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createExecutionSpecificationChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createAssociationChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createProfileApplicationChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
	}

	@Override
	public Adapter createExtendChangeAdapter() {
		return new DefaultUMLTooltipProvider(parentAdapterFactory);
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
