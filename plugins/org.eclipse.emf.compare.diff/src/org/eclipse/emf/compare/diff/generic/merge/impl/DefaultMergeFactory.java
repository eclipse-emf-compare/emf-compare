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

import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.merge.api.MergeFactory;

/**
 * Default implementation of a {@link MergeFactory}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DefaultMergeFactory extends MergeFactory {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.MergeFactory#createMerger(org.eclipse.emf.compare.diff.DiffElement)
	 */
	@Override
	public AbstractMerger createMerger(DiffElement element) {
		AbstractMerger elementMerger = new DefaultMerger(element);
		if (element instanceof AddModelElement) {
			elementMerger = new AddModelElementMerger(element);
		} else if (element instanceof RemoveModelElement) {
			elementMerger = new RemoveModelElementMerger(element);
		} else if (element instanceof UpdateAttribute) {
			elementMerger = new UpdateAttributeMerger(element);
		} else if (element instanceof AddReferenceValue) {
			elementMerger = new AddReferenceValueMerger(element);
		} else if (element instanceof RemoveReferenceValue) {
			elementMerger = new RemoveReferenceValueMerger(element);
		}

		return elementMerger;
	}
}
