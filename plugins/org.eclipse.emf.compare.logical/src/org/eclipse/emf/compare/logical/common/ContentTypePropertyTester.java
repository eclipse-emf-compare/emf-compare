/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.common;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;

/**
 * This will be used as a property tester for plugin.xml 'enablement' values. Specifically, we'll use this to
 * check whether a given IFile has a given content type ID.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class ContentTypePropertyTester extends PropertyTester {
	/** Name of the property we'll test with this tester. */
	private static final String PROPERTY_CONTENT_TYPE_ID = "contentTypeId"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String,
	 *      java.lang.Object[], java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IFile && expectedValue instanceof String) {
			if (PROPERTY_CONTENT_TYPE_ID.equals(property)) {
				return hasContentType((IFile)receiver, (String)expectedValue);
			}
		}
		return false;
	}

	/**
	 * This will return <code>true</code> if and only if the given IFile has the given <em>contentTypeId</em>
	 * configured (as returned by {@link IContentTypeManager#findContentTypesFor(InputStream, String)
	 * Platform.getContentTypeManager().findContentTypesFor(InputStream, String)}.
	 * 
	 * @param resource
	 *            The resource from which to test the content types.
	 * @param contentTypeId
	 *            Fully qualified identifier of the content type this <em>resource</em> has to feature.
	 * @return <code>true</code> if the given {@link IFile} has the given content type.
	 */
	private boolean hasContentType(IFile resource, String contentTypeId) {
		IContentTypeManager ctManager = Platform.getContentTypeManager();
		IContentType expected = ctManager.getContentType(contentTypeId);
		if (expected == null) {
			return false;
		}

		InputStream resourceContent = null;
		IContentType[] contentTypes = null;
		try {
			resourceContent = resource.getContents();
			contentTypes = ctManager.findContentTypesFor(resourceContent, resource.getName());
		} catch (CoreException e) {
			ctManager.findContentTypesFor(resource.getName());
		} catch (IOException e) {
			ctManager.findContentTypesFor(resource.getName());
		} finally {
			if (resourceContent != null) {
				try {
					resourceContent.close();
				} catch (IOException e) {
					// would have already been caught by the outer try, leave the stream open
				}
			}
		}

		boolean hasContentType = false;
		if (contentTypes != null) {
			for (int i = 0; i < contentTypes.length && !hasContentType; i++) {
				if (contentTypes[i].isKindOf(expected)) {
					hasContentType = true;
				}
			}
		}
		return hasContentType;
	}
}
