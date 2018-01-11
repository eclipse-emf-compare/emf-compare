/*******************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.logical;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.internal.EMFCompareIDEMessages;
import org.eclipse.emf.compare.ide.logical.IModelInclusionTester;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;

/**
 * A model inclusion tester based on another {@link IModelInclusionTester} contributed by an extension.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class DynamicModelInclusionTester extends AbstractModelInclusionTester {

	/** The configuration element that contributes the extension. */
	private IConfigurationElement element;

	/** The other model inclusion tester that we delegate to. */
	private IModelInclusionTester delegate;

	/**
	 * Creates a new dynamic model inclusion tester for the specified <code>configurationElement</code>.
	 * 
	 * @param element
	 *            The configuration element to create the contributed {@link IModelInclusionTester} from.
	 * @param key
	 *            The key of this tester.
	 */
	public DynamicModelInclusionTester(IConfigurationElement element, String key) {
		super(key);
		this.element = element;
	}

	/**
	 * Delegates the test to the contributed model instance tester.
	 * 
	 * @param file
	 *            The file to test.
	 * @return whether or not this file should be included.
	 */
	public boolean shouldInclude(IFile file) {
		return getDelegate().shouldInclude(file);
	}

	/**
	 * Returns the delegate if it has been created already, or creates it from this tester's configuration
	 * element.
	 * 
	 * @return The delegate.
	 */
	private IModelInclusionTester getDelegate() {
		if (delegate == null) {
			try {
				delegate = (IModelInclusionTester)element
						.createExecutableExtension(ModelInclusionTesterFactory.DYNAMIC_TESTER);
			} catch (CoreException e) {
				// Log issue
				String contributorName = element.getDeclaringExtension().getContributor().getName();
				String message = EMFCompareIDEMessages.getString(
						"ModelInclusionTesterRegistry.instantiationError", //$NON-NLS-1$
						contributorName);
				IStatus status = new Status(IStatus.ERROR, contributorName,
						message + element.getDeclaringExtension().getContributor().getName(), e);
				EMFCompareRCPPlugin.getDefault().getLog().log(status);

				// Avoid trying to instantiate this again and use an always-false stand-in
				delegate = new IModelInclusionTester() {
					public boolean shouldInclude(IFile file) {
						return false;
					}
				};
			}
		}
		return delegate;
	}

}
