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
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComparisonItemProviderSpec extends ComparisonItemProvider implements IItemStyledLabelProvider {

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
		return ImmutableList.copyOf(getChildrenIterable(comparison));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		Comparison comparison = (Comparison)object;
		return !isEmpty(getChildrenIterable(comparison));
	}

	private Iterable<EObject> getChildrenIterable(Comparison comparison) {
		Iterable<Match> match = getNonEmptyMatches(comparison);
		return concat(match, comparison.getMatchedResources());
	}

	private Iterable<Match> getNonEmptyMatches(Comparison comparison) {
		Iterable<Match> match = filter(comparison.getMatches(), new Predicate<Match>() {
			public boolean apply(Match input) {
				final boolean ret;
				IEditingDomainItemProvider editingDomainItemProvider = (IEditingDomainItemProvider)adapterFactory
						.adapt(input, IEditingDomainItemProvider.class);

				if (editingDomainItemProvider instanceof MatchItemProviderSpec) {
					ret = !isEmpty(((MatchItemProviderSpec)editingDomainItemProvider)
							.getChildrenIterable(input));
				} else if (editingDomainItemProvider != null) {
					ret = !editingDomainItemProvider.getChildren(input).isEmpty();
				} else {
					ret = false;
				}

				return ret;
			}
		});
		return match;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		return new ComposedStyledString(getText(object));
	}
}
