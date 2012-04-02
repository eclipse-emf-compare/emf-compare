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
package org.eclipse.emf.compare.tests.framework;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This provides a number of utility methods for EMF Compare tests.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareTestBase {
	/**
	 * This can be used to check whether all objects of the given list have a corresponding {@link Match} in
	 * the given {@link Comparison}. If one of said EObjects is not matched, we will check whether it is
	 * included in the given <code>scope</code> if it is a {@link FilterComparisonScope}.
	 * 
	 * @param eObjects
	 *            The list of EObjects for which we need corresponding {@link Match}es.
	 * @param comparison
	 *            The {@link Comparison} in which we are to check for Matches.
	 * @param scope
	 *            The scope that has been used to create the given <code>comparison</code>.
	 */
	protected void assertAllMatched(List<EObject> eObjects, Comparison comparison, IComparisonScope scope) {
		final Predicate<? super EObject> scopeFilter;
		if (scope instanceof FilterComparisonScope) {
			scopeFilter = getResourceChildrenFilteringPredicate((FilterComparisonScope)scope);
		} else {
			scopeFilter = Predicates.alwaysTrue();
		}

		final Iterator<EObject> eObjectIterator = eObjects.iterator();
		while (eObjectIterator.hasNext()) {
			final EObject eObject = eObjectIterator.next();
			final Match match = comparison.getMatch(eObject);
			assertTrue(match != null || !scopeFilter.apply(eObject));
		}
	}

	/**
	 * Returns all proper content of the given resource in the form of a list.
	 * 
	 * @param res
	 *            The resource which content we need.
	 * @return The list of all of the given resource's contained EObjects.
	 */
	protected static List<EObject> getAllProperContent(Resource res) {
		if (res == null) {
			return Lists.newArrayList();
		}

		final Iterator<Object> properContent = EcoreUtil.getAllProperContents(res, false);
		final Iterator<EObject> filter = Iterators.filter(properContent, EObject.class);
		return Lists.newArrayList(filter);
	}

	/**
	 * Retrieves the Predicate that is used by the given scope in order to filter out Resource children.
	 * <p>
	 * This uses reflection to access a protected field, and is only meant for testing purposes.
	 * </p>
	 * 
	 * @param scope
	 *            The scope which predicate we need to retrieve.
	 * @return The predicate that was used by the given scope to filter out Resource children.
	 */
	@SuppressWarnings("unchecked")
	private static Predicate<? super EObject> getResourceChildrenFilteringPredicate(
			FilterComparisonScope scope) {
		final String fieldName = "resourceContentFilter"; //$NON-NLS-1$
		try {
			final Field field = FilterComparisonScope.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (Predicate<? super EObject>)field.get(scope);
		} catch (Exception e) {
			fail("Could not retrieve the filtering predicate of " + scope.getClass().getName());
		}
		// Unreachable code
		return null;
	}
}
