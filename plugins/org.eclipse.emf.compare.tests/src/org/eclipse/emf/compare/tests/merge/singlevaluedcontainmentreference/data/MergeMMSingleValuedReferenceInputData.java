/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.merge.data.AbstractInputData;
import org.eclipse.emf.ecore.EObject;

@SuppressWarnings("nls")
public class MergeMMSingleValuedReferenceInputData extends AbstractInputData {
	public EObject getNoChild() throws IOException {
		String path = "noChildSingleContainment.merge";
		return loadFromClassloader(path);
	}

	public EObject getChild() throws IOException {
		String path = "childSingleContainment.merge";
		return loadFromClassloader(path);
	}

	public EObject getChangedChild() throws IOException {
		String path = "changedChildSingleContainment.merge";
		return loadFromClassloader(path);
	}
}
