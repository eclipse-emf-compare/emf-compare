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
package org.eclipse.emf.compare.internal.spec;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.impl.MatchImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * This specialization of the {@link MatchImpl} class allows us to define the derived features and operations
 * implementations.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchSpec extends MatchImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.MatchImpl#getComparison()
	 */
	@Override
	public Comparison getComparison() {
		Comparison ret = null;

		EObject eContainer = eContainer();
		while (!(eContainer instanceof Comparison) && eContainer != null) {
			eContainer = eContainer.eContainer();
		}

		if (eContainer != null) {
			ret = (Comparison)eContainer;
		}

		return ret;
	}
}
