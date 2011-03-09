/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.common.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.CommonMPatchPlugin;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchApplication;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.ListDialog;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The MPatch preferences page.
 * 
 * It stores the default choices for:
 * <ul>
 * <li>The default symbolic reference creator ({@link ISymbolicReferenceCreator}).
 * <li>The default model descriptor creator ({@link IModelDescriptorCreator}).
 * <li>The default selection of transformations after creating an {@link MPatchModel}.
 * <li>The default order of transformations after creating an {@link MPatchModel}.
 * <li>The default difference applier ({@link IMPatchApplication}).
 * <li>The default symbolic reference resolver ({@link IMPatchResolution}).
 * </ul>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class MPatchPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/* Keys for storing preferences. */
	public static final String PREFERENCES_KEY_TRANSFORMATION_ORDER = "transformation.order";
	public static final String PREFERENCES_KEY_SHOW_TRANSFORMATIONS = "transformations.hide";
	public static final String PREFERENCES_KEY_RESOLUTION = "resolution";
	public static final String PREFERENCES_KEY_APPLICATION = "application";
	public static final String PREFERENCES_KEY_SYMBOLIC_REFERENCE = "symbolic.reference";
	public static final String PREFERENCES_KEY_MODEL_DESCRIPTOR = "model.descriptor";

	/* Description for preference settings. */
	public static final String PREFERENCES_DESCRIPTION_TRANSFORMATION_ORDER = "Default selection and order of optional transformations";
	public static final String PREFERENCES_DESCRIPTION_SHOW_TRANSFORMATIONS = "Show mandatory transformations in "
			+ MPatchConstants.MPATCH_LONG_NAME + " creation wizard";
	public static final String PREFERENCES_DESCRIPTION_RESOLUTION = "Default symbolic reference resolution";
	public static final String PREFERENCES_DESCRIPTION_APPLICATION = "Default difference applier";
	public static final String PREFERENCES_DESCRIPTION_SYMBOLIC_REFERENCE = "Default symbolic reference creator";
	public static final String PREFERENCES_DESCRIPTION_MODEL_DESCRIPTOR = "Default model description creator";

	/* Additional constants used in the preference page. */
	public static final String PREFERENCES_LABEL = "Settings for " + MPatchConstants.MPATCH_LONG_NAME
			+ ", a model patching technology based on EMF Compare.";
	public static final String PREFERENCES_LABEL_DIFFAPPLYGROUP = MPatchConstants.MPATCH_LONG_NAME + " Application";
	public static final String PREFERENCES_LABEL_DIFFCREATEGROUP = MPatchConstants.MPATCH_LONG_NAME + " Creation";
	public static final String PREFERENCES_LABEL_TRANSFORMATIONS = "Transformation Details";
	public static final String PREFERENCES_LABEL_TRANSFLIST = "Select Transformation";

	/* Separator for storing transformation selections in a string. */
	public static final String PREFERENCES_TRANSFORMATION_SEPERATOR = ":";

	public MPatchPreferencesPage() {
		super(MPatchConstants.MPATCH_LONG_NAME, null, GRID);
		setPreferenceStore(CommonMPatchPlugin.getDefault().getPreferenceStore());
		setDescription(PREFERENCES_LABEL);
	}

	@Override
	protected void createFieldEditors() {
		createDiffCreationGroup();
		createDiffApplicationGroup();
	}

	protected void createDiffApplicationGroup() {
		final Group diffApplicationGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		diffApplicationGroup.setText(PREFERENCES_LABEL_DIFFAPPLYGROUP);
		final GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 2;
		gd.verticalIndent = 10;
		diffApplicationGroup.setLayoutData(gd);

		final List<String[]> comboboxValues = new ArrayList<String[]>();
		for (String res : ExtensionManager.getAllResolutions().keySet()) {
			comboboxValues.add(new String[] { res, res });
		}
		addField(new ComboFieldEditor(PREFERENCES_KEY_RESOLUTION, PREFERENCES_DESCRIPTION_RESOLUTION,
				comboboxValues.toArray(new String[0][0]), diffApplicationGroup));

		comboboxValues.clear();
		for (String app : ExtensionManager.getAllApplications().keySet()) {
			comboboxValues.add(new String[] { app, app });
		}
		addField(new ComboFieldEditor(PREFERENCES_KEY_APPLICATION, PREFERENCES_DESCRIPTION_APPLICATION,
				comboboxValues.toArray(new String[0][0]), diffApplicationGroup));
	}

	protected void createDiffCreationGroup() {
		final Group diffCreationGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
		diffCreationGroup.setText(PREFERENCES_LABEL_DIFFCREATEGROUP);
		final GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 2;
		gd.verticalIndent = 10;
		diffCreationGroup.setLayoutData(gd);

		addField(new TransformationListEditor(PREFERENCES_KEY_TRANSFORMATION_ORDER,
				PREFERENCES_DESCRIPTION_TRANSFORMATION_ORDER, diffCreationGroup));

		addField(new BooleanFieldEditor(PREFERENCES_KEY_SHOW_TRANSFORMATIONS,
				PREFERENCES_DESCRIPTION_SHOW_TRANSFORMATIONS, diffCreationGroup));

		final List<String[]> comboboxValues = new ArrayList<String[]>();
		for (String symrefs : ExtensionManager.getAllSymbolicReferenceCreators().keySet()) {
			comboboxValues.add(new String[] { symrefs, symrefs });
		}
		addField(new ComboFieldEditor(PREFERENCES_KEY_SYMBOLIC_REFERENCE, PREFERENCES_DESCRIPTION_SYMBOLIC_REFERENCE,
				comboboxValues.toArray(new String[0][0]), diffCreationGroup));

		comboboxValues.clear();
		for (String modeldescr : ExtensionManager.getAllModelDescriptorCreators().keySet()) {
			comboboxValues.add(new String[] { modeldescr, modeldescr });
		}
		addField(new ComboFieldEditor(PREFERENCES_KEY_MODEL_DESCRIPTOR, PREFERENCES_DESCRIPTION_MODEL_DESCRIPTOR,
				comboboxValues.toArray(new String[0][0]), diffCreationGroup));
	}

	public void init(IWorkbench workbench) {
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(PREFERENCES_KEY_TRANSFORMATION_ORDER)
						|| event.getProperty().equals(PREFERENCES_KEY_SHOW_TRANSFORMATIONS)
						|| event.getProperty().equals(PREFERENCES_KEY_APPLICATION)
						|| event.getProperty().equals(PREFERENCES_KEY_RESOLUTION)
						|| event.getProperty().equals(PREFERENCES_KEY_SYMBOLIC_REFERENCE)
						|| event.getProperty().equals(PREFERENCES_KEY_MODEL_DESCRIPTOR)) {
					reflectOnCore();
				}
			}
		});
	}

	protected void reflectOnCore() {
		final IEclipsePreferences corePreferences = new InstanceScope().getNode(CommonMPatchPlugin.PLUGIN_ID);
		corePreferences.put(PREFERENCES_KEY_TRANSFORMATION_ORDER,
				getPreferenceStore().getString(PREFERENCES_KEY_TRANSFORMATION_ORDER));
		corePreferences.putBoolean(PREFERENCES_KEY_SHOW_TRANSFORMATIONS,
				getPreferenceStore().getBoolean(PREFERENCES_KEY_SHOW_TRANSFORMATIONS));
		corePreferences.put(PREFERENCES_KEY_RESOLUTION, getPreferenceStore().getString(PREFERENCES_KEY_RESOLUTION));
		corePreferences.put(PREFERENCES_KEY_APPLICATION, getPreferenceStore().getString(PREFERENCES_KEY_APPLICATION));
		corePreferences.put(PREFERENCES_KEY_SYMBOLIC_REFERENCE,
				getPreferenceStore().getString(PREFERENCES_KEY_SYMBOLIC_REFERENCE));
		corePreferences.put(PREFERENCES_KEY_MODEL_DESCRIPTOR,
				getPreferenceStore().getString(PREFERENCES_KEY_MODEL_DESCRIPTOR));
		try {
			corePreferences.flush();
		} catch (BackingStoreException e) {
			// discard
		}
	}

	/**
	 * List Editor for selecting and ordering transformations. An additional text field shows the description of the
	 * currently selected transformation.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 * 
	 */
	private final class TransformationListEditor extends ListEditor {

		private Text infoText;

		private TransformationListEditor(String name, String labelText, Composite parent) {
			super(name, labelText, parent);
		}

		@Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			super.doFillIntoGrid(parent, numColumns);

			final Group group = new Group(parent, SWT.NONE);
			final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = numColumns;
			gd.heightHint = 50;
			group.setLayoutData(gd);
			group.setText(PREFERENCES_LABEL_TRANSFORMATIONS);
			group.setLayout(new FillLayout());
			infoText = new Text(group, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
			getList().addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					String info = "";
					if (getList().getSelectionCount() == 1) {
						final String selection = getList().getSelection()[0];
						final IMPatchTransformation transformation = ExtensionManager.getAllTransformations().get(
								selection);
						if (transformation != null)
							info = transformation.getDescription();
					}
					infoText.setText(info);
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}

		@Override
		protected String[] parseString(String stringList) {
			if (stringList == null || stringList.length() == 0) {
				return new String[0];
			}
			final List<String> list = new ArrayList<String>(Arrays.asList(stringList
					.split(PREFERENCES_TRANSFORMATION_SEPERATOR)));

			// maybe some old plugins are still stored in the preferences but are not any more available as plugins
			final Map<String, IMPatchTransformation> allTransformations = ExtensionManager.getAllTransformations();
			list.retainAll(allTransformations.keySet());

			// remove all mandatory transformations
			for (int i = list.size() - 1; i >= 0; i--) {
				if (!allTransformations.get(list.get(i)).isOptional())
					list.remove(i);
			}

			return list.toArray(new String[list.size()]);
		}

		@Override
		protected String getNewInputObject() {
			// all available plugins
			final Map<String, IMPatchTransformation> allTransformations = ExtensionManager.getAllTransformations();
			final Collection<String> optionalTransformations = new ArrayList<String>();
			for (IMPatchTransformation transformation : allTransformations.values()) {
				if (transformation.isOptional())
					optionalTransformations.add(transformation.getLabel());
			}

			// currently selected plugins
			// final String prefList = CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
			// CommonMPatchPlugin.PREFERENCES_KEY_TRANSFORMATION_ORDER);
			// final List<String> preferencesList = (prefList == null || prefList.length() == 0) ? Collections
			// .<String> emptyList() : Arrays.asList(prefList
			// .split(CommonMPatchPlugin.PREFERENCES_TRANSFORMATION_SEPERATOR));
			final List<String> preferencesList = Arrays.asList(getList().getItems());

			// compute the difference for selection
			if (optionalTransformations.size() > preferencesList.size()) {
				optionalTransformations.removeAll(preferencesList);

				// build and show list selection dialog
				final ListDialog dialog = new ListDialog(getShell());
				dialog.setTitle(PREFERENCES_LABEL_TRANSFLIST);
				dialog.setBlockOnOpen(true);
				dialog.setAddCancelButton(true);
				dialog.setContentProvider(new ArrayContentProvider());
				dialog.setLabelProvider(new LabelProvider());
				dialog.setInput(optionalTransformations.toArray());
				if (dialog.open() == ListDialog.OK && dialog.getResult().length > 0) {
					return (String) dialog.getResult()[0];
				} else {
					return null;
				}

			} else {
				MessageDialog.openInformation(getShell(), "No further Transformations available...",
						"No more Transformations are registered.");
				return null;
			}
		}

		@Override
		protected String createList(String[] items) {
			// encode the array of transformation labels as one string
			return CommonUtils.join(items, PREFERENCES_TRANSFORMATION_SEPERATOR);
		}
	}
}
