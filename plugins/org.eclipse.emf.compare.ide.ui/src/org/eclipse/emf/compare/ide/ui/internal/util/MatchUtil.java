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
package org.eclipse.emf.compare.ide.ui.internal.util;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchUtil {

	public static enum Side {
		LEFT, RIGHT, ANCESTOR;

		public static Side convert(MergeViewerSide mvs) {
			switch (mvs) {
				case LEFT:
					return Side.LEFT;
				case RIGHT:
					return Side.RIGHT;
				case ANCESTOR:
					return Side.ANCESTOR;
				default:
					throw new IllegalStateException();
			}
		}
	}

	public static Match getMatchOfInterrest(Diff diff, Side side) {
		final Match ret;
		if (diff instanceof ReferenceChange) {
			ret = getMatchOfInterrest((ReferenceChange)diff, side);
		} else if (diff instanceof AttributeChange) {
			ret = getMatchOfInterrest((AttributeChange)diff, side);
		} else {
			ret = diff.getMatch();
		}
		return ret;
	}

	/**
	 * @param side
	 */
	public static Match getMatchOfInterrest(AttributeChange change, Side side) {
		return change.getMatch();
	}

	public static Match getMatchOfInterrest(ReferenceChange change, Side side) {
		final Match ret;
		Match ownerMatch = change.getMatch();
		Comparison comparison = ownerMatch.getComparison();

		final EObject value;
		if (change.getKind() == DifferenceKind.CHANGE) {
			if (change.getReference().isMany()) {
				throw new IllegalStateException("We are assuming that ReferenceChange of kind " + //$NON-NLS-1$
						"CHANGE are for mono-valued reference only"); //$NON-NLS-1$
			}
			EReference reference = change.getReference();
			final EObject eObject;
			switch (side) {
				case LEFT:
					eObject = change.getMatch().getLeft();
					break;
				case RIGHT:
					eObject = change.getMatch().getRight();
					break;
				case ANCESTOR:
					eObject = change.getMatch().getOrigin();
					break;
				default:
					throw new IllegalStateException(
							"No other side than LEFT, RIGHT and ANCESTOR is supported"); //$NON-NLS-1$
			}
			if (eObject != null) {
				value = (EObject)eObject.eGet(reference);
			} else {
				value = null;
			}
		} else {
			value = change.getValue();
		}

		if (value != null) {
			Match matchOfValue = comparison.getMatch(value);
			if (matchOfValue != null) {
				ret = matchOfValue;
			} else {
				ret = ownerMatch;
			}
		} else {
			EReference reference = change.getReference();
			Match match = CompareFactory.eINSTANCE.createMatch();
			EObject left = ownerMatch.getLeft();
			if (left != null) {
				match.setLeft((EObject)left.eGet(reference));
			}
			EObject right = ownerMatch.getRight();
			if (right != null) {
				match.setRight((EObject)right.eGet(reference));
			}
			EObject origin = ownerMatch.getOrigin();
			if (origin != null) {
				match.setOrigin((EObject)origin.eGet(reference));
			}
			ret = match;
		}
		return ret;
	}
}
