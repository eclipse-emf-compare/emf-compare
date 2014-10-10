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
package org.eclipse.emf.compre.uml2.edit.papyrus.internal.decorator;

import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLProfileItemProviderAdapterFactoryDecorator;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;

/**
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class PapyrusProfileItemProviderAdapterFactoryDecorator extends UMLProfileItemProviderAdapterFactoryDecorator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLProfileItemProviderAdapterFactoryDecorator#createStereotypeElementItemProvider()
	 */
	@Override
	protected IItemProviderDecorator createStereotypeElementItemProvider() {
		return new PapyrusStereotypedElementItemProviderDecorator(this);
	}

}
