/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.ide.ui.internal.logical.RenameDetector;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.ITwoWayDiff;
import org.eclipse.team.core.diff.provider.ThreeWayDiff;
import org.eclipse.team.core.subscribers.Subscriber;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@SuppressWarnings({"nls", "boxing" })
public class RenameDetectorTest {

	RenameDetector sut;

	Subscriber subscriber;

	IStorageProviderAccessor accessor;

	IProject root;

	Map<IFile, String> contentsOrigin = new HashMap<IFile, String>();

	Map<IFile, String> contentsSource = new HashMap<IFile, String>();

	Map<IFile, String> contentsRemote = new HashMap<IFile, String>();

	@Before
	public void setUp() {
		subscriber = mock(Subscriber.class);
		accessor = mock(IStorageProviderAccessor.class);
		sut = new RenameDetector(subscriber, accessor);

		root = mock(IProject.class);
		when(subscriber.roots()).thenReturn(new IResource[] {root });
	}

	@Test
	public void testNoChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsRemote.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());
	}

	@Test
	public void testRenameSourceNoChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);
		IFile fileB2 = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB2, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsRemote.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertTrue(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.REMOTE).isPresent());
		assertTrue(sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).isPresent());

		assertSame(fileB2, sut.getFileAfterRename(fileB, DiffSide.SOURCE).get());
		assertSame(fileB, sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).get());
	}

	@Test
	public void testRenameRemoteNoChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);
		IFile fileB2 = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsRemote.put(fileB2, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertTrue(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).isPresent());
		assertTrue(sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).isPresent());

		assertSame(fileB2, sut.getFileAfterRename(fileB, DiffSide.REMOTE).get());
		assertSame(fileB, sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).get());
	}

	@Test
	public void testRenameSourceSlightChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);
		IFile fileB2 = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB2, SimilarityComputerTest.PREFIX2 + "contents2, slight change\nbla\n");
		contentsRemote.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertTrue(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.REMOTE).isPresent());
		assertTrue(sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).isPresent());

		assertSame(fileB2, sut.getFileAfterRename(fileB, DiffSide.SOURCE).get());
		assertSame(fileB, sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).get());
	}

	@Test
	public void testRenameRemoteSlightChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);
		IFile fileB2 = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsRemote.put(fileB2, SimilarityComputerTest.PREFIX2 + "contents2, slight change\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertTrue(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).isPresent());
		assertTrue(sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).isPresent());

		assertSame(fileB2, sut.getFileAfterRename(fileB, DiffSide.REMOTE).get());
		assertSame(fileB, sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).get());
	}

	@Test
	public void testRenameSourceBigChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);
		IFile fileB2 = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB2, SimilarityComputerTest.PREFIX + "contents2, slight change\nbla\n");
		contentsRemote.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).isPresent());
	}

	@Test
	public void testRenameRemoteBigChange() throws CoreException, UnsupportedEncodingException {
		IFile fileA = mock(IFile.class);
		IFile fileB = mock(IFile.class);
		IFile fileB2 = mock(IFile.class);

		contentsOrigin.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsSource.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");
		contentsRemote.put(fileA, SimilarityComputerTest.PREFIX + "contents\nbla\n");

		contentsOrigin.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsSource.put(fileB, SimilarityComputerTest.PREFIX2 + "contents2\nbla\n");
		contentsRemote.put(fileB2, SimilarityComputerTest.PREFIX + "contents2, slight change\nbla\n");

		processContents();

		assertFalse(sut.getFileAfterRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileA, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileA, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB, DiffSide.REMOTE).isPresent());

		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileAfterRename(fileB2, DiffSide.REMOTE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.SOURCE).isPresent());
		assertFalse(sut.getFileBeforeRename(fileB2, DiffSide.REMOTE).isPresent());
	}

	private void processContents() throws CoreException, UnsupportedEncodingException {
		Set<IFile> files = new HashSet<IFile>();
		files.addAll(contentsOrigin.keySet());
		files.addAll(contentsSource.keySet());
		files.addAll(contentsRemote.keySet());

		for (IFile file : files) {
			String originContent = contentsOrigin.get(file);
			String remoteContent = contentsRemote.get(file);
			String sourceContent = contentsSource.get(file);

			int remoteDiffKind = diffKind(originContent, remoteContent);
			int sourceDiffKind = diffKind(originContent, sourceContent);

			ThreeWayDiff threeWayDiff = mock(ThreeWayDiff.class);

			ITwoWayDiff remoteDiff = mock(ITwoWayDiff.class);
			ITwoWayDiff sourceDiff = mock(ITwoWayDiff.class);

			when(remoteDiff.getKind()).thenReturn(remoteDiffKind);
			when(sourceDiff.getKind()).thenReturn(sourceDiffKind);
			when(threeWayDiff.getLocalChange()).thenReturn(sourceDiff);
			when(threeWayDiff.getRemoteChange()).thenReturn(remoteDiff);

			when(subscriber.getDiff(file)).thenReturn(threeWayDiff);

			IStorageProvider originProvider = mock(IStorageProvider.class);
			IStorageProvider remoteProvider = mock(IStorageProvider.class);
			IStorageProvider sourceProvider = mock(IStorageProvider.class);

			IStorage originStorage = mock(IStorage.class);
			IStorage remoteStorage = mock(IStorage.class);
			IStorage sourceStorage = mock(IStorage.class);

			when(originProvider.getStorage(any(IProgressMonitor.class))).thenReturn(originStorage);
			when(remoteProvider.getStorage(any(IProgressMonitor.class))).thenReturn(remoteStorage);
			when(sourceProvider.getStorage(any(IProgressMonitor.class))).thenReturn(sourceStorage);

			when(originStorage.getContents()).then(openInputStream(originContent));
			when(remoteStorage.getContents()).then(openInputStream(remoteContent));
			when(sourceStorage.getContents()).then(openInputStream(sourceContent));

			when(accessor.getStorageProvider(file, DiffSide.ORIGIN)).thenReturn(originProvider);
			when(accessor.getStorageProvider(file, DiffSide.REMOTE)).thenReturn(remoteProvider);
			when(accessor.getStorageProvider(file, DiffSide.SOURCE)).thenReturn(sourceProvider);

		}
		when(subscriber.members(root)).thenReturn(files.toArray(new IFile[0]));
	}

	private Answer<?> openInputStream(String content) throws UnsupportedEncodingException {
		final byte[] bytes = content == null ? null : content.getBytes("UTF-8");
		return new Answer<InputStream>() {
			public InputStream answer(InvocationOnMock invocation) throws Throwable {
				return bytes == null ? null : new ByteArrayInputStream(bytes);
			}
		};
	}

	private int diffKind(String originContent, String branchContent) {
		if (originContent == null && branchContent == null) {
			return IDiff.NO_CHANGE;
		} else if (originContent == null) {
			return IDiff.ADD;
		} else if (branchContent == null) {
			return IDiff.REMOVE;
		} else {
			return IDiff.CHANGE;
		}
	}
}
