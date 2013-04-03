/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;

import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ISharedDocumentAdapter;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.variants.CachedResourceVariant;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.ui.IEditorInput;

/** This implementation of IStorage simply wraps a stream content accessor. */
public class StreamAccessorStorage implements IStorage {
	/** The wrapped accessor. */
	private final IStreamContentAccessor accessor;

	/** Full path of the underlying content. */
	private final String fullPath;

	/**
	 * Wraps the given accessor.
	 * 
	 * @param accessor
	 *            The accessor to wrap as an IStorage.
	 * @param fullPath
	 *            Full path to the underlying storage.
	 */
	public StreamAccessorStorage(IStreamContentAccessor accessor, String fullPath) {
		this.accessor = accessor;
		this.fullPath = fullPath;
	}

	/**
	 * Creates a StreamAccessorStorage given the input typed element. Note that the given typed element -must-
	 * implement {@link IStreamContentAccessor} as well.
	 * 
	 * @param element
	 *            The typed element for which we need to create a wrapper.
	 * @return The wrapped typed element.
	 * @throws IllegalArgumentException
	 *             If the given element does not implement {@link IStreamContentAccessor}.
	 */
	public static StreamAccessorStorage fromTypedElement(ITypedElement element)
			throws IllegalArgumentException {
		if (!(element instanceof IStreamContentAccessor)) {
			throw new IllegalArgumentException();
		}

		final String fullPath;
		final IFile file = findFile(element);

		if (file != null) {
			fullPath = file.getFullPath().toString();
		} else {
			final IFileRevision revision = findFileRevision(element);
			String tmp = null;
			if (revision != null) {
				final URI uri = revision.getURI();
				if (uri != null) {
					tmp = uri.toString();
				} else if (revision instanceof IAdaptable) {
					final IResourceVariant variant = (IResourceVariant)((IAdaptable)revision)
							.getAdapter(IResourceVariant.class);
					if (variant instanceof CachedResourceVariant) {
						tmp = ((CachedResourceVariant)variant).getDisplayPath().toString();
					}
				}
			}

			if (tmp != null) {
				fullPath = tmp;
			} else {
				// We can't do much here...
				fullPath = element.getName();
			}
		}

		return new StreamAccessorStorage((IStreamContentAccessor)element, fullPath);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	// suppressing since overriding.
	@SuppressWarnings({"rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		Object adapted = null;
		if (adapter.isInstance(this)) {
			adapted = this;
		} else if (adapter == IStreamContentAccessor.class) {
			adapted = accessor;
		} else if (accessor instanceof ITypedElement) {
			if (adapter == ITypedElement.class) {
				adapted = accessor;
			} else if (adapter.isAssignableFrom(IFile.class)) {
				adapted = findFile((ITypedElement)accessor);
			}
		}
		return adapted;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getContents()
	 */
	public InputStream getContents() throws CoreException {
		return accessor.getContents();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getFullPath()
	 */
	public IPath getFullPath() {
		return new Path(fullPath);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getName()
	 */
	public String getName() {
		if (accessor instanceof ITypedElement) {
			return ((ITypedElement)accessor).getName();
		}
		return getFullPath().lastSegment();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#isReadOnly()
	 */
	public boolean isReadOnly() {
		if (accessor instanceof ITypedElement) {
			final IFile file = findFile((ITypedElement)accessor);
			if (file != null) {
				return file.isReadOnly();
			}
		}
		final File file = getFullPath().toFile();
		return !file.exists() || !file.canWrite();
	}

	/**
	 * Try and determine the resource of the given element.
	 * 
	 * @param element
	 *            The element for which we need an {@link IFile}.
	 * @return The resource corresponding to the given {@code element} if we could find it, <code>null</code>
	 *         otherwise or if the resource is not a file.
	 */
	private static IFile findFile(ITypedElement element) {
		if (element == null) {
			return null;
		}

		// Can we adapt it directly?
		IResource resource = adaptAs(element, IResource.class);
		if (resource == null) {
			// We know about some types ...
			if (element instanceof IResourceProvider) {
				resource = ((IResourceProvider)element).getResource();
			}
		}

		if (resource instanceof IFile) {
			return (IFile)resource;
		}
		// Try with IFile in case adapter only checked for class equality
		return adaptAs(element, IFile.class);
	}

	/**
	 * Try and determine the file revision of the given element.
	 * 
	 * @param element
	 *            The element for which we need an {@link IFileRevision}.
	 * @return The file revision of the given element if we could find one, <code>null</code> otherwise.
	 */
	private static IFileRevision findFileRevision(ITypedElement element) {
		if (element == null) {
			return null;
		}

		// Can we adapt it directly?
		IFileRevision revision = adaptAs(element, IFileRevision.class);
		if (revision == null) {
			// Quite the workaround... but CVS does not offer us any other way.
			// These few lines of code is what make us depend on org.eclipse.ui... Can we find another way?
			final ISharedDocumentAdapter documentAdapter = adaptAs(element, ISharedDocumentAdapter.class);
			if (documentAdapter != null) {
				final IEditorInput editorInput = documentAdapter.getDocumentKey(element);
				if (editorInput != null) {
					revision = adaptAs(editorInput, IFileRevision.class);
				}
			}
		}

		if (revision == null) {
			// Couldn't do it the API way ...
			// At the time of writing, this was the case with EGit
			try {
				final Method method = element.getClass().getMethod("getFileRevision"); //$NON-NLS-1$
				final Object value = method.invoke(element);
				if (value instanceof IFileRevision) {
					revision = (IFileRevision)value;
				}
				// CHECKSTYLE:OFF this would require five "catch" for ignored exceptions...
			} catch (Exception e) {
				// CHECKSTYLE:ON
			}
		}

		return revision;
	}

	/**
	 * Tries and adapt the given <em>object</em> to an instance of the given class.
	 * 
	 * @param <T>
	 *            Type to which we need to adapt <em>object</em>.
	 * @param object
	 *            The object we need to coerce to a given {@link Class}.
	 * @param clazz
	 *            Class to which we are to adapt <em>object</em>.
	 * @return <em>object</em> cast to type <em>T</em> if possible, <code>null</code> if not.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T adaptAs(Object object, Class<T> clazz) {
		if (object == null) {
			return null;
		}

		T result = null;
		if (clazz.isInstance(object)) {
			result = (T)object;
		} else if (object instanceof IAdaptable) {
			result = (T)((IAdaptable)object).getAdapter(clazz);
		}

		if (result == null) {
			result = (T)Platform.getAdapterManager().getAdapter(object, clazz);
		}

		return result;
	}
}
