/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Utility class providing acces to some objects description.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class EMFCompareEObjectUtils {
	private static AdapterFactoryLabelProvider labelProvider;
	
	private EMFCompareEObjectUtils() {
		// prevents instantiation
	}
	
	/**
	 * Computes the name of the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			Object for which we need the name.
	 * @return
	 * 			Name of the given {@link EObject}.
	 */
	public static String computeObjectName(EObject eObject) {
		String objectName = getLabelProvider().getText(eObject);
		if (objectName == null || objectName.equals(new String()))
			objectName = "undefined"; //$NON-NLS-1$
		return objectName;
	}
	
	/**
	 * Computes the image of the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			Object for which we need the image.
	 * @return
	 * 			Image of the given {@link EObject}.
	 */
	public static Image computeObjectImage(EObject eObject) {
		return getLabelProvider().getImage(eObject);
	}
	
	/**
	 * Returns the left element of the given {@link EObject}. Will try to
	 * invoke the method called "getLeftElement" and, if it fails to find it,
	 * "getLeftParent". <code>Null</code> if neither of these methods can be
	 * found.<br/>
	 * This method is intended to be called with a {@link DiffElement} or
	 * {@link MatchElement} as argument.
	 * 
	 * @param object
	 * 			The {@link EObject}.
	 * @return
	 * 			The left element of the given {@link EObject}.
	 */
	public static EObject getLeftElement(EObject object) {
		EObject leftElement = null;
		
		if (ClassUtils.hasMethod(object.getClass(), "getLeftElement")) { //$NON-NLS-1$
			leftElement = (EObject)ClassUtils.invokeMethod(object, "getLeftElement"); //$NON-NLS-1$
		} else if (ClassUtils.hasMethod(object.getClass(), "getLeftParent")) { //$NON-NLS-1$
			leftElement = (EObject)ClassUtils.invokeMethod(object, "getLeftParent"); //$NON-NLS-1$
		}
		
		return leftElement;
	}
	
	/**
	 * Returns the right element of the given {@link EObject}. Will try to
	 * invoke the method called "getRightElement" and, if it fails to find it,
	 * "getRightParent". <code>Null</code> if neither of these methods can be
	 * found.<br/>
	 * This method is intended to be called with a {@link DiffElement} or
	 * {@link MatchElement} as argument.
	 * 
	 * @param object
	 * 			The {@link EObject}.
	 * @return
	 * 			The right element of the given {@link EObject}.
	 */
	public static EObject getRightElement(EObject object) {
		EObject leftElement = null;
		
		if (ClassUtils.hasMethod(object.getClass(), "getRightElement")) { //$NON-NLS-1$
			leftElement = (EObject)ClassUtils.invokeMethod(object, "getRightElement"); //$NON-NLS-1$
		} else if (ClassUtils.hasMethod(object.getClass(), "getRightParent")) { //$NON-NLS-1$
			leftElement = (EObject)ClassUtils.invokeMethod(object, "getRightParent"); //$NON-NLS-1$
		}
		
		return leftElement;
	}
	
	private static AdapterFactoryLabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new AdapterFactoryLabelProvider(EMFAdapterFactoryProvider.getAdapterFactory());
		}
		return labelProvider;
	}
}
