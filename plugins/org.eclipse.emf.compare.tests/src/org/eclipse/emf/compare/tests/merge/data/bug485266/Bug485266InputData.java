/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge.data.bug485266;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class Bug485266InputData extends AbstractInputData {

	public Resource getData1AncestorResource() throws IOException {
		return loadFromClassLoader("data1/ancestor.ecore"); //$NON-NLS-1$
	}

	public Resource getData1LeftResource() throws IOException {
		return loadFromClassLoader("data1/left.ecore"); //$NON-NLS-1$
	}

	public Resource getData1RightResource() throws IOException {
		return loadFromClassLoader("data1/right.ecore"); //$NON-NLS-1$
	}

	public Resource getData1ResultResource() throws IOException {
		return loadFromClassLoader("data1/result.ecore"); //$NON-NLS-1$
	}

	public Resource getData2AncestorResource() throws IOException {
		return loadFromClassLoader("data2/ancestor.ecore"); //$NON-NLS-1$
	}

	public Resource getData2LeftResource() throws IOException {
		return loadFromClassLoader("data2/left.ecore"); //$NON-NLS-1$
	}

	public Resource getData2RightResource() throws IOException {
		return loadFromClassLoader("data2/right.ecore"); //$NON-NLS-1$
	}

	public Resource getData2ResultResource() throws IOException {
		return loadFromClassLoader("data2/result.ecore"); //$NON-NLS-1$
	}

	public Resource getData3AncestorResource() throws IOException {
		return loadFromClassLoader("data3/ancestor.ecore"); //$NON-NLS-1$
	}

	public Resource getData3LeftResource() throws IOException {
		return loadFromClassLoader("data3/left.ecore"); //$NON-NLS-1$
	}

	public Resource getData3RightResource() throws IOException {
		return loadFromClassLoader("data3/right.ecore"); //$NON-NLS-1$
	}

	public Resource getData3ResultResource() throws IOException {
		return loadFromClassLoader("data3/result.ecore"); //$NON-NLS-1$
	}

	public Resource getData4AncestorResource() throws IOException {
		return loadFromClassLoader("data4/ancestor.ecore"); //$NON-NLS-1$
	}

	public Resource getData4LeftResource() throws IOException {
		return loadFromClassLoader("data4/left.ecore"); //$NON-NLS-1$
	}

	public Resource getData4RightResource() throws IOException {
		return loadFromClassLoader("data4/right.ecore"); //$NON-NLS-1$
	}

	public Resource getData4ResultResource() throws IOException {
		return loadFromClassLoader("data4/result.ecore"); //$NON-NLS-1$
	}

	public Resource getData5AncestorResource() throws IOException {
		return loadFromClassLoader("data5/ancestor.ecore"); //$NON-NLS-1$
	}

	public Resource getData5LeftResource() throws IOException {
		return loadFromClassLoader("data5/left.ecore"); //$NON-NLS-1$
	}

	public Resource getData5RightResource() throws IOException {
		return loadFromClassLoader("data5/right.ecore"); //$NON-NLS-1$
	}

	public Resource getData5ResultResource() throws IOException {
		return loadFromClassLoader("data5/result.ecore"); //$NON-NLS-1$
	}
}
