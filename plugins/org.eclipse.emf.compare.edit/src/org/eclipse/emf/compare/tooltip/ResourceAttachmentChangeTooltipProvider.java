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

import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Resource Attachment changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class ResourceAttachmentChangeTooltipProvider extends AbstractTooltipProvider<ResourceAttachmentChange> implements ITooltipLabelProvider {

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The composed adapter factory
	 */
	public ResourceAttachmentChangeTooltipProvider(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryItemDelegator(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ITooltipLabelProvider#getTooltip(MergeMode)
	 */
	public String getTooltip(MergeMode mode) throws IllegalArgumentException {
		String tooltip;
		ResourceAttachmentChange diff = (ResourceAttachmentChange)target;
		boolean isFromLeft = isFromLeft(diff);

		switch (diff.getKind()) {
			case ADD:
				tooltip = addResourceAttachmentChangeTooltip(mode, diff, isFromLeft);
				break;
			case DELETE:
				tooltip = deleteResourceAttachmentChangeTooltip(mode, diff, isFromLeft);
				break;
			case MOVE:
				tooltip = setResourceLocationChangeTooltip(mode, diff, isFromLeft);
				break;
			case CHANGE:
				tooltip = setResourceLocationChangeTooltip(mode, diff, isFromLeft);
				break;
			default:
				throw new IllegalArgumentException();
		}

		return tooltip;
	}

	/**
	 * Compute the tooltip for the addition of a resource attachment change.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String addResourceAttachmentChangeTooltip(MergeMode mode, ResourceAttachmentChange diff,
			boolean isFromLeft) {
		String value = getLabel(diff);
		EObject left = diff.getMatch().getLeft();
		EObject right = diff.getMatch().getRight();
		EObject origin = diff.getMatch().getOrigin();

		String leftUri = ""; //$NON-NLS-1$
		if (left != null) {
			leftUri = left.eResource().getURI().toString();
		}

		String rightUri = ""; //$NON-NLS-1$
		if (right != null) {
			rightUri = right.eResource().getURI().toString();
		}

		String originUri = ""; //$NON-NLS-1$
		if (origin != null) {
			originUri = origin.eResource().getURI().toString();
		}

		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rac.control.left.leftToRight", value, //$NON-NLS-1$
							leftUri);
				} else {
					body = getString("ContextualTooltip.rac.control.right.leftToRight", value, //$NON-NLS-1$
							leftUri);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rac.control.left.rightToLeft", value, //$NON-NLS-1$
							rightUri);
				} else {
					body = getString("ContextualTooltip.rac.control.right.rightToLeft", value, //$NON-NLS-1$
							rightUri);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rac.control.left.accept", value, //$NON-NLS-1$
							leftUri);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.rac.control.right.accept", value, //$NON-NLS-1$
							rightUri);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					if (originUri == null || "".equals(originUri)) { //$NON-NLS-1$
						originUri = leftUri;
					}
					body = getString("ContextualTooltip.rac.control.left.reject", value, //$NON-NLS-1$
							originUri);
					tooltip = rejectAndChanged(body);
				} else {
					if (originUri == null || "".equals(originUri)) { //$NON-NLS-1$
						originUri = rightUri;
					}
					body = getString("ContextualTooltip.rac.control.right.reject", value, //$NON-NLS-1$
							originUri);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for the deletion of a resource attachment change.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String deleteResourceAttachmentChangeTooltip(MergeMode mode, ResourceAttachmentChange diff,
			boolean isFromLeft) {
		String value = getLabel(diff);
		EObject left = diff.getMatch().getLeft();
		EObject right = diff.getMatch().getRight();
		EObject origin = diff.getMatch().getOrigin();

		String leftUri = ""; //$NON-NLS-1$
		if (left != null) {
			leftUri = left.eResource().getURI().toString();
		}

		String rightUri = ""; //$NON-NLS-1$
		if (right != null) {
			rightUri = right.eResource().getURI().toString();
		}

		String originUri = ""; //$NON-NLS-1$
		if (origin != null) {
			originUri = origin.eResource().getURI().toString();
		}

		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rac.uncontrol.left.leftToRight", value, //$NON-NLS-1$
							leftUri);
				} else {
					body = getString("ContextualTooltip.rac.uncontrol.right.leftToRight", value, //$NON-NLS-1$
							leftUri);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rac.uncontrol.left.rightToLeft", value, //$NON-NLS-1$
							rightUri);
				} else {
					body = getString("ContextualTooltip.rac.uncontrol.right.rightToLeft", value, //$NON-NLS-1$
							rightUri);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rac.uncontrol.left.accept", value, //$NON-NLS-1$
							leftUri);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.rac.uncontrol.right.accept", value, //$NON-NLS-1$
							rightUri);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					if (originUri == null || "".equals(originUri)) { //$NON-NLS-1$
						originUri = rightUri;
					}
					body = getString("ContextualTooltip.rac.uncontrol.left.reject", value, //$NON-NLS-1$
							originUri);
					tooltip = rejectAndChanged(body);
				} else {
					if (originUri == null || "".equals(originUri)) { //$NON-NLS-1$
						originUri = leftUri;
					}
					body = getString("ContextualTooltip.rac.uncontrol.right.reject", value, //$NON-NLS-1$
							originUri);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
