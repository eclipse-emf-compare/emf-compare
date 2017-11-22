/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 527567
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.matchs.provider.spec;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderDecorator;

/**
 * This is the base item provider decorator.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class BaseItemProviderDecorator extends ItemProviderDecorator implements IEditingDomainItemProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, Adapter {

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            The adapter factory.
	 */
	public BaseItemProviderDecorator(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This adds an overlay to the given image if the object is controlled.
	 * 
	 * @param object
	 *            the object being adapted.
	 * @param image
	 *            the base image.
	 * @return the overlayed image.
	 */
	protected Object overlayImage(Object object, Object image) {
		if (AdapterFactoryEditingDomain.isControlled(object)) {
			List<Object> images = new ArrayList<Object>(2);
			images.add(image);
			images.add(EMFEditPlugin.INSTANCE.getImage("full/ovr16/ControlledObject")); //$NON-NLS-1$
			return new ComposedImage(images);
		}
		return image;
	}

	/**
	 * Returns the root factory if this local adapter factory is composed, otherwise just the local one.
	 * 
	 * @return the root factory if this local adapter factory is composed, otherwise just the local one.
	 */
	protected AdapterFactory getRootAdapterFactory() {
		if (adapterFactory instanceof ComposeableAdapterFactory) {
			return ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory();
		}

		return adapterFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public Notifier getTarget() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTarget(Notifier newTarget) {
	}
}
