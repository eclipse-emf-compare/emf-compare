/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * Provides an adapterFactory adapted to an EMF Project.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class EMFAdapterFactoryProvider {
	/** {@link AdapterFactory} that will be fed by this provider. */
	private static ComposedAdapterFactory adapterFactory;

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private EMFAdapterFactoryProvider() {
		// prevents instantiation
	}

	/**
	 * Creates a list containing the project's needed {@link AdapterFactory adapterFactories}.
	 * 
	 * @return The default EMF adapterFactories list.
	 */
	public static List<AdapterFactory> createFactoryList() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());

		// Add other adapter factories as needed.
		return factories;
	}

	/**
	 * Returns the {@link ComposedAdapterFactory} containing all the adapterFactories defined in
	 * {@link #createFactoryList()}.
	 * 
	 * @return The adapter factory.
	 */
	public static ComposedAdapterFactory getAdapterFactory() {
		if (adapterFactory == null) {
			adapterFactory = new ComposedAdapterFactory(createFactoryList());
		}
		return adapterFactory;
	}

	/**
	 * Adds to the factories list an {@link AdapterFactory} adapted to the given {@link EObject}.
	 * 
	 * @param eObject
	 *            {@link EObject} we need to compute the {@link AdapterFactory} for.
	 * @return <code>True</code> if we found the {@link AdapterFactory} for the given {@link EObject},
	 *         <code>False</code> otherwise.
	 */
	public static boolean addAdapterFactoryFor(EObject eObject) {
		boolean success = false;
		if (AdapterUtils.findAdapterFactory(eObject) != null) {
			getAdapterFactory().addAdapterFactory(AdapterUtils.findAdapterFactory(eObject));
			success = true;
		}
		return success;
	}
}
