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
package org.eclipse.emf.compare.diff.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.api.DiffExtension;

/**
 * The engine descriptor represents a diff extension contribution trough the extension point.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DiffExtensionDescriptor implements Comparable {
	protected String priority;

	protected String diffextensionClassName;

	protected IConfigurationElement element;

	private DiffExtension diffExtension;

	/**
	 * Instantiate the descriptor given its configuration.
	 * 
	 * @param configuration
	 *            {@link IConfigurationElement configuration element} of this descriptor.
	 */
	public DiffExtensionDescriptor(IConfigurationElement configuration) {
		element = configuration;
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		diffextensionClassName = getAttribute("extensionClass", null); //$NON-NLS-1$
	}

	private String getAttribute(String name, String defaultValue) {
		final String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Returns this {@link DiffExtension} priority.
	 * 
	 * @return The diff extension priority.
	 */
	public String getPriority() {
		return priority.toLowerCase();
	}

	/**
	 * Returns this {@link DiffExtension} class name.
	 * 
	 * @return The diff extension class name.
	 */
	public String getdDiffExtensionClassName() {
		return diffextensionClassName;
	}

	/**
	 * Returns this {@link DiffExtension} instance.
	 * 
	 * @return the diff extension instance.
	 */
	public DiffExtension getDiffExtensionInstance() {
		if (diffExtension == null) {
			try {
				diffExtension = (DiffExtension)element.createExecutableExtension("diffExtensionClass"); //$NON-NLS-1$
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}
		return diffExtension;
	}

	private int getPriorityValue(String value) {
		if (value == null)
			throw new IllegalArgumentException("Priority cannot be null."); //$NON-NLS-1$
		int priorityValue = DiffExtension.PRIORITY_NORMAL;
		if (value.equals("lowest")) { //$NON-NLS-1$
			priorityValue = DiffExtension.PRIORITY_LOWEST;
		} else if (value.equals("low")) { //$NON-NLS-1$
			priorityValue = DiffExtension.PRIORITY_LOW;
		} else if (value.equals("high")) { //$NON-NLS-1$
			priorityValue = DiffExtension.PRIORITY_HIGH;
		} else if (value.equals("highest")) { //$NON-NLS-1$
			priorityValue = DiffExtension.PRIORITY_HIGHEST;
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
		if (diffextensionClassName != null)
			classNameHash = diffextensionClassName.hashCode();
		int priorityHash = 0;
		if (priority != null)
			priorityHash = priority.hashCode();
		
		int result = 1;
		result = ((prime + classNameHash) + priorityHash) * prime;
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		if (other instanceof DiffExtensionDescriptor) {
			final int nombre1 = getPriorityValue(((DiffExtensionDescriptor)other).getPriority());
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
			final DiffExtensionDescriptor other = (DiffExtensionDescriptor)obj;
			if (diffextensionClassName == null && other.diffextensionClassName != null) {
				isEqual = false;
			} else if (!diffextensionClassName.equals(other.diffextensionClassName)) {
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
