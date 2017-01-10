/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
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
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.collect.Iterables;

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
		final Object image;
		Conflict conflict = (Conflict)object;
		if (conflict.getKind() == ConflictKind.PSEUDO) {
			if (any(conflict.getDifferences(), EMFComparePredicates.hasState(DifferenceState.UNRESOLVED))) {
				image = overlayImage(object, getResourceLocator().getImage("full/obj16/PseudoConflict")); //$NON-NLS-1$
			} else {
				image = overlayImage(object,
						getResourceLocator().getImage("full/obj16/PseudoConflictResolved")); //$NON-NLS-1$
			}
		} else {
			if (any(conflict.getDifferences(), EMFComparePredicates.hasState(DifferenceState.UNRESOLVED))) {
				image = overlayImage(object, getResourceLocator().getImage("full/obj16/Conflict")); //$NON-NLS-1$
			} else {
				image = overlayImage(object, getResourceLocator().getImage("full/obj16/ConflictResolved")); //$NON-NLS-1$
			}
		}
		return image;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		Conflict conflict = (Conflict)object;
		ComposedStyledString ret = new ComposedStyledString();

		int unresolvedDiffCount = size(
				Iterables.filter(conflict.getDifferences(), hasState(DifferenceState.UNRESOLVED)));
		if (unresolvedDiffCount > 0) {
			ret.append("> ", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		}

		if (conflict.getKind() == ConflictKind.PSEUDO) {
			ret.append(EMFCompareEditMessages.getString("pseudoconflict")); //$NON-NLS-1$
		} else {
			ret.append(EMFCompareEditMessages.getString("conflict")); //$NON-NLS-1$
		}

		if (unresolvedDiffCount > 0) {
			ret.append(
					" [" //$NON-NLS-1$
							+ EMFCompareEditMessages.getString("unresolved", //$NON-NLS-1$
									Integer.valueOf(unresolvedDiffCount),
									Integer.valueOf(conflict.getDifferences().size()))
							+ "]", //$NON-NLS-1$
					Style.DECORATIONS_STYLER);
		} else {
			ret.append(" [" + EMFCompareEditMessages.getString("resolved") + "]", Style.DECORATIONS_STYLER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		String ret = EMFCompareEditMessages.getString("ConflictItemProviderSpec.description", conflict //$NON-NLS-1$
				.getKind().getName(), Integer.valueOf(size));
		return ret;
	}
}
