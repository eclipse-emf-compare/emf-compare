/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.preferences;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine.Factory;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.match.DefaultRCPMatchEngineFactory;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.rcp.internal.tracer.TracingConstant;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.AbstractConfigurationUI;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.IConfigurationUIFactory;
import org.eclipse.emf.compare.rcp.ui.internal.preferences.impl.InteractiveUIContent;
import org.eclipse.emf.compare.rcp.ui.internal.preferences.impl.InteractiveUIContent.InteractiveUIBuilder;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Preference page for engines preferences
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class EnginesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Pointer to all {@link InteractiveUIContent} of each tab */
	private final Map<String, InteractiveUIContent> interactiveUis = new HashMap<String, InteractiveUIContent>();

	/** Data regarding the Difference selected engine */
	private final DataHolder<IDiffEngine> diffEngineData = new DataHolder<IDiffEngine>();

	/** Data regarding the Equivalence selected engine */
	private final DataHolder<IEquiEngine> equiEngineData = new DataHolder<IEquiEngine>();

	/** Data regarding the Requirement selected engine */
	private final DataHolder<IReqEngine> reqEngineData = new DataHolder<IReqEngine>();

	/** Data regarding the Conflicts detector selected engine */
	private final DataHolder<IConflictDetector> conflictsDetectorData = new DataHolder<IConflictDetector>();

	/** Data regarding the selected match engine factories. */
	private final DataHolder<IMatchEngine.Factory> matchEnginesData = new DataHolder<IMatchEngine.Factory>();

	public EnginesPreferencePage() {
		super();
	}

	public EnginesPreferencePage(String title) {
		super(title);
	}

	public EnginesPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public void init(IWorkbench workbench) {
		// Do not use InstanceScope.Instance to be compatible with Helios.
		@SuppressWarnings("deprecation")
		ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(),
				EMFCompareRCPPlugin.PLUGIN_ID);
		setPreferenceStore(store);
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		// Create match engine tab
		createMatchEngineTab(tabFolder);
		// Create diff engine tab
		createDiffEngineTab(tabFolder);
		// Create equi engine tab
		createEquiEngineTab(tabFolder);
		// Create req engine tab
		createReqEngineTab(tabFolder);
		// Create conflicts detectors tab
		createConflictDetectorTab(tabFolder);

		return container;
	}

	/**
	 * Create an {@link InteractiveUIContent} object of a specific type of engine.
	 * 
	 * @param registry
	 *            Registry holding engines.
	 * @param enginePreferenceKey
	 *            Preference key use to store preferences
	 * @param pref
	 *            {@link IEclipsePreferences} holding preferences.
	 * @param tabComposite
	 *            Holding composite.
	 * @param dataHolder
	 *            Data that will be synchronized with the UI.
	 * @param <T>
	 *            type of engine.
	 * @return {@link InteractiveUIContent} for a specific type of engine.
	 */
	private <T> InteractiveUIContent createEngineUIBuilder(IItemRegistry<T> registry,
			String enginePreferenceKey, IEclipsePreferences pref, Composite tabComposite,
			DataHolder<T> dataHolder) {
		IItemDescriptor<T> defaultEngine = ItemUtil.getDefaultItemDescriptor(registry, enginePreferenceKey,
				pref);
		InteractiveUIBuilder<T> uiBuilder = new InteractiveUIBuilder<T>(tabComposite, registry);
		uiBuilder.setSimple(true).setDefaultCheck(Collections.singleton(defaultEngine)).setDefaultSelection(
				defaultEngine).setHoldingData(dataHolder);
		return uiBuilder.build();
	}

	/**
	 * Create a tab to select one Conflict Detector.
	 * 
	 * @param tabFolder
	 */
	private void createConflictDetectorTab(TabFolder tabFolder) {
		IItemRegistry<IConflictDetector> conflictDetectorDescriptorRegistry = EMFCompareRCPPlugin
				.getDefault().getConflictDetectorDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.CONFLICT_DETECTOR_TAB_LABEL"),//$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EnginesPreferencePage.CONFLICT_DETECTOR_INTRO_TEXT"));//$NON-NLS-1$

		InteractiveUIContent interactiveContent = createEngineUIBuilder(conflictDetectorDescriptorRegistry,
				EMFComparePreferences.CONFLICTS_DETECTOR, EMFCompareRCPPlugin.getDefault()
						.getEMFComparePreferences(), tabComposite, conflictsDetectorData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.CONFLICTS_DETECTOR, interactiveContent);
	}

	/**
	 * Create a tab to select one Requirement Engine.
	 * 
	 * @param tabFolder
	 */
	private void createReqEngineTab(TabFolder tabFolder) {
		IItemRegistry<IReqEngine> reqEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getReqEngineDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.REQUIREMENT_ENGINE_TAB_LABEL"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EnginesPreferencePage.REQ_ENGINE_INTRO_TEXT")); //$NON-NLS-1$

		InteractiveUIContent interactiveContent = createEngineUIBuilder(reqEngineDescriptorRegistry,
				EMFComparePreferences.REQ_ENGINES, EMFCompareRCPPlugin.getDefault()
						.getEMFComparePreferences(), tabComposite, reqEngineData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.REQ_ENGINES, interactiveContent);
	}

	/**
	 * Create a tab to select one Equivalence Engine.
	 * 
	 * @param tabFolder
	 */
	private void createEquiEngineTab(TabFolder tabFolder) {
		IItemRegistry<IEquiEngine> equiEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getEquiEngineDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.EQUIVALENCES_ENGINE_TAB_LABEL"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EnginesPreferencePage.EQUI_ENGINE_INTRO_TEXT")); //$NON-NLS-1$

		InteractiveUIContent interactiveContent = createEngineUIBuilder(equiEngineDescriptorRegistry,
				EMFComparePreferences.EQUI_ENGINES, EMFCompareRCPPlugin.getDefault()
						.getEMFComparePreferences(), tabComposite, equiEngineData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.EQUI_ENGINES, interactiveContent);
	}

	/**
	 * Create a tab to select one Difference Engine.
	 * 
	 * @param tabFolder
	 */
	private void createDiffEngineTab(TabFolder tabFolder) {
		IItemRegistry<IDiffEngine> diffEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getDiffEngineDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.DIFFERENCES_ENGINE_TAB_LABEL"), EMFCompareRCPUIMessages //$NON-NLS-1$
				.getString("EnginesPreferencePage.DIFF_ENGINE_INTRO_TEXT")); //$NON-NLS-1$

		InteractiveUIContent interactiveContent = createEngineUIBuilder(diffEngineDescriptorRegistry,
				EMFComparePreferences.DIFF_ENGINES, EMFCompareRCPPlugin.getDefault()
						.getEMFComparePreferences(), tabComposite, diffEngineData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.DIFF_ENGINES, interactiveContent);

	}

	/**
	 * Create a tab to disable/enable Match Engines.
	 * 
	 * @param tabFolder
	 */
	private void createMatchEngineTab(TabFolder tabFolder) {
		IItemRegistry<Factory> matchEngineFactoryDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry();
		IItemDescriptor<Factory> defaultMatchEngineDescriptor = matchEngineFactoryDescriptorRegistry
				.getItemDescriptor(DefaultRCPMatchEngineFactory.class.getCanonicalName());
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.MATCH_ENGINE_TAB_LABEL"), EMFCompareRCPUIMessages //$NON-NLS-1$
				.getString("EnginesPreferencePage.MATCH_ENGINE_INTRO_TEXT")); //$NON-NLS-1$

		Map<String, IConfigurationUIFactory> configuratorUIRegistry = EMFCompareRCPUIPlugin.getDefault()
				.getMatchEngineConfiguratorRegistry();
		String matchEnginePreferenceKey = EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES;

		Set<IItemDescriptor<Factory>> activeItems = ItemUtil.getActiveItems(
				matchEngineFactoryDescriptorRegistry, matchEnginePreferenceKey);

		InteractiveUIBuilder<IMatchEngine.Factory> builder = new InteractiveUIBuilder<IMatchEngine.Factory>(
				tabComposite, matchEngineFactoryDescriptorRegistry);
		builder.setConfiguratorUIRegistry(configuratorUIRegistry).setDefaultCheck(activeItems)
				.setConfigurationNodeKey(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES)
				.setDefaultSelection(defaultMatchEngineDescriptor).setHoldingData(matchEnginesData);
		// Save for reset default
		interactiveUis.put(matchEnginePreferenceKey, builder.build());

	}

	/**
	 * Create skeleton of a tab.
	 * 
	 * @param tabFolder
	 * @param tabLabel
	 * @param introText
	 *            Text use as description a tab
	 * @return Main composite of the tab
	 */
	private Composite createTabSkeleton(TabFolder tabFolder, String tabLabel, String introText) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(tabLabel);
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tbtmMain.setControl(tabComposite);
		tabComposite.setLayout(new GridLayout(1, true));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		tabComposite.setLayoutData(layoutData);
		// Description text
		Label introductionText = new Label(tabComposite, SWT.WRAP);
		introductionText.setText(introText);
		return tabComposite;
	}

	@Override
	public boolean performOk() {

		setEnginesPreferences();

		storeConfigurations();

		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder traceMessage = new StringBuilder("Engines preference serialization:\n"); //$NON-NLS-1$
			String prefDelimiter = " :\n"; //$NON-NLS-1$
			String new_line = "\n"; //$NON-NLS-1$
			traceMessage.append(EMFComparePreferences.DIFF_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.DIFF_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.EQUI_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.EQUI_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.REQ_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.REQ_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.CONFLICTS_DETECTOR).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.CONFLICTS_DETECTOR))
					.append(new_line);
			traceMessage.append(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES).append(prefDelimiter)
					.append(getPreferenceStore()
							.getString(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES)).append(new_line);

			EMFCompareRCPPlugin.getDefault().log(IStatus.INFO, traceMessage.toString());
		}
		return super.performOk();
	}

	/**
	 * Set all engines preferences.
	 */
	private void setEnginesPreferences() {
		// Update preferences preferences
		setEnginePreferences(EMFComparePreferences.DIFF_ENGINES, diffEngineData.getData(), Collections
				.singleton(EMFCompareRCPPlugin.getDefault().getDiffEngineDescriptorRegistry()
						.getHighestRankingDescriptor()));
		setEnginePreferences(EMFComparePreferences.EQUI_ENGINES, equiEngineData.getData(), Collections
				.singleton(EMFCompareRCPPlugin.getDefault().getEquiEngineDescriptorRegistry()
						.getHighestRankingDescriptor()));
		setEnginePreferences(EMFComparePreferences.REQ_ENGINES, reqEngineData.getData(), Collections
				.singleton(EMFCompareRCPPlugin.getDefault().getReqEngineDescriptorRegistry()
						.getHighestRankingDescriptor()));
		setEnginePreferences(EMFComparePreferences.CONFLICTS_DETECTOR, conflictsDetectorData.getData(),
				Collections.singleton(EMFCompareRCPPlugin.getDefault()
						.getConflictDetectorDescriptorRegistry().getHighestRankingDescriptor()));
		// Set match engine to disable
		Set<IItemDescriptor<Factory>> matchEngineRegsitry = Sets.newHashSet(EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry().getItemDescriptors());
		Set<IItemDescriptor<Factory>> matchingEngineToDisable = Sets.difference(matchEngineRegsitry,
				matchEnginesData.getData());
		setEnginePreferences(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, matchingEngineToDisable,
				Collections.<IItemDescriptor<Factory>> emptyList());
	}

	/**
	 * Store the value of the configuration of each engine into preferences.
	 */
	private void storeConfigurations() {
		for (Entry<String, InteractiveUIContent> interactiveContentEntry : interactiveUis.entrySet()) {
			for (Entry<String, AbstractConfigurationUI> configuratorEntry : interactiveContentEntry
					.getValue().getConfigurators().entrySet()) {
				AbstractConfigurationUI configurator = configuratorEntry.getValue();
				configurator.storeConfiguration();
			}
		}

		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder traceMessage = new StringBuilder("Configuration serialization:\n"); //$NON-NLS-1$
			String prefDelimiter = " :\n"; //$NON-NLS-1$
			String new_line = "\n"; //$NON-NLS-1$
			String node_Label = "Node "; //$NON-NLS-1$
			String double_dot_label = " : "; //$NON-NLS-1$
			String empty_label = "EMPTY"; //$NON-NLS-1$
			for (Entry<String, InteractiveUIContent> interactiveContentEntry : interactiveUis.entrySet()) {
				String itemTypeId = interactiveContentEntry.getKey();
				for (Entry<String, AbstractConfigurationUI> configuratorEntry : interactiveContentEntry
						.getValue().getConfigurators().entrySet()) {
					String itemToConfigureId = configuratorEntry.getKey();
					Preferences storeNode = ItemUtil.getConfigurationPreferenceNode(itemTypeId,
							itemToConfigureId);
					traceMessage.append(node_Label).append(storeNode.absolutePath()).append(prefDelimiter);
					try {
						for (String propertyKey : storeNode.keys()) {
							traceMessage.append(propertyKey).append(double_dot_label).append(
									storeNode.get(propertyKey, empty_label)).append(new_line);
						}
					} catch (BackingStoreException e) {
						e.printStackTrace();
						traceMessage.append("Error in tracing ").append(storeNode.absolutePath()); //$NON-NLS-1$
					}
				}
			}

			EMFCompareRCPPlugin.getDefault().log(IStatus.INFO, traceMessage.toString());
		}

	}

	@Override
	protected void performDefaults() {
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getDiffEngineDescriptorRegistry(), EMFComparePreferences.DIFF_ENGINES, diffEngineData);
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getReqEngineDescriptorRegistry(), EMFComparePreferences.REQ_ENGINES, reqEngineData);
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getEquiEngineDescriptorRegistry(), EMFComparePreferences.EQUI_ENGINES, equiEngineData);
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getConflictDetectorDescriptorRegistry(), EMFComparePreferences.CONFLICTS_DETECTOR,
				conflictsDetectorData);
		resetDefaultPreferencesToAll(EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry(),
				EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, matchEnginesData);

		resetConfigurations();

		super.performDefaults();
	}

	/**
	 * Reset all configuration of each engine to its default value.
	 */
	private void resetConfigurations() {
		for (Entry<String, InteractiveUIContent> interactiveContentEntry : interactiveUis.entrySet()) {
			for (AbstractConfigurationUI configurator : interactiveContentEntry.getValue().getConfigurators()
					.values()) {
				configurator.resetDefault();
			}
		}
	}

	/**
	 * Reset engine preference to default using highest rank strategy.
	 * 
	 * @param registry
	 * @param preferenceKey
	 */
	private <T> void resetDefaultPreferencesToHighestRank(IItemRegistry<T> registry, String preferenceKey,
			DataHolder<T> dataObject) {
		InteractiveUIContent interactiveContent = interactiveUis.get(preferenceKey);
		if (interactiveContent != null) {
			IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
			interactiveContent.select(defaultEngine);
			interactiveContent.checkElement(defaultEngine);
			dataObject.setData(Collections.singleton(defaultEngine));
		}
	}

	/**
	 * Reset to default for a collection (using all is default).
	 * 
	 * @param registry
	 * @param preferenceKey
	 * @param dataObject
	 */
	private <T> void resetDefaultPreferencesToAll(IItemRegistry<T> registry, String preferenceKey,
			DataHolder<T> dataObject) {
		InteractiveUIContent interactiveContent = interactiveUis.get(preferenceKey);
		if (interactiveContent != null) {
			IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
			interactiveContent.select(defaultEngine);
			List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
			interactiveContent.checkElements(itemDescriptors.toArray(new IItemDescriptor[itemDescriptors
					.size()]));
			dataObject.setData(Sets.newHashSet(itemDescriptors));
		}
	}

	/**
	 * Set an engine preferences into the preferences (for a collection).
	 * 
	 * @param preferenceKey
	 * @param currentSelectedEngine
	 * @param defaultConf
	 */
	private <T> void setEnginePreferences(String preferenceKey,
			Set<IItemDescriptor<T>> currentSelectedEngine, Collection<IItemDescriptor<T>> defaultConf) {
		if (currentSelectedEngine != null && !currentSelectedEngine.contains(defaultConf)) {
			StringBuilder descriptorsKey = new StringBuilder();
			for (Iterator<IItemDescriptor<T>> iterator = currentSelectedEngine.iterator(); iterator.hasNext();) {
				IItemDescriptor<T> iItemDescriptor = iterator.next();
				descriptorsKey.append(iItemDescriptor.getID());
				if (iterator.hasNext()) {
					descriptorsKey.append(ItemUtil.PREFERENCE_DELIMITER);
				}
			}
			getPreferenceStore().setValue(preferenceKey, descriptorsKey.toString());
		} else {
			getPreferenceStore().setToDefault(preferenceKey);
		}
	}

}
