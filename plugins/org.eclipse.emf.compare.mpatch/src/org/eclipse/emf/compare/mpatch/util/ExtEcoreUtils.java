/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;

/**
 * Helper class for miscellaneous ecore related operations.
 * 
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ExtEcoreUtils {

	/**
	 * Dynamically create an {@link EObject} with a collection called <code>children</code> which contains the given
	 * elements.
	 * 
	 * @param collection
	 *            The initial filling of the collection.
	 * @return An {@link EObject} which just has a containment list (named <code>children</code>) of type
	 *         {@link EObject}.
	 */
	@SuppressWarnings("unchecked")
	public static EObject wrapInGenericContainer(Collection<? extends EObject> collection) {

		// create the wrapper containment reference
		final EReference children = EcoreFactory.eINSTANCE.createEReference();
		children.setName("children");
		children.setLowerBound(0);
		children.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
		children.setContainment(true);
		children.setEType(EcorePackage.Literals.EOBJECT);

		// create the wrapper class
		final EClass containerClass = EcoreFactory.eINSTANCE.createEClass();
		containerClass.setName("GenericEContainer");
		containerClass.getEStructuralFeatures().add(children);

		// create a package for our wrapper class
		final EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("dynamic");
		ePackage.setNsPrefix("dynamic");
		ePackage.setNsURI("http://www.example.org/dynamic");
		ePackage.getEClassifiers().add(containerClass);

		// validate what we just created
		if (Diagnostician.INSTANCE.validate(ePackage).getCode() == Diagnostic.OK) {

			// instantiate our class, fill the children and return it
			final EObject container = ePackage.getEFactoryInstance().create(containerClass);
			((EList<EObject>) container.eGet(children)).addAll(collection);
			return container;
		} else {
			throw new UnknownError(
					"Dynamic EMF wrapper model creation failed for whatever reason. Developer, please debug better!");
		}
	}

	/**
	 * This basically calls a setter via EMF reflection to set a structural feature.
	 * <ul>
	 * <li>If <code>value</code> is a {@link List} of elements, the feature <code>ref</code> must, of course, also be a
	 * list.
	 * <li>If <code>value</code> is a single element, then <code>ref</code> might have multiplicity <code>1</code> or it
	 * might also be a list. In the latter case, <code>value</code> is added to the list.
	 * </ul>
	 * 
	 * @param obj
	 *            The object which holds the feature to set.
	 * @param ref
	 *            The feature which should be set.
	 * @param value
	 *            The value that should be set.
	 * @return <code>true</code>, if the value could be set or <b>if it was already set</b>; <code>false</code>
	 *         otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static boolean setStructuralFeature(EObject obj, EStructuralFeature ref, Object value) {
		if (!ref.isChangeable()) {
			throw new IllegalArgumentException("Cannot set a non-changeable reference: " + obj.eClass().getName() + "."
					+ ref.getName());
		}
		try {
			if (ref.isMany()) {
				final List list = (List) obj.eGet(ref);
				if (value instanceof List) {
					final List valueList = (List) value;
					for (final Object listValue : valueList) {
						if (!list.contains(listValue) && !list.add(listValue)) {
							return false;
						}
					}
				} else {
					if (!list.contains(value) && !list.add(value)) {
						return false;
					}
				}
				return true;
			} else {
				if (value instanceof List) {
					final List valueList = (List) value;
					if (valueList.size() > 1) {
						throw new IllegalArgumentException("Cannot set a list of values to a non-many feature!");
					} else if (valueList.size() == 1) {
						if (obj.eGet(ref) == null || !obj.eGet(ref).equals(valueList.get(0))) {
							obj.eSet(ref, valueList.get(0));
						}
					} else {
						obj.eSet(ref, null);
					}
				} else {
					if (obj.eGet(ref) == null || !obj.eGet(ref).equals(value)) {
						obj.eSet(ref, value);
					}
				}
				return true;
			}
		} catch (final Exception e) {
			throw new IllegalArgumentException("Could not set value (" + value + ") to: " + obj.eClass().getName()
					+ "." + ref.getName() + " of object " + obj, e);
		}
	}

	/**
	 * Returns a new collection holding references to all {@link EObject}s of the given elements. That is, the tree of
	 * elements is flattened into one collection. The elements, however, are not modified.
	 * 
	 * @param elements
	 *            A collection of {@link EObject}s.
	 * @return A new, flat list of all elements.
	 */
	public static List<? extends EObject> flattenEObjects(Collection<? extends EObject> elements) {
		final ArrayList<EObject> result = new ArrayList<EObject>();
		final Queue<EObject> queue = new ArrayDeque<EObject>();
		queue.addAll(elements);
		while (!queue.isEmpty()) {
			final EObject obj = queue.poll();
			result.add(obj);
			queue.addAll(obj.eContents());
		}
		return result;
	}

	/**
	 * Return all elements in a flat list which have the type given in <code>types</code>. The entire model tree is
	 * searched, i.e. it is a deep search.
	 * 
	 * @param elements
	 *            A set of elements.
	 * @param types
	 *            The types which should be returned.
	 * @param includeSubtypes
	 *            If <code>true</code>, then also subtypes of the given types are included in the result.
	 * @return A list of all elements which are of a type that is given in <code>types</code>.
	 */
	public static List<EObject> collectTypedElements(final List<? extends EObject> elements, final Set<EClass> types,
			boolean includeSubtypes) {
		final List<EObject> result = new ArrayList<EObject>();
		final Queue<EObject> queue = new ArrayDeque<EObject>();
		queue.addAll(elements);
		while (!queue.isEmpty()) {
			final EObject element = queue.poll();
			if (includeSubtypes) {
				for (EClass eClass : types) {
					if (eClass.isSuperTypeOf(element.eClass())) {
						result.add(element);
						break;
					}
				}
			} else {
				if (types.contains(element.eClass()))
					result.add(element);
			}
			queue.addAll(element.eContents());
		}
		return result;
	}

	/**
	 * This iterates over the entire contents and checks whether dangling references exist. If some are found, they are
	 * printed to the console (disabled) and the call returns the number of dangling references found.
	 * 
	 * @param resource
	 *            A resource.
	 * @return The number of dangling references.
	 */
	public static int checkDanglingReferences(Resource resource) {
		int counter = 0;
		final TreeIterator<EObject> iter = resource.getAllContents();
		while (iter.hasNext()) {
			final EObject obj = iter.next();
			for (EObject ref : obj.eCrossReferences()) {
				if (ref.eResource() == null) {
					counter++;
					// System.out.println("Dangling refrence found: " + ref);
				}
			}
		}
		return counter;
	}

	/**
	 * Transitively find the parent of type <code>container</code> of <code>element</code>. If <code>element</code> is
	 * already of type <code>container</code>, the element will be returned.
	 * 
	 * @param element
	 *            An arbitrary {@link EObject}.
	 * @param container
	 *            The parent type we are looking for.
	 * @return The parent of <code>element</code> of type <code>container</code> or <code>null</code>, if none is found.
	 */
	public static EObject getContainerOfType(EObject element, EClass container) {
		if (element == null || container == null)
			return null;
		if (container.isInstance(element))
			return element;
		return getContainerOfType(element.eContainer(), container);
	}

}
