/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.menus;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;

/**
 * The contextual menu descriptor represents a contextual menu contribution through the extension point.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class ContextualMenuDescriptor {

	/** Name of the "id" property of the contextual menu extension point. */
	public static final String ID_PROPERTY = "id"; //$NON-NLS-1$

	/** Name of the "class" property of the contextual menu extension point. */
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$

	/** Name of the "class" property of the structural or content viewer target extension point. */
	public static final String TARGET_CLASS_PROPERTY = "class"; //$NON-NLS-1$

	/** The underlying configuration element. */
	private final IConfigurationElement element;

	/** Value of the "id" attribute of this extension. */
	private final String id;

	/** Value of the "class" attribute of this extension. */
	private final String classname;

	/**
	 * Value of the "class" attribute of this extension for the structural or content viewer target extension.
	 */
	private String targetClassName;

	/** Value of the "class" of this extension for the structural or content viewer target extension. */
	private Class<?> targetClass;

	/** Instance of the contextual menu that this wrapped by this descriptor. */
	private IContextualMenu extension;

	/**
	 * Instantiates a descriptor given its configuration element.
	 * 
	 * @param anElement
	 *            The configuration element we are to create a descriptor for.
	 */
	public ContextualMenuDescriptor(IConfigurationElement anElement) {
		element = anElement;
		id = anElement.getAttribute(ID_PROPERTY);
		classname = anElement.getAttribute(CLASS_PROPERTY);
		final IConfigurationElement[] children = anElement.getChildren();
		if (children.length > 0) {
			final IConfigurationElement child = children[0];
			targetClassName = child.getAttribute(TARGET_CLASS_PROPERTY);
		}

		checks(this);
	}

	/**
	 * Returns the ID of the menu.
	 * 
	 * @return the ID of the menu
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the class name of the implemented menu.
	 * 
	 * @return the class name of the implemented menu.
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * Returns the class name on which the menus have to be created.
	 * 
	 * @return the class name on which the menus have to be created.
	 */
	public String getTargetClassname() {
		return targetClassName;
	}

	/**
	 * Returns the class on which the menus have to be created.
	 * 
	 * @return The extension wrapped by this descriptor.
	 */
	public Class<?> getTargetClass() {
		if (targetClassName != null && targetClass == null) {
			try {
				targetClass = org.eclipse.core.runtime.Platform.getBundle(element.getContributor().getName())
						.loadClass(targetClassName);
			} catch (ClassNotFoundException e) {
				EMFCompareUIPlugin.getDefault().getLog()
						.log(new Status(IStatus.ERROR, EMFCompareUIPlugin.PLUGIN_ID, e.getMessage(), e));
			}
		}
		return targetClass;
	}

	/**
	 * Validates the given descriptor.
	 * 
	 * @param descriptor
	 *            Descriptor of which we need to check the validity.
	 */
	private static void checks(ContextualMenuDescriptor descriptor) {
		if (descriptor.element == null) {
			throw new IllegalArgumentException(
					EMFCompareUIMessages
							.getString("ContextualMenuDescriptor.nullConfigurationElementException")); //$NON-NLS-1$
		}

		final String missingPropertyKey = "ContextualMenuDescriptor.missingMandatoryPropertyException"; //$NON-NLS-1$

		if (nullOrEmpty(descriptor.getID())) {
			throw new IllegalArgumentException(
					EMFCompareUIMessages.getString(missingPropertyKey, ID_PROPERTY));
		}

		if (nullOrEmpty(descriptor.getClassname())) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString(missingPropertyKey,
					CLASS_PROPERTY));
		}

		if (nullOrEmpty(descriptor.getTargetClassname())) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString(missingPropertyKey,
					TARGET_CLASS_PROPERTY));
		}
	}

	/**
	 * Instantiates the extension wrapped by this descriptor.
	 * 
	 * @return The extension wrapped by this descriptor.
	 */
	public IContextualMenu getExtension() {
		if (extension == null) {
			try {
				extension = (IContextualMenu)element.createExecutableExtension(CLASS_PROPERTY);
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
	 * @param string
	 *            String to consider.
	 * @return <code>true</code> if <em>string</em> is either <code>null</code> or empty, <code>false</code>
	 *         otherwise.
	 */
	private static boolean nullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}

}
