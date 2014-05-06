/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

/**
 * Defines the scope for the resolution of a file's logical model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public enum CrossReferenceResolutionScope {
	/**
	 * Crawls the whole workspace for model files, loading them as EMF models in order to determine their
	 * outgoing dependencies.
	 */
	WORKSPACE,

	/**
	 * Crawls the project containing the file which dependencies we're resolving in order to determine their
	 * outgoing dependencies.
	 */
	PROJECT,

	/**
	 * Only crawl the container of the file which dependencies we're resolving.
	 */
	CONTAINER,

	/**
	 * Do not try to find files that depend on the file currently being resolved, only check for its own
	 * outgoing dependencies.
	 */
	OUTGOING,

	/**
	 * Do not try and resolve cross-referenced resources for this logical model.
	 */
	SELF;
}
