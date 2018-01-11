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

/**
 * A model inclusion tester based on a {@link IFile file's} extension.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class FileExtensionModelInclusionTester extends AbstractModelInclusionTester {

	/** The file extension this tester tests against. */
	private String fileExtension;

	/**
	 * Creates a tester for the given <code>fileExtension</code>.
	 * 
	 * @param fileExtension
	 *            The file extension to test against.
	 * @param key
	 *            The key of this tester.
	 */
	public FileExtensionModelInclusionTester(String fileExtension, String key) {
		super(key);
		this.fileExtension = checkNotNull(fileExtension);
	}

	/**
	 * Tests whether the file should be included based on the file extension of this tester.
	 * 
	 * @param file
	 *            the file to test.
	 * @return <code>true</code> if it should be included, <code>false</code> otherwise.
	 */
	public boolean shouldInclude(IFile file) {
		return file != null && fileExtension.equals(file.getFileExtension());
	}

}
