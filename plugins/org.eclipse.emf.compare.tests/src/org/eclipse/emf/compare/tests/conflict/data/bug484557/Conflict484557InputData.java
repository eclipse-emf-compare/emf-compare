/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.conflict.data.bug484557;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class Conflict484557InputData extends AbstractInputData {

	public Resource getAttributeAncestorResource() throws IOException {
		return loadFromClassLoader("att_ancestor.nodes"); //$NON-NLS-1$
	}

	public Resource getAttributeLeftResource() throws IOException {
		return loadFromClassLoader("att_left.nodes"); //$NON-NLS-1$
	}

	public Resource getAttributeRightResource() throws IOException {
		return loadFromClassLoader("att_right.nodes"); //$NON-NLS-1$
	}

	public Resource getFeatureMapAncestorResource() throws IOException {
		return loadFromClassLoader("fm_ancestor.nodes"); //$NON-NLS-1$
	}

	public Resource getFeatureMapLeftResource() throws IOException {
		return loadFromClassLoader("fm_left.nodes"); //$NON-NLS-1$
	}

	public Resource getFeatureMapRightResource() throws IOException {
		return loadFromClassLoader("fm_right.nodes"); //$NON-NLS-1$
	}

	public Resource getSingleRefAncestorResource() throws IOException {
		return loadFromClassLoader("ref_ancestor.nodes"); //$NON-NLS-1$
	}

	public Resource getSingleRefLeftResource() throws IOException {
		return loadFromClassLoader("ref_left.nodes"); //$NON-NLS-1$
	}

	public Resource getSingleRefRightResource() throws IOException {
		return loadFromClassLoader("ref_right.nodes"); //$NON-NLS-1$
	}

	public Resource getMultiRefAncestorResource() throws IOException {
		return loadFromClassLoader("multi_ref_ancestor.nodes"); //$NON-NLS-1$
	}

	public Resource getMultiRefLeftResource() throws IOException {
		return loadFromClassLoader("multi_ref_left.nodes"); //$NON-NLS-1$
	}

	public Resource getMultiRefRightResource() throws IOException {
		return loadFromClassLoader("multi_ref_right.nodes"); //$NON-NLS-1$
	}
}
