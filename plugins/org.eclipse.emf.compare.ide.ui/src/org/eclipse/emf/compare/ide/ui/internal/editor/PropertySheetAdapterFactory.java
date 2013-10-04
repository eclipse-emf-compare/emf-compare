/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.editor;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * Specific Adapter factory that provides an {@link ExtendedPropertySheetPage} for the Compare Editor.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class PropertySheetAdapterFactory implements IAdapterFactory {

	/** The extended property sheet page provided by this adapter factory. */
	private ExtendedPropertySheetPage propertySheetPage;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(Object, Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySheetPage.class) {
			if (propertySheetPage == null) {
				if (adaptableObject instanceof CompareEditor) {
					IEditorInput editorInput = ((CompareEditor)adaptableObject).getEditorInput();
					if (editorInput instanceof CompareEditorInput) {
						propertySheetPage = new ExtendedPropertySheetPage(null);
					}
				}
			}
			return propertySheetPage;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IPropertySheetPage.class };
	}

}
