/*******************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.logical;

import org.eclipse.emf.compare.ide.logical.IModelInclusionTester;

/**
 * Abstract super class for {@link IModelInclusionTester}.
 * <p>
 * This class is not intended to be sub-classed by clients but is only used as a base class for representing
 * model inclusion testers internally.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public abstract class AbstractModelInclusionTester implements IModelInclusionTester {

	/** The key of this model inclusion tester. */
	private final String key;

	/**
	 * Creates a model inclusion tester with the specified <code>key</code>.
	 * 
	 * @param key
	 *            The key.
	 */
	public AbstractModelInclusionTester(String key) {
		this.key = key;
	}

	/**
	 * Returns the key of this model inclusion tester.
	 * 
	 * @return The key.
	 */
	public String getKey() {
		return key;
	}

}
