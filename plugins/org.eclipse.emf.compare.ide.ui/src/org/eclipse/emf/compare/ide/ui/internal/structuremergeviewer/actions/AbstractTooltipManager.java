/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages.getString;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * This class defines usual methods for contextual tooltips.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractTooltipManager {

	/**
	 * The line separator used to compute tooltips.
	 */
	private static final String LINE_SEPARATOR = "\n"; //$NON-NLS-1$

	/**
	 * The adapter factory.
	 */
	protected AdapterFactory adapterFactory;

	/**
	 * The label provider.
	 */
	protected AdapterFactoryLabelProvider labelProvider;

	/**
	 * Check if the given diff is a reference change.
	 * 
	 * @param diff
	 *            The given diff
	 * @return <code>true</code> if the diff is a containment reference change <code>false</code> otherwise
	 */
	protected boolean isContainmentReferenceChange(Diff diff) {
		boolean isContainmentReference = false;
		if (diff instanceof ReferenceChange) {
			if (((ReferenceChange)diff).getReference().isContainment()) {
				isContainmentReference = true;
			}
		}
		return isContainmentReference;
	}

	/**
	 * Create the final tooltip for an accepted change which lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String acceptAndChanged(String value) {
		String accept = getString("ContextualTooltip.acceptChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftChanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for an accepted change which don't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String acceptAndUnchanged(String value) {
		String accept = getString("ContextualTooltip.acceptChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftUnchanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a rejected change which lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rejectAndChanged(String value) {
		String accept = getString("ContextualTooltip.rejectChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftChanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a rejected change which don't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rejectAndUnchanged(String value) {
		String accept = getString("ContextualTooltip.rejectChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftUnchanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a change with two editable files which don't lead to a modification of the
	 * right side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rightUnchanged(String value) {
		String modify = getString("ContextualTooltip.editable.rightUnchanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a change with two editable files which lead to a modification of the right
	 * side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rightChanged(String value) {
		String modify = getString("ContextualTooltip.editable.rightChanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Returns the label of the given <code>object</code> by adapting it to
	 * {@link ISemanticObjectLabelProvider} and asking for its
	 * {@link ISemanticObjectLabelProvider#getSemanticObjectLabel(Object) text}. Returns null if
	 * <code>object</code> is null.
	 *
	 * @param eObject
	 *            The object
	 * @return the label of the object
	 */
	protected String getLabel(EObject eObject) {
		if (eObject != null) {
			Object adapter = adapterFactory.adapt(eObject, ISemanticObjectLabelProvider.class);
			if (adapter instanceof ISemanticObjectLabelProvider) {
				return ((ISemanticObjectLabelProvider)adapter).getSemanticObjectLabel(eObject);
			}
		}
		return null;
	}

	/**
	 * Returns the label of the given <code>object</code> by using the label provider.
	 *
	 * @param eObject
	 *            The object
	 * @return the label of the object
	 */
	protected String getLabelFromObject(EObject eObject) {
		return this.labelProvider.getText(eObject);
	}
}
