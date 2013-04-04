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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.ConflictItemProvider;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;

/**
 * Specialized {@link ConflictItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ConflictItemProviderSpec extends ConflictItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/**
	 * Returns the sub diffs of a given diff.
	 */
	private static Function<Diff, Iterable<Diff>> getSubDiffs = new Function<Diff, Iterable<Diff>>() {
		public Iterable<Diff> apply(Diff diff) {
			if (diff instanceof ReferenceChange) {
				Match match = diff.getMatch();
				Match matchOfValue = diff.getMatch().getComparison().getMatch(
						((ReferenceChange)diff).getValue());
				if (!match.equals(matchOfValue) && match.getSubmatches().contains(matchOfValue)) {
					final Iterable<Diff> subDiffs = matchOfValue.getAllDifferences();
					return ImmutableSet.copyOf(subDiffs);
				}
			}
			return ImmutableSet.of();
		}
	};

	/**
	 * Constructs a ComparisonItemProviderSpec with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public ConflictItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ConflictItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Conflict conflict = (Conflict)object;
		EList<Diff> differences = conflict.getDifferences();
		Collection<Diff> filteredDifferences = Sets.newLinkedHashSet();
		for (Diff diff : differences) {
			boolean subdiff = false;
			for (Diff diffParent : differences) {
				if (!diff.equals(diffParent) && Iterables.contains(getSubDiffs.apply(diffParent), diff)) {
					subdiff = true;
					break;
				}
			}
			if (!subdiff) {
				filteredDifferences.add(diff);
			}
		}

		return filteredDifferences;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		Conflict conflict = (Conflict)object;
		int size = conflict.getDifferences().size();
		String kind = conflict.getKind().getName().toLowerCase();
		ComposedStyledString ret = new ComposedStyledString(kind.substring(0, 1).toUpperCase()
				+ kind.substring(1) + " " + EMFCompareEditMessages.getString("conflict")); //$NON-NLS-1$ //$NON-NLS-2$

		ret.append(
				" [" + size + " " + EMFCompareEditMessages.getString("difference"), Style.DECORATIONS_STYLER); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		if (size > 1) {
			ret.append(EMFCompareEditMessages.getString("plural"), Style.DECORATIONS_STYLER); //$NON-NLS-1$
		}
		ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		Conflict conflict = (Conflict)object;
		int size = conflict.getDifferences().size() - 1;
		String ret = conflict.getKind().getName() + " conflict with " + size + " other difference";
		if (size > 1) {
			ret += "s";
		}
		return ret;
	}
}
