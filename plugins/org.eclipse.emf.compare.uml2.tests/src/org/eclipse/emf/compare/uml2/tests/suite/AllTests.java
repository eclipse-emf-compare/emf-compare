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
package org.eclipse.emf.compare.uml2.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.tests.suite.CompareTestSuite;
import org.eclipse.emf.compare.uml2.tests.association.AddAssociation2Test;
import org.eclipse.emf.compare.uml2.tests.association.AddAssociation3Test;
import org.eclipse.emf.compare.uml2.tests.association.AddAssociationTest;
import org.eclipse.emf.compare.uml2.tests.association.ChangeAssociationTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddAbstractionTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddDependencyTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddInterfaceRealizationTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddRealizationTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddSubstitutionTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddUsageTest;
import org.eclipse.emf.compare.uml2.tests.dependency.ChangeDependencyTest;
import org.eclipse.emf.compare.uml2.tests.dependency.ChangeUsageTest;
import org.eclipse.emf.compare.uml2.tests.executionSpecification.AddActionExecutionSpecificationTest;
import org.eclipse.emf.compare.uml2.tests.executionSpecification.AddBehaviorExecutionSpecificationTest;
import org.eclipse.emf.compare.uml2.tests.extend.AddExtendTest;
import org.eclipse.emf.compare.uml2.tests.generalizationSet.AddGeneralizationSetTest;
import org.eclipse.emf.compare.uml2.tests.include.AddIncludeTest;
import org.eclipse.emf.compare.uml2.tests.message.AddMessageTest;
import org.eclipse.emf.compare.uml2.tests.timeConstraint.AddTimeConstraintTest;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
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
@SuiteClasses({AddDependencyTest.class, AddAbstractionTest.class, AddAssociationTest.class,
		AddAssociation2Test.class, ChangeAssociationTest.class, ChangeDependencyTest.class,
		ChangeUsageTest.class, AddAssociation3Test.class, AddExtendTest.class,
		AddGeneralizationSetTest.class, AddInterfaceRealizationTest.class, AddRealizationTest.class,
		AddSubstitutionTest.class, AddUsageTest.class, AddMessageTest.class,
		AddActionExecutionSpecificationTest.class, AddBehaviorExecutionSpecificationTest.class,
		AddIncludeTest.class, AddTimeConstraintTest.class })
public class AllTests {
	/**
	 * Standalone launcher for all of compare's tests.
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * This will return a suite populated with all tests available through this class.
	 * 
	 * @generated
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(CompareTestSuite.class);
	}

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", //$NON-NLS-1$
				new UMLResourceFactoryImpl());
	}
}
