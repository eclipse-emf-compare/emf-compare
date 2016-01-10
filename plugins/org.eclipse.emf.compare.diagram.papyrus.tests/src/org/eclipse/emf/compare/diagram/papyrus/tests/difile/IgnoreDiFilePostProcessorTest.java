/*******************************************************************************
 * Copyright (C) 2016 EclipseSource Munich Gmbh and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.diagram.papyrus.tests.difile;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diagram.papyrus.internal.IgnoreDiFilePostProcessor;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests whether the {@link IgnoreDiFilePostProcessor} correctly removes the
 * matches of model elements in the di-files.
 * <p>
 * By removing the matches of di-file model elements, all changes to these model
 * elements will be ignored subsequently. Without ignoring those changes, this
 * scenario would lead to a conflict. The requirement, however, dictates that
 * di-file changes should never lead to a conflict (cf. bug 485494).
 * </p>
 *
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class IgnoreDiFilePostProcessorTest extends AbstractTest {

	/**
	 * The input data for this test.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private final class IgnoreDiFileModelElementsData extends DiagramInputData {
		public Resource getLeft() throws IOException {
			return loadFromClassLoader("data/left/model.di");
		}

		public Resource getRight() throws IOException {
			return loadFromClassLoader("data/right/model.di");
		}

		public Resource getOrigin() throws IOException {
			return loadFromClassLoader("data/origin/model.di");
		}
	}

	/**
	 * Returns the input data for this test.
	 */
	@Override
	protected IgnoreDiFileModelElementsData getInput() {
		return new IgnoreDiFileModelElementsData();
	}

	/**
	 * Register the {@link IgnoreDiFilePostProcessor}.
	 */
	@Before
	public void registerIgnoreDiFilePostProcessor() {
		getPostProcessorRegistry().put(
				IgnoreDiFilePostProcessor.class.getName(),
				new TestPostProcessor.TestPostProcessorDescriptor(Pattern
						.compile("http://www.eclipse.org/papyrus/\\d.\\d.\\d/sashdi"), null,
						new IgnoreDiFilePostProcessor(), 35));
	}

	/**
	 * Tests whether the matches of a comparison remain empty even though the
	 * di-files contain model elements.
	 */
	@Test
	public void testA1() throws IOException {
		IgnoreDiFileModelElementsData input = getInput();
		final Resource left = input.getLeft();
		final Resource right = input.getRight();
		final Resource origin = input.getOrigin();

		Comparison comparison = buildComparison(left, right, origin);

		assertEquals(0, comparison.getMatches().size());
	}
}
