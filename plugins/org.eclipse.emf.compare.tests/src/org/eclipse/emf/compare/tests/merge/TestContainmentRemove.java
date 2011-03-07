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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class TestContainmentRemove extends MergeTestBase {

	@Override
	public void setUp() throws Exception {

		super.setUp();

		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage p = factory.createEPackage();
		p.setName("TestPackage1");
		EClass class0 = factory.createEClass();
		class0.setName("Class0");

		leftModel = EcoreUtil.copy(p);
		URI leftURI = URI.createURI("leftmodel.ecore");
		ModelUtils.attachResource(leftURI, leftModel);

		expectedModel = EcoreUtil.copy(leftModel);
		URI expectedURI = URI.createURI("expectedmodel.ecore");
		ModelUtils.attachResource(expectedURI, expectedModel);

		p.getEClassifiers().add(class0);
		rightModel = EcoreUtil.copy(p);
		URI rightURI = URI.createURI("rightmodel.ecore");
		ModelUtils.attachResource(rightURI, rightModel);

	}

	@Override
	protected void preMergeHook(boolean isLeftToRight) {
		super.preMergeHook(isLeftToRight);
		doCheckFactory();
	}

	@Override
	protected void postMergeHook(boolean isLeftToRight) {
		super.postMergeHook(isLeftToRight);
		doCheckFactory();
	}

	private void doCheckFactory() {

		EPackage leftPackage = (EPackage)leftModel;
		EPackage rightPackage = (EPackage)rightModel;

		assertNotNull("Left eFactoryInstance is null ", leftPackage.getEFactoryInstance());
		assertNotNull("Right eFactoryInstance is null ", rightPackage.getEFactoryInstance());

	}
}
