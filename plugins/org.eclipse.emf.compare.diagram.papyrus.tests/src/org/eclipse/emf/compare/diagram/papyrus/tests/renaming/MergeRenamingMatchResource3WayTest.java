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
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MergeRenamingMatchResources2Ways;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MergeRenamingMatchResources3Ways;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests the {@link MergeRenamingMatchResources2Ways} and
 * {@link MergeRenamingMatchResources3Ways} classes. Can be run in standalone
 * mode, no need to be run as eclipse plug-in test.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MergeRenamingMatchResource3WayTest extends
		AbstractMatchResourceTest {

	@Test
	public void testRenamedRight() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource leftuml = newResource("/prj1/a.uml");
		Resource leftnot = newResource("/prj1/a.notation");
		Resource leftdi = newResource("/prj1/a.di");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(basedi, leftdi, null);
		addMatchResource(basenot, leftnot, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/a.uml",
				"/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/a.di",
				"/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/a.notation", "/prj1/b.notation");
	}

	@Test
	public void testRenamedRightWithAdditionalFiles() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource basesash = newResource("/prj1/a.sash");
		Resource basetxt = newResource("/prj1/a.txt");
		Resource leftuml = newResource("/prj1/a.uml");
		Resource leftnot = newResource("/prj1/a.notation");
		Resource leftdi = newResource("/prj1/a.di");
		Resource leftsash = newResource("/prj1/a.sash");
		Resource lefttxt = newResource("/prj1/a.txt");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");
		Resource rightsash = newResource("/prj1/b.sash");
		Resource righttxt = newResource("/prj1/a.txt");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(basedi, leftdi, null);
		addMatchResource(basenot, leftnot, null);
		addMatchResource(basesash, leftsash, null);
		addMatchResource(basetxt, lefttxt, righttxt);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);
		addMatchResource(null, null, rightsash);

		trt.run();

		assertEquals(5, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/a.uml",
				"/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/a.di",
				"/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/a.notation", "/prj1/b.notation");
		checkComparisonContainsMatchResource("/prj1/a.sash", "/prj1/a.sash",
				"/prj1/b.sash");
		checkComparisonContainsMatchResource("/prj1/a.txt", "/prj1/a.txt",
				"/prj1/a.txt");
	}

	@Test
	public void testRenamedLeft() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource leftuml = newResource("/prj1/b.uml");
		Resource leftnot = newResource("/prj1/b.notation");
		Resource leftdi = newResource("/prj1/b.di");
		Resource rightuml = newResource("/prj1/a.uml");
		Resource rightnot = newResource("/prj1/a.notation");
		Resource rightdi = newResource("/prj1/a.di");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(null, leftdi, null);
		addMatchResource(null, leftnot, null);
		addMatchResource(basedi, null, rightdi);
		addMatchResource(basenot, null, rightnot);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml",
				"/prj1/a.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/b.di",
				"/prj1/a.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/b.notation", "/prj1/a.notation");
	}

	@Test
	public void testRenamedBothSides() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource leftuml = newResource("/prj1/b.uml");
		Resource leftnot = newResource("/prj1/b.notation");
		Resource leftdi = newResource("/prj1/b.di");
		Resource rightuml = newResource("/prj1/c.uml");
		Resource rightnot = newResource("/prj1/c.notation");
		Resource rightdi = newResource("/prj1/c.di");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(basedi, null, null);
		addMatchResource(basenot, null, null);
		addMatchResource(null, leftdi, null);
		addMatchResource(null, leftnot, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml",
				"/prj1/c.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/b.di",
				"/prj1/c.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/b.notation", "/prj1/c.notation");
	}

	@Test
	public void testRenamedBothSidesWithAdditionalFiles() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource basetxt = newResource("/prj1/a.txt");
		Resource leftuml = newResource("/prj1/b.uml");
		Resource leftnot = newResource("/prj1/b.notation");
		Resource leftdi = newResource("/prj1/b.di");
		Resource lefttxt = newResource("/prj1/b.txt");
		Resource rightuml = newResource("/prj1/c.uml");
		Resource rightnot = newResource("/prj1/c.notation");
		Resource rightdi = newResource("/prj1/c.di");
		Resource righttxt = newResource("/prj1/c.txt");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(basedi, null, null);
		addMatchResource(basenot, null, null);
		addMatchResource(basetxt, null, null);
		addMatchResource(null, leftdi, null);
		addMatchResource(null, leftnot, null);
		addMatchResource(null, lefttxt, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);
		addMatchResource(null, null, righttxt);

		trt.run();

		assertEquals(6, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml",
				"/prj1/c.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/b.di",
				"/prj1/c.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/b.notation", "/prj1/c.notation");
		checkComparisonContainsMatchResource("/prj1/a.txt", null, null);
		checkComparisonContainsMatchResource(null, "/prj1/b.txt", null);
		checkComparisonContainsMatchResource(null, null, "/prj1/c.txt");
	}

	@Test
	public void testRenamedLeftDeletedRight() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource leftuml = newResource("/prj1/b.uml");
		Resource leftnot = newResource("/prj1/b.notation");
		Resource leftdi = newResource("/prj1/b.di");

		addMatchResource(baseuml, leftuml, null);
		addMatchResource(basedi, null, null);
		addMatchResource(basenot, null, null);
		addMatchResource(null, leftdi, null);
		addMatchResource(null, leftnot, null);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml", null);
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/b.di", null);
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/b.notation", null);
	}

	@Test
	public void testRenamedRightDeletedLeft() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");

		addMatchResource(baseuml, null, rightuml);
		addMatchResource(basedi, null, null);
		addMatchResource(basenot, null, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", null, "/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", null, "/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation", null,
				"/prj1/b.notation");
	}

	@Test
	public void testRenameWithUmlAndNotationKnown() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource leftuml = newResource("/prj1/a.uml");
		Resource leftnot = newResource("/prj1/a.notation");
		Resource leftdi = newResource("/prj1/a.di");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(basenot, leftnot, rightnot);
		addMatchResource(basedi, leftdi, null);
		addMatchResource(null, null, rightdi);

		trt.run();

		assertEquals(3, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/a.uml",
				"/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/a.di",
				"/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/a.notation", "/prj1/b.notation");
	}

	@Test
	public void testRenameWithUmlAndNotationKnownPlusNoise() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource basesash = newResource("/prj1/a.sash");
		Resource leftuml = newResource("/prj1/a.uml");
		Resource leftnot = newResource("/prj1/a.notation");
		Resource leftdi = newResource("/prj1/a.di");
		Resource leftsash = newResource("/prj1/a.sash");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");
		Resource rightsash = newResource("/prj1/b.sash");
		Resource arightdi = newResource("/prj1/a.di");
		Resource crightdi = newResource("/prj1/c.di");

		addMatchResource(baseuml, leftuml, rightuml);
		addMatchResource(basenot, leftnot, rightnot);
		addMatchResource(basedi, leftdi, null);
		addMatchResource(basesash, leftsash, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightsash);
		addMatchResource(null, null, arightdi);
		addMatchResource(null, null, crightdi);

		trt.run();

		assertEquals(6, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/a.uml",
				"/prj1/b.uml");
		checkComparisonContainsMatchResource("/prj1/a.di", "/prj1/a.di",
				"/prj1/b.di");
		checkComparisonContainsMatchResource("/prj1/a.notation",
				"/prj1/a.notation", "/prj1/b.notation");
		checkComparisonContainsMatchResource("/prj1/a.sash", "/prj1/a.sash",
				"/prj1/b.sash");
		checkComparisonContainsMatchResource(null, null, "/prj1/a.di");
		checkComparisonContainsMatchResource(null, null, "/prj1/c.di");
	}

	@Test
	public void testIncoherentRenameWithApparitionOfRenamedFile() {
		Resource auml = newResource("/prj1/a.uml");
		Resource cnot = newResource("/prj1/c.notation");
		Resource cdi = newResource("/prj1/c.di");
		Resource buml_l = newResource("/prj1/b.uml");
		Resource anot_l = newResource("/prj1/a.notation");
		Resource adi_l = newResource("/prj1/a.di");
		Resource bnot_r = newResource("/prj1/b.notation");
		Resource adi_r = newResource("/prj1/a.di");

		addMatchResource(auml, buml_l, null);
		addMatchResource(cdi, null, null);
		addMatchResource(cnot, null, null);
		addMatchResource(null, adi_l, adi_r);
		addMatchResource(null, anot_l, bnot_r);

		trt.run();

		assertEquals(5, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml", null);
		checkComparisonContainsMatchResource("/prj1/c.di", null, null);
		checkComparisonContainsMatchResource("/prj1/c.notation", null, null);
		checkComparisonContainsMatchResource(null, "/prj1/a.di", "/prj1/a.di");
		checkComparisonContainsMatchResource(null, "/prj1/a.notation",
				"/prj1/b.notation");
	}

	@Test
	public void testIncoherentRenames() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");
		Resource rightdi = newResource("/prj1/b.di");

		// a.uml renamed to b.uml on the left but no matching rename for di and
		// notation
		// And vice-versa on the right
		addMatchResource(baseuml, rightuml, null);
		addMatchResource(basedi, null, null);
		addMatchResource(basenot, null, null);
		addMatchResource(null, null, rightdi);
		addMatchResource(null, null, rightnot);

		trt.run();

		assertEquals(5, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml", null);
		checkComparisonContainsMatchResource("/prj1/a.di", null, null);
		checkComparisonContainsMatchResource("/prj1/a.notation", null, null);
		checkComparisonContainsMatchResource(null, null, "/prj1/b.di");
		checkComparisonContainsMatchResource(null, null, "/prj1/b.notation");
	}

	@Test
	public void testExtremeCaseWithInvalidData() {
		Resource baseuml = newResource("/prj1/a.uml");
		Resource basenot = newResource("/prj1/a.notation");
		Resource basedi = newResource("/prj1/a.di");
		Resource rightuml = newResource("/prj1/b.uml");
		Resource rightnot = newResource("/prj1/b.notation");

		// a.uml renamed to b.uml on the left but no matching rename for di and
		// notation
		// And vice-versa on the right
		addMatchResource(baseuml, rightuml, null);
		addMatchResource(basedi, null, null);
		addMatchResource(basenot, null, null);
		addMatchResource(null, null, rightnot);
		addMatchResource(null, null, null);

		trt.run();

		assertEquals(5, comparison.getMatchedResources().size());
		checkComparisonContainsMatchResource("/prj1/a.uml", "/prj1/b.uml", null);
		checkComparisonContainsMatchResource("/prj1/a.di", null, null);
		checkComparisonContainsMatchResource("/prj1/a.notation", null, null);
		checkComparisonContainsMatchResource(null, null, "/prj1/b.notation");
	}

	@Test(expected = IllegalStateException.class)
	public void testMRMR3WaysInstantiationRejects2WayComparison() {
		comparison.setThreeWay(false);
		new MergeRenamingMatchResources3Ways(comparison, new BasicMonitor());
	}

	@Test(expected = IllegalStateException.class)
	public void testMRMR3WaysRunRejects2WayComparison() {
		// We change the type of comparison after having instantiated the
		// treatment
		comparison.setThreeWay(false);
		trt.run();
	}

	@Test(expected = NullPointerException.class)
	public void testMRMR3WaysRejectsNullComparison() {
		new MergeRenamingMatchResources3Ways(null, new BasicMonitor());
	}

	@Test(expected = NullPointerException.class)
	public void testMRMR3WaysRejectsNullMonitor() {
		new MergeRenamingMatchResources3Ways(comparison, null);
	}

	@Override
	protected void createComparison() {
		super.createComparison();
		comparison.setThreeWay(true);
	}

	@Override
	protected void createMergeRenamingMatchResourceTreatment() {
		trt = new MergeRenamingMatchResources3Ways(comparison,
				new BasicMonitor());
	}
}
