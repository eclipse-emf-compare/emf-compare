/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison;

import java.io.IOException;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fullcomparison.data.distance.DistanceMatchInputData;
import org.eclipse.emf.compare.tests.suite.AllTests;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Before;
import org.junit.Test;

/**
 * A very crude performance test comparing the match per id and match per content time.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class MatchPerformanceComparisonTest {

	DistanceMatchInputData data = new DistanceMatchInputData();

	int nbIterations = 10;

	Resource left;

	Resource right;

	Resource origin;

	@SuppressWarnings("restriction")
	@Before
	public void setUp() throws Exception {
		AllTests.fillEMFRegistries();
		EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", //$NON-NLS-1$
				new org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl());
		left = data.getNominalUMLLeft();
		right = data.getNominalUMLRight();
		origin = data.getNominalUMLOrigin();
	}

	@Test
	public void warmup() throws IOException {
		final IEObjectMatcher contentMatcher = new ProximityEObjectMatcher(EditionDistance.builder().build());
		final IEObjectMatcher matcher = new IdentifierEObjectMatcher(contentMatcher);
		IMatchEngine matchEngine = new DefaultMatchEngine(matcher,
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		matchEngine.match(scope, new BasicMonitor());
	}

	@Test
	public void matchPerIdAlmostIdenticalModels() throws IOException {
		final IEObjectMatcher matcher = new IdentifierEObjectMatcher();
		IMatchEngine matchEngine = new DefaultMatchEngine(matcher,
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		for (int i = 0; i < nbIterations; i++) {
			matchEngine.match(scope, new BasicMonitor());
		}
	}

	@Test
	public void matchPerContentAlmostIdenticalModels() throws IOException {
		final IEObjectMatcher contentMatcher = new ProximityEObjectMatcher(EditionDistance.builder().build());
		IMatchEngine matchEngine = new DefaultMatchEngine(contentMatcher,
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		for (int i = 0; i < nbIterations; i++) {
			matchEngine.match(scope, new BasicMonitor());
		}
	}
}
