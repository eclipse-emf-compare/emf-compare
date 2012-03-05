/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.match.EMFCompareMatchMessages;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.match.engine.IMatchEngine;
import org.eclipse.emf.compare.util.ModelIdentifier;

/* (non-javadoc) we make use of the ordering of the engines, do not change Map and List implementations. */
/**
 * This registry will be initialized with all the match engines that could be parsed from the extension points
 * if Eclipse is running according to {@link EMFPlugin#IS_ECLIPSE_RUNNING}, else it will contain only the two
 * generic ones. Clients can add their own match engines in the registry for standalone usage.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class MatchEngineRegistry extends HashMap<String, List<Object>> {
	/** Singleton instance of the registry. */
	public static final MatchEngineRegistry INSTANCE = new MatchEngineRegistry();

	/** Name of the extension point to parse for engines. */
	private static final String MATCH_ENGINES_EXTENSION_POINT = "org.eclipse.emf.compare.match.engine"; //$NON-NLS-1$

	/** Separator for extension Metadata attributes. */
	private static final String SEPARATOR = ","; //$NON-NLS-1$

	/** Serial version UID is used when deserializing Objects. */
	private static final long serialVersionUID = 2237008034183610765L;

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_ENGINE = "matchengine"; //$NON-NLS-1$

	/** Wild card for file extensions. */
	private static final String WILDCARD = "*"; //$NON-NLS-1$

	/** Store the engine descriptors associated by a namespace pattern. */
	private final List<MatchEngineDescriptor> nsPatternDescriptors = new ArrayList<MatchEngineDescriptor>();

	/**
	 * As this is a singleton, hide the default constructor. Access the instance through the field
	 * {@link #INSTANCE}.
	 */
	private MatchEngineRegistry() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			parseExtensionMetadata();
		} else {
			// Add both generic engines
			putValue(WILDCARD, new GenericMatchEngine());
		}
	}

	/**
	 * This will return the list of engine descriptors available for a given model identifier. Engines must
	 * have been registered through an extension point for this to return anything else than an empty list.
	 * Note that engines registered against {@value #WILDCARD} will always be returned at the end of this
	 * list.
	 * 
	 * @param identifier
	 *            {@link ModelIdentifier} we seek the matching engines for.
	 * @return The list of available engine descriptors.
	 * @since 1.1
	 */
	public List<MatchEngineDescriptor> getDescriptors(ModelIdentifier identifier) {
		final List<Object> candidates = getEnginesForIdentifier(identifier);

		candidates.addAll(get(WILDCARD));

		final List<MatchEngineDescriptor> engines = new ArrayList<MatchEngineDescriptor>(candidates.size());
		for (final Object value : candidates) {
			if (value instanceof MatchEngineDescriptor) {
				engines.add((MatchEngineDescriptor)value);
			}
		}

		return engines;
	}

	/**
	 * This will return the list of engine descriptors available for a given engine identifier. Engines must
	 * have been registered through an extension point for this to return anything else than an empty list.
	 * Note that engines registered against {@value #WILDCARD} will always be returned at the end of this
	 * list.
	 * 
	 * @param engineIdentifier
	 *            Engine identifier we seek the matching engines for.<br/>
	 *            An engine identifier is a String that can describe either a file extension, a content-type
	 *            or a namespace.
	 * @return The list of available engine descriptors.
	 * @deprecated use {@link MatchEngineRegistry#getDescriptors(ModelIdentifier)} instead.
	 */
	@Deprecated
	public List<MatchEngineDescriptor> getDescriptors(String engineIdentifier) {
		final List<Object> specific = get(engineIdentifier);
		final List<Object> candidates = new ArrayList<Object>(get(WILDCARD));
		if (specific != null) {
			candidates.addAll(0, specific);
		}

		final List<MatchEngineDescriptor> engines = new ArrayList<MatchEngineDescriptor>(candidates.size());
		for (final Object value : candidates) {
			if (value instanceof MatchEngineDescriptor) {
				engines.add((MatchEngineDescriptor)value);
			}
		}
		return engines;
	}

	/**
	 * Returns the highest priority {@link IMatchEngine} registered against the given model identifiers.
	 * Specific engines will always come before generic ones regardless of their priority. If engines have
	 * been manually added to the list, the latest added will be returned.
	 * 
	 * @param identifier
	 *            {@link ModelIdentifier} to search on the registered {@link IMatchEngine}
	 * @return The best {@link IMatchEngine} for the given engine identifiers.
	 * @since 1.1
	 */
	public IMatchEngine getHighestEngine(ModelIdentifier identifier) {
		IMatchEngine highest = null;

		final List<Object> engines = getEnginesForIdentifier(identifier);

		if (engines.size() != 0) {
			highest = getSpecificHighestEngine(engines);
		}

		// couldn't find a specific engine, search through the generic ones
		if (highest == null) {
			highest = getSpecificHighestEngine(get(WILDCARD));
		}
		return highest;
	}

	/**
	 * Returns the highest priority {@link IMatchEngine} registered against the given engine identifier.
	 * Specific engines will always come before generic ones regardless of their priority. If engines have
	 * been manually added to the list, the latest added will be returned.
	 * 
	 * @param engineIdentifier
	 *            An engine identifier to search on the registered {@link IMatchEngine}.<br/>
	 *            An engine identifier is a String that can describe either a file extension, a content-type
	 *            or a namespace.
	 * @return The best {@link IMatchEngine} for the given file identifier.
	 * @deprecated use {@link MatchEngineRegistry#getHighestEngine(ModelIdentifier)} instead.
	 */
	@Deprecated
	public IMatchEngine getHighestEngine(String engineIdentifier) {
		final List<Object> engines = get(engineIdentifier);
		IMatchEngine highest = null;
		if (engines != null) {
			highest = getSpecificHighestEngine(engines);
		}

		// couldn't find a specific engine, search through the generic ones
		if (highest == null) {
			highest = getSpecificHighestEngine(get(WILDCARD));
		}
		return highest;
	}

	/**
	 * Adds the given value in the list of engines known for the given extension.
	 * 
	 * @param key
	 *            The file extension we wish to add an engine for.
	 * @param value
	 *            Engine to be added.
	 */
	public void putValue(String key, Object value) {
		if (value instanceof IMatchEngine || value instanceof MatchEngineDescriptor) {
			List<Object> values = get(key);
			if (values != null) {
				values.add(value);
			} else {
				values = new ArrayList<Object>();
				values.add(value);
				super.put(key, values);
			}
		} else
			throw new IllegalArgumentException(EMFCompareMatchMessages.getString(
					"MatchEngineRegistry.IllegalEngine", value.getClass().getName())); //$NON-NLS-1$
	}

	/**
	 * Retrieves the engines that correspond to the given identifier.
	 * 
	 * @param identifier
	 *            the {@link ModelIdentifier} we seek engines for.
	 * @return a list of {@link IMatchEngine} and {@link DiffEngineDescriptor}
	 */
	private List<Object> getEnginesForIdentifier(ModelIdentifier identifier) {
		final List<Object> candidates = new ArrayList<Object>();
		List<Object> newCandidates;

		if (identifier.getNamespace() != null) {
			newCandidates = get(identifier.getNamespace());
			if (newCandidates != null) {
				candidates.addAll(newCandidates);
			}

			for (MatchEngineDescriptor desc : nsPatternDescriptors) {
				if (identifier.getNamespace().matches(desc.getNamespacePattern())) {
					candidates.add(desc);
				}
			}
		}

		if (identifier.getContentType() != null) {
			newCandidates = get(identifier.getContentType());
			if (newCandidates != null) {
				candidates.addAll(newCandidates);
			}
		}

		if (identifier.getExtension() != null) {
			newCandidates = get(identifier.getExtension());
			if (newCandidates != null) {
				candidates.addAll(newCandidates);
			}
		}

		return candidates;
	}

	/**
	 * Returns the highest priority {@link IMatchEngine} registered among the given engine list. If engines
	 * have been manually added to the list, the latest added will be returned.
	 * 
	 * @param engines
	 *            List of engines.
	 * @return The best {@link IMatchEngine} for the given engine list.
	 */
	private IMatchEngine getSpecificHighestEngine(List<Object> engines) {
		int highestPriority = -1;
		IMatchEngine highest = null;

		for (final Object engine : engines) {
			if (engine instanceof MatchEngineDescriptor) {
				final MatchEngineDescriptor desc = (MatchEngineDescriptor)engine;
				if (desc.getPriorityValue() > highestPriority) {
					highest = desc.getEngineInstance();
					highestPriority = desc.getPriorityValue();
				}
			} else if (engine instanceof IMatchEngine) {
				highest = (IMatchEngine)engine;
			}
		}

		return highest;
	}

	/**
	 * This will parse the given {@link IConfigurationElement configuration element} and return a descriptor
	 * for it if it describes and engine.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link DiffEngineDescriptor} wrapped around <code>configElement</code> if it describes an
	 *         engine, <code>null</code> otherwise.
	 */
	private MatchEngineDescriptor parseEngine(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_ENGINE))
			return null;
		final MatchEngineDescriptor desc = new MatchEngineDescriptor(configElement);
		return desc;
	}

	/**
	 * This will parse the currently running platform for extensions and store all the match engines that can
	 * be found.
	 */
	private void parseExtensionMetadata() {
		final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
				MATCH_ENGINES_EXTENSION_POINT).getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final MatchEngineDescriptor desc = parseEngine(configElements[j]);

				final String namespacePattern = desc.getNamespacePattern();
				if (!"".equals(namespacePattern)) { //$NON-NLS-1$
					nsPatternDescriptors.add(desc);
				}

				final String[] namespaces = desc.getNamespace().split(SEPARATOR);
				for (final String ns : namespaces) {
					if (!"".equals(ns)) { //$NON-NLS-1$
						putValue(ns, desc);
					}
				}

				final String[] contentTypes = desc.getContentType().split(SEPARATOR);
				for (final String ct : contentTypes) {
					if (!"".equals(ct)) { //$NON-NLS-1$
						putValue(ct, desc);
					}
				}

				final String[] fileExtensions = desc.getFileExtension().split(SEPARATOR);
				for (final String ext : fileExtensions) {
					if (!"".equals(ext)) { //$NON-NLS-1$
						putValue(ext, desc);
					}
				}
			}
		}
	}
}
