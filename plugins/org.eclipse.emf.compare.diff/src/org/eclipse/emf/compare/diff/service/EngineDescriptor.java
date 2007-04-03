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
import org.eclipse.emf.compare.diff.api.DiffEngine;

public class EngineDescriptor implements Comparable   {
	protected String priority;


	protected String engineClassName;

	protected IConfigurationElement element;

	public EngineDescriptor(IConfigurationElement element) {
		this.element = element;
		priority = getAttribute("priority", "low");
		engineClassName = getAttribute("engineClass", null);
	}

	private String getAttribute(String name, String defaultValue) {
		String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute");
	}


	public String getPriority() {
		return priority.toLowerCase();
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getEngineClassName() {
		return engineClassName;
	}

	public void setEngineClassName(String engineClassName) {
		this.engineClassName = engineClassName;
	}

	private DiffEngine engine = null;

	public DiffEngine getEngineInstance() {
		if (engine == null)
			;
		{
			try {
				engine = (DiffEngine) element
						.createExecutableExtension("engineClass");
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}
		return engine;
	}

	private int getPriorityValue(String priority) {
		if (priority.equals("lowest"))
			return 1;
		if (priority.equals("low"))
			return 2;
		if (priority.equals("normal"))
			return 3;
		if (priority.equals("high"))
			return 4;
		if (priority.equals("highest"))
			return 5;
		return 0;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((engineClassName == null) ? 0 : engineClassName.hashCode());
		result = PRIME * result
				+ ((priority == null) ? 0 : priority.hashCode());
		return result;
	}

	public int compareTo(Object other) {
		if (other instanceof EngineDescriptor) {
			int nombre1 = getPriorityValue(((EngineDescriptor) other)
					.getPriority());
			int nombre2 = getPriorityValue(this.getPriority());
			if (nombre1 > nombre2)
				return -1;
			else if (nombre1 == nombre2)
				return 0;
			else
				return 1;
		} else {
			System.err.println("Silly compare with" + other);
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
		final EngineDescriptor other = (EngineDescriptor) obj;
		if (engineClassName == null) {
			if (other.engineClassName != null)
				return false;
		} else if (!engineClassName.equals(other.engineClassName))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		return true;
	}
}
