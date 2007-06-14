/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.structuremergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.structuremergeviewer.StructureDiffViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.ui.contentprovider.ModelStructureContentProvider;
import org.eclipse.emf.compare.ui.util.EMFAdapterFactoryProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Compare and merge viewer with an area showing diffs as a structured tree.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class TestModelStructureMergeViewer extends StructureDiffViewer {
	/**
	 * Creates a new model structure merge viewer and intializes it.
	 * 
	 * @param parent
	 * 			Parent composite for this viewer.
	 * @param config
	 * 			The configuration object.
	 */
	public TestModelStructureMergeViewer(Composite parent, CompareConfiguration config) {
		super(parent, config);
		setLabelProvider(new ModelStructureLabelProvider());
		setUseHashlookup(true);
		setContentProvider(new ModelStructureContentProvider());
	}
	
	/**
	 * Returns the viewer's name.
	 *
	 * @return 
	 * 			The viewer's name.
	 */
	public String getTitle() {
		return "Structural Differences"; //$NON-NLS-1$
	}
	
	/**
	 * {@link LabelProvider} of this viewer.
	 */
	private class ModelStructureLabelProvider extends AdapterFactoryLabelProvider {
		/**
		 * Default constructor.
		 */
		public ModelStructureLabelProvider() {
			super(EMFAdapterFactoryProvider.getAdapterFactory());
		}

		/**
		 * Returns the platform icon for a file. You can replace with your own
		 * icon if not a IFile, then pass it to the regular EMF.Edit providers.
		 * 
		 * @see AdapterFactoryLabelProvider#getImage(Object)
		 */
		public Image getImage(Object object) {
			Image image = null;
			if (object instanceof IFile) {
				image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			} else {
				image = super.getImage(object);
				if (image == null && object instanceof EObject) {
					if (EMFAdapterFactoryProvider.addAdapterFactoryFor((EObject)object)) {
						setAdapterFactory(EMFAdapterFactoryProvider.getAdapterFactory());
						image = super.getImage(object);
					}
				}
			}
			return image;
		}

		/**
		 * Returns the name of the given {@link IFile}, delegates to 
		 * {@link AdapterFactoryLabelProvider#getText(Object)} if not an 
		 * {@link IFile}.
		 * 
		 * @see AdapterFactoryLabelProvider#getText(Object)
		 */
		public String getText(Object object) {
			String text = null;
			if (object instanceof IFile) {
				text = ((IFile)object).getName();
			} else {
				text = super.getText(object);
				if (text == null && object instanceof EObject) {
					if (EMFAdapterFactoryProvider.addAdapterFactoryFor((EObject)object)) {
						setAdapterFactory(EMFAdapterFactoryProvider.getAdapterFactory());
						text = super.getText(object);
					}
				}
			}
			return super.getText(object);
		}
	}
}
