/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.framework;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Runs a subclass's fixture assertions independently with proximity and content-hash matching. This lets
 * existing difference, conflict and merge tests check the new matcher against their expected behavior.
 * <p>
 * The default parameters select one matcher for each JUnit execution. Subclasses use {@link #matcher} to
 * construct their comparison and choose the identifier policy appropriate to the scenario. Their assertions
 * define the expected result; this base class does not compare the two matchers' outputs.
 * </p>
 * <p>
 * JUnit creates separate test instances. Subclasses must load mutable fixtures for each instance or parameter
 * row so a merge in one execution cannot affect another. Subclasses with additional parameters must include
 * both matcher choices in their own parameter list.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@RunWith(Parameterized.class)
public abstract class AbstractMatcherTest extends EMFCompareTestBase {
	/** Matcher selected explicitly by the JUnit constructor parameter. */
	protected final MatchingAlgorithm matcher;

	/**
	 * Selects the matcher for this test instance.
	 *
	 * @param matcher
	 *            the matcher whose result the test asserts
	 */
	protected AbstractMatcherTest(MatchingAlgorithm matcher) {
		this.matcher = matcher;
	}

	/**
	 * Lists the two independent test executions.
	 *
	 * @return the matcher parameters
	 */
	@Parameters(name = "{0}")
	public static Collection<Object[]> matcherParameters() {
		return Arrays.asList(new Object[] {MatchingAlgorithm.PROXIMITY }, new Object[] {MatchingAlgorithm.CONTENT_HASH });
	}
}
