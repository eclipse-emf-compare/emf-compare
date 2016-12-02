/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ide.utils.tests.helper.IStreamProvider;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Abstract test class that supports the mocking of {@link IStorage}.
 */
public abstract class AbstractStorageTest {
	@SuppressWarnings("boxing")
	protected static IStorage mockStorage(final IStreamProvider stream) throws IOException {
		try {
			IStorage mockStorage = mock(IStorage.class);
			when(mockStorage.getContents()).thenAnswer(new Answer<InputStream>() {
				public InputStream answer(InvocationOnMock invocation) throws Throwable {
					return stream.createInputStream();
				}
			});
			when(mockStorage.isReadOnly()).thenReturn(true);
			return mockStorage;
		} catch (CoreException cEx) {
			// this is merely a checked exception of mockStorage.getContents() and will never happen since we
			// use mockito
			return null;
		}
	}
}
