/*******************************************************************************
 * Copyright (c) 2014 Obeo.
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.FeatureMapChangeItemProvider;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Specialized {@link FeatureMapChangeItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class FeatureMapChangeItemProviderSpec extends FeatureMapChangeItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/** The elide length. */
	private static final int ELIDE_LENGTH = 50;

	/** The image provider used with this item provider. */
	private final OverlayImageProvider overlayProvider;

	/** The item delegator for feature map change values. */
	private final AdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructs an FeatureMapChangeItemProviderSpec with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public FeatureMapChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new AdapterFactoryItemDelegator(getRootAdapterFactory());
		overlayProvider = new OverlayImageProvider(getResourceLocator());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.FeatureMapChangeItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		FeatureMapChange featureMapChange = (FeatureMapChange)object;
		FeatureMap.Entry value = (FeatureMap.Entry)featureMapChange.getValue();
		Object featureMapChangeValueImage = itemDelegator.getImage(value.getValue());

		if (featureMapChangeValueImage == null) {
			featureMapChangeValueImage = super.getImage(object);
		}

		Object diffImage = overlayProvider.getComposedImage(featureMapChange, featureMapChangeValueImage);
		Object ret = overlayImage(object, diffImage);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.FeatureMapChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/**
	 * Returns the name of the feature map linked to the given {@link FeatureMapChange}.
	 * 
	 * @param featureMapChange
	 *            the given {@link FeatureMapChange}.
	 * @return the name of the feature map linked to the given {@link FeatureMapChange}.
	 */
	protected String getFeatureMapText(final FeatureMapChange featureMapChange) {
		return featureMapChange.getAttribute().getName();
	}

	/**
	 * Converts to text the given {@link FeatureMapChange}.
	 * 
	 * @param featureMapChange
	 *            the given {@link FeatureMapChange}.
	 * @return a nice text from the the given {@link FeatureMapChange}.
	 */
	protected String getValueText(final FeatureMapChange featureMapChange) {
		String value;
		FeatureMap.Entry featureMapValue = (FeatureMap.Entry)featureMapChange.getValue();
		EStructuralFeature entryFeature = featureMapValue.getEStructuralFeature();
		Object entryValue = featureMapValue.getValue();

		value = "<" + entryFeature.getName() + "> "; //$NON-NLS-1$ //$NON-NLS-2$
		value += itemDelegator.getText(entryValue);

		if (isNullOrEmpty(value)) {
			value = "<null>"; //$NON-NLS-1$
		} else {
			value = org.eclipse.emf.compare.provider.spec.Strings.elide(value, ELIDE_LENGTH, "..."); //$NON-NLS-1$
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		FeatureMapChange featureMapChange = (FeatureMapChange)object;
		switch (featureMapChange.getState()) {
			case MERGED:
			case DISCARDED:
				return URI.createURI("color://rgb/156/156/156"); //$NON-NLS-1$
			default:
				return super.getForeground(object);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		final FeatureMapChange featureMapChange = (FeatureMapChange)object;

		final String valueText = getValueText(featureMapChange);

		final String featureMapText = getFeatureMapText(featureMapChange);

		ComposedStyledString ret = new ComposedStyledString();
		ret.append(valueText);
		ret.append(" [" + featureMapText, Style.DECORATIONS_STYLER); //$NON-NLS-1$

		switch (featureMapChange.getKind()) {
			case ADD:
				ret.append(" add", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case DELETE:
				ret.append(" delete", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case CHANGE:
				ret.append(" entry key change", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case MOVE:
				ret.append(" move", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + featureMapChange.getKind()); //$NON-NLS-1$
		}
		ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		final FeatureMapChange featureMapChange = (FeatureMapChange)object;

		final String valueText = getValueText(featureMapChange);
		final String featureMapText = getFeatureMapText(featureMapChange);

		String hasBeenAndSide = EMFCompareEditMessages.getString("change.local"); //$NON-NLS-1$
		if (featureMapChange.getSource() == DifferenceSource.RIGHT) {
			hasBeenAndSide = EMFCompareEditMessages.getString("change.remote"); //$NON-NLS-1$
		}

		String ret = ""; //$NON-NLS-1$

		switch (featureMapChange.getKind()) {
			case ADD:
				ret = EMFCompareEditMessages.getString("FeatureMapChangeItemProviderSpec.valueAdded", //$NON-NLS-1$
						valueText, hasBeenAndSide, featureMapText);
				break;
			case DELETE:
				ret = EMFCompareEditMessages.getString("FeatureMapChangeItemProviderSpec.valueRemoved", //$NON-NLS-1$
						valueText, hasBeenAndSide, featureMapText);
				break;
			case CHANGE:
				String changeText = ReferenceChangeItemProviderSpec.changeText(featureMapChange,
						featureMapChange.getAttribute());
				ret = EMFCompareEditMessages.getString("FeatureMapChangeItemProviderSpec.valueChanged", //$NON-NLS-1$
						featureMapText, valueText, hasBeenAndSide, changeText);
				break;
			case MOVE:
				ret = EMFCompareEditMessages.getString("FeatureMapChangeItemProviderSpec.valueMoved", //$NON-NLS-1$
						valueText, hasBeenAndSide, featureMapText);
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + featureMapChange.getKind()); //$NON-NLS-1$
		}

		return ret;
	}
}
