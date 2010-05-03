/*******************************************************************************
 * Copyright (c) 2010  itemis AG (http://www.itemis.de)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexander Nyssen - itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import org.eclipse.emf.compare.match.filter.IResourceFilter;

/**
 * A scope provider that can be passed to a {@link IMatchEngine} via the
 * {@link org.eclipse.emf.compare.match.MatchOptions#OPTION_MATCH_SCOPE_PROVIDER} option. The
 * {@link IMatchEngine} can evaluate the scope to restrict the set of
 * {@link org.eclipse.emf.ecore.resource.Resource}s and {@link org.eclipse.emf.ecore.EObject}s it compares. It
 * always has to provide a left and right {@link IMatchScope}, in case it is used in a three-way-comparison
 * setting, an ancestor {@link IMatchScope} also has to be provided.
 * 
 * @author <a href="mailto:alexander.nyssen@itemis.de">Alexander Nyssen</a>
 * @since 1.1
 */
public interface IMatchScopeProvider {

	/**
	 * The {@link IMatchScope} to restrict the left side of comparison.
	 * 
	 * @return an instance of {@link IMatchScope}. May not be <code>null</code>.
	 */
	IMatchScope getLeftScope();

	/**
	 * The {@link IMatchScope} to restrict the right side of comparison.
	 * 
	 * @return an instance of {@link IMatchScope}. May not be <code>null</code>.
	 */
	IMatchScope getRightScope();

	/**
	 * The {@link IMatchScope} to restrict the ancestor side of comparison.
	 * 
	 * @return an instance of {@link IMatchScope}. May also be <code>null</code> in case it is used in a
	 *         two-way-comparison scenario.
	 */
	IMatchScope getAncestorScope();

	/**
	 * In case the left, right, and target {@link IMatchScope}s are based on
	 * {@link org.eclipse.emf.ecore.resource.Resource}s and not {@link org.eclipse.emf.ecore.EObject}s, the
	 * filter can be applied to further restrict the scope. Otherwise it will be ignored.
	 * 
	 * @param filter
	 *            the {@link IResourceFilter} to be applied to the left, right scopes, as well as the ancestor
	 *            scope (if it is specified).
	 */
	void applyResourceFilter(IResourceFilter filter);
}
