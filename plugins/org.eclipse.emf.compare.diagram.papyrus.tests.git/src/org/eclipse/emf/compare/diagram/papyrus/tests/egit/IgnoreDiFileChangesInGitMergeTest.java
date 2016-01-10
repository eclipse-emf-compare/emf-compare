/*******************************************************************************
 * Copyright (C) 2016 EclipseSource Munich Gmbh and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.egit;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Tests that concurrent changes to di-files do not cause conflicts.
 * <dl>
 * <dt>Origin:</dt>
 * <dd>Given is a UML activity diagram with a di-file that contains a page list
 * and sash model windows.</dd>
 * <dt>Left:</dt>
 * <dd>A new diagram is created, which causes the di-file to contain a new sash
 * model window.</dd>
 * <dt>Right:</dt>
 * <dd>Again, a new diagram is created, which causes the di-file to contain a
 * new sash model window.</dd>
 * </dl>
 * <p>
 * Without properly ignoring changes of the di-file, this scenario would lead to
 * a conflict. The requirement, however, dictates that di-file changes should
 * never lead to a conflict (cf. bug 485494).
 * </p>
 *
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class IgnoreDiFileChangesInGitMergeTest extends AbstractGitMergeTestCase {

	@Override
	protected String getTestScenarioPath() {
		return "testmodels/ingore-di-file-changes/"; //$NON-NLS-1$
	}

	@Override
	protected boolean shouldValidate(File file) {
		return false;
	}

	@Override
	protected void validateResult() throws Exception {
		assertTrue(noConflict());
		assertTrue(fileExists("model.di"));
		assertTrue(fileExists("model.notation"));
		assertTrue(fileExists("model.uml"));
	}

	@Override
	protected void validateResult(Resource resource) throws Exception {
		// nothing else to validate
	}
}
