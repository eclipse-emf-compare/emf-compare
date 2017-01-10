/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
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

import static com.google.common.base.Strings.isNullOrEmpty;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.match.MatchOfContainmentReferenceChangeAdapter;
import org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.compare.provider.MatchItemProvider;
import org.eclipse.emf.compare.provider.SafeAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Specialized {@link MatchItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchItemProviderSpec extends MatchItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider, ISemanticObjectLabelProvider {
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
		Match match = (Match)object;
		Object ret = itemDelegator.getImage(match.getLeft());

		if (ret == null) {
			ret = itemDelegator.getImage(match.getOrigin());
		}

		if (ret == null) {
			ret = itemDelegator.getImage(match.getRight());
		}

		if (ret == null && match instanceof NotLoadedFragmentMatch) {
			ret = itemDelegator.getImage(((NotLoadedFragmentMatch)match).getResource());
		}

		if (ret == null) {
			Adapter adapter = EcoreUtil.getAdapter(match.eAdapters(),
					MatchOfContainmentReferenceChangeAdapter.class);
			if (adapter instanceof MatchOfContainmentReferenceChangeAdapter) {
				ReferenceChange referenceChange = ((MatchOfContainmentReferenceChangeAdapter)adapter)
						.getReferenceChange();
				if (referenceChange != null) {
					ret = itemDelegator.getImage(referenceChange.getValue());
				}
			}
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
			ret = itemDelegator.getText(match.getOrigin());
		}

		if (isNullOrEmpty(ret)) {
			ret = itemDelegator.getText(match.getRight());
		}

		if (isNullOrEmpty(ret)) {
			if (match instanceof NotLoadedFragmentMatch) {
				ret = "[ ... ]"; //$NON-NLS-1$
				String name = ((NotLoadedFragmentMatch)match).getName();
				if (!isNullOrEmpty(name)) {
					ret += " (" + name + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				Adapter matchAdapter = EcoreUtil.getAdapter(match.eAdapters(),
						MatchOfContainmentReferenceChangeAdapter.class);
				if (matchAdapter instanceof MatchOfContainmentReferenceChangeAdapter) {
					ReferenceChange referenceChange = ((MatchOfContainmentReferenceChangeAdapter)matchAdapter)
							.getReferenceChange();
					Adapter rcAdapter = null;
					if (referenceChange != null) {
						rcAdapter = EcoreUtil.getAdapter(referenceChange.eAdapters(),
								ReferenceChangeItemProviderSpec.class);
					}
					if (rcAdapter instanceof ReferenceChangeItemProviderSpec) {
						ret = ((ReferenceChangeItemProviderSpec)rcAdapter).getValueText(referenceChange);
					}
				}
				if (isNullOrEmpty(ret)) {
					ret = super.getText(object);
				}
			}
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 * @since 3.0
	 */
	@Override
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		ComposedStyledString styledString = new ComposedStyledString(getText(object));
		return styledString;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider#getSemanticObjectLabel(java.lang.Object)
	 * @since 4.2
	 */
	public String getSemanticObjectLabel(Object object) {
		Match match = (Match)object;
		String ret = itemDelegator.getText(match.getLeft());

		if (isNullOrEmpty(ret)) {
			ret = itemDelegator.getText(match.getOrigin());
		}

		if (isNullOrEmpty(ret)) {
			ret = itemDelegator.getText(match.getRight());
		}
		return ret;
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
