/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.provider.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.diagram.GMFCompare;
import org.eclipse.emf.compare.diagram.provider.IViewLabelProvider;

/**
 * Describes a extension as contributed to the "" extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ViewLabelProviderExtensionDescriptor {
	/** Names of the extension point's attributes. */
	public static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/** Name of the type of diagram. */
	public static final String ATTRIBUTE_DIAGRAM_TYPE = "diagramType"; //$NON-NLS-1$

	/** Configuration element of this descriptor. */
	private final IConfigurationElement element;

	/** Qualified class name of the extension. This will be used as an id to remove contributions. */
	private final String className;

	/** Name of the type of diagram. */
	private final String diagramType;

	/** We only need to create the instance once, this will keep reference to it. */
	private IViewLabelProvider extension;

	/**
	 * Instantiates a descriptor with all information.
	 * 
	 * @param configuration
	 *            Configuration element from which to create this descriptor.
	 */
	public ViewLabelProviderExtensionDescriptor(IConfigurationElement configuration) {
		element = configuration;
		className = configuration.getAttribute(ATTRIBUTE_CLASS);
		diagramType = configuration.getAttribute(ATTRIBUTE_DIAGRAM_TYPE);
	}

	/**
	 * Returns this descriptor's "extension" class name.
	 * 
	 * @return This descriptor's "extension" class name.
	 */
	public String getExtensionClassName() {
		return className;
	}

	public String getDiagramType() {
		return diagramType;
	}

	/**
	 * Creates an instance of this descriptor's {@link IViewLabelProvider}.
	 * 
	 * @return A new instance of this descriptor's {@link IViewLabelProvider}.
	 */
	public IViewLabelProvider getViewLabelTypeProviderExtension() {
		if (extension == null) {
			try {
				extension = (IViewLabelProvider)element.createExecutableExtension(ATTRIBUTE_CLASS);
			} catch (CoreException e) {
				GMFCompare.getDefault().getLog()
						.log(new Status(IStatus.ERROR, GMFCompare.PLUGIN_ID, e.getMessage(), e));
			}
		}
		return extension;
	}
}
