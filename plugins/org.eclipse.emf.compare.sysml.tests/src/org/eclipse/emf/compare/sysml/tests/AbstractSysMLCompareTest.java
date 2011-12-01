/**
 *  Copyright (c) 2011 Atos.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.tests;

import org.eclipse.emf.compare.diff.service.DiffEngineRegistry;
import org.eclipse.emf.compare.sysml.diff.SysMLDiffEngine;
import org.eclipse.emf.compare.uml2.diff.test.AbstractUMLCompareTest;

/**
 * Extends {@link AbstractUMLCompareTest} for SysML tests.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 */
public abstract class AbstractSysMLCompareTest extends AbstractUMLCompareTest {

	@Override
	public void before() {
		super.before();
		DiffEngineRegistry.INSTANCE.putValue("uml", new SysMLDiffEngine()); //$NON-NLS-1$
	}

}
