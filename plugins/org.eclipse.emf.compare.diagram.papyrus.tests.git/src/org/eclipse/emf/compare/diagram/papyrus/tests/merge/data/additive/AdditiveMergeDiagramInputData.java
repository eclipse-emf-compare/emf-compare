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
package org.eclipse.emf.compare.diagram.papyrus.tests.merge.data.additive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

@SuppressWarnings({"nls" })
public class AdditiveMergeDiagramInputData extends DiagramInputData {

	public ResourceSet getUmlDiagramNoConflictsResult() throws IOException {
		List<String> paths = new ArrayList<String>();
		paths.add("noConflicts/result/model.notation");
		paths.add("noConflicts/result/model.uml");
		paths.add("noConflicts/result/model.di");
		return loadFromClassLoader(paths, new ResourceSetImpl());
	}

	public ResourceSet getUmlDiagramConflictsTest1Result1() throws IOException {
		List<String> paths = new ArrayList<String>();
		paths.add("conflicts/test1/result1/model.notation");
		paths.add("conflicts/test1/result1/model.uml");
		paths.add("conflicts/test1/result1/model.di");
		return loadFromClassLoader(paths, new ResourceSetImpl());
	}

	public ResourceSet getUmlDiagramConflictsTest1Result2() throws IOException {
		List<String> paths = new ArrayList<String>();
		paths.add("conflicts/test1/result2/model.notation");
		paths.add("conflicts/test1/result2/model.uml");
		paths.add("conflicts/test1/result2/model.di");
		return loadFromClassLoader(paths, new ResourceSetImpl());
	}

	public ResourceSet getUmlDiagramConflictsTest2Result() throws IOException {
		List<String> paths = new ArrayList<String>();
		paths.add("conflicts/test2/result/model.notation");
		paths.add("conflicts/test2/result/model.uml");
		paths.add("conflicts/test2/result/model.di");
		return loadFromClassLoader(paths, new ResourceSetImpl());
	}

	public ResourceSet getUmlDiagramRACResult() throws IOException {
		List<String> paths = new ArrayList<String>();
		paths.add("control/result/model.notation");
		paths.add("control/result/model.uml");
		paths.add("control/result/model.di");
		paths.add("control/result/wired.notation");
		paths.add("control/result/wired.uml");
		paths.add("control/result/wired.di");
		paths.add("control/result/wave.notation");
		paths.add("control/result/wave.uml");
		paths.add("control/result/wave.di");
		return loadFromClassLoader(paths, new ResourceSetImpl());
	}

}
