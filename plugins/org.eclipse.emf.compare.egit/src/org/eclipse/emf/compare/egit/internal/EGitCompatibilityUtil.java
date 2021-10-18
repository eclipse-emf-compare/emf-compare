/*******************************************************************************
 * Copyright (C) 2021, Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffCache;
import org.eclipse.egit.core.internal.storage.WorkspaceFileRevision;
import org.eclipse.jgit.lib.Repository;
import org.osgi.framework.Version;

/**
 * This will act as a facade between EGit and EMF Compare so as to bypass the breaks that happen in EGit in
 * the restricted interfaces we need.
 * 
 * @author lgoubet
 */
@SuppressWarnings("restriction")
public class EGitCompatibilityUtil {

	/** Version 5.11.0. */
	public static final Version EGIT_5_11 = new Version(5, 11, 0);

	/** Version 5.12.0 */
	public static final Version EGIT_5_12 = new Version(5, 12, 0);

	/** Version 6.0.0 */
	public static final Version EGIT_6_0 = new Version(6, 0, 0);

	public static RepositoryUtil getRepositoryUtil() {
		if (getEGitVersion().compareTo(EGIT_6_0) >= 0) {
			return RepositoryUtil.INSTANCE;
		} else if (getEGitVersion().compareTo(EGIT_5_11) >= 0) {
			return (RepositoryUtil)invoke(RepositoryUtil.class, null, "getInstance"); //$NON-NLS-1$
		} else {
			Activator activator = Activator.getDefault();
			return (RepositoryUtil)invoke(Activator.class, activator, "getRepositoryUtil"); //$NON-NLS-1$
		}
	}

	public static IndexDiffCache getIndexDiffCache() {
		if (getEGitVersion().compareTo(EGIT_6_0) >= 0) {
			return IndexDiffCache.INSTANCE;
		} else if (getEGitVersion().compareTo(EGIT_5_11) >= 0) {
			return (IndexDiffCache)invoke(IndexDiffCache.class, null, "getInstance"); //$NON-NLS-1$
		} else {
			Activator activator = Activator.getDefault();
			return (IndexDiffCache)invoke(Activator.class, activator, "getIndexDiffCache"); //$NON-NLS-1$
		}
	}

	public static WorkspaceFileRevision createWorkspaceFileRevision(Repository repo, IResource local) {
		if (getEGitVersion().compareTo(EGIT_6_0) >= 0) {
			return WorkspaceFileRevision.forFile(repo, local);
		} else {
			try {
				Constructor<WorkspaceFileRevision> constructor = WorkspaceFileRevision.class
						.getConstructor(IResource.class);
				return constructor.newInstance(local);
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException
					| InstantiationException e) {
				// Will not happen as we're targeting specific methods we know of
			}
		}
		return null;
	}

	public static Version getEGitVersion() {
		return Platform.getBundle("org.eclipse.egit.ui").getVersion(); //$NON-NLS-1$
	}

	public static Object invoke(Class<?> targetClass, Object target, String methodName) {
		try {
			Method method = targetClass.getDeclaredMethod(methodName);
			return method.invoke(target);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			// Will not happen as we're targeting specific methods we know of
		}
		return null;
	}

}
