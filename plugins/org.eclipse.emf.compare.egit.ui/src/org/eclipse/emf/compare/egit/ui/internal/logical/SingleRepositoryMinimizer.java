/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.ui.internal.logical;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.jgit.lib.Repository;

/**
 * Instances of this class will be used by EMF Compare to minimize the left scope by pruning resources that do
 * not belong to an expected repository.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class SingleRepositoryMinimizer implements IModelMinimizer {

	/** the expected repository resources must belong to. */
	protected Repository expectedRepository;

	/**
	 * Creates a new minimizer that removes all resources that do not belong an expected repository.
	 */
	public SingleRepositoryMinimizer() {
	}

	/**
	 * {@inheritDoc} Specifically, we'll remove all resources that do not belong to the same repository as the
	 * expected repository. If no repository is expected, this minimizer does nothing.
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer#minimize(org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void minimize(SynchronizationModel syncModel, IProgressMonitor monitor) {
		if (getExpectedRepository() == null) {
			// we don't prune anything
			return;
		}
		SubMonitor progess = SubMonitor.convert(monitor, 100);
		progess.subTask(EMFCompareIDEUIMessages.getString("EMFSynchronizationModel.minimizing")); //$NON-NLS-1$

		final StorageTraversal leftTraversal = syncModel.getLeftTraversal();
		final Set<? extends IStorage> leftCopy = leftTraversal.getStorages();

		SubMonitor subMonitor = progess.newChild(100).setWorkRemaining(leftCopy.size());
		for (IStorage left : leftCopy) {
			if (!hasExpectedRepository(left)) {
				leftTraversal.removeStorage(left);
			}
			subMonitor.worked(1);
		}
	}

	/**
	 * {@inheritDoc} Specifically, this minimizer remove all resources that do not have the same repository as
	 * the repository the given file is contained in.
	 * 
	 * @param file
	 *            The file that has been used as the starting point to resolve the left logical model and from
	 *            which the expected repository is retrieved
	 */
	public void minimize(IFile file, SynchronizationModel syncModel, IProgressMonitor monitor) {
		setExpectedRepository(getRepository(file));
		minimize(syncModel, monitor);
	}

	/**
	 * Returns true if the given storage belongs to the expected repository. If no repository is expected,
	 * this method always returns true.
	 * 
	 * @param storage
	 *            storage that may be contained in a repository
	 * @return true if the storage has the expected repository, false otherwise
	 */
	protected boolean hasExpectedRepository(IStorage storage) {
		return getExpectedRepository() == null || getExpectedRepository() == getRepository(storage);
	}

	/**
	 * Sets the expected repository to the given instance. If null is given, no repository is expected and
	 * this minimizer does nothing.
	 * 
	 * @param expectedRepository
	 *            repository to which resources must belong in order to stay in the logical model
	 */
	protected void setExpectedRepository(Repository expectedRepository) {
		this.expectedRepository = expectedRepository;
	}

	/**
	 * Returns the expected repository or null if no repository is expected.
	 * 
	 * @return expected repository or null.
	 */
	public Repository getExpectedRepository() {
		return expectedRepository;
	}

	/**
	 * Returns the repository for the given file.
	 * 
	 * @param file
	 *            file that may be contained in a repository
	 * @return repository or null if no associated repository can be found.
	 * @see ResourceUtil#getRepository(org.eclipse.core.resources.IResource)
	 */
	public static Repository getRepository(IFile file) {
		if (file == null) {
			return null;
		}
		return ResourceUtil.getRepository(file);
	}

	/**
	 * Returns the repository for the given storage by adapting it to a file, if possible.
	 * 
	 * @param storage
	 *            storage to be adapted to a file
	 * @return repository or null if no associated repository can be found.
	 */
	public static Repository getRepository(IStorage storage) {
		IFile file = (IFile)storage.getAdapter(IFile.class);
		if (file != null) {
			return getRepository(file);
		}
		return null;
	}
}
