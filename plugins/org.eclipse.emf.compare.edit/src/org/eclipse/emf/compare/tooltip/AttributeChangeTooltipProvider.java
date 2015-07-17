/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tooltip;

import static org.eclipse.emf.compare.internal.EMFCompareEditMessages.getString;
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEGet;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Attribute changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class AttributeChangeTooltipProvider extends AbstractTooltipProvider<AttributeChange> implements ITooltipLabelProvider {

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The composed adapter factory
	 */
	public AttributeChangeTooltipProvider(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryItemDelegator(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ITooltipLabelProvider#getTooltip(MergeMode)
	 */
	public String getTooltip(MergeMode mode) throws IllegalArgumentException {
		AttributeChange diff = (AttributeChange)target;
		boolean isFromLeft = isFromLeft(diff);
		String tooltip;

		switch (diff.getKind()) {
			case DELETE:
				tooltip = setDeleteNonContainmentTooltip(mode, diff, isFromLeft);
				break;
			case ADD:
				tooltip = setAddAttributeTooltip(mode, diff, isFromLeft);
				break;
			case MOVE:
				tooltip = setMovePositionTooltip(mode, diff, isFromLeft);
				break;
			case CHANGE:
				if (isUnset(diff, getChangedValueFromModel(diff))) {
					tooltip = setUnsetTooltip(mode, diff, isFromLeft);
				} else {
					tooltip = setSetTooltip(mode, diff, isFromLeft);
				}
				break;
			default:
				throw new IllegalArgumentException();
		}
		return tooltip;
	}

	/**
	 * Returns the changed value, as it is right now stored in the model, of the attribute that is affected by
	 * the given {@code diff}.
	 *
	 * @param diff
	 *            The diff to get the changed value for.
	 * @return The changed value.
	 */
	private String getChangedValueFromModel(AttributeChange diff) {
		final EAttribute attribute = diff.getAttribute();
		final EObject changedContainer;
		switch (diff.getSource()) {
			case LEFT:
				changedContainer = diff.getMatch().getLeft();
				break;
			case RIGHT:
				changedContainer = diff.getMatch().getRight();
				break;
			default:
				throw new IllegalArgumentException();
		}
		return String.valueOf(safeEGet(changedContainer, attribute));
	}

	/**
	 * Specifies whether the given {@code diff} unsets the attribute value when updating the attribute with
	 * the given {@code targetValue}.
	 *
	 * @param diff
	 *            The difference to check.
	 * @param targetValue
	 *            The value to be set.
	 * @return <code>true</code> if setting {@code targetValue} is an unset, <code>false</code> otherwise.
	 */
	private boolean isUnset(AttributeChange diff, Object targetValue) {
		final Object defaultValue = diff.getAttribute().getDefaultValue();
		return targetValue == null || targetValue.equals(defaultValue)
				|| (defaultValue == null && "".equals(targetValue)); //$NON-NLS-1$
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
	private String setAddAttributeTooltip(MergeMode mode, AttributeChange diff, boolean isFromLeft) {
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
