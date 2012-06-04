/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.ComparisonItemProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComparisonItemProviderSpec extends ComparisonItemProvider {

	/**
	 * @param adapterFactory
	 */
	public ComparisonItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Comparison comparison = (Comparison)object;
		Iterable<Match> match = filter(comparison.getMatches(), new Predicate<Match>() {
			public boolean apply(Match input) {
				return !isEmpty(MatchItemProviderSpec.getChildrenIterable(input));
			}
		});
		return ImmutableList.copyOf(concat(match, comparison.getMatchedResources()));
	}
}
