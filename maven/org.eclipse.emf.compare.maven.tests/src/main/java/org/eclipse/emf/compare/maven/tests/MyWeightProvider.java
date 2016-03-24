/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.maven.tests;

import org.eclipse.emf.compare.match.eobject.EcoreWeightProvider;
import org.eclipse.emf.ecore.EStructuralFeature;

public class MyWeightProvider extends EcoreWeightProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWeight(EStructuralFeature feature) {
		return super.getWeight(feature);
	}
}
