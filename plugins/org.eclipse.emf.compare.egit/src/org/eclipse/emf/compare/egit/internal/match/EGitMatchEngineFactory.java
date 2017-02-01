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
package org.eclipse.emf.compare.egit.internal.match;

import com.google.common.collect.Sets;

import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ide.internal.utils.StoragePathAdapter;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.resource.IResourceMatchingStrategy;
import org.eclipse.emf.compare.match.resource.LocationMatchingStrategy;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.match.DefaultRCPMatchEngineFactory;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Specialization of {@link DefaultRCPMatchEngineFactory} for EGit. When a comparison involves models shared
 * with EGit, the {@link org.eclipse.emf.compare.match.resource.StrategyResourceMatcher} must use the
 * {@link org.eclipse.emf.compare.match.resource.LocationMatchingStrategy} instead of the
 * {@link org.eclipse.emf.compare.match.resource.NameMatchingStrategy}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class EGitMatchEngineFactory extends DefaultRCPMatchEngineFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMatchEngineFactoryFor(IComparisonScope scope) {

		final Notifier right = scope.getRight();
		boolean activateEGitMatchEngine = isNotifierContainsSharedResource(right);

		if (!activateEGitMatchEngine) {
			final Notifier left = scope.getLeft();
			activateEGitMatchEngine = isNotifierContainsSharedResource(left);
			if (!activateEGitMatchEngine) {
				final Notifier origin = scope.getOrigin();
				activateEGitMatchEngine = isNotifierContainsSharedResource(origin);
			}
		}

		return activateEGitMatchEngine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMatchEngine getMatchEngine() {
		final UseIdentifiers useUdentifier = getUseIdentifierValue();
		final Collection<IResourceMatchingStrategy> strategies = Sets.newLinkedHashSet();
		strategies.add(new LocationMatchingStrategy());
		return DefaultMatchEngine.create(useUdentifier,
				EMFCompareRCPPlugin.getDefault().getWeightProviderRegistry(), strategies);
	}

	/**
	 * Check if the given {@link Notifier} contains at least on resource shared with git.
	 * 
	 * @param notifier
	 *            the given {@link Notifier}.
	 * @return true if the given {@link Notifier} contains at least on resource shared with git, false
	 *         otherwise.
	 */
	private boolean isNotifierContainsSharedResource(final Notifier notifier) {
		if (notifier instanceof ResourceSet) {
			for (Resource resource : ((ResourceSet)notifier).getResources()) {
				if (isResourceSharedWithGit(resource)) {
					return true;
				}
			}
		} else if (notifier instanceof Resource && isResourceSharedWithGit((Resource)notifier)) {
			return true;
		}

		return false;
	}

	/**
	 * Check if the given {@link Resource} is shared with git.
	 * 
	 * @param resource
	 *            the given {@link Resource}.
	 * @return true if the given {@link Resource} is shared with git, false otherwise.
	 */
	private boolean isResourceSharedWithGit(Resource resource) {
		Adapter leftAdapter = EcoreUtil.getAdapter(resource.eAdapters(), StoragePathAdapter.class);
		// If the resource is a storage path adapter and this storage path adapter contains a file URI, this
		// means that this is a resource from the Local History, not from a Git repository.
		if (leftAdapter instanceof StoragePathAdapter && !((StoragePathAdapter)leftAdapter).isLocal()
				&& !isFileURI(((StoragePathAdapter)leftAdapter).getStoragePath())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the given path is has a file scheme (file:).
	 * 
	 * @param path
	 *            the given path.
	 * @return true if the given path is has a file scheme (file:), false otherwise.
	 */
	private boolean isFileURI(String path) {
		final boolean isFileURI;
		if (path == null) {
			isFileURI = false;
		} else {
			isFileURI = path.startsWith("file:"); //$NON-NLS-1$
		}
		return isFileURI;
	}
}
