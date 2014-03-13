/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework.junit.internal;

import org.junit.runners.model.Statement;

/**
 * This implementation of a Statement allows us to have a result on a JUnit work unit. We'll thus be able to
 * chain statements with the result of the preceding one.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @param <T>
 *            Type of this statement's result.
 */
public abstract class ResultStatement<T> extends Statement {
	/**
	 * This will be used by the framework to retrieve the result of this statement's evaluation.
	 * 
	 * @return The result of this statement's evaluation.
	 */
	public abstract T getResult();
}
