/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nonregression.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.merge.data.AbstractInputData;
import org.eclipse.emf.ecore.EObject;

@SuppressWarnings("nls")
public class NonRegressionInputData extends AbstractInputData {
	public EObject getBug355897Original() throws IOException {
		String path = "bug355897_1.uml";
		return loadFromClassloader(path);
	}

	public EObject getBug355897Modified() throws IOException {
		String path = "bug355897_2.uml";
		return loadFromClassloader(path);
	}
}
