/**
 * Copyright (c) 2009, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

// TODO subclass this in the compare.ide plugin to add the notion of content types identifier
/**
 * This will be used in order to store the common identifiers of two or three Resources.
 * <p>
 * Namely, we consider "identifiers" the extension and namespace of these resources.<b>Note</b> that we expect
 * only one of {@link #extension} or {@link #namespace} to be set, in the order of importance : namespace is
 * usually better suited than the file extension to identify a model.
 * </p>
 * 
 * @author <a href="mailto:gonzague.reydet@obeo.fr">Gonzague Reydet</a>
 * @since 1.1
 */
public class ModelIdentifier {
	/** Default extension we'll use if the resources have none. */
	private static final String DEFAULT_EXTENSION = "ecore"; //$NON-NLS-1$

	/**
	 * If this wildcard is passed over to {@link #matchExtension(String[])}, we will return {@code true}
	 * whatever the actual extension this identified is for.
	 */
	private static final String EXTENSIONS_WILDCARD = "*"; //$NON-NLS-1$

	/** Common namespace of the models. */
	private final String namespace;

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

		namespace = getCommonNamespace(resources);

		final String ext = getCommonExtension(uriArray);
		if (ext != null) {
			this.extension = ext;
		} else if (namespace == null) {
			this.extension = DEFAULT_EXTENSION;
		} else {
			this.extension = null;
		}
	}

	/**
	 * This can be used to check whether this identifier matches the given extension.
	 * <p>
	 * A list of extensions can be passed over as a comma-separated list.
	 * </p>
	 * 
	 * @param extensions
	 *            Extension(s) for which we seek a model identifier. If {@code null}, this will return
	 *            {@code false}.
	 * @return {@code true} if this {@link ModelIdentifier} matches the (or one of the) given extension.
	 */
	public boolean matchExtension(String extensions) {
		if (extension == null || extensions == null) {
			return false;
		}

		boolean match = false;
		final String[] extensionsArray = extensions.split(","); //$NON-NLS-1$
		for (int i = 0; i < extensionsArray.length && !match; i++) {
			final String candidate = extensionsArray[i].trim();
			match = extension.equals(candidate) || EXTENSIONS_WILDCARD.equals(candidate);
		}
		return match;
	}

	/**
	 * This can be used to check whether this identifier matches the given namespace.
	 * <p>
	 * The {@code candidate} here will be interpreted as a regular expression.
	 * </p>
	 * 
	 * @param candidate
	 *            Namespace for which we need to validate this {@link ModelIdentifier}. If {@code null}, this
	 *            will return {@code false} .
	 * @return {@code true} if this {@link ModelIdentifier} matches the given namespace.
	 */
	public boolean matchNamespace(String candidate) {
		if (namespace == null || candidate == null) {
			return false;
		}

		return namespace.matches(candidate.trim());
	}

	/**
	 * This will try and find the common file extension for the compared models.
	 * 
	 * @param uris
	 *            The resource URIs that will be compared.
	 * @return The common extension of these files or <code>null</code> if file extensions are distinct.
	 */
	private static String getCommonExtension(URI... uris) {
		String extension = null;
		for (int i = 0; i < uris.length; i++) {
			if (uris[i] != null) {
				final String fileExtension = uris[i].fileExtension();
				if (extension == null && fileExtension != null) {
					extension = fileExtension;
				} else if (fileExtension != null && !fileExtension.equals(extension)) {
					return null;
				}
			}
		}
		return extension;
	}

	/**
	 * This will try and find the common namespace of the given resources.
	 * 
	 * @param resources
	 *            The resources that will be compared.
	 * @return The common namespace of these Resources or <code>null</code> if they are distinct.
	 * @since 1.1
	 */
	private static String getCommonNamespace(Resource... resources) {
		String namespace = null;
		for (int i = 0; i < resources.length; i++) {
			if (resources[i] != null && !resources[i].getContents().isEmpty()) {
				final EObject rootContainer = EcoreUtil.getRootContainer(resources[i].getContents().get(0)
						.eClass());
				if (rootContainer instanceof EPackage) {
					final String nsURI = ((EPackage)rootContainer).getNsURI();
					if (namespace == null && nsURI != null) {
						namespace = nsURI;
					} else if (nsURI == null || !nsURI.equals(namespace)) {
						return null;
					}
				}
			}
		}
		return namespace;
	}
}
