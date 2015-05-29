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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.collect.Sets.intersection;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.asURI;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * Called by EMF Compare in order to resolve the logical models corresponding to the given IResources. Only
 * local data is available.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class LocalModelsResolution extends AbstractResolution {
	/** The local resolver. */
	private final IResourceDependencyLocalResolver resolver;

	/** The left resource, never null. */
	private final IResource left;

	/** The right resource, never null. */
	private final IResource right;

	/** The origin resource, may be null. */
	private final IResource origin;

	/**
	 * Constructor.
	 * 
	 * @param dependencyProvider
	 *            The dependency provider
	 * @param scheduler
	 *            multi-thread support
	 * @param eventBus
	 *            The event bus to signal events
	 * @param left
	 *            left resource
	 * @param right
	 *            right resource
	 * @param origin
	 *            common ancestor resource, may be null
	 * @param monitor
	 *            The progress monitor to use
	 */
	public LocalModelsResolution(IResolutionContext context, IResource left, IResource right,
			IResource origin, IProgressMonitor monitor) {
		super(context, monitor);
		this.left = Preconditions.checkNotNull(left);
		this.right = Preconditions.checkNotNull(right);
		this.origin = origin;
		this.resolver = context.getLocalResolver();
	}

	/**
	 * Executes this treatment.
	 * 
	 * @return The Logical model to use to compare the 2 or 3 resources.
	 */
	public SynchronizationModel run() {
		if (logger.isDebugEnabled()) {
			logger.debug("run() - START"); //$NON-NLS-1$
		}
		try {
			if (allResourcesAreFiles()) {
				return resolveLocalFiles();
			} else {
				return resolveLocalResources();
			}
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("run() - FINISH"); //$NON-NLS-1$
			}
			monitor.setWorkRemaining(0);
		}
	}

	/**
	 * Indicates whether all the resources are instances of {@link IFile}.
	 * 
	 * @return <code>true</code> if and only if left and right are {@link IFile}s and origin is null or is an
	 *         {@link IFile}.
	 */
	protected boolean allResourcesAreFiles() {
		return left instanceof IFile && right instanceof IFile && (origin == null || origin instanceof IFile);
	}

	/**
	 * Resolve the local resources.
	 * 
	 * @return The synchronization model that contains the logical models of each side.
	 */
	private SynchronizationModel resolveLocalResources() {
		// Sub-optimal implementation, we'll only try and resolve each side individually
		final StorageTraversal leftTraversal;
		final StorageTraversal rightTraversal;
		final StorageTraversal originTraversal;
		if (logger.isDebugEnabled()) {
			logger.debug("resolveLocalResources()"); //$NON-NLS-1$
		}
		// CHECKSTYLE:OFF No, I won't create constants.
		if (origin != null) {
			leftTraversal = resolveLocalModel(left, monitor.newChild(33));
			rightTraversal = resolveLocalModel(right, monitor.newChild(33));
			originTraversal = resolveLocalModel(origin, monitor.newChild(34));
		} else {
			leftTraversal = resolveLocalModel(left, monitor.newChild(50));
			rightTraversal = resolveLocalModel(right, monitor.newChild(50));
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}
		// CHECKSTYLE:ON

		return new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	/**
	 * Resolve one local model.
	 * 
	 * @param start
	 *            The resource to start from.
	 * @param subMonitor
	 *            The progress monitor to use
	 * @return The traversal for the given resource.
	 */
	private StorageTraversal resolveLocalModel(final IResource start, SubMonitor subMonitor) {
		LocalModelResolution comp = new LocalModelResolution(context, subMonitor);
		return comp.run(start);
	}

	/**
	 * Resolve several local files.
	 * 
	 * @return The synchronization model that contains the logical models of each side.
	 */
	private SynchronizationModel resolveLocalFiles() {
		if (logger.isDebugEnabled()) {
			logger.debug("resolveLocalFiles()"); //$NON-NLS-1$
		}
		return call(new Callable<SynchronizationModel>() {
			public SynchronizationModel call() throws Exception {
				if (logger.isDebugEnabled()) {
					logger.debug("Updating dependencies"); //$NON-NLS-1$
				}
				if (origin instanceof IFile) {
					resolver.updateDependencies(monitor, diagnostic, (IFile)left, (IFile)right, (IFile)origin);
				} else {
					resolver.updateDependencies(monitor, diagnostic, (IFile)left, (IFile)right);
				}

				final URI leftURI = createURIFor((IFile)left);
				final URI rightURI = createURIFor((IFile)right);
				final URI originURI;
				final Set<IFile> startingPoints;
				if (origin instanceof IFile) {
					startingPoints = ImmutableSet.of((IFile)left, (IFile)right, (IFile)origin);
					originURI = createURIFor((IFile)origin);
				} else {
					startingPoints = ImmutableSet.of((IFile)left, (IFile)right);
					originURI = null;
				}

				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Computing traversals"); //$NON-NLS-1$
				}
				final Set<IStorage> leftTraversal;
				final Set<IStorage> rightTraversal;
				final Set<IStorage> originTraversal;
				if (origin instanceof IFile) {
					leftTraversal = resolveTraversal((IFile)left, ImmutableSet.of(rightURI, originURI));
					rightTraversal = resolveTraversal((IFile)right, ImmutableSet.of(leftURI, originURI));
					originTraversal = resolveTraversal((IFile)origin, ImmutableSet.of(leftURI, rightURI));
				} else {
					leftTraversal = resolveTraversal((IFile)left, Collections.singleton(rightURI));
					rightTraversal = resolveTraversal((IFile)right, Collections.singleton(leftURI));
					originTraversal = Collections.emptySet();
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Computing synchronization model"); //$NON-NLS-1$
				}
				// If one resource of the logical model was pointing to both (or "all three") of our
				// starting elements, we'll have way too many things in our traversal. We need to remove the
				// intersection before going any further.
				Set<IStorage> intersection = intersection(leftTraversal, rightTraversal);
				if (!originTraversal.isEmpty()) {
					intersection = intersection(intersection, originTraversal);
				}
				logCoherenceThreats(Iterables.transform(startingPoints, asURI()), Iterables.transform(
						intersection, asURI()));

				final Set<IStorage> actualLeft = new LinkedHashSet<IStorage>(Sets.difference(leftTraversal,
						intersection));
				final Set<IStorage> actualRight = new LinkedHashSet<IStorage>(Sets.difference(rightTraversal,
						intersection));
				final Set<IStorage> actualOrigin = new LinkedHashSet<IStorage>(Sets.difference(
						originTraversal, intersection));
				final SynchronizationModel synchronizationModel = new SynchronizationModel(
						new StorageTraversal(actualLeft), new StorageTraversal(actualRight),
						new StorageTraversal(actualOrigin), diagnostic.getDiagnostic());

				return synchronizationModel;
			}
		});
	}

	/**
	 * When executing local comparisons, we resolve the full logical model of both (or "all three of") the
	 * compared files.
	 * <p>
	 * If there is one resource in the scope that references all of these starting points, then we'll have
	 * perfectly identical logical models for all comparison sides. Because of that, we need to constrain the
	 * logical model of each starting point to only parts that are not accessible from other starting points.
	 * This might cause coherence issues as merging could thus "break" references from other files to our
	 * compared ones.
	 * </p>
	 * <p>
	 * This method will be used to browse the files that are removed from the logical model, and log a warning
	 * for the files that are removed even though they are "parents" of one of the starting points.
	 * </p>
	 * 
	 * @param startingPoints
	 *            Starting points of the comparison.
	 * @param removedFromModel
	 *            All files that have been removed from the comparison scope.
	 */
	private void logCoherenceThreats(Iterable<URI> startingPoints, Iterable<URI> removedFromModel) {
		final Set<URI> coherenceThreats = new LinkedHashSet<URI>();
		for (URI start : startingPoints) {
			for (URI removed : removedFromModel) {
				if (context.getDependencyProvider().hasChild(removed, start)) {
					coherenceThreats.add(removed);
				}
			}
		}

		if (!coherenceThreats.isEmpty()) {
			// FIXME: should be added to diagnostic instead
			final String message = EMFCompareIDEUIMessages.getString("ModelResolver.coherenceWarning"); //$NON-NLS-1$
			final String details = Iterables.toString(coherenceThreats);
			EMFCompareIDEUIPlugin.getDefault().getLog().log(
					new Status(IStatus.WARNING, EMFCompareIDEUIPlugin.PLUGIN_ID, message + '\n' + details));
		}
	}
}
