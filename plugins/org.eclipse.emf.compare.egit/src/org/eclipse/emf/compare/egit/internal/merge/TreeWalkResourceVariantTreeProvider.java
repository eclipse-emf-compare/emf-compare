/*******************************************************************************
 * Copyright (C) 2015, Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *     Laurent Goubet <laurent.goubet@obeo.fr> - initial API and implementation
 *     Axel Richard <axel.richard@obeo.fr> - Add GitPathToProjectPathConverter
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.merge;

//CHECKSTYLE:OFF
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.GitProvider;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.emf.compare.egit.internal.storage.TreeParserResourceVariant;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuildIterator;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.NameConflictTreeWalk;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.variants.IResourceVariantTree;

/**
 * This will populate its three {@link IResourceVariantTree} by walking over a tree walk and caching the
 * IResources it spans.
 * <p>
 * Files that are not located within the workspace will be ignored and thus will not be accessible through the
 * trees created by this provider.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("restriction")
public class TreeWalkResourceVariantTreeProvider implements GitResourceVariantTreeProvider {
	private final IResourceVariantTree baseTree;

	private final IResourceVariantTree oursTree;

	private final IResourceVariantTree theirsTree;

	private final Set<IResource> roots;

	private final Set<IResource> knownResources;

	private final LinkedHashMap<IPath, IProject> map;

	private final Repository repository;

	private TreeWalkResourceVariantTreeProvider(Builder builder) {
		this.repository = builder.repository;
		this.map = builder.map;
		this.baseTree = builder.baseTree;
		this.theirsTree = builder.theirsTree;
		this.oursTree = builder.oursTree;
		this.roots = builder.roots;
		this.knownResources = builder.knownResources;
	}

	public IResourceVariantTree getBaseTree() {
		return baseTree;
	}

	public IResourceVariantTree getRemoteTree() {
		return theirsTree;
	}

	public IResourceVariantTree getSourceTree() {
		return oursTree;
	}

	public Set<IResource> getKnownResources() {
		return knownResources;
	}

	public Set<IResource> getRoots() {
		return roots;
	}

	/**
	 * Returns a resource handle for this path in the workspace. Note that neither the resource nor the result
	 * need exist in the workspace : this may return inexistent or otherwise non-accessible IResources.
	 *
	 * @param repository
	 *            The repository within which is tracked this file.
	 * @param repoRelativePath
	 *            Repository-relative path of the file we need an handle for.
	 * @param isFolder
	 *            <code>true</code> if the file being sought is a folder.
	 * @return The resource handle for the given path in the workspace.
	 */
	public IResource getResourceHandleForLocation(Repository repository, String repoRelativePath,
			boolean isFolder) {
		return getResourceHandleForLocation(repository, repoRelativePath, isFolder, map);
	}

	public Repository getRepository() {
		return repository;
	}

	/**
	 * Returns a resource handle for this path in the workspace. Note that neither the resource nor the result
	 * need exist in the workspace : this may return inexistent or otherwise non-accessible IResources.
	 *
	 * @param repository
	 *            The repository within which is tracked this file.
	 * @param repoRelativePath
	 *            Repository-relative path of the file we need an handle for.
	 * @param isFolder
	 *            <code>true</code> if the file being sought is a folder.
	 * @param map
	 *            a to keeps tracks of IPath to IProject
	 * @return The resource handle for the given path in the workspace.
	 */
	private static IResource getResourceHandleForLocation(Repository repository, String repoRelativePath,
			boolean isFolder, LinkedHashMap<IPath, IProject> map) {
		IResource resource = null;

		final String workDir = repository.getWorkTree().getAbsolutePath();
		final IPath path = new Path(workDir + '/' + repoRelativePath);
		final File file = path.toFile();
		if (file.exists()) {
			if (isFolder) {
				resource = ResourceUtil.getContainerForLocation(path, false);
			} else {
				resource = ResourceUtil.getFileForLocation(path, false);
			}
		}

		if (repoRelativePath.endsWith(".project")) { //$NON-NLS-1$
			IPath parentPath = path.removeLastSegments(1);
			IProject p = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(parentPath.lastSegment().toString());
			if (map.get(parentPath) == null) {
				map.put(parentPath, p);
			}
		}

		if (resource == null) {
			// It may be a file that only exists on remote side. We need to
			// create an IResource for it.
			// If it is a project file, then create an IProject.
			final List<IPath> list = new ArrayList<>(map.keySet());
			for (int i = list.size() - 1; i >= 0; i--) {
				IPath projectPath = list.get(i);
				if (projectPath.isPrefixOf(path) && !projectPath.equals(path)) {
					final IPath projectRelativePath = path.makeRelativeTo(projectPath);
					if (isFolder) {
						resource = map.get(projectPath).getFolder(projectRelativePath);
					} else {
						resource = map.get(projectPath).getFile(projectRelativePath);
					}
					break;
				}
			}
		}

		if (resource == null) {
			// This is a file that no longer exists locally, yet we still need
			// to determine an IResource for it.
			// Try and find a Project in the workspace which path is a prefix of
			// the file we seek and which is mapped to the current repository.
			final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			for (IProject project : root.getProjects()) {
				if (RepositoryProvider.getProvider(project, GitProvider.ID) != null) {
					final IPath projectLocation = project.getLocation();
					if (projectLocation != null && projectLocation.isPrefixOf(path)) {
						final IPath projectRelativePath = path.makeRelativeTo(projectLocation);
						if (isFolder) {
							resource = project.getFolder(projectRelativePath);
						} else {
							resource = project.getFile(projectRelativePath);
						}
						break;
					}
				}
			}
		}

		return resource;
	}

	public static class Builder {

		private Repository repository;

		private AbstractTreeIterator aBaseTree;

		private RevTree headTree;

		private RevTree mergeTree;

		private DirCache dircache;

		private ObjectReader reader;

		private IResourceVariantTree baseTree;

		private IResourceVariantTree oursTree;

		private IResourceVariantTree theirsTree;

		private Set<IResource> roots;

		private Set<IResource> knownResources;

		private GitResourceVariantCache baseCache;

		private GitResourceVariantCache theirsCache;

		private GitResourceVariantCache oursCache;

		private LinkedHashMap<IPath, IProject> map;

		/**
		 * @param repository
		 *            The repository this tree walk has been created for.
		 * @return
		 */
		public Builder setRepository(Repository repository) {
			this.repository = repository;
			return this;
		}

		public Builder setaBaseTree(AbstractTreeIterator aBaseTree) {
			this.aBaseTree = aBaseTree;
			return this;
		}

		public Builder setHeadTree(RevTree headTree) {
			this.headTree = headTree;
			return this;
		}

		public Builder setMergeTree(RevTree mergeTree) {
			this.mergeTree = mergeTree;
			return this;
		}

		public Builder setDircache(DirCache dircache) {
			this.dircache = dircache;
			return this;
		}

		public Builder setReader(ObjectReader reader) {
			this.reader = reader;
			return this;
		}

		public TreeWalkResourceVariantTreeProvider build() throws IOException {

			DirCacheBuilder aBuilder = dircache.builder();
			DirCacheBuildIterator buildIt = new DirCacheBuildIterator(aBuilder);

			this.map = new LinkedHashMap<>();

			try (NameConflictTreeWalk treeWalk = new NameConflictTreeWalk(repository, reader)) {

				int baseIndex = treeWalk.addTree(aBaseTree);
				int ourIndex = treeWalk.addTree(headTree);
				int theirIndex = treeWalk.addTree(mergeTree);
				int dciPos = treeWalk.addTree(buildIt);
				FileTreeIterator aWorkingTreeIterator = new FileTreeIterator(repository);
				treeWalk.addTree(aWorkingTreeIterator);
				aWorkingTreeIterator.setDirCacheIterator(treeWalk, dciPos);

				this.baseCache = new GitResourceVariantCache();
				this.theirsCache = new GitResourceVariantCache();
				this.oursCache = new GitResourceVariantCache();

				while (treeWalk.next()) {
					final int modeBase = treeWalk.getRawMode(baseIndex);
					final int modeOurs = treeWalk.getRawMode(ourIndex);
					final int modeTheirs = treeWalk.getRawMode(theirIndex);
					if (!hasSignificantDifference(modeBase, modeOurs, modeTheirs)) {
						// conflict on file modes, leave the default merger handle it
						continue;
					}

					final CanonicalTreeParser base = treeWalk.getTree(baseIndex, CanonicalTreeParser.class);
					final CanonicalTreeParser ours = treeWalk.getTree(ourIndex, CanonicalTreeParser.class);
					final CanonicalTreeParser theirs = treeWalk.getTree(theirIndex,
							CanonicalTreeParser.class);

					final int nonZeroMode = modeBase != 0 ? modeBase : modeOurs != 0 ? modeOurs : modeTheirs;
					final IResource resource = getResourceHandleForLocation(repository,
							treeWalk.getPathString(), FileMode.fromBits(nonZeroMode) == FileMode.TREE, map);

					// Resource variants only make sense for IResources.
					if (resource != null) {
						IPath workspacePath = resource.getFullPath();
						if (modeBase != 0) {
							baseCache.setVariant(resource,
									TreeParserResourceVariant.create(repository, base, workspacePath));
						}
						if (modeOurs != 0) {
							oursCache.setVariant(resource,
									TreeParserResourceVariant.create(repository, ours, workspacePath));
						}
						if (modeTheirs != 0) {
							theirsCache.setVariant(resource,
									TreeParserResourceVariant.create(repository, theirs, workspacePath));
						}
					}

					if (treeWalk.isSubtree()) {
						treeWalk.enterSubtree();
					}
				}

				baseTree = new GitCachedResourceVariantTree(baseCache);
				theirsTree = new GitCachedResourceVariantTree(theirsCache);
				oursTree = new GitCachedResourceVariantTree(oursCache);

				roots = new LinkedHashSet<>();
				roots.addAll(baseCache.getRoots());
				roots.addAll(oursCache.getRoots());
				roots.addAll(theirsCache.getRoots());

				knownResources = new LinkedHashSet<>();
				knownResources.addAll(baseCache.getKnownResources());
				knownResources.addAll(oursCache.getKnownResources());
				knownResources.addAll(theirsCache.getKnownResources());
			} finally {
				// Needs reset since it might be used elsewhere
				aBaseTree.reset();
			}

			return new TreeWalkResourceVariantTreeProvider(this);
		}

		private boolean hasSignificantDifference(int modeBase, int modeOurs, int modeTheirs) {
			if (modeBase == 0) {
				if (FileMode.fromBits(modeOurs | modeTheirs) != FileMode.MISSING) {
					return true;
				} else {
					return (FileMode.fromBits(modeOurs) == FileMode.TREE
							&& FileMode.fromBits(modeTheirs) != FileMode.TREE)
							|| (FileMode.fromBits(modeOurs) != FileMode.TREE
									&& FileMode.fromBits(modeTheirs) == FileMode.TREE);
				}
			}
			return FileMode.fromBits(modeBase & modeOurs) != FileMode.MISSING
					|| FileMode.fromBits(modeBase & modeTheirs) != FileMode.MISSING;
		}

	}
}
// CHECKSTYLE:ON
