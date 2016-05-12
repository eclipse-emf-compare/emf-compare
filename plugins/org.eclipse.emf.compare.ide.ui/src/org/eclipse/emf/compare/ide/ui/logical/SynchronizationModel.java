/*******************************************************************************
 * Copyright (c) 2011, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - checkstyle and javadoc fixes
 *     Alexandra Buzila - bug 487119
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.resources.ResourceStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.utils.IDiagnosable;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class acts as a simple DTO that allows us to store the three traversals corresponding to the three
 * sides of a comparison while we build its scope.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
@SuppressWarnings("restriction")
@Beta
public final class SynchronizationModel implements IDiagnosable {
	/** The traversal corresponding to the left side. */
	private final StorageTraversal leftTraversal;

	/** The traversal corresponding to the right side. */
	private final StorageTraversal rightTraversal;

	/** The traversal corresponding to the common ancestor of both other side. */
	private final StorageTraversal originTraversal;

	/** The diagnostic that may have been issued for this synchronization model. */
	private Diagnostic diagnostic;

	/** The resources that are part of this synchronization model. */
	private ImmutableSet<IResource> resources;

	/**
	 * All resources that are involved in this synchronization model before any minimization that may have
	 * been applied.
	 */
	private final ImmutableSet<IResource> allInvolvedResources;

	/**
	 * Provider for the composed diagnostic that aggregates the diagnostics of the logical model resolution
	 * and resource traversals.
	 */
	private SynchronizationModelDiagnosticProvider diagnosticProvider;

	/**
	 * Constructs our logical model given the three traversal for our sides.
	 * 
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal corresponding to the common ancestor of both other side. Can be
	 *            <code>null</code>.
	 */
	public SynchronizationModel(StorageTraversal leftTraversal, StorageTraversal rightTraversal,
			StorageTraversal originTraversal) {
		this(leftTraversal, rightTraversal, originTraversal, new BasicDiagnostic(
				EMFCompareIDEUIPlugin.PLUGIN_ID, 0, null, new Object[] {leftTraversal, rightTraversal,
						originTraversal, }));
	}

	/**
	 * Constructs our logical model given the three traversal for our sides.
	 * 
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal corresponding to the common ancestor of both other side. Can be
	 *            <code>null</code>.
	 * @param diagnostic
	 *            The diagnostic that have gathered during the computation of the traversals.
	 */
	public SynchronizationModel(StorageTraversal leftTraversal, StorageTraversal rightTraversal,
			StorageTraversal originTraversal, Diagnostic diagnostic) {
		this.diagnostic = Preconditions.checkNotNull(diagnostic);
		if (leftTraversal == null) {
			this.leftTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.leftTraversal = leftTraversal;
		}

		if (rightTraversal == null) {
			this.rightTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.rightTraversal = rightTraversal;
		}

		if (originTraversal == null) {
			this.originTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.originTraversal = originTraversal;
		}
		diagnosticProvider = new SynchronizationModelDiagnosticProvider(this);
		allInvolvedResources = computeResources();
	}

	/**
	 * Returns the left traversal of this model.
	 * 
	 * @return The left traversal of this model.
	 */
	public StorageTraversal getLeftTraversal() {
		return leftTraversal;
	}

	/**
	 * Returns the right traversal of this model.
	 * 
	 * @return The right traversal of this model.
	 */
	public StorageTraversal getRightTraversal() {
		return rightTraversal;
	}

	/**
	 * Returns the origin traversal of this model, if any.
	 * 
	 * @return The origin traversal of this model, <code>null</code> if none.
	 */
	public StorageTraversal getOriginTraversal() {
		return originTraversal;
	}

	/**
	 * Returns the diagnostics that may have been issued for the synchronization model, as well as for the
	 * left, right, and origin side.
	 * 
	 * @return The diagnostics of the synchronization model, left, right, and origin side.
	 */
	public Diagnostic getDiagnostic() {
		return diagnosticProvider.getDiagnostic();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.utils.IDiagnosable#setDiagnostic(org.eclipse.emf.common.util.Diagnostic)
	 */
	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SynchronizationModel) {
			final SynchronizationModel other = (SynchronizationModel)obj;
			return Objects.equal(leftTraversal, other.leftTraversal)
					&& Objects.equal(rightTraversal, other.rightTraversal)
					&& Objects.equal(originTraversal, other.originTraversal);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[] {leftTraversal, rightTraversal, originTraversal, });
	}

	/**
	 * Returns the set of resources this synchronization model spans.
	 * <p>
	 * The returned set may contain resources that do not exist locally. The set of resources is cached. If no
	 * cached set is available, this method will compute and cache it. Note that the cache may not be in sync,
	 * if the traversals of this synchronization model have been changed after {@link #getResources()} has
	 * been called.
	 * </p>
	 * 
	 * @return The set of resources this synchronization model spans.
	 * @since 4.1
	 */
	public Set<IResource> getResources() {
		if (resources == null) {
			resources = computeResources();
		}
		return resources;
	}

	/**
	 * Returns the all resources that are involved in this synchronization model.
	 * <p>
	 * This is the set of resources involved directly after the instantiation of this synchronization model
	 * and hence the set of resources before any minimization that may have been applied.
	 * </p>
	 * 
	 * @return The initial set of all resources this synchronization model spans.
	 * @since 4.3
	 */
	public Set<IResource> getAllInvolvedResources() {
		return allInvolvedResources;
	}

	/**
	 * Computes and returns the resources this synchronization model spans. The returned set may contain
	 * resources that do not exist locally.
	 * 
	 * @return The set of resources this synchronization model spans.
	 */
	private ImmutableSet<IResource> computeResources() {
		final Set<IResource> leftResources = collectResources(getLeftTraversal());
		final Set<IResource> rightResources = collectResources(getRightTraversal());
		final Set<IResource> originResources = collectResources(getOriginTraversal());
		return ImmutableSet.<IResource> builder().addAll(leftResources).addAll(rightResources).addAll(
				originResources).build();
	}

	/**
	 * Collect the set of IResources the given storage traversal spans.
	 * 
	 * @param traversal
	 *            The traversal from which to collect IResources. Might be <code>null</code>, in which case an
	 *            empty set will be returned.
	 * @return The converted traversal.
	 */
	private static Set<IResource> collectResources(StorageTraversal traversal) {
		final Set<IResource> resources = new LinkedHashSet<IResource>();
		if (traversal == null) {
			return resources;
		}

		for (IStorage storage : traversal.getStorages()) {
			if (storage instanceof IFile) {
				resources.add((IFile)storage);
			} else {
				/*
				 * Use a file handle. Since files can be both local and remote, they might not even exist in
				 * the current workspace. It will be the responsibility of the user to either get the remote
				 * or local content. The traversal itself only tells "all" potential resources linked to the
				 * current.
				 */
				resources.add(ResourcesPlugin.getWorkspace().getRoot().getFile(
						ResourceUtil.getFixedPath(storage)));
			}
		}
		return resources;
	}

	/**
	 * Provides a BasicDiagnostic for the synchronization model that aggregates the diagnostics from the model
	 * resolution and the diagnostics from the left, origin and right resource traversals.
	 */
	private static class SynchronizationModelDiagnosticProvider {
		private BasicDiagnostic syncModelDiagnostic;

		private HashSet<IPath> resourcePathCache;

		private SynchronizationModel syncModel;

		SynchronizationModelDiagnosticProvider(SynchronizationModel syncModel) {
			this.syncModel = syncModel;
		}

		public Diagnostic getDiagnostic() {
			if (syncModelDiagnostic == null) {
				buildDiagnostic();
			}
			return syncModelDiagnostic;
		}

		private void buildDiagnostic() {
			syncModelDiagnostic = new BasicDiagnostic(
					EMFCompareIDEUIPlugin.PLUGIN_ID,
					0,
					EMFCompareIDEUIMessages.getString("SynchronizationModel.diagnosticMesg"), new Object[] {syncModel, }); //$NON-NLS-1$
			// synchronization model child diagnostics
			syncModelDiagnostic.add(getSynchronizationModelDiagnostic());
			// resource traversals child diagnostics
			syncModelDiagnostic
					.add(getDiagnosticForSide(syncModel.getLeftTraversal().getDiagnostic(), "left")); //$NON-NLS-1$
			syncModelDiagnostic.add(getDiagnosticForSide(syncModel.getOriginTraversal().getDiagnostic(),
					"origin")); //$NON-NLS-1$
			syncModelDiagnostic.add(getDiagnosticForSide(syncModel.getRightTraversal().getDiagnostic(),
					"right")); //$NON-NLS-1$
		}

		/**
		 * Filters out from the existing diagnostic all child diagnostics that don't belong to resources that
		 * are part of the logical model.
		 */
		private BasicDiagnostic getSynchronizationModelDiagnostic() {
			BasicDiagnostic d = new BasicDiagnostic(syncModel.diagnostic.getSource(), syncModel.diagnostic
					.getCode(), null, EMFCompareIDEUIMessages.getString("SynchronizationModel.root"), null); //$NON-NLS-1$

			for (Diagnostic child : syncModel.diagnostic.getChildren()) {
				List<?> diagnosticData = child.getData();
				if (diagnosticData.isEmpty()) {
					continue;
				}
				// source of problem
				Object object = diagnosticData.get(0);

				if (object instanceof ResourceStatus) {
					ResourceStatus status = (ResourceStatus)object;
					final IPath resourceIPath = status.getPath();
					if (containsResourceWithPath(resourceIPath)) {
						d.merge(child);
					}
				} else if (object instanceof Resource.Diagnostic) {
					Resource.Diagnostic resourceDiagnostic = (Resource.Diagnostic)object;
					String location = resourceDiagnostic.getLocation();
					URI locationUri = URI.createURI(location, false);
					String fullPath = null;
					if (locationUri.isPlatform()) {
						fullPath = locationUri.toPlatformString(true);
					} else {
						fullPath = locationUri.toString();
					}
					if (fullPath == null) {
						continue;
					}
					final Path path = new Path(fullPath);
					if (containsResourceWithPath(path)) {
						d.merge(child);
					}
				} else {
					// best guess - if the source of the problem is one of the resources, we are interested in
					// the diagnostic
					if (syncModel.getResources().contains(object)) {
						d.merge(child);
					}
				}
			}
			return d;
		}

		/**
		 * Creates a diagnostic for the given diagnostic {@code toAdd} of the given {@code side}.
		 * 
		 * @param toAdd
		 *            The diagnostic to be added to the created diagnostic.
		 * @param side
		 *            The side, either left, right, or origin.
		 * @return The created diagnostic.
		 */
		private BasicDiagnostic getDiagnosticForSide(Diagnostic toAdd, String side) {
			BasicDiagnostic d = new BasicDiagnostic(toAdd.getSeverity(), toAdd.getSource(), 0,
					EMFCompareIDEUIMessages.getString("SynchronizationModel." + side), null); //$NON-NLS-1$
			if (!toAdd.getChildren().isEmpty()) {
				d.merge(toAdd);
			}
			return d;
		}

		/**
		 * Returns <code>true</code> if the given resource {@link IPath} belongs to a resource that is part of
		 * the synchronization model.
		 */
		private boolean containsResourceWithPath(final IPath resourcePath) {
			if (resourcePathCache == null) {
				buildResourcePathCache();
			}
			return resourcePathCache.contains(resourcePath);
		}

		/** Caches the {@link IPath paths} of the synchronization model's resources. */
		private void buildResourcePathCache() {
			resourcePathCache = new HashSet<IPath>();
			for (IResource resource : syncModel.getResources()) {
				if (resource.getFullPath() != null) {
					resourcePathCache.add(resource.getFullPath());
				}
			}
		}
	}
}
