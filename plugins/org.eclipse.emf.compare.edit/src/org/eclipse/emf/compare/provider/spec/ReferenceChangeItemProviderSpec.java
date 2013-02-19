/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ReferenceChangeItemProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * Specialized {@link ReferenceChangeItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeItemProviderSpec extends ReferenceChangeItemProvider implements IItemStyledLabelProvider {

	private final OverlayImageProvider overlayProvider;

	/**
	 * Constructor calling super {@link #ReferenceChangeItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public ReferenceChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
		overlayProvider = new OverlayImageProvider(getResourceLocator());
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

	private static String changeText(final EStructuralFeature eStructuralFeature, EObject sourceSide,
			EObject otherSide) {
		String changeText;
		if (sourceSide != null) {
			Object leftValue = ReferenceUtil.safeEGet(sourceSide, eStructuralFeature);
			if (leftValue == null || isStringAndNullOrEmpty(leftValue)) {
				changeText = "unset";
			} else if (otherSide != null) {
				Object otherValue = ReferenceUtil.safeEGet(otherSide, eStructuralFeature);
				if (otherValue == null || isStringAndNullOrEmpty(otherValue)) {
					changeText = "set";
				} else {
					changeText = "changed";
				}
			} else {
				changeText = "set";
			}
		} else {
			changeText = "unset";
		}
		return changeText;
	}

	private static boolean isStringAndNullOrEmpty(Object s) {
		if (s instanceof String) {
			return Strings.isNullOrEmpty((String)s);
		} else {
			return false;
		}
	}

	protected String getReferenceText(final ReferenceChange refChange) {
		return refChange.getReference().getName();
	}

	protected String getValueText(final ReferenceChange refChange) {
		String value = AdapterFactoryUtil.getText(getRootAdapterFactory(), refChange.getValue());
		if (value == null) {
			value = "<null>"; //$NON-NLS-1$
		} else {
			value = org.eclipse.emf.compare.provider.spec.Strings.elide(value, 50, "..."); //$NON-NLS-1$
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

		Object refChangeValueImage = AdapterFactoryUtil.getImage(getRootAdapterFactory(), refChange
				.getValue());

		Object diffImage = overlayProvider.getComposedImage(refChange, refChangeValueImage);
		Object ret = overlayImage(object, diffImage);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Collection<?> superChildren = super.getChildren(object);
		List<? super Object> ret = newArrayList(superChildren);

		ReferenceChange referenceChange = (ReferenceChange)object;
		EReference reference = referenceChange.getReference();

		if (reference.isContainment()) {
			Match matchOfValue = referenceChange.getMatch().getComparison().getMatch(
					referenceChange.getValue());
			if (matchOfValue != null) {
				Collection<?> children = getChildren(matchOfValue);
				children.remove(referenceChange);
				ret.addAll(children);
			}
		}

		return ImmutableList.copyOf(filter(ret, not(instanceOf(ResourceAttachmentChange.class))));

	}

	private Collection<?> getChildren(Match matchOfValue) {
		final Collection<?> children;
		ITreeItemContentProvider matchItemContentProvider = (ITreeItemContentProvider)adapterFactory.adapt(
				matchOfValue, ITreeItemContentProvider.class);
		if (matchItemContentProvider != null) {
			Collection<?> itemProviderChildren = matchItemContentProvider.getChildren(matchOfValue);
			if (itemProviderChildren instanceof ImmutableCollection<?>) {
				children = newArrayList(itemProviderChildren);
			} else {
				children = itemProviderChildren;
			}

			Iterator<?> childrenIterator = children.iterator();
			while (childrenIterator.hasNext()) {
				Object child = childrenIterator.next();
				if (child instanceof Match) {
					if (!matchItemContentProvider.hasChildren(child)) {
						childrenIterator.remove();
					}
				}

			}
		} else {
			children = ImmutableList.of();
		}
		return children;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		final ReferenceChange refChange = (ReferenceChange)object;

		final String valueText = getValueText(refChange);

		final String referenceText = getReferenceText(refChange);

		ComposedStyledString ret = new ComposedStyledString(valueText);
		ret.append(" [" + referenceText, Style.DECORATIONS_STYLER); //$NON-NLS-1$

		switch (refChange.getKind()) {
			case ADD:
				ret.append(" add", Style.DECORATIONS_STYLER);
				break;
			case DELETE:
				ret.append(" delete", Style.DECORATIONS_STYLER);
				break;
			case CHANGE:
				ret.append(" " + changeText(refChange, refChange.getReference()), Style.DECORATIONS_STYLER);
				break;
			case MOVE:
				ret.append(" move", Style.DECORATIONS_STYLER);
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + refChange.getKind());
		}
		ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$

		return ret;
	}
}
