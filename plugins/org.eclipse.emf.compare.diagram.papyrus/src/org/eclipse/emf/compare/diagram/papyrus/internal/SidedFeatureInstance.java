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
package org.eclipse.emf.compare.diagram.papyrus.internal;

import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Instance of a feature for an EObject on a side of a comparison.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
class SidedFeatureInstance extends FeatureInstance {
	/** The side. */
	private final DifferenceSource side;

	/**
	 * Constructor.
	 * 
	 * @param eObject
	 *            The EObject
	 * @param feature
	 *            The feature
	 * @param side
	 *            The side
	 */
	public SidedFeatureInstance(EObject eObject, EStructuralFeature feature, DifferenceSource side) {
		super(eObject, feature);
		this.side = side;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// CHECKSTYLE:OFF Code generated by JDT
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		// CHECKSTYLE:ON
		return result;
	}

	// CHECKSTYLE:OFF Code generated by JDT
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SidedFeatureInstance other = (SidedFeatureInstance)obj;
		if (side != other.side) {
			return false;
		}
		return true;
		// CHECKSTYLE:ON
	}

	@Override
	public String toString() {
		return super.toString() + '-' + side.getName();
	}
}