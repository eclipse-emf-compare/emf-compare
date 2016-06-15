/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.factory;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AbstractAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.MultiplicityElementAttributeChangeAccessor;

/**
 * Custom {@link AbstractAccessorFactory} for {@link MultiplicityElementChange} elements.
 * 
 * @author Alexandra Buzila
 */
@SuppressWarnings("restriction")
public class MultiplicityElementAttributeChangeAccessorFactory extends AbstractAccessorFactory {

	/**
	 * {@inheritDoc}
	 */
	public boolean isFactoryFor(Object target) {
		return target instanceof MultiplicityElementChange
				&& ((MultiplicityElementChange)target).getPrimeRefining() instanceof AttributeChange;
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return new MultiplicityElementAttributeChangeAccessor(adapterFactory, (Diff)target,
				MergeViewerSide.ANCESTOR);
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return new MultiplicityElementAttributeChangeAccessor(adapterFactory, (Diff)target,
				MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return new MultiplicityElementAttributeChangeAccessor(adapterFactory, (Diff)target,
				MergeViewerSide.RIGHT);
	}

}
