/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer (EclipseSource) - bug 488618
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.compare.provider.ResourceLocationChangeItemProvider;
import org.eclipse.emf.compare.provider.SafeAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Specialized {@link ResourceLocationChangeItemProvider} returning nice output for {@link #getText(Object)}
 * and {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class ResourceLocationChangeItemProviderSpec extends ResourceLocationChangeItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider, ISemanticObjectLabelProvider {

	/** Unsupported string constant. */
	private static final String UNSUPPORTED = "Unsupported "; //$NON-NLS-1$

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
	public ResourceLocationChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new SafeAdapterFactoryItemDelegator(getRootAdapterFactory());
		overlayProvider = new OverlayImageProvider(getResourceLocator());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final Object container = ((ResourceLocationChange)object).eContainer();
		Object ret = itemDelegator.getImage(container);
		if (ret == null) {
			ret = super.getImage(object);
		}

		Object matchImage = overlayProvider.getComposedImage((ResourceLocationChange)object, ret);
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public IComposedStyledString getStyledText(Object object) {
		final ResourceLocationChange resourceLocationChange = (ResourceLocationChange)object;
		final String baseLocation = resourceLocationChange.getBaseLocation();
		final String changedLocation = resourceLocationChange.getChangedLocation();

		ComposedStyledString ret = new ComposedStyledString(baseLocation);
		ret.append(" [", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		switch (resourceLocationChange.getKind()) {
			case CHANGE:
				ret.append(
						EMFCompareEditMessages.getString(
								"ResourceNameChangeItemProviderSpec.text.locationChanged", changedLocation), //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			default:
				break;
		}

		return ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider#getSemanticObjectLabel(java.lang.Object)
	 * @since 4.2
	 */
	public String getSemanticObjectLabel(Object object) {
		final ResourceLocationChange resourceLocationChange = (ResourceLocationChange)object;
		return resourceLocationChange.getBaseLocation();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		final ResourceLocationChange resourceLocationChange = (ResourceLocationChange)object;
		final String baseLocation = resourceLocationChange.getBaseLocation();
		final String changedLocation = resourceLocationChange.getChangedLocation();

		String hasBeenAndSide = EMFCompareEditMessages.getString("change.local"); //$NON-NLS-1$
		if (resourceLocationChange.getSource() == DifferenceSource.RIGHT) {
			hasBeenAndSide = EMFCompareEditMessages.getString("change.remote"); //$NON-NLS-1$
		}

		DifferenceKind labelValue = resourceLocationChange.getKind();
		final String ret;
		switch (labelValue) {
			case CHANGE:
				ret = EMFCompareEditMessages.getString(
						"ResourceNameChangeItemProviderSpec.description.locationChanged", //$NON-NLS-1$
						baseLocation, hasBeenAndSide, changedLocation);
				break;
			default:
				throw new IllegalStateException(UNSUPPORTED + DifferenceKind.class.getSimpleName()
						+ " value: " + resourceLocationChange.getKind()); //$NON-NLS-1$
		}
		return ret;
	}
}
