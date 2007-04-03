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
package org.eclipse.emf.compare.match.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.api.MatchEngine;

/**
 * Contribution representation one may give throught the "match engine"
 * extension point.
 * 
 * @author Cédric Brun <cedric.brun@obeo.fr>
 * 
 */
public class EngineDescriptor implements Comparable {
	protected String priority;

	protected String fileExtension;

	protected String engineClassName;

	protected IConfigurationElement element;

	/**
	 * Initializer from IConfigurationElement
	 * 
	 * @param element
	 */
	public EngineDescriptor(IConfigurationElement element) {
		this.element = element;
		fileExtension = getAttribute("fileExtension", "*");
		priority = getAttribute("priority", "low");
		engineClassName = getAttribute("engineClass", null);
	}

	/**
	 * Return a descriptor attribute
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	private String getAttribute(String name, String defaultValue) {
		String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute");
	}

	/**
	 * Return the file extension the engine should handle
	 * 
	 * @return
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Return the engine priority
	 * 
	 * @return
	 */
	public String getPriority() {
		return priority.toLowerCase();
	}

	/**
	 * Return the engine class name
	 * 
	 * @return
	 */
	public String getEngineClassName() {
		return engineClassName;
	}

	private MatchEngine engine = null;

	public MatchEngine getEngineInstance() {
		if (engine == null)
			;
		{
			try {
				engine = (MatchEngine) element
						.createExecutableExtension("engineClass");
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e,false);
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
				+ ((fileExtension == null) ? 0 : fileExtension.hashCode());
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
		if (fileExtension == null) {
			if (other.fileExtension != null)
				return false;
		} else if (!fileExtension.equals(other.fileExtension))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		return true;
	}
}
