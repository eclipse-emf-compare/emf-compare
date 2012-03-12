/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DanglingReferenceOnTwoAdds extends MergeTestBase {

	@Override
	public void setUp() throws Exception {

		super.setUp();

		// We create an EPackage with 4 classes. The fourth one references the first three in its ESuperTypes
		// attribute
		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage p = factory.createEPackage();
		p.setName("TestPackage1");
		EClass class0 = factory.createEClass();
		class0.setName("Class0");

		p.getEClassifiers().add(class0);

		leftModel = EcoreUtil.copy(p);
		URI leftURI = URI.createURI("file:/leftmodel.ecore");
		ModelUtils.attachResource(leftURI, leftModel);

		expectedModel = EcoreUtil.copy(leftModel);
		URI expectedURI = URI.createURI("file:/expectedmodel.ecore");
		ModelUtils.attachResource(expectedURI, expectedModel);

		EClass class1 = factory.createEClass();
		class1.setName("Class1");
		EClass class2 = factory.createEClass();
		class2.setName("Class2");
		EClass class3 = factory.createEClass();
		class3.setName("Class3");

		p.getEClassifiers().add(class1);
		p.getEClassifiers().add(class2);
		p.getEClassifiers().add(class3);

		EList<EClass> class3SuperTypes = class3.getESuperTypes();
		class3SuperTypes.add(class0);
		class3SuperTypes.add(class1);
		class3SuperTypes.add(class2);

		rightModel = EcoreUtil.copy(p);
		URI rightURI = URI.createURI("file:/rightmodel.ecore");
		ModelUtils.attachResource(rightURI, rightModel);

	}

	public void testLeftToRight() throws Exception {
		if (leftModel != null && rightModel != null)
			doPerformTest(true);
	}

	public void testRightToLeft() throws Exception {
		if (leftModel != null && rightModel != null)
			doPerformTest(false);
	}
}
