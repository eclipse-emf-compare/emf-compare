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
package org.eclipse.emf.compare.ide.logical;

import org.eclipse.core.resources.IFile;

/**
 * A model tester specifies whether an {@link IFile} shall be considered as EMF Model or not.
 * <p>
 * Implementations of this interface can be registered for the extension point
 * <code>org.eclipse.emf.compare.ide.modelInclusionTester</code> to specify whether EMF Compare's model
 * provider should be enabled for certain files or not. See extension point description for more details.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 3.4.2
 */
public interface IModelInclusionTester {

	/**
	 * Specifies whether the given <code>file</code> should be included or not.
	 * <p>
	 * It is strongly recommended to keep this method as efficient and simple as possible, because it will be
	 * called several times for existing and potentially inexistent files.
	 * </p>
	 * 
	 * @param file
	 *            The file to test.
	 * @return <code>true</code> if the <code>file</code> should be included, <code>false</code> otherwise.
	 */
	boolean shouldInclude(IFile file);

}
