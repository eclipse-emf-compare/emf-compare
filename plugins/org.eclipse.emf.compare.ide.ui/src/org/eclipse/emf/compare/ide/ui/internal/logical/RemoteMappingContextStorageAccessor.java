/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 470268
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberResourceMappingContext;

/**
 * This will use a {@link RemoteResourceMappingContext} in order to fetch the content of the sides of a
 * comparison during model resolving.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class RemoteMappingContextStorageAccessor implements IStorageProviderAccessor {
	/** The underlying {@link RemoteResourceMappingContext}. */
	private final RemoteResourceMappingContext context;

	/** The detector for determining whether a resource has been renamed based on the subscriber's diffs. */
	private final RenameDetector renameDetector;

	/**
	 * Wraps the given mapping context within this accessor.
	 * 
	 * @param context
	 *            The wrapped context.
	 */
	public RemoteMappingContextStorageAccessor(RemoteResourceMappingContext context) {
		this.context = checkNotNull(context);
		renameDetector = new RenameDetector(obtainSubscriber(context), this);
	}

	/**
	 * Obtains the {@link Subscriber} from the given {@code remoteResourceMappingContext}.
	 * <p>
	 * As the subscriber is not accessible from {@link RemoteResourceMappingContext} implementations, we use
	 * reflection to steal the value. As this may go wrong, the returned subscriber can be <code>null</code>
	 * and implementations should not rely on its existence for major use cases.
	 * </p>
	 * 
	 * @param remoteResourceMappingContext
	 *            To obtain the subscriber from.
	 * @return The obtained subscriber, or <code>null</code> if it failed.
	 */
	private Subscriber obtainSubscriber(final RemoteResourceMappingContext remoteResourceMappingContext) {
		final Object subscriber = AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					final Field field = SubscriberResourceMappingContext.class.getDeclaredField("subscriber"); //$NON-NLS-1$
					field.setAccessible(true);
					final Object subscriberValue = field.get(remoteResourceMappingContext);
					return subscriberValue;
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {
					// ignore and live without subscriber
				}
				return null;
			}
		});
		if (subscriber instanceof Subscriber) {
			return (Subscriber)subscriber;
		}
		return null;
	}

	/** {@inheritDoc} */
	public IStorageProvider getStorageProvider(IResource resource, DiffSide side) throws CoreException {
		resource.getProjectRelativePath();
		if (resource instanceof IFile) {
			return new RemoteMappingStorageProvider(context, side, (IFile)resource);
		}
		return null;
	}

	/** {@inheritDoc} */
	public boolean isInSync(IResource resource) throws CoreException {
		final IProgressMonitor monitor = new NullProgressMonitor();
		return context.hasLocalChange(resource, monitor) || context.hasRemoteChange(resource, monitor);
	}

	/** {@inheritDoc} */
	public IFile getFileBeforeRename(IFile sourceOrRemoteFile, DiffSide side) {
		return renameDetector.getFileBeforeRename(sourceOrRemoteFile, side).orNull();
	}

	/** {@inheritDoc} */
	public IFile getFileAfterRename(IFile originFile, DiffSide side) {
		return renameDetector.getFileAfterRename(originFile, side).orNull();
	}
}
