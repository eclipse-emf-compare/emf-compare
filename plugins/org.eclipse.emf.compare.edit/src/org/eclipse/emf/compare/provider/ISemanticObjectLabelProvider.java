/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider;

/**
 * Provider of labels for semantic objects.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 4.2
 */
public interface ISemanticObjectLabelProvider {

	/**
	 * Returns the label for the given object.
	 * 
	 * @param object
	 *            The object to evaluate the label for
	 * @return the label
	 */
	String getSemanticObjectLabel(Object object);

}
