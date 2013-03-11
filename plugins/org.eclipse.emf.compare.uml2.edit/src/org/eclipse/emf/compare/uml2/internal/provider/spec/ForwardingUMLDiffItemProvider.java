/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.spec;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.provider.ForwardingItemProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.spec.Strings;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Specialized ForwardingItemProvider for UML.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ForwardingUMLDiffItemProvider extends ForwardingItemProvider implements IItemStyledLabelProvider {

	/**
	 * The maximum length of displayed text.
	 */
	private static final int MAX_LENGTH = 50;

	/**
	 * This constructs an instance from an adapter.
	 * 
	 * @param delegate
	 *            the adapter to delegate to.
	 */
	public ForwardingUMLDiffItemProvider(ItemProviderAdapter delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.utils.ForwardingItemProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		final Collection<?> ret;
		UMLDiff umlDiff = (UMLDiff)object;
		if (umlDiff.getKind() == DifferenceKind.CHANGE) {
			ret = umlDiff.getRefinedBy();
		} else {
			ret = super.getChildren(object);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.utils.ForwardingItemProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		return !getChildren(object).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.utils.ForwardingItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/**
	 * Tries and find a non-<code>null</code> side for the given match. Still returns <code>null</code> if all
	 * three sides are <code>null</code> though.
	 * 
	 * @param match
	 *            The match for which we need a non-<code>null</code> side.
	 * @return The first side (in order : left, right or origin) that was not <code>null</code>.
	 */
	private EObject findNonNullSide(Match match) {
		final EObject side;
		if (match.getLeft() != null) {
			side = match.getLeft();
		} else if (match.getRight() != null) {
			side = match.getRight();
		} else {
			side = match.getOrigin();
		}
		return side;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;
		Object image = AdapterFactoryUtil.getImage(getRootAdapterFactory(), umlDiff.getDiscriminant());
		return image;
	}

	/**
	 * Returns the value text for the given umlDiff.
	 * 
	 * @param umlDiff
	 *            the given {@link UMLDiff}.
	 * @return the value text.
	 */
	private String getValueText(final UMLDiff umlDiff) {
		String value = AdapterFactoryUtil.getText(getRootAdapterFactory(), umlDiff.getDiscriminant());
		if (value == null) {
			value = "<null>";
		} else {
			value = Strings.elide(value, MAX_LENGTH, "...");
		}
		return value;
	}

	/**
	 * Returns the reference text for the given umlDiff.
	 * 
	 * @param umlDiff
	 *            the given {@link UMLDiff}.
	 * @return the reference text.
	 */
	private String getReferenceText(final UMLDiff umlDiff) {
		String ret = "";
		switch (umlDiff.getKind()) {
			case ADD:
			case DELETE:
			case MOVE:
				EObject discriminant = umlDiff.getDiscriminant();
				ret = discriminant.eContainingFeature().getName();
				break;
			case CHANGE:
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + umlDiff.getKind());
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		UMLDiff referenceChange = (UMLDiff)object;
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
		final UMLDiff umlDiff = (UMLDiff)object;

		final String valueText = getValueText(umlDiff);
		final String referenceText = getReferenceText(umlDiff);

		ComposedStyledString ret = new ComposedStyledString(valueText);
		ret.append(" [" + referenceText, Style.DECORATIONS_STYLER); //$NON-NLS-1$
		switch (umlDiff.getKind()) {
			case ADD:
				ret.append(" add", Style.DECORATIONS_STYLER); //$NON-NLS-1$ 
				break;
			case DELETE:
				ret.append(" delete", Style.DECORATIONS_STYLER); //$NON-NLS-1$ 
				break;
			case CHANGE:
				ret.append(" change", Style.DECORATIONS_STYLER); //$NON-NLS-1$ 
				break;
			case MOVE:
				ret.append(" move", Style.DECORATIONS_STYLER); //$NON-NLS-1$ 
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + umlDiff.getKind()); //$NON-NLS-1$
		}
		ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$

		return ret;
	}
}
