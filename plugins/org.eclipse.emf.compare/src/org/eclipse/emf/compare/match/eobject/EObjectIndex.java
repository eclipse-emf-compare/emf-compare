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
package org.eclipse.emf.compare.match.eobject;

import java.util.Map;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EObject;

/**
 * An EObjectIndex has for responsability to store/remove EObjects and return the closest EObject from another
 * one (each one being registered with a different Side.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface EObjectIndex {
	/**
	 * return the list of EObjects of a given side still available in the index.
	 * 
	 * @param side
	 *            the side we are looking for.
	 * @return the list of EObjects of a given side still available in the index.
	 */
	Iterable<EObject> getValuesStillThere(Side side);

	/**
	 * Return the closest EObjects found in other sides than the one given.
	 * 
	 * @param inProgress
	 *            the comparison currently being computed. It will not be changed directly but only queried to
	 *            know if some element has already been matched or not.
	 * @param eObj
	 *            the base EObject used to lookup similar ones.
	 * @param side
	 *            the side of the passed EObject.
	 * @return a map of Side, EObjects, returning all the found objects (and the passed one) which are the
	 *         closests.
	 */
	Map<Side, EObject> findClosests(Comparison inProgress, EObject eObj, Side side);

	/**
	 * Remove an object from the index.
	 * 
	 * @param eObj
	 *            object to remove.
	 * @param side
	 *            Side in which this object was.
	 */
	void remove(EObject eObj, Side side);

	/**
	 * Register an Object in the index with the given side.
	 * 
	 * @param eObj
	 *            the {@link EObject} to register.
	 * @param side
	 *            the side in which it should be registered.
	 */
	void index(EObject eObj, Side side);

	/**
	 * An enumeration used in the API to specify sides.
	 */
	enum Side {
		/**
		 * the left side.
		 */
		LEFT,
		/**
		 * The right side.
		 */
		RIGHT,
		/**
		 * The origin side (also known as ancestor).
		 */
		ORIGIN
	}

}
