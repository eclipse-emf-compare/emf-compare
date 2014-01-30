/*******************************************************************************
 * Copyright (c) 2002, 2014 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *   Obeo - Introduce generics and add documentation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Simple utility class to create proxy of extension that are
 * {@link IConfigurationElement#createExecutableExtension(String) instantiable}.
 * <p>
 * No test of the {@link IConfigurationElement#isValid() validity} of the wrapped
 * {@link IConfigurationElement} is performed. As such you should always extend this class while listening to
 * the {@link org.eclipse.core.runtime.IExtensionRegistry} and react properly the removal of the wrapped
 * {@link IConfigurationElement}.
 * <p>
 * Note: this is based on {@code org.eclipse.emf.ecore.plugin.RegistryReader.PluginClassDescriptor}
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @param <T>
 *            Type of the instances created from this descriptor.
 */
public class PluginClassDescriptor<T> {
	/**
	 * The element from which create an instance.
	 */
	protected IConfigurationElement element;

	/**
	 * The name of the attribute that holds the class full name to be instantiated.
	 */
	protected String attributeName;

	/**
	 * Creates a new descriptor for given element keeping the class name to be instantiated in an attribute
	 * named {@code attributeName}.
	 * 
	 * @param element
	 *            The element from which create an instance.
	 * @param attributeName
	 *            The name of the attribute that holds the class full name to be instantiated.
	 */
	public PluginClassDescriptor(IConfigurationElement element, String attributeName) {
		this.element = element;
		this.attributeName = attributeName;
	}

	/**
	 * Creates a new instance.
	 * 
	 * @return the new instance.
	 * @throws RuntimeException
	 *             wraps a CoreException if an instance of the executable extension could not be created for
	 *             any reason.
	 */
	@SuppressWarnings("unchecked")
	protected T createInstance() {
		try {
			return (T)element.createExecutableExtension(attributeName);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
}
