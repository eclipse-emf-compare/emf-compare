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

import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class EDiffContainer extends EDiffElement implements IDiffContainer {

	/**
	 * @param adapterFactory
	 */
	public EDiffContainer(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#hasChildren()
	 */
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#getChildren()
	 */
	public IDiffElement[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#add(org.eclipse.compare.structuremergeviewer.IDiffElement)
	 */
	public void add(IDiffElement child) {
		// TODO Auto-generated method stub

	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#removeToRoot(org.eclipse.compare.structuremergeviewer.IDiffElement)
	 */
	public void removeToRoot(IDiffElement child) {
		// TODO Auto-generated method stub

	}

}
