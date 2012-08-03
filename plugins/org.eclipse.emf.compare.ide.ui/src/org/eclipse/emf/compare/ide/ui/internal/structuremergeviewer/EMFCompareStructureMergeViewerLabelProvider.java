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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ImageProvider;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.MatchNode;
import org.eclipse.emf.compare.ide.ui.internal.util.EMFCompareCompositeImageDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

class EMFCompareStructureMergeViewerLabelProvider extends AdapterFactoryLabelProvider {

	private final boolean fLeftIsLocal;

	private final ImageProvider imgProvider;

	/**
	 * @param adapterFactory
	 */
	public EMFCompareStructureMergeViewerLabelProvider(AdapterFactory adapterFactory, boolean leftIsLocal) {
		super(adapterFactory);
		fLeftIsLocal = leftIsLocal;
		imgProvider = new ImageProvider(fLeftIsLocal);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IDiffElement) {
			return ((IDiffElement)element).getName();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		final Image ret;
		if (element instanceof DiffNode) {
			Diff target = ((DiffNode)element).getTarget();
			ImageDescriptor overlay = imgProvider.getImageDescriptorOverlay(target);
			Image base = super.getImage(target);
			EMFCompareCompositeImageDescriptor descriptor = new EMFCompareCompositeImageDescriptor(base,
					overlay, EMFCompareConstants.COMPARE_IMAGE_WIDTH, !fLeftIsLocal);
			ret = EMFCompareIDEUIPlugin.getDefault().getImage(descriptor);
		} else if (element instanceof MatchNode) {
			Match target = ((MatchNode)element).getTarget();
			ImageDescriptor overlay = imgProvider.getImageDescriptorOverlay(target);
			Image base = super.getImage(target);
			EMFCompareCompositeImageDescriptor descriptor = new EMFCompareCompositeImageDescriptor(base,
					overlay, EMFCompareConstants.COMPARE_IMAGE_WIDTH, !fLeftIsLocal);
			ret = EMFCompareIDEUIPlugin.getDefault().getImage(descriptor);
		} else if (element instanceof AbstractEDiffElement) {
			ret = ((AbstractEDiffElement)element).getImage();
		} else {
			ret = super.getImage(element);
		}

		return ret;
	}
}
