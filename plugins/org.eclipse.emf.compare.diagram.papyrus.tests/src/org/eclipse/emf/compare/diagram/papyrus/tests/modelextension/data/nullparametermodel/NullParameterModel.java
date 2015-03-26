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
package org.eclipse.emf.compare.diagram.papyrus.tests.modelextension.data.nullparametermodel;

import java.util.Map;

import org.eclipse.papyrus.infra.core.resource.AbstractBaseModel;
import org.eclipse.papyrus.infra.core.resource.IModel;

/**
 * Test Model Class to test for models which return {@code null} as save parameter.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class NullParameterModel extends AbstractBaseModel implements IModel {

	@Override
	protected String getModelFileExtension() {
		return "nullparametermodeltest";
	}

	@Override
	public String getIdentifier() {
		return "nullparametermodeltest";
	}

	@Override
	protected Map<Object, Object> getSaveOptions() {
		return null;
	}

}
