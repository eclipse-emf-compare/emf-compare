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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class EDiffNode extends EDiffContainer implements ICompareInput {

	/**
	 * @param adapterFactory
	 */
	public EDiffNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#addCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void addCompareInputChangeListener(ICompareInputChangeListener listener) {

	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#removeCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {

	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#copy(boolean)
	 */
	public void copy(boolean leftToRight) {
		// TODO Auto-generated method stub

	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		return null;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		return null;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		return null;
	}
}
