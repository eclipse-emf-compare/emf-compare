/**
 *  Copyright (c) 2011 Atos.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * object which represent and {@link AdapterFactory} registered.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 */
public class RegisteredItemProviderAdapterFactoryDescriptor {
	/**
	 * Default priority.
	 */
	private static final String DEFAULT_PRIORITY = "5"; //$NON-NLS-1$

	/**
	 * Priority Attribute name.
	 */
	private static final String PRIORITY_ATTRIBUTE = "priority"; //$NON-NLS-1$

	/**
	 * Class Attribute name.
	 */
	private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	/**
	 * Attribute name.
	 */
	private static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

	/**
	 * Registered AdapterFactory.
	 */
	private AdapterFactory adapterFactory;

	/**
	 * Priority with which the AdapterFactoryWillbeRefister.
	 */
	private int priority;

	/**
	 * URI Not use for now.
	 */
	private String uri;

	/**
	 * Name of the adapterFactory.
	 */
	private String name;

	/**
	 * ID of this registered adapter factory.
	 */
	private String id;

	/** The underlying configuration element. */
	private final IConfigurationElement element;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            {@link IConfigurationElement} from extension point
	 */
	public RegisteredItemProviderAdapterFactoryDescriptor(IConfigurationElement e) {
		element = e;
		adapterFactory = getAdapterFactoryFromExtPoint();
		priority = getPriorityfromExtPoint();
		name = getNameFromExtPoint();
		id = getIdFromExtPoint();
	}

	/**
	 * Get the id from the extension point.
	 * 
	 * @return {@link String} id
	 */
	private String getIdFromExtPoint() {
		final String value = adapterFactory.getClass().getCanonicalName();
		String result = ""; //$NON-NLS-1$
		if (value != null) {
			result = value;
		} else {
			throw new RuntimeException("Enable to find a correct id for AdapterFactory registered on " //$NON-NLS-1$
					+ RegisteredItemProviderAdapterFactoryRegistery.EXT_POINT_ID_ADAPTER_FACTORY);
		}
		return result;
	}

	/**
	 * Returns the value of the attribute <code>name</code> of this descriptor's configuration element. if the
	 * attribute hasn't been set, we'll return <code>defaultValue</code> instead.
	 * 
	 * @param nameEntry
	 *            Name of the attribute we seek the value of.
	 * @param defaultValue
	 *            Value to return if the attribute hasn't been set.
	 * @param elementEntry
	 *            IConfigurationElement from the extension point
	 * @return The value of the attribute <code>name</code>, <code>defaultValue</code> if it hasn't been set.
	 */
	private String getAttribute(String nameEntry, String defaultValue, IConfigurationElement elementEntry) {
		final String value = elementEntry.getAttribute(nameEntry);
		String result = ""; //$NON-NLS-1$
		if (value != null) {
			result = value;
		} else {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * get the name from the extension point.
	 * 
	 * @return {@link String}
	 */
	private String getNameFromExtPoint() {
		final String value = element.getAttribute(NAME_ATTRIBUTE);
		String result = ""; //$NON-NLS-1$
		if (value != null) {
			result = value;
		} else {
			throw new RuntimeException("Enable to find a correct name for AdapterFactory registered on " //$NON-NLS-1$
					+ RegisteredItemProviderAdapterFactoryRegistery.EXT_POINT_ID_ADAPTER_FACTORY);
		}
		return result;
	}

	/**
	 * Create the adapter factory from the extension point.
	 * 
	 * @return {@link AdapterFactory} from the extension point
	 */
	private AdapterFactory getAdapterFactoryFromExtPoint() {
		Object result;
		try {
			result = element.createExecutableExtension(CLASS_ATTRIBUTE);
		} catch (CoreException e) {
			result = null;
			throw new RuntimeException(
					"Enable to construct an AdapterFactory from the extension registered on " //$NON-NLS-1$
							+ RegisteredItemProviderAdapterFactoryRegistery.EXT_POINT_ID_ADAPTER_FACTORY);
		} catch (InvalidRegistryObjectException e) {
			result = null;
			throw new RuntimeException(
					"Enable to construct an AdapterFactory from the extension registered on " //$NON-NLS-1$
							+ RegisteredItemProviderAdapterFactoryRegistery.EXT_POINT_ID_ADAPTER_FACTORY);
		}
		if (!(result instanceof AdapterFactory)) {
			throw new RuntimeException(
					"The registered class on " + RegisteredItemProviderAdapterFactoryRegistery.EXT_POINT_ID_ADAPTER_FACTORY //$NON-NLS-1$
							+ " do not implement AdapterFactory Interface"); //$NON-NLS-1$
		}
		return (AdapterFactory)result;
	}

	/**
	 * Evaluate the priority of an adapter factory passed thought the extension point.
	 * 
	 * @return Integer which represent the priority
	 */
	private Integer getPriorityfromExtPoint() {
		final String priorityS = getAttribute(PRIORITY_ATTRIBUTE, DEFAULT_PRIORITY, element);
		if (priorityS != null && !"".equals(priorityS)) { //$NON-NLS-1$
			Integer result;
			try {
				result = Integer.parseInt(priorityS);
			} catch (NumberFormatException e) {
				result = new Integer(Integer.parseInt(DEFAULT_PRIORITY));
			}
			return result;
		}
		return new Integer(5);

	}

	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public int getPriority() {
		return priority;
	}

	public String getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

}
