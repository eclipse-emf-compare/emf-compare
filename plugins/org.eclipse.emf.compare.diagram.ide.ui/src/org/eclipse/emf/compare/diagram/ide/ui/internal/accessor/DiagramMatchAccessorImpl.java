/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;

/**
 * Input of each diagram merge viewer when a match is selected.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramMatchAccessorImpl implements IDiagramNodeAccessor, ITypedElement {

	/** The comparison. */
	protected Comparison fComparison;

	/** The related match object. */
	private Match fMatch;

	/** The side to impact. */
	private MergeViewerSide fSide;

	/**
	 * Constructor.
	 * 
	 * @param match
	 *            The related match object.
	 * @param side
	 *            The side to impact.
	 */
	public DiagramMatchAccessorImpl(Match match, MergeViewerSide side) {
		this.fMatch = match;
		this.fSide = side;
		fComparison = match.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		// if (getStructuralFeature() instanceof EAttribute) {
		// return ExtendedImageRegistry.getInstance().getImage(
		// EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute")); //$NON-NLS-1$
		// } else {
		// return ExtendedImageRegistry.getInstance().getImage(
		// EcoreEditPlugin.getPlugin().getImage("full/obj16/EReference")); //$NON-NLS-1$
		// }
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return DiagramContentMergeViewerConstants.MATCH_NODE_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getEObject(org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	public EObject getEObject(MergeViewerSide side) {
		return getEObject(fMatch, side);
	}

	/**
	 * Get the object from the given side.
	 * 
	 * @param match
	 *            The match of the expected object.
	 * @param side
	 *            The side to look for.
	 * @return The found object.
	 */
	protected EObject getEObject(Match match, MergeViewerSide side) {
		EObject result = null;
		switch (side) {
			case LEFT:
				result = match.getLeft();
				break;
			case RIGHT:
				result = match.getRight();
				break;
			case ANCESTOR:
				result = match.getOrigin();
				break;
			default:
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getDiagram(org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	public Diagram getDiagram(MergeViewerSide side) {
		Diagram diagram = null;
		EObject obj = getEObject(side);
		if (obj != null) {
			diagram = getDiagram(obj);
		} else {
			for (MergeViewerSide oppositeSide : getOtherSides(side)) {
				obj = getEObject(oppositeSide);
				if (obj != null) {
					diagram = getDiagram(obj);
					if (diagram != null) {
						Match diagramMatch = fComparison.getMatch(diagram);
						diagram = (Diagram)getEObject(diagramMatch, side);
						break;
					}
				}
			}
		}
		return diagram;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getOwnedDiagram()
	 */
	public Diagram getOwnedDiagram() {
		return getDiagram(fSide);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getOwnedView()
	 */
	public View getOwnedView() {
		EObject result = getEObject(fSide);
		if (result == null || !(result instanceof View)) {
			result = getDiagram(fSide);
		}
		return (View)result;
	}

	/**
	 * Get the related diagram from the given object.
	 * 
	 * @param obj
	 *            The object.
	 * @return The diagram.
	 */
	protected Diagram getDiagram(EObject obj) {
		Diagram diagram = null;
		if (obj instanceof Diagram) {
			diagram = (Diagram)obj;
		} else if (obj instanceof View) {
			diagram = ((View)obj).getDiagram();
		}
		return diagram;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getComparison()
	 */
	public Comparison getComparison() {
		return fComparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getOriginSide()
	 */
	public MergeViewerSide getOriginSide() {
		EObject origin = getEObject(MergeViewerSide.ANCESTOR);
		if (origin == null) {
			return fSide.opposite();
		} else {
			return MergeViewerSide.ANCESTOR;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getSide()
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor#getAllDiffs()
	 */
	public List<Diff> getAllDiffs() {
		return fComparison.getDifferences();
	}

	/**
	 * Get the opposite sides of the given one.
	 * 
	 * @param side
	 *            The given side.
	 * @return the opposite ones.
	 */
	private List<MergeViewerSide> getOtherSides(MergeViewerSide side) {
		List<MergeViewerSide> ret = null;
		switch (side) {
			case ANCESTOR:
				ret = Lists.newArrayList(MergeViewerSide.LEFT, MergeViewerSide.RIGHT);
				break;
			case LEFT:
				ret = Lists.newArrayList(MergeViewerSide.RIGHT, MergeViewerSide.ANCESTOR);
				break;
			case RIGHT:
				ret = Lists.newArrayList(MergeViewerSide.LEFT, MergeViewerSide.ANCESTOR);
				break;
			default:
				ret = Lists.newArrayList();
		}
		return ret;
	}

}
