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
package org.eclipse.emf.compare.ide.ui.tests.unit;

import org.eclipse.emf.compare.ide.ui.tests.automation.CompareBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;

public class EcoreComparisonTest extends SWTBotTestCase {

	private CompareBot bot = new CompareBot();

	public void testCompareWithEachOther() {
		bot.closeWelcome();
		bot.importTestProjecstInWorkspace("EcoreComparison");
		bot.compareWithEachOther("EcoreComparison", "comparev1.ecore", "comparev2.ecore");
		// bot.createGITRepository("UMLComparison");

		// bot.commit("UMLComparison", "v1Refv2.uml");
	}

}