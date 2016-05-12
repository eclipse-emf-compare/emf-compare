/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.scope;

import java.util.Set;

import org.eclipse.emf.common.util.URI;

/**
 * The scope of a model comparison.
 * <p>
 * In addition to the contract of {@link IComparisonScope}, this interface provides access to all resources
 * involved in the scope.
 * </p>
 * 
 * @since 3.4
 * @author Philip Langer <planger@eclipsesource.com>
 * @see IComparisonScope
 * @see AbstractComparisonScope
 * @see FilterComparisonScope
 */
public interface IComparisonScope2 extends IComparisonScope {

	/**
	 * Returns the URIs of the all files involved in this scope.
	 * <p>
	 * The returned URIs represent all files that have been selected to be in scope of the comparison. This
	 * may include files that are not existing anymore or that may have actually not been modified.
	 * </p>
	 * 
	 * @return The file URIs.
	 */
	Set<URI> getAllInvolvedResourceURIs();

}
