/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - fixed bug 461504
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.AttributeChangeItemProvider;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemFontProvider;

/**
 * Specialized {@link AttributeChangeItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class AttributeChangeItemProviderSpec extends AttributeChangeItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider, ISemanticObjectLabelProvider {

	/** The elide length. */
	private static final int ELIDE_LENGTH = 50;

	/** The image provider used with this item provider. */
	private final OverlayImageProvider overlayProvider;

	/** The item delegator for attribute change values. */
	private final AdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructs an AttributeChangeItemProviderSpec with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public AttributeChangeItemProviderSpec(AdapterFactory adapterFactory) {
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
		AttributeChange attributeChange = (AttributeChange)object;
		Object attributeChangeValueImage = itemDelegator.getImage(attributeChange.getValue());

		if (attributeChangeValueImage == null) {
			attributeChangeValueImage = super.getImage(object);
		}

		Object diffImage = overlayProvider.getComposedImage(attributeChange, attributeChangeValueImage);
		Object ret = overlayImage(object, diffImage);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.AttributeChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/**
	 * Returns the name of the attribute linked to the given {@link AttributeChange}.
	 * 
	 * @param attChange
	 *            the given {@link AttributeChange}.
	 * @return the name of the attribute linked to the given {@link AttributeChange}.
	 */
	protected String getAttributeText(final AttributeChange attChange) {
		return attChange.getAttribute().getName();
	}

	/**
	 * Converts to text the given {@link AttributeChange}.
	 * 
	 * @param attChange
	 *            the given {@link AttributeChange}.
	 * @return a nice text from the the given {@link AttributeChange}.
	 */
	protected String getValueText(final AttributeChange attChange) {
		String value;
		Object attValue = attChange.getValue();
		if (FeatureMapUtil.isFeatureMap(attChange.getAttribute())) {
			FeatureMap.Entry entry = (FeatureMap.Entry)attValue;
			EStructuralFeature entryFeature = entry.getEStructuralFeature();
			if (entryFeature instanceof EAttribute) {
				value = EcoreUtil.convertToString(((EAttribute)entryFeature).getEAttributeType(), attValue);
			} else {
				value = itemDelegator.getText(entry.getValue());
			}
		} else {
			value = EcoreUtil.convertToString(attChange.getAttribute().getEAttributeType(), attValue);
		}

		if (isNullOrEmpty(value)) {
			if (attValue instanceof EObject && ((EObject)attValue).eIsProxy()) {
				value = "proxy : " + ((InternalEObject)attValue).eProxyURI().toString(); //$NON-NLS-1$
			} else {
				value = "<null>"; //$NON-NLS-1$
			}
		} else {
			value = org.eclipse.emf.compare.provider.spec.Strings.removeLineBreaks(value);
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
		AttributeChange attributeChange = (AttributeChange)object;
		switch (attributeChange.getState()) {
			case MERGED:
			case DISCARDED:
				return URI.createURI("color://rgb/156/156/156"); //$NON-NLS-1$
			default:
				return super.getForeground(object);
		}
	}

	/* Missing override : only for EMF 2.10 and later. Do not tag. */
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		final AttributeChange attChange = (AttributeChange)object;

		final String valueText = getValueText(attChange);

		final String attributeText = getAttributeText(attChange);

		ComposedStyledString ret = new ComposedStyledString();
		final Object attChangeValue = attChange.getValue();
		if (attChangeValue instanceof EObject && ((EObject)attChangeValue).eIsProxy()) {
			Style italic = Style.builder().setFont(IItemFontProvider.ITALIC_FONT).build();
			ret.append(valueText, italic);
		} else {
			ret.append(valueText);
		}
		ret.append(" [" + attributeText, Style.DECORATIONS_STYLER); //$NON-NLS-1$

		switch (attChange.getKind()) {
			case ADD:
				ret.append(" add", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case DELETE:
				ret.append(" delete", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case CHANGE:
				ret.append(
						" " + ReferenceChangeItemProviderSpec.changeText(attChange, attChange.getAttribute()), //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			case MOVE:
				ret.append(" move", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + attChange.getKind()); //$NON-NLS-1$
		}
		ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider#getSemanticObjectLabel(java.lang.Object)
	 * @since 4.2
	 */
	public String getSemanticObjectLabel(Object object) {
		final AttributeChange attChange = (AttributeChange)object;
		return getValueText(attChange);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		final AttributeChange attChange = (AttributeChange)object;

		final String valueText = getValueText(attChange);
		final String attributeText = getAttributeText(attChange);

		String hasBeenAndSide = EMFCompareEditMessages.getString("change.local"); //$NON-NLS-1$
		if (attChange.getSource() == DifferenceSource.RIGHT) {
			hasBeenAndSide = EMFCompareEditMessages.getString("change.remote"); //$NON-NLS-1$
		}

		String ret = ""; //$NON-NLS-1$

		switch (attChange.getKind()) {
			case ADD:
				ret = EMFCompareEditMessages.getString("AttributeChangeItemProviderSpec.valueAdded", //$NON-NLS-1$
						valueText, hasBeenAndSide, attributeText);
				break;
			case DELETE:
				ret = EMFCompareEditMessages.getString("AttributeChangeItemProviderSpec.valueRemoved", //$NON-NLS-1$
						valueText, hasBeenAndSide, attributeText);
				break;
			case CHANGE:
				String changeText = ReferenceChangeItemProviderSpec.changeText(attChange, attChange
						.getAttribute());
				ret = EMFCompareEditMessages.getString("AttributeChangeItemProviderSpec.valueChanged", //$NON-NLS-1$
						attributeText, valueText, hasBeenAndSide, changeText);
				break;
			case MOVE:
				ret = EMFCompareEditMessages.getString("AttributeChangeItemProviderSpec.valueMoved", //$NON-NLS-1$
						valueText, hasBeenAndSide, attributeText);
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + attChange.getKind()); //$NON-NLS-1$
		}

		return ret;
	}
}
