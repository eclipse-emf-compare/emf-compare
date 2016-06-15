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
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * An extended {@link org.eclipse.emf.compare.uml2.internal.provider.StereotypeApplicationChangeItemProvider}
 * that handles {@link org.eclipse.emf.compare.provider.IItemStyledLabelProvider} and
 * {@link org.eclipse.emf.compare.provider.IItemDescriptionProvider}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class StereotypeApplicationChangeCustomItemProvider extends UMLDiffCustomItemProvider {

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public StereotypeApplicationChangeCustomItemProvider(AdapterFactory adapterFactory) {
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
		IComposedStyledString stereotypeText = getInternalText(object);

		final String action;
		switch (umlDiff.getKind()) {
			case ADD:
				action = "applied"; //$NON-NLS-1$
				break;
			case DELETE:
				action = "unapplied"; //$NON-NLS-1$
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

		return stereotypeText.append(" [stereotype " + action + "]", Style.DECORATIONS_STYLER); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Compute the label of the given object.
	 * 
	 * @param object
	 *            The object
	 * @return the label of the object
	 */
	private IStyledString.IComposedStyledString getInternalText(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;

		Stereotype stereotype = ((StereotypeApplicationChange)umlDiff).getStereotype();
		if (stereotype == null) {
			stereotype = UMLUtil.getStereotype(umlDiff.getDiscriminant());
		}

		final ComposedStyledString stereotypeText = new ComposedStyledString();
		if (stereotype != null) {
			stereotypeText.append(itemDelegator.getText(stereotype) + ' ');
		} else if (umlDiff.getDiscriminant() instanceof NamedElement) {
			stereotypeText.append("Stereotype " + ((NamedElement)umlDiff.getDiscriminant()).getName() + ' '); //$NON-NLS-1$
		} else {
			// Can't really do more
			stereotypeText.append("Stereotype "); //$NON-NLS-1$
		}

		return stereotypeText;
	}

	@Override
	public String getSemanticObjectLabel(Object object) {
		return getInternalText(object).getString();
	}

}
