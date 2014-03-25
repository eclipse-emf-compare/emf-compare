/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.saveAllResources;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.saveResource;

import com.google.common.collect.ImmutableMap;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.AccessorAdapter;
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
public class TreeContentMergeViewerContentProvider implements IMergeViewerContentProvider {

	/**
	 * The stored {@link CompareConfiguration} to delegates most methods of this object.
	 */
	private final EMFCompareConfiguration fCompareConfiguration;

	/**
	 * Creates a new {@link TreeContentMergeViewerContentProvider} and stored the given
	 * {@link CompareConfiguration}.
	 * 
	 * @param cc
	 *            the {@link CompareConfiguration} that will be used to get label and image of left, right and
	 *            ancestor.
	 * @param comparison
	 *            the comparison that is to be displayed by this viewer.
	 */
	public TreeContentMergeViewerContentProvider(EMFCompareConfiguration cc) {
		this.fCompareConfiguration = cc;
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

	public Object getAncestorContent(Object element) {
		if (element instanceof ICompareInput) {
			ITypedElement ancestor = ((ICompareInput)element).getAncestor();
			if (ancestor instanceof AccessorAdapter) {
				return ((AccessorAdapter)ancestor).getTarget();
			}
			return ancestor;
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

	public Object getLeftContent(Object element) {
		if (element instanceof ICompareInput) {
			ITypedElement left = ((ICompareInput)element).getLeft();
			if (left instanceof AccessorAdapter) {
				return ((AccessorAdapter)left).getTarget();
			}
			return left;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#isLeftEditable(java.lang.Object)
	 */
	public boolean isLeftEditable(Object element) {
		return fCompareConfiguration.isLeftEditable();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#saveLeftContent(java.lang.Object,
	 *      byte[])
	 */
	public void saveLeftContent(Object element, byte[] bytes) {
		EList<Match> matches = fCompareConfiguration.getComparison().getMatches();
		EObject leftEObject = null;
		for (Match match : matches) {
			leftEObject = match.getLeft();
			if (leftEObject != null) {
				break;
			}
		}
		if (leftEObject != null) {
			Resource eResource = leftEObject.eResource();
			if (eResource != null) {
				ResourceSet resourceSet = eResource.getResourceSet();
				if (resourceSet != null) {
					saveAllResources(resourceSet, ImmutableMap.of(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
							Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
				} else {
					saveResource(eResource, ImmutableMap.of(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
							Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
				}
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

	public Object getRightContent(Object element) {
		if (element instanceof ICompareInput) {
			ITypedElement right = ((ICompareInput)element).getRight();
			if (right instanceof AccessorAdapter) {
				return ((AccessorAdapter)right).getTarget();
			}
			return right;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#isRightEditable(java.lang.Object)
	 */
	public boolean isRightEditable(Object element) {
		return fCompareConfiguration.isRightEditable();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#saveRightContent(java.lang.Object,
	 *      byte[])
	 */
	public void saveRightContent(Object element, byte[] bytes) {
		EList<Match> matches = fCompareConfiguration.getComparison().getMatches();
		EObject rightEObject = null;
		for (Match match : matches) {
			rightEObject = match.getRight();
			if (rightEObject != null) {
				break;
			}
		}
		if (rightEObject != null) {
			Resource eResource = rightEObject.eResource();
			if (eResource != null) {
				ResourceSet resourceSet = eResource.getResourceSet();
				if (resourceSet != null) {
					saveAllResources(resourceSet, ImmutableMap.of(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
							Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
				} else {
					saveResource(eResource, ImmutableMap.of(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
							Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
				}
			}
		}
	}
}
