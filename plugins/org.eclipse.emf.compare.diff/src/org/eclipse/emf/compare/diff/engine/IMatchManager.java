/*******************************************************************************
 * Copyright (c) 2011 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Victor Roldan Betancort - [352002] initial API and implementation     
 *     Obeo
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine;

import org.eclipse.emf.ecore.EObject;

/**
 * An utility interface to encapsulate general purpose operations over the match MatchModel.
 * 
 * @author Victor Roldan Betancort
 * @since 1.3
 */
public interface IMatchManager {
	/**
	 * Return the left or right matched EObject from the one given. More specifically, this will return the
	 * left matched element if the given {@link EObject} is the right one, or the right matched element if the
	 * given {@link EObject} is either the left or the origin one.
	 * 
	 * @param from
	 *            The original {@link EObject}.
	 * @return The matched {@link EObject}.
	 */
	EObject getMatchedEObject(EObject from);

	/**
	 * Return the specified matched {@link EObject} from the one given.
	 * 
	 * @param from
	 *            The original {@link EObject}.
	 * @param side
	 *            side of the object we seek.
	 * @return The matched EObject.
	 */
	EObject getMatchedEObject(EObject from, MatchSide side);

	/**
	 * This will check whether the given EObject was part of this comparison's scope.
	 * 
	 * @param eObj
	 *            EObject to check.
	 * @return <code>true</code> if that EObject is in the scope, <code>false</code> otherwise.
	 */
	boolean isInScope(EObject eObj);

	/**
	 * Returns <code>true</code> if the given element corresponds to an UnmatchedElement, <code>false</code>
	 * otherwise.
	 * 
	 * @param element
	 *            The element for which we need to know whether it is unmatched.
	 * @return <code>true</code> if the given element corresponds to an UnmatchedElement, <code>false</code>
	 *         otherwise.
	 */
	boolean isUnmatched(EObject element);

	/**
	 * Indicates the actual side of a matched <code>EObject</code>, where it is always placed either at the
	 * LEFT side or at the RIGHT side. Additionally, the ANCESTOR value is included for three-way delta
	 * 
	 * @author vroldan
	 */
	public enum MatchSide {
		/**
		 * Indicates the left side of a matched element.
		 */
		LEFT,

		/**
		 * Indicates the right side of a matched element.
		 */
		RIGHT,

		/**
		 * Indicates the ancestor side of a three-way matched element.
		 */
		ANCESTOR
	}
}
