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
package org.eclipse.emf.compare.match;

import org.eclipse.emf.compare.utils.IEqualityHelper;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IEqualityHelperFactory {

	/**
	 * Returns a new {@link IEqualityHelper}.
	 * 
	 * @return a new {@link IEqualityHelper}.
	 */
	IEqualityHelper createEqualityHelper();

}
