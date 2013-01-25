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
package org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class BasicResourceContentsAccessorImpl implements IResourceContentsAccessor {

	/** The difference performed. */
	private final Diff fDiff;

	/** The side on which the difference is located. */
	private final MergeViewerSide fSide;

	/** The match associated to the performed difference. */
	private final Match fOwnerMatch;

	/** The list of sibling differences of the performed difference. */
	private final ImmutableList<Diff> fDifferences;

	/**
	 * @param diff
	 *            The difference performed.
	 * @param side
	 *            The side on which the difference is located.
	 */
	public BasicResourceContentsAccessorImpl(Diff diff, MergeViewerSide side) {
		fDiff = diff;
		fSide = side;
		fOwnerMatch = diff.getMatch();
		fDifferences = computeDifferences();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IResourceContentsAccessor#getComparison()
	 */
	public Comparison getComparison() {
		return fOwnerMatch.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IResourceContentsAccessor#getInitialItem()
	 */
	public IMergeViewerItem getInitialItem() {
		IMergeViewerItem ret = null;
		ImmutableList<? extends IMergeViewerItem> items = getItems();
		for (IMergeViewerItem item : items) {
			Diff diff = item.getDiff();
			if (diff == fDiff) {
				ret = item;
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IResourceContentsAccessor#getResource(org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	public Resource getResource(MergeViewerSide side) {
		Resource resource = null;
		Collection<MatchResource> matchResources = fOwnerMatch.getComparison().getMatchedResources();
		Iterator<MatchResource> it = matchResources.iterator();
		if (it.hasNext()) {
			MatchResource matchResource = it.next();
			switch (side) {
				case ANCESTOR:
					resource = matchResource.getOrigin();
					break;
				case LEFT:
					resource = matchResource.getLeft();
					break;
				case RIGHT:
					resource = matchResource.getRight();
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return resource;
	}

	/**
	 * Returns the contents of the current resource on the given side.
	 * 
	 * @param side
	 *            The given side.
	 * @return The contents of the current resource on the given side.
	 */
	public List<EObject> getResourceContents(MergeViewerSide side) {
		Resource resource = getResource(side);
		if (resource != null) {
			return resource.getContents();
		}
		return Collections.emptyList();

	}

	/**
	 * Returns the side of the content merge viewer on which the difference is performed.
	 * 
	 * @return The side of the content merge viewer on which the difference is performed.
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * Returns the list of sibling differences of the current difference.
	 * 
	 * @return The list of sibling differences of the current difference.
	 */
	protected final ImmutableList<Diff> getDifferences() {
		return fDifferences;
	}

	/**
	 * Computes the list of sibling differences of the current difference.
	 * 
	 * @return The list of sibling differences of the current difference.
	 */
	protected ImmutableList<Diff> computeDifferences() {
		Iterable<EObject> concat = Iterables.concat(getResourceContents(MergeViewerSide.LEFT),
				getResourceContents(MergeViewerSide.RIGHT), getResourceContents(MergeViewerSide.ANCESTOR));

		final Comparison comparison = fOwnerMatch.getComparison();
		Iterable<Match> matches = transform(concat, new Function<EObject, Match>() {
			public Match apply(EObject eObject) {
				return comparison.getMatch(eObject);
			}
		});
		Iterable<Diff> siblingDifferences = concat(transform(matches, new Function<Match, List<Diff>>() {
			public List<Diff> apply(Match match) {
				return match.getDifferences();
			}
		}));
		Predicate<? super Diff> diffFilter = and(instanceOf(ResourceAttachmentChange.class),
				not(hasConflict(ConflictKind.PSEUDO)));
		return ImmutableSet.<Diff> copyOf(filter(siblingDifferences, diffFilter)).asList();
	}

}
