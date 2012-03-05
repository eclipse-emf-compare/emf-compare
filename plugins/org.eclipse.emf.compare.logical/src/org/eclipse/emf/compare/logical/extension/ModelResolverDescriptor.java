/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.compare.util.ModelIdentifier;

/**
 * Describes a extension as contributed to the "org.eclipse.emf.compare.modelResolver" extension point.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class ModelResolverDescriptor {
	/** Name of the attribute holding the {@link IModelResolver} qualified names. */
	public static final String MODEL_RESOLVER_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	/** Name of the attribute holding the file extension(s) this resolver applies to. */
	public static final String MODEL_RESOLVER_FILE_EXTENSION_TAG = "fileExtension"; //$NON-NLS-1$

	/** Name of the attribute holding the content type(s) this resolver applies to. */
	public static final String MODEL_RESOLVER_CONTENT_TYPE_TAG = "contentType"; //$NON-NLS-1$

	/** Name of the attribute holding the namespace(s) this resolver applies to. */
	public static final String MODEL_RESOLVER_NAMESPACE_TAG = "namespace"; //$NON-NLS-1$

	/** This attribute is common to the "enablement" tags : fileExtension, contentType, namespace. */
	public static final String ENABLEMENT_TAG_VALUE = "value"; //$NON-NLS-1$

	/**
	 * This can be used by plugin developers to define a model resolver that applies on "all" file extensions.
	 */
	public static final String EXTENSIONS_WILDCARD = "*"; //$NON-NLS-1$

	/** All of our "enablement" tags accept comma-separated values. */
	public static final String VALUE_SEPARATOR = ","; //$NON-NLS-1$

	/** Configuration element of this descriptor. */
	private final IConfigurationElement element;

	/** Qualified class name of the model resolver. This will be used as an id to remove contributions. */
	private final String extensionClassName;

	/** Content type(s) of the models this resolver applies to. */
	private final String contentType;

	/** File extension(s) of the models this resolver applies to. */
	private final String fileExtension;

	/** Namespace(s) of the models this resolver applies to. */
	private final String namespace;

	/** We only need to create the instance once, this will keep reference to it. */
	private IModelResolver modelResolver;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 */
	public ModelResolverDescriptor(IConfigurationElement element) {
		this.element = element;
		this.extensionClassName = element.getAttribute(MODEL_RESOLVER_CLASS_ATTRIBUTE);

		IConfigurationElement[] contentTypeConfig = element.getChildren(MODEL_RESOLVER_CONTENT_TYPE_TAG);
		if (contentTypeConfig.length > 0) {
			contentType = contentTypeConfig[0].getAttribute(ENABLEMENT_TAG_VALUE);
		} else {
			contentType = null;
		}

		IConfigurationElement[] fileExtensionConfig = element.getChildren(MODEL_RESOLVER_FILE_EXTENSION_TAG);
		if (fileExtensionConfig.length > 0) {
			fileExtension = fileExtensionConfig[0].getAttribute(ENABLEMENT_TAG_VALUE);
		} else {
			fileExtension = null;
		}

		IConfigurationElement[] namespaceConfig = element.getChildren(MODEL_RESOLVER_NAMESPACE_TAG);
		if (namespaceConfig.length > 0) {
			namespace = namespaceConfig[0].getAttribute(ENABLEMENT_TAG_VALUE);
		} else {
			namespace = null;
		}
	}

	/**
	 * Returns this descriptor's model resolver qualified name.
	 * 
	 * @return This descriptor's model resolver qualified name.
	 */
	public String getExtensionClassName() {
		return extensionClassName;
	}

	/**
	 * Creates an instance of this descriptor's model resolver if needed, then return it.
	 * 
	 * @return An instance of this descriptor's model resolver.
	 */
	public IModelResolver getModelResolver() {
		if (modelResolver == null) {
			try {
				modelResolver = (IModelResolver)element
						.createExecutableExtension(MODEL_RESOLVER_CLASS_ATTRIBUTE);
			} catch (CoreException e) {
				// FIXME log this!
			}
		}
		return modelResolver;
	}

	/**
	 * This will be used by the framework in order to determine whether this descriptor's model resolver can
	 * be used for the given model identifier.
	 * 
	 * @param identifier
	 *            Identifier of the EMF Resource which logical model is to be resolved.
	 * @return <code>true</code> if this descriptor's model resolver can be used for the given model
	 *         identifier, <code>false</code> otherwise.
	 */
	public boolean canResolve(ModelIdentifier identifier) {
		/*
		 * Note that a given model resolver can only have a single one of file extension, namespace or content
		 * type defined
		 */
		boolean canResolve = false;
		if (fileExtension != null) {
			String[] validExtensions = fileExtension.split(VALUE_SEPARATOR);
			for (String validExtension : validExtensions) {
				canResolve = identifier.getExtension().equals(validExtension.trim())
						|| EXTENSIONS_WILDCARD.equals(validExtension.trim());
			}
		} else if (contentType != null) {
			String[] validContentTypes = contentType.split(VALUE_SEPARATOR);
			IContentTypeManager ctManager = Platform.getContentTypeManager();
			for (int i = 0; i < validContentTypes.length && !canResolve; i++) {
				IContentType expected = ctManager.getContentType(validContentTypes[i].trim());
				IContentType actual = ctManager.getContentType(identifier.getContentType());
				if (expected != null && actual != null) {
					canResolve = actual.isKindOf(expected);
				}
			}
		} else if (namespace != null) {
			String[] validNamespaces = namespace.split(VALUE_SEPARATOR);
			for (String validNamespace : validNamespaces) {
				canResolve = identifier.getNamespace().matches(validNamespace.trim());
			}
		}
		return canResolve;
	}
}
