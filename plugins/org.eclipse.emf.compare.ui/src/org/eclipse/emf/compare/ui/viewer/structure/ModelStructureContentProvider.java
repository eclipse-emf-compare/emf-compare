/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.util.DiffAdapterFactory;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.internal.ModelComparator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Structure viewer used by the {@link org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer
 * ModelStructureMergeViewer}. Assumes that its input is a {@link DiffModel}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelStructureContentProvider implements ITreeContentProvider {
	/**
	 * {@link CompareConfiguration} controls various aspect of the GUI elements. This will keep track of the
	 * one used to created this compare editor.
	 */
	private final CompareConfiguration configuration;

	/**
	 * Result of the underlying comparison. This can be either a {@link DiffModel} or {@link DiffResourceSet}.
	 */
	private Object input;

	/**
	 * Instantiates a content provider given the {@link CompareConfiguration} to use.
	 * 
	 * @param compareConfiguration
	 *            {@link CompareConfiguration} used for this comparison.
	 */
	public ModelStructureContentProvider(CompareConfiguration compareConfiguration) {
		configuration = compareConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		ModelComparator.removeComparator(configuration);
		input = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parentElement) {
		Object[] children = null;
		if (parentElement instanceof EObject) {
			final Collection<EObject> childrenList = new ArrayList<EObject>();
			for (final EObject child : ((EObject)parentElement).eContents()) {
				if (!DiffAdapterFactory.shouldBeHidden(child)) {
					childrenList.add(child);
				}
			}
			children = childrenList.toArray();
		}
		return children;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		Object[] elements = null;
		if (inputElement instanceof DiffModel) {
			elements = ((DiffModel)inputElement).getOwnedElements().toArray();
		} else if (input instanceof DiffModel) {
			elements = ((DiffModel)input).getOwnedElements().toArray();
		} else if (input instanceof DiffResourceSet) {
			final List<Object> elementList = new ArrayList<Object>(((DiffResourceSet)input).getDiffModels());
			elementList.addAll(((DiffResourceSet)input).getResourceDiffs());
			if (elementList.isEmpty()) {
				/*
				 * binary identical resources. Create a dummy diff to provide the user with feedback that 0
				 * differences were found.
				 */
				final DiffModel dummyDiff = DiffFactory.eINSTANCE.createDiffModel();
				dummyDiff.getOwnedElements().add(DiffFactory.eINSTANCE.createDiffGroup());
				elementList.add(dummyDiff);
			}
			elements = elementList.toArray();
		} else {
			elements = new Object[0];
		}

		return elements;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object element) {
		Object parent = null;
		if (element instanceof EObject) {
			parent = ((EObject)element).eContainer();
		}
		return parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object element) {
		boolean hasChildren = false;
		if (element instanceof EObject) {
			hasChildren = !((EObject)element).eContents().isEmpty();
		}
		return hasChildren;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		((TreeViewer)viewer).getTree().clearAll(true);
		if (newInput == null)
			return;
		final ModelComparator comparator;
		if (newInput instanceof ICompareInput) {
			comparator = ModelComparator.getComparator(configuration, (ICompareInput)newInput);
		} else {
			comparator = ModelComparator.getComparator(configuration);
		}
		if (newInput instanceof ComparisonResourceSnapshot) {
			input = ((ComparisonResourceSnapshot)newInput).getDiff();
		} else if (newInput instanceof ComparisonResourceSetSnapshot) {
			input = ((ComparisonResourceSetSnapshot)newInput).getDiffResourceSet();
		} else if (comparator.getComparisonResult() != null) {
			input = comparator.getComparisonResult().getDiffResourceSet();
		} else if (newInput instanceof ModelCompareInput) {
			input = ((ModelCompareInput)newInput).getDiff();
		} else if (oldInput != newInput && newInput instanceof ICompareInput) {
			comparator.loadResources((ICompareInput)newInput);
			input = comparator.compare(configuration).getDiffResourceSet();
		}
	}
}
