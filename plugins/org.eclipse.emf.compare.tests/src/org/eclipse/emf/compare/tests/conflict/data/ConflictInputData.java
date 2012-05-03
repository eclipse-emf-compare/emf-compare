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
package org.eclipse.emf.compare.tests.conflict.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This will provide the input model for all of our "conflict detection" tests.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class ConflictInputData extends AbstractInputData {
	public Resource getA1AttributeLeft() throws IOException {
		return loadFromClassloader("a1/conflict_a1_attribute_left.nodes");
	}

	public Resource getA1AttributeOrigin() throws IOException {
		return loadFromClassloader("a1/conflict_a1_attribute_origin.nodes");
	}

	public Resource getA1AttributeRight() throws IOException {
		return loadFromClassloader("a1/conflict_a1_attribute_right.nodes");
	}
}
