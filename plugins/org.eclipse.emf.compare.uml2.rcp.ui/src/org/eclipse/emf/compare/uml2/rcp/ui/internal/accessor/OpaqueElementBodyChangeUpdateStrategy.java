/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.IModelUpdateStrategy;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * The model update strategy for {@link OpaqueElementBodyChange opaque element body changes}.
 * <p>
 * As this update strategy is intended to be used only by the EMFCompareTextMergeViewer (or subclasses) for
 * changes of body values, this update strategy copes only with actual changes of body values where the
 * EMFCompareTextMergeViewer is used (i.e., for changes, deletions, and additions; not for moves). In case of
 * additions or deletions, only the side, in which the body is still present, is editable. The other side, on
 * which the body is not present anymore or not yet, is not editable.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyChangeUpdateStrategy implements IModelUpdateStrategy {

	/** The datatype of the body EAttribute of OpaqueAction (expression and behavior have the same). */
	private static final EDataType BODY_ATTRIBUTE_TYPE = UMLPackage.eINSTANCE.getOpaqueAction_Body()
			.getEAttributeType();

	/**
	 * {@inheritDoc}
	 * 
	 * @see IModelUpdateStrategy#canUpdate(Diff, MergeViewerSide)
	 */
	public boolean canUpdate(Diff diff, MergeViewerSide side) {
		if (diff instanceof OpaqueElementBodyChange) {
			final EObject targetObject = MergeViewerUtil.getEObject(diff.getMatch(), side);
			final List<String> languages = UMLCompareUtil.getOpaqueElementLanguages(targetObject);
			final List<String> bodies = UMLCompareUtil.getOpaqueElementBodies(targetObject);
			final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)diff;
			return languages.contains(bodyChange.getLanguage())
					&& languages.indexOf(bodyChange.getLanguage()) < bodies.size();
		}
		return false;
	}

	/**
	 * Returns the body value from the underlying model.
	 * 
	 * @param bodyChange
	 *            The change indicating the language at which the body value shall be obtained.
	 * @param side
	 *            The side from which to obtain the value.
	 * @return The body value; if the body value does not exist, this method returns an empty string.
	 */
	private String getOldBodyValue(OpaqueElementBodyChange bodyChange, MergeViewerSide side) {
		final EObject targetObject = MergeViewerUtil.getEObject(bodyChange.getMatch(), side);
		final int index = getIndexOfLanguage(bodyChange.getLanguage(), targetObject);
		final List<String> bodies = UMLCompareUtil.getOpaqueElementBodies(targetObject);
		if (index >= 0 && index < bodies.size()) {
			return bodies.get(index);
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Returns the index of the given {@code language} of the given {@code object}.
	 * 
	 * @param language
	 *            The language to get the index of.
	 * @param object
	 *            The object to get all language values from.
	 * @return The index of {@code language}; -1 if {@code language} does not exist.
	 */
	private int getIndexOfLanguage(String language, final EObject object) {
		final List<String> languages = UMLCompareUtil.getOpaqueElementLanguages(object);
		return languages.indexOf(language);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IModelUpdateStrategy#getModelUpdateCommand(Diff, Object, MergeViewerSide)
	 */
	public Command getModelUpdateCommand(final Diff diff, final Object newValue, final MergeViewerSide side) {
		final EObject targetObject = MergeViewerUtil.getEObject(diff.getMatch(), side);
		return new ChangeCommand(targetObject) {
			@Override
			public boolean canExecute() {
				return canUpdate(diff, side) && needsUpdate(diff, newValue, side) && super.canExecute();
			}

			@Override
			protected void doExecute() {
				final List<String> bodies = UMLCompareUtil.getOpaqueElementBodies(targetObject);
				final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)diff;
				final int index = getIndexOfLanguage(bodyChange.getLanguage(), targetObject);
				final String newStringValue = EcoreUtil.convertToString(BODY_ATTRIBUTE_TYPE, newValue);
				bodies.set(index, newStringValue);
			}
		};
	}

	/**
	 * Specifies whether the value in the model needs to be updated with the given {@code newValue} on the
	 * given {@code side}.
	 * 
	 * @param diff
	 *            The diff acting as context of the potential model update.
	 * @param newValue
	 *            The potentially changed new value to be checked against.
	 * @param side
	 *            The side to check.
	 * @return <code>true</code> if an update is necessary, <code>false</code> otherwise.
	 */
	private boolean needsUpdate(Diff diff, Object newValue, MergeViewerSide side) {
		final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)diff;
		final String oldValue = getOldBodyValue(bodyChange, side);
		final IEqualityHelper equalityHelper = ComparisonUtil.getComparison(diff).getEqualityHelper();
		return !equalityHelper.matchingAttributeValues(newValue, oldValue);
	}
}
