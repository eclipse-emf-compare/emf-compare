/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

/**
 * Useful methods for EMF adapter factories handling.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class AdapterUtils {
	/** Adapter factory instance. This contains all factories registered in the global registry. */
	private static final AdapterFactory FACTORY = createAdapterFactory();

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private AdapterUtils() {
		// prevents instantiation.
	}

	/**
	 * Returns a factory built with all the {@link AdapterFactory} instances available in the global registry.
	 * 
	 * @return A factory built with all the {@link AdapterFactory} instances available in the global registry.
	 */
	public static AdapterFactory getAdapterFactory() {
		return FACTORY;
	}

	/**
	 * Returns an adapter factory containing all the global EMF registry's factories.
	 * 
	 * @return An adapter factory made of all the adapter factories declared in the registry.
	 */
	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		// These two factories could prove useful to avoid potential errors
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}
}
