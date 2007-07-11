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
package org.eclipse.emf.compare.merge.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.merge.api.MergeFactory;
import org.eclipse.emf.compare.util.EngineConstants;

/**
 * Descriptor class for {@link MergeFactory} contribution.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class FactoryDescriptor implements Comparable {
	protected String priority;

	protected String factoryClassName;

	protected IConfigurationElement element;

	private MergeFactory factory;

	/**
	 * Creates a descriptor given its its configuration.
	 * 
	 * @param configuration
	 *            {@link IConfigurationElement configuration element} of this descriptor.
	 */
	public FactoryDescriptor(IConfigurationElement configuration) {
		element = configuration;
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		factoryClassName = getAttribute("class", null); //$NON-NLS-1$
	}

	private String getAttribute(String name, String defaultValue) {
		final String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Returns the factory priority.
	 * 
	 * @return The factory priority.
	 */
	public String getPriority() {
		return priority.toLowerCase();
	}

	/**
	 * Returns the factory class name.
	 * 
	 * @return The factory class name.
	 */
	public String getFactoryClassName() {
		return factoryClassName;
	}

	/**
	 * Returns the factory instance.
	 * 
	 * @return The factory instance.
	 */
	public MergeFactory getEngineInstance() {
		if (factory == null) {
			try {
				factory = (MergeFactory)element.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (CoreException e) {
				EMFComparePlugin.log(e, false);
			}
		}
		return factory;
	}

	private int getPriorityValue(String value) {
		if (value == null)
			throw new IllegalArgumentException("Priority cannot be null."); //$NON-NLS-1$
		int priorityValue = EngineConstants.PRIORITY_NORMAL;
		if (value.equals("lowest")) { //$NON-NLS-1$
			priorityValue = EngineConstants.PRIORITY_LOWEST;
		} else if (value.equals("low")) { //$NON-NLS-1$
			priorityValue = EngineConstants.PRIORITY_LOW;
		} else if (value.equals("high")) { //$NON-NLS-1$
			priorityValue = EngineConstants.PRIORITY_HIGH;
		} else if (value.equals("highest")) { //$NON-NLS-1$
			priorityValue = EngineConstants.PRIORITY_HIGHEST;
		}
		return priorityValue;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int classNameHash = 0;
		if (factoryClassName != null)
			classNameHash = factoryClassName.hashCode();
		int priorityHash = 0;
		if (priority != null)
			priorityHash = priority.hashCode();
		
		return (prime + classNameHash) * prime + priorityHash;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		if (other instanceof FactoryDescriptor) {
			final int nombre1 = getPriorityValue(((FactoryDescriptor)other).getPriority());
			final int nombre2 = getPriorityValue(getPriority());
			return nombre2 - nombre1;
		}
		return 1;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = true;
		if (this == obj) {
			isEqual = true;
		} else if (obj == null || getClass() != obj.getClass()) {
			isEqual = false;
		} else {
			final FactoryDescriptor other = (FactoryDescriptor)obj;
			if (factoryClassName == null && other.factoryClassName != null) {
				isEqual = false;
			} else if (!factoryClassName.equals(other.factoryClassName)) {
				isEqual = false;
			} else if (priority == null && other.priority != null) {
				isEqual = false;
			} else if (!priority.equals(other.priority)) {
				isEqual = false;
			}
		}
		return isEqual;
	}
}
