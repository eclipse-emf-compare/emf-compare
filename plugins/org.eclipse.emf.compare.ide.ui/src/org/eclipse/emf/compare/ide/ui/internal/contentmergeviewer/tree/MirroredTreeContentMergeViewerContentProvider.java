/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * Mirrored implementation of {@link TreeContentMergeViewerContentProvider} that swaps the left and right
 * side.
 *
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class MirroredTreeContentMergeViewerContentProvider extends TreeContentMergeViewerContentProvider {

	private TreeContentMergeViewerContentProvider delegate;

	public MirroredTreeContentMergeViewerContentProvider(EMFCompareConfiguration cc,
			TreeContentMergeViewerContentProvider delegate) {
		super(cc);
		this.delegate = delegate;
	}

	@Override
	public String getLeftLabel(Object element) {
		return delegate.getRightLabel(element);
	}

	@Override
	public Image getLeftImage(Object element) {
		return delegate.getRightImage(element);
	}

	@Override
	public Object getLeftContent(Object element) {
		return delegate.getRightContent(element);
	}

	@Override
	public boolean isLeftEditable(Object element) {
		return delegate.isRightEditable(element);
	}

	@Override
	public void saveLeftContent(Object element, byte[] bytes) {
		// The EMFCompareStructurededMergeViewer already "unswaps" the sides before saving-> keep sides
		delegate.saveLeftContent(element, bytes);
	}

	@Override
	public String getRightLabel(Object element) {
		return delegate.getLeftLabel(element);
	}

	@Override
	public Image getRightImage(Object element) {
		return delegate.getLeftImage(element);
	}

	@Override
	public Object getRightContent(Object element) {
		return delegate.getLeftContent(element);
	}

	@Override
	public boolean isRightEditable(Object element) {
		return delegate.isLeftEditable(element);
	}

	@Override
	public void saveRightContent(Object element, byte[] bytes) {
		// The EMFCompareStructurededMergeViewer already "unswaps" the sides before saving-> keep sides
		delegate.saveRightContent(element, bytes);
	}

	@Override
	public void inputChanged(Viewer v, Object o1, Object o2) {
		delegate.inputChanged(v, o1, o2);
	}

	@Override
	public String getAncestorLabel(Object element) {
		return delegate.getAncestorLabel(element);
	}

	@Override
	public Image getAncestorImage(Object element) {
		return delegate.getAncestorImage(element);
	}

	@Override
	public Object getAncestorContent(Object element) {
		return delegate.getAncestorContent(element);
	}

	@Override
	public boolean showAncestor(Object element) {
		return delegate.showAncestor(element);
	}

}
