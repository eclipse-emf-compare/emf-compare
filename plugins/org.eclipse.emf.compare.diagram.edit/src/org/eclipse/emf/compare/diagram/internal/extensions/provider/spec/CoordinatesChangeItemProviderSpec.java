/**
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Item provider for coordinates changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CoordinatesChangeItemProviderSpec extends ForwardingDiagramDiffItemProvider {

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            The origin item provider adapter.
	 */
	public CoordinatesChangeItemProviderSpec(ItemProviderAdapter delegate) {
		super(delegate);
	}

	@Override
	protected String getReferenceText(DiagramDiff diagramDiff) {
		return "coordinates"; //$NON-NLS-1$
	}

}
