/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Victor Roldan Betancort - [352002] introduce IMatchManager
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine.check;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.engine.IMatchManager;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.internal.DiffCollectionsHelper;
import org.eclipse.emf.compare.diff.internal.engine.CrossReferencerMatchManager;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * This provides a base implementation for the different checks that clients can need to call in specific
 * differencing engines implementations.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 * @since 1.0
 */
public abstract class AbstractCheck {
	/**
	 * Allows retrieval of the ancestor matched object.
	 * 
	 * @deprecated this is now available as a formal enum at {@link IMatchManager.MatchSide}.
	 */
	@Deprecated
	protected static final int ANCESTOR_OBJECT = 0;

	/**
	 * Allows retrieval of the left matched object.
	 * 
	 * @deprecated this is now available as a formal enum at {@link IMatchManager.MatchSide}.
	 */
	@Deprecated
	protected static final int LEFT_OBJECT = 1;

	/**
	 * Allows retrieval of the right matched object.
	 * 
	 * @deprecated this is now available as a formal enum at {@link IMatchManager.MatchSide}.
	 */
	@Deprecated
	protected static final int RIGHT_OBJECT = 2;

	/**
	 * If we're currently doing a resourceSet differencing, this will have been initialized with the whole
	 * MatchResourceSet.
	 * 
	 * @deprecated this field should no longer be used, use {@link #getMatchManager()} instead.
	 */
	@Deprecated
	protected final EcoreUtil.CrossReferencer crossReferencer;

	/**
	 * utility class to easily matches lists.
	 * 
	 * @since 1.2
	 */
	protected DiffCollectionsHelper matcherHelper;

	/**
	 * IMatchManager instance used to determine the match for an arbitrary EObject.
	 * 
	 * @see IMatchManager
	 */
	private IMatchManager matchManager;

	/**
	 * Instantiates the checker given the current crossreferencing members of the diff engine.
	 * 
	 * @param referencer
	 *            This cross referencer has been initialized with the whole MatchResourceSet and can be used
	 *            to retrieve matched EObjects towards other resources.
	 * @deprecated The CrossReferencer mechanism is now hidden behind the IMatchManager interface, use the
	 *             {@link #AbstractCheck(IMatchManager)} constructor instead.
	 */
	@Deprecated
	public AbstractCheck(EcoreUtil.CrossReferencer referencer) {
		crossReferencer = referencer;
		matchManager = new CrossReferencerMatchManager(referencer);
		matcherHelper = new DiffCollectionsHelper(matchManager);
	}

	/**
	 * Instantiates the checker given a certain IMatchManager instance.
	 * 
	 * @param manager
	 *            the IMatchManager instance which should be used to determine matches between
	 *            <code>EObject</code>s.
	 * @see IMatchManager
	 * @since 1.3
	 */
	public AbstractCheck(IMatchManager manager) {
		crossReferencer = null;
		matchManager = manager;
		matcherHelper = new DiffCollectionsHelper(manager);
	}

	/**
	 * Returns the match manager used by this checker.
	 * 
	 * @return The match manager used by this checker.
	 * @since 1.3
	 */
	protected IMatchManager getMatchManager() {
		return matchManager;
	}

	/**
	 * This will return a list containing only EObjects. This is mainly aimed at FeatureMap.Entry values.
	 * 
	 * @param input
	 *            List that is to be converted.
	 * @return A list containing only EObjects.
	 * @since 1.0
	 */
	protected final List<Object> convertFeatureMapList(List<?> input) {
		final List<Object> result = new ArrayList<Object>();
		for (final Object inputValue : input) {
			result.add(internalFindActualEObject(inputValue));
		}
		return result;
	}

	/**
	 * Return the left or right matched EObject from the one given. More specifically, this will return the
	 * left matched element if the given {@link EObject} is the right one, or the right matched element if the
	 * given {@link EObject} is either the left or the origin one.
	 * 
	 * @param from
	 *            The original {@link EObject}.
	 * @return The matched {@link EObject}.
	 * @deprecated this functionality has been encapsulated in the new {@link IMatchManager} interface.
	 */
	@Deprecated
	protected final EObject getMatchedEObject(EObject from) {
		return getMatchManager().getMatchedEObject(from);
	}

	/**
	 * Return the specified matched {@link EObject} from the one given.
	 * 
	 * @param from
	 *            The original {@link EObject}.
	 * @param side
	 *            side of the object we seek. Must be one of
	 *            <ul>
	 *            <li>{@link #ANCESTOR_OBJECT}</li>
	 *            <li>{@link #LEFT_OBJECT}</li>
	 *            <li>{@link #RIGHT_OBJECT}</li>
	 *            </ul>
	 *            .
	 * @return The matched EObject.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>side</code> is invalid.
	 * @deprecated this functionality has been encapsulated in the new {@link IMatchManager} interface.
	 */
	@Deprecated
	protected final EObject getMatchedEObject(EObject from, int side) throws IllegalArgumentException {
		IMatchManager.MatchSide matchSide;
		if (side == LEFT_OBJECT) {
			matchSide = MatchSide.LEFT;
		} else if (side == RIGHT_OBJECT) {
			matchSide = MatchSide.RIGHT;
		} else if (side == ANCESTOR_OBJECT) {
			matchSide = MatchSide.ANCESTOR;
		} else {
			throw new IllegalArgumentException(
					EMFCompareDiffMessages.getString("GenericDiffEngine.IllegalSide")); //$NON-NLS-1$
		}
		return getMatchManager().getMatchedEObject(from, matchSide);
	}

	/**
	 * Returns <code>true</code> if the given element corresponds to an UnmatchedElement, <code>false</code>
	 * otherwise.
	 * 
	 * @param element
	 *            The element for which we need to know whether it is unmatched.
	 * @return <code>true</code> if the given element corresponds to an UnmatchedElement, <code>false</code>
	 *         otherwise.
	 * @deprecated this functionality has been encapsulated in the new {@link IMatchManager} interface.
	 */
	@Deprecated
	protected final boolean isUnmatched(EObject element) {
		return getMatchManager().isUnmatched(element);
	}

	/**
	 * This will return the first value of <tt>data</tt> that is not an instance of {@link Entry}.
	 * 
	 * @param data
	 *            The object we need a valued of.
	 * @return The first value of <tt>data</tt> that is not an instance of FeatureMapEntry.
	 */
	private Object internalFindActualEObject(Object data) {
		if (data instanceof FeatureMap.Entry)
			return internalFindActualEObject(((FeatureMap.Entry)data).getValue());
		return data;
	}
}
