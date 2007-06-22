/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diff.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.api.DiffExtension;

/**
 * The engine descriptor represents an diff extension contribution trough the extension point
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 */
public class DiffExtensionDescriptor implements Comparable {
	protected String priority;

	protected String diffextensionClassName;

	protected IConfigurationElement element;

	/**
	 * Constructor
	 * 
	 * @param element
	 */
	public DiffExtensionDescriptor(IConfigurationElement element) {
		this.element = element;
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		diffextensionClassName = getAttribute("extensionClass", null); //$NON-NLS-1$
	}

	private String getAttribute(String name, String defaultValue) {
		String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Return the diff extension priority
	 * 
	 * @return the diff extension priority
	 */
	public String getPriority() {
		return priority.toLowerCase();
	}

	/**
	 * Return the diff extension class name
	 * 
	 * @return the diff extension class name
	 */
	public String getdDiffExtensionClassName() {
		return diffextensionClassName;
	}

	private DiffExtension diffExtension = null;

	/**
	 * @return the diff extension instance
	 */
	public DiffExtension getDiffExtensionInstance() {
		if (diffExtension == null)
			;
		{
			try {
				diffExtension = (DiffExtension)element.createExecutableExtension("diffExtensionClass"); //$NON-NLS-1$
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}
		return diffExtension;
	}

	private int getPriorityValue(String priority) {
		if (priority.equals("lowest")) //$NON-NLS-1$
			return 1;
		if (priority.equals("low")) //$NON-NLS-1$
			return 2;
		if (priority.equals("normal")) //$NON-NLS-1$
			return 3;
		if (priority.equals("high")) //$NON-NLS-1$
			return 4;
		if (priority.equals("highest")) //$NON-NLS-1$
			return 5;
		return 0;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((diffextensionClassName == null) ? 0 : diffextensionClassName.hashCode());
		result = PRIME * result + ((priority == null) ? 0 : priority.hashCode());
		return result;
	}

	public int compareTo(Object other) {
		if (other instanceof DiffExtensionDescriptor) {
			int nombre1 = getPriorityValue(((DiffExtensionDescriptor)other).getPriority());
			int nombre2 = getPriorityValue(this.getPriority());
			if (nombre1 > nombre2)
				return -1;
			else if (nombre1 == nombre2)
				return 0;
			else
				return 1;
		}
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DiffExtensionDescriptor other = (DiffExtensionDescriptor)obj;
		if (diffextensionClassName == null) {
			if (other.diffextensionClassName != null)
				return false;
		} else if (!diffextensionClassName.equals(other.diffextensionClassName))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		return true;
	}
}
