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
package org.eclipse.emf.compare.ide.internal.logical;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.ide.logical.IModelInclusionTester;

/**
 * Factory for creating {@link IModelInclusionTester model inclusion testers} out of
 * {@link IConfigurationElement configuration elements} registered through the extension point.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ModelInclusionTesterFactory {

	/** The attribute name of file-extension-based testers. */
	protected static final String FILE_EXTENSION = "fileExtension"; //$NON-NLS-1$

	/** The attribute name of content-type-based testers. */
	protected static final String CONTENT_TYPE = "contentType"; //$NON-NLS-1$

	/** The attribute name of dynamic testers. */
	protected static final String DYNAMIC_TESTER = "dynamicTester"; //$NON-NLS-1$

	/**
	 * Specifies whether this factory can create a model inclusion tester from the given <code>element</code>.
	 * 
	 * @param element
	 *            the element to test whether this factory can create a tester for.
	 * @return <code>true</code> if it can, <code>false</code> otherwise.
	 */
	public boolean canCreateModelInclusionTester(IConfigurationElement element) {
		return getModelInclusionTesterType(element) != null;
	}

	/**
	 * Creates a model inclusion tester for the specified <code>element</code>.
	 * 
	 * @param element
	 *            The element to create a model inclusion tester for.
	 * @return the created model inclusion tester.
	 */
	public AbstractModelInclusionTester createModelInclusionTester(IConfigurationElement element) {
		switch (getModelInclusionTesterType(element)) {
			case FILE_EXTENSION:
				return new FileExtensionModelInclusionTester(element.getAttribute(FILE_EXTENSION),
						element.getAttribute(getModelInclusionTesterType(element)));
			case CONTENT_TYPE:
				return new ContentTypeModelInclusionTester(element.getAttribute(CONTENT_TYPE),
						element.getAttribute(getModelInclusionTesterType(element)));
			case DYNAMIC_TESTER:
				return new DynamicModelInclusionTester(element,
						element.getAttribute(getModelInclusionTesterType(element)));
			default:
				throw new IllegalArgumentException("Unknown extension type"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns a key for the specified <code>element</code>.
	 * 
	 * @param element
	 *            The element to get the key for.
	 * @return The key.
	 */
	public String getKey(IConfigurationElement element) {
		return element.getAttribute(getModelInclusionTesterType(element));
	}

	/**
	 * Specifies the type depending on which attribute is specified in the provided <code>element</code>.
	 * 
	 * @param element
	 *            The element.
	 * @return The type, either {@link #FILE_EXTENSION}, {@link #CONTENT_TYPE}, {@link #DYNAMIC_TESTER}, or
	 *         <code>null</code> if no attribute is specified.
	 */
	private String getModelInclusionTesterType(IConfigurationElement element) {
		if (element.getAttribute(FILE_EXTENSION) != null) {
			return FILE_EXTENSION;
		} else if (element.getAttribute(CONTENT_TYPE) != null) {
			return CONTENT_TYPE;
		} else if (element.getAttribute(DYNAMIC_TESTER) != null) {
			return DYNAMIC_TESTER;
		}
		return null;
	}

}
