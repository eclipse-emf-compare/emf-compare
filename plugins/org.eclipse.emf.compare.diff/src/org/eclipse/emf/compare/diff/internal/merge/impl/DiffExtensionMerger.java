/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.merge.impl;

import org.eclipse.emf.compare.diff.merge.DefaultMerger;

/**
 * Merger for an {@link DiffExtension} operation.<br/>
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DiffExtensionMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#doApplyInOrigin()
	 */

	@Override
	public void doApplyInOrigin() {
		// TODO code diff extension merger
		// final AbstractDiffExtension extension = (AbstractDiffExtension)this.diff;
		// if (!extension.providesMerger()) {
		// /*
		// * No merger is provided so we'll browse the hidden elements and merge them..
		// */
		// for (final Iterator<DiffElement> iterator = extension.getHideElements().iterator();
		// iterator.hasNext(); ) {
		// final DiffElement hidden = iterator.next();
		// final AbstractMerger merger = MergeFactory.createMerger(hidden);
		// merger.applyInOrigin();
		// }
		// } else {
		// extension.getMerger().applyInOrigin();
		// }
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#doUndoInTarget()
	 */
	@Override
	public void doUndoInTarget() {
		// TODO code diff extension merger
		// final AbstractDiffExtension extension = (AbstractDiffExtension)this.diff;
		// if (!extension.providesMerger()) {
		// /*
		// * No merger is provided so we'll browse the hidden elements and merge them..
		// */
		// for (final Iterator<DiffElement> iterator = extension.getHideElements().iterator();
		// iterator.hasNext(); ) {
		// final DiffElement hidden = iterator.next();
		// final AbstractMerger merger = MergeFactory.createMerger(hidden);
		// merger.undoInTarget();
		// }
		// } else {
		// extension.getMerger().undoInTarget();
		// }
	}
}
