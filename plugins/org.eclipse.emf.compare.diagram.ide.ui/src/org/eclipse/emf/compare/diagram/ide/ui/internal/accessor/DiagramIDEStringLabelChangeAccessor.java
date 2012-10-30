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
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import java.io.InputStream;

import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.ContentMergeViewerConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramIDEStringLabelChangeAccessor implements ITypedElement, IStreamContentAccessor, IEditableContent {

	/**
	 * @param eObject
	 * @param eAtribute
	 * @param attributeChange
	 */
	public DiagramIDEStringLabelChangeAccessor(EObject eObject, LabelChange attributeChange) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return ExtendedImageRegistry.getInstance().getImage(
				EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ContentMergeViewerConstants.TEXT_DIFF_NODE_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		// TODO
		return null;
		// String stringValue = null;
		// if (stringValue != null) {
		// return new ByteArrayInputStream(stringValue.getBytes());
		// } else {
		// return new ByteArrayInputStream(new byte[0]);
		// }
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IEditableContent#isEditable()
	 */
	public boolean isEditable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IEditableContent#setContent(byte[])
	 */
	public void setContent(byte[] newContent) {
		throw new UnsupportedOperationException(
				"ITypedElement StringAttributeChangeAccessor#replace(ITypedElement, ITypedElement)"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IEditableContent#replace(org.eclipse.compare.ITypedElement,
	 *      org.eclipse.compare.ITypedElement)
	 */
	public ITypedElement replace(ITypedElement dest, ITypedElement src) {
		throw new UnsupportedOperationException(
				"ITypedElement StringAttributeChangeAccessor#replace(ITypedElement, ITypedElement)"); //$NON-NLS-1$
	}

}
