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
package org.eclipse.emf.compare.diff.merge.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.generic.merge.impl.AttributeChangeLeftTargetMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.AttributeChangeRightTargetMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.DefaultMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.ModelElementChangeLeftTargetMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.ModelElementChangeRightTargetMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.MoveModelElementMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.ReferenceChangeLeftTargetMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.ReferenceChangeRightTargetMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.UpdateAttributeMerger;
import org.eclipse.emf.compare.diff.generic.merge.impl.UpdateUniqueReferenceValueMerger;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;

/**
 * The merge factory allows the creation of a merger from any kind of {@link DiffElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class MergeFactory {
	/**
	 * This {@link ConcurrentHashMap map} keeps a bridge between a given {@link DiffElement}'s class and the most accurate merger's class for that particular
	 * {@link DiffElement}.
	 */
	private static final Map<Class<? extends DiffElement>, Class<? extends AbstractMerger>> MERGER_TYPES = new ConcurrentHashMap<Class<? extends DiffElement>, Class<? extends AbstractMerger>>(32);

	/**
	 * Associates basic {@link DiffElement}s with generic merger implementations.
	 */
	static {
		MERGER_TYPES.put(ModelElementChangeRightTarget.class, ModelElementChangeRightTargetMerger.class);
		MERGER_TYPES.put(ModelElementChangeLeftTarget.class, ModelElementChangeLeftTargetMerger.class);
		MERGER_TYPES.put(MoveModelElement.class, MoveModelElementMerger.class);
		MERGER_TYPES.put(ReferenceChangeRightTarget.class, ReferenceChangeRightTargetMerger.class);
		MERGER_TYPES.put(ReferenceChangeLeftTarget.class, ReferenceChangeLeftTargetMerger.class);
		MERGER_TYPES.put(UpdateUniqueReferenceValue.class, UpdateUniqueReferenceValueMerger.class);
		MERGER_TYPES.put(AttributeChangeRightTarget.class, AttributeChangeRightTargetMerger.class);
		MERGER_TYPES.put(AttributeChangeLeftTarget.class, AttributeChangeLeftTargetMerger.class);
		MERGER_TYPES.put(UpdateAttribute.class, UpdateAttributeMerger.class);
	}

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private MergeFactory() {
		// prevents instantiation
	}

	/**
	 * Associates {@link DiffElement}s of the given class with the given merger.
	 * <p>
	 * Mergers must extend {@link AbstractMerger org.eclipse.emf.compare.merge.api.AbstractMerger} and provide a default constructor.
	 * 
	 * @param diffClass
	 *            {@link Class} of the {@link DiffElement}s to associate with <code>mergerClass</code>.
	 * @param mergerClass
	 *            {@link Class} of the merger for these {@link DiffElement}.
	 */
	public static void addMergerType(Class<? extends DiffElement> diffClass, Class<? extends AbstractMerger> mergerClass) {
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
	 * @return The merger adapted to <code>element</code>, <code>null</code> if it cannot be instantiated.
	 */
	public static AbstractMerger createMerger(DiffElement element) {
		final Class<? extends AbstractMerger> mergerClass = getBestMerger(element);

		// If the merger provides a default constructor, we instantiate it
		AbstractMerger elementMerger = null;
		try {
			elementMerger = mergerClass.newInstance();
			elementMerger.setElement(element);
		} catch (InstantiationException e) {
			EMFComparePlugin.log(e.getMessage(), false);
		} catch (IllegalAccessException e) {
			EMFComparePlugin.log(e.getMessage(), false);
		}

		return elementMerger;
	}

	/**
	 * Returns the merger class that is best suited for the given {@link DiffElement}. Merger classes can be managed via
	 * {@link #addMergerType(Class, Class)} and {@link #removeMergerType(Class)}.
	 * 
	 * @param element
	 *            {@link DiffElement} we want a merger for.
	 * @return The merger class that is best suited for the given {@link DiffElement}.
	 */
	private static Class<? extends AbstractMerger> getBestMerger(DiffElement element) {
		Class<? extends AbstractMerger> mergerClass = DefaultMerger.class;
		// If we know the merger for this class, we return it
		if (MERGER_TYPES.containsKey(element.getClass())) {
			mergerClass = MERGER_TYPES.get(element.getClass());
			// Else we seek through the map if our element is an instance of one of the class keys.
		} else {
			for (Class clazz : MERGER_TYPES.keySet()) {
				if (clazz.isInstance(element)) {
					mergerClass = MERGER_TYPES.get(clazz);
					break;
				}
			}
		}
		return mergerClass;
	}
}
