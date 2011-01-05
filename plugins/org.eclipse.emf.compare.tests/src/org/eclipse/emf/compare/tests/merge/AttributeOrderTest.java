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

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class AttributeOrderTest extends MergeTestBase {

	@Override
	public void setUp() throws Exception {

		super.setUp();

		// We create a dynamic instance with attribute "att1" whose type is
		// EString and multiplicity = -1. By default it is unique, I think
		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage p = factory.createEPackage();
		p.setName("TestPackage1");

		EClass class0 = factory.createEClass();
		class0.setName("Class0");
		p.getEClassifiers().add(class0);

		EAttribute att = factory.createEAttribute();
		att.setName("att1");
		att.setEType(EcorePackage.eINSTANCE.getEString());
		att.setLowerBound(0);
		att.setUpperBound(-1);
		class0.getEStructuralFeatures().add(att);

		EObject instance = p.getEFactoryInstance().create(class0);
		List<String> attVal = (List<String>)instance.eGet(att);
		attVal.add("item1");
		attVal.add("item2");
		attVal.add("item3");

		leftModel = EcoreUtil.copy(instance);
		URI leftURI = URI.createURI("leftmodel.xmi");
		ModelUtils.attachResource(leftURI, leftModel);

		expectedModel = EcoreUtil.copy(leftModel);
		URI expectedURI = URI.createURI("expectedmodel.xmi");
		ModelUtils.attachResource(expectedURI, expectedModel);

		attVal.remove("item2");
		rightModel = EcoreUtil.copy(instance);
		URI rightURI = URI.createURI("rightmodel.xmi");
		ModelUtils.attachResource(rightURI, rightModel);
	}

}
