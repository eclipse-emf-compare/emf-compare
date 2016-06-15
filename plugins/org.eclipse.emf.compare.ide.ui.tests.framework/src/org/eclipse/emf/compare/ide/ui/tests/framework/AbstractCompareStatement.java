/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * EMFCompare specific statements must extends this class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public abstract class AbstractCompareStatement extends Statement {

	/** The class selector for the extension point. */
	private static final String EXTENSION_POINT_CLASS_SELECTOR = "impl"; //$NON-NLS-1$

	/** The id selector for the extension point. */
	private static final String EXTENSION_POINT_ID_SELECTOR = "id"; //$NON-NLS-1$

	/** The separator used to build a string of preferences. */
	private static final String PREFERENCES_SEPARATOR = ";"; //$NON-NLS-1$

	/** Separator used to construct the following IDs. */
	private static final String ID_SEPARATOR = "."; //$NON-NLS-1$

	/** The id of the diff extension point. */
	private static final String DIFF_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + ID_SEPARATOR
			+ EMFCompareRCPPlugin.DIFF_ENGINE_PPID;

	/** The id of the equivalence extension point. */
	private static final String EQ_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + ID_SEPARATOR
			+ EMFCompareRCPPlugin.EQUI_ENGINE_PPID;

	/** The id of the requirement extension point. */
	private static final String REQ_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + ID_SEPARATOR
			+ EMFCompareRCPPlugin.REQ_ENGINE_PPID;

	/** The id of the conflict extension point. */
	private static final String CONFLICT_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + ID_SEPARATOR
			+ EMFCompareRCPPlugin.CONFLICT_DETECTOR_PPID;

	/** The test class. */
	protected final Object testObject;

	/** The test method that will be run. */
	protected final FrameworkMethod test;

	/** The EMFCompare preferences. */
	private final IEclipsePreferences emfComparePreferences = EMFCompareRCPPlugin.getDefault()
			.getEMFComparePreferences();

	/** The EMFCompare UI preferences. */
	private final IPreferenceStore uiPreferenceStore = EMFCompareIDEUIPlugin.getDefault()
			.getPreferenceStore();

	/** The resolution strategy used for this test. */
	private final ResolutionStrategyID resolutionStrategy;

	/** The match engines disabled used for this test. */
	private final Class<?>[] disabledMatchEngines;

	/** The diff engine used for this test. */
	private final Class<?> diffEngine;

	/** The eq engine used for this test. */
	private final Class<?> eqEngine;

	/** The req engine used for this test. */
	private final Class<?> reqEngine;

	/** The conflict detector used for this test. */
	private final Class<?> conflictDetector;

	/** The post-processors disabled for this test. */
	private final Class<?>[] disabledPostProcessors;

	/** The default resolution strategy. */
	private String defaultResolutionStrategy = "WORKSPACE"; //$NON-NLS-1$

	/** The default disabled match engines. */
	private List<String> defaultDisabledMatchEngines = new ArrayList<String>();

	/** The default diff engine. */
	private String defaultDiffEngine = "org.eclipse.emf.compare.rcp.default.diffEngine"; //$NON-NLS-1$

	/** The default eq engine. */
	private String defaultEqEngine = "org.eclipse.emf.compare.rcp.default.equiEngine"; //$NON-NLS-1$

	/** The default req engine. */
	private String defaultReqEngine = "org.eclipse.emf.compare.rcp.default.reqEngine"; //$NON-NLS-1$

	/** The default conflict detector. */
	private String defaultConflictDetector = "org.eclipse.emf.compare.rcp.fast.conflictDetector"; //$NON-NLS-1$

	/** The default disabled post-processors. */
	private List<String> defaultDisabledPostProcessors = new ArrayList<String>();

	/**
	 * Constructor for the classic (no Git) comparison statement.
	 * 
	 * @param testObject
	 *            The test class
	 * @param test
	 *            The test method
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param selectedEngines
	 *            EMFComapre configurations for this test
	 */
	public AbstractCompareStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration selectedEngines) {
		this.testObject = testObject;
		this.test = test;
		this.resolutionStrategy = resolutionStrategy;
		this.disabledMatchEngines = selectedEngines.getDisabledMatchEngines();
		this.diffEngine = selectedEngines.getDiffEngine();
		this.eqEngine = selectedEngines.getEqEngine();
		this.reqEngine = selectedEngines.getReqEngine();
		this.conflictDetector = selectedEngines.getConflictDetector();
		this.disabledPostProcessors = selectedEngines.getDisabledPostProcessors();
	}

	/**
	 * Normalize the given path (remove first "/" and "./" if necessary).
	 * 
	 * @param value
	 *            The given path
	 * @return the normalized path
	 */
	protected String normalizePath(String value) {
		if (value.startsWith("/")) { //$NON-NLS-1$
			return value.substring(1, value.length());
		} else if (value.startsWith("./")) { //$NON-NLS-1$
			return value.substring(2, value.length());
		} else {
			return value;
		}
	}

	/**
	 * Restore preferences as they were before the test.
	 */
	protected void restoreEMFComparePreferences() {
		uiPreferenceStore.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				defaultResolutionStrategy);
		emfComparePreferences.put(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES,
				join(defaultDisabledMatchEngines, PREFERENCES_SEPARATOR));
		emfComparePreferences.put(EMFComparePreferences.DIFF_ENGINES, defaultDiffEngine);
		emfComparePreferences.put(EMFComparePreferences.EQUI_ENGINES, defaultEqEngine);
		emfComparePreferences.put(EMFComparePreferences.REQ_ENGINES, defaultReqEngine);
		emfComparePreferences.put(EMFComparePreferences.CONFLICTS_DETECTOR, defaultConflictDetector);
		emfComparePreferences.put(EMFComparePreferences.DISABLED_POST_PROCESSOR,
				join(defaultDisabledPostProcessors, PREFERENCES_SEPARATOR));
	}

	/**
	 * Set the preferences required to run the test.
	 */
	protected void setEMFComparePreferences() {
		setResolutionStrategyPreference();
		setMatchPreference();
		setDiffPreference();
		setEqPreference();
		setReqPreference();
		setConflictPreference();
		setPostProcessorPreference();
	}

	/**
	 * Set the resolution strategy preference.
	 */
	private void setResolutionStrategyPreference() {
		defaultResolutionStrategy = uiPreferenceStore
				.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		uiPreferenceStore.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				resolutionStrategy.name());
	}

	/**
	 * Set the match engine preference.
	 */
	private void setMatchPreference() {
		String disabMatchEngine = emfComparePreferences.get(
				EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES,
				join(defaultDisabledMatchEngines, PREFERENCES_SEPARATOR));
		defaultDisabledMatchEngines.clear();
		for (String matchEngine : disabMatchEngine.split(PREFERENCES_SEPARATOR)) {
			defaultDisabledMatchEngines.add(matchEngine);
		}

		List<String> matchEngineNames = Collections.emptyList();
		for (Class<?> matchEngine : disabledMatchEngines) {
			matchEngineNames.add(matchEngine.getCanonicalName());
		}
		emfComparePreferences.put(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES,
				join(matchEngineNames, PREFERENCES_SEPARATOR));
	}

	/**
	 * Set the diff engine preference.
	 */
	private void setDiffPreference() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(DIFF_EXTENSION_POINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		String diffEngineId = defaultDiffEngine;
		for (IExtension iExtension : extensions) {
			for (IConfigurationElement iConfig : iExtension.getConfigurationElements()) {
				if (iConfig.getAttribute(EXTENSION_POINT_CLASS_SELECTOR)
						.equals(diffEngine.getCanonicalName())) {
					diffEngineId = iConfig.getAttribute(EXTENSION_POINT_ID_SELECTOR);
					break;
				}
			}
		}

		defaultDiffEngine = emfComparePreferences.get(EMFComparePreferences.DIFF_ENGINES, defaultDiffEngine);
		emfComparePreferences.put(EMFComparePreferences.DIFF_ENGINES, diffEngineId);
	}

	/**
	 * Set the equivalence engine preference.
	 */
	private void setEqPreference() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(EQ_EXTENSION_POINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		String eqEngineId = defaultEqEngine;
		for (IExtension iExtension : extensions) {
			for (IConfigurationElement iConfig : iExtension.getConfigurationElements()) {
				if (iConfig.getAttribute(EXTENSION_POINT_CLASS_SELECTOR)
						.equals(eqEngine.getCanonicalName())) {
					eqEngineId = iConfig.getAttribute(EXTENSION_POINT_ID_SELECTOR);
					break;
				}
			}
		}

		defaultEqEngine = emfComparePreferences.get(EMFComparePreferences.EQUI_ENGINES, defaultEqEngine);
		emfComparePreferences.put(EMFComparePreferences.EQUI_ENGINES, eqEngineId);
	}

	/**
	 * Set the requirement engine preference.
	 */
	private void setReqPreference() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(REQ_EXTENSION_POINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		String reqEngineId = defaultReqEngine;
		for (IExtension iExtension : extensions) {
			for (IConfigurationElement iConfig : iExtension.getConfigurationElements()) {
				if (iConfig.getAttribute(EXTENSION_POINT_CLASS_SELECTOR)
						.equals(reqEngine.getCanonicalName())) {
					reqEngineId = iConfig.getAttribute(EXTENSION_POINT_ID_SELECTOR);
					break;
				}
			}
		}

		defaultReqEngine = emfComparePreferences.get(EMFComparePreferences.REQ_ENGINES, defaultReqEngine);
		emfComparePreferences.put(EMFComparePreferences.REQ_ENGINES, reqEngineId);
	}

	/**
	 * Set the conflict detector preference.
	 */
	private void setConflictPreference() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(CONFLICT_EXTENSION_POINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		String conflictDetectorId = defaultConflictDetector;
		for (IExtension iExtension : extensions) {
			for (IConfigurationElement iConfig : iExtension.getConfigurationElements()) {
				if (iConfig.getAttribute(EXTENSION_POINT_CLASS_SELECTOR)
						.equals(conflictDetector.getCanonicalName())) {
					conflictDetectorId = iConfig.getAttribute(EXTENSION_POINT_ID_SELECTOR);
					break;
				}
			}
		}

		defaultConflictDetector = emfComparePreferences.get(EMFComparePreferences.CONFLICTS_DETECTOR,
				defaultConflictDetector);
		emfComparePreferences.put(EMFComparePreferences.CONFLICTS_DETECTOR, conflictDetectorId);
	}

	/**
	 * Set the post-processors preference.
	 */
	private void setPostProcessorPreference() {
		String disabPostProcessors = emfComparePreferences.get(EMFComparePreferences.DISABLED_POST_PROCESSOR,
				join(defaultDisabledPostProcessors, PREFERENCES_SEPARATOR));
		defaultDisabledPostProcessors.clear();
		for (String postProcessor : disabPostProcessors.split(PREFERENCES_SEPARATOR)) {
			defaultDisabledPostProcessors.add(postProcessor);
		}

		List<String> postProcessorNames = Collections.emptyList();
		for (Class<?> postProcessor : disabledPostProcessors) {
			postProcessorNames.add(postProcessor.getCanonicalName());
		}
		emfComparePreferences.put(EMFComparePreferences.DISABLED_POST_PROCESSOR,
				join(postProcessorNames, PREFERENCES_SEPARATOR));
	}

	/**
	 * Join a collection of string with the given separator.
	 * 
	 * @param parts
	 *            The collection of Strings
	 * @param separator
	 *            The separator
	 * @return the joined string
	 */
	private String join(Collection<String> parts, String separator) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		int lastIndex = parts.size() - 1;
		for (String part : parts) {
			sb.append(part);
			if (i == lastIndex - 1) {
				sb.append(separator);
			} else if (i != lastIndex) {
				sb.append(separator);
			}
			i++;
		}
		return sb.toString();
	}

}
