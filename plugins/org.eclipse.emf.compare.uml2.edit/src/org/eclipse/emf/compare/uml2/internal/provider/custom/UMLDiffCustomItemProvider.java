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
package org.eclipse.emf.compare.uml2.internal.provider.custom;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.spec.Strings;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.provider.UMLDiffItemProvider;
import org.eclipse.emf.ecore.EObject;

/**
 * An extended {@link org.eclipse.emf.compare.uml2.internal.provider.UMLDiffItemProvider} that handles
 * {@link org.eclipse.emf.compare.provider.IItemStyledLabelProvider} and
 * {@link org.eclipse.emf.compare.provider.IItemDescriptionProvider}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLDiffCustomItemProvider extends UMLDiffItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/**
	 * The maximum length of displayed text.
	 */
	private static final int MAX_LENGTH = 50;

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public UMLDiffCustomItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		return itemDelegator.getDescription(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
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

	/**
	 * Returns the value text for the given umlDiff.
	 * 
	 * @param umlDiff
	 *            the given {@link UMLDiff}.
	 * @return the value text.
	 */
	private String getValueText(final UMLDiff umlDiff) {
		String value = itemDelegator.getText(umlDiff.getDiscriminant());
		if (value == null) {
			value = "<null>"; //$NON-NLS-1$
		} else {
			value = Strings.elide(value, MAX_LENGTH, "..."); //$NON-NLS-1$
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
	private static String getReferenceText(final UMLDiff umlDiff) {
		String ret = ""; //$NON-NLS-1$
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
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + umlDiff.getKind()); //$NON-NLS-1$
		}
		return ret;
	}

	/**
	 * Gets the item delegator.
	 * 
	 * @return {@link ExtendedAdapterFactoryItemDelegator}.
	 */
	protected ExtendedAdapterFactoryItemDelegator getItemDelegator() {
		return itemDelegator;
	}

}
