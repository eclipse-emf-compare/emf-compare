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
package org.eclipse.emf.compare.ui.contentprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.match.UnMatchElement;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.viewerpart.ModelContentMergePropertyPart;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider used by {@link ModelContentMergePropertiesPart ModelContentMergePropertiesParts}
 * displaying {@link Match2Elements}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class PropertyContentProvider implements IStructuredContentProvider {
	private int partSide;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		Object[] elements = new Object[]{};
		EObject input = null;
		
		if (inputElement instanceof Match2Elements) {
			final Match2Elements match = (Match2Elements)inputElement;
			
			if (partSide == EMFCompareConstants.LEFT) {
				input = match.getLeftElement();
			} else if (partSide == EMFCompareConstants.RIGHT) {
				input = match.getRightElement();
			} else {
				//TODO LGT input = match.getAncestor
			}
		} else if (inputElement instanceof UnMatchElement) {
			input = ((UnMatchElement)inputElement).getElement();
		}
		if (input != null) {
			final List<List<Object>> inputElements = new ArrayList<List<Object>>();
			
			for (Object attObject : input.eClass().getEAllAttributes()) {
				final List<Object> row = new ArrayList<Object>();
				row.add(attObject);
				row.add(input.eGet((EAttribute)attObject));
				inputElements.add(row);
			}

			elements = inputElements.toArray();
			Arrays.sort(elements, new Comparator<Object>() {
				public int compare(Object first, Object second) {
					final String name1 = ((EAttribute)((List)first).get(0)).getName();
					final String name2 = ((EAttribute)((List)second).get(0)).getName();
					
					return name1.compareTo(name2);
				}
			});
		}
		return elements;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Nothing needs to be disposed.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			if (viewer instanceof ModelContentMergePropertyPart) {
				final ModelContentMergePropertyPart properties = (ModelContentMergePropertyPart)viewer;
				properties.getTable().clearAll();
				partSide = properties.getSide();
			}
		}
	}
}
