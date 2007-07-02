/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge.api;

import java.util.HashMap;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.generic.merge.impl.AddAttributeMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.AddModelElementMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.AddReferenceValueMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.DefaultMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.MoveModelElementMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.RemoveAttributeMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.RemoveModelElementMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.RemoveReferenceValueMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.UpdateAttributeMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.UpdateUniqueReferenceValueMerger;
import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;

/**
 * The merge factory allows the creation of a merger from any kind of {@link DiffElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class MergeFactory {
	private static final HashMap<Class<? extends DiffElement>, Class<? extends AbstractMerger>> MERGER_TYPES = new HashMap<Class<? extends DiffElement>, Class<? extends AbstractMerger>>();

	/**
	 * Associates basic {@link DiffElement}s with basic merger implementations.
	 */
	static {
		MERGER_TYPES.put(AddModelElement.class, AddModelElementMerger.class);
		MERGER_TYPES.put(RemoveModelElement.class, RemoveModelElementMerger.class);
		MERGER_TYPES.put(MoveModelElement.class, MoveModelElementMerger.class);
		MERGER_TYPES.put(AddReferenceValue.class, AddReferenceValueMerger.class);
		MERGER_TYPES.put(RemoveReferenceValue.class, RemoveReferenceValueMerger.class);
		MERGER_TYPES.put(UpdateUniqueReferenceValue.class, UpdateUniqueReferenceValueMerger.class);
		MERGER_TYPES.put(AddAttribute.class, AddAttributeMerger.class);
		MERGER_TYPES.put(RemoveAttribute.class, RemoveAttributeMerger.class);
		MERGER_TYPES.put(UpdateAttribute.class, UpdateAttributeMerger.class);
	}

	private MergeFactory() {
		// prevents instantiation
	}

	/**
	 * Associates {@link DiffElement}s of the given class with the given merger.
	 * <p>
	 * Mergers must extend {@link AbstractMerger org.eclipse.emf.compare.merge.api.AbstractMerger} and provide
	 * a default constructor.
	 * 
	 * @param diffClass
	 *            {@link Class} of the {@link DiffElement}s to associate with <code>mergerClass</code>.
	 * @param mergerClass
	 *            {@link Class} of the merger for these {@link DiffElement}.
	 */
	public static void addMergerType(Class<? extends DiffElement> diffClass,
			Class<? extends AbstractMerger> mergerClass) {
		MERGER_TYPES.put(diffClass, mergerClass);
	}

	/**
	 * Unregisters the merger for the given {@link DiffElement} class.
	 * 
	 * @param diffClass
	 *            {@link Class} we want to unregister the merger for.
	 */
	public static void removeMergerType(Class<? extends DiffElement> diffClass) {
		MERGER_TYPES.remove(diffClass);
	}

	/**
	 * Handles the creation of the merger for a given {@link DiffElement}.
	 * 
	 * @param element
	 *            {@link DiffElement} for which we need a merger.
	 * @return The merger adapted to <code>element</code>, <code>null</code> if it cannot be
	 *         instantiated.
	 */
	public static AbstractMerger createMerger(DiffElement element) {
		final Class<? extends AbstractMerger> mergerClass = getBestMerger(element);

		// If the merger provides a default constructor, we instantiate it
		AbstractMerger elementMerger = null;
		try {
			elementMerger = mergerClass.newInstance();
			elementMerger.setElement(element);
		} catch (InstantiationException e) {
			EMFComparePlugin.getDefault().log(e.getMessage(), false);
		} catch (IllegalAccessException e) {
			EMFComparePlugin.getDefault().log(e.getMessage(), false);
		}

		return elementMerger;
	}

	private static Class<? extends AbstractMerger> getBestMerger(DiffElement element) {
		Class<? extends AbstractMerger> mergerClass = DefaultMerger.class;
		// If we know the merger for this class, we return it
		if (MERGER_TYPES.containsKey(element.getClass())) {
			mergerClass = MERGER_TYPES.get(element.getClass());
			// Else we seek through the implemented interfaces whether we know the merger for one
		} else {
			for (Class clazz : element.getClass().getInterfaces()) {
				if (MERGER_TYPES.containsKey(clazz)) {
					mergerClass = MERGER_TYPES.get(clazz);
					break;
				}
			}
		}
		return mergerClass;
	}
}
