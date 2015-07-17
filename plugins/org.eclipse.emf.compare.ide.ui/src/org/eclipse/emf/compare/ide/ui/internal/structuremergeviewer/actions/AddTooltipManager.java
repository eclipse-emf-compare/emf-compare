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
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * This class is used to handle creation tooltips for ADD modifications.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class AddTooltipManager extends AbstractTooltipManager {

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The given adapter factory.
	 */
	public AddTooltipManager(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	/**
	 * Entry point for the computation of ADD tooltips.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	public String setAddTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String tooltip;

		boolean isContainmentReference = isContainmentReferenceChange(diff);

		// Three different case are handled : add in a non-containment reference, add in a containment
		// reference, add of an attribute
		if (diff instanceof AttributeChange) {
			tooltip = setAddAttributeTooltip(mode, diff, isFromLeft);
		} else if (isContainmentReference) {
			tooltip = setAddContainmentTooltip(mode, diff, isFromLeft);
		} else {
			tooltip = setAddNonContainmentTooltip(mode, diff, isFromLeft);
		}

		return tooltip;
	}

	/**
	 * Compute the tooltip for an addition in a non-containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setAddNonContainmentTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String tooltip;
		String body;

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.accept", value); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.accept", value); //$NON-NLS-1$
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.reject", value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.reject", value); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for an addition in a containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setAddContainmentTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());

		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.containment.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.containment.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.add.containment.right.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.add.containment.right.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for an attribute addition.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setAddAttributeTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());

		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.attribute.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.attribute.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.attribute.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.attribute.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.attribute.left.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.add.attribute.right.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.attribute.left.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.add.attribute.right.reject", value, //$NON-NLS-1$
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
