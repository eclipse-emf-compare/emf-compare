/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput;

/**
 * A compare input whose purpose is to support a comparison with only pseudo-conflicts.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class OnlyPseudoConflictsCompareInput extends ForwardingCompareInput {

	/**
	 * @param compareInput
	 */
	public OnlyPseudoConflictsCompareInput(ICompareInput compareInput) {
		super(compareInput);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput#createForwardingTypedElement(org.eclipse.compare.ITypedElement)
	 */
	@Override
	protected ForwardingTypedElement createForwardingTypedElement(ITypedElement typedElement) {
		return new OnlyPseudoConflictsTypedElement(typedElement);
	}

	/**
	 * A specific {@link ITypedElement} to use with
	 * {@link org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.OnlyPseudoConflictsCompareInput}
	 * .
	 * 
	 * @author MB
	 */
	class OnlyPseudoConflictsTypedElement extends ForwardingTypedElement {

		/**
		 * @param delegate
		 */
		public OnlyPseudoConflictsTypedElement(ITypedElement delegate) {
			super(delegate);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getType() {
			return "org.eclipse.emf.compare.rcp.ui.eOnlyPseudoConflicts"; //$NON-NLS-1$
		}

	}
}
