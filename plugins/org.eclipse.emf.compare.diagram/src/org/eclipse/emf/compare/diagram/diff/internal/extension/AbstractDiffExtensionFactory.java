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
package org.eclipse.emf.compare.diagram.diff.internal.extension;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory of diagram difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	/**
	 * Switch which returns the <code>DifferenceKind</code> of the matching diagram extension in relation to
	 * the given difference.
	 */
	// CHECKSTYLE:OFF
	private CompareSwitch<DifferenceKind> differenceKindCompareSwitch = new CompareSwitch<DifferenceKind>() {
		// CHECKSTYLE:ON

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.util.CompareSwitch#caseAttributeChange(org.eclipse.emf.compare.AttributeChange)
		 */
		@Override
		public DifferenceKind caseAttributeChange(AttributeChange object) {
			DifferenceKind result;
			if (isRelatedToAnExtensionAdd(object)) {
				result = DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				result = DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				result = DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				result = DifferenceKind.MOVE;
			} else {
				result = super.caseAttributeChange(object);
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.util.CompareSwitch#caseReferenceChange(org.eclipse.emf.compare.ReferenceChange)
		 */
		@Override
		public DifferenceKind caseReferenceChange(ReferenceChange object) {
			DifferenceKind result;
			if (isRelatedToAnExtensionAdd(object)) {
				result = DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				result = DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				result = DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				result = DifferenceKind.MOVE;
			} else {
				result = super.caseReferenceChange(object);
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.util.CompareSwitch#caseResourceAttachmentChange(org.eclipse.emf.compare.ResourceAttachmentChange)
		 */
		@Override
		public DifferenceKind caseResourceAttachmentChange(ResourceAttachmentChange object) {
			DifferenceKind result;
			if (isRelatedToAnExtensionAdd(object)) {
				result = DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				result = DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				result = DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				result = DifferenceKind.MOVE;
			} else {
				result = super.caseResourceAttachmentChange(object);
			}
			return result;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.Diff)
	 */
	public boolean handles(Diff input) {
		return getRelatedExtensionKind(input) != null && !isDiffOnAddOrDelete(input);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	public Match getParentMatch(Diff input) {
		return input.getMatch();
	}

	/**
	 * Check if the given difference is part of an add or delete change.
	 * 
	 * @param input
	 *            The difference to check.
	 * @return True if it is part of an add or delete change, false otherwise.
	 */
	protected boolean isDiffOnAddOrDelete(Diff input) {
		final Match match = input.getMatch();
		final EObject container = match.eContainer();
		if (container instanceof Match) {
			final Iterator<Diff> diffs = ((Match)container).getAllDifferences().iterator();
			while (diffs.hasNext()) {
				final Diff diff = diffs.next();
				if (diff instanceof ReferenceChange
						&& (isRelatedToAnExtensionAdd((ReferenceChange)diff) || isRelatedToAnExtensionDelete((ReferenceChange)diff))
						&& match.getComparison().getMatch(((ReferenceChange)diff).getValue()) == match) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the <code>DifferenceKind</code> of the matching diagram difference extension in relation to the
	 * given difference.
	 * 
	 * @param input
	 *            The given difference.
	 * @return The kind of the diagram difference extension if this one exists, null otherwise.
	 */
	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		return differenceKindCompareSwitch.doSwitch(input);
	}

	/**
	 * Check if the given reference change is related to a graphical add. It may be overridden in the child
	 * factories in order to precise which kind of graphical add has to be considered.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if the reference change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return false;
	}

	/**
	 * Check if the given reference change is related to a graphical delete. It may be overridden in the child
	 * factories in order to precise which kind of graphical delete has to be considered.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if the reference change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return false;
	}

	/**
	 * Check if the given reference change is related to a graphical change. It may be overridden in the child
	 * factories in order to precise which kind of graphical change has to be considered.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if the reference change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return false;
	}

	/**
	 * Check if the given reference change is related to a graphical move. It may be overridden in the child
	 * factories in order to precise which kind of graphical move has to be considered.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if the reference change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionMove(ReferenceChange input) {
		return false;
	}

	/**
	 * Check if the given attribute change is related to a graphical add. It may be overridden in the child
	 * factories in order to precise which kind of graphical add has to be considered.
	 * 
	 * @param input
	 *            The attribute change.
	 * @return True if the attribute change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionAdd(AttributeChange input) {
		return false;
	}

	/**
	 * Check if the given attribute change is related to a graphical delete. It may be overridden in the child
	 * factories in order to precise which kind of graphical delete has to be considered.
	 * 
	 * @param input
	 *            The attribute change.
	 * @return True if the attribute change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionDelete(AttributeChange input) {
		return false;
	}

	/**
	 * Check if the given attribute change is related to a graphical change. It may be overridden in the child
	 * factories in order to precise which kind of graphical change has to be considered.
	 * 
	 * @param input
	 *            The attribute change.
	 * @return True if the attribute change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return false;
	}

	/**
	 * Check if the given attribute change is related to a graphical move. It may be overridden in the child
	 * factories in order to precise which kind of graphical move has to be considered.
	 * 
	 * @param input
	 *            The attribute change.
	 * @return True if the attribute change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionMove(AttributeChange input) {
		return false;
	}

	/**
	 * Check if the given resource attachment change is related to a graphical add. It may be overridden in
	 * the child factories in order to precise which kind of graphical add has to be considered.
	 * 
	 * @param input
	 *            The resource attachment change.
	 * @return True if the resource attachment change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionAdd(ResourceAttachmentChange input) {
		return false;
	}

	/**
	 * Check if the given resource attachment change is related to a graphical delete. It may be overridden in
	 * the child factories in order to precise which kind of graphical delete has to be considered.
	 * 
	 * @param input
	 *            The resource attachment change.
	 * @return True if the resource attachment change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionDelete(ResourceAttachmentChange input) {
		return false;
	}

	/**
	 * Check if the given resource attachment change is related to a graphical change. It may be overridden in
	 * the child factories in order to precise which kind of graphical change has to be considered.
	 * 
	 * @param input
	 *            The resource attachment change.
	 * @return True if the resource attachment change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionChange(ResourceAttachmentChange input) {
		return false;
	}

	/**
	 * Check if the given resource attachment change is related to a graphical ;ove. It may be overridden in
	 * the child factories in order to precise which kind of graphical ;ove has to be considered.
	 * 
	 * @param input
	 *            The resource attachment change.
	 * @return True if the resource attachment change is a good candidate, false otherwise.
	 */
	protected boolean isRelatedToAnExtensionMove(ResourceAttachmentChange input) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory#getExtensionKind()
	 */
	public Class<? extends Diff> getExtensionKind() {
		return Diff.class;
	}

	/**
	 * Get all the add and delete changes on the objects contained in the one concerned by the given reference
	 * change.
	 * 
	 * @param input
	 *            The given reference change.
	 * @return The found differences.
	 */
	protected Set<Diff> getAllContainedDifferences(ReferenceChange input) {
		final Comparison comparison = input.getMatch().getComparison();
		final Match match = comparison.getMatch(input.getValue());
		final Set<Diff> result = getAllContainedDifferences(comparison, match);

		return result;
	}

	/**
	 * Get all the add and delete changes under the given match.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param match
	 *            The match
	 * @return The found differences.
	 */
	private Set<Diff> getAllContainedDifferences(Comparison comparison, Match match) {
		final Set<Diff> result = Sets.newLinkedHashSet();

		final Set<Match> prune = Sets.newLinkedHashSet();
		for (Diff candidate : match.getDifferences()) {
			if (!getExtensionKind().isInstance(candidate)) {
				if (!(candidate instanceof ReferenceChange && (isRelatedToAnExtensionAdd((ReferenceChange)candidate) || isRelatedToAnExtensionDelete((ReferenceChange)candidate)))) {
					result.add(candidate);
				} else if (candidate instanceof ReferenceChange
						&& ((ReferenceChange)candidate).getReference().isContainment()) {
					prune.add(comparison.getMatch(((ReferenceChange)candidate).getValue()));
				}
			}
		}

		for (Match submatch : match.getSubmatches()) {
			if (!prune.contains(submatch)) {
				result.addAll(getAllContainedDifferences(comparison, submatch));
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
	}

	/**
	 * Find the differences, on the given model object, which match with the predicate.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param lookup
	 *            The model object.
	 * @param p
	 *            The predicate.
	 * @return The found differences.
	 */
	protected final List<Diff> findCrossReferences(Comparison comparison, EObject lookup, Predicate<Diff> p) {
		final List<Diff> result = new ArrayList<Diff>();
		for (Diff diff : comparison.getDifferences(lookup)) {
			if (p.apply(diff)) {
				result.add(diff);
			}
		}
		return result;
	}

	/**
	 * Get the GMF view which represents a semantic element and containing the given difference.
	 * 
	 * @param input
	 *            The difference.
	 * @return The found view.
	 */
	protected View getViewContainer(Diff input) {
		final Match match = input.getMatch();
		return getViewContainer(match);
	}

	/**
	 * From the one of the matched objects of the given match, it returns the first found parent GMF view
	 * which represents a semantic element.
	 * 
	 * @param match
	 *            The given match.
	 * @return The found view.
	 */
	protected View getViewContainer(Match match) {
		EObject result = match.getLeft();
		if (result == null) {
			result = match.getRight();
		}
		if (!(result instanceof View && ReferenceUtil
				.safeEGet(result, NotationPackage.Literals.VIEW__ELEMENT) != null)
				&& match.eContainer() instanceof Match) {
			result = getViewContainer((Match)match.eContainer());
		}
		return (View)result;
	}

}
