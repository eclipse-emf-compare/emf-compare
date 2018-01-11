/*******************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;

/**
 * Property tester specifying whether an {@link IFile} should be treated as EMF model resource and hence
 * should be handled by EMF Compare.
 * <p>
 * This tester takes into account all extensions registered via
 * <code>org.eclipse.emf.compare.ide.modelInclusionTester</code> to determine whether a given resource should
 * be treated as EMF model resource and returns <code>true</code> if any of the registered
 * <code>modelInclusionTexter</code> extensions specify that a file should be treated as EMF model resource.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class EMFModelResourceTester extends PropertyTester {

	private static final Object IS_MODEL_RESOURCE_PROPERTY = "isModelResource"; //$NON-NLS-1$

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IFile && IS_MODEL_RESOURCE_PROPERTY.equals(property)) {
			return isModelResource((IFile)receiver);
		}
		return false;
	}

	private boolean isModelResource(IFile file) {
		return EMFCompareIDEPlugin.getDefault().getModelInclusionTesterRegistry().anyTesterIncludes(file);
	}

}
