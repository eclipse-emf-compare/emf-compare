package org.eclipse.emf.compare.logical;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;

public class ContentTypePropertyTester extends PropertyTester {
	private static final String PROPERTY_CONTENT_TYPE_ID = "contentTypeId"; //$NON-NLS-1$

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IFile && expectedValue instanceof String) {
			if (PROPERTY_CONTENT_TYPE_ID.equals(property)) {
				return hasContentType((IFile)receiver, (String)expectedValue);
			}
		}
		return false;
	}

	private boolean hasContentType(IFile resource, String contentTypeId) {
		IContentTypeManager ctManager = Platform.getContentTypeManager();
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
					// would have already been catched by the outer try, leave the stream open
				}
			}
		}

		if (contentTypes != null) {
			for (IContentType type : contentTypes) {
				if (type.getId().equals(contentTypeId)) {
					return true;
				}
			}
		}
		return false;
	}
}
