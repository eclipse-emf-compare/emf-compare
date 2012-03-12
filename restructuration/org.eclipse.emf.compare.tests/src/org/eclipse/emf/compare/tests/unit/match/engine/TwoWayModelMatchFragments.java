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
package org.eclipse.emf.compare.tests.unit.match.engine;

import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class TwoWayModelMatchFragments extends TestCase {
	/** Current VM's encoding name. */
	private static final String ENCODING_STRING = System.getProperty("file.encoding");

	/** Current machine's line separator. */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	String expected = "<?xml version=\"1.0\" encoding=\""
			+ ENCODING_STRING
			+ "\"?>"
			+ LINE_SEPARATOR
			+ "<diff:DiffModel xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:diff=\"http://www.eclipse.org/emf/compare/diff/1.1\">"
			+ LINE_SEPARATOR
			+ "  <ownedElements xsi:type=\"diff:DiffGroup\">"
			+ LINE_SEPARATOR
			+ "    <subDiffElements xsi:type=\"diff:DiffGroup\">"
			+ LINE_SEPARATOR
			+ "      <subDiffElements xsi:type=\"diff:DiffGroup\">"
			+ LINE_SEPARATOR
			+ "        <subDiffElements xsi:type=\"diff:UpdateAttribute\">"
			+ LINE_SEPARATOR
			+ "          <attribute href=\"http://www.eclipse.org/emf/2002/Ecore#//ENamedElement/name\"/>"
			+ LINE_SEPARATOR
			+ "          <leftElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore#//ClassInParent\"/>"
			+ LINE_SEPARATOR
			+ "          <rightElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#//ClassInParentRenamed\"/>"
			+ LINE_SEPARATOR
			+ "        </subDiffElements>"
			+ LINE_SEPARATOR
			+ "        <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#//ClassInParentRenamed\"/>"
			+ LINE_SEPARATOR
			+ "      </subDiffElements>"
			+ LINE_SEPARATOR
			+ "      <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#/\"/>"
			+ LINE_SEPARATOR
			+ "    </subDiffElements>"
			+ LINE_SEPARATOR
			+ "    <subDiffElements xsi:type=\"diff:DiffGroup\">"
			+ LINE_SEPARATOR
			+ "      <subDiffElements xsi:type=\"diff:DiffGroup\">"
			+ LINE_SEPARATOR
			+ "        <subDiffElements xsi:type=\"diff:ReferenceChangeRightTarget\">"
			+ LINE_SEPARATOR
			+ "          <reference href=\"http://www.eclipse.org/emf/2002/Ecore#//EClass/eSuperTypes\"/>"
			+ LINE_SEPARATOR
			+ "          <rightElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/child.ecore#//ClassInChild\"/>"
			+ LINE_SEPARATOR
			+ "          <leftElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/child.ecore#//ClassInChild\"/>"
			+ LINE_SEPARATOR
			+ "          <rightTarget href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#//ClassInParentRenamed\"/>"
			+ LINE_SEPARATOR
			+ "          <leftTarget href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore#//ClassInParent\"/>"
			+ LINE_SEPARATOR
			+ "        </subDiffElements>"
			+ LINE_SEPARATOR
			+ "        <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/child.ecore#//ClassInChild\"/>"
			+ LINE_SEPARATOR
			+ "      </subDiffElements>"
			+ LINE_SEPARATOR
			+ "      <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/child.ecore#/\"/>"
			+ LINE_SEPARATOR
			+ "    </subDiffElements>"
			+ LINE_SEPARATOR
			+ "  </ownedElements>"
			+ LINE_SEPARATOR
			+ "  <leftRoots href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore#/\"/>"
			+ LINE_SEPARATOR
			+ "  <rightRoots href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#/\"/>"
			+ LINE_SEPARATOR + "</diff:DiffModel>" + LINE_SEPARATOR;

	public void testFragmentedEcoresResourceSetMatch() throws Exception {
		ResourceSet leftSet = new ResourceSetImpl();
		ResourceSet rightSet = new ResourceSetImpl();
		EObject left = ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore", true), leftSet);
		EObject right = ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore", true), rightSet);
		//
		MatchResourceSet match = MatchService.doResourceSetMatch(leftSet, rightSet, Collections.EMPTY_MAP);
		//
		DiffResourceSet diff = DiffService.doDiff(match);
		//
		String resultResourceSetMatch = ModelUtils.serialize(diff.getDiffModels().get(0));

		assertEquals(expected, resultResourceSetMatch);
	}

	public void testFragmentedEcoresContentMatch() throws Exception {
		ResourceSet leftSet = new ResourceSetImpl();
		ResourceSet rightSet = new ResourceSetImpl();
		EObject left = ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore", true), leftSet);
		EObject right = ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore", true), rightSet);
		//

		MatchModel modelMatch = MatchService.doContentMatch(left, right, Collections.EMPTY_MAP);
		DiffModel modelDiff = DiffService.doDiff(modelMatch);
		String resultContentMatch = ModelUtils.serialize(modelDiff);
		assertEquals(expected, resultContentMatch);
	}

	public void testFragmentedEcoresResourceMatch() throws Exception {
		ResourceSet leftSet = new ResourceSetImpl();
		ResourceSet rightSet = new ResourceSetImpl();
		EObject left = ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore", true), leftSet);
		EObject right = ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore", true), rightSet);
		//

		MatchModel resourceMatch = MatchService.doResourceMatch(left.eResource(), right.eResource(),
				Collections.EMPTY_MAP);
		DiffModel resourceDiff = DiffService.doDiff(resourceMatch);
		String resultResourceMatch = ModelUtils.serialize(resourceDiff);

		assertEquals(expected, resultResourceMatch);
	}

}
