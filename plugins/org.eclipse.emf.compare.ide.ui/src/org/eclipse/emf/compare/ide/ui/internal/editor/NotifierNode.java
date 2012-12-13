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
package org.eclipse.emf.compare.ide.ui.internal.editor;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class NotifierNode implements ITypedElement {

	private final Notifier fNotifier;

	private final AdapterFactory fAdapterFactory;

	public NotifierNode(Notifier notifier, AdapterFactory adapterFactory) {
		fNotifier = notifier;
		fAdapterFactory = adapterFactory;
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
			return EMFCompareConstants.NODE_TYPE__EMF_RESOURCESET;
		} else if (fNotifier instanceof Resource) {
			return EMFCompareConstants.NODE_TYPE__EMF_RESOURCE;
		} else if (fNotifier instanceof EObject) {
			return EMFCompareConstants.NODE_TYPE__EMF_EOBJECT;
		} else {
			return ITypedElement.UNKNOWN_TYPE;
		}
	}
}
