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
package org.eclipse.emf.compare.diagram.ide.ui.internal.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.DiagramIDEStringLabelChangeAccessor;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.ecore.EObject;

/**
 * Specific AbstractEDiffNode for {@link AttributeChange} objects.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramLabelChangeNode extends DiffNode {

	/**
	 * Creates a node with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public DiagramLabelChangeNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode#getTarget()
	 */
	@Override
	public LabelChange getTarget() {
		return (LabelChange)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode#getType()
	 */
	@Override
	public String getType() {
		return ITypedElement.TEXT_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode#getAncestor()
	 */
	@Override
	public ITypedElement getAncestor() {
		ITypedElement ret = null;
		EObject origin = getTarget().getMatch().getOrigin();
		if (origin != null) {
			ret = new DiagramIDEStringLabelChangeAccessor(origin, getTarget());
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode#getLeft()
	 */
	@Override
	public ITypedElement getLeft() {
		ITypedElement ret = null;
		EObject left = getTarget().getMatch().getLeft();
		if (left != null) {
			ret = new DiagramIDEStringLabelChangeAccessor(left, getTarget());
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode#getRight()
	 */
	@Override
	public ITypedElement getRight() {
		ITypedElement ret = null;
		EObject right = getTarget().getMatch().getRight();
		if (right != null) {
			ret = new DiagramIDEStringLabelChangeAccessor(right, getTarget());
		}
		return ret;
	}
}
