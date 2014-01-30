/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
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
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.MatchItemProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Specialized {@link MatchItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchItemProviderSpec extends MatchItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {
	/** The image provider used with this item provider. */
	private final OverlayImageProvider overlayProvider;

	/** The item delegator for match objects. */
	private final AdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructor calling super {@link #MatchItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchItemProviderSpec(AdapterFactory adapterFactory) {
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
		Match match = (Match)object;
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

		Object matchImage = overlayProvider.getComposedImage(match, ret);
		ret = overlayImage(object, matchImage);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		Match match = (Match)object;
		String ret = itemDelegator.getText(match.getLeft());

		if (isNullOrEmpty(ret)) {
			ret = itemDelegator.getText(match.getRight());
		}

		if (isNullOrEmpty(ret)) {
			ret = itemDelegator.getText(match.getOrigin());
		}

		if (isNullOrEmpty(ret)) {
			ret = super.getText(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 * @since 3.0
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		ComposedStyledString styledString = new ComposedStyledString(getText(object));
		return styledString;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		return getText(object);
	}
}
