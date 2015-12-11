/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.postprocessor.factories;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * Factory of difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractChangeFactory implements IChangeFactory {

	/**
	 * Switch which returns the <code>DifferenceKind</code> of the matching diagram extension in relation to
	 * the given difference.
	 */
	private CompareSwitch<DifferenceKind> differenceKindCompareSwitch = new DifferenceKindCompareSwitch();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#handles(org.eclipse.emf.compare.Diff)
	 */
	public boolean handles(Diff input) {
		return getRelatedExtensionKind(input) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#create(org.eclipse.emf.compare.Diff)
	 */
	public Diff create(Diff input) {
		Diff ret = createExtension();

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);
		ret.setKind(extensionKind);
		// It's important to set the source before calling setRefiningChanges()
		// because refines/refinedBy EReferences demand diffs on the same side
		ret.setSource(input.getSource());

		setRefiningChanges(ret, extensionKind, input);

		// FIXME: Maybe it would be better to get all conflict objects from all conflicting unit differences
		// and
		// create a new conflict object with these differences, to set on the macroscopic change (ret).
		Diff conflictingDiff = Iterators.find(ret.getRefinedBy().iterator(), new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return difference.getConflict() != null;
			}
		}, null);
		if (conflictingDiff != null) {
			ret.setConflict(conflictingDiff.getConflict());
		}

		return ret;
	}

	/**
	 * It creates the graphical change extension.
	 * 
	 * @return The extension.
	 */
	public abstract Diff createExtension();

	/**
	 * Get the refining differences and set the given extension with them, from the given refining one.
	 * 
	 * @param extension
	 *            The extension to set.
	 * @param extensionKind
	 *            The extension kind.
	 * @param refiningDiff
	 *            The refining difference.
	 */
	public abstract void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	public Match getParentMatch(Diff input) {
		return input.getMatch();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#getExtensionKind()
	 */
	public Class<? extends Diff> getExtensionKind() {
		return Diff.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		fillRequiredDifferencesForMacroToMacro(extension);
		fillRequiredDifferencesForUnitToMacro(extension);
	}

	/**
	 * Browse required and requiring unit differences to deduce the requirement link between this macroscopic
	 * change and other ones.
	 * 
	 * @param macro
	 *            The macroscopic change.
	 */
	private void fillRequiredDifferencesForMacroToMacro(Diff macro) {
		Set<Diff> requiredExtensions = new LinkedHashSet<Diff>();
		Set<Diff> requiringExtensions = new LinkedHashSet<Diff>();
		for (Diff refiningDiff : macro.getRefinedBy()) {
			requiredExtensions.addAll(getDistinctRefinedDifferences(refiningDiff.getRequires()));
			requiringExtensions.addAll(getDistinctRefinedDifferences(refiningDiff.getRequiredBy()));
		}
		// Keep only difference extensions as the given one
		requiredExtensions.remove(macro);
		requiringExtensions.remove(macro);

		macro.getRequires().addAll(
				Collections2.filter(requiredExtensions, EMFComparePredicates.fromSide(macro.getSource())));
		macro.getRequiredBy().addAll(
				Collections2.filter(requiringExtensions, EMFComparePredicates.fromSide(macro.getSource())));
	}

	/**
	 * Browse required and requiring unit differences to deduce the requirement link between this macroscopic
	 * change and other external unit differences.
	 * 
	 * @param macro
	 *            The macroscopic change.
	 */
	private void fillRequiredDifferencesForUnitToMacro(Diff macro) {
		Set<Diff> requiredExtensions = new LinkedHashSet<Diff>();
		Set<Diff> requiringExtensions = new LinkedHashSet<Diff>();
		for (Diff refiningDiff : macro.getRefinedBy()) {
			for (Diff unit : refiningDiff.getRequires()) {
				if (unit.getRefines().isEmpty()) {
					requiredExtensions.add(unit);
				}
			}
			for (Diff unit : refiningDiff.getRequiredBy()) {
				if (unit.getRefines().isEmpty()) {
					requiringExtensions.add(unit);
				}
			}
		}
		macro.getRequires().addAll(
				Collections2.filter(requiredExtensions, EMFComparePredicates.fromSide(macro.getSource())));
		macro.getRequiredBy().addAll(
				Collections2.filter(requiringExtensions, EMFComparePredicates.fromSide(macro.getSource())));
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
	 * Get all the add and delete changes on the objects contained in the one concerned by the given
	 * difference.
	 * 
	 * @param input
	 *            The given difference.
	 * @return The found differences.
	 */
	protected Set<Diff> getAllContainedDifferences(Diff input) {
		final Set<Diff> result = new LinkedHashSet<Diff>();

		final Comparison comparison = ComparisonUtil.getComparison(input);

		CompareSwitch<EObject> valueGetter = new CompareSwitch<EObject>() {
			@Override
			public EObject caseReferenceChange(ReferenceChange object) {
				return object.getValue();
			}

			@Override
			public EObject caseResourceAttachmentChange(ResourceAttachmentChange object) {
				return MatchUtil.getContainer(ComparisonUtil.getComparison(object), object);
			}

			@Override
			public EObject defaultCase(EObject object) {
				return null;
			}
		};
		EObject value = valueGetter.doSwitch(input);

		if (value != null) {
			final Match match = comparison.getMatch(value);
			result.addAll(getAllContainedDifferences(comparison, match));
		}

		return result;
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
		return Lists.newArrayList(Iterables.filter(comparison.getDifferences(lookup), p));
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
			// Keep only unit changes...
			if (!getExtensionKind().isInstance(candidate)) {
				// ... which are not related to an other macroscopic ADD or DELETE or MOVE of a graphical
				// object.
				if (getRelatedExtensionKind(candidate) == null) {
					result.add(candidate);
				} else if (candidate instanceof ReferenceChange
						&& ((ReferenceChange)candidate).getReference().isContainment()) {
					// match of an added or deleted graphical object.
					prune.add(comparison.getMatch(((ReferenceChange)candidate).getValue()));
				}
			}
		}

		// Re-iterate the research in sub matches of expected objects.
		for (Match submatch : match.getSubmatches()) {
			if (!prune.contains(submatch)) {
				result.addAll(getAllContainedDifferences(comparison, submatch));
			}
		}

		return result;
	}

	/**
	 * Get the distinct differences refined by the given differences.
	 * 
	 * @param refiningDifferences
	 *            The refining differences.
	 * @return The set of refined differences.
	 */
	private Set<Diff> getDistinctRefinedDifferences(List<Diff> refiningDifferences) {
		Iterator<Diff> unitDiffs = refiningDifferences.iterator();
		Set<Diff> extensions = new LinkedHashSet<Diff>();
		while (unitDiffs.hasNext()) {
			Diff unitDiff = unitDiffs.next();
			extensions.addAll(unitDiff.getRefines());
		}
		return extensions;
	}

	/**
	 * This can be used to determine the kind of an extension according to an input Diff.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class DifferenceKindCompareSwitch extends CompareSwitch<DifferenceKind> {
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
	}
}
