/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ResourceAttachmentChangeItemProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Specialized {@link ResourceAttachmentChangeItemProvider} returning nice output for {@link #getText(Object)}
 * and {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class ResourceAttachmentChangeItemProviderSpec extends ResourceAttachmentChangeItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/** The image provider used with this item provider. */
	private final OverlayImageProvider overlayProvider;

	/** Item delegator for resources. */
	private final AdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructor calling super {@link #ResourceAttachmentChangeItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            The adapter factory.
	 */
	public ResourceAttachmentChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new AdapterFactoryItemDelegator(getRootAdapterFactory());
		overlayProvider = new OverlayImageProvider(getResourceLocator());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final Match match = ((ResourceAttachmentChange)object).getMatch();
		Object ret = itemDelegator.getImage(match.getLeft());
		if (ret == null) {
			ret = itemDelegator.getImage(match.getRight());
		}
		if (ret == null) {
			ret = itemDelegator.getImage(match.getOrigin());
		}
		if (ret == null) {
			ret = super.getImage(object);
		}

		Object matchImage = overlayProvider.getComposedImage((ResourceAttachmentChange)object, ret);
		ret = overlayImage(object, matchImage);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ResourceAttachmentChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/* Missing override : only for EMF 2.10 and later. Do not tag. */
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IComposedStyledString getStyledText(Object object) {
		ResourceAttachmentChange resourceAttachmentChange = (ResourceAttachmentChange)object;
		final Match match = resourceAttachmentChange.getMatch();
		String value = itemDelegator.getText(match.getLeft());
		if (isNullOrEmpty(value)) {
			value = itemDelegator.getText(match.getRight());
		}
		if (isNullOrEmpty(value)) {
			value = itemDelegator.getText(match.getOrigin());
		}
		if (isNullOrEmpty(value)) {
			value = super.getText(object);
		}

		ComposedStyledString ret = new ComposedStyledString(value);
		ret.append(" [", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		switch (resourceAttachmentChange.getKind()) {
			case ADD:
				ret.append(EMFCompareEditMessages
						.getString("ResourceAttachmentChangeItemProviderSpec.decoration.control") + ' ', //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			case DELETE:
				ret.append(EMFCompareEditMessages
						.getString("ResourceAttachmentChangeItemProviderSpec.decoration.uncontrol") + ' ', //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			case MOVE:
				ret.append(EMFCompareEditMessages
						.getString("ResourceAttachmentChangeItemProviderSpec.decoration.move") + ' ', //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			default:
				break;
		}
		ret.append(resourceAttachmentChange.getResourceURI(), Style.DECORATIONS_STYLER);

		return ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		final ResourceAttachmentChange rac = (ResourceAttachmentChange)object;
		final Match match = rac.getMatch();
		String valueText = itemDelegator.getText(match.getLeft());
		if (isNullOrEmpty(valueText)) {
			valueText = itemDelegator.getText(match.getRight());
		}
		if (isNullOrEmpty(valueText)) {
			valueText = itemDelegator.getText(match.getOrigin());
		}
		if (isNullOrEmpty(valueText)) {
			valueText = super.getText(object);
		}

		String hasBeenAndSide = EMFCompareEditMessages.getString("change.local"); //$NON-NLS-1$
		if (rac.getSource() == DifferenceSource.RIGHT) {
			hasBeenAndSide = EMFCompareEditMessages.getString("change.remote"); //$NON-NLS-1$
		}

		DifferenceKind labelValue = rac.getKind();

		final String ret;
		switch (labelValue) {
			case ADD:
				ret = EMFCompareEditMessages.getString("ResourceAttachmentChangeItemProviderSpec.added", //$NON-NLS-1$
						valueText, hasBeenAndSide);
				break;
			case DELETE:
				ret = EMFCompareEditMessages.getString("ResourceAttachmentChangeItemProviderSpec.deleted", //$NON-NLS-1$
						valueText, hasBeenAndSide);
				break;
			case MOVE:
				ret = EMFCompareEditMessages.getString("ResourceAttachmentChangeItemProviderSpec.moved", //$NON-NLS-1$
						valueText, hasBeenAndSide);
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + rac.getKind()); //$NON-NLS-1$
		}
		return ret;
	}
}
