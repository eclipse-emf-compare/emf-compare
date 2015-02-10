/*******************************************************************************
 * Copyright (c) 2014, 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.factory;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.IModelUpdateStrategy;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.IModelUpdateStrategyProvider;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AbstractAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.OpaqueElementBodyChangeAccessor;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.OpaqueElementBodyChangeUpdateStrategy;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.OpaqueElementBodyMoveAccessor;

/**
 * An accessor factory for {@link OpaqueElementBodyChange opaque element body changes}.
 * <p>
 * Depending on whether the {@link OpaqueElementBodyChange} represents a move or an addition/deletion/change,
 * this factory returns different accessors, because different content merge viewers are used for moves as
 * opposed to any other change type. Therefore, for moves, the {@link OpaqueElementBodyMoveAccessor} is
 * created, which shows the list of language values. For any other change type, the
 * {@link OpaqueElementBodyChangeAccessor} is created, which shows the changed body values in a textual
 * content merge viewer.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyChangeAccessorFactory extends AbstractAccessorFactory implements IModelUpdateStrategyProvider {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#isFactoryFor(java.lang.Object)
	 */
	public boolean isFactoryFor(Object target) {
		return target instanceof OpaqueElementBodyChange;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createLeft(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return createOpaqueElementBodyChangeAccessor(adapterFactory, (OpaqueElementBodyChange)target,
				MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return createOpaqueElementBodyChangeAccessor(adapterFactory, (OpaqueElementBodyChange)target,
				MergeViewerSide.RIGHT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return createOpaqueElementBodyChangeAccessor(adapterFactory, (OpaqueElementBodyChange)target,
				MergeViewerSide.ANCESTOR);
	}

	/**
	 * Depending on whether the {@code bodyChange} represents a move or a addition/deletion/change, we will
	 * return different accessors, because we show different content merger viewers.
	 * 
	 * @param adapterFactory
	 *            the adapater factory used to create the accessor.
	 * @param bodyChange
	 *            The change to be accessed by this accessor.
	 * @param side
	 *            The side of this accessor.
	 * @return The respective accessor for the given {@code bodyChange}.
	 */
	private ITypedElement createOpaqueElementBodyChangeAccessor(AdapterFactory adapterFactory,
			OpaqueElementBodyChange bodyChange, final MergeViewerSide side) {
		if (DifferenceKind.MOVE.equals(bodyChange.getKind())) {
			return new OpaqueElementBodyMoveAccessor(adapterFactory, bodyChange, side);
		} else {
			return new OpaqueElementBodyChangeAccessor(adapterFactory, bodyChange, side);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IModelUpdateStrategyProvider#getModelUpdateStrategy()
	 */
	public IModelUpdateStrategy getModelUpdateStrategy() {
		return new OpaqueElementBodyChangeUpdateStrategy();
	}
}
