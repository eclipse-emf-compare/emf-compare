/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.mergeviewer.item.impl;

import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.ResourceAttachmentChangeProvider;

/**
 * Provider to keep backward compatibility with special {@link DanglingStereotypeApplicationMergeViewerItem}.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class DanglingStereotypeApplicationMergeViewerItemProvider extends ResourceAttachmentChangeProvider {

	@Override
	public boolean canHandle(Object object) {
		return object instanceof DanglingStereotypeApplicationMergeViewerItem;
	}
}
