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
package org.eclipse.emf.compare.tests.command;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.impl.DualCompareCommandStack;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestDualCompareCommandStack extends AbstractTestCompareCommandStack {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.command.AbstractTestCompareCommandStack#createCommandStack()
	 */
	@Override
	protected ICompareCommandStack createCommandStack() {
		return new DualCompareCommandStack(new BasicCommandStack(), new BasicCommandStack());
	}

}
