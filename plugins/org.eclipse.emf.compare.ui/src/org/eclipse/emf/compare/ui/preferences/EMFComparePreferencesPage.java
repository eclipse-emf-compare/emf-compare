/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.util.EMFComparePreferenceKeys;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
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
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		link.getControl().setLayoutData(gd);

		// Search window field
		final ImageIntegerFieldEditor searchWindowEditor = new ImageIntegerFieldEditor(
				EMFComparePreferenceKeys.PREFERENCES_KEY_SEARCH_WINDOW,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_SEARCH_WINDOW, getFieldEditorParent());
		addField(searchWindowEditor);
		
		// distinct meta model field
		/* TODO uncomment this to show the option on the preferences page. should work
		 * "out-of-the-box" but needs to be double-checked
		 */
//		addField(new BooleanFieldEditor(EMFComparePreferenceKeys.PREFERENCES_KEY_DISTINCT_METAMODEL,
//				EMFCompareConstants.PREFERENCES_DESCRIPTION_DISTINCT_METAMODEL, getFieldEditorParent()));

		// ignore ID field
		addField(new BooleanFieldEditor(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_ID,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_IGNORE_ID, getFieldEditorParent()));

		// ignore XMI ID field
		addField(new BooleanFieldEditor(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_XMIID,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_IGNORE_XMIID, getFieldEditorParent()));

		// draw differences field
		addField(new BooleanFieldEditor(EMFCompareConstants.PREFERENCES_KEY_DRAW_DIFFERENCES,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_DRAW_DIFFERENCES, getFieldEditorParent()));

		// color fields group
		final Group colorGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		colorGroup.setText(EMFCompareUIMessages.getString("EMFComparePreferencesPage.colorGroupTitle")); //$NON-NLS-1$
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
		gd = new GridData();
		gd.horizontalSpan = 3;
		gd.grabExcessHorizontalSpace = true;
		colorGroup.setLayoutData(gd);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFComparePreferenceKeys.PREFERENCES_KEY_SEARCH_WINDOW)
						|| event.getProperty().equals(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_ID)
						|| event.getProperty().equals(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_XMIID)) {
					reflectOnCore();
				}
			}
		});
	}

	/**
	 * Reflects the modifications to the search window and to the ignore booleans to
	 * org.eclipse.emf.compare.EMFComparePlugin preferences store.
	 */
	protected void reflectOnCore() {
		final Preferences corePreferences = EMFComparePlugin.getDefault().getPluginPreferences();
		// Search window
		corePreferences.setValue(EMFComparePreferenceKeys.PREFERENCES_KEY_SEARCH_WINDOW, getPreferenceStore()
				.getInt(EMFComparePreferenceKeys.PREFERENCES_KEY_SEARCH_WINDOW));
		// ID
		corePreferences.setValue(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_ID, getPreferenceStore()
				.getBoolean(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_ID));
		// XMI ID
		corePreferences.setValue(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_XMIID, getPreferenceStore()
				.getBoolean(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_XMIID));
	}

	/**
	 * Creates an {@link ColorFieldEditor} and forces it to span two columns.
	 */
	private final class CompareColorFieldEditor extends ColorFieldEditor {
		/**
		 * Constructs a new field editor given the <code>label</code> to display, the <code>name</code> of
		 * the preference it is affected to and its <code>parent</code> composite.
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
			gd.horizontalSpan = numColumns - 2;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.StringFieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
		 *      int)
		 */
		@Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			getLabelControl(parent);
			createTextControl(parent);

			GridData gd = new GridData();
			final int charCount = 5;
			final GC gc = new GC(textField);
			final Point extent = gc.textExtent("W"); //$NON-NLS-1$
			gc.dispose();
			gd.widthHint = charCount * extent.x;
			textField.setLayoutData(gd);

			image = new Label(parent, SWT.NONE);
			image.setImage(getHelpIcon());
			image
					.setToolTipText(EMFCompareUIMessages
							.getString("EMFComparePreferencesPage.searchWindowHelp")); //$NON-NLS-1$
			gd = new GridData();
			gd.horizontalSpan = numColumns - 1;
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
			} catch (NumberFormatException e) {
				isValid = false;
				showErrorMessage(EMFCompareUIMessages
						.getString("EMFComparePrefetencesPage.ImageIntegerFieldEditor.invalidInput")); //$NON-NLS-1$
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
			} catch (IOException e) {
				// this try/catch block only keeps the compiler happy, we should never be here
				assert false;
			}
			return helpIcon;
		}
	}
}
