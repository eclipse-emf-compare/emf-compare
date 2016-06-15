/*******************************************************************************
 * Copyright (c) 2014, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - bug 483798
 *******************************************************************************/
package org.eclipse.emf.compre.uml2.edit.papyrus.internal.decorator;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.uml2.internal.provider.decorator.StereotypedElementItemProviderDecorator;
import org.eclipse.emf.compre.uml2.edit.papyrus.Activator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.papyrus.infra.services.labelprovider.service.LabelProviderService;
import org.eclipse.papyrus.uml.tools.utils.StereotypeUtil;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Decorator that reuses the label provider of Papyrus.
 * 
 * @see org.eclipse.papyrus.infra.services.labelprovider.service.LabelProviderService
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class PapyrusStereotypedElementItemProviderDecorator extends StereotypedElementItemProviderDecorator {

	/** Context id for the comparison papyrus label providers. */
	private static final String PAPYRUS_LABEL_PROVIDER_COMPARE_CONTEXT = "org.eclipse.emf.compare.uml2.edit.papyrus"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to be used by the label providers.
	 */
	public PapyrusStereotypedElementItemProviderDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public String getText(Object object) {
		LabelProviderService labelProviderService = Activator.getDefault().getLabelProviderService();
		if (labelProviderService != null) {
			return labelProviderService.getLabelProvider(PAPYRUS_LABEL_PROVIDER_COMPARE_CONTEXT, object)
					.getText(object);
		}
		return super.getText(object);
	}

	@Override
	public Object getImage(Object object) {
		LabelProviderService labelProviderService = Activator.getDefault().getLabelProviderService();
		if (labelProviderService != null) {
			return labelProviderService.getLabelProvider(object).getImage(object);
		}
		return super.getImage(object);
	}

	@Override
	protected List<Object> getStereotypeIconsFromProfile(Stereotype appliedStereotype) {
		List<Object> images = new ArrayList<Object>();

		// Filter out images representing a shape. Shapes are usually to big to be used as icons.
		for (org.eclipse.uml2.uml.Image icon : filter(appliedStereotype.getIcons(),
				not(in(StereotypeUtil.getShapes(appliedStereotype))))) {

			String location = icon.getLocation();

			if (!UML2Util.isEmpty(location)) {
				Object img = getIconFromLocation(icon.eResource(), location);
				if (img != null) {
					images.add(img);
				}
			} else {
				// TODO handle icons defined by the "content" feature...
			}
		}
		if (images.isEmpty()) {
			images = null;
		}
		return images;
	}
}
