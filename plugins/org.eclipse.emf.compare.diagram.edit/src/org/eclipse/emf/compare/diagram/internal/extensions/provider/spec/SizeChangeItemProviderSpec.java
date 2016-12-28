/**
 * Copyright (c) 2016 EclipseSource Services GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Item provider for size changes.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class SizeChangeItemProviderSpec extends ForwardingDiagramDiffItemProvider {

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            The origin item provider adapter.
	 */
	public SizeChangeItemProviderSpec(ItemProviderAdapter delegate) {
		super(delegate);
	}

	@Override
	protected String getReferenceText(DiagramDiff diagramDiff) {
		return "size"; //$NON-NLS-1$
	}

}
