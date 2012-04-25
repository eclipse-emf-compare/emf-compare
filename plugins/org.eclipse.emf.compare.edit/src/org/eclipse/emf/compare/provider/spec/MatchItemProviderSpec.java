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
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.MatchItemProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchItemProviderSpec extends MatchItemProvider {

	/**
	 * @param adapterFactory
	 */
	public MatchItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Object getImage(Object object) {
		Match match = (Match)object;

		AdapterFactory af = adapterFactory;
		// If the adapter factory is composeable, we'll adapt using the root.
		if (adapterFactory instanceof ComposeableAdapterFactory) {
			af = ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory();
		}

		EObject left = match.getLeft();
		if (left != null) {
			Object adapter = af.adapt(left, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getImage(left);
			}
		}

		EObject right = match.getRight();
		if (right != null) {
			Object adapter = af.adapt(right, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getImage(right);
			}
		}

		return super.getImage(object);
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		Match match = (Match)object;

		AdapterFactory af = adapterFactory;
		// If the adapter factory is composeable, we'll adapt using the root.
		if (adapterFactory instanceof ComposeableAdapterFactory) {
			af = ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory();
		}

		EObject left = match.getLeft();
		if (left != null) {
			Object adapter = af.adapt(left, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getText(left);
			}
		}

		EObject right = match.getRight();
		if (right != null) {
			Object adapter = af.adapt(right, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getText(right);
			}
		}

		return super.getText(object);
	}
}
