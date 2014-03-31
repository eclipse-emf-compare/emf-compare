/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.graphics.Image;

/**
 * A compare input whose purpose is to support a comparison with no differences.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class NoDifferencesCompareInput implements ICompareInput {

	/** Listeners associated with this compare input. */
	private final ListenerList listeners = new ListenerList(ListenerList.IDENTITY);

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "NoDifferencesCompareInput"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getKind() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypedElement getAncestor() {
		return new NoDifferencesTypedElement();
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypedElement getLeft() {
		return new NoDifferencesTypedElement();
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypedElement getRight() {
		return new NoDifferencesTypedElement();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCompareInputChangeListener(ICompareInputChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void copy(boolean leftToRight) {
		// Nothing to do.
	}
}
