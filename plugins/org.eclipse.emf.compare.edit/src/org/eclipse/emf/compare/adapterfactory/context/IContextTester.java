/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.adapterfactory.context;

import java.util.Map;

/**
 * A tester to evaluate whether a given context matches certain criteria.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @since 4.3
 */
public interface IContextTester {

	/**
	 * Key used to store the comparison object in the context.
	 */
	String CTX_COMPARISON = "comparison"; //$NON-NLS-1$

	/**
	 * Returns the result of applying this tester to the given {@code context}.
	 * 
	 * @param context
	 *            a map of context elements stored under specific keys.
	 * @return {@code true} if the given {@code comparison} is of a certain context, {@code false} otherwise.
	 */
	boolean apply(Map<Object, Object> context);
}
