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

import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.AbstractEDiffNode;

/**
 * Specific {@link AbstractEDiffNode} for {@link Diff} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DiffNode extends AbstractEDiffNode {

	/**
	 * Call {@link AbstractEDiffNode super} constructor.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public DiffNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Diff getTarget() {
		return (Diff)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.AbstractEDiffElement#getKind()
	 */
	@Override
	public int getKind() {
		int ret = Integer.MIN_VALUE;
		DifferenceSource source = getTarget().getSource();
		Comparison c = getTarget().getMatch().getComparison();
		switch (getTarget().getKind()) {
			case ADD:
				ret = Differencer.ADDITION;
				break;
			case DELETE:
				ret = Differencer.DELETION;
				break;
			case CHANGE:
				ret = Differencer.CHANGE;
				break;
			default:
				ret = super.getKind();
		}
		return ret;
	}
}
