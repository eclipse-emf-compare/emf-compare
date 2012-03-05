/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.internal.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterDescriptor;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterRegistry;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Preference page used for <b>EMFCompare</b>, it allows the user to define which files to compare with
 * <b>EMFCompare</b> and the colors to use for the differences' highlighting.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFComparePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Adds our page to the default preferences dialog.
	 */
	public EMFComparePreferencesPage() {
		super(GRID);
		setPreferenceStore(EMFCompareUIPlugin.getDefault().getPreferenceStore());
		setDescription(EMFCompareUIMessages.getString("EMFComparePreferencesPage.description")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {
		// Link to the content types page
		final PreferenceLinkArea link = new PreferenceLinkArea(getFieldEditorParent(), SWT.NONE,
				"org.eclipse.ui.preferencePages.ContentTypes", //$NON-NLS-1$
				EMFCompareUIMessages.getString("EMFComparePreferencesPage.contentTypesLink"), //$NON-NLS-1$
				(IWorkbenchPreferenceContainer)getContainer(), null);
		final GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 3;
		gd.horizontalAlignment = GridData.FILL;
		link.getControl().setLayoutData(gd);

		createMatchOptionsGroup();
		createGUIOptionsGroup();
		createColorGroup();
		createFilterOptionsGroup();
	}

	/**
	 * Creates the SWT group containing the fields for all UI specific options.
	 */
	public void createGUIOptionsGroup() {
		final Group guiGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		guiGroup.setText(EMFCompareUIMessages.getString("EMFComparePreferencesPage.guiGroupTitle")); //$NON-NLS-1$
		final GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 3;
		guiGroup.setLayoutData(gd);

		// draw differences field
		addField(new BooleanFieldEditor(EMFCompareConstants.PREFERENCES_KEY_DRAW_DIFFERENCES,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_DRAW_DIFFERENCES, guiGroup));
		// present user with a coice of engines
		addField(new BooleanFieldEditor(EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_ENGINE_SELECTION, guiGroup));
	}

	/**
	 * Creates the SWT group containing the fields for all colors.
	 */
	public void createColorGroup() {
		final Group colorGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		colorGroup.setText(EMFCompareUIMessages.getString("EMFComparePreferencesPage.colorGroupTitle")); //$NON-NLS-1$
		final GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 3;
		colorGroup.setLayoutData(gd);

		addField(new CompareColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_HIGHLIGHT_COLOR, colorGroup));
		addField(new CompareColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_CHANGED_COLOR, colorGroup));
		addField(new CompareColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_CONFLICTING_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_CONFLICTING_COLOR, colorGroup));
		addField(new CompareColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_ADDED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_ADDED_COLOR, colorGroup));
		addField(new CompareColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_REMOVED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_REMOVED_COLOR, colorGroup));
	}

	/**
	 * Creates the SWT group containing the fields for all match specific options.
	 */
	public void createMatchOptionsGroup() {
		final Group matchGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		matchGroup.setText(EMFCompareUIMessages.getString("EMFComparePreferencesPage.matchGroupTitle")); //$NON-NLS-1$
		final GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 3;
		matchGroup.setLayoutData(gd);

		// distinct meta model field
		// addField(new BooleanFieldEditor(EMFComparePreferenceKeys.PREFERENCES_KEY_DISTINCT_METAMODEL,
		// EMFCompareConstants.PREFERENCES_DESCRIPTION_DISTINCT_METAMODEL, matchGroup));
		// Search window field
		final ImageIntegerFieldEditor searchWindowEditor = new ImageIntegerFieldEditor(
				EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_SEARCH_WINDOW, matchGroup);
		addField(searchWindowEditor);

		// ignore ID field
		final FieldEditor ignoreID = new BooleanFieldEditor(
				EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_IGNORE_ID, matchGroup);
		// ignore XMI ID field
		final FieldEditor ignoreXMIID = new BooleanFieldEditor(
				EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_IGNORE_XMIID, matchGroup);

		addField(ignoreID);
		addField(ignoreXMIID);

		final GridLayout layout = (GridLayout)matchGroup.getLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = false;
	}

	/**
	 * Creates the SWT group containing the fields for filters options.
	 */
	public void createFilterOptionsGroup() {
		final Group filterGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		filterGroup.setText(EMFCompareUIMessages.getString("EMFComparePreferencesPage.filterGroupTitle")); //$NON-NLS-1$
		filterGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

		final GridLayout filtersLayout = new GridLayout();
		filtersLayout.marginWidth = 0;
		filtersLayout.marginHeight = 0;
		filterGroup.setLayout(filtersLayout);
		filterGroup.setFont(getFieldEditorParent().getFont());

		addField(new MultiSelectionFieldEditor(EMFComparePreferenceConstants.PREFERENCES_KEY_DEFAULT_FILTERS,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_FILTERS, filterGroup));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW)
						|| event.getProperty()
								.equals(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID)
						|| event.getProperty().equals(
								EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID)
						|| event.getProperty().equals(
								EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION)) {
					reflectOnCore();
				}
			}
		});
	}

	/* (non-javadoc) changes #init(IWorkbench) accordingly when adding preferences to this list. */
	/**
	 * Reflects the modifications to the general options on the org.eclipse.emf.compare.EMFComparePlugin
	 * preferences store.
	 */
	protected void reflectOnCore() {
		final IEclipsePreferences corePreferences = new InstanceScope().getNode(EMFComparePlugin.PLUGIN_ID);
		// Search window
		corePreferences.putInt(EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW,
				getPreferenceStore().getInt(EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW));
		// ID
		corePreferences.putBoolean(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID,
				getPreferenceStore().getBoolean(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID));
		// XMI ID
		corePreferences.putBoolean(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID,
				getPreferenceStore().getBoolean(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID));
		// Engine selection
		corePreferences.putBoolean(
				EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION,
				getPreferenceStore().getBoolean(
						EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION));
		try {
			corePreferences.flush();
		} catch (BackingStoreException e) {
			// discard
		}
	}

	/**
	 * Creates an {@link ColorFieldEditor} and forces it to span two columns.
	 */
	private final class CompareColorFieldEditor extends ColorFieldEditor {
		/**
		 * Constructs a new field editor given the <code>label</code> to display, the <code>name</code> of the
		 * preference it is affected to and its <code>parent</code> composite.
		 * 
		 * @param name
		 *            Key of the preference to affect this editor to.
		 * @param label
		 *            Description to display for the field editor.
		 * @param parent
		 *            Parent composite of this editor.
		 */
		public CompareColorFieldEditor(String name, String label, Composite parent) {
			super(name, label, parent);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.ColorFieldEditor#adjustForNumColumns(int)
		 */
		@Override
		protected void adjustForNumColumns(int numColumns) {
			((GridData)getColorSelector().getButton().getLayoutData()).horizontalSpan = 1;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.ColorFieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
		 *      int)
		 */
		@Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			final Control control = getLabelControl(parent);
			final GridData gd = new GridData();
			gd.horizontalSpan = 1;
			control.setLayoutData(gd);

			final Button colorButton = getChangeControl(parent);
			colorButton.setLayoutData(new GridData());
		}
	}

	/**
	 * Creates an {@link FieldEditor} showing an image, a text field and a label, accepts only integers.
	 */
	private final class ImageIntegerFieldEditor extends FieldEditor {
		/** This label will display an help icon. */
		protected Label image;

		/** Defines whether this editor contains a valid value. */
		protected boolean isValid;

		/** Keeps track of the changes. */
		protected Integer oldValue;

		/** This is the actual editor for the value. */
		protected Text textField;

		/**
		 * Creates a new field editor.
		 * 
		 * @param name
		 *            The name of the preference this field editor works on.
		 * @param labelText
		 *            The label text of the field editor.
		 * @param parent
		 *            The parent of the field editor's control.
		 */
		public ImageIntegerFieldEditor(String name, String labelText, Composite parent) {
			init(name, labelText);
			createControl(parent);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.StringFieldEditor#getNumberOfControls()
		 */
		@Override
		public int getNumberOfControls() {
			return 3;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#isValid()
		 */
		@Override
		public boolean isValid() {
			return isValid;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.StringFieldEditor#adjustForNumColumns(int)
		 */
		@Override
		protected void adjustForNumColumns(int numColumns) {
			final GridData gd = (GridData)image.getLayoutData();
			gd.horizontalSpan = 1;
			gd.grabExcessHorizontalSpace = false;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.StringFieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
		 *      int)
		 */
		@Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			final Label label = getLabelControl(parent);
			final GC gc = new GC(label);
			final Point extent = gc.textExtent("W"); //$NON-NLS-1$
			gc.dispose();

			GridData gd = new GridData();
			gd.minimumWidth = label.getText().length() * extent.x;
			gd.horizontalSpan = 1;
			gd.grabExcessHorizontalSpace = true;
			label.setLayoutData(gd);
			createTextControl(parent);

			final int charCount = 5;
			gd.widthHint = charCount * extent.x;
			gd.horizontalSpan = 1;
			gd.grabExcessHorizontalSpace = false;
			textField.setLayoutData(gd);

			image = new Label(parent, SWT.NONE);
			image.setImage(getHelpIcon());
			image.setToolTipText(EMFCompareUIMessages.getString("EMFComparePreferencesPage.searchWindowHelp")); //$NON-NLS-1$
			gd = new GridData();
			gd.horizontalSpan = 1;
			gd.grabExcessHorizontalSpace = false;
			image.setLayoutData(gd);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
		 */
		@Override
		protected void doLoad() {
			if (textField != null) {
				oldValue = getPreferenceStore().getInt(getPreferenceName());
				textField.setText(Integer.toString(oldValue));
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
		 */
		@Override
		protected void doLoadDefault() {
			if (textField != null) {
				final int value = getPreferenceStore().getDefaultInt(getPreferenceName());
				textField.setText(Integer.toString(value));
			}
			valueChanged();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doStore()
		 */
		@Override
		protected void doStore() {
			if (textField != null) {
				final int newValue = Integer.valueOf(textField.getText());
				getPreferenceStore().setValue(getPreferenceName(), newValue);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#refreshValidState()
		 */
		@Override
		protected void refreshValidState() {
			clearErrorMessage();
			try {
				Integer.parseInt(textField.getText());
				isValid = true;
			} catch (final NumberFormatException e) {
				isValid = false;
				showErrorMessage(EMFCompareUIMessages
						.getString("EMFComparePreferencesPage.ImageIntegerFieldEditor.invalidInput")); //$NON-NLS-1$
			}
		}

		/**
		 * Indicates that this field editor's value has been modified.
		 */
		protected void valueChanged() {
			final boolean oldState = isValid;
			refreshValidState();

			if (isValid != oldState) {
				fireStateChanged(IS_VALID, oldState, isValid);
			}

			if (isValid) {
				final int newValue = Integer.parseInt(textField.getText());
				if (newValue != oldValue) {
					fireValueChanged(VALUE, oldValue, newValue);
					oldValue = Integer.parseInt(textField.getText());
				}
			}
		}

		/**
		 * Creates this field editor's text control.
		 * 
		 * @param parent
		 *            The parent for this control.
		 * @return The created Text control.
		 */
		private Text createTextControl(Composite parent) {
			if (textField == null) {
				textField = new Text(parent, SWT.SINGLE | SWT.BORDER);
				textField.setFont(parent.getFont());
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent event) {
						valueChanged();
					}
				});
				textField.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent event) {
						textField = null;
					}
				});
			}
			return textField;
		}

		/**
		 * Creates and return the help icon to show in our label.
		 * 
		 * @return The help icon to show in our label.
		 */
		private Image getHelpIcon() {
			Image helpIcon = null;
			try {
				final ImageDescriptor descriptor = ImageDescriptor.createFromURL(FileLocator
						.toFileURL(Platform.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry(
								EMFCompareConstants.ICONS_PREFERENCES_HELP)));
				helpIcon = descriptor.createImage();
			} catch (final IOException e) {
				// this try/catch block only keeps the compiler happy, we should never be here
				assert false;
			}
			return helpIcon;
		}
	}

	/**
	 * Editor to select several elements.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private final class MultiSelectionFieldEditor extends FieldEditor {

		/** Viewer used to manage checkboxes. */
		private CheckboxTableViewer checkboxViewer;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            The id of the editor.
		 * @param labelText
		 *            The label used with the editor.
		 * @param parent
		 *            The parent composite.
		 */
		public MultiSelectionFieldEditor(String name, String labelText, Composite parent) {
			init(name, labelText);
			createControl(parent);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
		 */
		@Override
		protected void adjustForNumColumns(int numColumns) {
			// nothing to do
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
		 *      int)
		 */
		@Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			// Checkbox table viewer of decorators
			checkboxViewer = CheckboxTableViewer.newCheckList(parent, SWT.SINGLE | SWT.TOP | SWT.BORDER);
			checkboxViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
			checkboxViewer.getTable().setFont(parent.getFont());
			checkboxViewer.setLabelProvider(new LabelProvider() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
				 */
				@Override
				public String getText(Object element) {
					return ((DifferenceFilterDescriptor)element).getName();
				}
			});

			checkboxViewer.setContentProvider(new IStructuredContentProvider() {
				/**
				 * {@inheritDoc}
				 */
				public void dispose() {
					// Nothing to do on dispose
				}

				/**
				 * {@inheritDoc}
				 */
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					// Nothing to do on input changed
				}

				/**
				 * {@inheritDoc}
				 */
				public Object[] getElements(Object inputElement) {
					return DifferenceFilterRegistry.INSTANCE.getDescriptors().toArray();
				}
			});

			checkboxViewer.addCheckStateListener(new ICheckStateListener() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse.jface.viewers.CheckStateChangedEvent)
				 */
				public void checkStateChanged(CheckStateChangedEvent event) {
					checkboxViewer.setSelection(new StructuredSelection(event.getElement()));
				}
			});

			checkboxViewer.setInput(Collections.EMPTY_LIST);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
		 */
		@Override
		protected void doLoad() {
			if (checkboxViewer != null) {
				final String oldValue = getPreferenceStore().getString(getPreferenceName());
				final List<DifferenceFilterDescriptor> descriptors = DifferenceFilterRegistry.INSTANCE
						.getDescriptors(oldValue);
				checkboxViewer.setCheckedElements(descriptors.toArray());
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
		 */
		@Override
		protected void doLoadDefault() {
			if (checkboxViewer != null) {
				final String value = getPreferenceStore().getDefaultString(getPreferenceName());
				final List<DifferenceFilterDescriptor> descriptors = DifferenceFilterRegistry.INSTANCE
						.getDescriptors(value);
				checkboxViewer.setCheckedElements(descriptors.toArray());
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#doStore()
		 */
		@Override
		protected void doStore() {
			if (checkboxViewer != null) {
				final String result = DifferenceFilterRegistry.INSTANCE.getDescriptors(getSelection());
				getPreferenceStore().setValue(getPreferenceName(), result);
			}
		}

		/**
		 * Returns the list of {@link DifferenceFilterDescriptor} selected through the checkboxes.
		 * 
		 * @return The filter descriptors
		 */
		private List<DifferenceFilterDescriptor> getSelection() {
			final List<DifferenceFilterDescriptor> result = new ArrayList<DifferenceFilterDescriptor>();
			final Object[] selection = checkboxViewer.getCheckedElements();
			for (Object object : selection) {
				if (object instanceof DifferenceFilterDescriptor) {
					result.add((DifferenceFilterDescriptor)object);
				}
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
		 */
		@Override
		public int getNumberOfControls() {
			// TODO Auto-generated method stub
			return 1;
		}
	}
}
