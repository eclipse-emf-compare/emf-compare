/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 479449
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
		return loadFromClassLoader("a1/conflict_a1_attribute_left.nodes");
	}

	public Resource getA1AttributeOrigin() throws IOException {
		return loadFromClassLoader("a1/conflict_a1_attribute_origin.nodes");
	}

	public Resource getA1AttributeRight() throws IOException {
		return loadFromClassLoader("a1/conflict_a1_attribute_right.nodes");
	}

	public Resource getA1ReferenceLeft() throws IOException {
		return loadFromClassLoader("a1/conflict_a1_reference_left.nodes");
	}

	public Resource getA1ReferenceOrigin() throws IOException {
		return loadFromClassLoader("a1/conflict_a1_reference_origin.nodes");
	}

	public Resource getA1ReferenceRight() throws IOException {
		return loadFromClassLoader("a1/conflict_a1_reference_right.nodes");
	}

	public Resource getA2AttributeLeft() throws IOException {
		return loadFromClassLoader("a2/conflict_a2_attribute_left.nodes");
	}

	public Resource getA2AttributeOrigin() throws IOException {
		return loadFromClassLoader("a2/conflict_a2_attribute_origin.nodes");
	}

	public Resource getA2AttributeRight() throws IOException {
		return loadFromClassLoader("a2/conflict_a2_attribute_right.nodes");
	}

	public Resource getA2ReferenceLeft() throws IOException {
		return loadFromClassLoader("a2/conflict_a2_reference_left.nodes");
	}

	public Resource getA2ReferenceOrigin() throws IOException {
		return loadFromClassLoader("a2/conflict_a2_reference_origin.nodes");
	}

	public Resource getA2ReferenceRight() throws IOException {
		return loadFromClassLoader("a2/conflict_a2_reference_right.nodes");
	}

	public Resource getA3AttributeLeft() throws IOException {
		return loadFromClassLoader("a3/conflict_a3_attribute_left.nodes");
	}

	public Resource getA3AttributeOrigin() throws IOException {
		return loadFromClassLoader("a3/conflict_a3_attribute_origin.nodes");
	}

	public Resource getA3AttributeRight() throws IOException {
		return loadFromClassLoader("a3/conflict_a3_attribute_right.nodes");
	}

	public Resource getA3ReferenceLeft() throws IOException {
		return loadFromClassLoader("a3/conflict_a3_reference_left.nodes");
	}

	public Resource getA3ReferenceOrigin() throws IOException {
		return loadFromClassLoader("a3/conflict_a3_reference_origin.nodes");
	}

	public Resource getA3ReferenceRight() throws IOException {
		return loadFromClassLoader("a3/conflict_a3_reference_right.nodes");
	}

	public Resource getB1AttributeLeft() throws IOException {
		return loadFromClassLoader("b1/conflict_b1_attribute_left.nodes");
	}

	public Resource getB1AttributeOrigin() throws IOException {
		return loadFromClassLoader("b1/conflict_b1_attribute_origin.nodes");
	}

	public Resource getB1AttributeRight() throws IOException {
		return loadFromClassLoader("b1/conflict_b1_attribute_right.nodes");
	}

	public Resource getB1ReferenceLeft() throws IOException {
		return loadFromClassLoader("b1/conflict_b1_reference_left.nodes");
	}

	public Resource getB1ReferenceOrigin() throws IOException {
		return loadFromClassLoader("b1/conflict_b1_reference_origin.nodes");
	}

	public Resource getB1ReferenceRight() throws IOException {
		return loadFromClassLoader("b1/conflict_b1_reference_right.nodes");
	}

	public Resource getB2AttributeLeft() throws IOException {
		return loadFromClassLoader("b2/conflict_b2_attribute_left.nodes");
	}

	public Resource getB2AttributeOrigin() throws IOException {
		return loadFromClassLoader("b2/conflict_b2_attribute_origin.nodes");
	}

	public Resource getB2AttributeRight() throws IOException {
		return loadFromClassLoader("b2/conflict_b2_attribute_right.nodes");
	}

	public Resource getB2ReferenceLeft() throws IOException {
		return loadFromClassLoader("b2/conflict_b2_reference_left.nodes");
	}

	public Resource getB2ReferenceOrigin() throws IOException {
		return loadFromClassLoader("b2/conflict_b2_reference_origin.nodes");
	}

	public Resource getB2ReferenceRight() throws IOException {
		return loadFromClassLoader("b2/conflict_b2_reference_right.nodes");
	}

	public Resource getB3AttributeLeft() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_attribute_left.nodes");
	}

	public Resource getB3AttributeOrigin() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_attribute_origin.nodes");
	}

	public Resource getB3AttributeRight() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_attribute_right.nodes");
	}

	public Resource getB3ReferenceLeft() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_reference_left.nodes");
	}

	public Resource getB3ReferenceOrigin() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_reference_origin.nodes");
	}

	public Resource getB3ReferenceRight() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_reference_right.nodes");
	}

	public Resource getB3ContainmentReferenceLeft() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_containment_reference_left.nodes");
	}

	public Resource getB3ContainmentReferenceOrigin() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_containment_reference_origin.nodes");
	}

	public Resource getB3ContainmentReferenceRight() throws IOException {
		return loadFromClassLoader("b3/conflict_b3_containment_reference_right.nodes");
	}

	public Resource getB4AttributeLeft() throws IOException {
		return loadFromClassLoader("b4/conflict_b4_attribute_left.nodes");
	}

	public Resource getB4AttributeOrigin() throws IOException {
		return loadFromClassLoader("b4/conflict_b4_attribute_origin.nodes");
	}

	public Resource getB4AttributeRight() throws IOException {
		return loadFromClassLoader("b4/conflict_b4_attribute_right.nodes");
	}

	public Resource getB4ReferenceLeft() throws IOException {
		return loadFromClassLoader("b4/conflict_b4_reference_left.nodes");
	}

	public Resource getB4ReferenceOrigin() throws IOException {
		return loadFromClassLoader("b4/conflict_b4_reference_origin.nodes");
	}

	public Resource getB4ReferenceRight() throws IOException {
		return loadFromClassLoader("b4/conflict_b4_reference_right.nodes");
	}

	public Resource getB5AttributeLeft() throws IOException {
		return loadFromClassLoader("b5/conflict_b5_attribute_left.nodes");
	}

	public Resource getB5AttributeOrigin() throws IOException {
		return loadFromClassLoader("b5/conflict_b5_attribute_origin.nodes");
	}

	public Resource getB5AttributeRight() throws IOException {
		return loadFromClassLoader("b5/conflict_b5_attribute_right.nodes");
	}

	public Resource getB5ReferenceLeft() throws IOException {
		return loadFromClassLoader("b5/conflict_b5_reference_left.nodes");
	}

	public Resource getB5ReferenceOrigin() throws IOException {
		return loadFromClassLoader("b5/conflict_b5_reference_origin.nodes");
	}

	public Resource getB5ReferenceRight() throws IOException {
		return loadFromClassLoader("b5/conflict_b5_reference_right.nodes");
	}

	public Resource getB6AttributeLeft() throws IOException {
		return loadFromClassLoader("b6/conflict_b6_attribute_left.nodes");
	}

	public Resource getB6AttributeOrigin() throws IOException {
		return loadFromClassLoader("b6/conflict_b6_attribute_origin.nodes");
	}

	public Resource getB6AttributeRight() throws IOException {
		return loadFromClassLoader("b6/conflict_b6_attribute_right.nodes");
	}

	public Resource getB6ReferenceLeft() throws IOException {
		return loadFromClassLoader("b6/conflict_b6_reference_left.nodes");
	}

	public Resource getB6ReferenceOrigin() throws IOException {
		return loadFromClassLoader("b6/conflict_b6_reference_origin.nodes");
	}

	public Resource getB6ReferenceRight() throws IOException {
		return loadFromClassLoader("b6/conflict_b6_reference_right.nodes");
	}

	public Resource getC1AttributeLeft() throws IOException {
		return loadFromClassLoader("c1/conflict_c1_attribute_left.nodes");
	}

	public Resource getC1AttributeOrigin() throws IOException {
		return loadFromClassLoader("c1/conflict_c1_attribute_origin.nodes");
	}

	public Resource getC1AttributeRight() throws IOException {
		return loadFromClassLoader("c1/conflict_c1_attribute_right.nodes");
	}

	public Resource getC1ReferenceLeft() throws IOException {
		return loadFromClassLoader("c1/conflict_c1_reference_left.nodes");
	}

	public Resource getC1ReferenceOrigin() throws IOException {
		return loadFromClassLoader("c1/conflict_c1_reference_origin.nodes");
	}

	public Resource getC1ReferenceRight() throws IOException {
		return loadFromClassLoader("c1/conflict_c1_reference_right.nodes");
	}

	public Resource getC2AttributeLeft() throws IOException {
		return loadFromClassLoader("c2/conflict_c2_attribute_left.nodes");
	}

	public Resource getC2AttributeOrigin() throws IOException {
		return loadFromClassLoader("c2/conflict_c2_attribute_origin.nodes");
	}

	public Resource getC2AttributeRight() throws IOException {
		return loadFromClassLoader("c2/conflict_c2_attribute_right.nodes");
	}

	public Resource getC2ReferenceLeft() throws IOException {
		return loadFromClassLoader("c2/conflict_c2_reference_left.nodes");
	}

	public Resource getC2ReferenceOrigin() throws IOException {
		return loadFromClassLoader("c2/conflict_c2_reference_origin.nodes");
	}

	public Resource getC2ReferenceRight() throws IOException {
		return loadFromClassLoader("c2/conflict_c2_reference_right.nodes");
	}

	public Resource getC3AttributeLeft() throws IOException {
		return loadFromClassLoader("c3/conflict_c3_attribute_left.nodes");
	}

	public Resource getC3AttributeOrigin() throws IOException {
		return loadFromClassLoader("c3/conflict_c3_attribute_origin.nodes");
	}

	public Resource getC3AttributeRight() throws IOException {
		return loadFromClassLoader("c3/conflict_c3_attribute_right.nodes");
	}

	public Resource getC3ReferenceLeft() throws IOException {
		return loadFromClassLoader("c3/conflict_c3_reference_left.nodes");
	}

	public Resource getC3ReferenceOrigin() throws IOException {
		return loadFromClassLoader("c3/conflict_c3_reference_origin.nodes");
	}

	public Resource getC3ReferenceRight() throws IOException {
		return loadFromClassLoader("c3/conflict_c3_reference_right.nodes");
	}

	public Resource getC4AttributeLeft() throws IOException {
		return loadFromClassLoader("c4/conflict_c4_attribute_left.nodes");
	}

	public Resource getC4AttributeOrigin() throws IOException {
		return loadFromClassLoader("c4/conflict_c4_attribute_origin.nodes");
	}

	public Resource getC4AttributeRight() throws IOException {
		return loadFromClassLoader("c4/conflict_c4_attribute_right.nodes");
	}

	public Resource getC4ReferenceLeft() throws IOException {
		return loadFromClassLoader("c4/conflict_c4_reference_left.nodes");
	}

	public Resource getC4ReferenceOrigin() throws IOException {
		return loadFromClassLoader("c4/conflict_c4_reference_origin.nodes");
	}

	public Resource getC4ReferenceRight() throws IOException {
		return loadFromClassLoader("c4/conflict_c4_reference_right.nodes");
	}

	public Resource getC5AttributeLeft() throws IOException {
		return loadFromClassLoader("c5/conflict_c5_attribute_left.nodes");
	}

	public Resource getC5AttributeOrigin() throws IOException {
		return loadFromClassLoader("c5/conflict_c5_attribute_origin.nodes");
	}

	public Resource getC5AttributeRight() throws IOException {
		return loadFromClassLoader("c5/conflict_c5_attribute_right.nodes");
	}

	public Resource getC5ReferenceLeft() throws IOException {
		return loadFromClassLoader("c5/conflict_c5_reference_left.nodes");
	}

	public Resource getC5ReferenceOrigin() throws IOException {
		return loadFromClassLoader("c5/conflict_c5_reference_origin.nodes");
	}

	public Resource getC5ReferenceRight() throws IOException {
		return loadFromClassLoader("c5/conflict_c5_reference_right.nodes");
	}

	public Resource getD1AttributeLeft() throws IOException {
		return loadFromClassLoader("d1/conflict_d1_attribute_left.nodes");
	}

	public Resource getD1AttributeOrigin() throws IOException {
		return loadFromClassLoader("d1/conflict_d1_attribute_origin.nodes");
	}

	public Resource getD1AttributeRight() throws IOException {
		return loadFromClassLoader("d1/conflict_d1_attribute_right.nodes");
	}

	public Resource getD1ReferenceLeft() throws IOException {
		return loadFromClassLoader("d1/conflict_d1_reference_left.nodes");
	}

	public Resource getD1ReferenceOrigin() throws IOException {
		return loadFromClassLoader("d1/conflict_d1_reference_origin.nodes");
	}

	public Resource getD1ReferenceRight() throws IOException {
		return loadFromClassLoader("d1/conflict_d1_reference_right.nodes");
	}

	public Resource getD2AttributeLeft() throws IOException {
		return loadFromClassLoader("d2/conflict_d2_attribute_left.nodes");
	}

	public Resource getD2AttributeOrigin() throws IOException {
		return loadFromClassLoader("d2/conflict_d2_attribute_origin.nodes");
	}

	public Resource getD2AttributeRight() throws IOException {
		return loadFromClassLoader("d2/conflict_d2_attribute_right.nodes");
	}

	public Resource getD2ReferenceLeft() throws IOException {
		return loadFromClassLoader("d2/conflict_d2_reference_left.nodes");
	}

	public Resource getD2ReferenceOrigin() throws IOException {
		return loadFromClassLoader("d2/conflict_d2_reference_origin.nodes");
	}

	public Resource getD2ReferenceRight() throws IOException {
		return loadFromClassLoader("d2/conflict_d2_reference_right.nodes");
	}

	public Resource getD3AttributeLeft() throws IOException {
		return loadFromClassLoader("d3/conflict_d3_attribute_left.nodes");
	}

	public Resource getD3AttributeOrigin() throws IOException {
		return loadFromClassLoader("d3/conflict_d3_attribute_origin.nodes");
	}

	public Resource getD3AttributeRight() throws IOException {
		return loadFromClassLoader("d3/conflict_d3_attribute_right.nodes");
	}

	public Resource getD3ReferenceLeft() throws IOException {
		return loadFromClassLoader("d3/conflict_d3_reference_left.nodes");
	}

	public Resource getD3ReferenceOrigin() throws IOException {
		return loadFromClassLoader("d3/conflict_d3_reference_origin.nodes");
	}

	public Resource getD3ReferenceRight() throws IOException {
		return loadFromClassLoader("d3/conflict_d3_reference_right.nodes");
	}

	public Resource getD4AttributeLeft() throws IOException {
		return loadFromClassLoader("d4/conflict_d4_attribute_left.nodes");
	}

	public Resource getD4AttributeOrigin() throws IOException {
		return loadFromClassLoader("d4/conflict_d4_attribute_origin.nodes");
	}

	public Resource getD4AttributeRight() throws IOException {
		return loadFromClassLoader("d4/conflict_d4_attribute_right.nodes");
	}

	public Resource getD4ReferenceLeft() throws IOException {
		return loadFromClassLoader("d4/conflict_d4_reference_left.nodes");
	}

	public Resource getD4ReferenceOrigin() throws IOException {
		return loadFromClassLoader("d4/conflict_d4_reference_origin.nodes");
	}

	public Resource getD4ReferenceRight() throws IOException {
		return loadFromClassLoader("d4/conflict_d4_reference_right.nodes");
	}

	public Resource getD5AttributeLeft() throws IOException {
		return loadFromClassLoader("d5/conflict_d5_attribute_left.nodes");
	}

	public Resource getD5AttributeOrigin() throws IOException {
		return loadFromClassLoader("d5/conflict_d5_attribute_origin.nodes");
	}

	public Resource getD5AttributeRight() throws IOException {
		return loadFromClassLoader("d5/conflict_d5_attribute_right.nodes");
	}

	public Resource getD5ReferenceLeft() throws IOException {
		return loadFromClassLoader("d5/conflict_d5_reference_left.nodes");
	}

	public Resource getD5ReferenceOrigin() throws IOException {
		return loadFromClassLoader("d5/conflict_d5_reference_origin.nodes");
	}

	public Resource getD5ReferenceRight() throws IOException {
		return loadFromClassLoader("d5/conflict_d5_reference_right.nodes");
	}

	public Resource getD6AttributeLeft() throws IOException {
		return loadFromClassLoader("d6/conflict_d6_attribute_left.nodes");
	}

	public Resource getD6AttributeOrigin() throws IOException {
		return loadFromClassLoader("d6/conflict_d6_attribute_origin.nodes");
	}

	public Resource getD6AttributeRight() throws IOException {
		return loadFromClassLoader("d6/conflict_d6_attribute_right.nodes");
	}

	public Resource getD6ReferenceLeft() throws IOException {
		return loadFromClassLoader("d6/conflict_d6_reference_left.nodes");
	}

	public Resource getD6ReferenceOrigin() throws IOException {
		return loadFromClassLoader("d6/conflict_d6_reference_origin.nodes");
	}

	public Resource getD6ReferenceRight() throws IOException {
		return loadFromClassLoader("d6/conflict_d6_reference_right.nodes");
	}

	public Resource getE1Left() throws IOException {
		return loadFromClassLoader("e1/conflict_e1_left.nodes");
	}

	public Resource getE1Origin() throws IOException {
		return loadFromClassLoader("e1/conflict_e1_origin.nodes");
	}

	public Resource getE1Right() throws IOException {
		return loadFromClassLoader("e1/conflict_e1_right.nodes");
	}

	public Resource getE2Left() throws IOException {
		return loadFromClassLoader("e2/conflict_e2_left.nodes");
	}

	public Resource getE2Origin() throws IOException {
		return loadFromClassLoader("e2/conflict_e2_origin.nodes");
	}

	public Resource getE2Right() throws IOException {
		return loadFromClassLoader("e2/conflict_e2_right.nodes");
	}

	public Resource getFLeft() throws IOException {
		return loadFromClassLoader("f/conflict_f_left.nodes");
	}

	public Resource getFOrigin() throws IOException {
		return loadFromClassLoader("f/conflict_f_origin.nodes");
	}

	public Resource getFRight() throws IOException {
		return loadFromClassLoader("f/conflict_f_right.nodes");
	}

	public Resource getGLeft() throws IOException {
		return loadFromClassLoader("g/conflict_g_left.nodes");
	}

	public Resource getGOrigin() throws IOException {
		return loadFromClassLoader("g/conflict_g_origin.nodes");
	}

	public Resource getGRight() throws IOException {
		return loadFromClassLoader("g/conflict_g_right.nodes");
	}

	public Resource getH1Left() throws IOException {
		return loadFromClassLoader("h1/conflict_h1_left.nodes");
	}

	public Resource getH1Origin() throws IOException {
		return loadFromClassLoader("h1/conflict_h1_origin.nodes");
	}

	public Resource getH1Right() throws IOException {
		return loadFromClassLoader("h1/conflict_h1_right.nodes");
	}

	public Resource getH2Left() throws IOException {
		return loadFromClassLoader("h2/conflict_h2_left.nodes");
	}

	public Resource getH2Origin() throws IOException {
		return loadFromClassLoader("h2/conflict_h2_origin.nodes");
	}

	public Resource getH2Right() throws IOException {
		return loadFromClassLoader("h2/conflict_h2_right.nodes");
	}

	public Resource getILeft() throws IOException {
		return loadFromClassLoader("i/conflict_i_left.nodes");
	}

	public Resource getIOrigin() throws IOException {
		return loadFromClassLoader("i/conflict_i_origin.nodes");
	}

	public Resource getIRight() throws IOException {
		return loadFromClassLoader("i/conflict_i_right.nodes");
	}

	public Resource getJLeft() throws IOException {
		return loadFromClassLoader("j/conflict_j_left.nodes");
	}

	public Resource getJOrigin() throws IOException {
		return loadFromClassLoader("j/conflict_j_origin.nodes");
	}

	public Resource getJRight() throws IOException {
		return loadFromClassLoader("j/conflict_j_right.nodes");
	}

	public Resource getK1Left() throws IOException {
		return loadFromClassLoader("k1/conflict_k1_left.nodes");
	}

	public Resource getK1Origin() throws IOException {
		return loadFromClassLoader("k1/conflict_k1_origin.nodes");
	}

	public Resource getK1Right() throws IOException {
		return loadFromClassLoader("k1/conflict_k1_right.nodes");
	}

	public Resource getK2Left() throws IOException {
		return loadFromClassLoader("k2/conflict_k2_left.nodes");
	}

	public Resource getK2Origin() throws IOException {
		return loadFromClassLoader("k2/conflict_k2_origin.nodes");
	}

	public Resource getK2Right() throws IOException {
		return loadFromClassLoader("k2/conflict_k2_right.nodes");
	}

	public Resource getK3Left() throws IOException {
		return loadFromClassLoader("k3/conflict_k3_left.nodes");
	}

	public Resource getK3Origin() throws IOException {
		return loadFromClassLoader("k3/conflict_k3_origin.nodes");
	}

	public Resource getK3Right() throws IOException {
		return loadFromClassLoader("k3/conflict_k3_right.nodes");
	}

	public Resource getK4Left() throws IOException {
		return loadFromClassLoader("k4/conflict_k4_left.nodes");
	}

	public Resource getK4Origin() throws IOException {
		return loadFromClassLoader("k4/conflict_k4_origin.nodes");
	}

	public Resource getK4Right() throws IOException {
		return loadFromClassLoader("k4/conflict_k4_right.nodes");
	}

	public Resource getComplexLeft() throws IOException {
		return loadFromClassLoader("complex/conflict_complex_left.nodes");
	}

	public Resource getComplexOrigin() throws IOException {
		return loadFromClassLoader("complex/conflict_complex_origin.nodes");
	}

	public Resource getComplexRight() throws IOException {
		return loadFromClassLoader("complex/conflict_complex_right.nodes");
	}
}
