/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.factory;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AbstractAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLDanglingStereotypeApplicationAccessor;

/**
 * This will be in charge of creating the accessor for dangling stereotype applications.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 2.4
 */
@SuppressWarnings("restriction")
public class UMLDanglingStereotypeApplicationAccessorFactory extends AbstractAccessorFactory {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#isFactoryFor(java.lang.Object)
	 */
	public boolean isFactoryFor(Object target) {
		return target instanceof DanglingStereotypeApplication;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createLeft(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return new UMLDanglingStereotypeApplicationAccessor(adapterFactory,
				(DanglingStereotypeApplication)target, MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return new UMLDanglingStereotypeApplicationAccessor(adapterFactory,
				(DanglingStereotypeApplication)target, MergeViewerSide.RIGHT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return new UMLDanglingStereotypeApplicationAccessor(adapterFactory,
				(DanglingStereotypeApplication)target, MergeViewerSide.ANCESTOR);
	}
}
