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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsEditPlugin;
import org.eclipse.emf.ecore.EObject;

/**
 * This is the specific item provider adapter for a {@link org.eclipse.gmf.runtime.notation.View} object.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class StyleItemProviderSpec extends BaseItemProviderDecorator {

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            The adapter factory.
	 */
	public StyleItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.notation.provider.ViewItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return ((EObject)object).eClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, ExtensionsEditPlugin.INSTANCE.getImage("full/obj16/Style")); //$NON-NLS-1$
	}
}
