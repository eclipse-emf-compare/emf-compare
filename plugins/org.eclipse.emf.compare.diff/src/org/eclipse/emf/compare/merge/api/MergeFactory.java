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
package org.eclipse.emf.compare.merge.api;

import org.eclipse.emf.compare.diff.DiffElement;

/**
 * A Merge factory handle the merge creation for DiffElements
 * 
 * @author Cedric Brun  <a href="mailto:cedric.brun@obeo.fr ">cedric.brun@obeo.fr</a> 
 * 
 */
public abstract class MergeFactory {
	/**
	 * 
	 * @param element
	 * @return the corresponding merger
	 */
	public abstract AbstractMerger createMerger(DiffElement element);

}
