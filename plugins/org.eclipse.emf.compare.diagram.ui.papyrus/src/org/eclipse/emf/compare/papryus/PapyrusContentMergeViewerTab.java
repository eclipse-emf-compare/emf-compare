/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.papryus;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diagram.ui.mergeviewer.GMFContentMergeTabFolder;
import org.eclipse.emf.compare.diagram.ui.mergeviewer.GMFContentMergeViewerTab;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.sashwindows.di.PageRef;
import org.eclipse.swt.widgets.Composite;

/**
 * The tab that contains the Papryus viewer.
 * @author Mickael Barbero <a href="mailto:mickael.barbero@obeo.fr">mickael.barbero@obeo.fr</a>
 */
public class PapyrusContentMergeViewerTab extends GMFContentMergeViewerTab {

	/**
	 * Constructor.
	 * @param parent The parent composite.
	 * @param side The side.
	 * @param parentFolder The parent folder.
	 */
	public PapyrusContentMergeViewerTab(Composite parent, int side, GMFContentMergeTabFolder parentFolder) {
		super(parent, side, parentFolder);
	}

	@Override
	public void setReflectiveInput(Object object) {
		if (object instanceof EObject) {
			if (object instanceof Diagram) {
				displayDiagram((Diagram)object);
			}
		} else {
			// may be invoked with a resourceSet, a list of resources, or a single resource
			assert object instanceof Resource || object instanceof List;
			if (object instanceof List) {
				for (Object item : (List<?>)object) {
					assert item instanceof Resource;
				}
			}
			if (object instanceof Resource) {
				displayDiagram(getDiagramFromResource((Resource)object));
			} else if (object instanceof List && !((List<?>)object).isEmpty()) {
				final Resource resource = (Resource)((List<?>)object).get(0);
				displayDiagram(getDiagramFromResource(resource));
			}
		}
		// maintain synchronization with tree
		contentMergeTabFolderParent.getTreePart().setReflectiveInput(object);
		contentMergeTabFolderParent.getPropertyPart().setReflectiveInput(object);
		redraw();
	}

	/**
	 * Get the list of diagrams from the given resource.
	 * @param resource The resource.
	 * @return The list of diagrams.
	 */
	public List<Diagram> getDiagrams(Resource resource) {
		final List<Diagram> ret = new ArrayList<Diagram>();

		final TreeIterator<EObject> eAllContents = resource.getAllContents();
		while (eAllContents.hasNext()) {
			final EObject element = (EObject)eAllContents.next();
			if (element instanceof PageRef) {
				final EObject emfPageIdentifier = ((PageRef)element).getEmfPageIdentifier();
				if (emfPageIdentifier instanceof Diagram) {
					final Diagram d = (Diagram)emfPageIdentifier;
					ret.add(d);
					eAllContents.prune();
				}
			}
		}

		return ret;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.diagram.ui.mergeviewer.GMFContentMergeViewerTab#getDiagramFromResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Diagram getDiagramFromResource(final Resource resource) {
		final List<Diagram> diagrams = getDiagrams(resource);
		return diagrams.get(0);
	}
}
