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
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class MergeInputData extends AbstractInputData {
	public Resource getComplexLeft() throws IOException {
		return loadFromClassloader("complex/merge_complex_left.nodes");
	}

	public Resource getComplexOrigin() throws IOException {
		return loadFromClassloader("complex/merge_complex_origin.nodes");
	}

	public Resource getComplexRight() throws IOException {
		return loadFromClassloader("complex/merge_complex_right.nodes");
	}
}
