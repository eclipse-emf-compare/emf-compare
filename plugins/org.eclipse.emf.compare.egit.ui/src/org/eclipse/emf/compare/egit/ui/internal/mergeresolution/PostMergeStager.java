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
package org.eclipse.emf.compare.egit.ui.internal.mergeresolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DefaultImplicitDependencies;
import org.eclipse.emf.compare.ide.ui.mergeresolution.IMergeResolutionListener;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Responsible for prompting the user about staging files after merge conflict resolution.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
@SuppressWarnings({"restriction" })
public class PostMergeStager implements IMergeResolutionListener {

	/**
	 * The default dependency resolver.
	 */
	private final DefaultImplicitDependencies dependencies = new DefaultImplicitDependencies();

	/**
	 * {@inheritDoc}
	 * 
	 * @see IMergeResolutionListener#mergeResolutionCompleted(Comparison)
	 */
	public void mergeResolutionCompleted(Comparison comparison) {
		EList<MatchResource> matchResources = comparison.getMatchedResources();
		final List<IResource> resources = new ArrayList<IResource>();
		for (MatchResource matchResource : matchResources) {
			Resource left = matchResource.getLeft();
			if (left != null) {
				URI matchUri = left.getURI();
				Set<URI> dependencyUris = dependencies.of(matchUri, left.getResourceSet().getURIConverter());

				for (URI uri : dependencyUris) {
					// FIXME: this feature will fail with resources that are not in the workspace (bug 478513)
					IResource resource = getResourceFromURI(uri);
					if (!resources.contains(resource)) {
						resources.add(resource);
					}
				}
			}
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				promptUser(resources.toArray(new IResource[resources.size()]));
			}
		});
	}

	/**
	 * Prompt the user about the specified resources.
	 * 
	 * @param resources
	 *            the resources to be staged
	 */
	private void promptUser(IResource[] resources) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		int result = new PostMergeDialog(shell, resources).open();
		if (IDialogConstants.OK_ID == result) {
			for (IResource resource : resources) {
				RepositoryMapping repoMapping = RepositoryMapping.getMapping(resource);
				@SuppressWarnings("resource")
				Repository repo = repoMapping.getRepository();
				Git git = new Git(repo);
				AddCommand gitAdd = git.add();
				String filepattern = repoMapping.getRepoRelativePath(resource);
				if ("".equals(filepattern)) { //$NON-NLS-1$
					filepattern = "."; //$NON-NLS-1$
				}
				gitAdd.addFilepattern(filepattern);
				try {
					gitAdd.call();
				} catch (NoFilepatternException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GitAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					git.close();
				}
			}
		}
	}

	/**
	 * Retrieves an IResource from URI.
	 * 
	 * @param uri
	 *            the URI to resolve
	 * @return the IResource representing the URI (it doesn't necessarily have to exist)
	 */
	// TODO: remove when change 51657 is accepted and replace by reference to ResourceUtil.getResourceFromURI
	private IResource getResourceFromURI(final URI uri) {
		final IResource targetFile;
		if (uri.isPlatform()) {
			IPath platformString = new Path(uri.trimFragment().toPlatformString(true));
			targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(platformString);
		} else {
			/*
			 * FIXME Deresolve the URI against the workspace root, if it cannot be done, delegate to
			 * super.createInputStream()
			 */
			targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
					new Path(uri.trimFragment().toString()));
		}
		return targetFile;
	}
}
