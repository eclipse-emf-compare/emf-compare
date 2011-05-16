/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.filter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;

/**
 * The difference filter descriptor represents an filter contribution trough the extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.2
 */
public final class DifferenceFilterDescriptor {
	/** Name of the "id" property of the Difference filter extension point. */
	public static final String ID_PROPERTY = "id"; //$NON-NLS-1$

	/** Name of the "name" property of the Difference filter extension point. */
	public static final String NAME_PROPERTY = "name"; //$NON-NLS-1$

	/** Name of the "class" property of the Difference filter extension point. */
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$

	/** The underlying configuration element. */
	private final IConfigurationElement element;

	/** Value of the "id" attribute of this extension. */
	private final String id;

	/** Value of the "name" attribute of this extension. */
	private final String name;

	/** Value of the "class" attribute of this extension. */
	private final String classname;

	/** Instance of the filter this wrapped by this descriptor. */
	private IDifferenceFilter extension;

	/**
	 * Instantiates a descriptor given its configuration element.
	 * 
	 * @param e
	 *            The configuration element we are to create a descriptor for.
	 */
	public DifferenceFilterDescriptor(IConfigurationElement e) {
		element = e;
		id = e.getAttribute(ID_PROPERTY, ""); //$NON-NLS-1$
		name = e.getAttribute(NAME_PROPERTY, ""); //$NON-NLS-1$
		classname = e.getAttribute(CLASS_PROPERTY, null);

		checks(this);
	}

	/**
	 * Returns the ID of the filter.
	 * 
	 * @return the ID of the filter
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the classname of the implemented filter.
	 * 
	 * @return the classname of the implemented filter
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * Returns the name of the implemented filter (translatable).
	 * 
	 * @return the name of the implemented filter (translatable)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Validates the given descriptor.
	 * 
	 * @param d
	 *            Descriptor of which we need to check the validity.
	 */
	private static void checks(DifferenceFilterDescriptor d) {
		if (d.element == null) {
			throw new IllegalArgumentException(
					EMFCompareUIMessages
							.getString("DifferenceFilterDescriptor.nullConfigurationElementException")); //$NON-NLS-1$
		}

		final String missingPropertyKey = "DifferenceFilterDescriptor.missingMandatoryPropertyException"; //$NON-NLS-1$

		if (nullOrEmpty(d.getID())) {
			throw new IllegalArgumentException(
					EMFCompareUIMessages.getString(missingPropertyKey, ID_PROPERTY));
		}

		if (nullOrEmpty(d.getName())) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString(missingPropertyKey,
					NAME_PROPERTY));
		}

		if (nullOrEmpty(d.getClassname())) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString(missingPropertyKey,
					CLASS_PROPERTY));
		}
	}

	/**
	 * Instantiates the extension wrapped by this descriptor.
	 * 
	 * @return The extension wrapped by this descriptor.
	 */
	public IDifferenceFilter getExtension() {
		if (extension == null) {
			try {
				extension = (IDifferenceFilter)element.createExecutableExtension(CLASS_PROPERTY);
			} catch (CoreException e) {
				EMFCompareUIPlugin.getDefault().getLog()
						.log(new Status(IStatus.ERROR, EMFCompareUIPlugin.PLUGIN_ID, e.getMessage(), e));
			}
		}
		return extension;
	}

	/**
	 * Checks whether the given String is either <code>null</code> or empty.
	 * 
	 * @param s
	 *            String to consider.
	 * @return <code>true</code> if <em>s</em> is either <code>null</code> or empty, <code>false</code>
	 *         otherwise.
	 */
	private static boolean nullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}
}
