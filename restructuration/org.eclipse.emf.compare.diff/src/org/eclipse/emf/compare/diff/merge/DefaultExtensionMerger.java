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
package org.eclipse.emf.compare.diff.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * Basic implementation of a {@link DefaultMerger}. Clients can extend this class.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class DefaultExtensionMerger extends DefaultMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#getDependencies(boolean)
	 */
	@Override
	protected List<DiffElement> getDependencies(boolean applyInOrigin) {
		final List<DiffElement> requiredDiffs = diff.getRequires();
		return getBusinessDependencies(applyInOrigin, requiredDiffs);
	}

	/**
	 * Get the difference dependencies to consider in the context of the merge process.
	 * 
	 * @param applyInOrigin
	 *            Direction of merge.
	 * @param requiredDiffs
	 *            The required differences.
	 * @return The required differences to keep.
	 */
	protected List<DiffElement> getBusinessDependencies(boolean applyInOrigin, List<DiffElement> requiredDiffs) {
		final List<DiffElement> result = new ArrayList<DiffElement>();
		for (DiffElement diffElement : requiredDiffs) {
			if (!(diffElement instanceof AbstractDiffExtension)
					|| diffElement instanceof AbstractDiffExtension
					&& isBusinessDependency(applyInOrigin, (AbstractDiffExtension)diffElement)) {
				result.add(diffElement);
			}
		}
		return result;
	}

	/**
	 * Check if the given required difference extension has to be considered in relation to the direction of
	 * merge.
	 * 
	 * @param applyInOrigin
	 *            Direction of merge.
	 * @param requiredDiff
	 *            The required difference.
	 * @return True if it has to be considered in the merge.
	 */
	protected boolean isBusinessDependency(boolean applyInOrigin, AbstractDiffExtension requiredDiff) {
		return true;
	}

}
