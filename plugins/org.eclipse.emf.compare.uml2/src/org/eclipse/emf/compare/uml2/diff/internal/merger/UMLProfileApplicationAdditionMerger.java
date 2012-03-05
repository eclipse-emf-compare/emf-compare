/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.merger;

import org.eclipse.emf.compare.diff.merge.DefaultExtensionMerger;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;

/**
 * Merger of UMLProfileApplicationAddition differences.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class UMLProfileApplicationAdditionMerger extends DefaultExtensionMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultExtensionMerger#isBusinessDependency(boolean,
	 *      org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension)
	 */
	@Override
	protected boolean isBusinessDependency(boolean applyInOrigin, AbstractDiffExtension requiredDiff) {
		return applyInOrigin;
	}

}
