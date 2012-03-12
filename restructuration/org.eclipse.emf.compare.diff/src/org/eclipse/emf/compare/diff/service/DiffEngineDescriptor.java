/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.engine.IDiffEngine;
import org.eclipse.emf.compare.util.EngineConstants;

/**
 * The engine descriptor represents an engine contribution trough the extension point.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DiffEngineDescriptor implements Comparable<DiffEngineDescriptor> {
	/** Wildcard character. */
	private static final String WILDCARD = "*"; //$NON-NLS-1$

	/**
	 * Content type this engine takes into account.
	 * 
	 * @since 1.1
	 */
	protected final String contentType;

	/** Configuration element of this descriptor. */
	protected final IConfigurationElement element;

	/** Class name of this engine. */
	protected final String engineClassName;

	/** File extensions this engine takes into account. */
	protected final String fileExtension;

	/** Icon of this engine. */
	protected final String icon;

	/** Label of this engine. */
	protected final String label;

	/**
	 * Namespace this engine takes into account.
	 * 
	 * @since 1.1
	 */
	protected final String namespace;

	/**
	 * Namespace pattern this engine takes into account.
	 * 
	 * @since 1.1
	 */
	protected final String namespacePattern;

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
	protected final String priority;

	/** {@link IDiffEngine} this descriptor describes. */
	private IDiffEngine engine;

	/** Integer representation of the priority. */
	private int priorityValue = -1;

	/**
	 * Instantiate the descriptor given its configuration.
	 * 
	 * @param configuration
	 *            {@link IConfigurationElement configuration element} of this descriptor.
	 */
	public DiffEngineDescriptor(IConfigurationElement configuration) {
		element = configuration;
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		engineClassName = getAttribute("engineClass", null); //$NON-NLS-1$
		label = getAttribute("label", ""); //$NON-NLS-1$ //$NON-NLS-2$
		icon = getAttribute("icon", ""); //$NON-NLS-1$ //$NON-NLS-2$

		contentType = getAttribute("contentType", ""); //$NON-NLS-1$ //$NON-NLS-2$
		namespace = getAttribute("namespace", ""); //$NON-NLS-1$ //$NON-NLS-2$
		namespacePattern = getAttribute("namespacePattern", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if ("".equals(contentType) && "".equals(namespace) && "".equals(namespacePattern)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fileExtension = getAttribute("fileExtension", WILDCARD); //$NON-NLS-1$
		} else {
			fileExtension = getAttribute("fileExtension", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DiffEngineDescriptor other) {
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
			final DiffEngineDescriptor other = (DiffEngineDescriptor)obj;
			if (engineClassName == null && other.engineClassName != null) {
				isEqual = false;
			} else if (engineClassName != null && !engineClassName.equals(other.engineClassName)) {
				isEqual = false;
			} else if (fileExtension == null && other.fileExtension != null) {
				isEqual = false;
			} else if (fileExtension != null && !fileExtension.equals(other.fileExtension)) {
				isEqual = false;
			} else if (contentType == null && other.contentType != null) {
				isEqual = false;
			} else if (contentType != null && !contentType.equals(other.contentType)) {
				isEqual = false;
			} else if (namespace == null && other.namespace != null) {
				isEqual = false;
			} else if (namespace != null && !namespace.equals(other.namespace)) {
				isEqual = false;
			} else if (priority == null && other.priority != null) {
				isEqual = false;
			} else if (priority != null && !priority.equals(other.priority)) {
				isEqual = false;
			}
		}
		return isEqual;
	}

	/**
	 * Returns the content type this engine should handle.
	 * 
	 * @return The content type this engine should handle.
	 * @since 1.1
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns the configuration element.
	 * 
	 * @return The configuration element.
	 * @since 1.0
	 */
	public IConfigurationElement getElement() {
		return element;
	}

	/**
	 * Returns the qualified name of the engine's class.
	 * 
	 * @return Qualified name of the engine's class.
	 * @since 1.0
	 */
	public String getEngineClassName() {
		return engineClassName;
	}

	/**
	 * Returns the engine instance.
	 * 
	 * @return The engine instance.
	 */
	public IDiffEngine getEngineInstance() {
		if (engine == null) {
			try {
				engine = (IDiffEngine)element.createExecutableExtension("engineClass"); //$NON-NLS-1$
			} catch (final CoreException e) {
				EMFComparePlugin.log(e, false);
			}
		}
		// engine could have thrown an exception during its initialization
		if (engine != null) {
			engine.reset();
		}
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
	 * @since 1.0
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Returns the label that represents the wrapped engine.
	 * 
	 * @return The label that represents the wrapped engine.
	 * @since 1.0
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the namespace this engine should handle.
	 * 
	 * @return The namespace this engine should handle.
	 * @since 1.1
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Returns the namespace pattern this engine should handle.
	 * 
	 * @return The namespace pattern this engine should handle.
	 * @since 1.1
	 */
	public String getNamespacePattern() {
		return namespacePattern;
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
	 * Returns the value of the priority of this engine.<br/>
	 * Returned values according to <code>priority</code> :
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
		int namespaceHash = 0;
		if (namespace != null) {
			namespaceHash = namespace.hashCode();
		}
		int contentTypeHash = 0;
		if (contentType != null) {
			contentTypeHash = contentType.hashCode();
		}
		int extensionHash = 0;
		if (fileExtension != null) {
			extensionHash = fileExtension.hashCode();
		}
		int priorityHash = 0;
		if (priority != null) {
			priorityHash = priority.hashCode();
		}

		int hashCode = (prime + classNameHash) * prime;
		hashCode = hashCode << 2;
		hashCode = (hashCode + extensionHash + namespaceHash + contentTypeHash) * prime;
		hashCode = hashCode << 1;
		hashCode = hashCode + priorityHash;
		return hashCode;
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
		throw new IllegalArgumentException(EMFCompareDiffMessages.getString(
				"Descriptor.MissingAttribute", name)); //$NON-NLS-1$
	}
}
