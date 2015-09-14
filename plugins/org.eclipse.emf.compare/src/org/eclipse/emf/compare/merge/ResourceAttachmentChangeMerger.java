/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - fix bug 465329
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge resource attachment changes.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceAttachmentChangeMerger extends AbstractMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target instanceof ResourceAttachmentChange;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.AbstractMerger#accept(org.eclipse.emf.compare.Diff, boolean)
	 */
	@Override
	protected void accept(Diff diff, boolean rightToLeft) {
		ResourceAttachmentChange resourceAttachmentChange = (ResourceAttachmentChange)diff;
		switch (diff.getKind()) {
			case ADD:
				// Create the same root in right
				addInTarget(resourceAttachmentChange, rightToLeft);
				break;
			case DELETE:
				// Delete that same root from right
				removeFromTarget(resourceAttachmentChange, rightToLeft);
				break;
			case MOVE:
				// Move that same root from right. A move of a ResourceAttachmentChange can only happens with
				// non local comparison. So if it is a Remote diff, only ACCEPT will actually merge the diff.
				// REJECT will do nothing. If it is a Local diff, only REJECT will actually change the model.
				// ACCEPT will do nothing.
				move(resourceAttachmentChange, rightToLeft);
				break;
			default:
				// other cases are unknown at the time of writing
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.AbstractMerger#reject(org.eclipse.emf.compare.Diff, boolean)
	 */
	@Override
	protected void reject(Diff diff, boolean rightToLeft) {
		ResourceAttachmentChange resourceAttachmentChange = (ResourceAttachmentChange)diff;
		switch (diff.getKind()) {
			case ADD:
				// Revert the addition of this root
				removeFromTarget(resourceAttachmentChange, rightToLeft);
				break;
			case DELETE:
				// re-create this element
				addInTarget(resourceAttachmentChange, rightToLeft);
				break;
			case MOVE:
				// Move that same root from right. A move of a ResourceAttachmentChange can only happens with
				// non local comparison. So if it is a Remote diff, only ACCEPT will actually merge the diff.
				// REJECT will do nothing. If it is a Local diff, only REJECT will actually change the model.
				// ACCEPT will do nothing.
				move(resourceAttachmentChange, rightToLeft);
				break;
			default:
				// other cases are unknown at the time of writing
				break;
		}
	}

	/**
	 * Handle moves of {@link ResourceAttachmentChange}s.
	 * 
	 * @param diff
	 *            The difference we are to merge.
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	protected void move(ResourceAttachmentChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final Comparison comparison = match.getComparison();
		final Resource expectedContainer = findOrCreateTargetResource(match, rightToLeft);

		if (expectedContainer == null) {
			// TODO log
			diff.setState(DifferenceState.UNRESOLVED);
			return;
		}

		// This is a move, match.getRight() & match.getLeft() can't be null
		final EObject sourceValue;
		if (rightToLeft) {
			sourceValue = match.getRight();
		} else {
			sourceValue = match.getLeft();
		}

		final EObject expectedValue;
		if (rightToLeft) {
			expectedValue = match.getLeft();
		} else {
			expectedValue = match.getRight();
		}

		// We have the container, reference and value. We need to know the insertion index.
		final Resource initialResource = sourceValue.eResource();
		final Resource oldResource = expectedValue.eResource();
		final List<EObject> sourceList = initialResource.getContents();
		final List<EObject> targetList = expectedContainer.getContents();
		final int insertionIndex = findInsertionIndex(comparison, sourceList, targetList, expectedValue);
		addAt(targetList, expectedValue, insertionIndex);

		// Copy XMI ID when applicable.
		if (initialResource instanceof XMIResource && expectedContainer instanceof XMIResource) {
			((XMIResource)expectedContainer).setID(expectedValue, ((XMIResource)initialResource)
					.getID(sourceValue));
		}

		// If after a move of a {@link ResourceAttachmentChange} the initial resource is empty, we have to
		// delete this resource
		EList<EObject> contents = oldResource.getContents();
		if (contents == null || contents.isEmpty()) {
			try {
				oldResource.delete(Collections.emptyMap());
			} catch (IOException e) {
				// FIXME log exception.
			}
		}
	}

	/**
	 * This will be called when we need to create an element in the target side.
	 * <p>
	 * All necessary sanity checks have been made to ensure that the current operation is one that should
	 * create an object in its side. In other words, either :
	 * <ul>
	 * <li>We are copying from right to left and
	 * <ul>
	 * <li>we are copying an addition to the right side (we need to create the same root in the left), or</li>
	 * <li>we are copying a deletion from the left side (we need to revert the deletion).</li>
	 * </ul>
	 * </li>
	 * <li>We are copying from left to right and
	 * <ul>
	 * <li>we are copying a deletion from the right side (we need to revert the deletion), or</li>
	 * <li>we are copying an addition to the left side (we need to create the same root in the right).</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param diff
	 *            The difference we are to merge.
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	protected void addInTarget(ResourceAttachmentChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final Comparison comparison = match.getComparison();
		final Resource expectedContainer = findOrCreateTargetResource(match, rightToLeft);
		if (expectedContainer == null) {
			// TODO log
			diff.setState(DifferenceState.UNRESOLVED);
			return;
		}

		final EObject sourceValue;
		if (rightToLeft) {
			sourceValue = match.getRight();
		} else {
			sourceValue = match.getLeft();
		}

		final EObject expectedValue;
		if (rightToLeft) {
			if (match.getLeft() != null) {
				expectedValue = match.getLeft();
			} else {
				expectedValue = createCopy(sourceValue);
				match.setLeft(expectedValue);
			}
		} else if (match.getRight() != null) {
			expectedValue = match.getRight();
		} else {
			expectedValue = createCopy(sourceValue);
			match.setRight(expectedValue);
		}

		// We have the container, reference and value. We need to know the insertion index.
		final Resource initialResource = sourceValue.eResource();
		final List<EObject> sourceList = initialResource.getContents();
		final List<EObject> targetList = expectedContainer.getContents();
		final int insertionIndex = findInsertionIndex(comparison, sourceList, targetList, expectedValue);
		addAt(targetList, expectedValue, insertionIndex);

		// Copy XMI ID when applicable.
		if (initialResource instanceof XMIResource && expectedContainer instanceof XMIResource) {
			((XMIResource)expectedContainer).setID(expectedValue, ((XMIResource)initialResource)
					.getID(sourceValue));
		}
	}

	/**
	 * This will try and locate the "target" resource of this merge in the current comparison. If we can't
	 * locate it, we assume that it needs to be created as we are in the process of adding a new element to
	 * it.
	 * 
	 * @param match
	 *            Match of the root which resource we need to find or create.
	 * @param rightToLeft
	 *            Direction of the merge. This will tell us which side we are to look up for the target
	 *            resource.
	 * @return The resource we could find in the current comparison if any. Otherwise, we'll return either a
	 *         newly created resource that can serve as a target of this merge, or <code>null</code> if no
	 *         valid target resource can be created.
	 */
	protected Resource findOrCreateTargetResource(Match match, boolean rightToLeft) {
		final Comparison comparison = match.getComparison();
		final Resource sourceRes;
		if (rightToLeft) {
			sourceRes = match.getRight().eResource();
		} else {
			sourceRes = match.getLeft().eResource();
		}

		final MatchResource soughtMatch = getMatchResource(comparison, sourceRes);

		// Is the resource already existing or do we need to create it?
		final Resource target;
		if (rightToLeft && soughtMatch.getLeft() != null) {
			target = soughtMatch.getLeft();
		} else if (!rightToLeft && soughtMatch.getRight() != null) {
			target = soughtMatch.getRight();
		} else {
			// we need to create it.
			final URI targetURI = computeTargetURI(match, rightToLeft);
			// FIXME this will most likely fail with remote URIs : we'll need to make it local afterwards
			if (targetURI == null) {
				// We treat null as "no valid target". We'll cancel the merge operation.
				// FIXME we need to rollback the current merge operation.
				throw new RuntimeException("Couldn't create a valid target resource for " //$NON-NLS-1$
						+ sourceRes.getURI());
			}

			final List<MatchResource> matchedResources = comparison.getMatchedResources();
			final int size = matchedResources.size();
			ResourceSet targetSet = null;
			for (int i = 0; i < size && targetSet == null; i++) {
				final MatchResource matchRes = matchedResources.get(i);
				if (rightToLeft && matchRes.getLeft() != null) {
					targetSet = matchRes.getLeft().getResourceSet();
				} else if (!rightToLeft && matchRes.getRight() != null) {
					targetSet = matchRes.getRight().getResourceSet();
				}
			}

			if (targetSet == null) {
				// Cannot create the target
				throw new RuntimeException(EMFCompareMessages.getString(
						"ResourceAttachmentChangeSpec.MissingRS", targetURI.lastSegment())); //$NON-NLS-1$
			}

			// This resource might already exists... in which case we cannot use it
			if (targetSet.getURIConverter().exists(targetURI, Collections.emptyMap())) {
				// FIXME we need to rollback the current merge operation.
				throw new RuntimeException("The resource '" + sourceRes.getURI() //$NON-NLS-1$
						+ "' already exists at that location."); //$NON-NLS-1$
			} else {
				target = targetSet.createResource(targetURI);

				if (rightToLeft) {
					soughtMatch.setLeft(target);
					soughtMatch.setLeftURI(target.getURI().toString());
				} else {
					soughtMatch.setRight(target);
					soughtMatch.setRightURI(target.getURI().toString());
				}
			}
		}

		return target;
	}

	/**
	 * Computes the URI of the "target" resource. Will be used if we need to create or "find" it.
	 * 
	 * @param match
	 *            Match of the root for which we need a resource URI.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return The URI that is to be used for our target resource. <code>null</code> if we cannot compute a
	 *         valid target URI.
	 */
	protected URI computeTargetURI(Match match, boolean rightToLeft) {
		final EObject sourceObject;
		final EObject targetObject;
		if (rightToLeft) {
			sourceObject = match.getRight();
			targetObject = match.getLeft();
		} else {
			sourceObject = match.getLeft();
			targetObject = match.getRight();
		}

		final Resource currentResource;
		if (targetObject == null) {
			// A new root in a resource we don't have yet.
			// Is this object container somewhere else (controlled)?
			final EObject sourceContainer = sourceObject.eContainer();
			if (sourceContainer != null) {
				final Match containerMatch = match.getComparison().getMatch(sourceContainer);
				if (rightToLeft) {
					currentResource = containerMatch.getLeft().eResource();
				} else {
					currentResource = containerMatch.getRight().eResource();
				}
			} else {
				// The sourceObject is the root object of a new resource that needs to be created. So return
				// the original URI of the new resource.
				return sourceObject.eResource().getURI();
			}
		} else {
			currentResource = targetObject.eResource();
		}
		final Resource sourceResource = sourceObject.eResource();

		final MatchResource matchCurrent = getMatchResource(match.getComparison(), currentResource);
		final Resource currentFromSourceSide;
		if (rightToLeft) {
			currentFromSourceSide = matchCurrent.getRight();
		} else {
			currentFromSourceSide = matchCurrent.getLeft();
		}

		if (currentFromSourceSide != null) {
			// Case of control/uncontrol
			final URI relativeTargetURI = sourceResource.getURI().deresolve(currentFromSourceSide.getURI());
			return relativeTargetURI.resolve(currentResource.getURI());
		} else {
			// Case of move
			return sourceResource.getURI();
		}
	}

	/**
	 * Returns the MatchResource corresponding to the given <code>resource</code>.
	 * 
	 * @param comparison
	 *            The current comparison.
	 * @param resource
	 *            Resource for which we need a MatchResource.
	 * @return The MatchResource corresponding to the given <code>resource</code>.
	 */
	protected MatchResource getMatchResource(Comparison comparison, Resource resource) {
		final List<MatchResource> matchedResources = comparison.getMatchedResources();
		final int size = matchedResources.size();
		MatchResource soughtMatch = null;
		for (int i = 0; i < size && soughtMatch == null; i++) {
			final MatchResource matchRes = matchedResources.get(i);
			if (matchRes.getRight() == resource || matchRes.getLeft() == resource
					|| matchRes.getOrigin() == resource) {
				soughtMatch = matchRes;
			}
		}

		if (soughtMatch == null) {
			// This should never happen
			throw new RuntimeException(EMFCompareMessages.getString(
					"ResourceAttachmentChangeSpec.MissingMatch", resource.getURI().lastSegment())); //$NON-NLS-1$
		}

		return soughtMatch;
	}

	/**
	 * This will be called when we need to remove an element from the target side.
	 * <p>
	 * All necessary sanity checks have been made to ensure that the current operation is one that should
	 * delete an object. In other words, we are :
	 * <ul>
	 * <li>Copying from right to left and either
	 * <ul>
	 * <li>we are copying a deletion from the right side (we need to remove the same root from the left) or,</li>
	 * <li>we are copying an addition to the left side (we need to revert the addition).</li>
	 * </ul>
	 * </li>
	 * <li>Copying from left to right and either
	 * <ul>
	 * <li>we are copying an addition to the right side (we need to revert the addition), or.</li>
	 * <li>we are copying a deletion from the left side (we need to remove the same root from the right).</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param diff
	 *            The difference we are to merge.
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	protected void removeFromTarget(ResourceAttachmentChange diff, boolean rightToLeft) {
		final Match valueMatch = diff.getMatch();
		final EObject expectedValue;
		if (rightToLeft) {
			expectedValue = valueMatch.getLeft();
		} else {
			expectedValue = valueMatch.getRight();
		}

		// if this is a pseudo conflict, we have no value to remove
		if (expectedValue != null) {
			// We only wish to remove the element from its containing resource, not from its container.
			// This will not affect the match.
			final Resource resource = ((InternalEObject)expectedValue).eDirectResource();
			resource.getContents().remove(expectedValue);
		}
	}

	/**
	 * This will be used by the distinct merge actions in order to find the index at which a value should be
	 * inserted in its target list. See {@link DiffUtil#findInsertionIndex(Comparison, Diff, boolean)} for
	 * more on this.
	 * <p>
	 * Sub-classes can override this if the insertion order is irrelevant. A return value of {@code -1} will
	 * be considered as "no index" and the value will be inserted at the end of its target list.
	 * </p>
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param source
	 *            The List from which one element has to be added to the {@code target} list.
	 * @param target
	 *            The List into which one element from {@code source} has to be added.
	 * @param newElement
	 *            The element from {@code source} that needs to be added into {@code target}.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The index at which the new value should be inserted into the 'target' list, as inferred from
	 *         its position in {@code source}. {@code -1} if the value should be inserted at the end of its
	 *         target list.
	 * @see DiffUtil#findInsertionIndex(Comparison, Diff, boolean)
	 */
	protected <E> int findInsertionIndex(Comparison comparison, List<E> source, List<E> target, E newElement) {
		return DiffUtil.findInsertionIndex(comparison, source, target, newElement);
	}
}
