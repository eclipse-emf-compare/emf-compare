/*******************************************************************************
 * Copyright (c) 2010 Gerhardt Informatics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerhardt Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class TestContainmentOrderAddMany extends MergeTestBase {

	@Override
	public void setUp() throws Exception {

		super.setUp();

		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage p = factory.createEPackage();
		p.setName("TestPackage1");
		EClass class0 = factory.createEClass();
		class0.setName("Class0");
		EClass class1 = factory.createEClass();
		class1.setName("Class1");
		EClass class2 = factory.createEClass();
		class2.setName("Class2");

		p.getEClassifiers().add(class0);
		p.getEClassifiers().add(class1);
		p.getEClassifiers().add(class2);

		int extraClassNum = 10;
		List<EClass> extraClasses = new ArrayList<EClass>();
		for (int i = 0; i < extraClassNum; i++) {
			EClass ec = factory.createEClass();
			ec.setName("ExtraClass_" + i);
			extraClasses.add(ec);
		}

		p.getEClassifiers().addAll(1, extraClasses);

		leftModel = EcoreUtil.copy(p);
		URI leftURI = URI.createURI("leftmodel.ecore");
		ModelUtils.attachResource(leftURI, leftModel);

		expectedModel = EcoreUtil.copy(leftModel);
		URI expectedURI = URI.createURI("expectedmodel.ecore");
		ModelUtils.attachResource(expectedURI, expectedModel);

		p.getEClassifiers().remove(class1);
		p.getEClassifiers().removeAll(extraClasses);
		rightModel = EcoreUtil.copy(p);
		URI rightURI = URI.createURI("rightmodel.ecore");
		ModelUtils.attachResource(rightURI, rightModel);

	}

}
