/**
 * Copyright (c) 2011, 2012 Obeo.
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
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class AbstractInputData {
	protected EObject loadFromClassloader(String string) throws IOException {
		final URL fileURL = FileLocator.toFileURL(getClass().getResource(string));
		final InputStream str = fileURL.openStream();
		final Resource res = ModelUtils.createResource(URI.createURI(fileURL.toString()));
		res.load(str, Collections.emptyMap());
		str.close();
		return res.getContents().get(0);
	}
}
