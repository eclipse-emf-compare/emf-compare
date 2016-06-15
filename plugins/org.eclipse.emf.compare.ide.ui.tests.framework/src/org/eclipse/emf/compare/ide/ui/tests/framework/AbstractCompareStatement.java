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

	private final static String EXTENSION_POINT_CLASS_SELECTOR = "impl"; //$NON-NLS-1$

	private final static String EXTENSION_POINT_ID_SELECTOR = "id"; //$NON-NLS-1$

	private final static String PREFERENCES_SEPARATOR = ";"; //$NON-NLS-1$

	private final static String DIFF_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + "." //$NON-NLS-1$
			+ EMFCompareRCPPlugin.DIFF_ENGINE_PPID;

	private final static String EQ_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + "." //$NON-NLS-1$
			+ EMFCompareRCPPlugin.EQUI_ENGINE_PPID;

	private final static String REQ_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + "." //$NON-NLS-1$
			+ EMFCompareRCPPlugin.REQ_ENGINE_PPID;

	private final static String CONFLICT_EXTENSION_POINT_ID = EMFCompareRCPPlugin.PLUGIN_ID + "." //$NON-NLS-1$
			+ EMFCompareRCPPlugin.CONFLICT_DETECTOR_PPID;

	private final IEclipsePreferences emfComparePreferences = EMFCompareRCPPlugin.getDefault()
			.getEMFComparePreferences();

	private final IPreferenceStore uiPreferenceStore = EMFCompareIDEUIPlugin.getDefault()
			.getPreferenceStore();

	protected final Object testObject;

	protected final FrameworkMethod test;

	private final ResolutionStrategyID resolutionStrategy;

	private final Class<?>[] disabledMatchEngines;

	private final Class<?> diffEngine;

	private final Class<?> eqEngine;

	private final Class<?> reqEngine;

	private final Class<?> conflictDetector;

	private final Class<?>[] disabledPostProcessors;

	private String defaultResolutionStrategy;

	private List<String> defaultDisabledMatchEngines = new ArrayList<String>();

	private String defaultDiffEngine = "org.eclipse.emf.compare.rcp.default.conflictDetector"; //$NON-NLS-1$

	private String defaultEqEngine = "org.eclipse.emf.compare.rcp.default.conflictDetector"; //$NON-NLS-1$

	private String defaultReqEngine = "org.eclipse.emf.compare.rcp.default.conflictDetector"; //$NON-NLS-1$

	private String defaultConflictDetector = "org.eclipse.emf.compare.rcp.default.conflictDetector"; //$NON-NLS-1$

	private List<String> defaultDisabledPostProcessors = new ArrayList<String>();

	public AbstractCompareStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, Class<?>[] disabledMatchEngineFactory,
			Class<?> diffEngine, Class<?> eqEngine, Class<?> reqEngine, Class<?> conflictDetector,
			Class<?>[] disabledPostProcessors) {
		this.testObject = testObject;
		this.test = test;
		this.resolutionStrategy = resolutionStrategy;
		this.disabledMatchEngines = disabledMatchEngineFactory;
		this.diffEngine = diffEngine;
		this.eqEngine = eqEngine;
		this.reqEngine = reqEngine;
		this.conflictDetector = conflictDetector;
		this.disabledPostProcessors = disabledPostProcessors;
	}

	protected String normalizePath(String value) {
		if (value.startsWith("/")) { //$NON-NLS-1$
			return value.substring(1, value.length());
		} else if (value.startsWith("./")) { //$NON-NLS-1$
			return value.substring(2, value.length());
		} else {
			return value;
		}
	}

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

	protected void setEMFComparePreferences() {
		setResolutionStrategyPreference();
		setMatchPreference();
		setDiffPreference();
		setEqPreference();
		setReqPreference();
		setConflictPreference();
		setPostProcessorPreference();
	}

	private void setResolutionStrategyPreference() {
		defaultResolutionStrategy = uiPreferenceStore
				.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		uiPreferenceStore.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				resolutionStrategy.name());
	}

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
