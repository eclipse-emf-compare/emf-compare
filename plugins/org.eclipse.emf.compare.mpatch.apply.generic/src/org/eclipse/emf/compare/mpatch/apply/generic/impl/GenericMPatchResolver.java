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
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.apply.generic.GenericApplyActivator;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolutionHost;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The default implementation of the interface for resolving symbolic references.
 * 
 * By default, it iterates over all symbolic references ({@link IElementReference}s) and resolves them against the
 * target model. The result is an instance of {@link ResolvedSymbolicReferences}. Then instances of
 * {@link IMPatchResolution} can be used for symbolic references refinements.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class GenericMPatchResolver implements IMPatchResolution, ISelectionChangedListener, ICheckStateListener {

	/** TableTreeViewer columns. */
	private static final String[] COLUMNS = new String[] { "OBJECT", "COUNT", "ELEMENTS" };

	/** The current mapping of resolved elements. */
	private ResolvedSymbolicReferences mapping;

	/** Keep track of all deactivated changes (used for viewer checkboxes. */
	private Map<IndepChange, Map<IElementReference, List<EObject>>> deactivatedChanges;

	/** The host to which we report the resolution in interactive mode. */
	private IMPatchResolutionHost host;

	/** The current selection in the viewer. */
	private IElementReference currentlySelectedElement;

	/** Cached flattened model. */
	private List<? extends EObject> cachedFlattenedModel;

	/** The table tree viewer for presenting the resolved references to the user. */
	private CheckboxTreeViewer viewer;

	/** The adapter factory for the providers. */
	private AdapterFactory adapterFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return "User-interactive " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + " resolution";
	}

	/**
	 * An automated refinement strategy:
	 * <ol>
	 * <li>remove all corresponding elements that produce invalid states
	 * <li>remove all additions for those corresponding parents that have already such an element
	 * <li>check cardinality of all corresponding elements and ignore violated changes
	 * <li>ignore all changes with invalid states
	 * <li>ignore all depending changes
	 * </ol>
	 * 
	 * The result should be free of any conflicts.
	 * 
	 * @param mapping
	 *            The resolution that should be refined automatically.
	 */
	@Override
	public void refineResolution(ResolvedSymbolicReferences mapping) {
		AutoMPatchResolver.resolve(mapping);
	}

	/**
	 * This implementation uses a {@link CheckboxTreeViewer} for presenting the resolution and provides cell editors for
	 * refining it.
	 * 
	 * This implementation supports refining multiple symbolic references at once by selecting them in the refinement
	 * cell editor dialog. However, the {@link InternalReferences} must be introduced before to make that work.
	 * 
	 * @see IMPatchResolution
	 */
	@Override
	public void refineResolution(ResolvedSymbolicReferences mapping, IMPatchResolutionHost host) {
		this.mapping = mapping;
		this.host = host;
		deactivatedChanges = new HashMap<IndepChange, Map<IElementReference, List<EObject>>>();
		cachedFlattenedModel = null;
		viewer.setContentProvider(new SymbolicReferenceContentProvider(mapping.getDirection(), adapterFactory));
		viewer.setLabelProvider(new SymbolicReferenceLabelProvider(mapping, adapterFactory));
		viewer.setInput(mapping.getMPatchModel());
		if (onlyGroups(mapping.getMPatchModel().getChanges()))
			viewer.expandToLevel(2);

		final Set<EClass> eClasses = new HashSet<EClass>();
		eClasses.add(MPatchPackage.eINSTANCE.getChangeGroup());
		eClasses.add(MPatchPackage.eINSTANCE.getIElementReference());
		final List<EObject> groupsAndSymrefs = ExtEcoreUtils.collectTypedElements(
				mapping.getMPatchModel().getChanges(), eClasses, true);
		viewer.setGrayedElements(groupsAndSymrefs.toArray());
		viewer.setCheckedElements(mapping.getResolutionByChange().keySet().toArray());
	}

	private boolean onlyGroups(EList<IndepChange> changes) {
		for (IndepChange indepChange : changes) {
			if (!(indepChange instanceof ChangeGroup))
				return false;
		}
		return true;
	}

	/**
	 * Build the {@link CheckboxTreeViewer} for refining symbolic references.
	 * 
	 * @see IMPatchResolution
	 */
	@Override
	public void buildResolutionGUI(final Composite parent, final AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;

		// two-column layout
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(2, false));

		// add a tree table viewer for the resolved references
		viewer = new CheckboxTreeViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		final GridData gd2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gd2.grabExcessHorizontalSpace = true;
		gd2.grabExcessVerticalSpace = true;
		gd2.horizontalSpan = 2;
		viewer.getTree().setLayoutData(gd2);

		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		// first column: indepchange / symref (leafs)
		final TreeColumn objectColumn = new TreeColumn(tree, SWT.NONE);
		objectColumn.setText("Change / " + MPatchConstants.SYMBOLIC_REFERENCE_NAME);
		objectColumn.setResizable(true);
		objectColumn.setWidth(450);

		// second column: total number of resolved elements
		final TreeColumn countColumn = new TreeColumn(tree, SWT.NONE);
		countColumn.setText("#");
		countColumn.setResizable(true);
		countColumn.setWidth(40);

		// third column: actual number of resolved elements
		final TreeColumn elementsColumn = new TreeColumn(tree, SWT.NONE);
		elementsColumn.setText("Resolution details");
		elementsColumn.setResizable(true);
		elementsColumn.setWidth(200);

		// add cell editor for new selection of emf model elements
		final CellEditor[] editors = new CellEditor[3];
		editors[0] = new ElementSelectionDialogCellEditor(viewer.getTree(), adapterFactory);
		editors[1] = editors[0];
		editors[2] = editors[0];
		viewer.setColumnProperties(COLUMNS);
		viewer.setCellEditors(editors);

		// provide option to automatically only select valid changes (call non-GUI refinement)
		final Button button = new Button(container, SWT.PUSH);
		button.setText("Resolve all conflicts");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (host != null && mapping != null) {
					resolveConflicts();
				}
			}
		});
		final Label infoLabel = new Label(container, SWT.NONE);
		infoLabel.setText("Automatically ignore all invalid changes -- manual review recommended afterwards!");
		infoLabel.setToolTipText("All changes that are not valid will be ignored.");

		// to update the mapping we need cellmodifiers
		viewer.setCellModifier(new SymbolicReferenceCellModifier());

		// keep track of the currently selected element for the cell editor
		viewer.addSelectionChangedListener(this);

		// get notified if the checkbox of an element changes
		viewer.addCheckStateListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		final boolean newState = event.getChecked();

		// allow only regular changed to be checked
		if (event.getElement() instanceof IndepChange && !(event.getElement() instanceof ChangeGroup)) {
			final IndepChange change = (IndepChange) event.getElement();
			updateMapping(change, newState); // update the mapping
			host.resolved(mapping); // report the updated mapping
			// StableTreeRefresher.refresh(viewer); // refresh the viewer
			viewer.refresh();
		} else {
			event.getCheckable().setChecked(event.getElement(), false);
		}

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		currentlySelectedElement = null;
		if (event.getSelection() instanceof IStructuredSelection) {
			final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if (selection.size() == 1 && selection.getFirstElement() instanceof IElementReference) {
				currentlySelectedElement = (IElementReference) selection.getFirstElement();
			}
		}
	}

	/**
	 * Update the mapping in case a change was selected or unselected.
	 * 
	 * @param change
	 *            The change whose state was changed.
	 * @param addition
	 *            <code>true</code>, if the change was selected and thus should be added to the mapping;
	 *            <code>false</code> if it was removed.
	 */
	private void updateMapping(final IndepChange change, final boolean addition) {
		if ((addition && mapping.getResolutionByChange().containsKey(change))
				|| (!addition && deactivatedChanges.containsKey(change))) {
			throw new IllegalStateException("The checkbox state and the mapping is inconsistent for: " + change);
		}

		// initialize collector
		final List<IndepChange> collector = new ArrayList<IndepChange>();

		// collect all changes according to the dependency graph which should also be selected / deselected
		final Queue<IndepChange> queue = new ArrayDeque<IndepChange>();
		queue.add(change);
		while (!queue.isEmpty()) {
			final IndepChange change2 = queue.poll();
			if (!collector.contains(change2)) {
				collector.add(change2);
				if (addition) {
					queue.addAll(change2.getDependsOn());
				} else
					queue.addAll(change2.getDependants());
			}
		}

		// store all checked elements in the viewer
		final List<Object> checkedElements = new ArrayList<Object>();
		Collections.addAll(checkedElements, viewer.getCheckedElements());

		if (addition) {
			// update the mapping
			for (IndepChange add : collector) {
				if (deactivatedChanges.keySet().contains(add))
					mapping.getResolutionByChange().put(add, deactivatedChanges.remove(add));
			}

			// update the check states in the viewer
			if (checkedElements.addAll(collector))
				viewer.setCheckedElements(checkedElements.toArray());
		} else { // deletion
			for (IndepChange del : collector) {
				if (mapping.getResolutionByChange().keySet().contains(del))
					deactivatedChanges.put(del, mapping.getResolutionByChange().remove(del));
			}

			// update the check states in the viewer
			if (checkedElements.removeAll(collector))
				viewer.setCheckedElements(checkedElements.toArray());
		}
	}

	private void updateElements(SymrefRefinement refinement) {
		for (IElementReference ref : refinement.references) {
			final IndepChange change = MPatchUtil.getChangeFor(ref);
			mapping.getResolutionByChange().get(change).put(ref, refinement.elements);
		}
		host.resolved(mapping);
		viewer.refresh();
	}

	private String getCount(int lowerBound, int upperBound) {
		if (lowerBound < 0) {
			throw new IllegalArgumentException("lower bound must not be less than 0!");
		} else if (upperBound == lowerBound) {
			return "exactly " + String.valueOf(upperBound) + " element" + (upperBound == 1 ? "" : "s");
		} else if (upperBound < 0) {
			return "at least " + lowerBound + " elements";
		} else if (upperBound > lowerBound) {
			return "between " + lowerBound + " and " + upperBound + " elements";
		} else {
			throw new IllegalArgumentException("lower bound must not be higher than upper bound!");
		}
	}

	private void resolveConflicts() {
		// temporarily add all (!) changes to deactivatedChanges
		for (IndepChange change : mapping.getResolutionByChange().keySet()) {
			// they should be disjunct!
			if (deactivatedChanges.containsKey(change))
				GenericApplyActivator.getDefault().logWarning("A change was active and inactive at the same time: " + change);
			deactivatedChanges.put(change, mapping.getResolutionByChange().get(change));
		}
		refineResolution(mapping);
		
		// remove all active changes from deactiveChanges
		for (IndepChange change : mapping.getResolutionByChange().keySet()) {
			deactivatedChanges.remove(change);
		}
		
		// update checkboxed
		viewer.setCheckedElements(mapping.getResolutionByChange().keySet().toArray());
		
		host.resolved(mapping);
		viewer.refresh();
	}

	/**
	 * Cell editor for refining a symbolic reference resolution. 
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private class ElementSelectionDialogCellEditor extends DialogCellEditor {

		private final ILabelProvider labelProvider;

		public ElementSelectionDialogCellEditor(Composite parent, AdapterFactory adapterFactory) {
			super(parent);
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
							if (fragment != null && fragment.length() > 0)
								return text + "    [" + resource + "#" + fragment + "]";
						}
					}
					return text;
				}
			};
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			if (currentlySelectedElement == null)
				return null;
			final IndepChange change = MPatchUtil.getChangeFor(currentlySelectedElement);

			// get all potential targets for the current symref
			if (cachedFlattenedModel == null) {
				cachedFlattenedModel = ExtEcoreUtils.flattenEObjects(Collections.singleton(mapping.getModel()));
			}
			final List<EObject> selectionList = new ArrayList<EObject>();
			final EClass filterEClass = currentlySelectedElement.getType();
			for (EObject obj : cachedFlattenedModel) {
				if (filterEClass.isSuperTypeOf(obj.eClass()))
					selectionList.add(obj);
			}

			// get current targets and bounds of current symref
			final List<EObject> internalList = mapping.getResolutionByChange().get(change)
					.get(currentlySelectedElement);
			final List<EObject> currentList = new ArrayList<EObject>(internalList); // create copy
			final String bounds = getCount(currentlySelectedElement.getLowerBound(), currentlySelectedElement
					.getUpperBound());

			// create dialog
			final SymrefResolutionDialog dialog = new SymrefResolutionDialog(viewer.getTree().getShell(),
					labelProvider, bounds, null, currentList, MPatchConstants.SYMBOLIC_REFERENCES_NAME + " resolution",
					selectionList, currentList, bounds, change);
			if (dialog.open() == Dialog.OK) {
				@SuppressWarnings("unchecked")
				final EList<EObject> result = (EList<EObject>) dialog.getResult();
				final List<IElementReference> refs = dialog.getReferences();
				return new SymrefRefinement(result, refs);
			} else
				return null;
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
				super(parent, labelProvider, object, eClassifier, currentValues, displayName, choiceOfValues);
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
				if (equalReferencesTable != null && equalReferencesTable.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection selection = (IStructuredSelection) equalReferencesTable.getSelection();
					for (Iterator<?> iter = selection.iterator(); iter.hasNext();)
						references.add((IElementReference) iter.next());
				}
				super.okPressed();
			}

			@Override
			protected Control createDialogArea(Composite parent) {
				final Composite contents = (Composite) super.createDialogArea(parent);
				addInfoLabel(contents);
				addEqualRefs(contents);
				return contents;
			}

			private void addEqualRefs(Composite contents) {
				// get labels
				final List<IElementReference> equalRefs = mapping
						.getEquallyResolvingReferences(currentlySelectedElement);
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
					final GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
					gd.grabExcessHorizontalSpace = false;
					gd.grabExcessVerticalSpace = true;
					gd.horizontalSpan = 3;

					equalReferencesTable = new TableViewer(contents, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI
							| SWT.FULL_SELECTION);
					equalReferencesTable.getTable().setLayoutData(gd);
					equalReferencesTable.setContentProvider(new ArrayContentProvider());
					equalReferencesTable.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

					// select those references which already resolve to the same list ;-)
					for (IElementReference ref : equalRefs) {
						final IndepChange otherChange = MPatchUtil.getChangeFor(ref);
						if (otherChange == null || !mapping.getResolutionByChange().containsKey(otherChange))
							continue;
						final List<EObject> otherList = mapping.getResolutionByChange().get(otherChange).get(ref);
						if (otherList == null)
							continue;
						if (otherList.size() == currentList.size() && otherList.containsAll(currentList))
							references.add(ref);
					}

					// set input and selection
					equalReferencesTable.setInput(equalRefs);
					equalReferencesTable.setSelection(new StructuredSelection(references));
				}
			}

			private void addInfoLabel(Composite contents) {
				String info = "This " + MPatchConstants.SYMBOLIC_REFERENCE_NAME + " ";
				if (currentlySelectedElement.eContainmentFeature() != null) {
					info += "(" + change.getChangeKind() + "."
							+ currentlySelectedElement.eContainmentFeature().getName() + ") ";
				}
				info += "must resolve " + bounds + ".";
				final Label infoLabel = new Label(contents, SWT.NONE);
				infoLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING,
						GridData.VERTICAL_ALIGN_BEGINNING, true, false, 3, 1));
				infoLabel.setText(info);
			}
		}
	}

	/**
	 * Simple wrapper class for symbolic reference refinements.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private class SymrefRefinement {
		final List<EObject> elements;
		final List<IElementReference> references;

		public SymrefRefinement(List<EObject> refinement, List<IElementReference> references) {
			this.references = references;
			this.elements = refinement;
		}
	}

	/**
	 * The cell modifier for symbolic reference refinements.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private class SymbolicReferenceCellModifier implements ICellModifier {

		@Override
		public boolean canModify(Object element, String property) {
			if (!(element instanceof IElementReference) || element instanceof ModelDescriptorReference)
				return false; // only regular symbolic references
			if (!property.equals(COLUMNS[2]))
				return false; // only second column
			if (currentlySelectedElement == null)
				return false; // an lement must be selected

			final IElementReference ref = (IElementReference) element;
			final IndepChange change = MPatchUtil.getChangeFor(ref);
			if (change != null && !mapping.getResolutionByChange().containsKey(change))
				return false;
			return true;
		}

		@Override
		public Object getValue(Object element, String property) {
			if (element instanceof IElementReference && property.equals(COLUMNS[2])) {
				// give some useful information when the user clicks the field
				final IElementReference ref = (IElementReference) element;
				final IndepChange change = MPatchUtil.getChangeFor(ref);
				if (change != null) {
					if (mapping.getResolutionByChange().containsKey(change)) {
						final int size = mapping.getResolutionByChange().get(change).get(ref).size();
						return size + " resolved; " + getCount(ref.getLowerBound(), ref.getUpperBound()) + " required";
					}
				}
			}
			return "click to edit"; // default string
		}

		@Override
		public void modify(Object element, String property, Object value) {
			if (property.equals(COLUMNS[2]) && element instanceof TreeItem && value != null
					&& value instanceof SymrefRefinement) {
				final TreeItem item = (TreeItem) element;
				if (item.getData() instanceof IElementReference) {
					final IElementReference ref = (IElementReference) item.getData();
					final SymrefRefinement symrefRefinement = (SymrefRefinement) value;

					// update!
					symrefRefinement.references.add(ref);
					updateElements(symrefRefinement);
				}
			}
		}
	}
}
