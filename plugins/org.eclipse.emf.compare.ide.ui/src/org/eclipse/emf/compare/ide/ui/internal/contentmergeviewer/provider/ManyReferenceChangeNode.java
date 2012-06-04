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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ManyReferenceChangeNode implements ITypedElement, IManyStructuralFeatureAccessor<EObject> {

	/**
	 * The EObject to get the value of the EReference from.
	 */
	private final EObject fEObject;

	/**
	 * The EReference to retrieve from the wrapped EObject.
	 */
	private final EReference fEReference;

	private final EObject fValue;

	/**
	 * 
	 */
	public ManyReferenceChangeNode(EObject eObject, EReference eReference, EObject value) {
		fEObject = eObject;
		fEReference = eReference;
		fValue = value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return ManyReferenceChangeNode.class.getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ContentMergeViewerConstants.REFERENCE_CHANGE_NODE_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor#getValues()
	 */
	@SuppressWarnings("unchecked")
	public List<EObject> getValues() {
		return (List<EObject>)fEObject.eGet(fEReference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor#getValue()
	 */
	public EObject getValue() {
		return fValue;
	}

}
