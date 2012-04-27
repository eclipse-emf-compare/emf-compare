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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ReferenceChange;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeNode extends DiffNode {

	/**
	 * @param adapterFactory
	 */
	public ReferenceChangeNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider.DiffNode#getTarget()
	 */
	@Override
	public ReferenceChange getTarget() {
		return (ReferenceChange)super.getTarget();
	}

}
