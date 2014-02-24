/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.extension;

import org.eclipse.emf.compare.EMFCompare.Builder;

/**
 * Engine provider for EMF Compare.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public interface IEMFCompareBuilderConfigurator {

	/**
	 * Configure a {@link org.eclipse.emf.compare.EMFCompare.EMFCompare.Builder} using engines from registry and
	 * user preferences.
	 * 
	 * @param builder
	 *            {@link org.eclipse.emf.compare.EMFCompare.EMFCompare.Builder}
	 */
	void configure(Builder builder);

}
