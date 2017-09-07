/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ecoretools.tests.suite;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.diagram.ecoretools.tests.edgechanges.EdgechangesTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.hide.HideTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.merge.ExtensionMergeTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.nodechanges.NodechangesTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.show.ShowTest;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsPackage;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite allows us to launch all tests for EMF Compare at once.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(Suite.class)
@SuiteClasses({HideTest.class, ShowTest.class, NodechangesTest.class, EdgechangesTest.class,
		ExtensionMergeTest.class })
public class AllTests {

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ExtensionsPackage.eNS_URI, ExtensionsPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecorediag", //$NON-NLS-1$
				new GMFResourceFactory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", //$NON-NLS-1$
				new EcoreResourceFactoryImpl());
	}
}
