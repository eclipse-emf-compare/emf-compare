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
package org.eclipse.emf.compare.ide.internal.utils;

import org.eclipse.emf.common.util.URI;

/**
 * The listener interface for receiving namespace declaration events from the XML parsers.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface INamespaceDeclarationListener {
	/**
	 * Notified when a schema location is declared from the XMLHandler.
	 * 
	 * @param key
	 *            Key for this schema location mapping.
	 * @param uri
	 *            URI for this schema.
	 */
	void schemaLocationDeclared(String key, URI uri);
}
