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
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.provider.ReferenceChangeItemProvider;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;

/**
 * Specialized {@link ReferenceChangeItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeItemProviderSpec extends ReferenceChangeItemProvider {

	/**
	 * Constructor calling super {@link #ReferenceChangeItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public ReferenceChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final ReferenceChange refChange = (ReferenceChange)object;

		EObject value = refChange.getValue();
		final String valueName = CompareItemProviderAdapterFactorySpec.getText(adapterFactory, value);

		String change = "";
		if (refChange.getSource() == DifferenceSource.RIGHT) {
			change = "remotely ";
		}
		if (refChange.getKind() == DifferenceKind.ADD) {
			change += "added to";
		} else if (refChange.getKind() == DifferenceKind.DELETE) {
			change += "deleted from";
		} else if (refChange.getKind() == DifferenceKind.CHANGE) {
			change += "changed from";
		} else {
			change += "moved from";
		}
		final String objectName;
		if (refChange.getMatch().getLeft() instanceof ENamedElement) {
			objectName = ((ENamedElement)refChange.getMatch().getLeft()).getName();
		} else if (refChange.getMatch().getRight() instanceof ENamedElement) {
			objectName = ((ENamedElement)refChange.getMatch().getRight()).getName();
		} else if (refChange.getMatch().getOrigin() instanceof ENamedElement) {
			objectName = ((ENamedElement)refChange.getMatch().getOrigin()).getName();
		} else {
			objectName = "";
		}
		return "value " + valueName + " has been " + change + " reference "
				+ refChange.getReference().getName() + " of object " + objectName;
	}
}
