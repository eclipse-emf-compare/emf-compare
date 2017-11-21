/*******************************************************************************
 * Copyright (c) 2017, 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback;

import org.eclipse.compare.internal.MergeViewerContentProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
@SuppressWarnings("restriction")
final class TextFallbackMergeViewerContentProvider extends MergeViewerContentProvider {

	private TextFallbackMergeViewer textFallbackMergeViewer;

	/**
	 * Creates a provider for the text fallback merge viewer.
	 * 
	 * @param textFallbackMergeViewer
	 *            the text fallback merge viewer.
	 */
	public TextFallbackMergeViewerContentProvider(TextFallbackMergeViewer textFallbackMergeViewer) {
		super(textFallbackMergeViewer.getCompareConfiguration());
		this.textFallbackMergeViewer = textFallbackMergeViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#isLeftEditable(java.lang.Object)
	 */
	@Override
	public boolean isLeftEditable(Object element) {
		return super.isLeftEditable(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#isRightEditable(java.lang.Object)
	 */
	@Override
	public boolean isRightEditable(Object element) {
		return super.isRightEditable(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#saveLeftContent(java.lang.Object, byte[])
	 */
	@Override
	public void saveLeftContent(Object element, byte[] bytes) {
		super.saveLeftContent(getEffectiveElement(element), bytes);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#saveRightContent(java.lang.Object, byte[])
	 */
	@Override
	public void saveRightContent(Object element, byte[] bytes) {
		super.saveRightContent(getEffectiveElement(element), bytes);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getAncestorLabel(java.lang.Object)
	 */
	@Override
	public String getAncestorLabel(Object element) {
		return super.getAncestorLabel(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getAncestorImage(java.lang.Object)
	 */
	@Override
	public Image getAncestorImage(Object element) {
		return super.getAncestorImage(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getAncestorContent(java.lang.Object)
	 */
	@Override
	public Object getAncestorContent(Object element) {
		return super.getAncestorContent(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getLeftLabel(java.lang.Object)
	 */
	@Override
	public String getLeftLabel(Object element) {
		return super.getLeftLabel(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getLeftImage(java.lang.Object)
	 */
	@Override
	public Image getLeftImage(Object element) {
		return super.getLeftImage(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getLeftContent(java.lang.Object)
	 */
	@Override
	public Object getLeftContent(Object element) {
		return super.getLeftContent(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getRightLabel(java.lang.Object)
	 */
	@Override
	public String getRightLabel(Object element) {
		return super.getRightLabel(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getRightImage(java.lang.Object)
	 */
	@Override
	public Image getRightImage(Object element) {
		return super.getRightImage(getEffectiveElement(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getRightContent(java.lang.Object)
	 */
	@Override
	public Object getRightContent(Object element) {
		return super.getRightContent(getEffectiveElement(element));
	}

	private Object getEffectiveElement(Object element) {
		if (element == textFallbackMergeViewer.getOriginalInput()) {
			return textFallbackMergeViewer.getEffectiveInput();
		} else {
			return element;
		}
	}
}
