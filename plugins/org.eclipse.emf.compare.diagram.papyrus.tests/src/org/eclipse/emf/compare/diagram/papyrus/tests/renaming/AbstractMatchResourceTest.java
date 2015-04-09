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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.AbstractMergeRenamingMatchResources;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MergeRenamingMatchResources2Ways;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MergeRenamingMatchResources3Ways;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.junit.Before;

/**
 * Tests the {@link MergeRenamingMatchResources2Ways} and
 * {@link MergeRenamingMatchResources3Ways} classes. Can be run in standalone
 * mode, no need to be run as eclipse plug-in test.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractMatchResourceTest {

	protected Comparison comparison;

	protected AbstractMergeRenamingMatchResources trt;

	protected Resource newResource(String uri) {
		return new ResourceImpl(URI.createPlatformResourceURI(uri, false));
	}

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

	protected void checkComparisonContainsMatchResource(String leftURI,
			String rightURI) {
		if (leftURI != null) {
			URI uri = URI.createPlatformResourceURI(leftURI, false);
			String originURIToCheck = uri.toString();
			for (MatchResource mr : comparison.getMatchedResources()) {
				if (originURIToCheck.equals(mr.getLeftURI())) {
					checkURI(rightURI, mr.getRightURI());
					return;
				}
			}
		} else if (rightURI != null) {
			for (MatchResource mr : comparison.getMatchedResources()) {
				if (equalURIs(rightURI, mr.getRightURI())) {
					checkURI(leftURI, mr.getLeftURI());
					return;
				}
			}
		} else {
			throw new IllegalStateException(
					"Invalid test set-up: at least one provided URI must be non-null.");
		}
		fail("No MatchResource found provided URIs " + leftURI + ", "
				+ rightURI);
	}

	protected void checkComparisonContainsMatchResource(String originURI,
			String leftURI, String rightURI) {
		if (originURI != null) {
			for (MatchResource mr : comparison.getMatchedResources()) {
				if (equalURIs(originURI, mr.getOriginURI())) {
					checkURI(leftURI, mr.getLeftURI());
					checkURI(rightURI, mr.getRightURI());
					return;
				}
			}
		} else if (leftURI != null) {
			for (MatchResource mr : comparison.getMatchedResources()) {
				if (equalURIs(leftURI, mr.getLeftURI())) {
					checkURI(originURI, mr.getOriginURI());
					checkURI(rightURI, mr.getRightURI());
					return;
				}
			}
		} else if (rightURI != null) {
			for (MatchResource mr : comparison.getMatchedResources()) {
				if (equalURIs(rightURI, mr.getRightURI())) {
					checkURI(originURI, mr.getOriginURI());
					checkURI(leftURI, mr.getLeftURI());
					return;
				}
			}
		} else {
			throw new IllegalStateException(
					"Invalid test set-up: at least one provided URI must be non-null.");
		}
		fail("No MatchResource found provided URIs " + originURI + ", "
				+ leftURI + ", " + rightURI);
	}

	protected void checkURI(String expected, String actual) {
		assertTrue(equalURIs(expected, actual));
	}

	protected boolean equalURIs(String expected, String actual) {
		if (expected == null) {
			return actual == null;
		}
		return URI.createPlatformResourceURI(expected, false).toString()
				.equals(actual);
	}

	@Before
	public void setUp() {
		createComparison();
		createMergeRenamingMatchResourceTreatment();
	}

	protected abstract void createMergeRenamingMatchResourceTreatment();

	protected void createComparison() {
		comparison = CompareFactory.eINSTANCE.createComparison();
	}
}
