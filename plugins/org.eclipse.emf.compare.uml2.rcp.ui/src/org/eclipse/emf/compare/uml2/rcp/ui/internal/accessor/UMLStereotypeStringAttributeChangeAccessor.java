/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.IStreamContentAccessor;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.TypeConstants;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Case of stereotype string attribute changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeStringAttributeChangeAccessor implements ITypedElement, IStreamContentAccessor {

	/** The EObject to get the value of the EAttribute from. */
	protected final EObject fEObject;

	/** The EAttribute to retrieve from the wrapped EObject. */
	protected final EAttribute fAttribute;

	/** The comparison object. */
	protected final Comparison fComparison;

	/**
	 * Constructor.
	 * 
	 * @param eObject
	 *            the EObject to get the value of the EAttribute from.
	 * @param propertyChange
	 *            the {@link StereotypeAttributeChange} concerned by the accessor.
	 */
	public UMLStereotypeStringAttributeChangeAccessor(EObject eObject,
			StereotypeAttributeChange propertyChange) {
		this.fEObject = eObject;
		this.fAttribute = (EAttribute)propertyChange.getDiscriminant();
		this.fComparison = propertyChange.getMatch().getComparison();
	}

	/**
	 * Returns the comparison object.
	 * 
	 * @return the fComparison
	 */
	public Comparison getComparison() {
		return fComparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		Object value = ReferenceUtil.safeEGet(getEObject(), getEAtribute());
		String stringValue = EcoreUtil.convertToString(getEAtribute().getEAttributeType(), value);
		// Assume that the platform locale is appropriate.
		if (stringValue != null) {
			return new ByteArrayInputStream(stringValue.getBytes());
		} else {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	/**
	 * Returns the EObject to get the value of the EAttribute from.
	 * 
	 * @return the fEObject
	 */
	protected final EObject getEObject() {
		return fEObject;
	}

	/**
	 * Returns the EAttribute to retrieve from the wrapped EObject.
	 * 
	 * @return the fEAttribute
	 */
	protected final EAttribute getEAtribute() {
		return fAttribute;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.compare.ITypedElement#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return ExtendedImageRegistry.getInstance()
				.getImage(EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.compare.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE_ETEXT_DIFF;
	}
}
