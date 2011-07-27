/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine.internal;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Specialization of the heuristic based similarity checker making sure before computing complex metrics that
 * both EClasses are the same.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DistinctEcoreSimilarityChecker extends StatisticBasedSimilarityChecker {
	/**
	 * Create a new checker.
	 * 
	 * @param filter
	 *            a metamodel filter the checker can use to know whether a feature alwaas has the same value
	 *            or not in the models.
	 * @param bridge
	 *            utility class to keep API compatibility.
	 */
	public DistinctEcoreSimilarityChecker(MetamodelFilter filter, GenericMatchEngineToCheckerBridge bridge) {
		super(filter, bridge);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException {
		if (!eClassMatch(obj1.eClass(), obj2.eClass())) {
			return false;
		}
		return super.isSimilar(obj1, obj2);
	}

	/**
	 * This will check whether the two given EClasses are the same. This has been created in order to avoid
	 * EcoreUtil.equals (perfs).
	 * 
	 * @param eClass1
	 *            First of the two EClasses to consider.
	 * @param eClass2
	 *            Second of the two EClasses to consider.
	 * @return <code>true</code> if the two EClasses match, <code>false</code> otherwise.
	 */
	private boolean eClassMatch(EClass eClass1, EClass eClass2) {
		boolean match = false;

		EPackage eClass1Package = eClass1.getEPackage();
		EPackage eClass2Package = eClass2.getEPackage();
		if (eClass1Package == eClass2Package) {
			match = eClass1 == eClass2;
		} else if (eClass1Package.getNsURI() != null
				&& eClass1Package.getNsURI().equals(eClass2Package.getNsURI())) {
			match = eClass1.getClassifierID() == eClass2.getClassifierID();
		} else if (eClass1Package.getNsURI() == null && eClass2Package.getNsURI() == null) {
			match = EcoreUtil.equals(eClass1, eClass2);
		}

		return match;
	}
}
