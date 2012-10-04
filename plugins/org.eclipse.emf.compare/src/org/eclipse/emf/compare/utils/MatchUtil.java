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
package org.eclipse.emf.compare.utils;

import static org.eclipse.emf.compare.utils.ReferenceUtil.getAsList;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This utility class holds methods that will be used by the diff and merge processes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class MatchUtil {
	/**
	 * Utility classes don't need a default constructor.
	 */
	private MatchUtil() {
		// Hides default constructor
	}

	/**
	 * Get the object which is the origin value from the given matching <code>object</code>.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param object
	 *            The given object.
	 * @return The origin value.
	 */
	public static EObject getOriginObject(Comparison comparison, EObject object) {
		EObject result = null;
		Match match = comparison.getMatch(object);
		if (match != null) {
			if (comparison.isThreeWay()) {
				result = match.getOrigin();
			} else {
				if (object == match.getLeft()) {
					result = match.getRight();
				} else {
					result = match.getLeft();
				}
			}
		}
		return result;
	}

	/**
	 * From a given mono-valued reference change, get the origin value.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The given reference change.
	 * @return The origin value.
	 */
	public static EObject getOriginValue(ReferenceChange difference) {
		if (!difference.getReference().isContainment() && !difference.getReference().isMany()
				&& difference.getKind().equals(DifferenceKind.CHANGE)) {
			EObject originContainer = getOriginContainer(difference);
			if (originContainer != null) {
				Object originValue = ReferenceUtil.safeEGet(originContainer, difference.getReference());
				if (originValue instanceof EObject) {
					return (EObject)originValue;
				}
			}
		}
		return null;
	}

	/**
	 * Get the business model object containing the given <code>difference</code> in the origin side.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The difference.
	 * @return The object.
	 */
	public static EObject getOriginContainer(Diff difference) {
		final EObject diffContainer;
		if (difference.getMatch().getComparison().isThreeWay()) {
			diffContainer = difference.getMatch().getOrigin();
		} else {
			if (getContainer(difference) == difference.getMatch().getLeft()) {
				diffContainer = difference.getMatch().getRight();
			} else {
				diffContainer = difference.getMatch().getLeft();
			}
		}
		return diffContainer;
	}

	// /**
	// * Get the business model object containing the given <code>difference</code>.
	// *
	// * @param comparison
	// * The comparison.
	// * @param difference
	// * The difference.
	// * @return The object.
	// */
	// public static EObject getContainer(Comparison comparison, ReferenceChange difference) {
	// EObject result = null;
	// Match match = difference.getMatch();
	// final DifferenceSource source = difference.getSource();
	// final DifferenceKind kind = difference.getKind();
	// switch (kind) {
	// case DELETE:
	// if (comparison.isThreeWay()) {
	// result = match.getOrigin();
	// } else {
	// result = match.getRight();
	// }
	// break;
	// case ADD:
	// // fall through
	// case MOVE:
	// if (source == DifferenceSource.LEFT) {
	// result = match.getLeft();
	// } else {
	// result = match.getRight();
	// }
	// break;
	// case CHANGE:
	// final EObject value = difference.getValue();
	// if (getAsList(match.getLeft(), difference.getReference()).contains(value)) {
	// result = match.getLeft();
	// } else if (getAsList(match.getRight(), difference.getReference()).contains(value)) {
	// result = match.getRight();
	// } else {
	// result = match.getOrigin();
	// }
	// break;
	// default:
	// // no other case for now.
	// }
	// return result;
	// }

	/**
	 * Get the business model object containing the given <code>difference</code>.
	 * 
	 * @param difference
	 *            The detected difference for which we need the actually modified object.
	 * @return The object which presents the given difference.
	 */
	public static EObject getContainer(Diff difference) {
		EObject result = null;
		final Object value = getValue(difference);
		if (value != null) {
			Match match = difference.getMatch();
			if (getAsList(match.getLeft(), getStructuralFeature(difference)).contains(value)) {
				result = match.getLeft();
			} else if (getAsList(match.getRight(), getStructuralFeature(difference)).contains(value)) {
				result = match.getRight();
			} else if (getAsList(match.getOrigin(), getStructuralFeature(difference)).contains(value)) {
				result = match.getOrigin();
			}
		} else {
			if (difference.getMatch().getLeft() != null) {
				return difference.getMatch().getLeft();
			}
			if (difference.getMatch().getRight() != null) {
				return difference.getMatch().getRight();
			}
			if (difference.getMatch().getOrigin() != null) {
				return difference.getMatch().getOrigin();
			}
		}

		return result;
	}

	/**
	 * Get the value of any difference.
	 * 
	 * @param input
	 *            The difference.
	 * @return the value of the difference.
	 */
	public static Object getValue(Diff input) {
		return new CompareSwitch<Object>() {
			@Override
			public Object caseAttributeChange(AttributeChange object) {
				return object.getValue();
			}

			@Override
			public Object caseReferenceChange(ReferenceChange object) {
				return object.getValue();
			}

		}.doSwitch(input);
	}

	/**
	 * Get the structural feature of any difference.
	 * 
	 * @param input
	 *            The difference.
	 * @return the structural feature.
	 */
	public static EStructuralFeature getStructuralFeature(Diff input) {
		return new CompareSwitch<EStructuralFeature>() {
			@Override
			public EStructuralFeature caseAttributeChange(AttributeChange object) {
				return object.getAttribute();
			}

			@Override
			public EStructuralFeature caseReferenceChange(ReferenceChange object) {
				return object.getReference();
			}

		}.doSwitch(input);
	}

}
