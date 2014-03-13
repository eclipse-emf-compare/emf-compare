/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.collect.ForwardingObject;

import org.eclipse.core.runtime.IAdaptable;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class ForwardingAdaptable extends ForwardingObject implements IAdaptable {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	// Suppressing warning : super.getAdapter() is raw
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return delegate().getAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.collect.ForwardingObject#delegate()
	 */
	@Override
	protected abstract IAdaptable delegate();

}
