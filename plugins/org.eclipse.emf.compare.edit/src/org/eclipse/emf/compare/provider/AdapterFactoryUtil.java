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
package org.eclipse.emf.compare.provider;

import com.google.common.base.Preconditions;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * Some utility methods to work with {@link AdapterFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class AdapterFactoryUtil {

	/**
	 * Prevents instantiation.
	 */
	private AdapterFactoryUtil() {
		// prevents instantiation
	}

	/**
	 * Returns the text of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getText(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a text
	 * @return the text, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	public static String getText(final AdapterFactory adapterFactory, final Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object == null) {
			return null;
		}

		String ret = null;
		Object itemLabelProvider = adapterFactory.adapt(object, IItemLabelProvider.class);
		if (itemLabelProvider instanceof IItemLabelProvider) {
			ret = ((IItemLabelProvider)itemLabelProvider).getText(object);
		}
		return ret;
	}

	/**
	 * Returns the image of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getImage(Object) image}. Returns null if <code>object</code>
	 * is null.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a image
	 * @return the image, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	public static Object getImage(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object == null) {
			return null;
		}

		Object ret = null;
		Object adapter = adapterFactory.adapt(object, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			ret = ((IItemLabelProvider)adapter).getImage(object);
		}
		return ret;
	}

	/**
	 * Returns the StyledText for the given object and the given adapter factory.
	 * 
	 * @param adapterFactory
	 *            the given adapter factory.
	 * @param object
	 *            the given object.
	 * @return the StyledText as an Object.
	 */
	public static Object getStyledText(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			Object adapter = adapterFactory.adapt(object, IItemStyledLabelProvider.class);
			if (adapter instanceof IItemStyledLabelProvider) {
				return ((IItemStyledLabelProvider)adapter).getStyledText(object);
			}
		}
		return null;
	}
}
