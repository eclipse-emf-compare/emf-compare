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
package org.eclipse.emf.compare.provider.spec;

import com.google.common.base.Preconditions;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * Specialized {@link CompareItemProviderAdapterFactory} returning ItemProviderAdapter with nice
 * {@link IItemLabelProvider#getText(Object)} and {@link IItemLabelProvider#getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareItemProviderAdapterFactorySpec extends CompareItemProviderAdapterFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createMatchAdapter()
	 */
	@Override
	public Adapter createMatchAdapter() {
		if (matchItemProvider == null) {
			matchItemProvider = new MatchItemProviderSpec(this);
		}

		return matchItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory#createReferenceChangeAdapter()
	 */
	@Override
	public Adapter createReferenceChangeAdapter() {
		if (referenceChangeItemProvider == null) {
			referenceChangeItemProvider = new ReferenceChangeItemProviderSpec(this);
		}

		return referenceChangeItemProvider;
	}

	/**
	 * Returns the {@link #getRootAdapterFactory() root adapter factory} of the given
	 * <code>adapterAdapter</code> if it is a {@link ComposeableAdapterFactory composeable} one.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 * @return either the {@link #getRootAdapterFactory() root adapter factory} of the given
	 *         <code>adapterAdapter</code> or <code>adapterAdapter</code>.
	 */
	static AdapterFactory getRootAdapterFactoryIfComposeable(AdapterFactory adapterFactory) {
		AdapterFactory af = adapterFactory;
		// If the adapter factory is composeable, we'll adapt using the root.
		if (adapterFactory instanceof ComposeableAdapterFactory) {
			af = ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory();
		}
		return af;
	}

	/**
	 * Returns the text of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getText(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * <p>
	 * The {@link AdapterFactory} used to adapt <code>object</code> is either the given {@link AdapterFactory}
	 * or its root if it is an {@link ComposeableAdapterFactory}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a text
	 * @return the text, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	static String getText(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			AdapterFactory af = getRootAdapterFactoryIfComposeable(adapterFactory);
			Object adapter = af.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getText(object);
			}
		}
		return null;
	}

	/**
	 * Returns the image of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getImage(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * <p>
	 * The {@link AdapterFactory} used to adapt <code>object</code> is either the given {@link AdapterFactory}
	 * or its root if it is an {@link ComposeableAdapterFactory}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a image
	 * @return the image, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	static Object getImage(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			AdapterFactory af = getRootAdapterFactoryIfComposeable(adapterFactory);
			Object adapter = af.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getImage(object);
			}
		}
		return null;
	}
}
