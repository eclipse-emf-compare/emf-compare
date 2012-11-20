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
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramIDEMatchAccessorImpl implements IDiagramNodeAccessor, ITypedElement, IStreamContentAccessor {

	private Match fMatch;

	private MergeViewerSide fSide;

	protected Comparison fComparison;

	/**
	 * @param adapterFactory
	 * @param eObject
	 */
	public DiagramIDEMatchAccessorImpl(Match match, MergeViewerSide side) {
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
		//					EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute")); //$NON-NLS-1$
		// } else {
		// return ExtendedImageRegistry.getInstance().getImage(
		//					EcoreEditPlugin.getPlugin().getImage("full/obj16/EReference")); //$NON-NLS-1$
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
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		/*
		 * #293926 : Whatever we return has no importance as long as it is not "null", this is only to make
		 * CompareUIPlugin#guessType happy. However, it is only happy if what we return resembles a text. Note
		 * that this bug has been fixed in 3.7.1, we're keeping this around for the compatibility with 3.5 and
		 * 3.6.
		 */
		return new ByteArrayInputStream(new byte[] {' ' });
	}

	public EObject getEObject(MergeViewerSide side) {
		return getEObject(fMatch, side);
	}

	protected EObject getEObject(Match match, MergeViewerSide side) {
		switch (side) {
			case LEFT:
				return match.getLeft();
			case RIGHT:
				return match.getRight();
			case ANCESTOR:
				return match.getOrigin();
			default:
				break;
		}
		return null;
	}

	public Diagram getDiagram(MergeViewerSide side) {
		EObject obj = getEObject(side);
		return getDiagram(obj);
	}

	public Diagram getOwnedDiagram() {
		return getDiagram(fSide);
	}

	public View getOwnedView() {
		View result = (View)getEObject(fSide);
		if (result == null) {
			result = getDiagram(fSide);
		}
		return result;
	}

	protected Diagram getDiagram(EObject obj) {
		if (obj instanceof Diagram) {
			return (Diagram)obj;
		} else if (obj instanceof View) {
			return ((View)obj).getDiagram();
		}
		return null;
	}

	public Comparison getComparison() {
		return fComparison;
	}

}
