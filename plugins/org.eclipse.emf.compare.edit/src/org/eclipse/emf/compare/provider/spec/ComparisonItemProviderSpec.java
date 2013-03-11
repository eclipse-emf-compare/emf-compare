/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
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
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.provider.ComparisonItemProvider;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;

/**
 * Specialized {@link ComparisonItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComparisonItemProviderSpec extends ComparisonItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/**
	 * Constructs a ComparisonItemProviderSpec with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
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

	/**
	 * Returns the children of the given {@link Comparison}. In this case children means: non empty matches
	 * (see {@link ComparisonItemProviderSpec#getNonEmptyMatches(Comparison)}), matches which not only
	 * contains {@link ResourceAttachmentChange} differences, and matched resources.
	 * 
	 * @param comparison
	 *            the given {@link Comparison}.
	 * @return the filtered list of children of the given {@link Comparison}.
	 */
	private Iterable<EObject> getChildrenIterable(Comparison comparison) {
		Iterable<? extends EObject> matches = getNonEmptyMatches(comparison);
		List<EObject> children = Lists.newArrayList(matches);
		for (EObject match : matches) {
			EList<Diff> differences = ((Match)match).getDifferences();
			if (!isEmpty(filter(differences, ResourceAttachmentChange.class))) {
				children.remove(match);
			}
		}

		return concat(children, comparison.getMatchedResources());
	}

	/**
	 * Filters out the empty matches of the given {@link Comparison}.
	 * 
	 * @param comparison
	 *            the given {@link Comparison}.
	 * @return an iterable of non empty matches.
	 */
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		return getText(object);
	}
}
