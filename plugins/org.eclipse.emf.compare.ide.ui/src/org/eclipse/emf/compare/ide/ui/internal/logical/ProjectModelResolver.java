/*******************************************************************************
 * Copyright (c) 2011, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil.adaptAs;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;

/**
 * This implementation of an {@link IModelResolver} will look up in the whole project contained by the
 * "starting points" of the models to resolve in order to check for referencing parents. Once this is done,
 * we'll know the whole logical model for the "local" resource. The right and origin (if any) resources will
 * be provided with the same traversal of resources, expanded with a "top-down" approach : load all models of
 * the traversal from the remote side, then resolve their containment tree to check whether there are new
 * remote resources in the logical model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ProjectModelResolver extends LogicalModelResolver {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.LogicalModelResolver#resolveLocalModels(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.resources.IResource, org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) {
		if (!(left instanceof IFile) || !(right instanceof IFile)) {
			return super.resolveLocalModels(left, right, origin, monitor);
		}

		final IProject project = left.getProject();
		final ModelResourceVisitor modelVisitor = new ModelResourceVisitor(monitor);
		try {
			project.accept(modelVisitor);
		} catch (CoreException e) {
			// TODO log
		}

		final Set<IStorage> leftTraversal = resolveTraversal((IFile)left, modelVisitor, monitor);
		final Set<IStorage> rightTraversal = resolveTraversal((IFile)right, modelVisitor, monitor);
		final Set<IStorage> originTraversal;
		if (origin instanceof IFile) {
			originTraversal = resolveTraversal((IFile)origin, modelVisitor, monitor);
		} else {
			originTraversal = Collections.emptySet();
		}

		return new SynchronizationModel(new StorageTraversal(leftTraversal), new StorageTraversal(
				rightTraversal), new StorageTraversal(originTraversal));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.LogicalModelResolver#resolveModels(org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor,
	 *      org.eclipse.core.resources.IStorage, org.eclipse.core.resources.IStorage,
	 *      org.eclipse.core.resources.IStorage, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor) {
		final IFile leftFile = adaptAs(left, IFile.class);
		if (leftFile == null) {
			return super.resolveModels(storageAccessor, leftFile, right, origin, monitor);
		}

		final IProject project = leftFile.getProject();
		final ModelResourceVisitor modelVisitor = new ModelResourceVisitor(storageAccessor, DiffSide.SOURCE,
				monitor);
		try {
			project.accept(modelVisitor);
		} catch (CoreException e) {
			// TODO log
		}

		final Set<IStorage> leftTraversal = resolveTraversal(leftFile, modelVisitor, monitor);
		final Set<IStorage> rightTraversal = resolveTraversal(storageAccessor, DiffSide.REMOTE,
				leftTraversal, monitor);
		final Set<IStorage> originTraversal;
		if (origin != null) {
			originTraversal = resolveTraversal(storageAccessor, DiffSide.ORIGIN, leftTraversal, monitor);
		} else {
			originTraversal = Collections.emptySet();
		}

		return new SynchronizationModel(new StorageTraversal(leftTraversal), new StorageTraversal(
				rightTraversal), new StorageTraversal(originTraversal));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.LogicalModelResolver#resolveLocalModel(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public StorageTraversal resolveLocalModel(IResource start, IProgressMonitor monitor) {
		if (!(start instanceof IFile)) {
			return super.resolveLocalModel(start, monitor);
		}

		final IProject project = start.getProject();
		final ModelResourceVisitor modelVisitor = new ModelResourceVisitor(monitor);
		try {
			project.accept(modelVisitor);
		} catch (CoreException e) {
			// TODO log
		}

		return new StorageTraversal(resolveTraversal((IFile)start, modelVisitor, monitor));
	}

	/**
	 * This will be used to resolve the traversal of a file's logical model, given the resource visitor that
	 * has been used to browse the scope for potential dependencies (by default, the scope is the resource's
	 * project).
	 * 
	 * @param resource
	 *            The resource for which we need the full logical model.
	 * @param visitor
	 *            A resource visitor that has already browsed the scope for potential dependencies.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return The set of all storages that compose the logical model of <code>resource</code>.
	 */
	private Set<IStorage> resolveTraversal(IFile resource, ModelResourceVisitor visitor,
			IProgressMonitor monitor) {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final Set<IStorage> traversal = new LinkedHashSet<IStorage>();
		final Iterable<URI> uris = visitor.getDependencyGraph(resource);
		for (URI uri : uris) {
			final StringBuilder path = new StringBuilder();
			List<String> segments = uri.segmentsList();
			if (uri.isPlatformResource()) {
				segments = segments.subList(1, segments.size());
			}
			for (String segment : segments) {
				path.append(segment).append('/');
			}
			traversal.add(root.getFile(new Path(path.toString())));
		}
		return traversal;
	}

	/**
	 * This will be used to resolve the logical model of a remote resource variant. Since we have no direct
	 * access to the resources themselves, we cannot simply browse one of their container for the traversal.
	 * Instead of that, we'll use the local traversal as a "reference", load remote variants of all of these
	 * local files, and re-resolve them in a "top-down" approach in case there are new resources on the remote
	 * side.
	 * 
	 * @param storageAccessor
	 *            The accessor that can be used to retrieve synchronization information between our resources.
	 * @param side
	 *            Side of the logical model to resolve. Used in conjunction with the storage accessor to
	 *            retrieve appropriate contents for the remote variants.
	 * @param localTraversal
	 *            Traversal resolved for the logical model of the local file.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return The set of all remote storages composing the same logical model as the given local traversal.
	 */
	private Set<IStorage> resolveTraversal(IStorageProviderAccessor storageAccessor, DiffSide side,
			Set<IStorage> localTraversal, IProgressMonitor monitor) {
		final DependencyResourceSet resourceSet = new DependencyResourceSet();
		final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				storageAccessor, side);
		resourceSet.setURIConverter(converter);

		final Set<IStorage> storages = Sets.newLinkedHashSet();
		for (IStorage local : localTraversal) {
			final IFile localFile = adaptAs(local, IFile.class);

			try {
				final IStorageProvider remoteStorageProvider = storageAccessor.getStorageProvider(localFile,
						side);
				final IStorage start = remoteStorageProvider.getStorage(monitor);

				if (resourceSet.resolveAll(start, monitor)) {
					if (!contains(storages, start)) {
						storages.add(start);
					}
					for (IStorage loaded : converter.getLoadedRevisions()) {
						if (!contains(storages, loaded)) {
							storages.add(loaded);
						}
					}
				} else {
					// failed to load a remote version of this resource
				}
			} catch (CoreException e) {
				// failed to load a remote version of this resource
			}
		}
		return storages;
	}

	/**
	 * This implementation of a resource visitor will allow us to browse all models in a given hierarchy.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
	 */
	private static class ModelResourceVisitor implements IResourceVisitor {
		/** Content types of the files to consider as potential parents. */
		private static final String[] MODEL_CONTENT_TYPES = new String[] {
				"org.eclipse.emf.compare.content.type", "org.eclipse.emf.ecore", //$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.emf.ecore.xmi", }; //$NON-NLS-1$

		/** Resource Set in which we should load the temporary resources. */
		private final DependencyResourceSet resourceSet;

		/** Monitor to report progress on. */
		// FIXME logarithmic progress
		private final IProgressMonitor monitor;

		/**
		 * Instantiates a resource visitor.
		 * 
		 * @param monitor
		 *            The monitor to report progress on.
		 */
		public ModelResourceVisitor(IProgressMonitor monitor) {
			this.resourceSet = new DependencyResourceSet();
			this.monitor = monitor;
			final StorageURIConverter converter = new StorageURIConverter(resourceSet.getURIConverter());
			this.resourceSet.setURIConverter(converter);
		}

		/**
		 * Instantiates a resource visitor given the storage accessor to use for I/O operations.
		 * 
		 * @param storageAccessor
		 *            The accessor to use for all I/O operations to fetch resource content.
		 * @param side
		 *            Side of the resources. Used in conjunction with the storage accessor to fetch proper
		 *            content.
		 * @param monitor
		 *            The monitor to report progress on.
		 */
		public ModelResourceVisitor(IStorageProviderAccessor storageAccessor, DiffSide side,
				IProgressMonitor monitor) {
			this.resourceSet = new DependencyResourceSet();
			this.monitor = monitor;
			final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
					storageAccessor, side);
			this.resourceSet.setURIConverter(converter);
		}

		/**
		 * Returns an iterable over all of the URIs that compose the given IFile's logical model.
		 * 
		 * @param resource
		 *            The file for which we need the resolved model.
		 * @return An iterable over all of the URIs that compose the given IFile's logical model.
		 */
		public Iterable<URI> getDependencyGraph(IFile resource) {
			final URI uri = ResourceUtil.createURIFor(resource);
			return resourceSet.getDependencyGraph(uri);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
		 */
		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IFile) {
				IFile file = (IFile)resource;
				boolean isModel = false;
				for (int i = 0; i < MODEL_CONTENT_TYPES.length && !isModel; i++) {
					isModel = hasContentType(file, MODEL_CONTENT_TYPES[i]);
				}

				if (isModel) {
					resourceSet.resolveAll(file, monitor);
					return true;
				}
				return false;
			}
			return true;
		}

		/**
		 * This will return <code>true</code> if and only if the given IFile has the given
		 * <em>contentTypeId</em> configured (as returned by
		 * {@link IContentTypeManager#findContentTypesFor(InputStream, String)
		 * Platform.getContentTypeManager().findContentTypesFor(InputStream, String)}.
		 * 
		 * @param resource
		 *            The resource from which to test the content types.
		 * @param contentTypeId
		 *            Fully qualified identifier of the content type this <em>resource</em> has to feature.
		 * @return <code>true</code> if the given {@link IFile} has the given content type.
		 */
		@SuppressWarnings("resource")
		private boolean hasContentType(IFile resource, String contentTypeId) {
			IContentTypeManager ctManager = Platform.getContentTypeManager();
			IContentType expected = ctManager.getContentType(contentTypeId);
			if (expected == null) {
				return false;
			}

			InputStream resourceContent = null;
			IContentType[] contentTypes = null;
			try {
				resourceContent = resource.getContents();
				contentTypes = ctManager.findContentTypesFor(resourceContent, resource.getName());
			} catch (CoreException e) {
				ctManager.findContentTypesFor(resource.getName());
			} catch (IOException e) {
				ctManager.findContentTypesFor(resource.getName());
			} finally {
				if (resourceContent != null) {
					try {
						resourceContent.close();
					} catch (IOException e) {
						// would have already been caught by the outer try, leave the stream open
					}
				}
			}

			boolean hasContentType = false;
			if (contentTypes != null) {
				for (int i = 0; i < contentTypes.length && !hasContentType; i++) {
					if (contentTypes[i].isKindOf(expected)) {
						hasContentType = true;
					}
				}
			}
			return hasContentType;
		}
	}
}
