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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * The registry which will hold the ItemAdapterProviderfactory contributed through the
 * "org.eclipse.emf.compare.ui.diff.filter" extension point.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 */
public final class RegisteredItemProviderAdapterFactoryRegistery {
	/**
	 * ID of the extension point used to register specific adapter factory.
	 */
	public static final String EXT_POINT_ID_ADAPTER_FACTORY = "org.eclipse.emf.compare.itemprovideradapterFactory"; //$NON-NLS-1$

	/**
	 * Composed adapter factory of all registered.
	 */
	private ComposedAdapterFactoryOrdered composedAdapterFactory;

	/**
	 * Store the IPAFs.
	 */
	private final Map<String, RegisteredItemProviderAdapterFactoryDescriptor> storage = new HashMap<String, RegisteredItemProviderAdapterFactoryDescriptor>();

	/**
	 * Count the number of {@link AdapterFactory} for each priority.
	 */
	private int[] priorityCounter = new int[] {0, 0, 0, 0, 0 };

	/**
	 * Hide constructor Constructor.
	 */
	RegisteredItemProviderAdapterFactoryRegistery() {
		// Hides default constructor.
	}

	/**
	 * Singleton holder.
	 */
	private static class SingletonHolder {
		/**
		 * Instance.
		 */
		protected static RegisteredItemProviderAdapterFactoryRegistery instance;
		static {
			instance = new RegisteredItemProviderAdapterFactoryRegistery();
		}
	}

	/**
	 * get the instance of the registry.
	 * 
	 * @return RegisteredItemProviderAdapterFactoryRegistery
	 */
	public static RegisteredItemProviderAdapterFactoryRegistery getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Adds a descriptor for the given configuration element to the list of contributed extensions.
	 * 
	 * @param element
	 *            The element for which we are to add a descriptor for.
	 * @return RegisteredItemProviderAdapterFactoryDescriptor newly created
	 */
	public synchronized RegisteredItemProviderAdapterFactoryDescriptor addExtension(
			IConfigurationElement element) {
		final RegisteredItemProviderAdapterFactoryDescriptor desc = new RegisteredItemProviderAdapterFactoryDescriptor(
				element);
		storage.put(desc.getId(), desc);

		addElementToPriorityCounter(desc);
		return desc;
	}

	/**
	 * Add new element into the {@link ComposedAdapterFactory} at the correct index.
	 * 
	 * @param desc
	 *            {@link RegisteredItemProviderAdapterFactoryDescriptor} of the new element
	 */
	public synchronized void updateAddedElementInComposedAdapterFactory(
			RegisteredItemProviderAdapterFactoryDescriptor desc) {
		if (composedAdapterFactory != null) {
			int index = 0;
			for (int i = 0; i < desc.getPriority() - 1; i++) {
				index += priorityCounter[i];
			}
			composedAdapterFactory.insertAtAdapterFactory(desc.getAdapterFactory(), index);
		}
	}

	/**
	 * Add element to the PriorityCounter.
	 * 
	 * @param desc
	 *            New {@link RegisteredItemProviderAdapterFactoryDescriptor} to be added
	 */
	private void addElementToPriorityCounter(RegisteredItemProviderAdapterFactoryDescriptor desc) {
		priorityCounter[desc.getPriority() - 1]++;
	}

	/**
	 * Remove element to the PriorityCounter.
	 * 
	 * @param desc
	 *            New {@link RegisteredItemProviderAdapterFactoryDescriptor} to be added
	 */
	private void removeElementToPriorityCounter(RegisteredItemProviderAdapterFactoryDescriptor desc) {
		priorityCounter[desc.getPriority() - 1]--;
	}

	/**
	 * Fill the list with all the IPAF from the extension point (respecting the priority order).
	 * 
	 * @return A list of all specific IPAF sorted by priorityOrder
	 */
	public ComposedAdapterFactory createSortedListOfSpecifcIPAF() {
		if (composedAdapterFactory == null) {
			composedAdapterFactory = new ComposedAdapterFactoryOrdered();
			final TreeMap<Integer, List<AdapterFactory>> specificAdapterFactory = new TreeMap<Integer, List<AdapterFactory>>();
			for (RegisteredItemProviderAdapterFactoryDescriptor registeredIpaf : getDescriptors()) {
				final List<AdapterFactory> list = specificAdapterFactory.get(registeredIpaf.getPriority());
				if (list != null) {
					list.add(registeredIpaf.getAdapterFactory());
				} else {
					specificAdapterFactory.put(registeredIpaf.getPriority(), new ArrayList<AdapterFactory>(
							Collections.singletonList(registeredIpaf.getAdapterFactory())));
				}
			}
			for (Entry<Integer, List<AdapterFactory>> entry : specificAdapterFactory.entrySet()) {
				final List<AdapterFactory> value = entry.getValue();
				// result2.addAll(value);
				for (AdapterFactory adapter : value) {
					composedAdapterFactory.addAdapterFactory(adapter);
				}
			}
		}
		return composedAdapterFactory;
	}

	/**
	 * Returns all registered descriptors.
	 * 
	 * @return all registered descriptors.
	 */
	public synchronized List<RegisteredItemProviderAdapterFactoryDescriptor> getDescriptors() {
		return new ArrayList<RegisteredItemProviderAdapterFactoryDescriptor>(storage.values());
	}

	/**
	 * This will parse the currently running platform for extensions and store all the difference filters that
	 * can be found.
	 */
	public void parseInitialContributions() {
		final IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(EXT_POINT_ID_ADAPTER_FACTORY).getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				addExtension(configElements[j]);
			}
		}
	}

	/**
	 * Clears this registry from any contributed descriptors.
	 */
	public synchronized void clearRegistry() {
		storage.clear();
	}

	/**
	 * Returns the {@link RegisteredItemProviderAdapterFactoryDescriptor} in relation to its id.
	 * 
	 * @param id
	 *            The id.
	 * @return The descriptor.
	 */
	public RegisteredItemProviderAdapterFactoryDescriptor getDescriptor(int id) {
		return storage.get(id);

	}

	/**
	 * Removes a given descriptor from the list of known contributions and remove it from the composed
	 * adapterFacotry.
	 * 
	 * @param element
	 *            The element for which we are to remove a descriptor for.
	 */
	public synchronized void removeExtension(IConfigurationElement element) {
		final RegisteredItemProviderAdapterFactoryDescriptor desc = new RegisteredItemProviderAdapterFactoryDescriptor(
				element);
		storage.remove(desc.getId());
		removeElementToPriorityCounter(desc);
	}

	/**
	 * remove from the {@link AdapterFactory} for the composed adapterFactory.
	 * 
	 * @param element
	 *            {@link IConfigurationElement}
	 */
	public synchronized void removeFromComposedAdapterFactory(IConfigurationElement element) {
		final RegisteredItemProviderAdapterFactoryDescriptor desc = new RegisteredItemProviderAdapterFactoryDescriptor(
				element);
		if (composedAdapterFactory != null) {
			composedAdapterFactory.removeAdapterFactory(desc.getAdapterFactory());
		}
	}
}
