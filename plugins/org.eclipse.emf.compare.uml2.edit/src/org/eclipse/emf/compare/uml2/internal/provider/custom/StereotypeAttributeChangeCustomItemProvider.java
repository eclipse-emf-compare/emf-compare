/*******************************************************************************
 * Copyright (c) 2013 Obeo.
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
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Item Provider for Stereotype Property Change.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class StereotypeAttributeChangeCustomItemProvider extends UMLDiffCustomItemProvider {

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public StereotypeAttributeChangeCustomItemProvider(AdapterFactory adapterFactory) {
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
		final UMLDiff umlDiff = (UMLDiff)object;

		EObject discriminant = umlDiff.getDiscriminant();

		final ComposedStyledString stereotypeText = new ComposedStyledString();
		final String prefix = "Stereotype Property ";
		if (discriminant instanceof NamedElement) {
			stereotypeText.append(prefix + ((NamedElement)discriminant).getName() + ' ');
		} else if (discriminant instanceof EAttribute) {
			stereotypeText.append(prefix + ((EAttribute)discriminant).getName() + ' ');
		} else {
			// Can't really do more
			stereotypeText.append(prefix);
		}

		final String action;
		switch (umlDiff.getKind()) {
			case ADD:
				action = "add"; //$NON-NLS-1$
				break;
			case DELETE:
				action = "remove"; //$NON-NLS-1$
				break;
			case CHANGE:
				action = "changed"; //$NON-NLS-1$
				break;
			case MOVE:
				action = "moved"; //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + umlDiff.getKind()); //$NON-NLS-1$
		}

		return stereotypeText.append(" [stereotype attribute " + action + "]", Style.DECORATIONS_STYLER);
	}

}
