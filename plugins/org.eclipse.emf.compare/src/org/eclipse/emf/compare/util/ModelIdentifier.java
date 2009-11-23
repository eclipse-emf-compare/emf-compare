/**
 * Copyright (c) 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Store the models' common identification elements used to retrieve the comparison engines.
 * 
 * @author <a href="mailto:gonzague.reydet@obeo.fr">Gonzague Reydet</a>
 * @since 1.1
 */
public class ModelIdentifier {
	/** Default extension for EObjects not attached to a resource. */
	private static final String DEFAULT_EXTENSION = "ecore"; //$NON-NLS-1$

	/** Common namespace of the models. */
	private final String namespace;

	/** Common content-type of the models. */
	private final String contentType;

	/** Common extension of the model. */
	private final String extension;

	/**
	 * Default constructor.
	 * 
	 * @param resources
	 *            Resources for which we need to identify an engine.
	 */
	public ModelIdentifier(Resource... resources) {
		final List<URI> uris = new ArrayList<URI>();
		for (int i = 0; i < resources.length; i++) {
			if (resources[i] != null) {
				uris.add(resources[i].getURI());
			}
		}
		final URI[] uriArray = uris.toArray(new URI[uris.size()]);

		namespace = ModelUtils.getCommonNamespace(resources);

		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			contentType = EclipseModelUtils.getCommonContentType(uriArray);
		} else {
			contentType = null;
		}

		final String ext = ModelUtils.getCommonExtension(uriArray);
		if (ext != null) {
			this.extension = ext;
		} else if (contentType == null && namespace == null) {
			this.extension = DEFAULT_EXTENSION;
		} else {
			this.extension = null;
		}
	}

	/**
	 * Returns the common namespace found for the models considered by this instance.
	 * 
	 * @return The common namespace found for the models considered by this instance.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Returns the common content-type found for the models considered by this instance.
	 * 
	 * @return The common content-type found for the models considered by this instance.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns the common extension found for the models considered by this instance.
	 * 
	 * @return The common extension found for the models considered by this instance.
	 */
	public String getExtension() {
		return extension;
	}
}
