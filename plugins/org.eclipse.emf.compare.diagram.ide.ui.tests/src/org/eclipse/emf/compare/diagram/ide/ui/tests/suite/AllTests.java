/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.tests.suite;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.diagram.ide.ui.tests.suite.BugsTestSuite;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BugsTestSuite.class })
public class AllTests {

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, NodesPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("nodes", //$NON-NLS-1$
				new NodesResourceFactoryImpl());
	}
}
