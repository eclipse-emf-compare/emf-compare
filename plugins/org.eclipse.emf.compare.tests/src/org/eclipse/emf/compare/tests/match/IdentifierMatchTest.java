/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.match;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.EMFCompareTestRunner;
import org.eclipse.emf.compare.tests.framework.IdentifierMatchValidator;
import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.annotation.BeforeMatch;
import org.eclipse.emf.compare.tests.framework.annotation.MatchTest;
import org.eclipse.emf.compare.tests.framework.annotation.UseCase;
import org.eclipse.emf.compare.tests.match.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.runner.RunWith;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@RunWith(EMFCompareTestRunner.class)
public class IdentifierMatchTest {
	private IdentifierMatchInputData inputData = new IdentifierMatchInputData();

	@UseCase("Extended library three-way")
	public NotifierTuple extlibraryTuple() throws IOException {
		final Resource left = inputData.getExtlibraryLeft();
		final Resource right = inputData.getExtlibraryRight();
		final Resource origin = inputData.getExtlibraryOrigin();

		return new NotifierTuple(left, right, origin);
	}

	@BeforeMatch
	public void beforeMatch(NotifierTuple tuple) {
		assertNotNull(tuple);
		assertTrue(tuple.getLeft() instanceof Resource);
		assertTrue(tuple.getRight() instanceof Resource);
		// We have both two-way and three-way use cases here
	}

	@MatchTest
	public void testIdentifierMatch(IComparisonScope scope,
			Comparison comparison) {
		final Resource left = (Resource) scope.getLeft();
		final Resource right = (Resource) scope.getRight();
		final Resource origin = (Resource) scope.getOrigin();
		assertSame(origin != null, comparison.isThreeWay());

		assertSame(1, comparison.getMatchedResources().size());
		final MatchResource matchedResource = comparison.getMatchedResources()
				.get(0);
		assertEquals(left.getURI().toString(), matchedResource.getLeftURI());
		assertEquals(right.getURI().toString(), matchedResource.getRightURI());
		if (origin != null) {
			assertEquals(origin.getURI().toString(),
					matchedResource.getOriginURI());
		}

		final IdentifierMatchValidator validator = new IdentifierMatchValidator();
		validator.validate(comparison);

		// Make sure that we have a Match for all EObjects
		final List<EObject> leftChildren = getAllProperContent(left);
		final List<EObject> rightChildren = getAllProperContent(right);
		final List<EObject> originChildren = getAllProperContent(origin);

		assertAllMatched(leftChildren, comparison, scope);
		assertAllMatched(rightChildren, comparison, scope);
		assertAllMatched(originChildren, comparison, scope);
	}

	/**
	 * This can be used to check whether all objects of the given list have a
	 * corresponding {@link Match} in the given {@link Comparison}. If one of
	 * said EObjects is not matched, we will check whether it is included in the
	 * given <code>scope</code> if it is a {@link FilterComparisonScope}.
	 * 
	 * @param eObjects
	 *            The list of EObjects for which we need corresponding
	 *            {@link Match}es.
	 * @param comparison
	 *            The {@link Comparison} in which we are to check for Matches.
	 * @param scope
	 *            The scope that has been used to create the given
	 *            <code>comparison</code>.
	 */
	protected static void assertAllMatched(List<EObject> eObjects,
			Comparison comparison, IComparisonScope scope) {
		final Predicate<? super EObject> scopeFilter;
		if (scope instanceof FilterComparisonScope) {
			scopeFilter = getResourceChildrenFilteringPredicate((FilterComparisonScope) scope);
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
		final Iterator<Object> properContent = EcoreUtil.getAllProperContents(
				res, false);
		final Iterator<EObject> filter = Iterators.filter(properContent,
				EObject.class);
		return Lists.newArrayList(filter);
	}

	/**
	 * Retrieves the Predicate that is used by the given scope in order to
	 * filter out Resource children.
	 * <p>
	 * This uses reflection to access and call a protected method, and is only
	 * meant for testing purposes.
	 * </p>
	 * 
	 * @param scope
	 *            The scope which predicate we need to retrieve.
	 * @return The predicate that was used by the given scope to filter out
	 *         Resource children.
	 */
	@SuppressWarnings("unchecked")
	private static Predicate<? super EObject> getResourceChildrenFilteringPredicate(
			FilterComparisonScope scope) {
		final String methodName = "getResourceChildrenFilter";
		try {
			final Method method = scope.getClass()
					.getDeclaredMethod(methodName);
			method.setAccessible(true);
			return (Predicate<? super EObject>) method.invoke(scope);
		} catch (Exception e) {
			fail("Could not retrieve the filtering predicate of "
					+ scope.getClass().getName());
		}
		// Unreachable code
		return null;
	}
}
