/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Tobias Ortmayr - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.internal.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeEditorInput;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeInput;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that the {@link ComparisonScopeEditorInput} is correctly initialized with the
 * {@link EMFCompareConfiguration}. This test is related to the bug
 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=506928">506928</a>
 * 
 * @author <a href="mailto:tobias.ortmayr@gmail.com">Tobias Ortmayr</a>
 */
@SuppressWarnings("restriction")
public class TestBug506928 {
	private Resource left, right, origin;

	private ICompareEditingDomain editingDomain;

	private ComposedAdapterFactory adapterFactory;

	private EMFCompare comparator;

	@Before
	public void setUp() throws IOException {
		Bug506928InputData inputData = new Bug506928InputData();
		left = inputData.getResource("left.nodes"); //$NON-NLS-1$
		right = inputData.getResource("right.nodes"); //$NON-NLS-1$
		origin = inputData.getResource("origin.nodes"); //$NON-NLS-1$
		editingDomain = EMFCompareEditingDomain.create(left, right, origin);
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		comparator = EMFCompare.builder().build();
	}

	@SuppressWarnings({"boxing" })
	protected void testComparisonScopeEditorInput(boolean leftEditable, boolean rightEditable) {
		// create configuration
		EMFCompareConfiguration configuration = new EMFCompareConfiguration(new CompareConfiguration());
		configuration.setLeftEditable(leftEditable);
		configuration.setRightEditable(rightEditable);

		// create comparison scope editor input based on configuration
		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		ComparisonScopeEditorInput input = new ComparisonScopeEditorInput(configuration, editingDomain,
				adapterFactory, comparator, scope);

		// no comparison result before opening the editor
		assertEquals(input.getCompareResult(), null);

		// open editor to get comparison result
		try {
			input.run(new NullProgressMonitor());
		} catch (InvocationTargetException e) {
			fail(e.getMessage());
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}

		// ensure correct result
		assertTrue(input.getCompareResult() instanceof ComparisonScopeInput);
		ComparisonScopeInput result = (ComparisonScopeInput)input.getCompareResult();

		// assert compare configuration has not been changed
		assertEquals(leftEditable, input.getCompareConfiguration().isLeftEditable());
		assertEquals(rightEditable, input.getCompareConfiguration().isRightEditable());

		// assert result reflects the correct configuration
		assertEquals(input.getCompareConfiguration().isLeftEditable(), result.isLeftEditable());
		assertEquals(input.getCompareConfiguration().isRightEditable(), result.isRightEditable());
	}

	/**
	 * Tests that the compare configuration is propagated correctly when both sides are not editable.
	 */
	@Test
	public void testComparisionScopeEditorInput_BothNotEditable() {
		testComparisonScopeEditorInput(false, false);
	}

	/**
	 * Tests that the compare configuration is propagated correctly when only the left side is editable.
	 */
	@Test
	public void testComparisionScopeEditorInput_LeftEditable() {
		testComparisonScopeEditorInput(true, false);
	}

	/**
	 * Tests that the compare configuration is propagated correctly when only the right side is editable.
	 */
	@Test
	public void testComparisionScopeEditorInput_RightEditable() {
		testComparisonScopeEditorInput(false, true);
	}

	/**
	 * Tests that the compare configuration is propagated correctly when both sides are editable.
	 */
	@Test
	public void testComparisionScopeEditorInput_BothEditable() {
		testComparisonScopeEditorInput(true, true);
	}

	public class Bug506928InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_506928/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}

}
