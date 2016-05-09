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
 * This interface is used to select mergers according to some criterion.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.4
 */
public interface IMergeCriterionAware {

	/**
	 * Indicate whether a merger supports a given criterion.
	 * 
	 * @param criterion
	 *            A criterion
	 * @return <code>true</code> if the merger is able to handle the given criterion, <code>false</code>
	 *         otherwise.
	 */
	boolean apply(IMergeCriterion criterion);

}
