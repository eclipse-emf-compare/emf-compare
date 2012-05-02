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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.EObjectNode;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;
import org.eclipse.emf.ecore.EObject;

/**
 * Specific AbstractEDiffNode for {@link Match} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchNode extends AbstractEDiffNode {

	/**
	 * Creates a node with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public MatchNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Match getTarget() {
		return (Match)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getType()
	 */
	@Override
	public String getType() {
		return "eobject";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getAncestor()
	 */
	@Override
	public ITypedElement getAncestor() {
		EObject o = getTarget().getOrigin();
		if (o != null) {
			return new EObjectNode(getAdapterFactory(), o);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getLeft()
	 */
	@Override
	public ITypedElement getLeft() {
		EObject o = getTarget().getLeft();
		if (o != null) {
			return new EObjectNode(getAdapterFactory(), o);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getRight()
	 */
	@Override
	public ITypedElement getRight() {
		EObject o = getTarget().getRight();
		if (o != null) {
			return new EObjectNode(getAdapterFactory(), o);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getKind()
	 */
	@Override
	public int getKind() {
		int ret = super.getKind();
		if (getTarget().getLeft() == null) {
			ret = Differencer.DELETION;
		}
		if (getTarget().getRight() == null) {
			ret = Differencer.ADDITION;
		}
		return ret;
	}
}
