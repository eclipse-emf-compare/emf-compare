/*******************************************************************************
 * Copyright (C) 2015 EclipseSource Munich Gmbh and Others.
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

/**
 * Extends {@link ResourceAttachmentChangeAdd1GitMergeTest} with the deletion of
 * an unrelated file on the right-hand side.
 *
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ResourceAttachmentChangeAdd2GitMergeTest extends ResourceAttachmentChangeAdd1GitMergeTest {
	@Override
	protected String getTestScenarioPath() {
		return "testmodels/resourceattachmentchange/add2/";
	}
}
