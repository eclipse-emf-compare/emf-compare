/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.tests;

import static org.eclipse.emf.compare.epatch.applier.ApplyStrategy.LEFT_TO_RIGHT;
import static org.eclipse.emf.compare.epatch.applier.ApplyStrategy.RIGHT_TO_LEFT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.applier.CopyingEpatchApplier;
import org.eclipse.emf.compare.epatch.applier.EpatchMapping.EpatchMappingEntry;
import org.eclipse.emf.compare.epatch.recorder.EpatchRecorder;
import org.eclipse.emf.compare.epatch.tests.testdata.Change;
import org.eclipse.emf.compare.epatch.tests.util.EmfAssert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(Parameterized.class)
public class ApplierTest {

	@Parameters
	public static ArrayList<Object[]> getData() {
		return Change.Util.getAllChanges();
	}

	public ApplierTest(Change ch) {
		change = ch;
	}

	public Change change;

	@Test
	public void testApplierRightToLeft() throws Exception {
		ResourceSet rs1 = change.getResourceSet();

		EpatchRecorder rec = new EpatchRecorder(rs1, change.toString());
		change.apply(rs1);
		Epatch patch = rec.endRecording();
		CopyingEpatchApplier app = new CopyingEpatchApplier(RIGHT_TO_LEFT, patch, rs1);
		app.apply();
		String name = rs1.getResources().get(0).getURI().trimFileExtension().lastSegment();
		EmfAssert.assertResourcesEqual(change.getResourceSet(), app.getOutputResourceSet(), name);
	}

	@Test
	public void testApplierLeftToRight() throws Exception {
		ResourceSet rs1 = change.getResourceSet();
		ResourceSet rs2 = change.getResourceSet();

		EpatchRecorder rec = new EpatchRecorder(rs1, change.toString());
		change.apply(rs1);
		Epatch patch = rec.endRecording();

		CopyingEpatchApplier app = new CopyingEpatchApplier(LEFT_TO_RIGHT, patch, rs2);
		app.apply();

		Resource r1 = rs1.getResources().get(0);
		Resource r2 = app.getMap().getDstResources().get(patch.getResources().get(0));
		r2.setURI(r1.getURI());

		EmfAssert.assertEObjectsEqual(r1.getContents().get(0), r2.getContents().get(0));
	}

	private Epatch getPatch() {
		ResourceSet rs = change.getResourceSet();
		EpatchRecorder rec = new EpatchRecorder(rs, change.toString());
		change.apply(rs);
		return rec.endRecording();
	}

	private CopyingEpatchApplier getAppliedPatch() {
		ResourceSet rs2 = change.getResourceSet();
		Epatch patch = getPatch();

		CopyingEpatchApplier app = new CopyingEpatchApplier(LEFT_TO_RIGHT, patch, rs2);
		app.apply();
		return app;
	}

	@Test
	public void testAllObjectsAreCopied() throws Exception {
		CopyingEpatchApplier app = getAppliedPatch();
		Resource r2 = app.getMap().getSrcResources().get(app.getEpatch().getResources().get(0));
		Resource r3 = app.getMap().getDstResources().get(app.getEpatch().getResources().get(0));
		Set<EObject> orig = new HashSet<EObject>();
		System.out.println("src");
		for (TreeIterator<EObject> i = r2.getAllContents(); i.hasNext();) {
			EObject o = i.next();
			System.out.println(Integer.toHexString(o.hashCode()) + ": " + o);
			orig.add(o);
		}

		System.out.println("dst");
		for (TreeIterator<EObject> i = r3.getAllContents(); i.hasNext();) {
			EObject o = i.next();
			System.out.println(o);
			assertFalse(orig.contains(o));
		}
	}

	@Test
	public void testAllSrcObjectsAreInSrc() throws Exception {
		CopyingEpatchApplier app = getAppliedPatch();
		Resource r2 = app.getMap().getSrcResources().get(app.getEpatch().getResources().get(0));
		Set<EObject> orig = new HashSet<EObject>();
		System.out.println("src");
		for (TreeIterator<EObject> i = r2.getAllContents(); i.hasNext();) {
			EObject o = i.next();
			System.out.println(Integer.toHexString(o.hashCode()) + ": " + o);
			orig.add(o);
		}

		System.out.println("triMap");
		for (EpatchMappingEntry o : app.getMap().getAllEntries()) {
			System.out.println(o);
			if (o.getSrc() != null)
				assertTrue(orig.contains(o.getSrc()));
		}
	}

	@Test
	public void testAllDstObjectsAreInDst() throws Exception {
		CopyingEpatchApplier app = getAppliedPatch();
		Resource r3 = app.getMap().getDstResources().get(app.getEpatch().getResources().get(0));
		Set<EObject> orig = new HashSet<EObject>();
		System.out.println("dst");
		for (TreeIterator<EObject> i = r3.getAllContents(); i.hasNext();) {
			EObject o = i.next();
			System.out.println(Integer.toHexString(o.hashCode()) + ": " + o);
			orig.add(o);
		}

		System.out.println("triMap");
		for (EpatchMappingEntry o : app.getMap().getAllEntries()) {
			if (o.getDst() != null)
				assertTrue(orig.contains(o.getDst()));
		}
	}
}
