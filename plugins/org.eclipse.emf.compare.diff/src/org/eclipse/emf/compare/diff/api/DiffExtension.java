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
package org.eclipse.emf.compare.diff.api;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.merge.api.AbstractMerger;

/**
 * A Diff Extension may be used to define higher level diff operations.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public interface DiffExtension {
	/**
	 * TODOCBR comment.
	 * 
	 * @param diff
	 *            TODO comment
	 */
	void visit(DiffModel diff);

	/**
	 * TODOCBR comment.
	 * 
	 * @param element
	 *            TODO comment
	 * @return TODO comment
	 */
	AbstractMerger providesMerger(DiffElement element);
}
