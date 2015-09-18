/*******************************************************************************
 * Copyright (C) 2015 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.models;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.egit.core.op.MergeOperation;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.GitTestRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Before;

/**
 * Provides shared utility methods for unit tests working on logical models. The model provider used for
 * tests, {@link SampleModelProvider}, links all "*.sample" files from a common directory into a single
 * logical model.
 */
@SuppressWarnings("restriction")
public abstract class ModelTestCase extends CompareGitTestCase {
	protected static final String SAMPLE_FILE_EXTENSION = SampleModelProvider.SAMPLE_FILE_EXTENSION;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		IContentType textType = Platform.getContentTypeManager().getContentType(
				"org.eclipse.core.runtime.text"); //$NON-NLS-1$
		textType.addFileSpec(SAMPLE_FILE_EXTENSION, IContentType.FILE_EXTENSION_SPEC);
	}

	protected RevCommit setContentsAndCommit(GitTestRepository testRepository, IFile targetFile,
			String newContents, String commitMessage) throws Exception {
		targetFile.setContents(new ByteArrayInputStream(newContents.getBytes("UTF-8")), IResource.FORCE, //$NON-NLS-1$
				new NullProgressMonitor());
		testRepository.addToIndex(targetFile);
		return testRepository.commit(commitMessage);
	}

	/**
	 * Checks that the content of the given file is equal to the given String. End-of-line characters are NOT
	 * ignored: They must be equal too, i.e. the expected content is tested as is and not tokenized against
	 * line separators.
	 * 
	 * @param file
	 * @param expectedContents
	 * @throws Exception
	 */
	protected void assertContentEquals(IFile file, String expectedContents) throws Exception {
		Scanner scanner = new Scanner(file.getContents());
		try {
			scanner.useDelimiter("\\A"); //$NON-NLS-1$
			String fileContent = ""; //$NON-NLS-1$
			if (scanner.hasNext()) {
				fileContent = scanner.next();
			}
			assertEquals(expectedContents, fileContent);
		} finally {
			scanner.close();
		}
	}

	protected void merge(Repository repo, String refName) throws CoreException {
		new MergeOperation(repo, refName).execute(null);
	}

	protected Status status(Repository repo) throws Exception {
		Git git = new Git(repo);
		try {
			return git.status().call();
		} finally {
			git.close();
		}
	}
}
