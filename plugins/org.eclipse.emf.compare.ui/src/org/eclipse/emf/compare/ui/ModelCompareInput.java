/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Element;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.ui.internal.ModelComparator;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.util.EMFCompareEObjectUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Image;

/**
 * Input to be used for a 2 or 3-way comparison in a
 * {@link org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer ModelContentMergeViewer}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelCompareInput implements ICompareInput {
	/** Resource containing the ancestor object of this comparison. */
	private Resource ancestorResource;

	/** {@link DiffModel} result of the underlying comparison. */
	private final DiffModel diff;

	/** Keeps a list of all the differences (without DiffGroup) detected. */
	private List<DiffElement> diffList;

	/** Memorizes all listeners registered for this {@link ICompareInput compare input}. */
	private final List<ICompareInputChangeListener> inputChangeListeners = new ArrayList<ICompareInputChangeListener>();

	/** Resource containing the left compared object. */
	private Resource leftResource;

	/** {@link MatchModel} result of the underlying comparison. */
	private final MatchModel match;

	/** Resource containing the right compared object. */
	private Resource rightResource;

	/**
	 * Creates a CompareInput given the resulting
	 * {@link org.eclipse.emf.compare.match.diff.match.MatchModel match} and
	 * {@link org.eclipse.emf.compare.match.diff.diff.DiffModel diff} of the comparison.
	 * 
	 * @param matchModel
	 *            {@link org.eclipse.emf.compare.match.diff.match.MatchModel match} of the comparison.
	 * @param diffModel
	 *            {@link org.eclipse.emf.compare.match.diff.diff.DiffModel diff} of the comparison.
	 */
	public ModelCompareInput(MatchModel matchModel, DiffModel diffModel) {
		match = matchModel;
		diff = diffModel;
	}

	/**
	 * Creates a CompareInput given the resulting
	 * {@link org.eclipse.emf.compare.match.diff.match.MatchModel match} and
	 * {@link org.eclipse.emf.compare.match.diff.diff.DiffModel diff} of the comparison.
	 * 
	 * @param matchModel
	 *            {@link org.eclipse.emf.compare.match.diff.match.MatchModel match} of the comparison.
	 * @param diffModel
	 *            {@link org.eclipse.emf.compare.match.diff.diff.DiffModel diff} of the comparison.
	 * @param comparator
	 *            The comparator which has been used for this comparison.
	 */
	public ModelCompareInput(MatchModel matchModel, DiffModel diffModel, ModelComparator comparator) {
		this(matchModel, diffModel);
		leftResource = comparator.getLeftResource();
		rightResource = comparator.getRightResource();
		ancestorResource = comparator.getAncestorResource();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#addCompareInputChangeListener(ICompareInputChangeListener)
	 */
	public void addCompareInputChangeListener(ICompareInputChangeListener listener) {
		inputChangeListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#copy(boolean)
	 */
	public void copy(boolean leftToRight) {
		final List<DiffElement> differences = new ArrayList<DiffElement>(diff.getOwnedElements());
		doCopy(differences, leftToRight);
		fireCompareInputChanged();
	}

	/**
	 * Copies a single {@link DiffElement} or a {@link DiffGroup} in the given direction.
	 * 
	 * @param element
	 *            {@link DiffElement Element} to copy.
	 * @param leftToRight
	 *            Direction of the copy.
	 */
	public void copy(DiffElement element, boolean leftToRight) {
		doCopy(element, leftToRight);
		fireCompareInputChanged();
	}

	/**
	 * Copies a list of {@link DiffElement}s or {@link DiffGroup}s in the given direction.
	 * 
	 * @param elements
	 *            {@link DiffElement Element}s to copy.
	 * @param leftToRight
	 *            Direction of the copy.
	 */
	public void copy(List<DiffElement> elements, boolean leftToRight) {
		doCopy(elements, leftToRight);
		fireCompareInputChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		ITypedElement ancestor = null;
		if (ancestorResource != null) {
			if (ancestorResource.getContents().size() > 0)
				ancestor = new TypedElementWrapper(ancestorResource.getContents().get(0));
		} else {
			// Seeks a resource from the MatchModel
			// Assumes that some elements have been matched
			final TreeIterator<EObject> matchIterator = match.eAllContents();
			EObject root = null;
			while (matchIterator.hasNext()) {
				final EObject matchElement = matchIterator.next();
				if (matchElement instanceof Match3Element) {
					root = ((Match3Element)matchElement).getOriginElement().eResource().getContents().get(0);
					break;
				}
			}
			ancestor = new TypedElementWrapper(root);
		}
		return ancestor;
	}

	/**
	 * Returns this ModelCompareInput's DiffModel.
	 * 
	 * @return This ModelCompareInput's DiffModel.
	 */
	public DiffModel getDiff() {
		return diff;
	}

	/**
	 * Returns the {@link DiffElement} of the input {@link DiffModel} as a list. Doesn't take
	 * {@link DiffGroup}s into account.
	 * 
	 * @return The {@link DiffElement} of the input {@link DiffModel} as a list.
	 */
	public List<DiffElement> getDiffAsList() {
		if (diffList == null) {
			diffList = new ArrayList<DiffElement>();
			// ordering is needed in order to merge modelElement diffs before references change
			// We'll order the diffs by class (modelElementChange, attributechange then referenceChange)
			final List<ModelElementChange> modelElementDiffs = new ArrayList<ModelElementChange>();
			final List<AttributeChange> attributeChangeDiffs = new ArrayList<AttributeChange>();
			final List<ReferenceChange> referenceChangeDiffs = new ArrayList<ReferenceChange>();
			final TreeIterator<EObject> iterator = getDiff().eAllContents();
			while (iterator.hasNext()) {
				final DiffElement aDiff = (DiffElement)iterator.next();
				if (aDiff instanceof ModelElementChange)
					modelElementDiffs.add((ModelElementChange)aDiff);
				else if (aDiff instanceof AttributeChange)
					attributeChangeDiffs.add((AttributeChange)aDiff);
				else if (aDiff instanceof ReferenceChange)
					referenceChangeDiffs.add((ReferenceChange)aDiff);
				// fallthrough
				else if (!(aDiff instanceof DiffGroup))
					diffList.add(aDiff);
			}
			diffList.addAll(modelElementDiffs);
			diffList.addAll(attributeChangeDiffs);
			diffList.addAll(referenceChangeDiffs);
			modelElementDiffs.clear();
			attributeChangeDiffs.clear();
			referenceChangeDiffs.clear();
		}

		return diffList;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getImage()
	 */
	public Image getImage() {
		Image image = null;

		if (getMatch() != null)
			image = EMFCompareEObjectUtils.computeObjectImage(getMatch());
		else if (getDiff() != null)
			image = EMFCompareEObjectUtils.computeObjectImage(getDiff());

		return image;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getKind()
	 */
	public int getKind() {
		if (getAncestor() != null)
			return EMFCompareConstants.ENABLE_ANCESTOR;
		return EMFCompareConstants.NO_CHANGE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		ITypedElement left = null;
		if (leftResource != null) {
			if (leftResource.getContents().size() > 0)
				left = new TypedElementWrapper(leftResource.getContents().get(0));
		} else {
			// Seeks a resource from the MatchModel
			// Assumes that some elements have been matched
			final TreeIterator<EObject> matchIterator = match.eAllContents();
			EObject root = null;
			while (matchIterator.hasNext()) {
				final EObject matchElement = matchIterator.next();
				if (matchElement instanceof Match2Elements) {
					root = ((Match2Elements)matchElement).getLeftElement().eResource().getContents().get(0);
					break;
				}
			}
			left = new TypedElementWrapper(root);
		}
		return left;
	}

	/**
	 * Returns this ModelCompareInput's MatchModel.
	 * 
	 * @return This ModelCompareInput's MatchModel.
	 */
	public MatchModel getMatch() {
		return match;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getName()
	 */
	public String getName() {
		String name = null;

		if (getMatch() != null)
			name = EMFCompareEObjectUtils.computeObjectName(getMatch());
		else if (getDiff() != null)
			name = EMFCompareEObjectUtils.computeObjectName(getDiff());

		return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		ITypedElement right = null;
		if (rightResource != null) {
			if (rightResource.getContents().size() > 0)
				right = new TypedElementWrapper(rightResource.getContents().get(0));
		} else {
			// Seeks a resource from the MatchModel
			// Assumes that some elements have been matched
			final TreeIterator<EObject> matchIterator = match.eAllContents();
			EObject root = null;
			while (matchIterator.hasNext()) {
				final EObject matchElement = matchIterator.next();
				if (matchElement instanceof Match2Elements) {
					root = ((Match2Elements)matchElement).getRightElement().eResource().getContents().get(0);
					break;
				}
			}
			right = new TypedElementWrapper(root);
		}
		return right;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#removeCompareInputChangeListener(ICompareInputChangeListener)
	 */
	public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {
		inputChangeListeners.remove(listener);
	}

	/**
	 * Applies the changes implied by a given {@link DiffElement} in the direction specified by
	 * <code>leftToRight</code>.
	 * 
	 * @param element
	 *            {@link DiffElement} containing the copy information.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> otherwise.
	 */
	protected void doCopy(DiffElement element, boolean leftToRight) {
		MergeService.merge(element, leftToRight);
	}

	/**
	 * Applies the changes implied by a list of {@link DiffElement} in the direction specified by
	 * <code>leftToRight</code>.
	 * 
	 * @param elements
	 *            {@link DiffElement}s containing the copy information.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> otherwise.
	 */
	protected void doCopy(List<DiffElement> elements, boolean leftToRight) {
		MergeService.merge(elements, leftToRight);
	}

	/**
	 * Notifies all {@link ICompareInputChangeListener listeners} registered for this
	 * {@link ModelCompareInput input} that a change occured.
	 */
	protected void fireCompareInputChanged() {
		diffList.clear();
		diffList = null;
		for (ICompareInputChangeListener listener : inputChangeListeners) {
			listener.compareInputChanged(this);
		}
	}
}
