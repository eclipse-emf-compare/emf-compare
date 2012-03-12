/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.tests;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.diff.DiffEpatchService;
import org.eclipse.emf.compare.epatch.recorder.EpatchRecorder;
import org.eclipse.emf.compare.epatch.tests.testdata.Change;
import org.eclipse.emf.compare.epatch.tests.testdata.ListChanges;
import org.eclipse.emf.compare.epatch.tests.testdata.ObjectChanges;
import org.eclipse.emf.compare.epatch.tests.util.EmfAssert;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(Parameterized.class)
public class DiffTest {

	@Parameters
	public static ArrayList<Object[]> getData() {
		return Change.Util.getAllChanges();
	}

	private Change change;

	private DiffModel diff;

	private Epatch expected;

	private MatchModel match;

	private String name;

	public DiffTest(Change ch) throws InterruptedException {
		change = ch;
		name = ch.toString();
		ResourceSet rs = change.getResourceSet();
		Resource res = rs.getResources().get(0);

		// backup left model
		EObject left = EcoreUtil.copy(res.getContents().get(0));

		// modify model and record changes
		EpatchRecorder rec = new EpatchRecorder(rs, change.toString());
		change.apply(rs);
		expected = rec.endRecording();

		// rename/create resources
		URI u1 = res.getURI();
		rs.createResource(u1).getContents().add(left);
		res.setURI(u1.trimSegments(1).appendSegment(newURI(u1.lastSegment())));
		EObject right = res.getContents().get(0);

		// do matching + diffing
		match = MatchService.doContentMatch(left, right, null);
		diff = DiffService.doDiff(match);
	}

	private String newURI(String uri) {
		int p = uri.lastIndexOf('.');
		if (p < 0)
			return uri + "1";
		else
			return uri.substring(0, p) + "1" + uri.substring(p);
	}

	@Ignore
	@Test
	public void testCreateDiff() {

		// EMF Compare does not support detecting moving attribute values
		if (change == ListChanges.MOVE_INT)
			return;

		// for these tests, EMF compares matching algorithm makes problems
		if (change == ObjectChanges.MOVE_OBJECT_FROM_SINGLE_TO_LIST)
			return;

		Epatch epatch = DiffEpatchService.createEpatch(match, diff, name);
		EmfAssert.assertEObjectsEqual(expected, epatch);
	}

	@Ignore
	@Test
	public void testForOrphanedObjects() throws InterruptedException {
		Epatch epatch = DiffEpatchService.createEpatch(match, diff, name);
		org.junit.Assert.assertNotNull(epatch.eResource());
		EmfAssert.assertNoCrossRefsLeaveReources(epatch, epatch.eResource());
	}
}
