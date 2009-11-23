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
package org.eclipse.emf.compare.diff.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.engine.IDiffEngine;
import org.eclipse.emf.compare.diff.internal.service.DefaultDiffEngineSelector;
import org.eclipse.emf.compare.diff.internal.service.DiffExtensionDescriptor;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.compare.match.metamodel.UnmatchModel;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.compare.util.ModelIdentifier;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

/**
 * Parses extension meta data to fetch the diff engine to use.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class DiffService {
	/** Wild card for file extensions. */
	private static final String ALL_ID = "*"; //$NON-NLS-1$

	/** Externalized here to avoid too many distinct usages. */
	private static final String DIFF_EXTENSION_EXTENSION_POINT = "org.eclipse.emf.compare.diff.extension"; //$NON-NLS-1$

	/** Currently set diff engine selector. */
	private static IDiffEngineSelector diffEngineSelector = new DefaultDiffEngineSelector();

	/** Keeps track of all the diff extensions we've parsed. */
	private static final Map<String, ArrayList<DiffExtensionDescriptor>> PARSED_DIFF_EXTENSIONS = new EMFCompareMap<String, ArrayList<DiffExtensionDescriptor>>();

	/** Separator for extension Metadata attributes. */
	private static final String SEPARATOR = ","; //$NON-NLS-1$

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_DIFF_EXTENSION = "diffExtension"; //$NON-NLS-1$

	static {
		// FIXME Should be externalized in a DiffExtensionRegistry
		parseExtensionMetadata();
	}

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private DiffService() {
		// prevents instantiation
	}

	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            The matching model.
	 * @return The corresponding diff model.
	 */
	public static DiffModel doDiff(MatchModel match) {
		return doDiff(match, false);
	}

	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            the matching model
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code> otherwise.
	 * @return the corresponding diff model
	 */
	public static DiffModel doDiff(MatchModel match, boolean threeWay) {
		final IDiffEngine engine = getBestDiffEngine(match);
		final DiffModel diff = engine.doDiff(match, threeWay);

		final Collection<AbstractDiffExtension> extensions = DiffService
				.getCorrespondingDiffExtensions(match);
		for (final AbstractDiffExtension ext : extensions) {
			if (ext != null) {
				ext.visit(diff);
			}
		}

		engine.reset();
		return diff;
	}

	/**
	 * Returns a DiffResourceSet created by differencing all MatchModels contained by <code>match</code>. This
	 * will call for a two-way differencing.
	 * 
	 * @param matchResourceSet
	 *            Contains the MatchModels for all compared resources.
	 * @return DiffResourceSet created by differencing all MatchModels.
	 */
	public static DiffResourceSet doDiff(MatchResourceSet matchResourceSet) {
		return doDiff(matchResourceSet, false);
	}

	/**
	 * Returns a DiffResourceSet created by differencing all MatchModels contained by <code>match</code>.
	 * Depending on the value of <code>threeWay</code>, this will call for either two- or three-way
	 * differencing.
	 * 
	 * @param matchResourceSet
	 *            Contains the MatchModels for all compared resources.
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code> otherwise.
	 * @return DiffResourceSet created by differencing all MatchModels.
	 */
	public static DiffResourceSet doDiff(MatchResourceSet matchResourceSet, boolean threeWay) {
		final DiffResourceSet diff = DiffFactory.eINSTANCE.createDiffResourceSet();
		final CrossReferencer crossReferencer = new CrossReferencer(matchResourceSet) {
			private static final long serialVersionUID = 1L;

			/** initializer. */
			{
				crossReference();
			}
		};
		for (final MatchModel match : matchResourceSet.getMatchModels()) {
			final IDiffEngine engine = getBestDiffEngine(match);
			final DiffModel diffmodel = engine.doDiffResourceSet(match, threeWay, crossReferencer);

			final Collection<AbstractDiffExtension> extensions = DiffService
					.getCorrespondingDiffExtensions(match);
			for (final AbstractDiffExtension ext : extensions) {
				if (ext != null) {
					ext.visit(diffmodel);
				}
			}

			engine.reset();
			diff.getDiffModels().add(diffmodel);
		}
		for (final UnmatchModel unmatch : matchResourceSet.getUnmatchedModels()) {
			ResourceDependencyChange dependencyChange;
			if (unmatch.getSide() == Side.LEFT) {
				dependencyChange = DiffFactory.eINSTANCE.createResourceDependencyChangeRightTarget();
			} else {
				dependencyChange = DiffFactory.eINSTANCE.createResourceDependencyChangeLeftTarget();
			}
			if (unmatch.isRemote()) {
				dependencyChange.setRemote(true);
			}
			dependencyChange.getRoots().addAll(unmatch.getRoots());
			diff.getResourceDiffs().add(dependencyChange);
		}
		return diff;
	}

	/**
	 * Returns the best {@link IDiffEngine} for the given {@link MatchModel}.
	 * 
	 * @param matchModel
	 *            The match model to differentiate.
	 * @return The best {@link IDiffEngine} for the given {@link MatchModel}
	 * @since 1.1
	 */
	public static IDiffEngine getBestDiffEngine(MatchModel matchModel) {
		IDiffEngine engine = null;
		Resource resource = null;

		if (!matchModel.getLeftRoots().isEmpty()) {
			resource = matchModel.getLeftRoots().get(0).eResource();
		}

		final ModelIdentifier identifier = new ModelIdentifier(resource);

		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault().getBoolean(
						EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION)) {
			final List<DiffEngineDescriptor> engines = DiffEngineRegistry.INSTANCE.getDescriptors(identifier);

			if (engines.size() == 1) {
				engine = engines.iterator().next().getEngineInstance();
			} else {
				engine = diffEngineSelector.selectDiffEngine(engines).getEngineInstance();
			}
		} else {
			engine = DiffEngineRegistry.INSTANCE.getHighestEngine(identifier);
		}
		return engine;
	}

	/**
	 * Returns the best {@link IDiffEngine} for a file given its extension.
	 * 
	 * @param extension
	 *            The extension of the file we need an {@link IDiffEngine} for.
	 * @return The best {@link IDiffEngine} for the given file extension.
	 * @deprecated use {@link DiffService#getBestDiffEngine(MatchModel)} instead.
	 */
	@Deprecated
	public static IDiffEngine getBestDiffEngine(String extension) {
		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault().getBoolean(
						EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION)) {
			final DiffEngineDescriptor desc = getBestDescriptor(extension);
			return desc.getEngineInstance();
		}
		return DiffEngineRegistry.INSTANCE.getHighestEngine(extension);
	}

	/**
	 * Returns all {@link AbstractDiffExtension}s registered against the given {@link MatchModel}.
	 * 
	 * @param matchModel
	 *            The {@link MatchModel} we need the {@link AbstractDiffExtension}s for.
	 * @return All of the {@link AbstractDiffExtension}s registered against the {@link MatchModel}.
	 * @since 1.1
	 */
	public static Collection<AbstractDiffExtension> getCorrespondingDiffExtensions(MatchModel matchModel) {
		final Collection<AbstractDiffExtension> result = new ArrayList<AbstractDiffExtension>();
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			Resource resource = null;
			URI uri = null;

			if (!matchModel.getLeftRoots().isEmpty()) {
				resource = matchModel.getLeftRoots().get(0).eResource();
			}
			if (resource != null) {
				uri = resource.getURI();
			}

			// FIXME engine identifiers should be wrapped into a ModelIdentifier once DiffExtension have their
			// own registry
			final ArrayList<String> engineIdentifiers = new ArrayList<String>();
			final String ns = ModelUtils.getCommonNamespace(resource);
			if (ns != null) {
				engineIdentifiers.add(ns);
			}
			final String ct = EclipseModelUtils.getCommonContentType(uri);
			if (ct != null) {
				engineIdentifiers.add(ct);
			}
			final String ext = ModelUtils.getCommonExtension(uri);
			if (ext != null) {
				engineIdentifiers.add(ext);
			}

			if (PARSED_DIFF_EXTENSIONS.containsKey(ALL_ID)) {
				for (final DiffExtensionDescriptor extensionDesc : PARSED_DIFF_EXTENSIONS.get(ALL_ID)) {
					result.add(extensionDesc.getDiffExtensionInstance());
				}
			}
			for (final String engineId : engineIdentifiers) {
				final Collection<DiffExtensionDescriptor> descs = PARSED_DIFF_EXTENSIONS.get(engineId);
				if (descs != null) {
					for (final DiffExtensionDescriptor desc : descs) {
						result.add(desc.getDiffExtensionInstance());
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns all {@link AbstractDiffExtension}s registered against the given file <tt>extension</tt>.
	 * 
	 * @param extension
	 *            The extension of the file we need the {@link AbstractDiffExtension}s for.
	 * @return All of the {@link AbstractDiffExtension}s registered against the given file <tt>extension</tt>.
	 * @deprecated use {@link DiffService#getCorrespondingDiffExtensions(MatchModel)} instead.
	 */
	@Deprecated
	public static Collection<AbstractDiffExtension> getCorrespondingDiffExtensions(String extension) {
		final Collection<AbstractDiffExtension> result = new ArrayList<AbstractDiffExtension>();
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {

			if (PARSED_DIFF_EXTENSIONS.containsKey(ALL_ID)) {
				for (final DiffExtensionDescriptor extensionDesc : PARSED_DIFF_EXTENSIONS.get(ALL_ID)) {
					result.add(extensionDesc.getDiffExtensionInstance());
				}
			}
			final Collection<DiffExtensionDescriptor> descs = PARSED_DIFF_EXTENSIONS.get(extension);
			if (descs != null) {
				for (final DiffExtensionDescriptor desc : descs) {
					result.add(desc.getDiffExtensionInstance());
				}
			}
		}
		return result;
	}

	/**
	 * Sets the diff engine selector that is to be used.
	 * 
	 * @param selector
	 *            the new engine selector.
	 */
	public static void setDiffEngineSelector(IDiffEngineSelector selector) {
		diffEngineSelector = selector;
	}

	/**
	 * Returns the best {@link DiffEngineDescriptor}.
	 * 
	 * @param extension
	 *            The file extension we need a diff engine for.
	 * @return The best {@link DiffEngineDescriptor}.
	 */
	@Deprecated
	private static DiffEngineDescriptor getBestDescriptor(String extension) {
		final List<DiffEngineDescriptor> engines = DiffEngineRegistry.INSTANCE.getDescriptors(extension);
		DiffEngineDescriptor engine = null;
		if (engines.size() == 1) {
			engine = engines.iterator().next();
		} else if (engines.size() > 1) {
			engine = diffEngineSelector.selectDiffEngine(engines);
		}
		return engine;
	}

	/**
	 * This will parse the given {@link IConfigurationElement configuration element} and return a descriptor
	 * for it if it describes a DiffExtension.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link DiffExtensionDescriptor} wrapped around <code>configElement</code> if it describes an
	 *         diff extension, <code>null</code> otherwise.
	 */
	private static DiffExtensionDescriptor parseDiffExtension(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_DIFF_EXTENSION))
			return null;
		final DiffExtensionDescriptor desc = new DiffExtensionDescriptor(configElement);
		return desc;
	}

	/**
	 * This will parse the currently running platform for extensions and store all the diff engines and diff
	 * extensions that can be found.
	 */
	private static void parseExtensionMetadata() {
		// FIXME Should be externalized in a DiffExtensionRegistry
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
					DIFF_EXTENSION_EXTENSION_POINT).getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					final DiffExtensionDescriptor desc = parseDiffExtension(configElements[j]);
					storeDiffExtensionDescriptor(desc);
				}
			}
		}
	}

	/**
	 * Stores the given descriptor in the {@link List} of known {@link DiffExtensionDescriptor}s.
	 * 
	 * @param desc
	 *            Descriptor to be added to the list of all know descriptors.
	 */
	private static void storeDiffExtensionDescriptor(DiffExtensionDescriptor desc) {
		// FIXME Should be externalized in a DiffExtensionRegistry
		final String[] namespaces = desc.getNamespace().split(SEPARATOR);
		for (String ns : namespaces) {
			if (!"".equals(ns)) { //$NON-NLS-1$
				if (!PARSED_DIFF_EXTENSIONS.containsKey(ns)) {
					PARSED_DIFF_EXTENSIONS.put(ns, new ArrayList<DiffExtensionDescriptor>());
				}
				final List<DiffExtensionDescriptor> set = PARSED_DIFF_EXTENSIONS.get(ns);
				set.add(desc);
			}
		}

		final String[] contentTypes = desc.getContentType().split(SEPARATOR);
		for (String ct : contentTypes) {
			if (!"".equals(ct)) { //$NON-NLS-1$
				if (!PARSED_DIFF_EXTENSIONS.containsKey(ct)) {
					PARSED_DIFF_EXTENSIONS.put(ct, new ArrayList<DiffExtensionDescriptor>());
				}
				final List<DiffExtensionDescriptor> set = PARSED_DIFF_EXTENSIONS.get(ct);
				set.add(desc);
			}
		}

		final String[] extensions = desc.getFileExtension().split(SEPARATOR);
		for (String ext : extensions) {
			if (!"".equals(ext)) { //$NON-NLS-1$
				if (!PARSED_DIFF_EXTENSIONS.containsKey(ext)) {
					PARSED_DIFF_EXTENSIONS.put(ext, new ArrayList<DiffExtensionDescriptor>());
				}
				final List<DiffExtensionDescriptor> set = PARSED_DIFF_EXTENSIONS.get(ext);
				set.add(desc);
			}
		}
	}
}
