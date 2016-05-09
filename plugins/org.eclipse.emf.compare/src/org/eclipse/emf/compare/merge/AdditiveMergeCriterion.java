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
package org.eclipse.emf.compare.merge;

/**
 * Merge Criterion that indicates we want to perform an 'Additive' merge.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.4
 */
public final class AdditiveMergeCriterion implements IMergeCriterion {

	/** The singleton instance of this class. */
	public static final AdditiveMergeCriterion INSTANCE = new AdditiveMergeCriterion();

	/**
	 * Private constructor (singleton pattern).
	 */
	private AdditiveMergeCriterion() {
		// Singleton
	}
}
