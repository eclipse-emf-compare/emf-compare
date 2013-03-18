/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.impl;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TypedNotifier implements ITypedElement {

	public static final String NODE_TYPE__EMF_RESOURCESET = "NODE_TYPE__EMF_RESOURCESET"; //$NON-NLS-1$

	public static final String NODE_TYPE__EMF_RESOURCE = "NODE_TYPE__EMF_RESOURCE"; //$NON-NLS-1$

	public static final String NODE_TYPE__EMF_EOBJECT = "NODE_TYPE__EMF_EOBJECT"; //$NON-NLS-1$

	public static final String NODE_TYPE__EMF_COMPARISON = "NODE_TYPE__EMF_COMPARISON"; //$NON-NLS-1$

	private final Notifier fNotifier;

	private final AdapterFactory fAdapterFactory;

	public TypedNotifier(AdapterFactory adapterFactory, Notifier notifier) {
		fAdapterFactory = adapterFactory;
		fNotifier = notifier;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return AdapterFactoryUtil.getText(fAdapterFactory, fNotifier);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		Object imageObject = AdapterFactoryUtil.getImage(fAdapterFactory, fNotifier);
		return ExtendedImageRegistry.getInstance().getImage(imageObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		if (fNotifier instanceof ResourceSet) {
			return NODE_TYPE__EMF_RESOURCESET;
		} else if (fNotifier instanceof Resource) {
			return NODE_TYPE__EMF_RESOURCE;
		} else if (fNotifier instanceof Comparison) {
			return NODE_TYPE__EMF_COMPARISON;
		} else if (fNotifier instanceof EObject) {
			return NODE_TYPE__EMF_EOBJECT;
		} else {
			return ITypedElement.UNKNOWN_TYPE;
		}
	}
}
