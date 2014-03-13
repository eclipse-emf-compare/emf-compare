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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.mapping.provider.ResourceDiff;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.core.variants.IResourceVariantTree;
import org.eclipse.team.core.variants.ResourceVariantTreeSubscriber;

/**
 * This is used by our synchronization model to access the innards of Team's {@link Subscriber}s.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class SubscriberStorageAccessor implements IStorageProviderAccessor {
	/** Our underlying subscriber. */
	private final Subscriber subscriber;

	/** The resource variant tree holding data for the common ancestor of the underlying subscriber. */
	private final IResourceVariantTree originTree;

	/** The resource variant tree holding data for the remote side of the underlying subscriber. */
	private final IResourceVariantTree remoteTree;

	/** The resource variant tree holding data for the source side of the underlying subscriber. */
	private final IResourceVariantTree sourceTree;

	/**
	 * Wraps the given subscriber within this accessor.
	 * 
	 * @param subscriber
	 *            The wrapped subscriber.
	 */
	public SubscriberStorageAccessor(Subscriber subscriber) {
		this.subscriber = subscriber;
		originTree = initTree(subscriber, "getBaseTree"); //$NON-NLS-1$
		remoteTree = initTree(subscriber, "getRemoteTree"); //$NON-NLS-1$
		sourceTree = initTree(subscriber, "getSourceTree"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor#getStorageProvider(org.eclipse.core.resources.IResource,
	 *      org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide)
	 */
	public IStorageProvider getStorageProvider(IResource resource, DiffSide side) throws CoreException {
		final IStorageProvider provider;
		switch (side) {
			case SOURCE:
				provider = getSourceVariant(resource);
				break;
			case REMOTE:
				provider = getRemoteVariant(resource);
				break;
			case ORIGIN:
				provider = getOriginVariant(resource);
				break;
			default:
				provider = null;
				break;
		}
		return provider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor#isInSync(org.eclipse.core.resources.IResource)
	 */
	public boolean isInSync(IResource resource) throws CoreException {
		return subscriber.getDiff(resource) == null;
	}

	/**
	 * Finds and returns the "origin" variant of the given IResource as provided by the underlying subscriber.
	 * 
	 * @param resource
	 *            The resource for which we need a variant.
	 * @return The "origin" variant of the given IResource.
	 * @throws CoreException
	 */
	private IStorageProvider getOriginVariant(IResource resource) throws CoreException {
		if (originTree != null) {
			return wrapStorageProvider(resource.getFullPath().toString(), originTree
					.getResourceVariant(resource));
		}

		final IDiff diff = subscriber.getDiff(resource);
		return wrapStorageProvider(getOrigin(diff));
	}

	/**
	 * Finds and returns the "source" variant of the given IResource as provided by the underlying subscriber.
	 * 
	 * @param resource
	 *            The resource for which we need a variant.
	 * @return The "source" variant of the given IResource.
	 * @throws CoreException
	 */
	private IStorageProvider getSourceVariant(IResource resource) throws CoreException {
		if (sourceTree != null) {
			return wrapStorageProvider(resource.getFullPath().toString(), sourceTree
					.getResourceVariant(resource));
		}

		final IDiff diff = subscriber.getDiff(resource);
		return wrapStorageProvider(getSource(diff));
	}

	/**
	 * Finds and returns the "remote" variant of the given IResource as provided by the underlying subscriber.
	 * 
	 * @param resource
	 *            The resource for which we need a variant.
	 * @return The "remote" variant of the given IResource.
	 * @throws CoreException
	 */
	private IStorageProvider getRemoteVariant(IResource resource) throws CoreException {
		if (remoteTree != null) {
			return wrapStorageProvider(resource.getFullPath().toString(), remoteTree
					.getResourceVariant(resource));
		}

		final IDiff diff = subscriber.getDiff(resource);
		return wrapStorageProvider(getRemote(diff));
	}

	/**
	 * Wraps the given file revision as an {@link IStorageProvider}.
	 * 
	 * @param revision
	 *            The wrapped revision.
	 * @return The wrapping storage provider.
	 */
	private static IStorageProvider wrapStorageProvider(IFileRevision revision) {
		if (revision != null) {
			return new FileRevisionStorageProvider(revision);
		}
		return null;
	}

	/**
	 * Wraps the given resource variant as an {@link IStorageProvider}.
	 * 
	 * @param path
	 *            Path of that storage.
	 * @param revision
	 *            The wrapped resource variant.
	 * @return The wrapping storage provider.
	 */
	private static IStorageProvider wrapStorageProvider(String path, IResourceVariant variant) {
		if (variant != null) {
			return new ResourceVariantStorageProvider(path, variant);
		}
		return null;
	}

	/**
	 * Try and locate the revision that was used as a common ancestor by the given diff.
	 * <p>
	 * Will always return <code>null</code> in the case of two-way comparisons.
	 * </p>
	 * 
	 * @param diff
	 *            Diff for which we're searching the common ancestor.
	 * @return the revision that was used as a common ancestor by the given diff.
	 * @throws CoreException
	 */
	private static IFileRevision getOrigin(IDiff diff) throws CoreException {
		IFileRevision revision = null;

		if (diff instanceof IThreeWayDiff) {
			final IDiff localChange = ((IThreeWayDiff)diff).getLocalChange();
			final IDiff remoteChange = ((IThreeWayDiff)diff).getRemoteChange();

			if (localChange instanceof ResourceDiff) {
				revision = ((ResourceDiff)localChange).getBeforeState();
			} else if (remoteChange instanceof ResourceDiff) {
				revision = ((ResourceDiff)remoteChange).getBeforeState();
			}
		}

		return revision;
	}

	/**
	 * Try and locate the revision that was used as a source by the given diff.
	 * <p>
	 * Note that this could be either a local or remote resource. "source" can also be referred as the "left"
	 * side of a comparison.
	 * </p>
	 * 
	 * @param diff
	 *            Diff for which we're searching the source side.
	 * @return the revision that was used as the source side by the given diff.
	 * @throws CoreException
	 */
	private static IFileRevision getSource(IDiff diff) throws CoreException {
		IFileRevision revision = null;

		if (diff instanceof IThreeWayDiff) {
			final IDiff localChange = ((IThreeWayDiff)diff).getLocalChange();

			if (localChange instanceof ResourceDiff) {
				revision = ((ResourceDiff)localChange).getAfterState();
			}
		} else if (diff instanceof ResourceDiff) {
			revision = ((ResourceDiff)diff).getAfterState();
		}

		return revision;
	}

	/**
	 * Try and locate the revision that was used as a remote by the given diff.
	 * <p>
	 * Note that despite its name, this could also be a local resource in case of local comparisons. "remote"
	 * can also be referred to as the "right" or "reference" side of a comparison.
	 * </p>
	 * 
	 * @param diff
	 *            Diff for which we're searching the remote side.
	 * @return the revision that was used as the remote side by the given diff.
	 * @throws CoreException
	 */
	private static IFileRevision getRemote(IDiff diff) throws CoreException {
		IFileRevision revision = null;

		if (diff instanceof IThreeWayDiff) {
			final IDiff remoteChange = ((IThreeWayDiff)diff).getRemoteChange();

			if (remoteChange instanceof ResourceDiff) {
				revision = ((ResourceDiff)remoteChange).getAfterState();
			}
		} else if (diff instanceof ResourceDiff) {
			revision = ((ResourceDiff)diff).getBeforeState();
		}

		return revision;
	}

	private static IResourceVariantTree initTree(final Subscriber teamSubscriber, final String methodName) {
		if (teamSubscriber instanceof ResourceVariantTreeSubscriber) {
			final Object tree = AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					try {
						final Method method;
						if (methodName.contains("Source")) { //$NON-NLS-1$
							// Only available on git, only from 3.0
							method = teamSubscriber.getClass().getDeclaredMethod(methodName);
						} else {
							method = ResourceVariantTreeSubscriber.class.getDeclaredMethod(methodName);
						}
						method.setAccessible(true);
						return method.invoke(teamSubscriber);
					} catch (SecurityException e) {
						// Swallow all this, don't use the variant tree.
					} catch (NoSuchMethodException e) {
						// Swallow all this, don't use the variant tree.
					} catch (IllegalArgumentException e) {
						// Swallow all this, don't use the variant tree.
					} catch (IllegalAccessException e) {
						// Swallow all this, don't use the variant tree.
					} catch (InvocationTargetException e) {
						// Swallow all this, don't use the variant tree.
					}
					return null;
				}
			});
			if (tree instanceof IResourceVariantTree) {
				return (IResourceVariantTree)tree;
			}
		}
		return null;
	}
}
