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
package org.eclipse.emf.compare.req;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;

/**
 * This class defines the general contract of a Requirements engine. We expect subclasses to have a public,
 * no-argument default constructor for instantiation.
 * <p>
 * We generally expect that a call to {@link #computeRequirements(Comparison)} will complete every single
 * {@link org.eclipse.emf.compare.Diff} it finds with all required differences that can be detected on its
 * sides.
 * </p>
 * <p>
 * Clients can also subclass the {@link DefaultReqEngine default implementation}.
 * </p>
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @see DefaultReqEngine
 */
public interface IReqEngine {

	/**
	 * This is the entry point of the requirements computing process.
	 * <p>
	 * It will complete the input <code>comparison</code> by iterating over the
	 * {@link org.eclipse.emf.compare.Diff differences} it contains, filling in the requirements it can detect
	 * for each distinct Diff.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void computeRequirements(Comparison comparison, Monitor monitor);
}
