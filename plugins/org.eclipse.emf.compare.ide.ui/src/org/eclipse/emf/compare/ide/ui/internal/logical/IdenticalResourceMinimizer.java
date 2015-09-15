/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.binaryIdentical;

import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * Instances of this class will be used by EMF Compare to minimize the scope to parts of a logical model that
 * can be considered valid candidates for a difference.
 * <p>
 * This default implementation will consider that all files that are binary identical between the two (or
 * three) sides of the comparison can be safely removed from the scope. Likewise, unmatched read-only files
 * will be removed from the scope.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class IdenticalResourceMinimizer implements IModelMinimizer {

	/**
	 * {@inheritDoc} Specifically, we'll remove all resources that can be seen as binary identical (we match
	 * resources through exact equality of their names).
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer#minimize(org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void minimize(SynchronizationModel syncModel, IProgressMonitor monitor) {
		SubMonitor progess = SubMonitor.convert(monitor, 100);
		progess.subTask(EMFCompareIDEUIMessages.getString("EMFSynchronizationModel.minimizing")); //$NON-NLS-1$

		final StorageTraversal leftTraversal = syncModel.getLeftTraversal();
		final StorageTraversal rightTraversal = syncModel.getRightTraversal();
		final StorageTraversal originTraversal = syncModel.getOriginTraversal();

		final boolean threeWay = !originTraversal.getStorages().isEmpty();
		// Copy the sets to update them as we go.
		final Set<IStorage> leftCopy = Sets.newLinkedHashSet(leftTraversal.getStorages());
		final Set<IStorage> rightCopy = Sets.newLinkedHashSet(rightTraversal.getStorages());
		final Set<IStorage> originCopy = Sets.newLinkedHashSet(originTraversal.getStorages());

		SubMonitor subMonitor = progess.newChild(98).setWorkRemaining(leftCopy.size());
		for (IStorage left : leftCopy) {
			final IStorage right = removeLikeNamedStorageFrom(left, rightCopy);
			if (right != null && threeWay) {
				final IStorage origin = removeLikeNamedStorageFrom(left, originCopy);

				if (origin != null && equals(left, right, origin)) {
					leftTraversal.removeStorage(left);
					rightTraversal.removeStorage(right);
					originTraversal.removeStorage(origin);
				}
			} else if (right != null && equals(left, right)) {
				leftTraversal.removeStorage(left);
				rightTraversal.removeStorage(right);
			} else if (right == null && isIgnoredStorage(left)) {
				/*
				 * This has no match and is in plugins. We would detect an insane number of false positives on
				 * it (every element "added"), so remove it from the scope.
				 */
				leftTraversal.getStorages().remove(left);
			}
			subMonitor.worked(1);
		}

		subMonitor = progess.newChild(1).setWorkRemaining(rightCopy.size());
		for (IStorage right : rightCopy) {
			final IStorage origin = removeLikeNamedStorageFrom(right, originCopy);
			if (origin != null) {
				// we had a match in the origin, leave this file in scope (it's been removed from left)
			} else if (isIgnoredStorage(right)) {
				/*
				 * This has no match and is in plugins. We would detect an insane number of false positives on
				 * it (every element "removed"), so remove it from the scope.
				 */
				rightTraversal.removeStorage(right);
			}
			subMonitor.worked(1);
		}

		subMonitor = progess.newChild(1).setWorkRemaining(rightCopy.size());
		for (IStorage origin : originCopy) {
			// These have no match on left and right.
			if (isIgnoredStorage(origin)) {
				originTraversal.removeStorage(origin);
			}
			subMonitor.worked(1);
		}
	}

	/**
	 * Checks whether the three given (non-<code>null</code>) resources are identical. This default
	 * implementation only checks that the three are identical binary-wise.
	 * <p>
	 * Identical resources will be filtered out of the comparison scope.
	 * </p>
	 * 
	 * @param left
	 *            Left of the resources to consider.
	 * @param right
	 *            Right of the resources to consider.
	 * @param origin
	 *            Common ancestor of the left and right resources.
	 * @return <code>true</code> if the given resources are to be considered identical, <code>false</code>
	 *         otherwise.
	 */
	protected boolean equals(IStorage left, IStorage right, IStorage origin) {
		return binaryIdentical(left, right, origin);
	}

	/**
	 * Checks whether the two given (non-<code>null</code>) resources are identical. This default
	 * implementation only checks that the two are identical binary-wise.
	 * <p>
	 * Identical resources will be filtered out of the comparison scope.
	 * </p>
	 * 
	 * @param left
	 *            Left of the resources to consider.
	 * @param rightRight
	 *            of the resources to consider.
	 * @return <code>true</code> if the given resources are to be considered identical, <code>false</code>
	 *         otherwise.
	 */
	protected boolean equals(IStorage left, IStorage right) {
		return binaryIdentical(left, right);
	}

	/**
	 * Looks up into the {@code candidates} set for a storage which name matches that of the {@code reference}
	 * storage, removing it if there is one.
	 * 
	 * @param reference
	 *            The storage for which we'll seek a match into {@code candidates}.
	 * @param candidates
	 *            The set of candidates into which to look up for a match to {@code reference}.
	 * @return The first storage from the set of candidates that matches the {@code reference}, if any.
	 *         <code>null</code> if none match.
	 */
	protected IStorage removeLikeNamedStorageFrom(IStorage reference, Set<IStorage> candidates) {
		final String referenceName = reference.getName();
		final Iterator<IStorage> candidatesIterator = candidates.iterator();
		while (candidatesIterator.hasNext()) {
			final IStorage candidate = candidatesIterator.next();
			final String candidateName = candidate.getName();

			if (referenceName.equals(candidateName)) {
				candidatesIterator.remove();
				return candidate;
			}
		}
		return null;
	}

	/**
	 * We will remove from the scope any storage that is located in the plugins (detecting differences on such
	 * files is meaningless).
	 * 
	 * @param storage
	 *            The storage we need to test.
	 * @return <code>true</code> if this storage should be ignored and removed from this scope.
	 */
	private boolean isIgnoredStorage(IStorage storage) {
		return storage.getFullPath().toString().startsWith("platform:/plugin"); //$NON-NLS-1$
	}
}
