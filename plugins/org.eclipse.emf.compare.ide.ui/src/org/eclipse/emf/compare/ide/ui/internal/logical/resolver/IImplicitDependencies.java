/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * This interface describes the contract of providing the set of files that must be used "together with" a
 * given file, whatever the desired semantics of "together with" is. It is used in EMFCompare to get the files
 * that are part of the same logical model when it's not possible to guess that by looking at the actual
 * dependencies (via EReferences).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IImplicitDependencies {

	/**
	 * Provides a set continaing the given URI plus its implicit dependencies (those that cannot be found by
	 * instpecting the cross-references of the resource represented by the given URI).
	 * 
	 * @param uri
	 *            URI of a model resource for which we want the set of related dependencies
	 * @param uriConverter
	 *            URI Converter to use
	 * @return Must return a Set that's never null nor empty, that contains at least the given uri, plus its
	 *         implicit dependencies if it has any..
	 */
	Set<URI> of(URI uri, URIConverter uriConverter);
}
