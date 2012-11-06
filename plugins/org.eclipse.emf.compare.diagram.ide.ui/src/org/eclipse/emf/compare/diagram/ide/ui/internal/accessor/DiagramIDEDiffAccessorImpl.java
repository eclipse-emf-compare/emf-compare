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
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramIDEDiffAccessorImpl implements IDiagramDiffAccessor, ITypedElement, IStreamContentAccessor {

	private DiagramDiff fDiff;

	private MergeViewerSide fSide;

	private Comparison fComparison;

	private Match fOwnerMatch;

	/**
	 * @param diff
	 * @param side
	 */
	public DiagramIDEDiffAccessorImpl(DiagramDiff diff, MergeViewerSide side) {
		this.fDiff = diff;
		this.fSide = side;
		this.fOwnerMatch = fDiff.getMatch();
		this.fComparison = fOwnerMatch.getComparison();
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
		return DiagramContentMergeViewerConstants.DIFF_NODE_TYPE;
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

	public EObject getEObject() {
		if (fDiff instanceof DiagramDiff) {
			return ((DiagramDiff)fDiff).getView();
		}
		return null;
	}

	public EObject getEObject(MergeViewerSide side) {
		EObject obj = getEObject();
		Match eObjectMatch = fComparison.getMatch(obj);
		if (obj instanceof View) {
			switch (side) {
				case LEFT:
					return eObjectMatch.getLeft();
				case RIGHT:
					return eObjectMatch.getRight();
				case ANCESTOR:
					return eObjectMatch.getOrigin();
				default:
					break;
			}
		}
		return null;
	}

	public Comparison getComparison() {
		return fComparison;
	}

	public DiagramDiff getDiff() {
		return fDiff;
	}

	public Diagram getDiagram(MergeViewerSide side) {
		EObject obj = getEObject(side);
		if (obj != null) {
			return getDiagram(obj);
		} else {
			Diagram diagram = getDiagram();
			if (diagram != null) {
				Match diagramMatch = fComparison.getMatch(diagram);
				switch (side) {
					case LEFT:
						return (Diagram)diagramMatch.getLeft();
					case RIGHT:
						return (Diagram)diagramMatch.getRight();
					case ANCESTOR:
						return (Diagram)diagramMatch.getOrigin();
					default:
						break;
				}
			}
		}
		return null;
	}

	public Diagram getDiagram() {
		EObject obj = getEObject();
		return getDiagram(obj);
	}

	private Diagram getDiagram(EObject obj) {
		if (obj instanceof Diagram) {
			return (Diagram)obj;
		} else if (obj instanceof View) {
			return ((View)obj).getDiagram();
		}
		return null;
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

}
