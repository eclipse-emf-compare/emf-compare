/*******************************************************************************
 * Copyright (c) 2014, 2106 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Tanja Mayerhofer - bug 501864
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.tests.suite;

import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.ConflictsGroupTest;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.UMLDifferencesOrderTest;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.profile.DynamicProfileIntegrationDisplayTest;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.profile.StaticProfileIntegrationDisplayTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite allows us to launch all tests for EMF Compare at once.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(Suite.class)
@SuiteClasses({UMLDifferencesOrderTest.class, DynamicProfileIntegrationDisplayTest.class,
		StaticProfileIntegrationDisplayTest.class, ConflictsGroupTest.class })
public class AllTests {

}
