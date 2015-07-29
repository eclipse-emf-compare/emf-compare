/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.e4;

import org.eclipse.ui.progress.PendingUpdateAdapter;

/**
 * Extend class to make methods visible in this package.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class E4PendingUpdateAdapter extends PendingUpdateAdapter {
	@Override
	protected boolean isRemoved() {
		return super.isRemoved();
	}

	@Override
	protected void setRemoved(boolean removedValue) {
		super.setRemoved(removedValue);
	}
}
