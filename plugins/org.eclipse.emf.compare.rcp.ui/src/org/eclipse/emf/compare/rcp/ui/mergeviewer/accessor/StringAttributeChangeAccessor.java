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
package org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A {@link ITypedElement} that can be used as input of TextMergeViewer. The returned content is the value of
 * the given {@link EAttribute} on the given {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class StringAttributeChangeAccessor {

	/**
	 * The EObject to get the value of the EAttribute from.
	 */
	private final EObject fEObject;

	/**
	 * The EAttribute to retrieve from the wrapped EObject.
	 */
	private final EAttribute fEAttribute;

	private final Comparison fComparison;

	/**
	 * Creates a new object for the given <code>eObject</code> and <code>eAttribute</code>.
	 * 
	 * @param eObject
	 *            The EObject to get the value of the EAttribute from
	 * @param eAttribute
	 *            The EAttribute to retrieve from the wrapped EObject
	 */
	public StringAttributeChangeAccessor(EObject eObject, EAttribute eAttribute,
			AttributeChange attributeChange) {
		this.fEObject = eObject;
		this.fEAttribute = eAttribute;
		this.fComparison = attributeChange.getMatch().getComparison();
	}

	/**
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
		Object value = getEObject().eGet(getEAtribute());
		String stringValue = EcoreUtil.convertToString(getEAtribute().getEAttributeType(), value);
		// Assume that the platform locale is appropriate.
		if (stringValue != null) {
			return new ByteArrayInputStream(stringValue.getBytes());
		} else {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	/**
	 * @return the fEObject
	 */
	protected final EObject getEObject() {
		return fEObject;
	}

	/**
	 * @return the fEAttribute
	 */
	protected final EAttribute getEAtribute() {
		return fEAttribute;
	}

}
