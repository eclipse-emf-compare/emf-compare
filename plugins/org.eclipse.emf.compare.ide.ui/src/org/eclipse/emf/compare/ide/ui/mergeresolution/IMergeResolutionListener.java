/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.mergeresolution;

import org.eclipse.emf.compare.Comparison;

/**
 * Provides an interface for listening to the events of a successful merge resolution.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 * @since 4.2
 */
public interface IMergeResolutionListener {
	/**
	 * Fired when a merge is resolved entirely (no more unresolved conflicts) and the user saves the merge
	 * result.
	 * 
	 * @param comparison
	 *            the comparison which has just been resolved
	 */
	void mergeResolutionCompleted(Comparison comparison);
}
