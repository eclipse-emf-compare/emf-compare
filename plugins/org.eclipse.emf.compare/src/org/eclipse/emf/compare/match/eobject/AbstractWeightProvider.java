/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

/**
 * Abstract implementation which is parameterized to set weights based on features, to ignore features and
 * consider "name" features as more important.
 * 
 * @since 3.1.0
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class AbstractWeightProvider implements WeightProvider {

	/***
	 * Something not impacting the object identity unless it adds up a lot.
	 */
	public static final int SMALL = 5;

	/**
	 * A normal change in an object.
	 */
	public static final int NORMAL = 10;

	/**
	 * Likely to impact the object identity.
	 */
	public static final int SIGNIFICANT = 20;

	/**
	 * Quite important regarding the Object identity.
	 */
	public static final int MAJOR = 150;

	/**
	 * Very important regarding the Object identity.
	 */
	public static final int MASSIVE = 350;

	/**
	 * It is very unlikely the elements are matching if they have differences of this magnitude.
	 */
	public static final int UNLIKELY_TO_MATCH = 1000;

}
