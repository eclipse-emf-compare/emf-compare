/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.postprocessor;

import java.util.regex.Pattern;

/**
 * Abstract implementation of a post processor. It handles nsURI and resourceURI patterns as final fields.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractPostProcessor implements IPostProcessor {

	/** Namespace URIs (regex) this processor applies to. */
	private final Pattern nsUri;

	/** Resource URIs (regex) this processor applies to. */
	private final Pattern resourceUri;

	/**
	 * Constructor.
	 */
	public AbstractPostProcessor(Pattern nsURI, Pattern resourceURI) {
		nsUri = nsURI;
		resourceUri = resourceURI;
	}

	/**
	 * Returns the namespace URI.
	 * 
	 * @return The namespace URI.
	 */
	public Pattern getNsURI() {
		return nsUri;
	}

	/**
	 * Returns the resource URI.
	 * 
	 * @return The resource URI.
	 */
	public Pattern getResourceURI() {
		return resourceUri;
	}

}
