/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - added further tests
 *     Alexandra Buzila - additional tests
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.suite;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsPackage;
import org.eclipse.emf.compare.diagram.papyrus.tests.conflicts.MoveOfDiagramConflictDetectionTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.IgnoreDiFileChangesInGitMergeTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.ResourceAttachmentChangeAdd1GitMergeTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.ResourceAttachmentChangeAdd2GitMergeTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.ResourceAttachmentChangeDelete1GitMergeTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.ResourceAttachmentChangeDelete2GitMergeTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.StereotypeConflictTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.mergeresolution.MergeResolutionManagerTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.merge.AdditiveMergeDiagramTests;
import org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.implication.AttachmentChangeImplicationTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move.ResourceAttachmentChangeMoveConflictTests;
import org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move.ResourceAttachmentChangeMoveNoConflictTests;
import org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move.ResourceAttachmentChangeMoveOrderTests;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.papyrus.infra.core.sashwindows.di.DiPackage;
import org.eclipse.papyrus.infra.core.sashwindows.di.util.DiResourceFactoryImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuppressWarnings("restriction")
@RunWith(Suite.class)
@SuiteClasses({AttachmentChangeImplicationTest.class, ResourceAttachmentChangeAdd1GitMergeTest.class,
		ResourceAttachmentChangeAdd2GitMergeTest.class, ResourceAttachmentChangeDelete1GitMergeTest.class,
		ResourceAttachmentChangeDelete2GitMergeTest.class, ResourceAttachmentChangeMoveConflictTests.class,
		ResourceAttachmentChangeMoveNoConflictTests.class, ResourceAttachmentChangeMoveOrderTests.class,
		StereotypeConflictTest.class, IgnoreDiFileChangesInGitMergeTest.class,
		MoveOfDiagramConflictDetectionTest.class, AdditiveMergeDiagramTests.class,
		MergeResolutionManagerTest.class, })
public class PapyrusGitTests {

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ExtensionsPackage.eNS_URI, ExtensionsPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(DiPackage.eNS_URI, DiPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("di", //$NON-NLS-1$
				new DiResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("notation", //$NON-NLS-1$
				new GMFResourceFactory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", //$NON-NLS-1$
				new UMLResourceFactoryImpl());
	}
}
