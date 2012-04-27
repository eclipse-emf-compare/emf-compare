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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer.provider.SingleAttributeChange;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class AttributeChangeNode extends DiffNode {

	/**
	 * @param adapterFactory
	 */
	public AttributeChangeNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider.DiffNode#getTarget()
	 */
	@Override
	public AttributeChange getTarget() {
		return (AttributeChange)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.AbstractEDiffElement#getType()
	 */
	@Override
	public String getType() {
		return ITypedElement.TEXT_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.AbstractEDiffNode#getAncestor()
	 */
	@Override
	public ITypedElement getAncestor() {
		ITypedElement ret = null;
		EAttribute attribute = getTarget().getAttribute();
		EObject origin = getTarget().getMatch().getOrigin();
		if (origin != null) {
			if (attribute.isMany()) {
				// todo
			} else {
				ret = new SingleAttributeChange(origin, attribute);
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.AbstractEDiffNode#getLeft()
	 */
	@Override
	public ITypedElement getLeft() {
		ITypedElement ret = null;
		EAttribute attribute = getTarget().getAttribute();
		EObject left = getTarget().getMatch().getLeft();
		if (left != null) {
			if (attribute.isMany()) {
				// todo
			} else {
				ret = new SingleAttributeChange(left, attribute);
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.AbstractEDiffNode#getRight()
	 */
	@Override
	public ITypedElement getRight() {
		ITypedElement ret = null;
		EAttribute attribute = getTarget().getAttribute();
		EObject right = getTarget().getMatch().getRight();
		if (right != null) {
			if (attribute.isMany()) {
				// todo
			} else {
				ret = new SingleAttributeChange(right, attribute);
			}
		}
		return ret;
	}

}
