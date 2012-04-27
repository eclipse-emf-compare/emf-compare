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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchResourceNode extends AbstractEDiffNode {

	/**
	 * @param adapterFactory
	 */
	public MatchResourceNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public MatchResource getTarget() {
		return (MatchResource)super.getTarget();
	}
}
