/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.internal.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * This merger will handle DiffGroup merging by recursively create mergers on each of its contents.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DiffGroupMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.diff.merge.api.DefaultMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		// Creates a temporary list to avoid ConcurentModificationExceptions
		final List<DiffElement> subDiffs = new ArrayList<DiffElement>(diff.getSubDiffElements());
		MergeService.merge(subDiffs, false);
		super.applyInOrigin();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.diff.merge.api.DefaultMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		// Creates a temporary list to avoid ConcurentModificationExceptions
		final List<DiffElement> subDiffs = new ArrayList<DiffElement>(diff.getSubDiffElements());
		MergeService.merge(subDiffs, true);
		super.undoInTarget();
	}
}
