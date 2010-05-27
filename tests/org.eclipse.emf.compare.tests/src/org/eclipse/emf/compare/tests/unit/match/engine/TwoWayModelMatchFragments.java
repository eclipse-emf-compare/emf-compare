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

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<diff:DiffModel xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:diff=\"http://www.eclipse.org/emf/compare/diff/1.1\">\n"
			+ "  <ownedElements xsi:type=\"diff:DiffGroup\">\n"
			+ "    <subDiffElements xsi:type=\"diff:DiffGroup\">\n"
			+ "      <subDiffElements xsi:type=\"diff:DiffGroup\">\n"
			+ "        <subDiffElements xsi:type=\"diff:UpdateAttribute\">\n"
			+ "          <attribute href=\"http://www.eclipse.org/emf/2002/Ecore#//ENamedElement/name\"/>\n"
			+ "          <leftElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore#//ClassInParent\"/>\n"
			+ "          <rightElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#//ClassInParentRenamed\"/>\n"
			+ "        </subDiffElements>\n"
			+ "        <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#//ClassInParentRenamed\"/>\n"
			+ "      </subDiffElements>\n"
			+ "      <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#/\"/>\n"
			+ "    </subDiffElements>\n"
			+ "    <subDiffElements xsi:type=\"diff:DiffGroup\">\n"
			+ "      <subDiffElements xsi:type=\"diff:DiffGroup\">\n"
			+ "        <subDiffElements xsi:type=\"diff:ReferenceChangeRightTarget\">\n"
			+ "          <reference href=\"http://www.eclipse.org/emf/2002/Ecore#//EClass/eSuperTypes\"/>\n"
			+ "          <rightElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/child.ecore#//ClassInChild\"/>\n"
			+ "          <leftElement href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/child.ecore#//ClassInChild\"/>\n"
			+ "          <rightTarget href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#//ClassInParentRenamed\"/>\n"
			+ "          <leftTarget href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore#//ClassInParent\"/>\n"
			+ "        </subDiffElements>\n"
			+ "        <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/child.ecore#//ClassInChild\"/>\n"
			+ "      </subDiffElements>\n"
			+ "      <rightParent href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/child.ecore#/\"/>\n"
			+ "    </subDiffElements>\n"
			+ "  </ownedElements>\n"
			+ "  <leftRoots href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v1/parent.ecore#/\"/>\n"
			+ "  <rightRoots href=\"platform:/plugin/org.eclipse.emf.compare.tests/inputs/fragment/v2/parent.ecore#/\"/>\n"
			+ "</diff:DiffModel>\n";

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
