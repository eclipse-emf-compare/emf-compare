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
package org.eclipse.emf.compare.diagram.internal.matchs.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsEditPlugin;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.provider.ViewItemProvider;

/**
 * This is the specific item provider adapter for a {@link org.eclipse.gmf.runtime.notation.View} object.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ViewItemProviderSpec extends ViewItemProvider {

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            The adapter factory.
	 */
	public ViewItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.notation.provider.ViewItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		View obj = (View)object;
		return getEClassText(obj) + " on " + getElementText(obj);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		EObject obj = (View)object;
		return overlayImage(object, ExtensionsEditPlugin.INSTANCE.getImage("full/obj16/"
				+ obj.eClass().getName()));
	}

	/**
	 * It returns the text related to the EClass of the given view.
	 * 
	 * @param obj
	 *            The given view.
	 * @return The text.
	 */
	protected String getEClassText(View obj) {
		return obj.eClass().getName();
	}

	/**
	 * It returns the default text for the given view.
	 * 
	 * @param obj
	 *            The given view.
	 * @return The text.
	 */
	protected String getElementText(View obj) {
		EObject element = obj.getElement();
		if (element != null) {
			return AdapterFactoryUtil.getText(getRootAdapterFactory(), element);
		}
		return "<null>";
	}

}
