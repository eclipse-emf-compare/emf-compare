/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.renaming;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MergeRenamingMatchResources2Ways;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MergeRenamingMatchResources3Ways;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.junit.Test;

/**
 * Tests the {@link MergeRenamingMatchResources2Ways} and
 * {@link MergeRenamingMatchResources3Ways} classes. Can be run in standalone
 * mode, no need to be run as eclipse plug-in test.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MergeRenamingMatchResource2WayTest extends
		AbstractMatchResourceTest {

	@Test
	public void testSimpleCase2Ways() {
		Resource leftuml = newResource("/prj1/a.uml");
		Resource leftnot = newResource("/prj1/a.notation");
		Resource leftdi = newResource("/prj1/a.di");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");

		addMatchResource(null, leftuml, rightuml);
		addMatchResource(null, leftdi, null);
		addMatchResource(null, leftnot, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/b.notation");

	}

	@Test
	public void testSimpleCase2WaysWithSashAndNonPapyrus() {
		Resource leftuml = newResource("/prj1/a.uml");
		Resource leftnot = newResource("/prj1/a.notation");
		Resource leftdi = newResource("/prj1/a.di");
		Resource leftsash = newResource("/prj1/a.sash");
		Resource lefttxt = newResource("/prj1/a.txt");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");
		Resource rightsash = newResource("/prj1/b.sash");
		Resource righttxt = newResource("/prj1/b.txt");

		addMatchResource(null, leftuml, rightuml);
		addMatchResource(null, leftdi, null);
		addMatchResource(null, leftnot, null);
		addMatchResource(null, leftsash, null);
		addMatchResource(null, lefttxt, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);
		addMatchResource(null, null, rightsash);
		addMatchResource(null, null, righttxt);

		trt.run();

		assertEquals(6, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/b.notation");
		checkComparisonContainsMatchResource("/prj1/a.sash", "/prj1/b.sash");
		checkComparisonContainsMatchResource("/prj1/a.txt", null);
		checkComparisonContainsMatchResource(null, "/prj1/b.txt");

	}

	@Test(expected = IllegalStateException.class)
	public void testMRMR2WaysInstantiationRejects3WayComparison() {
		comparison.setThreeWay(true);
		new MergeRenamingMatchResources2Ways(comparison, new BasicMonitor());
	}

	@Test(expected = IllegalStateException.class)
	public void testMRMR2WaysRunRejects3WayComparison() {
		// We change the type of comparison after having instantiated the
		// treatment
		comparison.setThreeWay(true);
		trt.run();
	}

	@Test(expected = NullPointerException.class)
	public void testMRMR2WaysRejectsNullComparison() {
		new MergeRenamingMatchResources2Ways(null, new BasicMonitor());
	}

	@Test(expected = NullPointerException.class)
	public void testMRMR2WaysRejectsNullMonitor() {
		new MergeRenamingMatchResources2Ways(comparison, null);
	}

	@Override
	protected Resource newResource(String uri) {
		return new ResourceImpl(URI.createPlatformResourceURI(uri, false));
	}

	@Override
	protected MatchResource addMatchResource(Resource origin, Resource left,
			Resource right) {
		MatchResource result = CompareFactory.eINSTANCE.createMatchResource();
		if (left != null) {
			result.setLeft(left);
			result.setLeftURI(left.getURI().toString());
		}
		if (origin != null) {
			result.setOrigin(origin);
			result.setOriginURI(origin.getURI().toString());
		}
		if (right != null) {
			result.setRight(right);
			result.setRightURI(right.getURI().toString());
		}
		result.setComparison(comparison);
		return result;
	}

	@Override
	protected void createMergeRenamingMatchResourceTreatment() {
		trt = new MergeRenamingMatchResources2Ways(comparison,
				new BasicMonitor());
	}
}
