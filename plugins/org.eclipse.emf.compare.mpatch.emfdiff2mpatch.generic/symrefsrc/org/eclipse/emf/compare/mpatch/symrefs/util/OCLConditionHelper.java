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
package org.eclipse.emf.compare.mpatch.symrefs.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.Activator;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.handlers.PruneHandler;
import org.eclipse.emf.query.ocl.conditions.BooleanOCLCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.Query;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;

public class OCLConditionHelper {

	/** OCL instance with regex support for string primitives. */
	protected final static OCL ocl = org.eclipse.ocl.ecore.OCL.newInstance(new RegexEnvironmentFactory());

	/**
	 * A cache for OCL String conditions converted to EMF Query conditions. This increases the performance when the map
	 * is filled.
	 * 
	 * TODO: Maybe we could think of an alternative improvement, e.g. building the query / condition object ourselves.
	 */
	private static final Map<String, EObjectCondition> cachedConditions = new HashMap<String, EObjectCondition>();

	/**
	 * Use EMF Query and OCL to find all emf model elements in the given resource which match the given ocl condition.
	 * 
	 * @param condition
	 * @param resource
	 * @return
	 */
	public static Collection<EObject> collectValidElements(OclCondition condition, EObject model) {

		// convert the condition and quit if that was not successful
		EObjectCondition whereCondition = getWhereCondition(condition);
		if (whereCondition == null)
			return Collections.emptyList();

		// perform the query
		final IQueryResult result;
		try {
			result = new SELECT(new FROM(model), new WHERE(whereCondition)).execute();
		} catch (Exception e) {
			Activator.getDefault().logError("Error while executing OCL statement with condition: " + whereCondition, e);
			return Collections.emptyList();
		}

		/*
		 * some debugging output
		 */
		// msg("=========> " + condition.getExpression());
		// msg("applied to: " + model);
		// for (final Object next : result) {
		// msg("-> " + next);
		// }

		return result;
	}

	// /**
	// * For debugging.
	// */
	// private static void msg(String s) {
	// // Activator.getDefault().logInfo(s);
	// System.out.println("OCLConditionHelper: " + s);
	// }

	/**
	 * Create a condition for emf query out of an ocl condition having an expression and optionally checking also the
	 * type.
	 * 
	 * @param condition
	 *            An OCL condition containing an expression and information whether to also check the type.
	 * @return A condition for EMF query or <code>null</code>, if the condition could not be created e.g. because the
	 *         expression was not parseable.
	 */
	public static EObjectCondition getWhereCondition(OclCondition condition) {

		// build the key for this condition, depending on the expression and the type
		String key = condition.getElementReference().getType().getInstanceClassName()
				+ (condition.isCheckType() ? "?" : "!") + condition.getExpression();

		// try to get an already cached ocl condition
		EObjectCondition whereCondition = cachedConditions.get(key);

		// if condition is not yet cached, lets parse it from the string expression and create a query
		if (whereCondition == null) {

			// ocl's little helper
			OCLHelper<EClassifier, ?, ?, ?> helper = ocl.createOCLHelper();
			helper.setContext(condition.getElementReference().getType());
			Query<EClassifier, EClass, EObject> query;
			try {

				// create the query (costly operation)
				final OCLExpression<EClassifier> oclExpression = helper.createQuery(condition.getExpression());
				query = ocl.createQuery(oclExpression);
			} catch (final ParserException e) {
				Activator.getDefault().logError("Error while parsing ocl condition: " + condition.getExpression(), e);
				return null;
			}

			// build a condition from the query (somehow costly operation)
			whereCondition = new BooleanOCLCondition<EClassifier, EClass, EObject>(ocl.getEnvironment(), query,
					condition.isCheckType() ? condition.getElementReference().getType() : null, PruneHandler.NEVER);

			// cache that condition!
			cachedConditions.put(key, whereCondition);
		}
		return whereCondition;
	}

}
