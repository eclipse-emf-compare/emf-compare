/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.internal.impl;

import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;

/**
 * Merger for an {@link DiffExtension} operation.<br/>
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DiffExtensionMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */

	@Override
	public void applyInOrigin() {
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
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
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
