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
package org.eclipse.emf.compare.mpatch.transform.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.symrefs.Condition;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;

/**
 * This compares differences and creates groups for them. In case of big lists, this operation might be very
 * time-consuming, since each call operates in O(n^2)!
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class MergeChangesCompare {

	/**
	 * Compares two changes and checks whether they can be generalized or not.
	 * 
	 * @param change1
	 *            One change.
	 * @param change2
	 *            Another change.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	static boolean generalizable(IndepChange change1, IndepChange change2) {
		final EClass eClass = change1.eClass();
		if (eClass == null || !eClass.equals(change2.eClass())) {
			return false;
		}

		// check corresponding element symbolic reference
		if (!areGeneralizableSymbolicReferences(change1.getCorrespondingElement(),
				change2.getCorrespondingElement())) {
			return false;
		}

		// individual checks
		final boolean isGeneralizable;
		if (MPatchPackage.Literals.INDEP_ADD_ATTRIBUTE_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableAddRemAttributeChanges((IndepAddAttributeChange)change1,
					(IndepAddAttributeChange)change2);
		} else if (MPatchPackage.Literals.INDEP_REMOVE_ATTRIBUTE_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableAddRemAttributeChanges((IndepRemoveAttributeChange)change1,
					(IndepRemoveAttributeChange)change2);
		} else if (MPatchPackage.Literals.INDEP_UPDATE_ATTRIBUTE_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableUpdateAttributeChanges((IndepUpdateAttributeChange)change1,
					(IndepUpdateAttributeChange)change2);
		} else if (MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableAddRemElementChanges((IndepAddElementChange)change1,
					(IndepAddElementChange)change2);
		} else if (MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableAddRemElementChanges((IndepRemoveElementChange)change1,
					(IndepRemoveElementChange)change2);
		} else if (MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableMoveElementChanges((IndepMoveElementChange)change1,
					(IndepMoveElementChange)change2);
		} else if (MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableAddRemReferenceChanges((IndepAddReferenceChange)change1,
					(IndepAddReferenceChange)change2);
		} else if (MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableAddRemReferenceChanges((IndepRemoveReferenceChange)change1,
					(IndepRemoveReferenceChange)change2);
		} else if (MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE.equals(eClass)) {
			isGeneralizable = generzlizableUpdateReferenceChanges((IndepUpdateReferenceChange)change1,
					(IndepUpdateReferenceChange)change2);
		} else {
			isGeneralizable = false;
		}
		return isGeneralizable;
	}

	/**
	 * Two add/remove attribute changes are generalizable, if the following conditions hold:
	 * <ul>
	 * <li>The value will be added/removed to the same {@link EAttribute}.
	 * <li>The attribute values are the same.
	 * </ul>
	 * 
	 * @param change1
	 *            The first change of comparison.
	 * @param change2
	 *            The second change of comparison.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	private static boolean generzlizableAddRemAttributeChanges(IndepAddRemAttributeChange change1,
			IndepAddRemAttributeChange change2) {

		// check attributes
		if (change1.getChangedAttribute() == null
				|| !change1.getChangedAttribute().equals(change2.getChangedAttribute())) {
			return false;
		}

		// check attribute values
		if (change1.getValue() == null ^ change2.getValue() == null) {
			return false;
		}
		if (change1.getValue() != null && !change1.getValue().equals(change2.getValue())) {
			return false;
		}

		return true; // generalizable!!!
	}

	/**
	 * Two update attribute changes are generalizable, if the following conditions hold:
	 * <ul>
	 * <li>The value will be changed at the same {@link EAttribute}.
	 * <li>The old and new attribute values are the same.
	 * </ul>
	 * 
	 * @param change1
	 *            The first change of comparison.
	 * @param change2
	 *            The second change of comparison.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	private static boolean generzlizableUpdateAttributeChanges(IndepUpdateAttributeChange change1,
			IndepUpdateAttributeChange change2) {

		// check attributes
		if (change1.getChangedAttribute() == null
				|| !change1.getChangedAttribute().equals(change2.getChangedAttribute())) {
			return false;
		}

		// check attribute values
		if (!equalAttributeValues(change1.getOldValue(), change2.getOldValue())) {
			return false;
		}
		if (!equalAttributeValues(change1.getNewValue(), change2.getNewValue())) {
			return false;
		}

		return true; // generalizable!!!
	}

	/**
	 * Check whether the two given attribute values are equal.
	 * 
	 * @param value1
	 *            One value.
	 * @param value2
	 *            Another value.
	 * @return <code>true</code> if they are equal, <code>false</code> otherwise.
	 */
	private static boolean equalAttributeValues(Object value1, Object value2) {
		if (value1 == null ^ value2 == null) {
			return false;
		}
		if (value1 != null && !value1.equals(value2)) {
			return false;
		}
		return true;
	}

	/**
	 * Two add/remove element changes are generalizable, if the following conditions hold:
	 * <ul>
	 * <li>The element will be added to/removed from the same {@link EReference}.
	 * <li>The added/removed model descriptors equal.
	 * </ul>
	 * 
	 * @param change1
	 *            The first change of comparison.
	 * @param change2
	 *            The second change of comparison.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	private static boolean generzlizableAddRemElementChanges(IndepAddRemElementChange change1,
			IndepAddRemElementChange change2) {

		// check containment references
		if (change1.getContainment() == null || !change1.getContainment().equals(change2.getContainment())) {
			return false;
		}

		// check self references (these will be merged!)
		if (!areGeneralizableSymbolicReferences(change1.getSubModelReference(),
				change2.getSubModelReference())) {
			return false;
		}

		// check model descriptors
		if (change1.getSubModel() == null || !change1.getSubModel().describesEqual(change2.getSubModel())) {
			return false;
		}

		// check whether both changes are cross-referenced equally, e.g. via ModelDescriptorReferences
		final EObject mpatch = ExtEcoreUtils.getContainerOfType(change1, MPatchPackage.Literals.MPATCH_MODEL);
		if (!sameCrossReferences(change1, change2, mpatch)) {
			return false;
		}

		return true; // generalizable!!!
	}

	/**
	 * Check whether these two objects can be merged regarding their cross references.
	 * 
	 * @param obj1
	 *            An EObject.
	 * @param obj2
	 *            Another EObject.
	 * @param root
	 *            Their common root element.
	 * @return <code>true</code>, if thez have the same cross-references (thus, can be merged).
	 *         <code>false</code> otherwise.
	 */
	private static boolean sameCrossReferences(EObject obj1, EObject obj2, EObject root) {
		final List<? extends EObject> flat1 = ExtEcoreUtils.flattenEObjects(Collections.singleton(obj1));
		final List<? extends EObject> flat2 = ExtEcoreUtils.flattenEObjects(Collections.singleton(obj2));
		final Map<EObject, Collection<Setting>> refs1 = UsageCrossReferencer.findAll(flat1, root);
		final Map<EObject, Collection<Setting>> refs2 = UsageCrossReferencer.findAll(flat2, root);

		// trivial but efficient check
		// if (refs1.size() != refs2.size())
		// return false;
		if (refs1.isEmpty() && refs1.isEmpty()) {
			return true;
		}

		/*
		 * FIXME: At the moment, I cannot see how multiple changes can be merged if they have
		 * cross-references. The problem is that the powerset will be created during change application, and,
		 * thus, the semantics completely change!!! So for now, we don't allow any merges if there are
		 * cross-references!
		 */
		return onlyDependencyReferences(refs1, flat1) && onlyDependencyReferences(refs2, flat2);
		// return exactlySameCrossReferences(obj1, obj2, refs1, refs2, flat1, flat2);
	}

	/**
	 * Check whether the cross references in refs are internal only, i.e. they point only to elements in flat.
	 * This ignores any dependency references.
	 * 
	 * @param refs
	 *            A mapping of cross references.
	 * @param flat
	 *            A flat list of contents elements.
	 * @return <code>true</code> if the only cross references are internal. <code>false</code> if there exists
	 *         any cross-reference that refers to an elements not in flat.
	 */
	private static boolean onlyDependencyReferences(Map<EObject, Collection<Setting>> refs,
			List<? extends EObject> flat) {
		for (EObject key : refs.keySet()) {
			final Collection<Setting> settings = refs.get(key);
			for (Setting setting : settings) {
				if (MPatchPackage.Literals.INDEP_CHANGE__DEPENDANTS.equals(setting.getEStructuralFeature())
						|| MPatchPackage.Literals.INDEP_CHANGE__DEPENDS_ON.equals(setting
								.getEStructuralFeature())) {
					continue;
				}
				if (!flat.contains(setting.getEObject())) {
					return false; // this is a cross-reference!!!
				}
			}
		}
		return true;
	}

	/**
	 * Checks whether obj1 and obj2 have exactly the same cross references, ignoring dependency references.
	 * 
	 * @param obj1
	 *            An EObject.
	 * @param obj2
	 *            Another EObject.
	 * @param refs1
	 *            All cross references of obj1.
	 * @param refs2
	 *            All cross references of obj2.
	 * @param flat1
	 *            A flat content tree of obj1.
	 * @param flat2
	 *            A flat content tree of obj2.
	 * @return Whether obj1 and obj2 have the same cross references.
	 * @deprecated This method is *probably* not used anymore. Having exactly the same cross-references is
	 *             very unlikely! And even if they have different cross-references, they can still be merged.
	 *             Just during difference application, they need to be resolved properly by the user. So there
	 *             shouldn't be a problem.
	 */
	@Deprecated
	protected static boolean exactlySameCrossReferences(EObject obj1, EObject obj2,
			Map<EObject, Collection<Setting>> refs1, Map<EObject, Collection<Setting>> refs2,
			List<? extends EObject> flat1, List<? extends EObject> flat2) {
		// more detailed check requires matching elements in obj1 and obj2!
		final Map<EObject, EObject> match = CommonUtils.getMatchingObjects(obj1, obj2);

		// comparing refs1 and refs2
		outer1: for (EObject key1 : refs1.keySet()) {
			final EObject key2 = match.get(key1);
			if (refs2.keySet().contains(key2)) {

				// if we got a match here; check all settings
				final Collection<Setting> settings1 = refs1.get(key1);
				final Collection<Setting> settings2 = refs2.get(key2);

				// trivial checks
				if (settings1 == null && settings2 == null) {
					continue outer1; // nothing to compare here, still equal.
				}
				if (settings1 == null ^ settings2 == null) {
					return false; // definitely not equal!
				}
				if (settings1 == null || settings2 == null || settings1.size() != settings2.size()) {
					return false;
				}

				// actual check
				outer2: for (Setting setting1 : settings1) {
					for (Setting setting2 : settings2) {
						final EObject eobj1 = setting1.getEObject();
						final EObject eobj2 = setting2.getEObject();
						final EStructuralFeature feature1 = setting1.getEStructuralFeature();
						final EStructuralFeature feature2 = setting2.getEStructuralFeature();

						// unfortunately, the cross referencer also finds internal references! ignore them
						// here!
						if (flat1.contains(eobj1) && flat2.contains(eobj2)) {
							continue outer2;
						}

						if (eobj1.equals(eobj2) && feature1.equals(feature2)) {
							continue outer2; // we found a match setting1 = setting2!
						}
					}
					// System.out.println("No match found for: " + setting1);
					return false; // coudn't find a match for setting1!
				}

				continue outer1; // we found a match key1 == key2!
			}
			// System.out.println("No match found for: " + key1);
			return false; // couldn't find a match for key1!
		}
		return true; // everything matched :-D
	}

	/**
	 * Two move element changes are generalizable, if the following conditions hold:
	 * <ul>
	 * <li>The element will be moved from the same and to the same parent.
	 * <li>The element will be moved from/to the same {@link EReference}.
	 * </ul>
	 * 
	 * @param change1
	 *            The first change of comparison.
	 * @param change2
	 *            The second change of comparison.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	private static boolean generzlizableMoveElementChanges(IndepMoveElementChange change1,
			IndepMoveElementChange change2) {

		// check parents and containments
		if (change1.getOldParent() == null || change1.getNewParent() == null
				|| change2.getOldParent() == null || change2.getNewParent() == null) {
			return false;
		}
		if (change1.getOldContainment() == null || change1.getNewContainment() == null
				|| change2.getOldContainment() == null || change2.getNewContainment() == null) {
			return false;
		}
		if (!change1.getOldParent().resolvesEqual(change2.getOldParent())) {
			return false;
		}
		if (!change1.getNewParent().resolvesEqual(change2.getNewParent())) {
			return false;
		}
		if (!change1.getOldContainment().equals(change2.getOldContainment())) {
			return false;
		}
		if (!change1.getNewContainment().equals(change2.getNewContainment())) {
			return false;
		}

		return true;
	}

	/**
	 * Two add/remove reference changes are generalizable, if the following conditions hold:
	 * <ul>
	 * <li>The reference will be added/removed to the same {@link EReference}.
	 * <li>The added/removed referenced element is the same.
	 * </ul>
	 * 
	 * @param change1
	 *            The first change of comparison.
	 * @param change2
	 *            The second change of comparison.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	private static boolean generzlizableAddRemReferenceChanges(IndepAddRemReferenceChange change1,
			IndepAddRemReferenceChange change2) {

		// check eReference
		if (change1.getReference() == null || !change1.getReference().equals(change2.getReference())) {
			return false;
		}

		// check target
		if (!change1.getChangedReference().resolvesEqual(change2.getChangedReference())) {
			return false;
		}

		return true;
	}

	/**
	 * Two update reference changes are generalizable, if the following conditions hold:
	 * <ul>
	 * <li>The updated reference is the same {@link EReference}.
	 * <li>The old and new referenced element are the same.
	 * </ul>
	 * 
	 * @param change1
	 *            The first change of comparison.
	 * @param change2
	 *            The second change of comparison.
	 * @return <code>true</code>, if these two changes can be generalized or <code>false</code>, if they
	 *         cannot.
	 */
	private static boolean generzlizableUpdateReferenceChanges(IndepUpdateReferenceChange change1,
			IndepUpdateReferenceChange change2) {

		// check eReference
		if (change1.getReference() == null || !change1.getReference().equals(change2.getReference())) {
			return false;
		}

		// check target
		if (change1.getOldReference() == null ^ change2.getOldReference() == null) {
			return false;
		}
		if (change1.getNewReference() == null ^ change2.getNewReference() == null) {
			return false;
		}
		if (change1.getOldReference() != null
				&& !change1.getOldReference().resolvesEqual(change2.getOldReference())) {
			return false;
		}
		if (change1.getNewReference() != null
				&& !change1.getNewReference().resolvesEqual(change2.getNewReference())) {
			return false;
		}

		return true;
	}

	/**
	 * Check whether the given symbolic references are generalizable:<br>
	 * They must be an instance of {@link ElementSetReference} with {@link OclCondition}s and their types and
	 * contexts must be equal.
	 * 
	 * @param symref1
	 *            A symbolic reference.
	 * @param symref1
	 *            Another symbolic reference.
	 * @return <code>true</code>, if they are generalizable, <code>false</code> otherwise.
	 */
	private static boolean areGeneralizableSymbolicReferences(IElementReference symref1,
			IElementReference symref2) {

		// check types
		if (!(symref1 instanceof ElementSetReference) || !(symref2 instanceof ElementSetReference)) {
			return false;
		}
		final ElementSetReference setref1 = (ElementSetReference)symref1;
		final ElementSetReference setref2 = (ElementSetReference)symref2;

		/*
		 * At the moment, we only allow symbolic references with ONE condition to be generalized!!! In the
		 * future, more complex condition are imaginable like composed conditions to realize OR and AND.
		 * However, in order to merge conditions (the only generalization implemented at the moment), we must
		 * ensure that there is only one condition!
		 */
		if (setref1.getConditions().size() != 1 || setref2.getConditions().size() != 1) {
			return false;
		}

		// check conditions
		for (Condition condition : setref1.getConditions()) {
			if (!(condition instanceof OclCondition)) {
				return false;
			}
		}
		for (Condition condition : setref2.getConditions()) {
			if (!(condition instanceof OclCondition)) {
				return false;
			}
		}

		// check referenced model element types
		if (!symref1.getType().equals(symref2.getType())) {
			return false;
		}

		// perform the same check on the context, if it exists
		if (setref1.getContext() == null ^ setref2.getContext() == null) {
			return false;
		}
		if (setref1.getContext() != null
				&& !areGeneralizableSymbolicReferences(setref1.getContext(), setref2.getContext())) {
			return false;
		}

		return true; // generalizable!!!
	}
}
