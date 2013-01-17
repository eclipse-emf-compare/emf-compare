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
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.actions.group.DifferenceGroup;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ImageProvider;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.MatchNode;
import org.eclipse.emf.compare.ide.ui.internal.util.EMFCompareCompositeImageDescriptor;
import org.eclipse.emf.compare.ide.ui.internal.util.StyledStringConverter;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

class EMFCompareStructureMergeViewerLabelProvider extends AdapterFactoryLabelProvider.FontAndColorProvider implements IStyledLabelProvider {

	private final boolean fLeftIsLocal;

	private final ImageProvider imgProvider;

	/**
	 * @param adapterFactory
	 */
	public EMFCompareStructureMergeViewerLabelProvider(AdapterFactory adapterFactory, Viewer viewer,
			boolean leftIsLocal) {
		super(adapterFactory, viewer);
		fLeftIsLocal = leftIsLocal;
		imgProvider = new ImageProvider(fLeftIsLocal);
	}

	@Override
	public String getText(Object element) {
		return getStyledText(element).getString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getFont(java.lang.Object)
	 */
	@Override
	public Font getFont(Object object) {
		if (object instanceof AbstractEDiffElement) {
			return super.getFont(((AbstractEDiffElement)object).getTarget());
		}
		return super.getFont(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getForeground(java.lang.Object)
	 */
	@Override
	public Color getForeground(Object object) {
		if (object instanceof AbstractEDiffElement) {
			return super.getForeground(((AbstractEDiffElement)object).getTarget());
		}
		return super.getForeground(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getBackground(java.lang.Object)
	 */
	@Override
	public Color getBackground(Object object) {
		if (object instanceof AbstractEDiffElement) {
			return super.getBackground(((AbstractEDiffElement)object).getTarget());
		}
		return super.getBackground(object);
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
		} else if (element instanceof DifferenceGroup) {
			final Image groupImage = ((DifferenceGroup)element).getImage();
			if (groupImage != null) {
				ret = groupImage;
			} else {
				ret = EMFCompareIDEUIPlugin.getDefault().getImage("icons/full/toolb16/group.gif"); //$NON-NLS-1$ 
			}
		} else {
			ret = super.getImage(element);
		}

		return ret;
	}

	public StyledString getStyledText(Object element) {
		Object target = null;
		Object adapter = null;
		if (element instanceof Adapter) {
			target = ((Adapter)element).getTarget();
			adapter = adapterFactory.adapt(target, IItemStyledLabelProvider.class);
			if (adapter instanceof IItemStyledLabelProvider) {
				StyledStringConverter stringConverter = new StyledStringConverter();
				return stringConverter.toJFaceStyledString(((IItemStyledLabelProvider)adapter)
						.getStyledText(target));
			}
		}

		final String ret;
		if (element instanceof IDiffElement) {
			ret = ((IDiffElement)element).getName();
		} else if (element instanceof DifferenceGroup) {
			ret = ((DifferenceGroup)element).getName();
		} else {
			ret = super.getText(element);
		}
		return new StyledString(ret);

	}
}
