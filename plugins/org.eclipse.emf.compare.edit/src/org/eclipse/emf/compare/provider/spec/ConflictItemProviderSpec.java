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

import static com.google.common.collect.Iterables.any;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.ConflictItemProvider;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.utils.EMFComparePredicates;

/**
 * Specialized {@link ConflictItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ConflictItemProviderSpec extends ConflictItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/**
	 * Constructs a ComparisonItemProviderSpec with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public ConflictItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ConflictItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ConflictItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		if (((Conflict)object).getKind() == ConflictKind.PSEUDO) {
			return overlayImage(object, getResourceLocator().getImage("full/obj16/PseudoConflict")); //$NON-NLS-1$
		} else {
			return overlayImage(object, getResourceLocator().getImage("full/obj16/Conflict")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		Conflict conflict = (Conflict)object;
		ComposedStyledString ret = new ComposedStyledString();

		if (any(conflict.getDifferences(), EMFComparePredicates.hasState(DifferenceState.UNRESOLVED))) {
			ret.append("> ", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		}

		if (conflict.getKind() == ConflictKind.PSEUDO) {
			ret.append(EMFCompareEditMessages.getString("pseudoconflict")); //$NON-NLS-1$
		} else {
			ret.append(EMFCompareEditMessages.getString("conflict")); //$NON-NLS-1$ 
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		Conflict conflict = (Conflict)object;
		int size = conflict.getDifferences().size() - 1;
		String ret = conflict.getKind().getName() + " conflict with " + size + " other difference";
		if (size > 1) {
			ret += "s";
		}
		return ret;
	}
}
