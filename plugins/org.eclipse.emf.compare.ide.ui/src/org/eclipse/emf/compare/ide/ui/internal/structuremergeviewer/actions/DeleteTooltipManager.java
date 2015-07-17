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
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * This class is used to compute tooltips for DELETE modifications.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class DeleteTooltipManager extends AbstractTooltipManager {

	/**
	 * The constructor.
	 *
	 * @param adapterFactory
	 *            The given adapter factory
	 */
	public DeleteTooltipManager(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	/**
	 * This method is the entry point of tooltip creation for a DELETE mode difference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	public String setDeleteTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String tooltip;

		boolean isContainmentReference = false;
		if (diff instanceof ReferenceChange) {
			if (((ReferenceChange)diff).getReference().isContainment()) {
				isContainmentReference = true;
			}
		}

		// Two different case are handled : deletion on an element referenced by a non-containment reference,
		// deletion of an element referenced by a containment reference
		if (isContainmentReference) {
			tooltip = setDeleteContainmentTooltip(mode, diff, isFromLeft);
		} else {
			tooltip = setDeleteNonContainmentTooltip(mode, diff, isFromLeft);
		}

		return tooltip;
	}

	/**
	 * Compute the tooltip for an deletion in a non-containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setDeleteNonContainmentTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String tooltip;
		String body;

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.accept", value); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.accept", value); //$NON-NLS-1$
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.reject", value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.reject", value); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for an deletion in a containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setDeleteContainmentTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());
		String tooltip;
		String body;

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
