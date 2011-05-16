/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.synchronization;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.compare.EMFCompareException;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.logical.common.EMFResourceUtil;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.engine.GenericMatchScopeProvider;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.internal.VisualEngineSelector;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IDiffTree;
import org.eclipse.team.core.mapping.ISynchronizationContext;

/**
 * This class will serve as the root of our logical model deltas.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFModelDelta extends EMFDelta {
	/** Identifier of the model provider for which this delta has been created. */
	private String modelProviderId;

	/** Synchronization context of this delta. */
	private ISynchronizationContext context;

	/** Keeps track of the local resource set for which this instance holds deltas. */
	private ResourceSet localResourceSet;

	/** Keeps track of the remote resource set for which this instance holds deltas. */
	private ResourceSet remoteResourceSet;

	/** Keeps track of the ancestor resource set for which this instance holds deltas. */
	private ResourceSet ancestorResourceSet;

	/** Cached result of this comparison. */
	private ComparisonSnapshot comparisonSnapshot;

	/**
	 * Creates the root of our model delta.
	 * 
	 * @param context
	 *            Synchronization context of this delta.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this delta should be created.
	 */
	private EMFModelDelta(ISynchronizationContext context, String modelProviderId) {
		super(null);
		this.modelProviderId = modelProviderId;
		this.context = context;
	}

	/**
	 * Initializes a synchronization delta given the identifier of the model provider that required this delta
	 * and the current synchronization context.
	 * 
	 * @param context
	 *            Synchronization context of this delta.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this delta should be created.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @return The created Synchronization delta.
	 * @throws CoreException
	 *             Thrown when the comparison failed somehow.
	 */
	public static EMFModelDelta createDelta(ISynchronizationContext context, String modelProviderId,
			IProgressMonitor monitor) throws CoreException {
		EMFModelDelta delta = new EMFModelDelta(context, modelProviderId);

		delta.initialize(monitor);
		context.getCache().put(EMFSaveableBuffer.SYNCHRONIZATION_CACHE_KEY, delta);

		return delta;
	}

	/**
	 * This will return the cached delta within the given context.
	 * 
	 * @param context
	 *            Context from which to retrieve the cached delta.
	 * @return The cached delta.
	 */
	public static EMFModelDelta getDelta(ISynchronizationContext context) {
		return (EMFModelDelta)context.getCache().get(EMFSaveableBuffer.SYNCHRONIZATION_CACHE_KEY);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#clear()
	 */
	@Override
	public void clear() {
		/*
		 * FIXME we're nulling out the references, but the resource mapping isn't disposed ... where could we
		 * unload the resources?
		 */
		localResourceSet = null;
		remoteResourceSet = null;
		ancestorResourceSet = null;

		comparisonSnapshot = null;

		super.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getAncestor()
	 */
	@Override
	public Object getAncestor() {
		return ancestorResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getLocal()
	 */
	@Override
	public Object getLocal() {
		return localResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getRemote()
	 */
	@Override
	public Object getRemote() {
		return remoteResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getDiff()
	 */
	@Override
	public IDiff getDiff() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getPath()
	 */
	@Override
	public IPath getPath() {
		return Path.EMPTY;
	}

	/**
	 * Returns the underlying comparison snapshot.
	 * 
	 * @return The underlying comparison snapshot.
	 */
	public ComparisonSnapshot getComparisonSnapshot() {
		return comparisonSnapshot;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#createChildren()
	 */
	@Override
	protected void createChildren() {
		IDiffTree diffTree = context.getDiffTree();

		if (comparisonSnapshot instanceof ComparisonResourceSetSnapshot) {
			ComparisonResourceSetSnapshot resourceSetSnapshot = (ComparisonResourceSetSnapshot)comparisonSnapshot;

			for (DiffModel diffModel : resourceSetSnapshot.getDiffResourceSet().getDiffModels()) {
				// Are there any differences in this model?
				if (diffModel.getSubchanges() <= 0) {
					continue;
				}

				// Find all three resources
				Resource local = null;
				Resource remote = null;
				Resource ancestor = null;
				Iterator<EObject> leftRootsIterator = diffModel.getLeftRoots().iterator();
				while (local == null && leftRootsIterator.hasNext()) {
					EObject leftRoot = leftRootsIterator.next();
					local = leftRoot.eResource();
				}
				Iterator<EObject> rightRootsIterator = diffModel.getRightRoots().iterator();
				while (remote == null && rightRootsIterator.hasNext()) {
					EObject rightRoot = rightRootsIterator.next();
					remote = rightRoot.eResource();
				}
				Iterator<EObject> ancestorRootsIterator = diffModel.getAncestorRoots().iterator();
				while (ancestor == null && ancestorRootsIterator.hasNext()) {
					EObject ancestorRoot = ancestorRootsIterator.next();
					ancestor = ancestorRoot.eResource();
				}

				IResource localResource = EMFResourceUtil.findIResource(local);
				if (localResource != null) {
					IDiff diff = diffTree.getDiff(localResource.getFullPath());
					new EMFResourceDelta(this, diffModel, diff, local, remote, ancestor);
				}
			}
		} else {
			// We only allow for resource set comparisons
		}
	}

	/**
	 * This will be called internally to create or reset the comparison delta.
	 * 
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if we did not manage to load the remote resources.
	 */
	private void initialize(IProgressMonitor monitor) throws CoreException {
		clear();
		// FIXME monitor.subTask("comparing models")

		// Retrieve all three resource sets from the scope
		ResourceMapping[] mappings = EMFCompareAdapter.getAdditionalMappings(context);
		if (mappings == null) {
			mappings = context.getScope().getMappings();
		}

		for (ResourceMapping mapping : mappings) {
			if (modelProviderId.equals(mapping.getModelProviderId()) && mapping instanceof EMFResourceMapping) {
				EMFResourceMapping emfMapping = (EMFResourceMapping)mapping;
				ResourceSet local = emfMapping.getLocalResourceSet();
				ResourceSet remote = emfMapping.getRemoteResourceSet();
				ResourceSet ancestor = emfMapping.getAncestorResourceSet();
				// If any one of these is null, continue on to the next mapping
				if (local == null || remote == null || ancestor == null) {
					// Continue
				} else if (localResourceSet == null
						|| localResourceSet.getResources().size() < local.getResources().size()) {
					localResourceSet = local;
					remoteResourceSet = remote;
					ancestorResourceSet = ancestor;
				}
			}
		}

		/*
		 * If all three resource sets were null on all mappings, try to force their resolution (called when
		 * using right-click > compare with... actions from an EMF model editor).
		 */
		if (localResourceSet == null) {
			// Seek an EMFResourceMapping
			EMFResourceMapping emfMapping = null;
			for (int i = 0; i < mappings.length && emfMapping == null; i++) {
				ResourceMapping mapping = mappings[i];
				if (modelProviderId.equals(mapping.getModelProviderId())
						&& mapping instanceof EMFResourceMapping) {
					emfMapping = (EMFResourceMapping)mapping;
				}
			}

			if (emfMapping != null) {
				emfMapping.forceResolving(context.getScope().getContext(), monitor);

				localResourceSet = emfMapping.getLocalResourceSet();
				remoteResourceSet = emfMapping.getRemoteResourceSet();
				ancestorResourceSet = emfMapping.getAncestorResourceSet();
			}
		}

		// Ask EMF Compare to compute the DiffModel
		comparisonSnapshot = compare(localResourceSet, remoteResourceSet, ancestorResourceSet, monitor);

		// Then prepare the delta
		createChildren();
	}

	/**
	 * This will delegate to the EMF Compare core in order to compare the logical model contained in the given
	 * resource sets.
	 * 
	 * @param local
	 *            The resource set containing the local variant of the logical model.
	 * @param remote
	 *            The resource set containing the remote variant of the logical model.
	 * @param ancestor
	 *            The resource set containing the ancestor variant of the logical model.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @return The result of this comparison.
	 */
	private ComparisonSnapshot compare(ResourceSet local, ResourceSet remote, ResourceSet ancestor,
			IProgressMonitor monitor) {
		// Note that the ancestor can legitimately be null
		if (local == null || remote == null) {
			// FIXME throw something at user's face
		}

		MatchService.setMatchEngineSelector(new VisualEngineSelector());
		DiffService.setDiffEngineSelector(new VisualEngineSelector());

		ComparisonSnapshot comparisonResult = doResourceSetCompare(local, remote, ancestor, monitor);

		comparisonResult.setDate(Calendar.getInstance().getTime());

		return comparisonResult;
	}

	private ComparisonSnapshot doResourceSetCompare(ResourceSet local, ResourceSet remote,
			ResourceSet ancestor, IProgressMonitor monitor) {
		final ComparisonResourceSetSnapshot snapshot = DiffFactory.eINSTANCE
				.createComparisonResourceSetSnapshot();
		snapshot.setDiffResourceSet(DiffFactory.eINSTANCE.createDiffResourceSet());
		snapshot.setMatchResourceSet(MatchFactory.eINSTANCE.createMatchResourceSet());

		try {
			final Map<String, Object> options = new EMFCompareMap<String, Object>();
			options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);

			final MatchResourceSet match;
			if (ancestor == null) {
				options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new GenericMatchScopeProvider(local,
						remote));
				match = MatchService.doResourceSetMatch(local, remote, options);
			} else {
				options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new GenericMatchScopeProvider(local,
						remote, ancestor));
				match = MatchService.doResourceSetMatch(local, remote, ancestor, options);
			}

			final DiffResourceSet diff = DiffService.doDiff(match, ancestor != null);
			snapshot.setDiffResourceSet(diff);
			snapshot.setMatchResourceSet(match);
		} catch (final InterruptedException e) {
			EMFComparePlugin.log(e, false);
		} catch (final EMFCompareException e) {
			EMFComparePlugin.log(e, false);
		}

		return snapshot;
	}

}
