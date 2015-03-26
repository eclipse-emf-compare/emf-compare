/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.modelextension.data.noparametermodel;

import java.io.IOException;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.papyrus.infra.core.resource.AbstractModel;
import org.eclipse.papyrus.infra.core.resource.IModel;

/**
 * Test Model Class to test for models without save parameters.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class NoParameterModel extends AbstractModel implements IModel {

	@Override
	public String getIdentifier() {
		return "noparametermodeltest";
	}

	/**
	 * {@inheritDoc}
	 */
	public void changeModelPath(IPath arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void createModel(IPath arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void createModel(URI arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<URI> getModifiedURIs() {
		// Stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void importModel(IPath arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void importModel(URI arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void loadModel(IPath arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void loadModel(URI arg0) {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveModel() throws IOException {
		// Stub
	}

	/**
	 * {@inheritDoc}
	 */
	public void setModelURI(URI arg0) {
		// Stub
	}
}
