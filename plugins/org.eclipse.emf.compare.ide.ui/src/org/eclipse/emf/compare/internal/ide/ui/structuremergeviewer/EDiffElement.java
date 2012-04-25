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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class EDiffElement extends AdapterImpl implements IDiffElement {

	private final AdapterFactory fAdapterFactory;

	/**
	 * @param adapterFactory
	 */
	public EDiffElement(AdapterFactory adapterFactory) {
		fAdapterFactory = adapterFactory;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == fAdapterFactory;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return null;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		if (getTarget() instanceof AttributeChange) {
			EAttribute attribute = ((AttributeChange)getTarget()).getAttribute();
			if (attribute.getEType().getInstanceClass().equals(String.class)) {
				return ITypedElement.TEXT_TYPE;
			}
		}
		return "ecore";
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffElement#getKind()
	 */
	public int getKind() {
		if (getTarget() instanceof Diff) {
			DifferenceKind kind = ((Diff)getTarget()).getKind();
			switch (kind) {
				case ADD:
					return Differencer.ADDITION;
				case DELETE:
					return Differencer.DELETION;
				case CHANGE:
					return Differencer.CHANGE;
			}
		}

		if (getTarget() instanceof Conflict) {
			return Differencer.CONFLICTING;
		}

		if (getTarget() instanceof Match) {
			if (((Match)getTarget()).getLeft() == null) {
				return Differencer.DELETION;
			}
			if (((Match)getTarget()).getRight() == null) {
				return Differencer.ADDITION;
			}
		}

		return Differencer.NO_CHANGE;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffElement#getParent()
	 */
	public IDiffContainer getParent() {
		if (target instanceof EObject) {
			EObject eContainer = ((EObject)target).eContainer();
			if (eContainer != null) {
				return (IDiffContainer)fAdapterFactory.adapt(eContainer, IDiffContainer.class);
			}
		}
		return null;
	}

	protected final AdapterFactory getAdapterFactory() {
		return fAdapterFactory;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffElement#setParent(org.eclipse.compare.structuremergeviewer.IDiffContainer)
	 */
	public void setParent(IDiffContainer parent) {

	}

}
