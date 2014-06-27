/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.profile;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.uml2.internal.provider.custom.UMLCompareCustomItemProviderAdapterFactory;

/**
 * Custom item provider adapter factory used for a better integration of UML profile in EMF Compare.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ProfiledUMLCompareItemProviderAdapterFactory extends UMLCompareCustomItemProviderAdapterFactory {

	@Override
	public Adapter createStereotypeAttributeChangeAdapter() {
		return new StereotypeAttributeChangeProfileSupportItemProvider(this);
	}

	@Override
	public Adapter createStereotypeReferenceChangeAdapter() {
		return new StereotypeReferenceChangeProfileSupportItemProvider(this);
	}
}
