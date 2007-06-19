/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.contentprovider;

import java.io.IOException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.generic.DiffMaker;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Structure viewer used by the
 * {@link org.eclipse.emf.compare.ui.structuremergeviewer.ModelStructureMergeViewer ModelStructureMergeViewer}.
 * Assumes that its input is a {@link DiffModel}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ModelStructureContentProvider implements ITreeContentProvider {
	private CompareConfiguration configuration;

	private ModelInputSnapshot snapshot;

	private DiffModel diffInput;

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
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parentElement) {
		Object[] children = null;
		if (parentElement instanceof EObject) {
			children = ((EObject)parentElement).eContents().toArray();
		}
		return children;
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
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		Object[] elements = null;
		if (inputElement instanceof DiffModel) {
			elements = ((DiffModel)inputElement).getOwnedElements().toArray();
		} else {
			elements = diffInput.getOwnedElements().toArray();
		}
		return elements;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Nothing needs to be disposed of here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		((TreeViewer)viewer).getTree().clearAll(true);
		if (newInput instanceof ICompareInput) {
			final ITypedElement left = ((ICompareInput)newInput).getLeft();
			final ITypedElement right = ((ICompareInput)newInput).getRight();

			EObject leftModel = null;
			EObject rightModel = null;
			try {
				if (left instanceof ResourceNode) {
					leftModel = ModelUtils.load(((ResourceNode)left).getResource().getFullPath());
				} else if (left instanceof IStreamContentAccessor) {
					leftModel = ModelUtils.load(((IStreamContentAccessor)left).getContents(), left.getName());
					configuration.setLeftEditable(false);
				}
				if (right instanceof ResourceNode) {
					rightModel = ModelUtils.load(((ResourceNode)right).getResource().getFullPath());
				} else if (right instanceof IStreamContentAccessor) {
					rightModel = ModelUtils.load(((IStreamContentAccessor)right).getContents(), right
							.getName());
					configuration.setRightEditable(false);
				}
			} catch (IOException e) {
				EMFComparePlugin.getDefault().log(e.getMessage(), true);
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e.getMessage(), true);
			}

			if (leftModel != null && rightModel != null) {
				try {
					final MatchModel match = new MatchService().doMatch(rightModel, leftModel,
							new NullProgressMonitor());
					final DiffModel diff = new DiffMaker().doDiff(match);

					snapshot = DiffFactory.eINSTANCE.createModelInputSnapshot();
					snapshot.setDiff(diff);
					snapshot.setMatch(match);

					diffInput = diff;
				} catch (InterruptedException e) {
					EMFComparePlugin.getDefault().log(e.getMessage(), true);
				}
			}
		} else if (newInput instanceof ModelInputSnapshot) {
			snapshot = (ModelInputSnapshot)newInput;
			diffInput = snapshot.getDiff();
		}
	}

	/**
	 * Returns this content provider's input.
	 * 
	 * @return This content provider's input.
	 */
	public ModelInputSnapshot getSnapshot() {
		return snapshot;
	}
}
