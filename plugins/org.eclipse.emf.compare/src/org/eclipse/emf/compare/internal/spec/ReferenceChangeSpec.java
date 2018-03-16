/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 516520
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.ReferenceChangeImpl;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.utils.Objects;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * This specialization of the {@link ReferenceChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceChangeSpec extends ReferenceChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyLeftToRight()
	 * @deprecated Use
	 *             {@link org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff, org.eclipse.emf.common.util.Monitor)}
	 *             instead. See javadoc of IMerger.Registry for usage instructions.
	 */
	@Deprecated
	@Override
	public void copyLeftToRight() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyLeftToRight(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyRightToLeft()
	 * @deprecated Use
	 *             {@link org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff, org.eclipse.emf.common.util.Monitor)}
	 *             instead. See javadoc of IMerger.Registry for usage instructions.
	 */
	@Deprecated
	@Override
	public void copyRightToLeft() {
		/*
		 * This is not extensible : we always use the default merger instead of querying the registry. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyRightToLeft(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
		// Should we also discard equivalent diffs? And diffs that require this one?
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		// @formatter:off
		StringBuilder b = new StringBuilder();
		return b.append(getState()).append(' ').append(getSource()).append(' ').append(getKind()).append(' ')
				.append(Objects.toStringHelper(this)
						.add("reference",
								getReference().getEContainingClass().getName() + "."
										+ getReference().getName())
						.add("value", EObjectUtil.getLabel(getValue()))
						.add("parentMatch", getMatch().toString())
						.add("match of value", getMatch().getComparison().getMatch(getValue())))
				.toString();
		// @formatter:on
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#basicGetMatch()
	 */
	@Override
	public Match basicGetMatch() {
		if (eContainer() instanceof Match) {
			return (Match)eContainer();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#setMatch(org.eclipse.emf.compare.Match)
	 */
	@Override
	public void setMatch(Match newMatch) {
		Match oldMatch = basicGetMatch();
		if (newMatch != null) {
			EList<Diff> differences = newMatch.getDifferences();
			differences.add(this);
			if (eNotificationRequired()) {
				eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__MATCH, oldMatch,
						newMatch));
			}
		} else if (eContainer() instanceof Match) {
			EList<Diff> differences = ((Match)eContainer()).getDifferences();
			differences.remove(this);
			if (eNotificationRequired()) {
				eNotify(new ENotificationImpl(this, Notification.UNSET, ComparePackage.DIFF__MATCH, oldMatch,
						newMatch));
			}
		}
	}
}
