/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.adapterfactory;

import java.util.Set;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * A descriptor that can create an adapter factory. They are used as the values in a
 * {@link RankedAdapterFactoryDescriptor.Registry registry}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public interface RankedAdapterFactoryDescriptor extends ComposedAdapterFactory.Descriptor {

	/**
	 * The ranking of the adapter factory binded to this descriptor.
	 * 
	 * @return the ranking of the adapter factory binded to this descriptor.
	 */
	int getRanking();

	/**
	 * Gets a unique id for this descriptor.
	 * 
	 * @return an unique id.
	 */
	String getId();

	/**
	 * Returns a human readable label for this adapter factory.
	 * 
	 * @return a label.
	 */
	String getLabel();

	/**
	 * Returns a human readable description for this adapter factory.
	 * 
	 * @return a description or null if none.
	 */
	String getDescription();

	/**
	 * Returns <code>true</code> if the adapter factory is optional.
	 * 
	 * @return <code>true</code> if optional or <code>false</code> otherwise.
	 */
	boolean isOptional();

	/**
	 * A registry is an index that takes a collection of keys, typically a pair consisting of an EPackage or
	 * java.lang.Package, and a java.lang.Class, and maps it to a {@link RankedAdapterFactoryDescriptor
	 * descriptor}.
	 */
	interface Registry extends ComposedAdapterFactory.Descriptor.Registry {

		/**
		 * Returns all {@link RankedAdapterFactoryDescriptor} of the registry.
		 * 
		 * @return an immutable {@link Set} of {@link RankedAdapterFactoryDescriptor}
		 */
		Set<RankedAdapterFactoryDescriptor> getDescriptors();

	}
}
