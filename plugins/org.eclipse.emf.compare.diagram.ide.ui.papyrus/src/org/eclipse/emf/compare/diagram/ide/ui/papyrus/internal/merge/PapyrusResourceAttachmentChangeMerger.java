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
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.merge;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.CompareDiagramIDEUIPapyrusPlugin;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.CompareUIPapyrusMessages;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.PapyrusPostProcessor;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.merge.ResourceAttachmentChangeMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This Merger deals with ResourceAttachmentChanges as soon as there is Papyrus involved. Its purpose it to
 * make sure additions, deletions, and renames of papyrus resources are made synchronously (*.notation, *.uml
 * and *.di need to be synchronously created/deleted).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class PapyrusResourceAttachmentChangeMerger extends ResourceAttachmentChangeMerger {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(PapyrusResourceAttachmentChangeMerger.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public boolean isMergerFor(Diff target) {
		try {
			return target instanceof ResourceAttachmentChange
					&& isPapyrusChange((ResourceAttachmentChange)target);
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// The change resource has no URI or other strange issue
		}
		// CHECKSTYLE:ON
		return false;
	}

	/**
	 * Manages move of empty resources related to the model element move from one resource to another. Then
	 * delegates to the super implementation to actually perform the move.
	 */
	@Override
	protected void move(final ResourceAttachmentChange diff, boolean rightToLeft) {
		dealWithAssociatedEmptyResources(diff, rightToLeft);
		super.move(diff, rightToLeft);
	}

	@Override
	protected void addInTarget(ResourceAttachmentChange diff, boolean rightToLeft) {
		dealWithAssociatedEmptyResources(diff, rightToLeft);
		super.addInTarget(diff, rightToLeft);
	}

	@Override
	protected void removeFromTarget(ResourceAttachmentChange diff, boolean rightToLeft) {
		dealWithAssociatedEmptyResources(diff, rightToLeft);
		super.removeFromTarget(diff, rightToLeft);
	}

	/**
	 * Manages the creation and deletion of associated empty resources that cannot be handled by model diffs
	 * since they contain no model element to which attach any diff.
	 * 
	 * @param diff
	 *            The change to apply
	 * @param rightToLeft
	 *            The direction in which the change must be applied
	 */
	private void dealWithAssociatedEmptyResources(final ResourceAttachmentChange diff, boolean rightToLeft) {
		Comparison comp = ComparisonUtil.getComparison(diff);
		Iterable<MatchResource> relatedMatchResource = Iterables.filter(comp.getMatchedResources(),
				concernsTheSamePapyrusVirtualNodeAs(diff));
		final ResourceSet targetRS;
		if (rightToLeft) {
			targetRS = getResourceSet(comp, DifferenceSource.LEFT);
		} else {
			targetRS = getResourceSet(comp, DifferenceSource.RIGHT);
		}
		for (MatchResource mr : relatedMatchResource) {
			if (rightToLeft) {
				if (mr.getLeft() != null && mr.getLeft().getContents().isEmpty()) {
					// Delete only if former file doesn't exist on the other side
					if (mr.getRight() == null) {
						delete(targetRS, mr.getLeft().getURI());
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info("Deleted empty resource " + mr.getLeft().getURI()); //$NON-NLS-1$
						}
					}
				} else if (mr.getRight() != null && mr.getRight().getContents().isEmpty()) {
					URI targetURI = mr.getRight().getURI();
					if (!targetRS.getURIConverter().exists(targetURI, Collections.emptyMap())) {
						targetRS.createResource(targetURI);
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info("Created empty resource " + targetURI); //$NON-NLS-1$
						}
					}
				}
			} else {
				if (mr.getRight() != null && mr.getRight().getContents().isEmpty()) {
					// Delete only if former file doesn't exist on the other side
					if (mr.getLeft() == null) {
						delete(targetRS, mr.getRight().getURI());
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info("Deleted empty resource " + mr.getRight().getURI()); //$NON-NLS-1$
						}
					}
				} else if (mr.getLeft() != null && mr.getLeft().getContents().isEmpty()) {
					URI targetURI = mr.getLeft().getURI();
					if (!targetRS.getURIConverter().exists(targetURI, Collections.emptyMap())) {
						targetRS.createResource(targetURI);
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info("Created empty resource " + targetURI); //$NON-NLS-1$
						}
					}
				}
			}
		}
	}

	/**
	 * Obtain the resource set to use for a side.
	 * 
	 * @param comp
	 *            Comparison
	 * @param side
	 *            side
	 * @return The resource set to use, never null.
	 */
	private ResourceSet getResourceSet(Comparison comp, DifferenceSource side) {
		// CHECKSTYLE:OFF
		switch (side) {
			case LEFT:
				for (MatchResource mr : comp.getMatchedResources()) {
					if (mr.getLeft() != null && mr.getLeft().getResourceSet() != null) {
						return mr.getLeft().getResourceSet();
					}
				}
			case RIGHT:
				for (MatchResource mr : comp.getMatchedResources()) {
					if (mr.getRight() != null && mr.getRight().getResourceSet() != null) {
						return mr.getRight().getResourceSet();
					}
				}
		}
		// CHECKSTYLE:ON
		throw new IllegalStateException();
	}

	/**
	 * Delete a resource.
	 * 
	 * @param rs
	 *            Resource set from where to delete the resource
	 * @param uri
	 *            URI of the resource to delete
	 */
	private void delete(ResourceSet rs, URI uri) {
		Resource toDelete = rs.getResource(uri, false);
		if (toDelete == null) {
			return;
		}
		try {
			toDelete.delete(Collections.EMPTY_MAP);
		} catch (IOException e) {
			CompareDiagramIDEUIPapyrusPlugin.getDefault().getLog().log(
					new Status(IStatus.ERROR, CompareDiagramIDEUIPapyrusPlugin.PLUGIN_ID,
							CompareUIPapyrusMessages
									.getString("PapyrusResourceAttachmentChangeMerge.deleteFailure"), e)); //$NON-NLS-1$
		}
	}

	/**
	 * Indicates whether the given change concerns a papyrus resource.
	 * 
	 * @param change
	 *            The change to test
	 * @return <code>true</code> if the change concerns a papyrus resource.
	 */
	private boolean isPapyrusChange(ResourceAttachmentChange change) {
		Match match = change.getMatch();
		EObject o = match.getLeft();
		if (o == null) {
			o = match.getRight();
			if (o == null) {
				o = match.getOrigin();
			}
		}
		return PapyrusPostProcessor.FILE_EXTENSIONS.contains(o.eResource().getURI().fileExtension());
	}

	/**
	 * Provide a predicate to filter MatchResources.
	 * 
	 * @param change
	 *            The reference change
	 * @return A predicate that will filter the MatchResources that will concerne resources that are part of
	 *         the same virtual node as the given change's resource(s).
	 */
	private SameVirtualNode concernsTheSamePapyrusVirtualNodeAs(ResourceAttachmentChange change) {
		return new SameVirtualNode(change);
	}

	/**
	 * Predicate that matches all {@link MatchResource}s that are related to a papyrus related resource of its
	 * reference change (the change that is passed to the constructor).
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class SameVirtualNode implements Predicate<MatchResource> {
		/** The trimmed left URI, can be null. */
		private final URI leftURI;

		/** The trimmed right URI, can be null. */
		private final URI rightURI;

		/** The trimmed ancestor URI, can be null. */
		private final URI originURI;

		/**
		 * Constructor.
		 * 
		 * @param change
		 *            The change
		 */
		private SameVirtualNode(ResourceAttachmentChange change) {
			Match match = change.getMatch();
			leftURI = getResourceURI(match.getLeft());
			rightURI = getResourceURI(match.getRight());
			originURI = getResourceURI(match.getOrigin());
		}

		/**
		 * Compute the URI of a given EObject's resource, avoiding NPEs.
		 * 
		 * @param o
		 *            The EObject
		 * @return The given EObject's resource URI, may be null.
		 */
		private URI getResourceURI(EObject o) {
			if (o == null || o.eResource() == null) {
				return null;
			}
			return o.eResource().getURI();
		}

		/**
		 * Predicate implementation.
		 * 
		 * @param input
		 *            the MatchResource to filter.
		 * @return true if the given matchResource is part of the same papyrus virtual node as this
		 *         predicate's reference change, but is not related to the same resource.
		 */
		public boolean apply(MatchResource input) {
			if (input.getLeft() != null && leftURI != null) {
				URI uri = input.getLeft().getURI();
				return !leftURI.equals(uri) && leftURI.trimFileExtension().equals(uri.trimFileExtension());
			}
			if (input.getRight() != null && rightURI != null) {
				URI uri = input.getRight().getURI();
				return !rightURI.equals(uri) && rightURI.trimFileExtension().equals(uri.trimFileExtension());
			}
			if (input.getOrigin() != null && originURI.trimFileExtension() != null) {
				URI uri = input.getOrigin().getURI();
				return !originURI.equals(uri)
						&& originURI.trimFileExtension().equals(uri.trimFileExtension());
			}
			return false;
		}
	}
}
