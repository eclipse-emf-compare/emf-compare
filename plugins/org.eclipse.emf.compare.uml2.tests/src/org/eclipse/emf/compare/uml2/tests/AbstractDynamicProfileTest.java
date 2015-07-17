/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.uml2.uml.UMLPlugin;

public abstract class AbstractDynamicProfileTest extends AbstractUMLProfileTest {

	static URI registeredURI;

	static Object registeredPackage;

	/**
	 * Each sublass of AbstractUMLTest have to call this method in a @BeforeClass annotated method. This allow
	 * each test to customize its context.
	 */
	public static void initEPackageNsURIToProfileLocationMap() {
		addProfilePathmap();
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			// It is required to link the EPackage to the UML package of the UML Profile
			UMLPlugin
					.getEPackageNsURIToProfileLocationMap()
					.put("http://www.eclipse.org/emf/compare/uml2/1.0.0/testprofile",
							URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw")); //$NON-NLS-1$
		} else {
			registeredURI = UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(
					UML2CompareTestProfilePackage.eNS_URI);
			registeredPackage = EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
		}
	}

	/**
	 * Each sublass of AbstractUMLTest have to call this method in a @BeforeClass annotated method. This allow
	 * each test to safely delete its context.
	 */
	public static void resetEPackageNsURIToProfileLocationMap() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(
					"http://www.eclipse.org/emf/compare/uml2/1.0.0/testprofile");
		} else {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().put(UML2CompareTestProfilePackage.eNS_URI,
					registeredURI);
			EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI, registeredPackage);
		}
		resetProfilePathmap();
	}
}
