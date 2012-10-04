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
package org.eclipse.emf.compare.utils;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;

/**
 * Use to compare objects by the {@link org.eclipse.emf.compare.match.IMatchEngine}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IEqualityHelper extends Adapter {

	/**
	 * Check that the two given values are "equal", considering the specifics of EMF.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if both objects are to be considered equal, <code>false</code> otherwise.
	 */
	boolean matchingValues(Object object1, Object object2);

	/**
	 * This should only be used if the two given Objects are known not to be instances of EObjects. EObjects
	 * passed for comparison through here will be compared through their {@link Object#equals(Object)}
	 * implementation.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if both objects are to be considered equal, <code>false</code> otherwise.
	 */
	boolean matchingAttributeValues(Object object1, Object object2);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	Comparison getTarget();

}
