/*******************************************************************************
 * Copyright (c) 2014, 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * A {@link ResourceSet} that offers a dispose method.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public interface DisposableResourceSet extends ResourceSet {

	/** This constant is used to resolve Xtext resources. */
	public final String XTEXT_SCOPING_LIVE_SCOPE_OPTION = "org.eclipse.xtext.scoping.LIVE_SCOPE"; //$NON-NLS-1$

	/**
	 * This method should be called when the resource set is no longer needed.
	 */
	void dispose();

}
