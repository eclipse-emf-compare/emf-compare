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
package org.eclipse.emf.compare.diagram.ide.ui.internal.preferences;

import org.eclipse.emf.compare.diagram.ide.ui.internal.CompareDiagramIDEUIPlugin;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramConstants;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramUIMessages;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page used for <b>EMFCompare</b>, it allows the user to define the move threshold detection in
 * the diagram comparison.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramComparePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Adds our page to the default preferences dialog.
	 */
	public DiagramComparePreferencesPage() {
		super(GRID);
		setPreferenceStore(CompareDiagramIDEUIPlugin.getDefault().getPreferenceStore());
		setDescription(CompareDiagramUIMessages.getString("DiagramComparePreferencesPage.description")); //$NON-NLS-1$
	}

	/**
	 * Create the group for diagram comparison.
	 */
	public void createDiagramComparisonGroup() {
		final Group diagramGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		diagramGroup.setText(CompareDiagramUIMessages
				.getString("DiagramComparePreferencesPage.diagramComparisonGroupTitle")); //$NON-NLS-1$
		diagramGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

		final GridLayout diagramLayout = new GridLayout();
		diagramLayout.marginWidth = 0;
		diagramLayout.marginHeight = 0;
		diagramGroup.setLayout(diagramLayout);
		diagramGroup.setFont(getFieldEditorParent().getFont());

		addField(new IntegerFieldEditor(CompareDiagramConstants.PREFERENCES_KEY_MOVE_THRESHOLD,
				CompareDiagramConstants.PREFERENCES_DESCRIPTION_MOVE_THRESHOLD, diagramGroup, 3) {
			@Override
			protected void adjustForNumColumns(int numColumns) {
				// do nothing
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {

		createDiagramComparisonGroup();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// Nothing to do
	}

}
