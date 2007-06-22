/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.generic.merge.impl;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.merge.api.AbstractMerger;

/**
 * Default implementation of an {@link AbstractMerger}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DefaultMerger extends AbstractMerger {
	/**
	 * Create a merger from a given {@link DiffElement}.
	 * 
	 * @param element
	 *            The {@link DiffElement} we need a merger for.
	 */
	public DefaultMerger(DiffElement element) {
		super(element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#undoInTarget()
	 */
	public void undoInTarget() {
		removeFromContainer(diff);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	public void applyInOrigin() {
		removeFromContainer(diff);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#canApplyInOrigin()
	 */
	public boolean canApplyInOrigin() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#canUndoInTarget()
	 */
	public boolean canUndoInTarget() {
		return true;
	}
}
