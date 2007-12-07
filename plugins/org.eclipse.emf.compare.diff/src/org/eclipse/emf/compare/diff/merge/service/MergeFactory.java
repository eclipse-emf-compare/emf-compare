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
package org.eclipse.emf.compare.diff.merge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.generic.merge.DefaultMergerProvider;
import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.merge.api.IMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.util.ClassUtils;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.compare.util.EngineConstants;
import org.eclipse.emf.ecore.EObject;

/**
 * The merge factory allows the creation of a merger from any kind of {@link DiffElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class MergeFactory {
	/** Wild card for file extensions. */
	private static final String ALL_EXTENSIONS = "*"; //$NON-NLS-1$

	/** Remembers the last file extension for which we've populated {@link #MERGER_TYPES}. */
	private static String lastExtension = new String();

	/** Name of the extension point to parse for merger providers. */
	private static final String MERGER_PROVIDER_EXTENSION_POINT = "org.eclipse.emf.compare.diff.mergerprovider"; //$NON-NLS-1$

	/**
	 * This map will be populated with the merger associated to given {@link DiffElement}s. Each diff element
	 * will be mapped to the merger provided by the MergerProvider contributed with the highest priority.
	 */
	private static final Map<Class<? extends DiffElement>, Class<? extends IMerger>> MERGER_TYPES = new EMFCompareMap<Class<? extends DiffElement>, Class<? extends IMerger>>();

	/** Keeps track of all the providers we've parsed. */
	private static final Map<String, ArrayList<MergerProviderDescriptor>> PARSED_PROVIDERS = new EMFCompareMap<String, ArrayList<MergerProviderDescriptor>>();

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_PROVIDER = "mergerprovider"; //$NON-NLS-1$

	static {
		parseExtensionMetadata();
	}

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private MergeFactory() {
		// prevents instantiation
	}

	/**
	 * Handles the creation of the merger for a given {@link DiffElement}.
	 * 
	 * @param element
	 *            {@link DiffElement} for which we need a merger.
	 * @return The merger adapted to <code>element</code>, <code>null</code> if it cannot be
	 *         instantiated.
	 */
	public static IMerger createMerger(DiffElement element) {
		final Class<? extends IMerger> mergerClass = getBestMerger(element);

		// If the merger provides a default constructor, we instantiate it
		IMerger elementMerger = null;
		try {
			elementMerger = mergerClass.newInstance();
			elementMerger.setDiffElement(element);
		} catch (InstantiationException e) {
			EMFComparePlugin.log(e.getMessage(), false);
		} catch (IllegalAccessException e) {
			EMFComparePlugin.log(e.getMessage(), false);
		}

		return elementMerger;
	}

	/**
	 * Returns the merger class that is best suited for the given {@link DiffElement}. Merger classes can be
	 * managed via {@link #addMergerType(Class, Class)} and {@link #removeMergerType(Class)}.
	 * 
	 * @param element
	 *            {@link DiffElement} we want a merger for.
	 * @return The merger class that is best suited for the given {@link DiffElement}.
	 */
	private static Class<? extends IMerger> getBestMerger(DiffElement element) {
		Class<? extends IMerger> mergerClass = DefaultMerger.class;

		EObject leftElement = (EObject)ClassUtils.invokeMethod(element, "getLeftElement"); //$NON-NLS-1$
		if (leftElement == null)
			leftElement = (EObject)ClassUtils.invokeMethod(element, "getLeftParent"); //$NON-NLS-1$
		String resourceFileExtension = leftElement.eResource().getURI().fileExtension();

		if (resourceFileExtension == null)
			resourceFileExtension = ALL_EXTENSIONS;

		final Map<Class<? extends DiffElement>, Class<? extends IMerger>> mergersMap;
		if (EMFPlugin.IS_ECLIPSE_RUNNING)
			mergersMap = getMergerTypes(resourceFileExtension);
		else
			mergersMap = MERGER_TYPES;

		// If we know the merger for this class, we return it
		if (mergersMap.containsKey(element.getClass())) {
			mergerClass = mergersMap.get(element.getClass());
			// Else we seek through the map if our element is an instance of one of the class keys.
		} else {
			for (Class<? extends DiffElement> clazz : mergersMap.keySet()) {
				if (clazz.isInstance(element)) {
					mergerClass = mergersMap.get(clazz);
					break;
				}
			}
		}
		return mergerClass;
	}

	/**
	 * This will iterate through the list of the parsed providers from the lowest to the highest priority and
	 * associate each known {@link DiffElement} to a merger in order of their provider's priority.
	 * 
	 * @param fileExtension
	 *            File extension on which the provider can apply its mergers.
	 * @return The map of the merger associated to given {@link DiffElement}s.
	 */
	private static Map<Class<? extends DiffElement>, Class<? extends IMerger>> getMergerTypes(
			String fileExtension) {
		if (!fileExtension.equals(lastExtension)) {
			lastExtension = fileExtension;
			MERGER_TYPES.clear();
			/*
			 * TODO We'll iterate n times over all the parsed providers (one loop for each priority). See if
			 * we can optimize this.
			 */
			final int[] priorities = {EngineConstants.PRIORITY_LOWEST, EngineConstants.PRIORITY_LOW,
					EngineConstants.PRIORITY_NORMAL, EngineConstants.PRIORITY_HIGH,
					EngineConstants.PRIORITY_HIGHEST, };
			for (int priority : priorities) {
				final Map<Class<? extends DiffElement>, Class<? extends IMerger>> mergers = new EMFCompareMap<Class<? extends DiffElement>, Class<? extends IMerger>>();
				// Iterates through the list of providers registered for all extensions
				if (PARSED_PROVIDERS.containsKey(ALL_EXTENSIONS)) {
					final List<MergerProviderDescriptor> list = PARSED_PROVIDERS.get(ALL_EXTENSIONS);
					Collections.sort(list);
					for (MergerProviderDescriptor descriptor : list) {
						if (descriptor.getPriorityValue(descriptor.priority) == priority) {
							mergers.putAll(descriptor.getMergerProviderInstance().getMergers());
						}
					}
				}
				if (PARSED_PROVIDERS.containsKey(fileExtension)) {
					final List<MergerProviderDescriptor> list = PARSED_PROVIDERS.get(fileExtension);
					Collections.sort(list);
					for (MergerProviderDescriptor descriptor : list) {
						if (descriptor.getPriorityValue(descriptor.priority) == priority) {
							mergers.putAll(descriptor.getMergerProviderInstance().getMergers());
						}
					}
				}
				MERGER_TYPES.putAll(mergers);
			}
		}
		return MERGER_TYPES;
	}

	/**
	 * This will parse the currently running platform for extensions and store all the diff engines and diff
	 * extensions that can be found.
	 */
	private static void parseExtensionMetadata() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
					MERGER_PROVIDER_EXTENSION_POINT).getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					final MergerProviderDescriptor desc = parseProvider(configElements[j]);
					storeProviderDescriptor(desc);
				}
			}
		} else {
			MERGER_TYPES.putAll(new DefaultMergerProvider().getMergers());
		}
	}

	/**
	 * This will parse the given {@link IConfigurationElement configuration element} and return a descriptor
	 * for it if it describes a merger provider.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link MergerProviderDescriptor} wrapped around <code>configElement</code> if it describes a
	 *         merger provider, <code>null</code> otherwise.
	 */
	private static MergerProviderDescriptor parseProvider(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_PROVIDER))
			return null;
		final MergerProviderDescriptor desc = new MergerProviderDescriptor(configElement);
		return desc;
	}

	/**
	 * Stores the given descriptor in the list of known {@link MergerProviderDescriptor}s.
	 * 
	 * @param desc
	 *            Descriptor to be added to the list of all know descriptors.
	 */
	private static void storeProviderDescriptor(MergerProviderDescriptor desc) {
		if (desc.getFileExtension() == null)
			return;

		final String[] extensions = desc.getFileExtension().split(","); //$NON-NLS-1$
		for (String mergerExtension : extensions) {
			if (!PARSED_PROVIDERS.containsKey(mergerExtension)) {
				PARSED_PROVIDERS.put(mergerExtension, new ArrayList<MergerProviderDescriptor>());
			}
			final List<MergerProviderDescriptor> set = PARSED_PROVIDERS.get(mergerExtension);
			set.add(desc);
		}
	}
}
