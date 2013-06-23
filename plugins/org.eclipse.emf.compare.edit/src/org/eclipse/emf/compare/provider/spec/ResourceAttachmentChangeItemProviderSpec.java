/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ResourceAttachmentChangeItemProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;

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

	/**
	 * Constructor calling super {@link #ResourceAttachmentChangeItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            The adapter factory.
	 */
	public ResourceAttachmentChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
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
		Object ret = AdapterFactoryUtil.getImage(getRootAdapterFactory(), match.getLeft());
		if (ret == null) {
			ret = AdapterFactoryUtil.getImage(getRootAdapterFactory(), match.getRight());
		}
		if (ret == null) {
			ret = AdapterFactoryUtil.getImage(getRootAdapterFactory(), match.getOrigin());
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IComposedStyledString getStyledText(Object object) {
		ResourceAttachmentChange resourceAttachmentChange = (ResourceAttachmentChange)object;
		final Match match = resourceAttachmentChange.getMatch();
		String value = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getLeft());
		if (value == null) {
			value = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getRight());
		}
		if (value == null) {
			value = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getOrigin());
		}
		if (value == null) {
			value = super.getText(object);
		}

		ComposedStyledString ret = new ComposedStyledString(value);
		ret.append(" [", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		switch (resourceAttachmentChange.getKind()) {
			case ADD:
				ret.append("controlled in ", Style.DECORATIONS_STYLER);
				break;
			case DELETE:
				ret.append("uncontrolled from ", Style.DECORATIONS_STYLER);
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
		String ret = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getLeft());
		if (ret == null) {
			ret = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getRight());
		}
		if (ret == null) {
			ret = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getOrigin());
		}
		if (ret == null) {
			ret = super.getText(object);
		}

		String remotely = "";
		if (rac.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely ";
		}

		DifferenceKind labelValue = rac.getKind();
		final String hasBeen = " has been ";

		switch (labelValue) {
			case ADD:
				ret += hasBeen + remotely + "added to resource contents";
				break;
			case DELETE:
				ret += hasBeen + remotely + "deleted from resource contents";
				break;
			case MOVE:
				ret += hasBeen + remotely + "moved in resource contents";
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + rac.getKind());
		}
		return ret;
	}
}
