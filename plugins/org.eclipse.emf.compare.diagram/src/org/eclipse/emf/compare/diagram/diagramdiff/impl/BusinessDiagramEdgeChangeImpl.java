/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diagramdiff.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil.Side;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Extension of {@link DiagramEdgeChangeImpl}.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramEdgeChangeImpl extends DiagramEdgeChangeImpl implements BusinessDiagramEdgeChange {

	/**
	 * The left edge.
	 */
	private Edge leftEdge;

	/**
	 * The right edge.
	 */
	private Edge rightEdge;

	/**
	 * The related eattribute.
	 */
	private EAttribute attribute;

	/**
	 * Constructor.
	 */
	public BusinessDiagramEdgeChangeImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl#getText()
	 */
	@Override
	public String getText() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl#getImage()
	 */
	@Override
	public Object getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl#provideMerger()
	 */
	@Override
	public IMerger provideMerger() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#init(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer,
	 *      org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	public void init(DiffElement origin, EcoreUtil.CrossReferencer crossReferencer, MatchModel match) {

		setHiddenDifferences(origin, crossReferencer);

		getRequires().addAll(getHideElements());

		if (leftEdge == null) {
			leftEdge = DiffUtil.getElement(origin, Side.LEFT, Edge.class);
			setLeftElement(leftEdge);
		}

		if (rightEdge == null) {
			rightEdge = DiffUtil.getElement(origin, Side.RIGHT, Edge.class);
			setRightElement(rightEdge);
		}

		if (origin instanceof UpdateAttribute && attribute == null) {
			attribute = ((UpdateAttribute)origin).getAttribute();
		}

		setRemote(DiffUtil.isRemote(getHideElements()));

		registerMyself(crossReferencer);
	}

	/**
	 * Register the extension in the cross referencer.
	 * 
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	private void registerMyself(EcoreUtil.CrossReferencer crossReferencer) {
		final Setting leftSetting = eSetting(DiffPackage.eINSTANCE.getUpdateModelElement_LeftElement());
		final Setting rightSetting = eSetting(DiffPackage.eINSTANCE.getUpdateModelElement_RightElement());

		final Collection<Setting> values1 = new ArrayList<Setting>();
		values1.add(leftSetting);
		crossReferencer.put(leftEdge, values1);
		final Collection<Setting> values2 = new ArrayList<Setting>();
		values2.add(rightSetting);
		crossReferencer.put(rightEdge, values2);
	}

	/**
	 * Set the elements hidden by this extension.
	 * 
	 * @param origin
	 *            The input difference element.
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	private void setHiddenDifferences(DiffElement origin, EcoreUtil.CrossReferencer crossReferencer) {

		final Edge lleftEdge = DiffUtil.getElement(origin, Side.LEFT, Edge.class);
		final Set<DiffElement> hiddenDiffsFromLeft = getAllRelatedEdgeChangesFrom(lleftEdge, crossReferencer);

		final Edge lrightEdge = DiffUtil.getElement(origin, Side.RIGHT, Edge.class);
		hiddenDiffsFromLeft.addAll(getAllRelatedEdgeChangesFrom(lrightEdge, crossReferencer));

		getHideElements().addAll(new ArrayList<DiffElement>(hiddenDiffsFromLeft));
	}

	/**
	 * From the specified view, it returns the set of the difference elements related to a diagram edge
	 * change.
	 * 
	 * @param view
	 *            The view.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return List of {@link DiffElement}
	 */
	private Set<DiffElement> getAllRelatedEdgeChangesFrom(View view, EcoreUtil.CrossReferencer crossReferencer) {
		final Set<DiffElement> result = new HashSet<DiffElement>();
		if (view != null && crossReferencer != null) {
			final Iterator<EObject> it = view.eAllContents();
			while (it.hasNext()) {
				final EObject prop = it.next();
				final Collection<Setting> diffs = crossReferencer.get(prop);
				if (diffs != null) {
					for (Setting setting : diffs) {
						final EObject obj = setting.getEObject();
						if (obj instanceof DiffElement) {
							if (isLayoutEdgeComparison((DiffElement)obj)) {
								result.add((DiffElement)obj);
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramEdgeChange#getLeftEdge()
	 */
	public Edge getLeftEdge() {
		return leftEdge;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramEdgeChange#getRightEdge()
	 */
	public Edge getRightEdge() {
		return rightEdge;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramEdgeChange#getAttribute()
	 */
	public EAttribute getAttribute() {
		return attribute;
	}

	/**
	 * Check if the difference {@link diff} is concerned by the creation of this kind of extension.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return True if {@link diff} is concerned.
	 */
	public static boolean isConcernedBy(DiffElement diff, EcoreUtil.CrossReferencer crossReferencer) {
		if (isLayoutEdgeComparison(diff)) {
			return !isDiagramEdgeChangeRegistered(diff, crossReferencer);
		}
		return false;

	}

	/**
	 * Checks if a {@link BusinessDiagramEdgeChange} is already registered in the specified cross referencer
	 * for the element referenced by the input difference.
	 * 
	 * @param diff
	 *            The input difference.
	 * @param crossReferencer
	 *            the cross referencer.
	 * @return True if registered.
	 */
	private static boolean isDiagramEdgeChangeRegistered(DiffElement diff,
			EcoreUtil.CrossReferencer crossReferencer) {
		final Edge edge = DiffUtil.getElement(diff, Side.ANY, Edge.class);
		final Collection<Setting> diffs = crossReferencer.get(edge);
		if (diffs != null) {
			for (Setting setting : diffs) {
				final EObject obj = setting.getEObject();
				if (obj instanceof DiagramEdgeChange) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if the difference {@link diff} is about a layout edge comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns a layout edge comparison.
	 */
	private static boolean isLayoutEdgeComparison(DiffElement diff) {
		return isBendpointsComparison(diff) || isAnchorsComparison(diff);
	}

	/**
	 * Check if the difference {@link diff} is a bend points comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns a bend points comparison.
	 */
	private static boolean isBendpointsComparison(DiffElement diff) {
		return diff instanceof UpdateAttribute && isBendpointsComparison((UpdateAttribute)diff);
	}

	/**
	 * Check if the difference {@link diff} is a bend points comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns a bend points comparison.
	 */
	private static boolean isBendpointsComparison(UpdateAttribute diff) {
		return diff.getAttribute().eContainer().equals(NotationPackage.eINSTANCE.getRelativeBendpoints());
	}

	/**
	 * Check if the difference {@link diff} is an anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns an anchor comparison.
	 */
	private static boolean isAnchorsComparison(DiffElement diff) {
		return isAnchorAdded(diff) || isAnchorChanged(diff);
	}

	/**
	 * Check if the difference {@link diff} is an anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns an anchor comparison.
	 */
	private static boolean isAnchorsComparison(UpdateAttribute diff) {
		return diff.getAttribute().equals(NotationPackage.eINSTANCE.getIdentityAnchor_Id());
	}

	/**
	 * Check if the difference {@link diff} is an anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns an anchor comparison.
	 */
	private static boolean isAnchorsComparison(ModelElementChange diff) {
		boolean result = false;
		if (diff instanceof ModelElementChangeLeftTarget) {
			result = isAnchorsComparison((ModelElementChangeLeftTarget)diff);
		} else if (diff instanceof ModelElementChangeRightTarget) {
			result = isAnchorsComparison((ModelElementChangeRightTarget)diff);
		}
		return result;
	}

	/**
	 * Check if the difference {@link diff} is an anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns an anchor comparison.
	 */
	private static boolean isAnchorsComparison(ModelElementChangeLeftTarget diff) {
		return diff.getLeftElement() instanceof IdentityAnchor;
	}

	/**
	 * Check if the difference {@link diff} is an anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns an anchor comparison.
	 */
	private static boolean isAnchorsComparison(ModelElementChangeRightTarget diff) {
		return diff.getRightElement() instanceof IdentityAnchor;
	}

	/**
	 * Check if the difference {@link diff} is an add of anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns the add of an anchor comparison.
	 */
	private static boolean isAnchorAdded(DiffElement diff) {
		return diff instanceof ModelElementChange && isAnchorsComparison((ModelElementChange)diff);
	}

	/**
	 * Check if the difference {@link diff} is a change of anchor comparison.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @return True if it concerns the change of an anchor comparison.
	 */
	private static boolean isAnchorChanged(DiffElement diff) {
		return diff instanceof UpdateAttribute && isAnchorsComparison((UpdateAttribute)diff);
	}

	/**
	 * Retrieve the object of the specified {@link side} related to the difference {@link diff}.
	 * 
	 * @param diff
	 *            The difference to scan.
	 * @param side
	 *            The side to scan.
	 * @return The object.
	 */
	public static EObject getElement(DiffElement diff, Side side) {
		if (diff instanceof BusinessDiagramEdgeChange) {
			return ((BusinessDiagramEdgeChange)diff).getLeftElement();
		}
		return DiffUtil.getElement(diff, side, Edge.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#getElement()
	 */
	public EObject getElement() {
		return getLeftElement();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#getRightResource()
	 */
	public Resource getRightResource() {
		return getRightElement().eResource();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#getLeftResource()
	 */
	public Resource getLeftResource() {
		return getLeftElement().eResource();
	}

}
