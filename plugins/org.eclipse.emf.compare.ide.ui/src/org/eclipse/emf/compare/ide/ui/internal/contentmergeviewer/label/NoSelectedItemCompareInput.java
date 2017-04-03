/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput;

/**
 * A compare input whose purpose is to support a comparison with no selected items.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class NoSelectedItemCompareInput extends ForwardingCompareInput {
	/**
	 * @param compareInput
	 *            compare input
	 */
	public NoSelectedItemCompareInput(ICompareInput compareInput) {
		super(compareInput);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput#createForwardingTypedElement(org.eclipse.compare.ITypedElement)
	 */
	@Override
	protected ForwardingTypedElement createForwardingTypedElement(ITypedElement typedElement) {
		return new NoSelectedItemTypedElement(typedElement);
	}

	/**
	 * A specific {@link ITypedElement} to use with
	 * {@link org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoSelectedItemCompareInput}.
	 * 
	 * @author Martin Fleck <mfleck@eclipsesource.com>
	 */
	class NoSelectedItemTypedElement extends ForwardingTypedElement {

		/**
		 * @param delegate
		 *            typed element delegate
		 */
		public NoSelectedItemTypedElement(ITypedElement delegate) {
			super(delegate);
		}

		@Override
		public String getType() {
			return "org.eclipse.emf.compare.rcp.ui.eNoSelectedItem"; //$NON-NLS-1$
		}

	}
}
