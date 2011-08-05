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
package org.eclipse.emf.compare.tests.merge.singlevaluedreference.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.merge.data.AbstractInputData;
import org.eclipse.emf.ecore.EObject;

@SuppressWarnings("nls")
public class MergeMMSingleValuedReferenceInputData extends AbstractInputData {
	public EObject getNoValue() throws IOException {
		String path = "noValue.merge";
		return loadFromClassloader(path);
	}

	public EObject getValue() throws IOException {
		String path = "value.merge";
		return loadFromClassloader(path);
	}

	public EObject getChangedValue() throws IOException {
		String path = "changedValue.merge";
		return loadFromClassloader(path);
	}

	public EObject getValueProxy() throws IOException {
		String path = "valueProxy.merge";
		return loadFromClassloader(path);
	}

	public EObject getChangedValueProxy() throws IOException {
		String path = "changedValueProxy.merge";
		return loadFromClassloader(path);
	}
}
