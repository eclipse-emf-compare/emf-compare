/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.sirius.tests.suite;

import org.eclipse.emf.compare.diagram.sirius.internal.TestBug561458;
import org.eclipse.emf.compare.diagram.sirius.internal.merge.TestBug561825;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class is used to run all the bugfix test classes of the
 * <code>org.eclipse.emf.compare.diagram.sirius.tests</code> project.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@RunWith(Suite.class)
@SuiteClasses({TestBug561458.class, TestBug561825.class })
public class BugsTestSuite {
}
