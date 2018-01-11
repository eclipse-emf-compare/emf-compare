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

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * A model inclusion tester based on a {@link IFile file's} content type.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ContentTypeModelInclusionTester extends AbstractModelInclusionTester {

	/** The content type this tester tests against. */
	private String contentType;

	/**
	 * Creates a tester for the given <code>contentType</code>.
	 * 
	 * @param contentType
	 *            The content type to test against.
	 * @param key
	 *            The key of this tester.
	 */
	public ContentTypeModelInclusionTester(String contentType, String key) {
		super(key);
		this.contentType = checkNotNull(contentType);
	}

	/**
	 * Tests whether the file should be included based on the content type of this tester.
	 * 
	 * @param file
	 *            the file to test.
	 * @return <code>true</code> if it should be included, <code>false</code> otherwise.
	 */
	public boolean shouldInclude(IFile file) {
		try {
			return file != null && file.getContentDescription() != null
					&& file.getContentDescription().getContentType() != null
					&& contentType.equals(file.getContentDescription().getContentType().getId());
		} catch (CoreException e) {
			// can't access content type of specified file -- we'll ignore that
			return false;
		}
	}

}
