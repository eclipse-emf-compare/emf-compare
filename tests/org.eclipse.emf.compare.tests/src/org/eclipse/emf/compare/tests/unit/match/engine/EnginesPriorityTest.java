/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.engine;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.match.service.MatchService;

/**
 * Test the engine contribution priority handling.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class EnginesPriorityTest extends TestCase {
	/**
	 * Test the engine choosing with file extension.
	 * 
	 * @throws ComparisonFailure
	 *             Thrown when an assertEquals for String has failed.
	 */
	public void testEngineFileExtension() throws ComparisonFailure {
		IMatchEngine engine = null;
		engine = MatchService.getBestMatchEngine("a"); //$NON-NLS-1$
		assertEquals("AEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
		engine = MatchService.getBestMatchEngine("b"); //$NON-NLS-1$
		assertEquals("BEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
	}

	/**
	 * Test the engine picking with priority.
	 * 
	 * @throws ComparisonFailure
	 *             Thrown when an assertEquals for String has failed.
	 */
	public void testEnginePriority() throws ComparisonFailure {
		IMatchEngine engine = null;
		engine = MatchService.getBestMatchEngine("c"); //$NON-NLS-1$
		assertEquals("CHighEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
	}
}
