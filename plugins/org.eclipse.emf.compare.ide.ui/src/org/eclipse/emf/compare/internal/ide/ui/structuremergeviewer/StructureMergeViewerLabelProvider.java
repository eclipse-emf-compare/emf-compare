/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
final class StructureMergeViewerLabelProvider extends AdapterFactoryLabelProvider {

	private final CompareConfiguration fCompareConfiguration;

	/**
	 * @param adapterFactory
	 * @param emfCompareStructureMergeViewer
	 */
	StructureMergeViewerLabelProvider(AdapterFactory adapterFactory, CompareConfiguration compareConfiguration) {
		super(adapterFactory);
		this.fCompareConfiguration = compareConfiguration;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		return super.getText(getTargetIfNotifier(object));
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object object) {
		if (object instanceof IDiffElement) {
			IDiffElement input = (IDiffElement)object;

			int kind = input.getKind();
			if (Utilities.getBoolean(fCompareConfiguration, "LEFT_IS_LOCAL", false)) {
				switch (kind & Differencer.DIRECTION_MASK) {
					case Differencer.LEFT:
						kind = (kind & ~Differencer.LEFT) | Differencer.RIGHT;
						break;
					case Differencer.RIGHT:
						kind = (kind & ~Differencer.RIGHT) | Differencer.LEFT;
						break;
				}
			}

			return fCompareConfiguration.getImage(super.getImage(getTargetIfNotifier(object)), kind);
		}
		return null;
	}

	private static Object getTargetIfNotifier(Object object) {
		if (object instanceof Adapter) {
			return ((Adapter)object).getTarget();
		}
		return object;
	}
}
