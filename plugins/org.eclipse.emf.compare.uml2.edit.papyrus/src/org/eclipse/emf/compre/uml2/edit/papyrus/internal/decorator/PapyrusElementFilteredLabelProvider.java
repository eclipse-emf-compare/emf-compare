/*****************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH
 * *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Alexandra Buzila (EclipseSource) - Initial API and implementation
 *****************************************************************************/

package org.eclipse.emf.compre.uml2.edit.papyrus.internal.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.emf.facet.custom.core.ICustomizationManager;
import org.eclipse.papyrus.emf.facet.custom.core.exception.CustomizationException;
import org.eclipse.papyrus.emf.facet.custom.ui.internal.PropertiesHandler;
import org.eclipse.papyrus.infra.services.labelprovider.service.IFilteredLabelProvider;
import org.eclipse.papyrus.uml.tools.providers.UMLLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Filtered label provider for Papyrus UML elements.
 * 
 * @author Alexandra Buzila (EclipseSource)
 */
public class PapyrusElementFilteredLabelProvider extends UMLLabelProvider implements IFilteredLabelProvider {

	/** The properties handler. */
	private PropertiesHandler propertiesHandler;

	/** Constructor. */
	public PapyrusElementFilteredLabelProvider() {
		this.propertiesHandler = new PropertiesHandler(getCustomizationManager());
	}

	/**
	 * Returns <code>true</code> if the label provider applies to the given object.
	 * 
	 * @param object
	 *            the object
	 * @return <code>true</code> if the Label provider handles the given object
	 */
	public boolean accept(Object object) {
		return PapyrusStereotypedElementUtil.isPapyrusStereotypedElement(object);
	}

	@Override
	public String getText(Object object) {
		EObject element = (EObject)object;
		ICustomizationManager customizationManager = getCustomizationManager();
		if (customizationManager == null) {
			return super.getText(element);
		}
		try {
			return customizationManager.getCustomValueOf(element, propertiesHandler.getLabelProperty(),
					String.class);
		} catch (CustomizationException e) {
			// fall back to super class
			return super.getText(element);
		}
	}

	@Override
	public Image getImage(Object object) {
		EObject element = (EObject)object;
		ICustomizationManager customizationManager = getCustomizationManager();
		if (customizationManager == null) {
			return super.getImage(element);
		}
		try {
			return customizationManager.getCustomValueOf(element, propertiesHandler.getImageProperty(),
					Image.class);
		} catch (CustomizationException e) {
			// fall back to super class
			return super.getImage(element);
		}
	}
}
