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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor;

import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.ecore.EObject;

/**
 * Interface to specify that the implementing class is wrapping an EObject.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IEObjectAccessor extends ITypedElement {

	/**
	 * Returns the wrapped EObject.
	 * 
	 * @return the wrapped EObject.
	 */
	EObject getEObject();
}
