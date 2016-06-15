/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.custom;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication;
import org.eclipse.emf.compare.uml2.internal.EMFCompareUML2EditMessages;

/**
 * A custom {@link org.eclipse.emf.compare.uml2.internal.provider.DanglingStereotypeApplicationItemProvider}
 * that handles {@link org.eclipse.emf.compare.provider.IItemStyledLabelProvider} and
 * {@link org.eclipse.emf.compare.provider.IItemDescriptionProvider}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DanglingStereotypeApplicationCustomItemProvider extends UMLDiffCustomItemProvider {

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public DanglingStereotypeApplicationCustomItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public IComposedStyledString getStyledText(Object object) {
		ComposedStyledString ret = new ComposedStyledString(getInternalText(object));
		ret.append(" [" + EMFCompareUML2EditMessages.getString("DanglingStereotypeApplication.message") + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Style.DECORATIONS_STYLER);

		return ret;
	}

	/**
	 * Compute the label of the given object.
	 * 
	 * @param object
	 *            The given object
	 * @return the label of the object
	 */
	private String getInternalText(Object object) {
		DanglingStereotypeApplication danglingStereotypeApplication = (DanglingStereotypeApplication)object;
		final Match match = danglingStereotypeApplication.getMatch();
		String value = itemDelegator.getText(match.getLeft());
		if (isNullOrEmpty(value)) {
			value = itemDelegator.getText(match.getRight());
		}
		if (isNullOrEmpty(value)) {
			value = itemDelegator.getText(match.getOrigin());
		}
		if (isNullOrEmpty(value)) {
			value = super.getText(object);
		}

		return value;
	}

	@Override
	public String getSemanticObjectLabel(Object object) {
		return getInternalText(object);
	}

}
