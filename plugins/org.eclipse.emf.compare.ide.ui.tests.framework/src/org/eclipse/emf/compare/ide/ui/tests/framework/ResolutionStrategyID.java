/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.framework;

/**
 * The differents strategies than can be used to compute the ogical model.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public enum ResolutionStrategyID {

	/** Use workspace strategy to build logical model. */
	WORKSPACE,

	/** Use container strategy to build logical model. */
	CONTAINER,

	/** Use project strategy to build logical model. */
	PROJECT,

	/** Use outgoing strategy to build logical model. */
	OUTGOING;

}
