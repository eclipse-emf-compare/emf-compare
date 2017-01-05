/*******************************************************************************
 * Copyright (c) 2014, 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Obeo - Prevent mixing sides in requires/implies/refines
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.getOpaqueElementBodies;
import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.getOpaqueElementLanguages;
import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.isChangeOfOpaqueElementBodyAttribute;
import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.isChangeOfOpaqueElementLanguageAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;

/**
 * A change factory for creating {@link OpaqueElementBodyChange changes of bodies} of {@link OpaqueAction
 * opaque actions}, {@link OpaqueBehavior opaque behaviors}, and {@link OpaqueExpression opaque expressions}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class UMLOpaqueElementBodyChangeFactory extends AbstractUMLChangeFactory {

	@Override
	public boolean handles(Diff input) {
		if (input instanceof AttributeChange && !refinesOpaqueElementBodyChange(input)) {
			final AttributeChange attributeChange = (AttributeChange)input;
			if (isChangeOfBodyAttributeWithLanguage(attributeChange)
					|| isMoveOfLanguageAttributeValue(attributeChange)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Specifies whether the given {@code diff} refines an {@link OpaqueElementBodyChange}.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it refines an {@link OpaqueElementBodyChange}, <code>false</code>
	 *         otherwise.
	 */
	private boolean refinesOpaqueElementBodyChange(Diff diff) {
		for (Diff refinedDiff : diff.getRefines()) {
			if (refinedDiff instanceof OpaqueElementBodyChange) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Specifies whether the given {@code diff} is a change of a body attribute value of an
	 * {@link OpaqueAction}, {@link OpaqueBehavior}, or {@link OpaqueExpression} for which a corresponding
	 * language attribute value exists.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a change of a body value that has a language attribute value,
	 *         <code>false</code> otherwise.
	 */
	private boolean isChangeOfBodyAttributeWithLanguage(AttributeChange diff) {
		return isChangeOfOpaqueElementBodyAttribute(diff) && affectsBodyWithLanguage(diff);
	}

	/**
	 * Specifies whether the given {@code diff} affects a body value for which a language value exists or
	 * whether it affects a language value directly.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it affects a body value with a language value, <code>false</code>
	 *         otherwise.
	 */
	private boolean affectsBodyWithLanguage(AttributeChange diff) {
		return getAffectedLanguage(diff).isPresent();
	}

	/**
	 * Returns the language value of the body value that is changed by the given {@code diff} or, if
	 * {@code diff} represents a change of a language value directly, this method returns the changed language
	 * value.
	 * 
	 * @param diff
	 *            The difference to get the language value for.
	 * @return The language value.
	 */
	private Optional<String> getAffectedLanguage(AttributeChange diff) {
		Optional<String> language = Optional.absent();
		if (isChangeOfOpaqueElementBodyAttribute(diff)) {
			final EObject changedObject = getObjectContainingTheChangedValue(diff);
			final List<String> languages = getOpaqueElementLanguages(changedObject);
			final List<String> bodies = getOpaqueElementBodies(changedObject);
			final int changedIndex = bodies.indexOf(diff.getValue());
			language = safeGet(languages, changedIndex);
		} else if (isChangeOfOpaqueElementLanguageAttribute(diff)) {
			language = Optional.of((String)diff.getValue());
		}
		return language;
	}

	/**
	 * Returns the object that contains the value changed by the given {@code diff}.
	 * <p>
	 * If {@code diff} is a deletion, it will return the object that still contains the deleted value; that
	 * is, the object in the origin model or in the model of the opposite side of the deletion. Otherwise, it
	 * will return the object of the changed side.
	 * </p>
	 * 
	 * @param diff
	 *            The difference to get the object containing the changed value for.
	 * @return The object containing the changed value.
	 */
	private EObject getObjectContainingTheChangedValue(AttributeChange diff) {
		if (!DELETE.equals(diff.getKind())) {
			return getDifferenceSourceEObject(diff);
		} else {
			return getOriginalSideEObject(diff);
		}
	}

	/**
	 * Returns the object of the source side of the given {@code diff}.
	 * <p>
	 * This is the object of the side at which the given {@code diff} has been performed (e.g., where the
	 * added value has been added, the changed value has been set, etc.). Thus, in the returned object, the
	 * {@code diff} will already be effective.
	 * </p>
	 * 
	 * @param diff
	 *            The difference to get the source side for.
	 * @return The source-side object affected by the {@code diff}.
	 */
	private EObject getDifferenceSourceEObject(AttributeChange diff) {
		if (DifferenceSource.LEFT.equals(diff.getSource())) {
			return diff.getMatch().getLeft();
		} else {
			return diff.getMatch().getRight();
		}
	}

	/**
	 * Returns the object of the original side of the given {@code diff}.
	 * <p>
	 * This is the object affected by {@code diff} of the original side; that is, either the origin in a
	 * three-way scenario or the opposite of the difference source side in a two-way scenario.
	 * </p>
	 * 
	 * @param diff
	 *            The difference to get the original-side object for.
	 * @return The original-side object affected by the {@code diff}.
	 */
	private EObject getOriginalSideEObject(AttributeChange diff) {
		final EObject changedObject;
		if (diff.getMatch().getComparison().isThreeWay()) {
			changedObject = diff.getMatch().getOrigin();
		} else {
			if (DifferenceSource.RIGHT.equals(diff.getSource())) {
				changedObject = diff.getMatch().getLeft();
			} else {
				changedObject = diff.getMatch().getRight();
			}
		}
		return changedObject;
	}

	/**
	 * Returns the {@link Optional optional} value at {@code index} from the given {@code list}.
	 * <p>
	 * If the index is out of bounds or -1, this method will return {@link Optional#absent()}.
	 * </p>
	 * 
	 * @param list
	 *            The list to get the value from.
	 * @param index
	 *            The index to get from the list.
	 * @return The value at index, or {@link Optional#absent()} if index is out of bounds or -1.
	 */
	private Optional<String> safeGet(final List<String> list, final int index) {
		final Optional<String> item;
		if (index != -1 && list.size() > index) {
			item = Optional.of(list.get(index));
		} else {
			item = Optional.absent();
		}
		return item;
	}

	/**
	 * Specifies whether the given {@code diff} represents a move of a value of a language attribute.
	 * 
	 * @param diff
	 *            The {@code diff} to check.
	 * @return <code>true</code> if it is a move of a value in a language attribute.
	 */
	private boolean isMoveOfLanguageAttributeValue(AttributeChange diff) {
		return MOVE.equals(diff.getKind()) && isChangeOfOpaqueElementLanguageAttribute(diff);
	}

	@Override
	public Diff create(Diff input) {
		final OpaqueElementBodyChange extension = (OpaqueElementBodyChange)super.create(input);

		// getAffectedLanguage must yield a value at this point, otherwise we wouldn't have
		// returned true when handle was called
		extension.setLanguage(getAffectedLanguage((AttributeChange)input).get());
		extension.setKind(computeDifferenceKind(extension));

		// remove conflict from extension that has been inferred from refined differences (cf.
		// AbstractChangeFactory#create()) because we add specific conflicts for OpaqueElementBodyChanges
		// later in the post processor
		extension.setConflict(null);

		return extension;
	}

	/**
	 * Determines the difference kind of the given {@code bodyChange} based on its refining differences.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to get its difference kind.
	 * @return The difference kind of {@code bodyChange}.
	 */
	private DifferenceKind computeDifferenceKind(OpaqueElementBodyChange bodyChange) {
		final DifferenceKind differenceKind;
		final List<Diff> refiningDiffs = bodyChange.getRefinedBy();
		if (all(refiningDiffs, ofKind(ADD))) {
			differenceKind = ADD;
		} else if (all(refiningDiffs, ofKind(DELETE))) {
			differenceKind = DELETE;
		} else if (all(refiningDiffs, ofKind(MOVE))) {
			differenceKind = MOVE;
		} else {
			differenceKind = CHANGE;
		}
		return differenceKind;
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)extension;
		if (refiningDiff.getSource() == bodyChange.getSource()) {
			bodyChange.getRefinedBy().add(refiningDiff);
			collectAndAddOtherRefiningDiffs(bodyChange, refiningDiff);
		}
	}

	/**
	 * Collects all {@link #isRefiningDiff(OpaqueElementBodyChange) refining differences} for the given
	 * {@code bodyChange} from all differences of the match of the {@code bodyChange} and adds it to the
	 * {@link Diff#getRefinedBy() refining differences} of {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to collect the refining changes for.
	 * @param refiningDiff
	 *            The difference that is refining {@code bodyChange}.
	 */
	private void collectAndAddOtherRefiningDiffs(final OpaqueElementBodyChange bodyChange,
			Diff refiningDiff) {
		final RefinementCollector collector = new RefinementCollector((AttributeChange)refiningDiff);
		Iterable<Diff> collectedRefiningDiffs = collector.collect();
		for (Diff otherRefiningDiff : collectedRefiningDiffs) {
			if (otherRefiningDiff.getSource() == bodyChange.getSource()) {
				bodyChange.getRefinedBy().add(otherRefiningDiff);
			}
		}
	}

	/**
	 * A collector for obtaining the refining differences of an {@link OpaqueElementBodyChange} based on a
	 * reference differences.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class RefinementCollector {
		/** The reference difference. */
		private final AttributeChange referenceDiff;

		/** The language of the reference difference. */
		private String language;

		/**
		 * Creates a new collector for the given {@code referenceDiff}.
		 * 
		 * @param referenceDiff
		 *            The reference difference that is used to collect the other refining differences.
		 */
		RefinementCollector(AttributeChange referenceDiff) {
			this.referenceDiff = referenceDiff;
			this.language = getLanguage(referenceDiff).get();
		}

		/**
		 * Collects all refining differences from all differences of the match of the reference difference.
		 * 
		 * @return The refining differences that have been selected based on the given reference difference.
		 */
		public Iterable<Diff> collect() {
			final EList<Diff> differencesOfMatch = referenceDiff.getMatch().getDifferences();
			return filter(differencesOfMatch, isRefiningDiff());
		}

		/**
		 * Returns a predicate determining whether a {@link Diff} is a difference that refines
		 * {@link #referenceDiff}.
		 * 
		 * @return The predicate that can be used for checking whether a {@link Diff} refines
		 *         {@link #referenceDiff}.
		 */
		private Predicate<Diff> isRefiningDiff() {
			return new Predicate<Diff>() {
				public boolean apply(Diff diff) {
					return diff instanceof AttributeChange
							&& isRefiningAttributeChange((AttributeChange)diff);
				}
			};
		}

		/**
		 * Specifies whether {@code attributeChange} is a refinement of an {@link OpaqueElementBodyChange}
		 * that is refined by the {@link #referenceDiff}.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to check.
		 * @return <code>true</code> if {@code attributeChange} is a refinement with respect to
		 *         {@link #referenceDiff}, <code>false</code> otherwise.
		 */
		private boolean isRefiningAttributeChange(AttributeChange attributeChange) {
			return isChangeOfBodyOrLanguageAttribute(attributeChange) //
					&& isOnSameSide(referenceDiff, attributeChange) //
					&& concernsSameObjectAndLanguage(attributeChange) //
					&& isCorrespondingChangeType(attributeChange);
		}

		/**
		 * Specifies whether two differences, {@code diff1} and {@code diff2} have the same difference source.
		 * 
		 * @param diff1
		 *            The first difference to check.
		 * @param diff2
		 *            The second difference to check.
		 * @return <code>true</code> if they have the same difference source, <code>false</code> otherwise.
		 */
		private boolean isOnSameSide(final Diff diff1, Diff diff2) {
			return diff1.getSource().equals(diff2.getSource());
		}

		/**
		 * Specifies whether {@code attributeChange} affects the same object and the same language as
		 * {@link #referenceDiff}.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to check.
		 * @return <code>true</code> if {@code attributeChange} concern the same object and language as
		 *         {@link #referenceDiff} does, <code>false</code> otherwise.
		 */
		private boolean concernsSameObjectAndLanguage(AttributeChange attributeChange) {
			return concernsSameObject(attributeChange) && concernsSameLanguage(attributeChange);
		}

		/**
		 * Specifies whether {@code attributeChange} affects the same container object as
		 * {@link #referenceDiff}.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to check.
		 * @return <code>true</code> if {@code attributeChange} concerns the same container object as
		 *         {@link #referenceDiff}, <code>false</code> otherwise.
		 */
		private boolean concernsSameObject(AttributeChange attributeChange) {
			return referenceDiff.getMatch().equals(attributeChange.getMatch());
		}

		/**
		 * Specifies whether {@code attributeChange} concerns the same language as {@link #referenceDiff}.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to check.
		 * @return <code>true</code> if {@code attributeChange} concern the same language, <code>false</code>
		 *         otherwise.
		 */
		private boolean concernsSameLanguage(AttributeChange attributeChange) {
			return language.equals(getLanguage(attributeChange).get());
		}

		/**
		 * Returns the {@link Optional optional} language of the given {@code attributeChange}.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to get the language for.
		 * @return The language of the {@code attributeChange} or {@link Optional#absent()} if not available.
		 */
		private Optional<String> getLanguage(AttributeChange attributeChange) {
			final Optional<String> languageOfAttributeChange;
			if (isChangeOfOpaqueElementBodyAttribute(attributeChange)) {
				languageOfAttributeChange = getAffectedLanguage(attributeChange);
			} else if (isChangeOfOpaqueElementLanguageAttribute(attributeChange)) {
				languageOfAttributeChange = Optional.of((String)attributeChange.getValue());
			} else {
				languageOfAttributeChange = Optional.absent();
			}
			return languageOfAttributeChange;
		}

		/**
		 * Specifies whether {@code attributeChange} has a change types (i.e., add, delete, move, change) that
		 * corresponds to the one of {@link #referenceDiff}. The change types correspond either if both are
		 * moves or if both are anything else except for moves.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to check.
		 * @return <code>true</code> if the change types correspond to each other, <code>false</code>
		 *         otherwise.
		 */
		private boolean isCorrespondingChangeType(AttributeChange attributeChange) {
			final boolean isCorrespondingChangeType;
			if (MOVE.equals(referenceDiff.getKind())) {
				isCorrespondingChangeType = MOVE.equals(attributeChange.getKind());
			} else {
				isCorrespondingChangeType = !MOVE.equals(attributeChange.getKind());
			}
			return isCorrespondingChangeType;
		}

		/**
		 * Specifies whether the given {@code attributeChange} is either a change of the body attribute or a
		 * change of the language attribute of an {@link OpaqueAction}, {@link OpaqueBehavior}, or
		 * {@link OpaqueExpression}.
		 * 
		 * @param attributeChange
		 *            The {@link AttributeChange} to check.
		 * @return <code>true</code> if it is a body change or a language change, <code>false</code>
		 *         otherwise.
		 */
		private boolean isChangeOfBodyOrLanguageAttribute(AttributeChange attributeChange) {
			return isChangeOfOpaqueElementBodyAttribute(attributeChange)
					|| isChangeOfOpaqueElementLanguageAttribute(attributeChange);
		}

	}

	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return OpaqueElementBodyChange.class;
	}

	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createOpaqueElementBodyChange();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected EObject getDiscriminant(Diff input) {
		return find(getDiscriminants(input), or(instanceOf(OpaqueAction.class),
				instanceOf(OpaqueBehavior.class), instanceOf(OpaqueExpression.class)), null);
	}

	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new DiscriminantsGetter() {
			@Override
			public Set<EObject> caseOpaqueAction(OpaqueAction object) {
				return getObjectAsSet(object);
			}

			@Override
			public Set<EObject> caseOpaqueBehavior(OpaqueBehavior object) {
				return getObjectAsSet(object);
			}

			@Override
			public Set<EObject> caseOpaqueExpression(OpaqueExpression object) {
				return getObjectAsSet(object);
			}

			private Set<EObject> getObjectAsSet(EObject object) {
				Set<EObject> result = new LinkedHashSet<EObject>();
				result.add(object);
				return result;
			}
		};
	}

}
