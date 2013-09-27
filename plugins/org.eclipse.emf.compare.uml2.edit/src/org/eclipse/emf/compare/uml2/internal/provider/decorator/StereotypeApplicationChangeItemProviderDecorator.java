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
package org.eclipse.emf.compare.uml2.internal.provider.decorator;

import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Specifialized StereotypeApplicationChangeItemProvider.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class StereotypeApplicationChangeItemProviderDecorator extends UMLDiffItemProviderDecorator {

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the factory to delegate to.
	 */
	public StereotypeApplicationChangeItemProviderDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;

		Stereotype stereotype = ((StereotypeApplicationChange)umlDiff).getStereotype();
		if (stereotype == null) {
			stereotype = UMLUtil.getStereotype(umlDiff.getDiscriminant());
		}

		final Object image;
		if (stereotype != null) {
			image = getItemDelegator().getImage(stereotype);
		} else {
			image = getItemDelegator().getImage(umlDiff.getDiscriminant());
		}

		return getOverlayProvider().getComposedImage(umlDiff, image);
	}

}
