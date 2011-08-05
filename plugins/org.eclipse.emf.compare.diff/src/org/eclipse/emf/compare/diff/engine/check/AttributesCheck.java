/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Taal - [299641] Compare arrays by their content instead of instance equality - Note : moved to DiffCollectionsHelper
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeOrderChange;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Elements;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This will implement the attribute checks : order of attribute values, changes between two versions, ...
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 * @since 1.0
 */
public class AttributesCheck extends AbstractCheck {
	/**
	 * Simply delegates to the super constructor.
	 * 
	 * @param referencer
	 *            CrossReferencer instantiated with the match model or match resource set.
	 * @see {@link AbstractCheck#DefaultCheck(org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)}
	 */
	public AttributesCheck(EcoreUtil.CrossReferencer referencer) {
		super(referencer);
	}

	/**
	 * This will iterate through all the attributes of the <code>mapping</code>'s two elements to check if any
	 * of them has been modified.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create if one of the attributes has
	 *            actually been changed.
	 * @param mapping
	 *            This contains the mapping information about the elements we need to check.
	 * @throws FactoryException
	 *             Thrown if one of the checks fails.
	 */
	public void checkAttributesUpdates(DiffGroup root, Match2Elements mapping) throws FactoryException {
		final EClass eClass = mapping.getLeftElement().eClass();

		final List<EAttribute> eclassAttributes = eClass.getEAllAttributes();
		// for each feature, compare the value
		final Iterator<EAttribute> it = eclassAttributes.iterator();
		while (it.hasNext()) {
			final EAttribute next = it.next();
			if (!shouldBeIgnored(next)) {
				checkAttributeUpdates(root, mapping, next);
			}
		}
	}

	/**
	 * This will iterate through all the attributes of the <code>mapping</code>'s three elements to check if
	 * any of them has been modified.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create if one of the attribute has
	 *            actually been changed.
	 * @param mapping
	 *            This contains the mapping information about the elements we need to check for a move.
	 * @throws FactoryException
	 *             Thrown if one of the checks fails.
	 */
	public void checkAttributesUpdates(DiffGroup root, Match3Elements mapping) throws FactoryException {
		// Ignores matchElements when they don't have origin (no updates on
		// these)
		if (mapping.getOriginElement() == null)
			return;
		final EClass eClass = mapping.getOriginElement().eClass();

		final List<EAttribute> eclassAttributes = eClass.getEAllAttributes();
		// for each feature, compare the value
		final Iterator<EAttribute> it = eclassAttributes.iterator();
		while (it.hasNext()) {
			final EAttribute next = it.next();
			if (!shouldBeIgnored(next)) {
				checkAttributeUpdates(root, mapping, next);
			}
		}
	}

	/**
	 * This will be used internaly to check that an attribute's values have changed from one version to the
	 * other.
	 * <p>
	 * Specifically, this will check for :
	 * <ul>
	 * <li>Enumeration literals : if they have the same literal and integer values.</li>
	 * <li>Feature Map Entries : if their respective values have been matched.</li>
	 * <li>Arrays : compare the content of the two arrays.</li>
	 * <li>Other : if the left value is equal to the right value or both are <code>null</code>.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param left
	 *            The value of the attribute from the left compare resource.
	 * @param right
	 *            The value of the attribute from the right compare resource.
	 * @return <code>true</code> if the <code>left</code> value is distinct from the <code>right</code> value.
	 */
	protected boolean areDistinctValues(Object left, Object right) {
		return matcherHelper.areDistinctValues(left, right);
	}

	/**
	 * This will check that the values of the given attribute from the objects contained by mapping has been
	 * modified.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create if one of the attributes has
	 *            actually been changed.
	 * @param mapping
	 *            This contains the mapping information about the elements we need to check.
	 * @param attribute
	 *            The attribute we need to check for differences.
	 * @throws FactoryException
	 *             Thrown if one of the checks fails.
	 * @since 1.0
	 */
	protected void checkAttributeUpdates(DiffGroup root, Match2Elements mapping, EAttribute attribute)
			throws FactoryException {
		final String attributeName = attribute.getName();

		boolean distinct = false;
		if (attribute.isMany()) {
			final List<Object> leftValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getLeftElement(), attributeName));
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getRightElement(), attributeName));

			if (leftValue.size() != rightValue.size()) {
				distinct = true;
			} else {
				for (int i = 0; !distinct && i < leftValue.size(); i++) {
					distinct = areDistinctValues(leftValue.get(i), rightValue.get(i));
				}
			}
		} else {
			final Object leftValue = EFactory.eGet(mapping.getLeftElement(), attributeName);
			final Object rightValue = EFactory.eGet(mapping.getRightElement(), attributeName);

			distinct = areDistinctValues(leftValue, rightValue);
		}

		if (distinct) {
			createNonConflictingAttributeChange(root, attribute, mapping.getLeftElement(),
					mapping.getRightElement());
		}
	}

	/**
	 * This will check that the values of the given attribute from the objects contained by mapping has been
	 * modified.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create if one of the attributes has
	 *            actually been changed.
	 * @param mapping
	 *            This contains the mapping information about the elements we need to check.
	 * @param attribute
	 *            The attribute we need to check for differences.
	 * @throws FactoryException
	 *             Thrown if one of the checks fails.
	 * @since 1.0
	 */
	protected void checkAttributeUpdates(DiffGroup root, Match3Elements mapping, EAttribute attribute)
			throws FactoryException {
		final String attributeName = attribute.getName();

		boolean rightDistinctFromOrigin = false;
		boolean rightDistinctFromLeft = false;
		boolean leftDistinctFromOrigin = false;
		if (attribute.isMany()) {
			final List<Object> leftValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getLeftElement(), attributeName));
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getRightElement(), attributeName));
			final List<Object> ancestorValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getOriginElement(), attributeName));

			if (rightValue.size() != ancestorValue.size()) {
				rightDistinctFromOrigin = true;
			} else {
				for (int i = 0; !rightDistinctFromOrigin && i < rightValue.size(); i++) {
					rightDistinctFromOrigin = areDistinctValues(rightValue.get(i), ancestorValue.get(i));
				}
			}
			if (leftValue.size() != ancestorValue.size()) {
				leftDistinctFromOrigin = true;
			} else {
				for (int i = 0; !leftDistinctFromOrigin && i < leftValue.size(); i++) {
					leftDistinctFromOrigin = areDistinctValues(leftValue.get(i), ancestorValue.get(i));
				}
			}
			if (leftValue.size() != rightValue.size()) {
				rightDistinctFromLeft = true;
			} else {
				for (int i = 0; !rightDistinctFromLeft && i < leftValue.size(); i++) {
					rightDistinctFromLeft = areDistinctValues(leftValue.get(i), rightValue.get(i));
				}
			}
		} else {
			final Object leftValue = EFactory.eGet(mapping.getLeftElement(), attributeName);
			final Object rightValue = EFactory.eGet(mapping.getRightElement(), attributeName);
			final Object ancestorValue = EFactory.eGet(mapping.getOriginElement(), attributeName);

			rightDistinctFromOrigin = areDistinctValues(rightValue, ancestorValue);
			rightDistinctFromLeft = areDistinctValues(rightValue, leftValue);
			leftDistinctFromOrigin = areDistinctValues(leftValue, ancestorValue);
		}

		// non conflicting change
		if (leftDistinctFromOrigin && !rightDistinctFromOrigin) {
			createNonConflictingAttributeChange(root, attribute, mapping.getLeftElement(),
					mapping.getRightElement());
			// only latest from head has changed
		} else if (rightDistinctFromOrigin && !leftDistinctFromOrigin) {
			createRemoteAttributeChange(root, attribute, mapping);
			// conflicting
		} else if (rightDistinctFromOrigin && leftDistinctFromOrigin || rightDistinctFromLeft) {
			checkConflictingAttributesUpdate(root, attribute, mapping);
		}
	}

	/**
	 * Determines if we should ignore an attribute for diff detection.
	 * <p>
	 * Default is to ignore attributes marked either
	 * <ul>
	 * <li>Transient</li>
	 * <li>Derived</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Clients should override this if they wish to ignore other attributes.
	 * </p>
	 * 
	 * @param attribute
	 *            Attribute to determine whether it should be ignored.
	 * @return <code>True</code> if attribute has to be ignored, <code>False</code> otherwise.
	 */
	protected boolean shouldBeIgnored(EAttribute attribute) {
		boolean ignore = attribute.isTransient();
		ignore = ignore || attribute.isDerived();
		return ignore;
	}

	/**
	 * Checks if there are conflictual changes between the values of the given {@link EAttribute}.<br/>
	 * <p>
	 * An attribute update is considered &quot;conflictual&quot; if it isn't multi-valued and its left value
	 * differs from the right value.
	 * </p>
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create if there actually are
	 *            conflictual changes in the mapped elements <code>attribute</code> values.
	 * @param attribute
	 *            Target {@link EAttribute} to check.
	 * @param mapping
	 *            Contains the three (ancestor, left, right) elements' mapping.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch <code>attribute</code>'s values for either one of the mapped
	 *             elements.
	 */
	private void checkConflictingAttributesUpdate(DiffGroup root, EAttribute attribute, Match3Elements mapping)
			throws FactoryException {
		if (!attribute.isMany()) {
			createConflictingAttributeChange(root, attribute, mapping);
		} else {
			final List<Object> remoteDeletedValues = new ArrayList<Object>();
			final List<Object> remoteAddedValues = new ArrayList<Object>();
			final List<Object> deletedValues = new ArrayList<Object>();
			final List<Object> addedValues = new ArrayList<Object>();

			populateThreeWayAttributeChanges(mapping, attribute, addedValues, deletedValues,
					remoteAddedValues, remoteDeletedValues);
			createRemoteAttributeDiffs(root, attribute, mapping.getLeftElement(), mapping.getRightElement(),
					remoteAddedValues, remoteDeletedValues);
			createLocalAttributeDiffs(root, attribute, mapping.getLeftElement(), mapping.getRightElement(),
					addedValues, deletedValues);
		}
	}

	/**
	 * Creates "local" Attribute diffs according to the given information.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param attribute
	 *            Attribute which value has been changed.
	 * @param leftElement
	 *            Left element of the attribute change.
	 * @param rightElement
	 *            Right element of the attribute change.
	 * @param addedValues
	 *            Values that have been added to the left element.
	 * @param deletedValues
	 *            Values that have been deleted from the left element.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the ordering diffs for this attribute.
	 */
	private void createLocalAttributeDiffs(DiffGroup root, EAttribute attribute, EObject leftElement,
			EObject rightElement, List<Object> addedValues, List<Object> deletedValues)
			throws FactoryException {
		final List<AttributeChangeLeftTarget> addedValuesDiffs = new ArrayList<AttributeChangeLeftTarget>(
				addedValues.size());
		final List<AttributeChangeRightTarget> deletedValuesDiffs = new ArrayList<AttributeChangeRightTarget>(
				deletedValues.size());

		// ADD Attribute values
		for (final Object aValue : addedValues) {
			final AttributeChangeLeftTarget operation = DiffFactory.eINSTANCE
					.createAttributeChangeLeftTarget();
			operation.setAttribute(attribute);
			operation.setRightElement(rightElement);
			operation.setLeftElement(leftElement);
			operation.setLeftTarget(aValue);
			root.getSubDiffElements().add(operation);

			addedValuesDiffs.add(operation);
		}

		// REMOVE Attribute values
		for (final Object aValue : deletedValues) {
			final AttributeChangeRightTarget operation = DiffFactory.eINSTANCE
					.createAttributeChangeRightTarget();
			operation.setAttribute(attribute);
			operation.setRightElement(rightElement);
			operation.setLeftElement(leftElement);
			operation.setRightTarget(aValue);
			root.getSubDiffElements().add(operation);

			deletedValuesDiffs.add(operation);
		}

		// ORDER CHANGE
		if (attribute.isOrdered()) {
			checkAttributeOrderChange(root, attribute, leftElement, rightElement, addedValuesDiffs,
					deletedValuesDiffs);
		}
	}

	/**
	 * Creates "remote" Attribute diffs according to the given information.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param attribute
	 *            Attribute which value has been changed.
	 * @param leftElement
	 *            Left element of the attribute change.
	 * @param rightElement
	 *            Right element of the attribute change.
	 * @param remoteDeletedValues
	 *            Values that have been deleted from the remote (right) element.
	 * @param remoteAddedValues
	 *            Values that have been added to the remote (right) element.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the ordering diffs for this attribute.
	 */
	private void createRemoteAttributeDiffs(DiffGroup root, EAttribute attribute, EObject leftElement,
			EObject rightElement, List<Object> remoteDeletedValues, List<Object> remoteAddedValues)
			throws FactoryException {
		final List<AttributeChangeLeftTarget> remoteDeletedValuesDiffs = new ArrayList<AttributeChangeLeftTarget>(
				remoteDeletedValues.size());
		final List<AttributeChangeRightTarget> remoteAddedValuesDiffs = new ArrayList<AttributeChangeRightTarget>(
				remoteAddedValues.size());

		// REMOTE REMOVE Attribute values
		for (final Object aValue : remoteDeletedValues) {
			final AttributeChangeLeftTarget operation = DiffFactory.eINSTANCE
					.createAttributeChangeLeftTarget();
			operation.setAttribute(attribute);
			operation.setRightElement(rightElement);
			operation.setLeftElement(leftElement);
			operation.setLeftTarget(aValue);
			operation.setRemote(true);
			root.getSubDiffElements().add(operation);

			remoteDeletedValuesDiffs.add(operation);
		}

		// REMOTE ADD Attribute values
		for (final Object aValue : remoteAddedValues) {
			final AttributeChangeRightTarget operation = DiffFactory.eINSTANCE
					.createAttributeChangeRightTarget();
			operation.setAttribute(attribute);
			operation.setRightElement(rightElement);
			operation.setLeftElement(leftElement);
			operation.setRightTarget(aValue);
			operation.setRemote(true);
			root.getSubDiffElements().add(operation);

			remoteAddedValuesDiffs.add(operation);
		}

		// REMOTE ORDER CHANGE
		if (attribute.isOrdered()) {
			checkAttributeRemoteOrderChange(root, attribute, leftElement, rightElement,
					remoteDeletedValuesDiffs, remoteAddedValuesDiffs);
		}
	}

	/**
	 * Checks a given {@link EAttribute attribute} for changes related to a given <code>mapping</code> and
	 * populates the given {@link List}s with the attribute values belonging to them.
	 * <p>
	 * <ul>
	 * <li>&quot;Added&quot; values are the values that have been added in the left element since the origin
	 * and that haven't been added in the right element.</li>
	 * <li>&quot;Deleted&quot; values are the values that have been removed from the left element since the
	 * origin but are still present in the right element.</li>
	 * <li>&quot;Remotely added&quot; values are the values that have been added in the right element since
	 * the origin but haven't been added in the left element.</li>
	 * <li>&quot;Remotely deleted&quot; values are the values that have been removed from the right element
	 * since the origin but are still present in the left element.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            Contains informations about the left, right and origin elements.
	 * @param attribute
	 *            {@link EAttribute} we're checking for changes.
	 * @param addedValues
	 *            {@link List} that will be populated with the values that have been added in the left element
	 *            since the origin.
	 * @param deletedValues
	 *            {@link List} that will be populated with the values that have been removed from the left
	 *            element since the origin.
	 * @param remoteAddedValues
	 *            {@link List} that will be populated with the values that have been added in the right
	 *            element since the origin.
	 * @param remoteDeletedValues
	 *            {@link List} that will be populated with the values that have been removed from the right
	 *            element since the origin.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the attribute's values in either the left, right or origin
	 *             element.
	 */
	private void populateThreeWayAttributeChanges(Match3Elements mapping, EAttribute attribute,
			List<Object> addedValues, List<Object> deletedValues, List<Object> remoteAddedValues,
			List<Object> remoteDeletedValues) throws FactoryException {
		final String attributeName = attribute.getName();

		final List<Object> leftValues = convertFeatureMapList(EFactory.eGetAsList(mapping.getLeftElement(),
				attributeName));
		final List<Object> rightValues = convertFeatureMapList(EFactory.eGetAsList(mapping.getRightElement(),
				attributeName));
		final List<Object> ancestorValues = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getOriginElement(), attributeName));

		// populates remotely added and locally deleted lists
		final List<Object> leftCopy = new ArrayList<Object>(leftValues);
		List<Object> ancestorCopy = new ArrayList<Object>(ancestorValues);
		for (Object right : rightValues) {
			Object leftMatched = null;
			final Iterator<Object> leftIterator = leftCopy.iterator();
			while (leftMatched == null && leftIterator.hasNext()) {
				final Object next = leftIterator.next();
				if (!areDistinctValues(right, next)) {
					leftMatched = next;
				}
			}

			Object ancestorMatched = null;
			final Iterator<Object> ancestorIterator = ancestorCopy.iterator();
			while (ancestorMatched == null && ancestorIterator.hasNext()) {
				final Object next = ancestorIterator.next();
				if (!areDistinctValues(right, next)) {
					ancestorMatched = next;
				}
			}

			if (leftMatched == null && ancestorMatched == null) {
				remoteAddedValues.add(right);
			} else if (leftMatched == null) {
				deletedValues.add(right);
			}
			if (leftMatched != null) {
				leftCopy.remove(leftMatched);
			}
			if (ancestorMatched != null) {
				ancestorCopy.remove(ancestorMatched);
			}
		}

		// populates remotely deleted and locally added lists
		final List<Object> rightCopy = new ArrayList<Object>(rightValues);
		ancestorCopy = new ArrayList<Object>(ancestorValues);
		for (Object left : leftValues) {
			Object rightMatched = null;
			final Iterator<Object> rightIterator = rightCopy.iterator();
			while (rightMatched == null && rightIterator.hasNext()) {
				final Object next = rightIterator.next();
				if (!areDistinctValues(left, next)) {
					rightMatched = next;
				}
			}

			Object ancestorMatched = null;
			final Iterator<Object> ancestorIterator = ancestorCopy.iterator();
			while (ancestorMatched == null && ancestorIterator.hasNext()) {
				final Object next = ancestorIterator.next();
				if (!areDistinctValues(left, next)) {
					ancestorMatched = next;
				}
			}

			if (rightMatched == null && ancestorMatched == null) {
				addedValues.add(left);
			} else if (rightMatched == null) {
				remoteDeletedValues.add(left);
			}
			if (rightMatched != null) {
				rightCopy.remove(rightMatched);
			}
			if (ancestorMatched != null) {
				ancestorCopy.remove(ancestorMatched);
			}
		}
	}

	/**
	 * This will create the {@link ConflictingDiffGroup} and its children for a conflictual AttributeChange.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param attribute
	 *            Attribute which has been changed to conflictual values.
	 * @param mapping
	 *            Contains informations about the left, right and origin element.
	 * @throws FactoryException
	 *             Thrown if we cannot create the {@link ConflictingDiffGroup}'s children.
	 */
	private void createConflictingAttributeChange(DiffGroup root, EAttribute attribute, Match3Elements mapping)
			throws FactoryException {
		// We'll use this diffGroup to make use of #createNonConflictingAttributeChange(DiffGroup, EAttribute,
		// EObject, EObject)
		final DiffGroup dummyGroup = DiffFactory.eINSTANCE.createDiffGroup();
		createNonConflictingAttributeChange(dummyGroup, attribute, mapping.getLeftElement(),
				mapping.getRightElement());

		if (dummyGroup.getSubDiffElements().size() > 0) {
			final ConflictingDiffElement conflictingDiff = DiffFactory.eINSTANCE
					.createConflictingDiffElement();
			conflictingDiff.setLeftParent(mapping.getLeftElement());
			conflictingDiff.setRightParent(mapping.getRightElement());
			conflictingDiff.setOriginElement(mapping.getOriginElement());
			for (final DiffElement subDiff : new ArrayList<DiffElement>(dummyGroup.getSubDiffElements())) {
				conflictingDiff.getSubDiffElements().add(subDiff);
			}
			root.getSubDiffElements().add(conflictingDiff);
		}
	}

	/**
	 * Creates and add the {@link DiffGroup} corresponding to an AttributeChange operation to the given
	 * {@link DiffGroup root}.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param attribute
	 *            Attribute which value has been changed.
	 * @param leftElement
	 *            Left element of the attribute change.
	 * @param rightElement
	 *            Right element of the attribute change.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the attribute's value for either one of the elements.
	 */
	private void createNonConflictingAttributeChange(DiffGroup root, EAttribute attribute,
			EObject leftElement, EObject rightElement) throws FactoryException {
		if (attribute.isMany()) {
			final List<Object> rightValues = convertFeatureMapList(EFactory.eGetAsList(rightElement,
					attribute.getName()));
			final List<Object> leftValues = convertFeatureMapList(EFactory.eGetAsList(leftElement,
					attribute.getName()));

			final List<Object> addedValues = computeAddedValues(leftValues, rightValues);
			final List<Object> deletedValues = computeDeletedValues(leftValues, rightValues);

			createLocalAttributeDiffs(root, attribute, leftElement, rightElement, addedValues, deletedValues);
		} else {
			final UpdateAttribute operation = DiffFactory.eINSTANCE.createUpdateAttribute();
			operation.setRightElement(rightElement);
			operation.setLeftElement(leftElement);
			operation.setAttribute(attribute);
			root.getSubDiffElements().add(operation);
		}
	}

	/**
	 * This will be called to check for changes on a given attribute's values. Note that we know that
	 * <code>attribute.isMany()</code> and <code>attribute.isOrdered()</code> always return <code>true</code>
	 * here.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param attribute
	 *            {@link EAttribute} to check for modifications.
	 * @param leftElement
	 *            Element corresponding to the final holder of the given attribute.
	 * @param rightElement
	 *            Element corresponding to the initial holder of the given attribute.
	 * @param addedValues
	 *            Contains the created differences for added attribute values.
	 * @param deletedValues
	 *            Contains the created differences for removed attribute values.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the attribute's values.
	 * @since 1.3
	 */
	protected void checkAttributeOrderChange(DiffGroup root, EAttribute attribute, EObject leftElement,
			EObject rightElement, List<AttributeChangeLeftTarget> addedValues,
			List<AttributeChangeRightTarget> deletedValues) throws FactoryException {
		final List<Object> rightValues = convertFeatureMapList(EFactory.eGetAsList(rightElement,
				attribute.getName()));
		final List<Object> leftValues = convertFeatureMapList(EFactory.eGetAsList(leftElement,
				attribute.getName()));
		final List<Integer> removedIndices = new ArrayList<Integer>(deletedValues.size());

		// Purge "left" list of all attribute values that have been added to it
		for (AttributeChangeLeftTarget added : addedValues) {
			leftValues.remove(added.getLeftTarget());
		}

		// Compute the list of indices that have been removed from the list
		for (AttributeChangeRightTarget removed : deletedValues) {
			final int removedIndex = rightValues.indexOf(removed.getRightTarget());
			removedIndices.add(Integer.valueOf(removedIndex));
		}

		// Iterate over the list to detect values that actually changed order.
		int expectedIndex = 0;
		for (int i = 0; i < leftValues.size(); i++) {
			for (Integer removedIndex : new ArrayList<Integer>(removedIndices)) {
				if (i == removedIndex.intValue()) {
					expectedIndex++;
					removedIndices.remove(removedIndex);
				}
			}

			if (areDistinctValues(leftValues.get(i), rightValues.get(expectedIndex++))) {
				final AttributeOrderChange attributeChange = DiffFactory.eINSTANCE
						.createAttributeOrderChange();
				attributeChange.setAttribute(attribute);
				attributeChange.setLeftElement(leftElement);
				attributeChange.setRightElement(rightElement);

				root.getSubDiffElements().add(attributeChange);
				break;
			}
		}
	}

	/**
	 * This will be called to check for changes on a given attribute's values. Note that we know that
	 * <code>attribute.isMany()</code> and <code>attribute.isOrdered()</code> always return <code>true</code>
	 * here.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param attribute
	 *            {@link EAttribute} to check for modifications.
	 * @param leftElement
	 *            Element corresponding to the final holder of the given attribute.
	 * @param rightElement
	 *            Element corresponding to the initial holder of the given attribute.
	 * @param remoteDeletedValues
	 *            Contains the created differences for remotely removed attribute values.
	 * @param remoteAddedValues
	 *            Contains the created differences for remotely added attribute values.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the attribute's values.
	 * @since 1.3
	 */
	protected void checkAttributeRemoteOrderChange(DiffGroup root, EAttribute attribute, EObject leftElement,
			EObject rightElement, List<AttributeChangeLeftTarget> remoteDeletedValues,
			List<AttributeChangeRightTarget> remoteAddedValues) throws FactoryException {
		final List<Object> rightValues = convertFeatureMapList(EFactory.eGetAsList(rightElement,
				attribute.getName()));
		final List<Object> leftValues = convertFeatureMapList(EFactory.eGetAsList(leftElement,
				attribute.getName()));
		final List<Integer> removedIndices = new ArrayList<Integer>(remoteAddedValues.size());

		// Purge "left" list of all attribute values that have been added to it
		for (AttributeChangeLeftTarget remoteDeleted : remoteDeletedValues) {
			leftValues.remove(remoteDeleted.getLeftTarget());
		}

		// Compute the list of indices that have been removed from the list
		for (AttributeChangeRightTarget remoteAdded : remoteAddedValues) {
			final int removedIndex = rightValues.indexOf(remoteAdded.getRightTarget());
			removedIndices.add(Integer.valueOf(removedIndex));
		}

		// Iterate over the list to detect values that actually changed order.
		int expectedIndex = 0;
		for (int i = 0; i < leftValues.size(); i++) {
			for (Integer removedIndex : new ArrayList<Integer>(removedIndices)) {
				if (i == removedIndex.intValue()) {
					expectedIndex++;
					removedIndices.remove(removedIndex);
				}
			}

			if (areDistinctValues(leftValues.get(i), rightValues.get(expectedIndex))) {
				final AttributeOrderChange attributeChange = DiffFactory.eINSTANCE
						.createAttributeOrderChange();
				attributeChange.setAttribute(attribute);
				attributeChange.setLeftElement(leftElement);
				attributeChange.setRightElement(rightElement);
				attributeChange.setRemote(true);

				root.getSubDiffElements().add(attributeChange);
				break;
			}
		}
	}

	/**
	 * This will create and populate a {@link List} with all the values from the <code>leftValues</code> list
	 * that are not present the the <code>rightValues</code> list.
	 * 
	 * @param leftValues
	 *            List of the left element attribute values.
	 * @param rightValues
	 *            List of the right element attribute values.
	 * @return The list of all values that are contained by <code>leftValues</code> but not by
	 *         <code>rightValues</code>.
	 */
	private List<Object> computeAddedValues(List<Object> leftValues, List<Object> rightValues) {
		final List<Object> result = new ArrayList<Object>(leftValues);
		final List<Object> rightCopy = new ArrayList<Object>(rightValues);

		for (Object left : leftValues) {
			Object matched = null;
			final Iterator<Object> rightIterator = rightCopy.iterator();
			while (matched == null && rightIterator.hasNext()) {
				final Object next = rightIterator.next();
				if (!areDistinctValues(left, next)) {
					matched = next;
				}
			}

			if (matched != null) {
				rightCopy.remove(matched);
				result.remove(left);
			}
		}

		return result;
	}

	/**
	 * This will create and populate a {@link List} with all the values from the <code>rightValues</code> list
	 * that are not present the the <code>leftValues</code> list.
	 * 
	 * @param leftValues
	 *            List of the left element attribute values.
	 * @param rightValues
	 *            List of the right element attribute values.
	 * @return The list of all values that are contained by <code>rightValues</code> but not by
	 *         <code>leftValues</code>.
	 */
	private List<Object> computeDeletedValues(List<Object> leftValues, List<Object> rightValues) {
		final List<Object> result = new ArrayList<Object>(rightValues);
		final List<Object> leftCopy = new ArrayList<Object>(leftValues);

		for (Object right : rightValues) {
			Object matched = null;
			final Iterator<Object> leftIterator = leftCopy.iterator();
			while (matched == null && leftIterator.hasNext()) {
				final Object next = leftIterator.next();
				if (!areDistinctValues(right, next)) {
					matched = next;
				}
			}

			if (matched != null) {
				leftCopy.remove(matched);
				result.remove(right);
			}
		}

		return result;
	}

	/**
	 * This will create the needed remote attribute change {@link DiffElement} under the given
	 * {@link DiffGroup root}.<br/>
	 * An attribute is &quot;remotely changed&quot; if it has been added, updated or deleted in the right
	 * (latest from head) version but it has kept its former value in the left (working copy) version.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param attribute
	 *            Target {@link EAttribute} of the update.
	 * @param mapping
	 *            Contains the three (ancestor, left, right) elements' mapping.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch <code>attribute</code>'s left and right values.
	 */
	private void createRemoteAttributeChange(DiffGroup root, EAttribute attribute, Match3Elements mapping)
			throws FactoryException {
		if (attribute.isMany()) {
			final List<Object> remoteDeletedValues = new ArrayList<Object>();
			final List<Object> remoteAddedValues = new ArrayList<Object>();

			// We know that there is no "local" diff, thus addedValues and deletedValues are not used.
			populateThreeWayAttributeChanges(mapping, attribute, new ArrayList<Object>(),
					new ArrayList<Object>(), remoteAddedValues, remoteDeletedValues);
			createRemoteAttributeDiffs(root, attribute, mapping.getLeftElement(), mapping.getRightElement(),
					remoteDeletedValues, remoteAddedValues);
		} else {
			final UpdateAttribute operation = DiffFactory.eINSTANCE.createUpdateAttribute();
			operation.setRemote(true);
			operation.setRightElement(mapping.getRightElement());
			operation.setLeftElement(mapping.getLeftElement());
			operation.setAttribute(attribute);
			root.getSubDiffElements().add(operation);
		}
	}

	/**
	 * This can be used to check that the given list contains the given value. This will use the checks
	 * described in {@link #areDistinctValues(Object, Object)}.
	 * 
	 * @param values
	 *            The list we need to check for a value equivalent to <code>value</code>.
	 * @param value
	 *            The value we need to know if it's contained by <code>values</code>.
	 * @return <code>true</code> if {@link #areDistinctValues(Object, Object)} returned true for one of the
	 *         objects contained by <code>values</code> when compared with <code>value</code>.
	 * @deprecated no longer in use.
	 */
	@Deprecated
	protected final boolean attributeListContains(List<Object> values, Object value) {
		for (Object aValue : values) {
			if (!areDistinctValues(aValue, value)) {
				return true;
			}
		}
		return false;
	}
}
