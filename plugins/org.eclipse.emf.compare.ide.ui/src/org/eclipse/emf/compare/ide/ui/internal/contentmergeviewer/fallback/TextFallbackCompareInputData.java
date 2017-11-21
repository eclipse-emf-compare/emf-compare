/*******************************************************************************
 * Copyright (c) 2017, 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback;

import static com.google.common.collect.Iterables.getFirst;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.internal.ui.synchronize.LocalResourceTypedElement;

/**
 * An encapsulation of information about the three sides of a {@link TextFallbackCompareInput}.
 * 
 * @see TextFallbackMergeViewer#getAdaptedCompareInput(CompareInputAdapter)}.
 */
@SuppressWarnings("restriction")
public class TextFallbackCompareInputData {

	/**
	 * The origin object.
	 */
	private EObject origin;

	/**
	 * The origin resource.
	 */
	private Resource originResource;

	/**
	 * The origin storage.
	 */
	private IStorage originStorage;

	/**
	 * The origin typed element.
	 */
	private ITypedElement originTypedElement;

	/**
	 * The left object.
	 */
	private EObject left;

	/**
	 * The left resource.
	 */
	private Resource leftResource;

	/**
	 * The left storage.
	 */
	private IStorage leftStorage;

	/**
	 * The left typed element.
	 */
	private ITypedElement leftTypedElement;

	/**
	 * The right object.
	 */
	private EObject right;

	/**
	 * The right resource.
	 */
	private Resource rightResource;

	/**
	 * The right storage.
	 */
	private IStorage rightStorage;

	/**
	 * The right typed element.
	 */
	private ITypedElement rightTypedElement;

	/**
	 * Creates an instance for the given object. It calls {@link #populate(EObject)} to populate the
	 * {@link #origin}, {@link #originResource}, {@link #left}, {@link #leftResource}, {@link #right}, and
	 * {@link #rightResource} followed by {@link #populateStorage()} to populate the {@link #originStorage},
	 * {@link #originTypedElement}, {@link #leftStorage}, {@link #leftTypedElement}, {@link #rightStorage},
	 * and {@link #rightTypedElement}.
	 * 
	 * @param eObject
	 *            the object for which to populate the sides of the text input data.
	 */
	public TextFallbackCompareInputData(EObject eObject) {
		populate(eObject);
		populateStorage();
	}

	/**
	 * Returns {@code true}, when at least one of {@link #originTypedElement}, {@link #leftTypedElement}, or
	 * {@link #rightTypedElement} is not {@code null}.
	 * 
	 * @return whether this text input data has a typed element for at least one of the sides.
	 */
	public boolean hasTypedElement() {
		return getOriginTypedElement() != null || getLeftTypedElement() != null
				|| getRightTypedElement() != null;
	}

	/**
	 * Populates the {@link #origin}, {@link #originResource}, {@link #left}, {@link #leftResource},
	 * {@link #right}, and {@link #rightResource}. Note that method calls itself recursively.
	 * 
	 * @param eObject
	 *            the object for which to populate the sides of the text input data.
	 */
	private void populate(EObject eObject) {
		if (eObject instanceof Match) {
			populateFromMatch((Match)eObject);
		} else if (eObject instanceof Diff) {
			populateFromDiff((Diff)eObject);
		} else if (eObject instanceof MatchResource) {
			// If its a resource match, use the sides resources.
			MatchResource matchResource = (MatchResource)eObject;
			this.originResource = matchResource.getOrigin();
			this.leftResource = matchResource.getLeft();
			this.rightResource = matchResource.getRight();
		} else if (eObject instanceof Equivalence) {
			// If it's an equivalence, use the first diff.
			Equivalence equivalence = (Equivalence)eObject;
			EList<Diff> differences = equivalence.getDifferences();
			Diff first = getFirst(differences, null);
			populate(first);
		} else if (eObject instanceof Conflict) {
			// If it's a conflict, use the first diff.
			Conflict conflict = (Conflict)eObject;
			EList<Diff> differences = conflict.getDifferences();
			Diff first = getFirst(differences, null);
			populate(first);
		}
	}

	private void populateFromMatch(Match match) {
		this.origin = match.getOrigin();
		this.left = match.getLeft();
		this.right = match.getRight();

		// If they're all null, and there is a containing match, populate using that instead.
		if (getOrigin() == null && getLeft() == null && getRight() == null) {
			EObject eContainer = match.eContainer();
			if (eContainer instanceof Match) {
				populateFromMatch((Match)eContainer);
			}
		} else {
			// If there is a comparison...
			Comparison comparison = match.getComparison();
			if (comparison != null) {
				// Try to find a resource match for the left, right, or origin.
				MatchResource matchResource = getMatchResource(comparison, getSideResource(getLeft()));
				if (matchResource == null) {
					matchResource = getMatchResource(comparison, getSideResource(getRight()));
					if (matchResource == null) {
						matchResource = getMatchResource(comparison, getSideResource(getOrigin()));
					}
				}
				// Use the sides from the resource match, or failing that the side resource of the side.
				if (matchResource != null) {
					this.originResource = matchResource.getOrigin();
					if (getOriginResource() == null) {
						this.originResource = getSideResource(getOrigin());
					}
					this.leftResource = matchResource.getLeft();
					if (getLeftResource() == null) {
						this.leftResource = getSideResource(getLeft());
					}
					this.rightResource = matchResource.getRight();
					if (getRightResource() == null) {
						this.rightResource = getSideResource(getRight());
					}
				}
			} else {
				// Otherwise we're forced to use only the side resources from the sides themselves.
				this.originResource = getSideResource(getOrigin());
				this.leftResource = getSideResource(getLeft());
				this.rightResource = getSideResource(getRight());
			}
		}
	}

	private void populateFromDiff(Diff diff) {
		for (Diff refinedDiff : diff.getRefinedBy()) {
			if (areAllResourcesNull()) {
				populateFromDiff(refinedDiff);
			}
		}

		if (areAllResourcesNull()) {
			// If the diff is a reference change of a containment reference and that change has a non-null
			// value...
			if (diff instanceof ReferenceChange) {
				ReferenceChange referenceChange = (ReferenceChange)diff;
				if (referenceChange.getReference().isContainment()) {
					EObject value = referenceChange.getValue();
					if (value != null) {
						// Get the comparison for that value, and if it's not null, and there is a match
						// for the value, populate using that match.
						Comparison comparison = ComparisonUtil.getComparison(diff);
						if (comparison != null) {
							Match match = comparison.getMatch(value);
							if (match != null) {
								populateFromMatch(match);
							}
						}
					}
				}
			}
		}

		if (areAllResourcesNull()) {
			// Failing those possibilities, populate using the containing match.
			populateFromMatch(diff.getMatch());
		}
	}

	/**
	 * Specifies whether all resources are <code>null</code>.
	 * <p>
	 * All resources are {@link #getOriginResource()}, {@link #getLeftResource()}, and
	 * {@link #getRightResource()}.
	 * <p>
	 * 
	 * @return <code>true</code> if all resources are <code>null</code>, <code>false</code> otherwise.
	 */
	private boolean areAllResourcesNull() {
		return getOriginResource() == null && getLeftResource() == null && getRightResource() == null;
	}

	/**
	 * Returns the match resource of the comparison for the given resource.
	 * 
	 * @param comparison
	 *            the comparison in which to find a match resource.
	 * @param resource
	 *            the resource for which to find a match resource.
	 * @return the match resource of the comparison for the given resource.
	 */
	private MatchResource getMatchResource(Comparison comparison, Resource resource) {
		if (resource != null) {
			for (MatchResource matchResource : comparison.getMatchedResources()) {
				if (matchResource.getLeft() == resource || matchResource.getRight() == resource
						|| matchResource.getOrigin() == resource) {
					return matchResource;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the {@link EObject#eResource() containing} resource of the object.
	 * 
	 * @param eObject
	 *            the object for which to get the containing resource, or {@code null}.
	 * @return the resource of the object, or {@code null}.
	 */
	private Resource getSideResource(EObject eObject) {
		if (eObject != null) {
			return eObject.eResource();
		} else {
			return null;
		}
	}

	/**
	 * Populates the {@link #originStorage}, {@link #originTypedElement}, {@link #leftStorage},
	 * {@link #leftTypedElement}, {@link #rightStorage}, and {@link #rightTypedElement}.
	 */
	private void populateStorage() {
		this.originStorage = getStorage(getOriginResource());
		this.originTypedElement = getTypedElement(getOriginStorage());
		this.leftStorage = getStorage(getLeftResource());
		this.leftTypedElement = getTypedElement(getLeftStorage());
		this.rightStorage = getStorage(getRightResource());
		this.rightTypedElement = getTypedElement(getRightStorage());
	}

	/**
	 * Returns the {@link ResourceUtil#getAssociatedStorage(Resource) associated storage} of the resource.
	 * 
	 * @param resource
	 *            the resource for which to get associated storage.
	 * @return the associated storage of the resource
	 */
	private IStorage getStorage(Resource resource) {
		if (resource != null) {
			return ResourceUtil.getAssociatedStorage(resource);
		} else {
			return null;
		}
	}

	/**
	 * Creates an appropriate typed element for the given storage.
	 * 
	 * @param storage
	 *            the storage or {@code null}.
	 * @return a typed element for the storage or {@code null}.
	 */
	private ITypedElement getTypedElement(IStorage storage) {
		if (storage instanceof IResource) {
			IResource resourceStorage = (IResource)storage;
			return new LocalResourceTypedElement(resourceStorage);
		} else if (storage != null) {
			return new StorageTypedElement(storage, ResourceUtil.getFixedPath(storage).toString());
		} else {
			return null;
		}
	}

	public EObject getOrigin() {
		return origin;
	}

	public Resource getOriginResource() {
		return originResource;
	}

	public IStorage getOriginStorage() {
		return originStorage;
	}

	public ITypedElement getOriginTypedElement() {
		return originTypedElement;
	}

	public EObject getLeft() {
		return left;
	}

	public Resource getLeftResource() {
		return leftResource;
	}

	public IStorage getLeftStorage() {
		return leftStorage;
	}

	public ITypedElement getLeftTypedElement() {
		return leftTypedElement;
	}

	public EObject getRight() {
		return right;
	}

	public Resource getRightResource() {
		return rightResource;
	}

	public IStorage getRightStorage() {
		return rightStorage;
	}

	public ITypedElement getRightTypedElement() {
		return rightTypedElement;
	}
}
