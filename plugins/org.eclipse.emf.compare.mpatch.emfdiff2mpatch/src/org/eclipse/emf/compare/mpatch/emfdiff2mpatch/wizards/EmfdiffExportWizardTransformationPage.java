/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.wizards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page for configuring the transformation of creating an {@link MPatchModel}.
 * 
 * It ask the user for:
 * <ul>
 * <li>The symbolic reference creator
 * <li>The model descirptor creator
 * <li>Additional transformations
 * </ul>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class EmfdiffExportWizardTransformationPage extends WizardPage implements ISelectionChangedListener {

	private final static Map<String, IMPatchTransformation> allTransformations = ExtensionManager
			.getAllTransformations();

	/** All transformations and their order. */
	private List<String> orderedTransformations;

	/** Current choice of selected transformations. */
	private List<String> selectedTransformations;

	/** The table containing all available transformations. */
	private TableViewer transformationTableViewer;

	private Button upButton;

	private Button downButton;

	private Combo symrefCombo;

	private Combo descriptorCombo;

	private Text infoText;

	/** Disable check for mandatory transformations. */
	private boolean expertMode = false;
	
	/**
	 * Constructor for this page.
	 * 
	 * @param pageName
	 *            The name and title of the page.
	 */
	public EmfdiffExportWizardTransformationPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Configuration of " + MPatchConstants.MPATCH_SHORT_NAME + " creation.");
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		{
			final Label label1 = new Label(container, SWT.NULL);
			label1.setText("Additional transformations for " + MPatchConstants.MPATCH_SHORT_NAME + " configuration:");
			final GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
			gd1.horizontalSpan = 2;
			label1.setLayoutData(gd1);
		}
		{
			transformationTableViewer = new TableViewer(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			final GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
			gd2.horizontalSpan = 2;
			transformationTableViewer.getTable().setLayoutData(gd2);
			transformationTableViewer.addSelectionChangedListener(this);
		}
		{
			upButton = new Button(container, SWT.PUSH);
			upButton.setText("Move selection up");
			upButton.setEnabled(false);
			upButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					moveCurrentSelection(true);
					dialogChanged();
				}
			});
			downButton = new Button(container, SWT.PUSH);
			downButton.setText("Move selection down");
			downButton.setEnabled(false);
			downButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					moveCurrentSelection(false);
					dialogChanged();
				}
			});
		}
		{
			final Group infoGroup = new Group(container, SWT.NONE);
			final GridData gd3 = new GridData(GridData.FILL_BOTH);
			gd3.horizontalSpan = 2;
			infoGroup.setLayoutData(gd3);
			infoGroup.setText("Transformation Details");
			infoGroup.setLayout(new FillLayout());
			infoText = new Text(infoGroup, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		}
		{
			new Label(container, SWT.NULL).setText(MPatchConstants.SYMBOLIC_REFERENCES_NAME + " Creator:");
			symrefCombo = new Combo(container, SWT.READ_ONLY);

			new Label(container, SWT.NULL).setText("Model Descriptor Creator:");
			descriptorCombo = new Combo(container, SWT.READ_ONLY);
		}
		{
			final Label label2 = new Label(container, SWT.NULL);
			label2.setText("Tipp: the default values are defined in the preferences page!");
			final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			label2.setLayoutData(gd);
			label2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					expertMode = true;
				}
			});
		}

		initialize();
		setControl(container);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		upButton.setEnabled(!event.getSelection().isEmpty());
		downButton.setEnabled(!event.getSelection().isEmpty());
		String info = "";
		if (!event.getSelection().isEmpty()) {
			if (event.getSelection() instanceof IStructuredSelection) {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.size() == 1) {
					final Object element = selection.getFirstElement();
					if (element instanceof String) {
						final IMPatchTransformation transformation = allTransformations.get(element);
						if (transformation != null)
							info = transformation.getDescription();
					}
				}
			}
		}
		infoText.setText(info);

		// update selected transformations
		selectedTransformations.clear();
		for (TableItem item : transformationTableViewer.getTable().getItems()) {
			if (item.getChecked())
				selectedTransformations.add(item.getText());
		}
		dialogChanged();
	}

	@SuppressWarnings("unchecked")
	protected void moveCurrentSelection(boolean up) {
		// get selection
		if (!transformationTableViewer.getSelection().isEmpty()) {
			if (transformationTableViewer.getSelection() instanceof IStructuredSelection) {
				final IStructuredSelection selection = (IStructuredSelection) transformationTableViewer.getSelection();
				final List<String> elements = selection.toList();

				// update list
				boolean ignore = true; // ignore the first element, since it cannot be moved further
				for (int i = 0; i < orderedTransformations.size(); i++) {
					final int j = up ? i : orderedTransformations.size() - i - 1;
					if (ignore && !elements.contains(orderedTransformations.get(j))) {
						ignore = false;
					} else if (ignore && elements.contains(orderedTransformations.get(j))) {
						// skip this
					} else if (!ignore && elements.contains(orderedTransformations.get(j))) {
						Collections.swap(orderedTransformations, j, up ? j - 1 : j + 1);
					} else if (!ignore && !elements.contains(orderedTransformations.get(j))) {
						// skip this
					}
				}
			}
		}

	}

	private void initialize() {
		// prepare transformations, first all mandatory transformations
		selectedTransformations = new ArrayList<String>(ExtensionManager.getSelectedOptionalTransformations());
		selectedTransformations.addAll(ExtensionManager.getMandatoryTransformations());
		orderedTransformations = new ArrayList<String>(allTransformations.keySet());

		// initialize table
		transformationTableViewer.setLabelProvider(new LabelProvider());
		transformationTableViewer.setContentProvider(new ArrayContentProvider());
		dialogChanged();

		// set symref and descriptor creators
		final Map<String, ISymbolicReferenceCreator> allSymrefCreators = ExtensionManager
				.getAllSymbolicReferenceCreators();
		symrefCombo.setItems(allSymrefCreators.keySet().toArray(new String[0]));
		symrefCombo.setText(ExtensionManager.getSelectedSymbolicReferenceCreator().getLabel());

		final Map<String, IModelDescriptorCreator> allDescriptorCreators = ExtensionManager
				.getAllModelDescriptorCreators();
		descriptorCombo.setItems(allDescriptorCreators.keySet().toArray(new String[0]));
		descriptorCombo.setText(ExtensionManager.getSelectedModelDescriptorCreator().getLabel());
		
		dialogChanged();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			initialize();
		}
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Set status and notify user if something is wrong.
	 */
	private void dialogChanged() {
		transformationTableViewer.setInput(orderedTransformations);
		String msg = null;
		for (TableItem item : transformationTableViewer.getTable().getItems()) {
			final String label = item.getText();
			final boolean checked = selectedTransformations.contains(label);
			item.setChecked(checked);
			if (!checked && !expertMode && !allTransformations.get(label).isOptional()) {
				msg = (msg == null ? "" : msg + " ") + label + " is a mandatory transformation!";
			}
		}
		updateStatus(msg);
	}

	/**
	 * Get the selected transformation. This is for the Wizard to get the selection of the user.
	 * 
	 * @return The list of transformation.
	 */
	public List<IMPatchTransformation> getTransformations() {
		final List<IMPatchTransformation> result = new ArrayList<IMPatchTransformation>();
		for (String label : selectedTransformations) {
			result.add(allTransformations.get(label));
		}
		return result;
	}

	public ISymbolicReferenceCreator getSymbolicReferenceCreator() {
		return ExtensionManager.getAllSymbolicReferenceCreators().get(symrefCombo.getText());
	}

	public IModelDescriptorCreator getModelDescriptorCreator() {
		return ExtensionManager.getAllModelDescriptorCreators().get(descriptorCombo.getText());
	}
}
