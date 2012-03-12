/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework;

import org.eclipse.emf.ecore.EObject;

/**
 * This represents a "use case" as EMF Compare understands it, i.e : a couple of EObjects.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EObjectCouple {
	/** The left EObject. */
	private final EObject left;

	/** The right EObject. */
	private final EObject right;

	/**
	 * Constructs a use case given the left and right EObjects.
	 * 
	 * @param left
	 *            The left EObject.
	 * @param right
	 *            The right EObject.
	 */
	public EObjectCouple(EObject leftEObject, EObject rightEObject) {
		this.left = leftEObject;
		this.right = rightEObject;
	}

	/**
	 * Returns the left EObject of this use case.
	 * 
	 * @return The left EObject of this use case.
	 */
	public EObject getLeft() {
		return left;
	}

	/**
	 * Returns the right EObject of this use case.
	 * 
	 * @return The right EObject of this use case.
	 */
	public EObject getRight() {
		return right;
	}
}
