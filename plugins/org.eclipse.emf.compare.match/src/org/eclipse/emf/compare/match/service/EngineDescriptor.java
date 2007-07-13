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
package org.eclipse.emf.compare.match.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.Messages;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.util.EngineConstants;

/**
 * Contribution representation one may give throught the "match engine" extension point.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EngineDescriptor implements Comparable {
	/**
	 * Priority of this descriptor. Should be one of
	 * <ul>
	 * <li>{@link EngineConstants#PRIORITY_HIGHEST}</li>
	 * <li>{@link EngineConstants#PRIORITY_HIGH}</li>
	 * <li>{@link EngineConstants#PRIORITY_NORMAL}</li>
	 * <li>{@link EngineConstants#PRIORITY_LOW}</li>
	 * <li>{@link EngineConstants#PRIORITY_LOWEST}</li>
	 * </ul>
	 */
	protected String priority;

	/** File extensions this engine takes into account. */
	protected final String fileExtension;

	/** Class name of this engine. */
	protected final String engineClassName;

	/** Configuration element of this descriptor. */
	protected final IConfigurationElement element;

	/** {@link MatchEngine} this descriptor describes. */
	private MatchEngine engine;

	/**
	 * Instantiate the descriptor given its configuration.
	 * 
	 * @param configuration
	 *            {@link IConfigurationElement configuration element} of this descriptor.
	 */
	public EngineDescriptor(IConfigurationElement configuration) {
		element = configuration;
		fileExtension = getAttribute("fileExtension", "*"); //$NON-NLS-1$ //$NON-NLS-2$
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		engineClassName = getAttribute("engineClass", null); //$NON-NLS-1$
	}

	private String getAttribute(String name, String defaultValue) {
		final String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException(Messages.getString("Descriptor.MissingAttribute", name)); //$NON-NLS-1$
	}

	/**
	 * Returns the file extension this engine should handle.
	 * 
	 * @return The file extension this engine should handle.
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Returns the engine priority.
	 * 
	 * @return The engine priority.
	 */
	public String getPriority() {
		return priority.toLowerCase();
	}

	/**
	 * Returns the engine instance.
	 * 
	 * @return The engine instance.
	 */
	public MatchEngine getEngineInstance() {
		if (engine == null) {
			try {
				engine = (MatchEngine)element.createExecutableExtension("engineClass"); //$NON-NLS-1$
			} catch (CoreException e) {
				EMFComparePlugin.log(e, false);
			}
		}
		return engine;
	}

	private int getPriorityValue(String value) {
		if (value == null)
			throw new IllegalArgumentException(Messages.getString("Descriptor.IllegalPriority")); //$NON-NLS-1$
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
		if (engineClassName != null)
			classNameHash = engineClassName.hashCode();
		int extensionHash = 0;
		if (fileExtension != null)
			extensionHash = fileExtension.hashCode();
		int priorityHash = 0;
		if (priority != null)
			priorityHash = priority.hashCode();

		return (((prime + classNameHash) * prime) + extensionHash) * prime + priorityHash;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		if (other instanceof EngineDescriptor) {
			final int nombre1 = getPriorityValue(((EngineDescriptor)other).getPriority());
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
			final EngineDescriptor other = (EngineDescriptor)obj;
			if (engineClassName == null && other.engineClassName != null) {
				isEqual = false;
			} else if (!engineClassName.equals(other.engineClassName)) {
				isEqual = false;
			} else if (fileExtension == null && other.fileExtension != null) {
				isEqual = false;
			} else if (!fileExtension.equals(other.fileExtension)) {
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
