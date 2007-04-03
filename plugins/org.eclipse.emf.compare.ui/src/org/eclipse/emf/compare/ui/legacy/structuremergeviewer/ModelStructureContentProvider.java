/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.structuremergeviewer;

import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.ui.legacy.ModelCompareInput;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelStructureContentProvider implements ITreeContentProvider {

	private static final String ROOT = "Differences";

	private ModelCompareInput deltaInput;

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object parentElement) {
		if (parentElement.equals(ROOT)) {
			return (this.deltaInput).getDiff().eContents()
					.toArray();
			// List<DiffElement> Match2Elementss = new ArrayList<DiffElement>();
			// while (tree.hasNext())
			// {
			// Match2Elementss.add((DiffElement) tree.next());
			// }
			// return Match2Elementss.toArray();
		}
		if (parentElement instanceof Match2Elements) {
			// TODOCBR FIX getChildren
			// return ((Match2Elements) parentElement).getDiffs().toArray();
		}
		if (parentElement instanceof DiffElement) {

			// TODOCBR fix getParentElement
			// return ((DiffElement) parentElement).getDiffs() != null ?
			// ((DiffElement) parentElement).getDiffs().toArray() : null;
		}
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(final Object element) {
		if (element.equals(ROOT)) {
			return null;
		}
		if (element instanceof Match2Elements) {
			return ROOT;
		}
		if (element instanceof DiffElement) {
			return ((DiffElement) element).eContainer();
		}
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(final Object element) {
		if (element.equals(ROOT)) {
			return true;
		}
		if (element instanceof Match2Elements) {
			return !((Match2Elements) element).eContents().isEmpty();
		}
		if (element instanceof DiffElement) {
			return false;
		}
		// FIXMECBR
		// return (((DiffElement) element).getDiffs() != null &&
		// (!((DiffElement) element).getDiffs().isEmpty()));
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(final Object inputElement) {
		final String[] root = new String[1];
		root[0] = ROOT;
		return root;
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
		assert (newInput instanceof ModelCompareInput);
		assert (oldInput instanceof ModelCompareInput);
		((TreeViewer) viewer).getTree().clearAll(true);
		this.deltaInput = (ModelCompareInput) newInput;
	}

}
