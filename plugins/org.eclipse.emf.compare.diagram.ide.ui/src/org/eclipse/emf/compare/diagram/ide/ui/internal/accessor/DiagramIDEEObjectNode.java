/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.EObjectAccessor;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramIDEEObjectNode extends EObjectAccessor implements ITypedElement, IStreamContentAccessor {

	/**
	 * @param adapterFactory
	 * @param eObject
	 */
	public DiagramIDEEObjectNode(AdapterFactory adapterFactory, EObject eObject) {
		super(adapterFactory, eObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return DiagramContentMergeViewerConstants.EOBJECT_NODE_TYPE;
	}

}
