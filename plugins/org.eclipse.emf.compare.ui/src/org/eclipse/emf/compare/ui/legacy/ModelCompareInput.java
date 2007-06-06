/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.AttributeChange;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.ReferenceChange;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelCompareInput implements ICompareInput {
	private final List<ICompareInputChangeListener> listeners = new ArrayList<ICompareInputChangeListener>();

	private Object leftStorage;

	private MatchModel delta;

	private DiffModel diff;

	private Object rightStorage;

	/**
	 * @param deltas
	 */
	public ModelCompareInput(final MatchModel delta, final DiffModel diff) {
		this.delta = delta;
		this.diff = diff;
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#addCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void addCompareInputChangeListener(
			final ICompareInputChangeListener listener) {
		this.listeners.add(listener);

	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#copy(boolean)
	 */
	public void copy(final boolean leftToRight) {
		// if (leftToRight)
		// {
		// TreeIterator treeIter = getDelta().treeIterator();
		// while (treeIter.hasNext())
		// {
		// Delta child = (Delta) treeIter.next();
		// child.setComparedElt(child.getElt());
		// if (child.getKind() != DiffConstants.NO_CHANGE)
		// {
		// child.setKind(getDelta().getKind()|DiffConstants.RESOLVED);
		// }
		// }
		//			
		// }
		// else
		// {
		// TreeIterator treeIter = getDelta().treeIterator();
		// while (treeIter.hasNext())
		// {
		// Delta child = (Delta) treeIter.next();
		// child.setElt(child.getComparedElt());
		// if (child.getKind() != DiffConstants.NO_CHANGE)
		// {
		// child.setKind(getDelta().getKind()|DiffConstants.RESOLVED);
		// }
		// }
		// }
		// EMFComparePlugin.getDefault().log(getDelta().getElt() + " " +
		// getDelta().getComparedElt()); //REMOVE
		// TODOCBR merge support
		fireCompareInputChanged();

	}

	public DiffElement findDiffFromMatch(final Match2Elements match) {
		DiffElement result = null;
		final TreeIterator it = getDiff().eAllContents();
		while (it.hasNext()) {

			final DiffElement target = (DiffElement) it.next();
			if (target instanceof AttributeChange) {
				if (((AttributeChange) target).getLeftElement() == match
						.getLeftElement()) {
					result = target;
				}
				if (((AttributeChange) target).getRightElement() == match
						.getRightElement()) {
					result = target;
				}

			}
			if (target instanceof ReferenceChange) {
				if (((ReferenceChange) target).getLeftElement() == match
						.getLeftElement()) {
					result = target;
				}
				if (((ReferenceChange) target).getRightElement() == match
						.getRightElement()) {
					result = target;
				}
			}

		}
		return result;
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		// TODOCBR handle 3way diff
		// return new TypedElementWrapper(getDelta().getAncestorElt());
		return null;
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getImage()
	 */
	public Image getImage() {
		/*
		 * if (getDelta().getElt() != null) return
		 * DiffUtils.computeElementImage(getDelta().getElt()); if
		 * (getDelta().getComparedElt() != null) return
		 * DiffUtils.computeElementImage(getDelta().getComparedElt()); if
		 * (getDelta().getAncestorElt() != null) return
		 * DiffUtils.computeElementImage(getDelta().getAncestorElt());
		 */return null;
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getKind()
	 */
	public int getKind() {
		final int side = DiffConstants.NO_CHANGE;
		return side;
		// TreeIterator iter =
		// ((MatchElement)getDelta().getMatchedElements().get(0)).eAllContents();
		// while(iter.hasNext())
		// {
		// if (side == DiffConstants.CONFLICTING)
		// break;
		//			
		// MatchElement delta = (MatchElement) iter.next();
		// //TODOBR FIXMECBR
		// // if (side == DiffConstants.NO_CHANGE)
		// // side = delta.getKind();
		// // else
		// // {
		// // if (side != delta.getKind())
		// // {
		// // side = DiffConstants.CONFLICTING;
		// // }
		// // }
		// }

	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		return new TypedElementWrapper(((Match2Elements) getDelta()
				.getMatchedElements().get(0)).getLeftElement());
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getName()
	 */
	public String getName() {

		return "input Name -FIXME";
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		return new TypedElementWrapper(((Match2Elements) getDelta()
				.getMatchedElements().get(0)).getRightElement());
	}

	/**
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#removeCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void removeCompareInputChangeListener(
			final ICompareInputChangeListener listener) {
		this.listeners.remove(listener);

	}

	public MatchModel getDelta() {
		return this.delta;
	}

	public DiffModel getDiff() {
		return this.diff;
	}

	public boolean hasChildren() {
		return (getDelta() != null);
	}

	protected void fireCompareInputChanged() {
		for (final ICompareInputChangeListener listener : this.listeners) {
			listener.compareInputChanged(this);
		}
	}

	public Object getLeftStorage() {
		return this.leftStorage;
	}

	public void setLeftStorage(final Object leftStorage) {
		this.leftStorage = leftStorage;
	}

	public Object getRightStorage() {
		return this.rightStorage;
	}

	public void setRightStorage(final Object rightStorage) {
		this.rightStorage = rightStorage;
	}

}
