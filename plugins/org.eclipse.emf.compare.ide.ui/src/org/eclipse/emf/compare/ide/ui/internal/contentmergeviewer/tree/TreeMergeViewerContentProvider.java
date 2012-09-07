/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * An {@link IMergeViewerContentProvider} that delegates <code>getXXXImage(Object)</code> and
 * <code>getXXXText()</code> to a {@link CompareConfiguration}.
 * <p>
 * <code>getXXXContent()</code> is computed by getting the side form the given object or from its parent if
 * null, recursively.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeMergeViewerContentProvider implements IMergeViewerContentProvider {

	/**
	 * The stored {@link CompareConfiguration} to delegates most methods of this object.
	 */
	private final CompareConfiguration fCompareConfiguration;

	/** The comparison currently being displayed. */
	private final Comparison fComparison;

	/**
	 * Creates a new {@link TreeMergeViewerContentProvider} and stored the given {@link CompareConfiguration}.
	 * 
	 * @param cc
	 *            the {@link CompareConfiguration} that will be used to get label and image of left, right and
	 *            ancestor.
	 * @param comparison
	 *            the comparison that is to be displayed by this viewer.
	 */
	public TreeMergeViewerContentProvider(CompareConfiguration cc, Comparison comparison) {
		this.fCompareConfiguration = cc;
		this.fComparison = comparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// empty default implementation
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer v, Object o1, Object o2) {
		// we are not interested in since we have no state
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getAncestorLabel(java.lang.Object)
	 */
	public String getAncestorLabel(Object element) {
		return fCompareConfiguration.getAncestorLabel(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getAncestorImage(java.lang.Object)
	 */
	public Image getAncestorImage(Object element) {
		return fCompareConfiguration.getAncestorImage(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getAncestorContent(java.lang.Object)
	 */
	public Object getAncestorContent(Object element) {
		if (element instanceof ICompareInput) {
			ICompareInput compareInput = (ICompareInput)element;
			Object ret = compareInput.getAncestor();
			// if no ancestor and element is a diff, try to reach the ancestor of parent, recursively
			if (ret == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)compareInput).getParent();
				ret = getAncestorContent(parent);
			}
			return ret;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#showAncestor(java.lang.Object)
	 */
	public boolean showAncestor(Object element) {
		if (element instanceof ICompareInput) {
			return true; // fix for #45239: Show ancestor for incoming and outgoing changes
		}
		// return (((ICompareInput)element).getKind() & Differencer.DIRECTION_MASK) ==
		// Differencer.CONFLICTING;
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getLeftLabel(java.lang.Object)
	 */
	public String getLeftLabel(Object element) {
		return fCompareConfiguration.getLeftLabel(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getLeftImage(java.lang.Object)
	 */
	public Image getLeftImage(Object element) {
		return fCompareConfiguration.getLeftImage(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getLeftContent(java.lang.Object)
	 */
	public Object getLeftContent(Object element) {
		if (element instanceof ICompareInput) {
			ICompareInput compareInput = (ICompareInput)element;
			Object ret = compareInput.getLeft();
			// if no left and element is a diff, try to reach the left of parent, recursively
			if (ret == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)compareInput).getParent();
				ret = getLeftContent(parent);
			}
			return ret;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#isLeftEditable(java.lang.Object)
	 */
	public boolean isLeftEditable(Object element) {
		boolean ret = false;
		Object left = getLeftContent(element);
		if (left instanceof IEditableContent) {
			ret = ((IEditableContent)left).isEditable();
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#saveLeftContent(java.lang.Object,
	 *      byte[])
	 */
	public void saveLeftContent(Object element, byte[] bytes) {
		EList<Match> matches = fComparison.getMatches();
		EObject leftEObject = null;
		for (Match match : matches) {
			leftEObject = match.getLeft();
			if (leftEObject != null) {
				break;
			}
		}
		if (leftEObject != null) {
			Resource eResource = leftEObject.eResource();
			ResourceSet resourceSet = eResource.getResourceSet();
			saveAllResources(resourceSet);
		}
	}

	private void saveAllResources(ResourceSet resourceSet) {
		EList<Resource> resources = resourceSet.getResources();
		for (Resource resource : resources) {
			try {
				resource.save(ImmutableMap.of());
			} catch (IOException e) {
				EMFCompareIDEUIPlugin.getDefault().log(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getRightLabel(java.lang.Object)
	 */
	public String getRightLabel(Object element) {
		return fCompareConfiguration.getRightLabel(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getRightImage(java.lang.Object)
	 */
	public Image getRightImage(Object element) {
		return fCompareConfiguration.getRightImage(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getRightContent(java.lang.Object)
	 */
	public Object getRightContent(Object element) {
		if (element instanceof ICompareInput) {
			ICompareInput compareInput = (ICompareInput)element;
			Object ret = compareInput.getRight();
			// if no right and element is a diff, try to reach the right of parent, recursively
			if (ret == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)compareInput).getParent();
				ret = getRightContent(parent);
			}
			return ret;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#isRightEditable(java.lang.Object)
	 */
	public boolean isRightEditable(Object element) {
		if (element instanceof ICompareInput) {
			Object right = ((ICompareInput)element).getRight();
			if (right == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)element).getParent();
				if (parent instanceof ICompareInput) {
					right = ((ICompareInput)parent).getRight();
				}
			}
			if (right instanceof IEditableContent) {
				return ((IEditableContent)right).isEditable();
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#saveRightContent(java.lang.Object,
	 *      byte[])
	 */
	public void saveRightContent(Object element, byte[] bytes) {
		EList<Match> matches = fComparison.getMatches();
		EObject rightEObject = null;
		for (Match match : matches) {
			rightEObject = match.getRight();
			if (rightEObject != null) {
				break;
			}
		}
		if (rightEObject != null) {
			Resource eResource = rightEObject.eResource();
			ResourceSet resourceSet = eResource.getResourceSet();
			saveAllResources(resourceSet);
		}
	}

}
