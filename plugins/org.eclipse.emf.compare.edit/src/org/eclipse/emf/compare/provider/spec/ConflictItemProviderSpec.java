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

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.EMFCompareEditMessages;
import org.eclipse.emf.compare.provider.ConflictItemProvider;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;

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
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Conflict conflict = (Conflict)object;
		EList<Diff> differences = conflict.getDifferences();
		return differences;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		Conflict conflict = (Conflict)object;
		int size = conflict.getDifferences().size();
		String kind = conflict.getKind().getName().toLowerCase();
		ComposedStyledString ret = new ComposedStyledString(kind.substring(0, 1).toUpperCase()
				+ kind.substring(1) + " " + EMFCompareEditMessages.getString("conflict")); //$NON-NLS-1$ //$NON-NLS-2$

		ret.append(
				" [" + size + " " + EMFCompareEditMessages.getString("difference"), Style.DECORATIONS_STYLER); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		if (size > 1) {
			ret.append(EMFCompareEditMessages.getString("plural"), Style.DECORATIONS_STYLER); //$NON-NLS-1$
		}
		ret.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$
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
