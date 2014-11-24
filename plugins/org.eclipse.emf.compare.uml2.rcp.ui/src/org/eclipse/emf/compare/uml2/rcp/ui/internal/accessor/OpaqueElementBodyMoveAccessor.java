/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An accessor for {@link OpaqueElementBodyChange opaque element body changes} that represent moves of
 * language/body values of {@link OpaqueAction}, {@link OpaqueBehavior}, and {@link OpaqueExpression}. For
 * moves of language/body values, we show the list of language values for the left-hand side and the
 * right-hand side.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyMoveAccessor extends ManyStructuralFeatureAccessorImpl {

	/**
	 * Creates a new accessor for {@link OpaqueElementBodyChange opaque element body changes} that represent
	 * moves of body languages.
	 * 
	 * @param adapterFactory
	 *            the adapater factory used to create the accessor.
	 * @param bodyChange
	 *            The change to be accessed by this accessor.
	 * @param side
	 *            The side of this accessor.
	 */
	public OpaqueElementBodyMoveAccessor(AdapterFactory adapterFactory, OpaqueElementBodyChange bodyChange,
			MergeViewerSide side) {
		super(adapterFactory, bodyChange, side);
		if (!DifferenceKind.MOVE.equals(bodyChange.getKind())) {
			throw new IllegalArgumentException("This accessor handles only opaque element body moves."); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.
	 *      AbstractStructuralFeatureAccessor.getAffectedFeature(Diff)
	 */
	@Override
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		/*
		 * This accessor handles only opaque element changes that represent moves. For such changes, we
		 * display the list of languages. Thus, only the language feature is of interest. We need the
		 * respective language feature depending on the type of the changed object (e.g., OpaqueBehavior). As
		 * the side does not matter, we just return the language feature of the left-hand side's object's
		 * type. Also, getMatch().getLeft() can never be null, since it would not be a move if it would have
		 * been deleted on the left-hand side.
		 */
		return getLanguageFeature(diff.getMatch().getLeft());
	}

	/**
	 * Returns the language feature of the given {@code object} depending on whether it is an
	 * {@link OpaqueAction}, {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * <p>
	 * If {@code object} is not any of those types, an {@link IllegalArgumentException} is thrown, since this
	 * must never happen and something beforehand went horribly wrong.
	 * </p>
	 * 
	 * @param object
	 *            The instance of {@link OpaqueAction}, {@link OpaqueBehavior}, or {@link OpaqueExpression} to
	 *            get the language feature for.
	 * @return The language feature of {@link #eObject}.
	 */
	private EStructuralFeature getLanguageFeature(final EObject object) {
		final EStructuralFeature languageFeature;
		if (object instanceof OpaqueAction) {
			languageFeature = UMLPackage.eINSTANCE.getOpaqueAction_Language();
		} else if (object instanceof OpaqueBehavior) {
			languageFeature = UMLPackage.eINSTANCE.getOpaqueBehavior_Language();
		} else if (object instanceof OpaqueExpression) {
			languageFeature = UMLPackage.eINSTANCE.getOpaqueExpression_Language();
		} else {
			throw new IllegalArgumentException("Cannot get language feature of the class " //$NON-NLS-1$
					+ object.eClass().getName());
		}
		return languageFeature;
	}
}
