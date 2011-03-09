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
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.apply.generic.impl.GenericMPatchResolver.SymrefRefinement;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Cell editor for refining a symbolic reference resolution.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
class ElementSelectionDialogCellEditor extends DialogCellEditor {

	private final GenericMPatchResolver genericMPatchResolver;

	/** Cached flattened model. */
	private List<? extends EObject> cachedFlattenedModel;

	private final ILabelProvider labelProvider;

	private final AdapterFactoryContentProvider adapterFactoryContentProvider;

	private final AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	private ResolvedSymbolicReferences mapping;

	private final Shell shell;

	void reset(ResolvedSymbolicReferences mapping) {
		cachedFlattenedModel = null;
		this.mapping = mapping;
	}

	public ElementSelectionDialogCellEditor(GenericMPatchResolver genericMPatchResolver, Composite composite,
			AdapterFactory adapterFactory, AdapterFactoryLabelProvider adapterFactoryLabelProvider,
			ResolvedSymbolicReferences mapping2) {
		super(composite);
		reset(mapping);
		this.shell = composite.getShell();
		this.genericMPatchResolver = genericMPatchResolver;
		this.adapterFactoryContentProvider = new AdapterFactoryContentProvider(adapterFactory);
		this.adapterFactoryLabelProvider = adapterFactoryLabelProvider;
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory) {
			@Override
			public String getColumnText(Object object, int columnIndex) {
				return getText(object);
			}

			@Override
			public String getText(Object object) {
				final String text = super.getText(object);
				if (object != null && text != null && object instanceof EObject) {
					final EObject obj = (EObject)object;
					if (obj.eResource() != null) {
						final String fragment = obj.eResource().getURIFragment(obj);
						final String resource = obj.eResource().getURI().lastSegment();
						if (fragment != null && fragment.length() > 0) {
							return text + "    [" + resource + "#" + fragment + "]";
						}
					}
				}
				return text;
			}
		};
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		if (this.genericMPatchResolver.getCurrentlySelectedElement() == null) {
			return null;
		} else if (this.genericMPatchResolver.getCurrentlySelectedElement() instanceof IElementReference) {
			return refineSymref((IElementReference)this.genericMPatchResolver.getCurrentlySelectedElement());
		} else if (this.genericMPatchResolver.getCurrentlySelectedElement() instanceof IndepAddElementChange) {
			return bindAddedElement((IndepAddElementChange)this.genericMPatchResolver
					.getCurrentlySelectedElement());
		}
		return null;
	}

	private Object bindAddedElement(IndepAddElementChange change) {
		// get current element, if there is any
		final List<EObject> bound = mapping.getResolutionByChange().get(change)
				.get(change.getSubModelReference());

		// create dialog
		final BindAddedElementDialog dialog = new BindAddedElementDialog(shell, mapping.getModel(),
				change.getSubModel(), bound);
		if (dialog.open() == Dialog.OK) {
			final List<EObject> result = dialog.getResult();
			return result;
		} else {
			return null;
		}
	}

	private SymrefRefinement refineSymref(IElementReference symref) {
		final IndepChange change = MPatchUtil.getChangeFor(symref);

		// get all potential targets for the current symref
		if (cachedFlattenedModel == null) {
			cachedFlattenedModel = ExtEcoreUtils.flattenEObjects(Collections.singleton(mapping.getModel()));
		}
		final List<EObject> selectionList = new ArrayList<EObject>();
		final EClass filterEClass = symref.getType();
		for (EObject obj : cachedFlattenedModel) {
			if (filterEClass.isSuperTypeOf(obj.eClass())) {
				selectionList.add(obj);
			}
		}

		// get current targets and bounds of current symref
		final List<EObject> internalList = mapping.getResolutionByChange().get(change).get(symref);
		final List<EObject> currentList = new ArrayList<EObject>(internalList); // create copy
		final String bounds = GenericMPatchResolver.getCount(symref.getLowerBound(), symref.getUpperBound());

		// create dialog
		final SymrefResolutionDialog dialog = new SymrefResolutionDialog(shell, labelProvider, bounds, null,
				currentList, MPatchConstants.SYMBOLIC_REFERENCES_NAME + " resolution", selectionList,
				currentList, bounds, change);
		if (dialog.open() == Dialog.OK) {
			@SuppressWarnings("unchecked")
			final EList<EObject> result = (EList<EObject>)dialog.getResult();
			final List<IElementReference> refs = dialog.getReferences();
			return this.genericMPatchResolver.new SymrefRefinement(result, refs);
		} else {
			return null;
		}
	}

	/**
	 * The concrete dialog for binding existing elements.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private final class BindAddedElementDialog extends Dialog implements ISelectionChangedListener {
		private final List<EObject> initialSelection;

		private final List<EObject> currentSelection;

		private final IModelDescriptor modelDescriptor;

		private final EObject input;

		private TreeViewer modelTreeViewer;

		private Label statusLabel;

		private BindAddedElementDialog(Shell parent, EObject model, IModelDescriptor modelDescriptor,
				List<EObject> currentList) {
			super(parent);
			this.initialSelection = currentList;
			this.modelDescriptor = modelDescriptor;
			this.input = model;
			currentSelection = new ArrayList<EObject>();
		}

		@Override
		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			shell.setText("Select existing model element");
		}

		@Override
		protected boolean isResizable() {
			return true;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			final Composite composite = (Composite)super.createDialogArea(parent);

			// build table viewer
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.verticalIndent = 6;
			modelTreeViewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION
					| SWT.H_SCROLL | SWT.V_SCROLL);
			modelTreeViewer.getTree().setLayoutData(gd);
			modelTreeViewer.setContentProvider(adapterFactoryContentProvider);
			modelTreeViewer.setLabelProvider(adapterFactoryLabelProvider);
			modelTreeViewer.addSelectionChangedListener(this);
			modelTreeViewer.setInput(input);
			if (initialSelection != null) {
				modelTreeViewer.setSelection(new StructuredSelection(initialSelection.toArray()));
			}

			// clear selection button
			final Button button = new Button(composite, SWT.PUSH);
			button.setText("Clear selection");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					modelTreeViewer.setSelection(StructuredSelection.EMPTY);
				}
			});

			// add labels
			addInfoLabels(composite);

			return composite;
		}

		private void addInfoLabels(Composite contents) {
			// info label
			String info = "Please select one or multiple model elements that should be bound instead of adding new elements; "
					+ "if none is selected, a new model element is created.";
			final Label infoLabel = new Label(contents, SWT.NONE);
			infoLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING,
					GridData.VERTICAL_ALIGN_BEGINNING, true, false));
			infoLabel.setText(info);

			// status label
			statusLabel = new Label(contents, SWT.NONE);
			statusLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING,
					GridData.VERTICAL_ALIGN_BEGINNING, true, false));
		}

		private void setStatus(String message, boolean valid) {
			if (statusLabel != null) {
				statusLabel.setText(message);
			}
			Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(valid);
			}
		}

		List<EObject> getResult() {
			return currentSelection;
		}

		@SuppressWarnings("unchecked")
		public void selectionChanged(SelectionChangedEvent event) {
			currentSelection.clear();
			if (event.getSelection() != null && event.getSelection() instanceof StructuredSelection) {
				final IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if (!selection.isEmpty()) {
					currentSelection.addAll(selection.toList());
				}
			}
			validateSelection();
		}

		private void validateSelection() {
			if (currentSelection.isEmpty()) {
				setStatus("No elements selected.", true);
			} else {

				// To get a valid binding afterwards, all selected elements must have the same parent!
				EObject parent = null;
				for (EObject obj : currentSelection) {
					if (parent == null) {
						parent = obj.eContainer();
					} else if (!parent.equals(obj.eContainer())) {
						setStatus("The selected elements must have the same parent!", false);
						return; // Dirty, I know, but we are done here!!
					}
				}

				// are they described correctly?
				final List<EObject> invalid = new ArrayList<EObject>();
				for (EObject obj : currentSelection) {
					if (modelDescriptor.isDescriptorFor(obj, false) == null) {
						invalid.add(obj);
					}
				}
				if (invalid.isEmpty()) {
					setStatus(currentSelection.size() + " elements selected.", true);
				} else {
					String message = "Invalid model element(s):\n";
					for (EObject obj : invalid) {
						message += adapterFactoryLabelProvider.getText(obj) + "\n";
					}
					setStatus(message, false);
				}
			}
		}
	}

	/**
	 * The concrete dialog for refining a symbolic reference resolution.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private final class SymrefResolutionDialog extends FeatureEditorDialog {
		private final List<EObject> currentList;

		private final String bounds;

		private final IndepChange change;

		private final List<IElementReference> references;

		private TableViewer equalReferencesTable;

		private SymrefResolutionDialog(Shell parent, ILabelProvider labelProvider, Object object,
				EClassifier eClassifier, List<?> currentValues, String displayName, List<?> choiceOfValues,
				List<EObject> currentList, String bounds, IndepChange change) {
			super(parent, labelProvider, object, eClassifier, currentValues, displayName, choiceOfValues,
					false, true, true);
			this.currentList = currentList;
			this.bounds = bounds;
			this.change = change;
			references = new ArrayList<IElementReference>();
		}

		public List<IElementReference> getReferences() {
			return references;
		}

		@Override
		protected void okPressed() {
			references.clear();
			if (equalReferencesTable != null
					&& equalReferencesTable.getSelection() instanceof IStructuredSelection) {
				final IStructuredSelection selection = (IStructuredSelection)equalReferencesTable
						.getSelection();
				for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
					references.add((IElementReference)iter.next());
				}
			}
			super.okPressed();
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			final Composite contents = (Composite)super.createDialogArea(parent);
			addInfoLabel(contents);
			addEqualRefs(contents);
			return contents;
		}

		private void addEqualRefs(Composite contents) {
			// get labels
			final List<IElementReference> equalRefs = mapping
					.getEquallyResolvingReferences((IElementReference)genericMPatchResolver
							.getCurrentlySelectedElement());
			if (equalRefs != null && equalRefs.size() > 0) {

				// separator
				final Label separator = new Label(contents, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.LINE_SOLID);
				final GridData data = new GridData(GridData.FILL_HORIZONTAL);
				data.horizontalSpan = 3;
				separator.setLayoutData(data);

				// info label for reference list
				final Label infoLabel = new Label(contents, SWT.NONE);
				infoLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING,
						GridData.VERTICAL_ALIGN_BEGINNING, true, false, 3, 1));
				infoLabel.setText("Apply selection to the following equally resolving "
						+ MPatchConstants.SYMBOLIC_REFERENCES_NAME + " (CTRL + mouse click for selecting):");

				// create list
				final GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_FILL);
				gd.grabExcessHorizontalSpace = false;
				gd.grabExcessVerticalSpace = true;
				gd.horizontalSpan = 3;

				equalReferencesTable = new TableViewer(contents, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI
						| SWT.FULL_SELECTION);
				equalReferencesTable.getTable().setLayoutData(gd);
				equalReferencesTable.setContentProvider(new ArrayContentProvider());
				equalReferencesTable.setLabelProvider(adapterFactoryLabelProvider);

				// select those references which already resolve to the same list ;-)
				for (IElementReference ref : equalRefs) {
					final IndepChange otherChange = MPatchUtil.getChangeFor(ref);
					if (otherChange == null || !mapping.getResolutionByChange().containsKey(otherChange)) {
						continue;
					}
					final List<EObject> otherList = mapping.getResolutionByChange().get(otherChange).get(ref);
					if (otherList == null) {
						continue;
					}
					if (otherList.size() == currentList.size() && otherList.containsAll(currentList)) {
						references.add(ref);
					}
				}

				// set input and selection
				equalReferencesTable.setInput(equalRefs);
				equalReferencesTable.setSelection(new StructuredSelection(references));
			}
		}

		private void addInfoLabel(Composite contents) {
			String info = "This " + MPatchConstants.SYMBOLIC_REFERENCE_NAME + " ";
			if (genericMPatchResolver.getCurrentlySelectedElement().eContainmentFeature() != null) {
				info += "(" + change.getChangeKind() + "."
						+ genericMPatchResolver.getCurrentlySelectedElement().eContainmentFeature().getName()
						+ ") ";
			}
			info += "must resolve " + bounds + ".";
			final Label infoLabel = new Label(contents, SWT.NONE);
			infoLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING,
					GridData.VERTICAL_ALIGN_BEGINNING, true, false, 3, 1));
			infoLabel.setText(info);
		}
	}
}
