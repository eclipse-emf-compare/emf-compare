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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.engine.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.engine.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.engine.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.rcp.internal.tracer.TracingConstant;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Preference page for engines preferences
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class EnginesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Label of the group holding the text description */
	private static final String DESCRIPTION_Label = "Description"; //$NON-NLS-1$

	/** Key used to map button to the {@link Text} widget that display the description */
	private static final String DESCRIPTION_TEXT_DATA_KEY = "descriptionText"; //$NON-NLS-1$

	/** Label provider for {@link IItemDescriptor} */
	private final EngineDescriptorLabelProvider descriptorLabelProvider = new EngineDescriptorLabelProvider();

	/** ID in the preference store when no engine has been set */
	private static final String DEFAULT_ENGINE_ID = ""; //$NON-NLS-1$

	/** Pointer to list viewers for each tab */
	private Map<String, StructuredViewer> viewerFromTabs = new HashMap<String, StructuredViewer>();

	/** Label of the Diff engine tab */
	private static final String DIFFERENCES_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.DIFFERENCES_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Equi engine tab */
	private static final String EQUIVALENCES_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.EQUIVALENCES_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Req engine tab */
	private static final String REQUIREMENT_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.REQUIREMENT_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Conflict detector tab */
	private static final String CONFLICT_DETECTOR_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.CONFLICT_DETECTOR_TAB_LABEL"); //$NON-NLS-1$

	private static final String DATA_FIELD_NAME = "currentSelection"; //$NON-NLS-1$

	/** Data regarding the Difference selected engine */
	private SingleValueHolder<IDiffEngine> diffEngineData = new SingleValueHolder<IDiffEngine>();

	/** Data regarding the Equivalence selected engine */
	private SingleValueHolder<IEquiEngine> equiEngineData = new SingleValueHolder<IEquiEngine>();

	/** Data regarding the Requirement selected engine */
	private SingleValueHolder<IReqEngine> reqEngineData = new SingleValueHolder<IReqEngine>();

	/** Data regarding the Conflicts detector selected engine */
	private SingleValueHolder<IConflictDetector> conflictsDetectorData = new SingleValueHolder<IConflictDetector>();

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
		// The preferences shall be stored under EMF Compare RCP Plugin
		ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(),
				EMFCompareRCPPlugin.PLUGIN_ID);
		setPreferenceStore(store);
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		// Create diff engine tab
		createEngineTab(tabFolder, DIFFERENCES_ENGINE_TAB_LABEL, EMFCompareRCPPlugin.getDefault()
				.getDiffEngineDescriptorRegistry(), DATA_FIELD_NAME, EMFComparePreferences.DIFF_ENGINES,
				diffEngineData);
		// Create equi engine tab
		createEngineTab(tabFolder, EQUIVALENCES_ENGINE_TAB_LABEL, EMFCompareRCPPlugin.getDefault()
				.getEquiEngineDescriptorRegistry(), DATA_FIELD_NAME, EMFComparePreferences.EQUI_ENGINES,
				equiEngineData);
		// Create req engine tab
		createEngineTab(tabFolder, REQUIREMENT_ENGINE_TAB_LABEL, EMFCompareRCPPlugin.getDefault()
				.getReqEngineDescriptorRegistry(), DATA_FIELD_NAME, EMFComparePreferences.REQ_ENGINES,
				reqEngineData);
		// Create conflicts detectors tab
		createEngineTab(tabFolder, CONFLICT_DETECTOR_TAB_LABEL, EMFCompareRCPPlugin.getDefault()
				.getConflictDetectorDescriptorRegistry(), DATA_FIELD_NAME,
				EMFComparePreferences.CONFLICTS_DETECTOR, conflictsDetectorData);

		return container;
	}

	/**
	 * Create a tab using an {@link IItemRegistry}
	 * 
	 * @param tabFolder
	 *            Holder tab folder
	 * @param label
	 *            Label of the new tab
	 * @param registry
	 *            {@link IItemRegistry} used to fill the tab
	 * @param engineBindingProperty
	 *            Name of the property in {@link SingleValueHolder} that represent the type of engine
	 * @param preferenceKey
	 *            The preference key of the engine type
	 * @param dataObject
	 */
	private <T> void createEngineTab(TabFolder tabFolder, String label, IItemRegistry<T> registry,
			String engineBindingProperty, String preferenceKey, SingleValueHolder<T> dataObject) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(label);
		// Parent container
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbtmMain.setControl(composite);
		// Engine chooser composite
		Composite comboBoxCompsite = new Composite(composite, SWT.NONE);
		comboBoxCompsite.setLayout(new GridLayout(1, false));
		comboBoxCompsite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));
		// Descriptor engine Text
		Text engineDescriptionText = createDescriptionComposite(composite);

		fillEngineComposite(registry, engineBindingProperty, comboBoxCompsite, engineDescriptionText,
				preferenceKey, dataObject);

	}

	/**
	 * Composite for description. This composite hold the text field that will update with the current
	 * selection
	 * 
	 * @param composite
	 * @return
	 */
	private Text createDescriptionComposite(Composite composite) {
		Group descriptionComposite = new Group(composite, SWT.BORDER);
		descriptionComposite.setText(DESCRIPTION_Label);
		descriptionComposite.setLayout(new GridLayout(1, false));
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		Text engineDescriptionText = new Text(descriptionComposite, SWT.WRAP);
		engineDescriptionText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		engineDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		engineDescriptionText.setEditable(false);
		return engineDescriptionText;
	}

	/**
	 * Fill the composite with list of engine from a registry.
	 * 
	 * @param registry
	 *            Registry containing data
	 * @param engineBindingProperty
	 *            Name of the field of {@link SingleValueHolder} that reflect the selection of an engine (use
	 *            for binding)
	 * @param comboBoxComposite
	 *            Parent composite
	 * @param descriptionText
	 *            Text that need to be update on selection representing the description of an engine
	 * @param dataObject
	 *            Model object holding the information selected by the user
	 * @param preferenceKey
	 *            Preference key link to this tab
	 */
	private <T> void fillEngineComposite(IItemRegistry<T> registry, String engineBindingProperty,
			Composite comboBoxComposite, final Text descriptionText, String preferenceKey,
			SingleValueHolder<T> dataObject) {
		ListViewer descriptorViewer = new ListViewer(comboBoxComposite);
		descriptorViewer.addSelectionChangedListener(new DescriptionListener(descriptionText));
		descriptorViewer.setData(DESCRIPTION_TEXT_DATA_KEY, descriptionText);
		descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
		descriptorViewer.setLabelProvider(descriptorLabelProvider);
		// Save for reset default
		viewerFromTabs.put(preferenceKey, descriptorViewer);
		List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
		Collections.sort(itemDescriptors);
		descriptorViewer.setInput(itemDescriptors);
		bindEngineData(engineBindingProperty, descriptorViewer, dataObject);

		initdefaultDescriptor(registry, descriptionText, preferenceKey, descriptorViewer);
	}

	/**
	 * Initialize UI to reflect actual preferences
	 * 
	 * @param registry
	 * @param descriptionText
	 * @param preferenceKey
	 * @param descriptorViewer
	 */
	private void initdefaultDescriptor(IItemRegistry<?> registry, final Text descriptionText,
			String preferenceKey, ListViewer descriptorViewer) {
		IItemDescriptor<?> defaultEngine = ItemUtil.getDefaultItemDescriptor(registry, preferenceKey,
				EMFCompareRCPPlugin.getDefault().getEMFComparePreferences());
		descriptorViewer.setSelection(new StructuredSelection(defaultEngine), true);
		descriptionText.setText(defaultEngine.getDescription());
	}

	/**
	 * Bind selection to one {@link SingleValueHolder} field.
	 * 
	 * @param engineBindingProperty
	 *            Name of the field of {@link SingleValueHolder} that represent this engine descriptor
	 * @param engineDescriptor
	 *            {@link IItemDescriptor} linked to this button
	 * @param engineButton
	 *            button to bind
	 */
	private <T> void bindEngineData(String engineBindingProperty, ListViewer viewer,
			SingleValueHolder<T> dataObject) {
		DataBindingContext ctx = new DataBindingContext();
		// Bind the button with the corresponding field in data
		IViewerObservableValue target = ViewersObservables.observeSinglePostSelection(viewer);
		// IObservableValue target = WidgetProperties.selection().observe(viewer);
		IObservableValue model = PojoProperties.value(SingleValueHolder.class, engineBindingProperty)
				.observe(dataObject);
		ctx.bindValue(target, model);

	}

	@Override
	public boolean performOk() {

		// Update preferences preferences
		setEnginePreferences(EMFComparePreferences.DIFF_ENGINES, diffEngineData.getCurrentSelection(),
				EMFCompareRCPPlugin.getDefault().getDiffEngineDescriptorRegistry()
						.getHighestRankingDescriptor());
		setEnginePreferences(EMFComparePreferences.EQUI_ENGINES, equiEngineData.getCurrentSelection(),
				EMFCompareRCPPlugin.getDefault().getEquiEngineDescriptorRegistry()
						.getHighestRankingDescriptor());
		setEnginePreferences(EMFComparePreferences.REQ_ENGINES, reqEngineData.getCurrentSelection(),
				EMFCompareRCPPlugin.getDefault().getReqEngineDescriptorRegistry()
						.getHighestRankingDescriptor());
		setEnginePreferences(EMFComparePreferences.CONFLICTS_DETECTOR, conflictsDetectorData
				.getCurrentSelection(), EMFCompareRCPPlugin.getDefault()
				.getConflictDetectorDescriptorRegistry().getHighestRankingDescriptor());

		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder traceMessage = new StringBuilder("Preference serialization:\n"); //$NON-NLS-1$
			String prefDelimiter = " :\n"; //$NON-NLS-1$
			String new_line = "\n"; //$NON-NLS-1$
			traceMessage.append(EMFComparePreferences.DIFF_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.DIFF_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.EQUI_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.EQUI_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.REQ_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.REQ_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.CONFLICTS_DETECTOR).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.CONFLICTS_DETECTOR)).append(
					new_line);
			EMFCompareRCPPlugin.getDefault().log(IStatus.INFO, traceMessage.toString());
		}
		return super.performOk();
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
		super.performDefaults();
	}

	/**
	 * Reset preference to default
	 * 
	 * @param registry
	 * @param preferenceKey
	 */
	private <T> void resetDefaultPreferencesToHighestRank(IItemRegistry<T> registry, String preferenceKey,
			SingleValueHolder<T> dataObject) {
		StructuredViewer descriptorViewer = viewerFromTabs.get(preferenceKey);
		if (descriptorViewer != null) {
			Object _descriptionText = descriptorViewer.getData(DESCRIPTION_TEXT_DATA_KEY);
			if (_descriptionText instanceof Text) {
				Text descriptionText = (Text)_descriptionText;
				IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
				descriptorViewer.setSelection(new StructuredSelection(defaultEngine), true);
				descriptionText.setText(defaultEngine.getDescription());
				dataObject.setCurrentSelection(defaultEngine);
			}
		}
	}

	private <T> void setEnginePreferences(String preferenceKey, IItemDescriptor<T> currentSelectedEngine,
			IItemDescriptor<T> defaultConf) {
		if (currentSelectedEngine != null && !currentSelectedEngine.equals(defaultConf)) {
			getPreferenceStore().setValue(preferenceKey, currentSelectedEngine.getID());
		} else {
			getPreferenceStore().setValue(preferenceKey, DEFAULT_ENGINE_ID);
		}
	}

	/**
	 * Label provider for {@link IItemDescriptor}
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class EngineDescriptorLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IItemDescriptor<?>) {
				IItemDescriptor<?> desc = (IItemDescriptor<?>)element;
				return desc.getLabel();
			}
			return super.getText(element);
		}
	}

	/**
	 * Listener to update description text
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class DescriptionListener implements ISelectionChangedListener {
		private final Text descriptionText;

		private DescriptionListener(Text descriptionText) {
			this.descriptionText = descriptionText;
		}

		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structSelection = (IStructuredSelection)selection;
				Object selected = structSelection.getFirstElement();
				if (selected instanceof IItemDescriptor<?>) {
					IItemDescriptor<?> desc = (IItemDescriptor<?>)selected;
					String description = desc.getDescription();
					descriptionText.setText(description);
				}
			}

		}
	}

	/**
	 * Data object use to store a simple value data
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class SingleValueHolder<T> {
		/** Name of the diff engine field */

		/** Current selected diff engine */
		public IItemDescriptor<T> currentSelection;

		public IItemDescriptor<T> getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(IItemDescriptor<T> currentSelection) {
			this.currentSelection = currentSelection;
		}

	}

}
