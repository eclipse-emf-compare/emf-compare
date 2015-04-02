/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.hasModelType;

import com.google.common.collect.ImmutableSet;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;

/**
 * This will listen to workspace changes and react to all changes on "model" resources as determined by
 * {@link ThreadedModelResolver#MODEL_CONTENT_TYPES}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see ThreadedModelResolver#hasModelType(IFile)
 */
public class ModelResourceListener implements IResourceChangeListener, IResourceDeltaVisitor {
	/** Keeps track of the URIs that need to be reparsed when next we need the dependencies graph . */
	protected final Set<URI> changedURIs;

	/** Tracks the files that have been removed. */
	protected final Set<URI> removedURIs;

	/** Prevents concurrent access to the two internal sets. */
	protected final ReentrantLock internalLock;

	/** Initializes this listener. */
	public ModelResourceListener() {
		this.changedURIs = new LinkedHashSet<URI>();
		this.removedURIs = new LinkedHashSet<URI>();
		this.internalLock = new ReentrantLock();
	}

	/** {@inheritDoc} */
	public void resourceChanged(IResourceChangeEvent event) {
		final IResourceDelta delta = event.getDelta();
		if (delta == null) {
			return;
		}

		/*
		 * We must block any and all threads from using the two internal sets through either popChangedURIs or
		 * popRemovedURIs while we are parsing a resource delta. This particular locking is here to avoid such
		 * misuses.
		 */
		internalLock.lock();
		try {
			delta.accept(this);
		} catch (CoreException e) {
			EMFCompareIDEUIPlugin.getDefault().log(e);
		} finally {
			internalLock.unlock();
		}
	}

	/**
	 * Retrieves the set of all changed URIs since we last updated the dependencies graph, and clears it for
	 * subsequent calls.
	 * 
	 * @return The set of all changed URIs since we last updated the dependencies graph.
	 */
	public Set<URI> popChangedURIs() {
		final Set<URI> changed;
		internalLock.lock();
		try {
			changed = ImmutableSet.copyOf(changedURIs);
			changedURIs.clear();
		} finally {
			internalLock.unlock();
		}
		return changed;
	}

	/**
	 * Retrieves the set of all removed URIs since we last updated the dependencies graph, and clears it for
	 * subsequent calls.
	 * 
	 * @return The set of all removed URIs since we last updated the dependencies graph.
	 */
	public Set<URI> popRemovedURIs() {
		final Set<URI> removed;
		internalLock.lock();
		try {
			removed = ImmutableSet.copyOf(removedURIs);
			removedURIs.clear();
		} finally {
			internalLock.unlock();
		}
		return removed;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	public boolean visit(IResourceDelta delta) throws CoreException {
		// Note : the lock (#lock) must be acquired by the current thread _before_ calling #accept() on
		// this visitor.
		if (delta.getFlags() == IResourceDelta.MARKERS || delta.getResource().getType() != IResource.FILE) {
			return true;
		}

		final IFile file = (IFile)delta.getResource();
		final URI fileURI = createURIFor(file);
		// We can't check the content type of a removed resource
		if (delta.getKind() == IResourceDelta.REMOVED) {
			removedURIs.add(fileURI);
			changedURIs.remove(fileURI);
		} else if (hasModelType(file)) {
			if ((delta.getKind() & IResourceDelta.CHANGED) != 0) {
				changedURIs.add(fileURI);
				// Probably can't happen, but let's stay on the safe side
				removedURIs.remove(fileURI);
			} else if ((delta.getKind() & IResourceDelta.ADDED) != 0) {
				// If a previously removed resource is changed, we can assume it's been re-added
				if (removedURIs.remove(fileURI)) {
					changedURIs.add(fileURI);
				}
			}
		}

		return true;
	}
}
