/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import org.eclipse.emf.compare.match.EMFCompareMatchMessages;
import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.util.EngineConstants;

/**
 * Contribution representation one may give through the "match engine" extension point.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class MatchEngineDescriptor implements Comparable<MatchEngineDescriptor> {
	/** Configuration element of this descriptor. */
	protected final IConfigurationElement element;

	/** Class name of this engine. */
	protected final String engineClassName;

	/** File extensions this engine takes into account. */
	protected final String fileExtension;

	/** Label of this engine. */
	protected final String label;

	/** Icon of this engine. */
	protected final String icon;

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

	/** Integer representation of the priority. */
	private int priorityValue = -1;

	/** {@link IMatchEngine} this descriptor describes. */
	private IMatchEngine engine;

	/**
	 * Instantiate the descriptor given its configuration.
	 * 
	 * @param configuration
	 *            {@link IConfigurationElement configuration element} of this descriptor.
	 */
	public MatchEngineDescriptor(IConfigurationElement configuration) {
		element = configuration;
		fileExtension = getAttribute("fileExtension", "*"); //$NON-NLS-1$ //$NON-NLS-2$
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		engineClassName = getAttribute("engineClass", null); //$NON-NLS-1$
		label = getAttribute("label", ""); //$NON-NLS-1$ //$NON-NLS-2$
		icon = getAttribute("icon", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MatchEngineDescriptor other) {
		final int nombre1 = other.getPriorityValue();
		final int nombre2 = getPriorityValue();
		return nombre2 - nombre1;
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
			final MatchEngineDescriptor other = (MatchEngineDescriptor)obj;
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

	/**
	 * Returns the configuration element.
	 * 
	 * @return The configuration element.
	 * @since 0.9
	 */
	public IConfigurationElement getElement() {
		return element;
	}

	/**
	 * Returns the qualified name of the engine's class.
	 * 
	 * @return Qualified name of the engine's class.
	 * @since 0.9
	 */
	public String getEngineClassName() {
		return engineClassName;
	}

	/**
	 * Returns the engine instance.
	 * 
	 * @return The engine instance.
	 */
	public IMatchEngine getEngineInstance() {
		if (engine == null) {
			try {
				engine = (IMatchEngine)element.createExecutableExtension("engineClass"); //$NON-NLS-1$
			} catch (final CoreException e) {
				EMFComparePlugin.log(e, false);
			}
		}
		engine.reset();
		return engine;
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
	 * Returns the icon that represents the wrapped engine.
	 * 
	 * @return The icon that represents the wrapped engine.
	 * @since 0.9
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Returns the label that represents the wrapped engine.
	 * 
	 * @return The label that represents the wrapped engine.
	 * @since 0.9
	 */
	public String getLabel() {
		return label;
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
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int classNameHash = 0;
		if (engineClassName != null) {
			classNameHash = engineClassName.hashCode();
		}
		int extensionHash = 0;
		if (fileExtension != null) {
			extensionHash = fileExtension.hashCode();
		}
		int priorityHash = 0;
		if (priority != null) {
			priorityHash = priority.hashCode();
		}

		return (((prime + classNameHash) * prime) + extensionHash) * prime + priorityHash;
	}

	/**
	 * Returns the value of the attribute <code>name</code> of this descriptor's configuration element. if the
	 * attribute hasn't been set, we'll return <code>defaultValue</code> instead.
	 * 
	 * @param name
	 *            Name of the attribute we seek the value of.
	 * @param defaultValue
	 *            Value to return if the attribute hasn't been set.
	 * @return The value of the attribute <code>name</code>, <code>defaultValue</code> if it hasn't been set.
	 */
	private String getAttribute(String name, String defaultValue) {
		final String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException(EMFCompareMatchMessages.getString(
				"Descriptor.MissingAttribute", name)); //$NON-NLS-1$
	}

	/**
	 * Returns the value of the priority of this engine.<br/>Returned values according to
	 * <code>priority</code> :
	 * <ul>
	 * <li>&quot;lowest&quot; =&gt; {@value EngineConstants#PRIORITY_LOWEST}</li>
	 * <li>&quot;low&quot; =&gt; {@value EngineConstants#PRIORITY_LOW}</li>
	 * <li>&quot;high&quot; =&gt; {@value EngineConstants#PRIORITY_HIGH}</li>
	 * <li>&quot;highest&quot; =&gt; {@value EngineConstants#PRIORITY_HIGHEST}</li>
	 * <li>anything else =&gt; {@value EngineConstants#PRIORITY_NORMAL}</li>
	 * </ul>
	 * 
	 * @return <code>int</code> corresponding to this engine priority.
	 */
	public int getPriorityValue() {
		if (priorityValue == -1) {
			priorityValue = EngineConstants.PRIORITY_NORMAL;
			if ("lowest".equals(priority)) { //$NON-NLS-1$
				priorityValue = EngineConstants.PRIORITY_LOWEST;
			} else if ("low".equals(priority)) { //$NON-NLS-1$
				priorityValue = EngineConstants.PRIORITY_LOW;
			} else if ("high".equals(priority)) { //$NON-NLS-1$
				priorityValue = EngineConstants.PRIORITY_HIGH;
			} else if ("highest".equals(priority)) { //$NON-NLS-1$
				priorityValue = EngineConstants.PRIORITY_HIGHEST;
			}
		}
		return priorityValue;
	}
}
