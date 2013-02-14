/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.decoration;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;

/**
 * DecorationEditPolicy which forces the creation of decorators after each refresh.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiffDecorationEditPolicy extends DecorationEditPolicy {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy#refresh()
	 */
	@Override
	public void refresh() {
		decorators = null;
		super.refresh();
	}

}
