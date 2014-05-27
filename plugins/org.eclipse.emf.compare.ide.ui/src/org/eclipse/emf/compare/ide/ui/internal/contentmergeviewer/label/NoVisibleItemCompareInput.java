/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput;

/**
 * A compare input whose purpose is to support a comparison with no visible items.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class NoVisibleItemCompareInput extends ForwardingCompareInput {

	/**
	 * @param compareInput
	 */
	public NoVisibleItemCompareInput(ICompareInput compareInput) {
		super(compareInput);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput#createForwardingTypedElement(org.eclipse.compare.ITypedElement)
	 */
	@Override
	protected ForwardingTypedElement createForwardingTypedElement(ITypedElement typedElement) {
		return new NoVisibleItemTypedElement(typedElement);
	}

	/**
	 * A specific {@link ITypedElement} to use with
	 * {@link org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoVisibleItemCompareInput}.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	class NoVisibleItemTypedElement extends ForwardingTypedElement {

		/**
		 * @param delegate
		 */
		public NoVisibleItemTypedElement(ITypedElement delegate) {
			super(delegate);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getType() {
			return "org.eclipse.emf.compare.rcp.ui.eNoVisibleItem"; //$NON-NLS-1$
		}

	}
}
