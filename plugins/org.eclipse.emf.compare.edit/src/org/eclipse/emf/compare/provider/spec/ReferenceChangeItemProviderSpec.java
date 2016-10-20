/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo.
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
import static com.google.common.collect.Iterables.any;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.compare.provider.ReferenceChangeItemProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemFontProvider;

/**
 * Specialized {@link ReferenceChangeItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeItemProviderSpec extends ReferenceChangeItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider, ISemanticObjectLabelProvider {

	/** The elide length. */
	private static final int ELIDE_LENGTH = 50;

	/** The image provider used with this item provider. */
	private final OverlayImageProvider overlayProvider;

	/** The item delegator for reference change values. */
	private final AdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructor calling super {@link #ReferenceChangeItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public ReferenceChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new AdapterFactoryItemDelegator(getRootAdapterFactory());
		overlayProvider = new OverlayImageProvider(getResourceLocator());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#isAdapterForType(Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		if (type == ReferenceChangeItemProviderSpec.class) {
			return true;
		}
		return super.isAdapterForType(type);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/**
	 * Returns the change text for the given diff on the given feature.
	 * 
	 * @param diff
	 *            the diff representing the change.
	 * @param feature
	 *            the feature that changed.
	 * @return the change text for the given diff on the given feature.
	 */
	static String changeText(final Diff diff, EStructuralFeature feature) {
		DifferenceSource source = diff.getSource();
		Match matchOfInterrest = diff.getMatch();
		final EObject sourceSide;
		final EObject otherSide;
		if (source == DifferenceSource.LEFT) {
			sourceSide = matchOfInterrest.getLeft();
			otherSide = matchOfInterrest.getRight();
		} else { // source == DifferenceSource.RIGHT
			sourceSide = matchOfInterrest.getRight();
			otherSide = matchOfInterrest.getLeft();
		}
		String changeText = changeText(feature, sourceSide, otherSide);
		return changeText;
	}

	/**
	 * Returns the type of change linked to the given {@link EStructuralFeature} ("unset", "set" or
	 * "changed"), according the the given sides.
	 * 
	 * @param eStructuralFeature
	 *            the given {@link EStructuralFeature}.
	 * @param sourceSide
	 *            the source side as an {@link EObject}.
	 * @param otherSide
	 *            the other side as an {@link EObject}.
	 * @return a String ("unset", "set" or "changed") containing the type of change linked to the given
	 *         {@link EStructuralFeature}, according the the given sides.
	 */
	private static String changeText(final EStructuralFeature eStructuralFeature, EObject sourceSide,
			EObject otherSide) {
		String changeText;
		if (sourceSide != null) {
			Object leftValue = ReferenceUtil.safeEGet(sourceSide, eStructuralFeature);
			if (leftValue == null || isStringAndNullOrEmpty(leftValue)) {
				changeText = EMFCompareEditMessages.getString("ReferenceChangeItemProviderSpec.unset"); //$NON-NLS-1$
			} else if (otherSide != null) {
				Object otherValue = ReferenceUtil.safeEGet(otherSide, eStructuralFeature);
				if (otherValue == null || isStringAndNullOrEmpty(otherValue)) {
					changeText = EMFCompareEditMessages.getString("ReferenceChangeItemProviderSpec.set"); //$NON-NLS-1$
				} else {
					changeText = EMFCompareEditMessages.getString("ReferenceChangeItemProviderSpec.changed"); //$NON-NLS-1$
				}
			} else {
				changeText = EMFCompareEditMessages.getString("ReferenceChangeItemProviderSpec.set"); //$NON-NLS-1$
			}
		} else {
			changeText = EMFCompareEditMessages.getString("ReferenceChangeItemProviderSpec.unset"); //$NON-NLS-1$
		}
		return changeText;
	}

	/**
	 * Checks if the given Object is a null or empty String.
	 * 
	 * @param s
	 *            the given Object.
	 * @return true if the Object is a null or empty String, false otherwise.
	 */
	private static boolean isStringAndNullOrEmpty(Object s) {
		if (s instanceof String) {
			return isNullOrEmpty((String)s);
		} else {
			return false;
		}
	}

	/**
	 * Returns the name of the reference linked to the given {@link ReferenceChange}.
	 * 
	 * @param refChange
	 *            the given {@link ReferenceChange}.
	 * @return the name of the reference linked to the given {@link ReferenceChange}.
	 */
	protected String getReferenceText(final ReferenceChange refChange) {
		return refChange.getReference().getName();
	}

	/**
	 * Converts to text the given {@link ReferenceChange}.
	 * 
	 * @param refChange
	 *            the given {@link ReferenceChange}.
	 * @return a nice text from the the given {@link ReferenceChange}.
	 */
	protected String getValueText(final ReferenceChange refChange) {
		EObject refChangeValue = refChange.getValue();
		String value = itemDelegator.getText(refChangeValue);
		if (isNullOrEmpty(value)) {
			if (refChangeValue.eIsProxy()) {
				value = "proxy : " + ((InternalEObject)refChangeValue).eProxyURI().toString(); //$NON-NLS-1$
			} else {
				value = "<null>"; //$NON-NLS-1$
			}
		} else {
			value = org.eclipse.emf.compare.provider.spec.Strings.elide(value, ELIDE_LENGTH, "..."); //$NON-NLS-1$
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		ReferenceChange refChange = (ReferenceChange)object;

		Object refChangeValueImage = itemDelegator.getImage(refChange.getValue());

		Object diffImage = overlayProvider.getComposedImage(refChange, refChangeValueImage);
		Object ret = overlayImage(object, diffImage);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		ReferenceChange referenceChange = (ReferenceChange)object;
		switch (referenceChange.getState()) {
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
		final ReferenceChange refChange = (ReferenceChange)object;

		final String valueText = getValueText(refChange);

		final String referenceText = getReferenceText(refChange);

		ComposedStyledString ret = new ComposedStyledString();

		if (refChange.getReference().isContainment()) {
			EObject value = refChange.getValue();
			Match match = refChange.getMatch().getComparison().getMatch(value);
			if (match != null) {
				Iterable<Diff> subDifferences = match.getAllDifferences();
				if (refChange.getState() != DifferenceState.UNRESOLVED
						&& any(subDifferences, EMFComparePredicates.hasState(DifferenceState.UNRESOLVED))) {
					ret.append("> ", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				}
			}
		}
		final EObject refChangeValue = refChange.getValue();
		if (refChangeValue.eIsProxy()) {
			Style italic = Style.builder().setFont(IItemFontProvider.ITALIC_FONT).build();
			ret.append(valueText, italic);
		} else {
			ret.append(valueText);
		}
		ret.append(" [" + referenceText, Style.DECORATIONS_STYLER); //$NON-NLS-1$

		switch (refChange.getKind()) {
			case ADD:
				ret.append(
						' ' + EMFCompareEditMessages
								.getString("ReferenceChangeItemProviderSpec.decoration.add"), //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			case DELETE:
				ret.append(
						' ' + EMFCompareEditMessages
								.getString("ReferenceChangeItemProviderSpec.decoration.delete"), //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			case CHANGE:
				ret.append(' ' + changeText(refChange, refChange.getReference()), Style.DECORATIONS_STYLER);
				break;
			case MOVE:
				ret.append(
						' ' + EMFCompareEditMessages
								.getString("ReferenceChangeItemProviderSpec.decoration.move"), //$NON-NLS-1$
						Style.DECORATIONS_STYLER);
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + refChange.getKind()); //$NON-NLS-1$
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
		final ReferenceChange refChange = (ReferenceChange)object;
		final String valueText = getValueText(refChange);

		StringBuilder ret = new StringBuilder();

		if (refChange.getReference().isContainment()) {
			EObject value = refChange.getValue();
			Match match = refChange.getMatch().getComparison().getMatch(value);
			if (match != null) {
				Iterable<Diff> subDifferences = match.getAllDifferences();
				if (refChange.getState() != DifferenceState.UNRESOLVED
						&& any(subDifferences, EMFComparePredicates.hasState(DifferenceState.UNRESOLVED))) {
					ret.append("> "); //$NON-NLS-1$
				}
			}
		}
		ret.append(valueText);
		return ret.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		final ReferenceChange refChange = (ReferenceChange)object;

		final String valueText = getValueText(refChange);
		final String referenceText = getReferenceText(refChange);

		String remotely = ""; //$NON-NLS-1$
		if (refChange.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely "; //$NON-NLS-1$
		}

		String ret = ""; //$NON-NLS-1$
		final String hasBeen = " has been "; //$NON-NLS-1$

		switch (refChange.getKind()) {
			case ADD:
				ret = valueText + hasBeen + remotely + "added to " + referenceText; //$NON-NLS-1$
				break;
			case DELETE:
				ret = valueText + hasBeen + remotely + "deleted from " + referenceText; //$NON-NLS-1$
				break;
			case CHANGE:
				String changeText = changeText(refChange, refChange.getReference());
				ret = referenceText + " " + valueText + hasBeen + remotely + changeText; //$NON-NLS-1$
				break;
			case MOVE:
				ret = valueText + hasBeen + remotely + "moved in " + referenceText; //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + refChange.getKind()); //$NON-NLS-1$
		}

		return ret;
	}
}
