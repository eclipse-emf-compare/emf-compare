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

	public Resource getA1ReferenceLeft() throws IOException {
		return loadFromClassloader("a1/conflict_a1_reference_left.nodes");
	}

	public Resource getA1ReferenceOrigin() throws IOException {
		return loadFromClassloader("a1/conflict_a1_reference_origin.nodes");
	}

	public Resource getA1ReferenceRight() throws IOException {
		return loadFromClassloader("a1/conflict_a1_reference_right.nodes");
	}

	public Resource getA2AttributeLeft() throws IOException {
		return loadFromClassloader("a2/conflict_a2_attribute_left.nodes");
	}

	public Resource getA2AttributeOrigin() throws IOException {
		return loadFromClassloader("a2/conflict_a2_attribute_origin.nodes");
	}

	public Resource getA2AttributeRight() throws IOException {
		return loadFromClassloader("a2/conflict_a2_attribute_right.nodes");
	}

	public Resource getA2ReferenceLeft() throws IOException {
		return loadFromClassloader("a2/conflict_a2_reference_left.nodes");
	}

	public Resource getA2ReferenceOrigin() throws IOException {
		return loadFromClassloader("a2/conflict_a2_reference_origin.nodes");
	}

	public Resource getA2ReferenceRight() throws IOException {
		return loadFromClassloader("a2/conflict_a2_reference_right.nodes");
	}

	public Resource getA3AttributeLeft() throws IOException {
		return loadFromClassloader("a3/conflict_a3_attribute_left.nodes");
	}

	public Resource getA3AttributeOrigin() throws IOException {
		return loadFromClassloader("a3/conflict_a3_attribute_origin.nodes");
	}

	public Resource getA3AttributeRight() throws IOException {
		return loadFromClassloader("a3/conflict_a3_attribute_right.nodes");
	}

	public Resource getA3ReferenceLeft() throws IOException {
		return loadFromClassloader("a3/conflict_a3_reference_left.nodes");
	}

	public Resource getA3ReferenceOrigin() throws IOException {
		return loadFromClassloader("a3/conflict_a3_reference_origin.nodes");
	}

	public Resource getA3ReferenceRight() throws IOException {
		return loadFromClassloader("a3/conflict_a3_reference_right.nodes");
	}

	public Resource getH1Left() throws IOException {
		return loadFromClassloader("h1/conflict_h1_left.nodes");
	}

	public Resource getH1Origin() throws IOException {
		return loadFromClassloader("h1/conflict_h1_origin.nodes");
	}

	public Resource getH1Right() throws IOException {
		return loadFromClassloader("h1/conflict_h1_right.nodes");
	}
}
