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
package org.eclipse.emf.compare.ide.internal.utils;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * A {@link ResourceSet} that offers a dispose method.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public interface DisposableResourceSet extends ResourceSet {

	/**
	 * This method should be called when the resource set is no longer needed.
	 */
	void dispose();

}
