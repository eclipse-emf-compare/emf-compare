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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.util.concurrent.FutureCallback;

/**
 * Encapsulates the logic of a computation that can be identified by a key.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <T>
 *            The type of the keys used to identify the computations.
 */
public interface IComputation<T> extends Runnable {

	/**
	 * The computation identifier.
	 * 
	 * @return The computation identifier.
	 */
	T getKey();

	/**
	 * Post-treatment.
	 * 
	 * @return The post-tretament to run when this computation is over, whatever its outcome. This should be
	 *         called by the "framework" in a finally clause to guarantee it is always executed. It is allowed
	 *         for implementors to return {@code null}.
	 */
	FutureCallback<Object> getPostTreatment();
}
