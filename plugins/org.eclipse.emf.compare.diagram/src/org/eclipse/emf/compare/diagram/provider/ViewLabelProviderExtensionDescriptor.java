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
package org.eclipse.emf.compare.diagram.provider;

/**
 * Describes a extension as contributed to the "" extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ViewLabelProviderExtensionDescriptor {

	/** Qualified class name of the extension. This will be used as an id to remove contributions. */
	protected String className;

	/** Name of the type of diagram. */
	protected String diagramType;

	/** We only need to create the instance once, this will keep reference to it. */
	protected IViewLabelProvider extension;

	/**
	 * Constructor
	 */
	public ViewLabelProviderExtensionDescriptor() {
		this.className = null;
		this.diagramType = null;
		this.extension = null;
	}

	public ViewLabelProviderExtensionDescriptor(String className, String diagramType,
			IViewLabelProvider extension) {
		this.className = className;
		this.diagramType = diagramType;
		this.extension = extension;
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

	public IViewLabelProvider getExtension() {
		return extension;
	}

}
