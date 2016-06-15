/*******************************************************************************
 * Copyright (C) 2016 EclipseSource Munich Gmbh and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.egit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Tests that conflicting changes of stereotype attributes are correctly detected.
 * <dl>
 * <dt>Origin:</dt>
 * <dd>Contains a Papyrus model with the ModelElements SysML profile applied. The model contains a
 * ModelElements:View package, containing a ModelElements:ViewPoint class.</dd>
 * <dt>Right:</dt>
 * <dd>The value of the Purpose attribute of the ViewPoint element is changed.</dd>
 * <dt>Left:</dt>
 * <dd>The ViewPoint is deleted.</dd>
 * </dl>
 * <p>
 * These changes should lead to a conflict.
 * </p>
 *
 * @author Alexandra Buzila
 */
public class StereotypeConflictTest extends AbstractGitMergeTestCase {

	@Override
	protected String getTestScenarioPath() {
		return "testmodels/stereotype-changes/conflict/"; //$NON-NLS-1$
	}

	@Override
	protected boolean shouldValidate(File file) {
		return false;
	}

	@Override
	protected void validateResult() throws Exception {
		assertTrue(isConflicting());
		assertTrue(fileExists("model.di"));
		assertTrue(fileExists("model.notation"));
		assertTrue(fileExists("model.uml"));
		Set<String> conflicting = repository.status().getConflicting();
		assertEquals(conflicting.size(), 1);
		assertTrue(conflicting.contains("Project1/model.uml"));
	}

	@Override
	protected void validateResult(Resource resource) throws Exception {
		// do nothing
	}
}
