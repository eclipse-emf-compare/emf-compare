/*******************************************************************************
 * Copyright (c) 2017 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Christian W. Damus - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.util;

import com.google.common.base.Optional;

import java.util.Iterator;
import java.util.List;

/**
 * API that is missing from the Guava {@link Iterables=} class.
 *
 * @author Christian W. Damus
 */
public final class UMLCompareIterables {

	/**
	 * Not instantiable by clients.
	 */
	private UMLCompareIterables() {
		super();
	}

	/**
	 * Obtain the first element, if there is one, of an {@code iterable}.
	 * 
	 * @param iterable
	 *            an iterable
	 * @return optionally its first element
	 * @param <T>
	 *            the element type of the iterable
	 */
	public static <T> Optional<T> tryFirst(Iterable<T> iterable) {
		final Optional<T> result;

		if (iterable instanceof List<?>) {
			List<T> list = (List<T>)iterable;
			if (list.isEmpty()) {
				result = Optional.absent();
			} else {
				result = Optional.of(list.get(0));
			}
		} else {
			Iterator<T> iter = iterable.iterator();
			if (iter.hasNext()) {
				result = Optional.of(iter.next());
			} else {
				result = Optional.absent();
			}
		}

		return result;
	}

}
