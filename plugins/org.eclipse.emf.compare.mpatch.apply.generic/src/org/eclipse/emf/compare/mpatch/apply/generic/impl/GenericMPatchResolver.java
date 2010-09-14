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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.apply.generic.GenericApplyActivator;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolutionHost;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
	private EObject currentlySelectedElement;

	/** The table tree viewer for presenting the resolved references to the user. */
	private CheckboxTreeViewer viewer;

	/** The adapter factory for the providers. */
	private AdapterFactory adapterFactory;
	
	/** A default adapter factory label provider. */
	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	/** The element selection dialog cell editor. */
	private ElementSelectionDialogCellEditor elementSelectionDialogCellEditor;

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
		
		// update validation states because we need them already for displaying them in the viewer!
		MPatchValidator.validateResolutions(mapping);
		
		deactivatedChanges = new HashMap<IndepChange, Map<IElementReference, List<EObject>>>();
		elementSelectionDialogCellEditor.reset(mapping);
		viewer.setContentProvider(new ReferenceResolutionContentProvider(mapping, adapterFactory));
		viewer.setLabelProvider(new ReferenceResolutionLabelProvider(mapping, adapterFactory));
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
		this.adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(adapterFactory);

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
		elementsColumn.setText("Resolution details and refinement");
		elementsColumn.setResizable(true);
		elementsColumn.setWidth(200);

		// add cell editor for new selection of emf model elements
		final CellEditor[] editors = new CellEditor[3];
		elementSelectionDialogCellEditor = new ElementSelectionDialogCellEditor(this, viewer.getTree(), adapterFactory, adapterFactoryLabelProvider, mapping);
		editors[0] = elementSelectionDialogCellEditor;
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
			} else if (selection.size() == 1 && selection.getFirstElement() instanceof IndepAddRemElementChange) {
				currentlySelectedElement = (IndepAddRemElementChange) selection.getFirstElement();
			}
		}
	}
	
	/** Return the currently selected element. */
	EObject getCurrentlySelectedElement() {
		return currentlySelectedElement;
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

	private void updateElements(IndepAddRemElementChange change, List<?> value) {
		@SuppressWarnings("unchecked")
		final List<EObject> boundElements = (List<EObject>) value;
		final IElementReference selfReference = change.getSubModelReference();
		mapping.getResolutionByChange().get(change).put(selfReference, boundElements);
		host.resolved(mapping);
		viewer.refresh();
	}

	static String getCount(int lowerBound, int upperBound) {
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
				GenericApplyActivator.getDefault().logWarning(
						"A change was active and inactive at the same time: " + change);
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
	 * Simple wrapper class for symbolic reference refinements.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	class SymrefRefinement {
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

		/**
		 * Two cases are modifyable:
		 * <ul>
		 * <li> {@link IndepAddElementChange} might be bound to existing elements.
		 * <li>Any {@link IElementReference} might be refined (under certain conditions).
		 * </ul>
		 */
		@Override
		public boolean canModify(Object element, String property) {
			if (element instanceof IndepAddElementChange && COLUMNS[2].equals(property))
				return true; // existing elements can be bound to this change!
			
			if (!(element instanceof IElementReference) || element instanceof ModelDescriptorReference)
				return false; // only regular symbolic references
			if (!COLUMNS[2].equals(property))
				return false; // only second column
			if (currentlySelectedElement == null)
				return false; // an element must be selected

			final IElementReference ref = (IElementReference) element;
			final IndepChange change = MPatchUtil.getChangeFor(ref);
			if (change != null && !mapping.getResolutionByChange().containsKey(change))
				return false;
			return true;
		}

		@Override
		public Object getValue(Object element, String property) {
			if (element instanceof IElementReference && COLUMNS[2].equals(property)) {
				// give some useful information when the user clicks the field
				final IElementReference ref = (IElementReference) element;
				final IndepChange change = MPatchUtil.getChangeFor(ref);
				if (change != null) {
					if (mapping.getResolutionByChange().containsKey(change)) {
						final int size = mapping.getResolutionByChange().get(change).get(ref).size();
						return size + " resolved; " + getCount(ref.getLowerBound(), ref.getUpperBound()) + " required";
					}
				}
			} else if (element instanceof IndepAddElementChange && COLUMNS[2].equals(property)) {
				final IndepAddElementChange change = (IndepAddElementChange) element;
				final IElementReference selfRef = change.getSubModelReference();
				final List<EObject> elements = mapping.getResolutionByChange().get(change).get(selfRef);
				if (elements != null && elements.size() == 1) {
					return "Bound to: " + adapterFactoryLabelProvider.getText(elements.get(0));
				} else if (elements != null && elements.size() > 1) {
					return "Bound to " + elements.size() + " elements.";
				} else {
					return "Create new.";
				}
			}

			return "click to edit"; // default string
		}

		@Override
		public void modify(Object element, String property, Object value) {
			if (COLUMNS[2].equals(property) && element instanceof TreeItem && value != null
					&& value instanceof SymrefRefinement) {
				final TreeItem item = (TreeItem) element;
				if (item.getData() instanceof IElementReference) {
					final IElementReference ref = (IElementReference) item.getData();
					final SymrefRefinement symrefRefinement = (SymrefRefinement) value;

					// update!
					symrefRefinement.references.add(ref);
					updateElements(symrefRefinement);
				}
			} else if (COLUMNS[2].equals(property) && element instanceof TreeItem && value != null
					&& value instanceof List) {
				final TreeItem item = (TreeItem) element;
				if (item.getData() instanceof IndepAddElementChange) {
					final IndepAddElementChange change = (IndepAddElementChange) item.getData();
					updateElements(change, (List<?>) value);
				}
			}
		}
	}
}
