/*******************************************************************************
 * Copyright (c) 2019 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent Goubet - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff.data.nonuniquemultivaluedattribute;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class NonUniqueMultiValuedAttributeInputData extends AbstractInputData {
	public Resource getNonUniqueMultiValuedAttributeCaseALeft() throws IOException {
		return loadFromClassLoader("a/left.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseARight() throws IOException {
		return loadFromClassLoader("a/right.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseAOrigin() throws IOException {
		return loadFromClassLoader("a/origin.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseBLeft() throws IOException {
		return loadFromClassLoader("b/left.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseBRight() throws IOException {
		return loadFromClassLoader("b/right.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseBOrigin() throws IOException {
		return loadFromClassLoader("b/origin.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseCLeft() throws IOException {
		return loadFromClassLoader("c/left.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseCRight() throws IOException {
		return loadFromClassLoader("c/right.nodes");
	}

	public Resource getNonUniqueMultiValuedAttributeCaseCOrigin() throws IOException {
		return loadFromClassLoader("c/origin.nodes");
	}
}
