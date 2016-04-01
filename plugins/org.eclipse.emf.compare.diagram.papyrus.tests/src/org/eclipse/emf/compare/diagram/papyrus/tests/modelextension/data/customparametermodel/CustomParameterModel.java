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
package org.eclipse.emf.compare.diagram.papyrus.tests.modelextension.data.customparametermodel;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.core.resource.AbstractBaseModel;
import org.eclipse.papyrus.infra.core.resource.IModel;

/**
 * Test Model Class to test for models with custom save parameters.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class CustomParameterModel extends AbstractBaseModel implements IModel {

	/**
	 * Save parameter key.
	 */
	private static final String CUSTOM_OPTION_KEY = "customoptionkeytest";

	/**
	 * Save parameter value.
	 */
	private static final String CUSTOM_OPTION_VALUE = "customoptionvaluetest";

	@Override
	protected String getModelFileExtension() {
		return "customparametermodeltest";
	}

	@Override
	public String getIdentifier() {
		return "customparametermodeltest";
	}

	@Override
	protected Map<Object, Object> getSaveOptions() {
		return getSaveParametersForTest();
	}

	/**
	 * Returns the save parameters for this test class.
	 * 
	 * @return The save parameters for this test class.
	 */
	public Map<Object, Object> getSaveParametersForTest() {
		Map<Object, Object> options = super.getSaveOptions();
		options.put(CUSTOM_OPTION_KEY, CUSTOM_OPTION_VALUE);
		return options;
	}

	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	public boolean canPersist(EObject arg0) {
		return false;
	}

	public void persist(EObject arg0) {

	}
}
