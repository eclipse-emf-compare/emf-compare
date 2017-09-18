/*******************************************************************************
 * Copyright (c) 2015, 2018 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 514079
 *******************************************************************************/
package org.eclipse.emf.compare.tooltip;

import static org.eclipse.emf.compare.internal.EMFCompareEditMessages.getString;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Resource Attachment changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class ResourceAttachmentChangeTooltipProvider extends AbstractTooltipProvider<ResourceAttachmentChange> {

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

		String leftUri = getResourceUri(left);
		String rightUri = getResourceUri(right);
		String originUri = getResourceUri(origin);

		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		String effectiveLeft;
		String effectiveRight;
		switch (mode) {
			case LEFT_TO_RIGHT:
				effectiveLeft = getDirectionalValue(mirrored, leftUri, rightUri);
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.rac.control.left.leftToRight", value, //$NON-NLS-1$
							effectiveLeft);
				} else {
					body = getString("ContextualTooltip.rac.control.right.leftToRight", value, //$NON-NLS-1$
							effectiveLeft);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				effectiveRight = getDirectionalValue(mirrored, rightUri, leftUri);
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.rac.control.left.rightToLeft", value, //$NON-NLS-1$
							effectiveRight);
				} else {
					body = getString("ContextualTooltip.rac.control.right.rightToLeft", value, //$NON-NLS-1$
							effectiveRight);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.control.left.accept"), //$NON-NLS-1$
							value, leftUri);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.control.right.accept"), //$NON-NLS-1$
							value, rightUri);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					if (originUri.isEmpty()) {
						originUri = leftUri;
					}
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.control.left.reject"), //$NON-NLS-1$
							value, originUri);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					if (originUri.isEmpty()) {
						originUri = rightUri;
					}
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.control.right.reject"), //$NON-NLS-1$
							value, originUri);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
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

		String leftUri = getResourceUri(left);
		String rightUri = getResourceUri(right);

		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		String effectiveLeft;
		String effectiveRight;
		switch (mode) {
			case LEFT_TO_RIGHT:
				effectiveLeft = getDirectionalValue(mirrored, leftUri, rightUri);
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.rac.uncontrol.left.leftToRight", value, //$NON-NLS-1$
							effectiveLeft);
				} else {
					body = getString("ContextualTooltip.rac.uncontrol.right.leftToRight", value, //$NON-NLS-1$
							effectiveLeft);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				effectiveRight = getDirectionalValue(mirrored, rightUri, leftUri);
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.rac.uncontrol.left.rightToLeft", value, //$NON-NLS-1$
							effectiveRight);
				} else {
					body = getString("ContextualTooltip.rac.uncontrol.right.rightToLeft", value, //$NON-NLS-1$
							effectiveRight);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.uncontrol.left.accept"), //$NON-NLS-1$
							value, leftUri);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.uncontrol.right.accept"), //$NON-NLS-1$
							value, rightUri);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				String originUri = getResourceUri(origin);
				if (isFromLeft != isLeftToRight != mirrored) {
					if (originUri.isEmpty()) {
						originUri = rightUri;
					}
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.uncontrol.left.reject"), //$NON-NLS-1$
							value, originUri);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					if (originUri.isEmpty()) {
						originUri = leftUri;
					}
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.rac.uncontrol.right.reject"), //$NON-NLS-1$
							value, originUri);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Provides a String representation of the given object's resource URI, never <code>null</code>.
	 * 
	 * @param o
	 *            The EObject we want the resource URI of
	 * @return A String representation of the given object's resource URI, or an empty String. Never
	 *         <code>null</code>.
	 */
	private String getResourceUri(EObject o) {
		if (o != null) {
			Resource resource = o.eResource();
			if (resource != null) {
				URI uri = resource.getURI();
				if (uri != null) {
					return uri.toString();
				}
			}
		}
		return ""; //$NON-NLS-1$
	}

}
