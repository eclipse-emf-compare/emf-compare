/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.provider.spec;

import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

public class LabelChangeItemProviderSpec extends ForwardingDiagramDiffItemProvider {

	public LabelChangeItemProviderSpec(ItemProviderAdapter delegate) {
		super(delegate);
	}

	@Override
	protected String getValueText(DiagramDiff diagramDiff) {
		return "The label";
	}

}
