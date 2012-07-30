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

import com.google.common.collect.Iterables;

import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.internal.actions.group.DifferenceGrouper;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;

/**
 * Specific AbstractEDiffNode for {@link Comparison} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComparisonNode extends AbstractEDiffNode {
	private DifferenceGrouper grouper;

	/**
	 * Creates a node with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public ComparisonNode(AdapterFactory adapterFactory, DifferenceGrouper grouper) {
		super(adapterFactory);
		this.grouper = grouper;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Comparison getTarget() {
		return (Comparison)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffContainer#getChildren()
	 */
	@Override
	public IDiffElement[] getChildren() {
		if (grouper != null) {
			final Iterable<? extends IDiffElement> groups = grouper.getGroups(getTarget(),
					getAdapterFactory());
			if (groups != null) {
				return Iterables.toArray(groups, IDiffElement.class);
			}
		}
		return super.getChildren();
	}

}
