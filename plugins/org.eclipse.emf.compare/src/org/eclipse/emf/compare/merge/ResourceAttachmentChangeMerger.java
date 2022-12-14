/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - fix bug 465329
 *     Simon Delisle - fix bug 488089
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.merge.IMergeCriterion.NONE;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
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

	@Override
	public boolean apply(IMergeCriterion criterion) {
		return criterion == null || criterion == NONE;
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
		final Resource expectedResource = findOrCreateTargetResource(match, rightToLeft);

		if (expectedResource == null) {
			throw new IllegalStateException("Another diff should have been merged before " + diff); //$NON-NLS-1$
		}

		final EObject sourceValue;
		if (comparison.isThreeWay()) {
			// This is a 3-way move, match.getOrigin() can't be null
			if (rightToLeft) {
				if (match.getRight() != null) {
					sourceValue = match.getRight();
				} else {
					sourceValue = match.getOrigin();
				}
			} else {
				if (match.getLeft() != null) {
					sourceValue = match.getLeft();
				} else {
					sourceValue = match.getOrigin();
				}
			}
		} else if (rightToLeft) {
			// This is a 2-way move, match.getRight() & match.getLeft() can't be null
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
		final List<EObject> sourceList = initialResource.getContents();
		final List<EObject> targetList = expectedResource.getContents();
		final int insertionIndex = findInsertionIndex(comparison, sourceList, targetList, expectedValue);
		addAt(targetList, expectedValue, insertionIndex);

		// Copy XMI ID when applicable.
		if (initialResource instanceof XMIResource && expectedResource instanceof XMIResource) {
			((XMIResource)expectedResource).setID(expectedValue,
					((XMIResource)initialResource).getID(sourceValue));
		}
	}

	/**
	 * A move of an EObject to a different resource has just been made. Do whatever post-treatment is needed.
	 * The default implementation deletes the former resource if it's no longer supposed to be here.
	 * 
	 * @param comparison
	 *            The comparison
	 * @param oldResource
	 *            The resource from where the EObject has been moved
	 * @param rightToLeft
	 *            The direction of the change
	 * @deprecated Use
	 *             {@link ResourceAttachmentChangeMerger#deleteFormerResourceIfNecessary(ResourceAttachmentChange, Resource, boolean)}
	 *             instead.
	 */
	@Deprecated
	protected void deleteFormerResourceIfNecessary(final Comparison comparison, final Resource oldResource,
			boolean rightToLeft) {
		// Do nothing
	}

	/**
	 * Indicate whether the given resource must be marked for deletion or not. Can be overridden in
	 * sub-classes if necessary.
	 * 
	 * @param resource
	 *            The resource candidate for deletion
	 * @param diff
	 *            The ResourceAttachmentChange that's just been merged
	 * @param rightToLeft
	 *            The direction of the merge
	 * @return <code>true</code> if the given resource must be deleted.
	 * @deprecated Don't use this method.
	 */
	@Deprecated
	protected boolean mustDelete(Resource resource, ResourceAttachmentChange diff, boolean rightToLeft) {
		return false;
	}

	/**
	 * This method doesn't do anything now. The deletion of resources that need to be deleted is performed
	 * elsewhere.
	 * 
	 * @param resource
	 *            The resource to delete
	 * @deprecated Not used anymore, it's not the responsibility of the ResourceAttachmentChangeMerger to
	 *             delete resources. This is now achieved by installing a {@link ResourceChangeAdapter} on
	 *             comparisons.
	 */
	@Deprecated
	protected void deleteResource(final Resource resource) {
		// Don't do anything
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

		EObject sourceValue;
		if (rightToLeft) {
			sourceValue = match.getRight();
		} else {
			sourceValue = match.getLeft();
		}
		if (sourceValue == null) {
			sourceValue = match.getOrigin();
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
			((XMIResource)expectedContainer).setID(expectedValue,
					((XMIResource)initialResource).getID(sourceValue));
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
			if (match.getRight() != null) {
				sourceRes = match.getRight().eResource();
			} else {
				sourceRes = match.getOrigin().eResource();
			}
		} else {
			if (match.getLeft() != null) {
				sourceRes = match.getLeft().eResource();
			} else {
				sourceRes = match.getOrigin().eResource();
			}
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
				throw new RuntimeException("Couldn't create a valid target resource for " //$NON-NLS-1$
						+ sourceRes.getURI());
			}

			ResourceSet targetSet = null;
			IComparisonScope scope = (IComparisonScope)EcoreUtil.getAdapter(comparison.eAdapters(),
					IComparisonScope.class);
			if (rightToLeft) {
				Notifier left = scope.getLeft();
				if (left instanceof ResourceSet) {
					targetSet = (ResourceSet)left;
				} else if (left instanceof Resource) {
					targetSet = ((Resource)left).getResourceSet();
				} else if (left instanceof EObject) {
					Resource res = ((EObject)left).eResource();
					if (res != null) {
						targetSet = res.getResourceSet();
					}
				}
			} else {
				Notifier right = scope.getRight();
				if (right instanceof ResourceSet) {
					targetSet = (ResourceSet)right;
				} else if (right instanceof Resource) {
					targetSet = ((Resource)right).getResourceSet();
				} else if (right instanceof EObject) {
					Resource res = ((EObject)right).eResource();
					if (res != null) {
						targetSet = res.getResourceSet();
					}
				}
			}

			if (targetSet == null) {
				// Cannot create the target
				throw new RuntimeException(EMFCompareMessages
						.getString("ResourceAttachmentChangeSpec.MissingRS", targetURI.lastSegment())); //$NON-NLS-1$
			}

			// This resource might already exists (on disk)... in which case we cannot use it
			if (targetSet.getURIConverter().exists(targetURI, Collections.emptyMap())) {
				throw new RuntimeException("The resource '" + sourceRes.getURI() //$NON-NLS-1$
						+ "' already exists at that location."); //$NON-NLS-1$
			} else {
				// The resource might already exist because a IResourceChangeParticipant has created it or in
				// some undo/redo cases
				// If that is the case, just reuse the existing resource
				Resource existing = targetSet.getResource(targetURI, false);
				if (existing == null) {
					target = targetSet.createResource(targetURI);
				} else {
					target = existing;
				}

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
		EObject sourceObject;
		final EObject targetObject;
		if (rightToLeft) {
			sourceObject = match.getRight();
			targetObject = match.getLeft();
		} else {
			sourceObject = match.getLeft();
			targetObject = match.getRight();
		}
		if (sourceObject == null) {
			sourceObject = match.getOrigin();
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
			throw new RuntimeException(EMFCompareMessages
					.getString("ResourceAttachmentChangeSpec.MissingMatch", resource.getURI().lastSegment())); //$NON-NLS-1$
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
	 * <li>we are copying a deletion from the right side (we need to remove the same root from the left) or,
	 * </li>
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
			final Resource resource = ((InternalEObject)expectedValue).eDirectResource();
			// We only wish to remove the element from its containing resource, not from its container.
			// This will not affect the match.
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
	protected <E> int findInsertionIndex(Comparison comparison, List<E> source, List<E> target,
			E newElement) {
		return DiffUtil.findInsertionIndex(comparison, source, target, newElement);
	}
}
