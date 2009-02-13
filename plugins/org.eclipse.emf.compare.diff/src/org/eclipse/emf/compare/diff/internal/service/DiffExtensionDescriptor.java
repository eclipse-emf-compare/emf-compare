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
package org.eclipse.emf.compare.diff.internal.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;

/**
 * The engine descriptor represents a diff extension contribution trough the extension point.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DiffExtensionDescriptor {
	/** Class name of this {@link DiffExtension}. */
	protected final String diffextensionClassName;

	/** Configuration element of this descriptor. */
	protected final IConfigurationElement element;

	/** File extension on which applying this diff extension. */
	protected final String fileExtension;

	/** {@link DiffExtension} this descriptor describes. */
	private AbstractDiffExtension diffExtension;

	/**
	 * Instantiate the descriptor given its configuration.
	 * 
	 * @param configuration
	 *            {@link IConfigurationElement configuration element} of this descriptor.
	 */
	public DiffExtensionDescriptor(IConfigurationElement configuration) {
		element = configuration;
		fileExtension = getAttribute("fileExtension", "*"); //$NON-NLS-1$//$NON-NLS-2$
		diffextensionClassName = getAttribute("extensionClass", null); //$NON-NLS-1$
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
	public AbstractDiffExtension getDiffExtensionInstance() {
		if (diffExtension == null) {
			try {
				diffExtension = (AbstractDiffExtension)element.createExecutableExtension("extensionClass"); //$NON-NLS-1$
			} catch (CoreException e) {
				EMFComparePlugin.log(e, false);
			}
		}
		return diffExtension;
	}

	/**
	 * Returns the file extension associated with this contribution.
	 * 
	 * @return The file extension associated with this contribution.
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Returns the value of the attribute <code>name</code> of this descriptor's configuration element. if
	 * the attribute hasn't been set, we'll return <code>defaultValue</code> instead.
	 * 
	 * @param name
	 *            Name of the attribute we seek the value of.
	 * @param defaultValue
	 *            Value to return if the attribute hasn't been set.
	 * @return The value of the attribute <code>name</code>, <code>defaultValue</code> if it hasn't been
	 *         set.
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
