/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
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
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Item Provider for Stereotype Property Change.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class StereotypeReferenceChangeCustomItemProvider extends UMLDiffCustomItemProvider {

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public StereotypeReferenceChangeCustomItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		IComposedStyledString stereotypeText = getInternalText(object);
		return stereotypeText.append(" [stereotype reference changed]", Style.DECORATIONS_STYLER);
	}

	/**
	 * Compute the label of the given object.
	 * 
	 * @param object
	 *            The object
	 * @return The label of the object
	 */
	private IStyledString.IComposedStyledString getInternalText(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;

		EObject discriminant = umlDiff.getDiscriminant();

		final ComposedStyledString stereotypeText = new ComposedStyledString();
		final String prefix = "Stereotype Property ";
		if (discriminant instanceof NamedElement) {
			stereotypeText.append(prefix + ((NamedElement)discriminant).getName() + ' ');
		} else if (discriminant instanceof EReference) {
			stereotypeText.append(prefix + ((EReference)discriminant).getName() + ' ');
		} else {
			// Can't really do more
			stereotypeText.append(prefix);
		}
		return stereotypeText;
	}

	@Override
	public String getSemanticObjectLabel(Object object) {
		return getInternalText(object).getString();
	}

}
