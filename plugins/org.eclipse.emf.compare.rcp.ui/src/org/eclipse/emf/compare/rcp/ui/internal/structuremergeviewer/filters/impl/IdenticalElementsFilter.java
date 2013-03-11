/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.spec.MatchItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.ecore.EObject;

/**
 * A filter used by default that filtered out identical elements.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class IdenticalElementsFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> predicateWhenSelected = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			if (input instanceof Match) {
				Match match = (Match)input;
				Iterator<Adapter> adapters = match.eAdapters().iterator();
				while (adapters.hasNext()) {
					Adapter adapter = adapters.next();
					if (adapter instanceof MatchItemProviderSpec) {
						MatchItemProviderSpec matchItem = (MatchItemProviderSpec)adapter;
						return Iterables.isEmpty(matchItem.getFilteredChildren((match)));
					}
				}
				return Iterables.isEmpty(match.getAllDifferences());
			}
			return false;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return predicateWhenSelected;
	}

}
