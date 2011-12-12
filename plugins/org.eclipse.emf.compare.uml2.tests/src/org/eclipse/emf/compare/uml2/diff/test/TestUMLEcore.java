package org.eclipse.emf.compare.uml2.diff.test;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.engine.DefaultMatchScopeProvider;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for cases for Ecore having proxies (which might be resolvables or not). see #362997
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class TestUMLEcore {
	@Test
	public void identicEcoresWithProxiesAndScope() throws IOException, InterruptedException {
		ResourceSet s1 = new ResourceSetImpl();
		Resource v1 = s1.getResource(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/model/UML.ecore"), //$NON-NLS-1$
				true);

		ResourceSet s2 = new ResourceSetImpl();

		Resource v2 = s2.getResource(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/model/UML.ecore"), //$NON-NLS-1$
				true);

		EcoreUtil.resolveAll(s1);
		EcoreUtil.resolveAll(s2);

		final Map<String, Object> options = new EMFCompareMap<String, Object>();
		options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new DefaultMatchScopeProvider(v1, v2));
		MatchModel match = MatchService.doResourceMatch(v1, v2, options);
		DiffModel diff = DiffService.doDiff(match, false);

		Assert.assertFalse(
				"Even if references are not in scope we should have no difference, especially not references, both models are identicals.", //$NON-NLS-1$
				Iterators.filter(diff.eAllContents(), ReferenceChange.class).hasNext());

		Assert.assertEquals(
				"Even if references are not in scope we should have no difference, especially not references, both models are identicals.", //$NON-NLS-1$
				0, diff.getDifferences().size());

	}

	@Test
	public void identicEcoresWithProxies() throws IOException, InterruptedException {
		ResourceSet s1 = new ResourceSetImpl();
		Resource v1 = s1.getResource(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/model/UML.ecore"), //$NON-NLS-1$
				true);

		ResourceSet s2 = new ResourceSetImpl();

		Resource v2 = s2.getResource(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/model/UML.ecore"), //$NON-NLS-1$
				true);

		EcoreUtil.resolveAll(s1);
		EcoreUtil.resolveAll(s2);

		final Map<String, Object> options = new EMFCompareMap<String, Object>();
		MatchModel match = MatchService.doResourceMatch(v1, v2, options);
		DiffModel diff = DiffService.doDiff(match, false);

		Assert.assertFalse(
				"Even if references are not in scope we should have no difference, especially not references, both models are identicals.", //$NON-NLS-1$
				Iterators.filter(diff.eAllContents(), ReferenceChange.class).hasNext());

		Assert.assertEquals(
				"Even if references are not in scope we should have no difference, especially not references, both models are identicals.", //$NON-NLS-1$
				0, diff.getDifferences().size());

	}

}
