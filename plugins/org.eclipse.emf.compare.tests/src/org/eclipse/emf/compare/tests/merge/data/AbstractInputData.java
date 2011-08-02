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
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class AbstractInputData {
	protected EObject loadFromClassloader(String string) throws IOException {
		InputStream str = this.getClass().getResourceAsStream(string);
		XMIResourceImpl res = new XMIResourceImpl(URI.createURI("http://" + string)); //$NON-NLS-1$
		res.load(str, Collections.EMPTY_MAP);
		return res.getContents().get(0);
	}
}
