/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This specialization of the {@link ResourceAttachmentChangeImpl} class allows us to define the derived
 * features and operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceAttachmentChangeSpec extends ResourceAttachmentChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyLeftToRight()
	 */
	@Override
	public void copyLeftToRight() {
		// Don't merge an already merged (or discarded) diff
		if (getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		setState(DifferenceState.MERGED);
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
			}
		}

		if (getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(false);

			switch (getKind()) {
				case ADD:
					// Create the same root in right
					addInTarget(false);
					break;
				case DELETE:
					// Delete that same root from right
					removeFromTarget(false);
					break;
				default:
					// other cases are unknown at the time of writing
					break;
			}
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(false);

			switch (getKind()) {
				case ADD:
					// Revert the addition of this root
					removeFromTarget(false);
					break;
				case DELETE:
					// re-create this element
					addInTarget(false);
					break;
				default:
					// other cases are unknown at the time of writing
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyRightToLeft()
	 */
	@Override
	public void copyRightToLeft() {
		// Don't merge an already merged (or discarded) diff
		if (getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		setState(DifferenceState.MERGED);
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
			}
		}

		if (getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(true);

			switch (getKind()) {
				case ADD:
					// Revert the addition of this root
					removeFromTarget(true);
					break;
				case DELETE:
					// re-create this element
					addInTarget(true);
					break;
				default:
					// other cases are unknown at the time of writing
					break;
			}
		} else {
			// merge all "requires" diffs
			mergeRequires(true);

			switch (getKind()) {
				case ADD:
					// Create the same root in left
					addInTarget(true);
					break;
				case DELETE:
					// Delete that same root from left
					removeFromTarget(true);
					break;
				default:
					// other cases are unknown at the time of writing
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
		// Should we also discard equivalent diffs? And diffs that require this one?
	}

	/**
	 * This will merge all {@link #getRequiredBy() differences that require us} in the given direction.
	 * 
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft() apply} all {@link #getRequiredBy() differences
	 *            that require us}. Otherwise, {@link #copyLeftToRight() revert} them.
	 */
	protected void mergeRequiredBy(boolean rightToLeft) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : getRequiredBy()) {
			// TODO: what to do when state = Discarded but is required?
			if (rightToLeft) {
				dependency.copyRightToLeft();
			} else {
				dependency.copyLeftToRight();
			}
		}
	}

	/**
	 * This will merge all {@link #getRequires() required differences} in the given direction.
	 * 
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft() apply} all {@link #getRequires() required
	 *            differences}. Otherwise, {@link #copyLeftToRight() revert} them.
	 */
	protected void mergeRequires(boolean rightToLeft) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : getRequires()) {
			// TODO: what to do when state = Discarded but is required?
			if (rightToLeft) {
				dependency.copyRightToLeft();
			} else {
				dependency.copyLeftToRight();
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
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	protected void addInTarget(boolean rightToLeft) {
		final Comparison comparison = getMatch().getComparison();
		final Resource expectedContainer = findOrCreateTargetResource(rightToLeft);

		final EObject sourceValue;
		if (rightToLeft) {
			sourceValue = getMatch().getRight();
		} else {
			sourceValue = getMatch().getLeft();
		}

		final EObject expectedValue;
		final Match valueMatch = getMatch();
		if (rightToLeft) {
			if (valueMatch.getLeft() != null) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = createTarget(sourceValue);
				valueMatch.setLeft(expectedValue);
			}
		} else if (valueMatch.getRight() != null) {
			expectedValue = valueMatch.getRight();
		} else {
			expectedValue = createTarget(sourceValue);
			valueMatch.setRight(expectedValue);
		}

		// double-check : is our target already present in the target resource?
		final URI sourceURI = EcoreUtil.getURI(sourceValue);
		if (expectedContainer.getEObject(sourceURI.fragment()) != null) {
			/*
			 * The only way for this use case to kick in is if we have both (or "all three") compared models
			 * side-by-side during a local comparison. In such an event, the "new" resource can only be an
			 * existing one (since relative paths will always resolve to the same location whatever the side),
			 * and it will obviously already contain the object since we detected the resource change. In such
			 * a case, we do not want to erase the already existing object or copy a duplicate in the target
			 * resource. We'll simply change the "to-be-modified" object to point to that already existing one
			 * through proxification and re-resolution. This is costly and clumsy, but this use case should be
			 * sufficiently rare to not be noticed, we only want it to be functional.
			 */
			((InternalEObject)expectedValue).eSetProxyURI(sourceURI);
			if (expectedContainer.getResourceSet() != null) {
				EcoreUtil.resolveAll(expectedContainer.getResourceSet());
			} else {
				EcoreUtil.resolveAll(expectedContainer);
			}
			if (rightToLeft) {
				valueMatch.setLeft(expectedContainer.getEObject(sourceURI.fragment()));
			} else {
				valueMatch.setRight(expectedContainer.getEObject(sourceURI.fragment()));
			}
			return;
		}

		// We have the container, reference and value. We need to know the insertion index.
		final Resource initialResource = sourceValue.eResource();
		final List<EObject> sourceList = initialResource.getContents();
		final List<EObject> targetList = expectedContainer.getContents();
		final int insertionIndex = DiffUtil.findInsertionIndex(comparison, sourceList, targetList,
				expectedValue);

		if (targetList instanceof InternalEList<?>) {
			((InternalEList<EObject>)targetList).addUnique(insertionIndex, expectedValue);
		} else {
			targetList.add(insertionIndex, expectedValue);
		}

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
	 * @param rightToLeft
	 *            Direction of the merge. This will tell us which side we are to look up for the target
	 *            resource.
	 * @return The resource we could find in the current comparison if any. Otherwise, we'll return either a
	 *         newly created resource that can serve as a target of this merge, or <code>null</code> if no
	 *         valid target resource can be created.
	 */
	private Resource findOrCreateTargetResource(boolean rightToLeft) {
		final Comparison comparison = getMatch().getComparison();
		final Resource sourceRes;
		if (rightToLeft) {
			sourceRes = getMatch().getRight().eResource();
		} else {
			sourceRes = getMatch().getLeft().eResource();
		}

		final List<MatchResource> matchedResources = comparison.getMatchedResources();
		final int size = matchedResources.size();
		final MatchResource soughtMatch = getMatchResource(sourceRes);

		// Is the resource already existing or do we need to create it?
		final Resource target;
		if (rightToLeft && soughtMatch.getLeft() != null) {
			target = soughtMatch.getLeft();
		} else if (!rightToLeft && soughtMatch.getRight() != null) {
			target = soughtMatch.getRight();
		} else {
			// we need to create it.
			final URI targetURI = computeTargetURI(rightToLeft);
			// FIXME this will most likely fail with remote URIs : we'll need to make it local afterwards
			if (targetURI == null) {
				// We treat null as "no valid target". We'll cancel the merge operation.
				return null;
			}

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

			// This resource might already exists
			if (targetSet.getURIConverter().exists(targetURI, Collections.emptyMap())) {
				target = targetSet.getResource(targetURI, true);
			} else {
				target = targetSet.createResource(targetURI);
			}

			if (rightToLeft) {
				soughtMatch.setLeft(target);
			} else {
				soughtMatch.setRight(target);
			}

		}

		return target;
	}

	/**
	 * Computes the URI of the "target" resource. Will be used if we need to create or "find" it.
	 * 
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return The URI that is to be used for our target resource. <code>null</code> if we cannot compute a
	 *         valid target URI.
	 */
	protected URI computeTargetURI(boolean rightToLeft) {
		final EObject sourceObject;
		final EObject targetObject;
		if (rightToLeft) {
			sourceObject = getMatch().getRight();
			targetObject = getMatch().getLeft();
		} else {
			sourceObject = getMatch().getLeft();
			targetObject = getMatch().getRight();
		}
		final Resource sourceResource = sourceObject.eResource();
		// This is the resource that will change through this merge.
		// We will only use it to determine a relative path for the real target resource.
		final Resource currentResource = targetObject.eResource();

		final MatchResource matchCurrent = getMatchResource(currentResource);
		final Resource currentFromSourceSide;
		if (rightToLeft) {
			currentFromSourceSide = matchCurrent.getRight();
		} else {
			currentFromSourceSide = matchCurrent.getLeft();
		}

		// Case of control/uncontrol
		final URI relativeTargetURI = sourceResource.getURI().deresolve(currentFromSourceSide.getURI());

		return relativeTargetURI.resolve(currentResource.getURI());
	}

	/**
	 * Returns the MatchResource corresponding to the given <code>resource</code>.
	 * 
	 * @param resource
	 *            Resource for which we need a MatchResource.
	 * @return The MatchResource corresponding to the given <code>resource</code>.
	 */
	protected MatchResource getMatchResource(Resource resource) {
		final List<MatchResource> matchedResources = getMatch().getComparison().getMatchedResources();
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
	 * This will create a copy of the given EObject that can be used as the target of an addition (or the
	 * reverting of a deletion).
	 * <p>
	 * The target will be self-contained and will have no reference towards any other EObject set (neither
	 * containment nor "classic" references). All of its attributes' values will match the given
	 * {@code referenceObject}'s.
	 * </p>
	 * 
	 * @param referenceObject
	 *            The EObject for which we'll create a copy.
	 * @return A self-contained copy of {@code referenceObject}.
	 * @see EMFCompareCopier#copy(EObject)
	 */
	protected EObject createTarget(EObject referenceObject) {
		/*
		 * We can't simply use EcoreUtil.copy. References will have their own diffs and will thus be merged
		 * later on.
		 */
		final EcoreUtil.Copier copier = new EMFCompareCopier();
		return copier.copy(referenceObject);
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
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	protected void removeFromTarget(boolean rightToLeft) {
		final Match valueMatch = getMatch();
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
}
