/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.source;

import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * This adapter is used by EMFCompare to check if a compare button especially for EMFCompare should be added.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 * @since 4.2
 */
public interface IEMFComparisonSource {

	/**
	 * A human readable name for the comparison source.
	 * 
	 * @return The name of this comparison source.
	 */
	public String getName();

	/**
	 * The {@link StorageTraversal} which shall be used by EMFCompare.
	 * 
	 * @return The traversal which shall be used by EMFCompare.
	 */
	public StorageTraversal getStorageTraversal();
}
