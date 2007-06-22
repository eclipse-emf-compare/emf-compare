/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diff.api;

import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.merge.api.AbstractMerger;

/**
 * A Diff Extension may be used to define higher level diff operations.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public interface DiffExtension {
	/**
	 * TODOCBR comment
	 * 
	 * @param diff
	 */
	public void visit(DiffModel diff);

	/**
	 * @param element
	 * @return  TODO comment
	 */
	public AbstractMerger providesMerger(DiffElement element);
}
