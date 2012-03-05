/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
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

	/**
	 * Looks for all the stereotype applications inside the given {@link EObject} of the model from the
	 * specified side. When stereotype applications are found on the child model objects, this returns the
	 * potential differences, related to these objects, from a given difference type.
	 * 
	 * @param obj
	 *            The start point to look for child objects on which stereotypes are applied.
	 * @param crossReferencer
	 *            A cross referencer to look for difference from model objects.
	 * @param diffSide
	 *            The side from which it is required to look for:
	 *            DiffPackage.Literals.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT or
	 *            DiffPackage.Literals.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT
	 * @param expectedDiff
	 *            The type of difference to look for:
	 *            UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_ADDITION or
	 *            UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_REMOVAL
	 * @return The list of matching differences.
	 */
	protected List<DiffElement> getEmbeddedStereotypeApplicationDiffs(EObject obj,
			EcoreUtil.CrossReferencer crossReferencer, EReference diffSide, EClass expectedDiff) {
		final List<DiffElement> result = new ArrayList<DiffElement>();
		result.addAll(getStereotypeDifferences(crossReferencer, diffSide, expectedDiff, obj));
		final Iterator<EObject> it = obj.eAllContents();
		while (it.hasNext()) {
			final EObject next = it.next();
			result.addAll(getStereotypeDifferences(crossReferencer, diffSide, expectedDiff, next));
		}
		return result;
	}

	/**
	 * Looks for all the stereotype applications on the given {@link EObject} of the model from the specified
	 * side. When stereotype applications are found, this returns the potential differences, related to this
	 * model object, from a given difference type.
	 * 
	 * @param crossReferencer
	 *            A cross referencer to look for difference from model objects.
	 * @param diffSide
	 *            The side from which it is required to look for:
	 *            DiffPackage.Literals.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT or
	 *            DiffPackage.Literals.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT
	 * @param expectedDiff
	 *            The type of difference to look for:
	 *            UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_ADDITION or
	 *            UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_REMOVAL
	 * @param next
	 *            model object on which stereotypes are applied.
	 * @return The list of matching differences.
	 */
	private List<DiffElement> getStereotypeDifferences(EcoreUtil.CrossReferencer crossReferencer,
			EReference diffSide, EClass expectedDiff, final EObject next) {
		final List<DiffElement> result = new ArrayList<DiffElement>();
		if (next instanceof Element && ((Element)next).getStereotypeApplications().size() > 0) {
			// Look for the UMLStereotypeApplication Difference
			final List<DiffElement> findCrossReferences = findCrossReferences(next, diffSide, crossReferencer);
			for (DiffElement diffElement : findCrossReferences) {
				if (expectedDiff.isInstance(diffElement)) {
					result.add(diffElement);
				}
			}
		}
		return result;
	}

	/**
	 * Retrieve all the stereotype applied on the given UML element and its children (all descendants).
	 * 
	 * @param elt
	 *            The UML element.
	 * @return The list of stereotype applications.
	 */
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
