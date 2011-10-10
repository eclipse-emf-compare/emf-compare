/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.extension.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;

/**
 * Common factory for UML model element changes.
 * 
 * @see Bug 351593.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractUMLElementChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public AbstractUMLElementChangeFactory(UML2DiffEngine engine) {
		super(engine);
	}

	protected List<DiffElement> getEmbeddedStereotypeApplicationDiffs(EObject obj,
			EcoreUtil.CrossReferencer crossReferencer, EReference diffSide, EClass expectedDiff) {
		final List<DiffElement> result = new ArrayList<DiffElement>();
		final Iterator<EObject> it = obj.eAllContents();
		while (it.hasNext()) {
			final EObject next = it.next();
			if (next instanceof Element) {
				if (((Element)next).getStereotypeApplications().size() > 0) {
					// Look for the UMLStereotypeApplication Difference
					final List<DiffElement> findCrossReferences = findCrossReferences(next, diffSide,
							crossReferencer);
					for (DiffElement diffElement : findCrossReferences) {
						if (expectedDiff.isInstance(diffElement)) {
							result.add(diffElement);
						}
					}
				}
			}
		}
		return result;
	}

	protected List<EObject> getAllStereotypeApplications(Element elt) {
		final List<EObject> result = new ArrayList<EObject>();
		result.addAll(elt.getStereotypeApplications());
		final Iterator<EObject> it = elt.eAllContents();
		while (it.hasNext()) {
			final EObject obj = it.next();
			if (obj instanceof Element) {
				result.addAll(((Element)obj).getStereotypeApplications());
			}
		}
		return result;
	}

}
