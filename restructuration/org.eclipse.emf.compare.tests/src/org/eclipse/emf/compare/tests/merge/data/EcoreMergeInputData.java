/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;

public class EcoreMergeInputData extends AbstractInputData {

	public EObject getA() throws IOException {
		String path = "a.ecore";
		return loadFromClassloader(path);
	}

	public EObject getB() throws IOException {
		return loadFromClassloader("b.ecore");
	}

	public EObject getC() throws IOException {
		return loadFromClassloader("c.ecore");
	}

	public EObject getD() throws IOException {
		return loadFromClassloader("d.ecore");
	}

	public EObject getE() throws IOException {
		return loadFromClassloader("e.ecore");
	}

	public EObject getF() throws IOException {
		return loadFromClassloader("f_orderchange.ecore");
	}

	public EObject getG() throws IOException {
		return loadFromClassloader("g_move.ecore");
	}

	public EObject getH() throws IOException {
		return loadFromClassloader("h_delete.ecore");
	}

	public EObject getUML171() throws IOException {
		return loadFromClassloader("UML171.ecore");
	}

	public EObject getUML172() throws IOException {
		return loadFromClassloader("UML172.ecore");
	}

	public EObject getUML173() throws IOException {
		return loadFromClassloader("UML173.ecore");
	}
}
