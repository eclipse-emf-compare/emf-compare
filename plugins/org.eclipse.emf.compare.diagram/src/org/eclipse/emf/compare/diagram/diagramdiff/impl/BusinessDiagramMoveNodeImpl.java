/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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
import java.util.List;

import org.eclipse.emf.compare.diagram.GMFCompare;
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode;
import org.eclipse.emf.compare.diagram.diff.util.DiagramCompareConstants;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Extension of {@link DiagramMoveNodeImpl}.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramMoveNodeImpl extends DiagramMoveNodeImpl implements BusinessDiagramMoveNode {

	/**
	 * The list of the hidden differences.
	 */
	private List<UpdateAttribute> updateAttributeLocationDiffs;

	/**
	 * The left node.
	 */
	private Node leftNode;

	/**
	 * The right node.
	 */
	private Node rightNode;

	/**
	 * Constructor.
	 */
	public BusinessDiagramMoveNodeImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl#getText()
	 */
	@Override
	public String getText() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl#getImage()
	 */
	@Override
	public Object getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl#provideMerger()
	 */
	@Override
	public IMerger provideMerger() {
		return null;
	}

	/**
	 * Set the hidden differences from the specified difference.
	 * 
	 * @param origin
	 *            The difference to scan.
	 */
	protected void initHiddenDiffElements(DiffElement origin) {
		updateAttributeLocationDiffs = findHiddenDiffElements(origin);
		getHideElements().addAll(updateAttributeLocationDiffs);
	}

	/**
	 * Set the left node from the hidden differences.
	 * 
	 * @param diffs
	 *            The hidden differences.
	 */
	protected void initLeftElement(List<UpdateAttribute> diffs) {
		if (diffs.size() > 0) {
			final EObject obj = diffs.get(0).getLeftElement().eContainer();
			if (obj instanceof Node) {
				leftNode = (Node)obj;
			}
			setLeftElement(leftNode);
		}
	}

	/**
	 * Set the right node from the hidden differences.
	 * 
	 * @param diffs
	 *            The hidden differences.
	 */
	protected void initRightElement(List<UpdateAttribute> diffs) {
		if (diffs.size() > 0) {
			final EObject obj = diffs.get(0).getRightElement().eContainer();
			if (obj instanceof Node) {
				rightNode = (Node)obj;
			}
			setRightElement(rightNode);
		}
	}

	/**
	 * Check if the difference {@link diff} is concerned by the creation of this kind of extension.
	 * 
	 * @param diff
	 *            The difference.
	 * @return True if {@link diff} is concerned.
	 */
	public static boolean isConcernedBy(DiffElement diff) {
		return diff instanceof DiffGroup && isNodeLocationComparison((DiffGroup)diff);
	}

	/**
	 * Retrieve the x location from the node.
	 * 
	 * @param node
	 *            The node.
	 * @return The x location.
	 */
	private static int getLocationX(Node node) {
		final LayoutConstraint lc = node.getLayoutConstraint();
		if (lc instanceof Bounds) {
			return ((Bounds)lc).getX();
		}
		return -1;
	}

	/**
	 * Retrieve the y location from the node.
	 * 
	 * @param node
	 *            The node.
	 * @return The y location.
	 */
	private static int getLocationY(Node node) {
		final LayoutConstraint lc = node.getLayoutConstraint();
		if (lc instanceof Bounds) {
			return ((Bounds)lc).getY();
		}
		return -1;
	}

	/**
	 * Check if the difference group {@link diff} is a node location comparison.
	 * 
	 * @param diff
	 *            The difference group
	 * @return True if it concerns the location comparison.
	 */
	private static boolean isNodeLocationComparison(DiffGroup diff) {
		for (DiffElement diffElement : DiffUtil.getSubDiffElements(diff)) {
			if (isNodeLocationComparison(diffElement)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the difference {@link diff} is a node location comparison.
	 * 
	 * @param diff
	 *            The difference
	 * @return True if it concerns the location comparison.
	 */
	private static boolean isNodeLocationComparison(DiffElement diff) {
		return diff instanceof UpdateAttribute
				&& ((UpdateAttribute)diff).getAttribute().eContainer()
						.equals(NotationPackage.eINSTANCE.getLocation())
				&& DiffUtil.eContainer(((UpdateAttribute)diff).getLeftElement(), Edge.class) == null
				&& isOverThreshold((UpdateAttribute)diff);
	}

	/**
	 * Check if the moving of the node is over the threshold (in pixels) specified in the emf compare
	 * preference page.
	 * 
	 * @param diff
	 *            The difference.
	 * @return True if it is over the threshold.
	 */
	private static boolean isOverThreshold(UpdateAttribute diff) {
		final EObject left = diff.getLeftElement();
		final EObject right = diff.getRightElement();
		if (left instanceof Bounds && right instanceof Bounds) {
			final int leftX = ((Bounds)left).getX();
			final int leftY = ((Bounds)left).getY();
			final int rightX = ((Bounds)right).getX();
			final int rightY = ((Bounds)right).getY();
			final int deltaX = Math.abs(leftX - rightX);
			final int deltaY = Math.abs(leftY - rightY);
			final int threshold = GMFCompare.getDefault().getPreferenceStore()
					.getInt(DiagramCompareConstants.PREFERENCES_KEY_MOVE_THRESHOLD);
			return deltaX + deltaY > threshold;
		}
		return false;
	}

	/**
	 * Retrieve the hidden differences from the specified difference.
	 * 
	 * @param origin
	 *            The difference.
	 * @return The hidden differences.
	 */
	private List<UpdateAttribute> findHiddenDiffElements(DiffElement origin) {
		final List<UpdateAttribute> result = new ArrayList<UpdateAttribute>();
		for (DiffElement diffElt : DiffUtil.getSubDiffElements(origin)) {
			if (isNodeLocationComparison(diffElt)) {
				result.add((UpdateAttribute)diffElt);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode#getUpdateAttributeLocationDiffs()
	 */
	public List<UpdateAttribute> getUpdateAttributeLocationDiffs() {
		return updateAttributeLocationDiffs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode#getLeftNode()
	 */
	public Node getLeftNode() {
		return leftNode;
	}

	public Node getRightNode() {
		return rightNode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode#getLeftLocationX()
	 */
	public int getLeftLocationX() {
		final Node node = getLeftNode();
		return getLocationX(node);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode#getLeftLocationY()
	 */
	public int getLeftLocationY() {
		final Node node = getLeftNode();
		return getLocationY(node);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode#getRightLocationX()
	 */
	public int getRightLocationX() {
		final Node node = getRightNode();
		return getLocationX(node);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode#getRightLocationY()
	 */
	public int getRightLocationY() {
		final Node node = getRightNode();
		return getLocationY(node);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#init(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer,
	 *      org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	public void init(DiffElement origin, CrossReferencer crossReferencer, MatchModel match) {
		if (origin instanceof DiffGroup) {
			initHiddenDiffElements(origin);
			getRequires().addAll(getHideElements());
			initLeftElement(updateAttributeLocationDiffs);
			initRightElement(updateAttributeLocationDiffs);
			setRemote(DiffUtil.isRemote(getHideElements()));
		}
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
