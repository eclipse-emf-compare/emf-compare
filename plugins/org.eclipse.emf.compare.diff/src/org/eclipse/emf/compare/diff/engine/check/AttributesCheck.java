/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
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
import org.eclipse.emf.ecore.EEnumLiteral;
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
		final boolean distinct;
		if (left instanceof EEnumLiteral && right instanceof EEnumLiteral) {
			final StringBuilder value1 = new StringBuilder();
			value1.append(((EEnumLiteral)left).getLiteral()).append(((EEnumLiteral)left).getValue());
			final StringBuilder value2 = new StringBuilder();
			value2.append(((EEnumLiteral)right).getLiteral()).append(((EEnumLiteral)right).getValue());
			distinct = !value1.toString().equals(value2.toString());
		} else if (left instanceof EObject && right instanceof EObject) {
			// [248442] This will handle FeatureMapEntries detection
			distinct = left != getMatchedEObject((EObject)right);
		} else {
			distinct = left != null && !left.equals(right) || left == null && left != right;
		}
		return distinct;
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
	 */
	protected final boolean attributeListContains(List<Object> values, Object value) {
		for (Object aValue : values) {
			if (!areDistinctValues(aValue, value)) {
				return true;
			}
		}
		return false;
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
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(mapping
					.getRightElement(), attributeName));

			if (leftValue.size() != rightValue.size()) {
				distinct = true;
			} else {
				for (Object left : leftValue) {
					distinct = !attributeListContains(rightValue, left);
					if (distinct) {
						break;
					}
				}
				for (Object right : rightValue) {
					distinct = !attributeListContains(leftValue, right);
					if (distinct) {
						break;
					}
				}
			}
		} else {
			final Object leftValue = EFactory.eGet(mapping.getLeftElement(), attributeName);
			final Object rightValue = EFactory.eGet(mapping.getRightElement(), attributeName);

			distinct = areDistinctValues(leftValue, rightValue);
		}

		if (distinct) {
			createNonConflictingAttributeChange(root, attribute, mapping.getLeftElement(), mapping
					.getRightElement());
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
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(mapping
					.getRightElement(), attributeName));
			final List<Object> ancestorValue = convertFeatureMapList(EFactory.eGetAsList(mapping
					.getOriginElement(), attributeName));

			for (Object right : rightValue) {
				rightDistinctFromOrigin = !attributeListContains(ancestorValue, right);
				if (rightDistinctFromOrigin) {
					break;
				}
			}
			for (Object right : rightValue) {
				rightDistinctFromLeft = !attributeListContains(leftValue, right);
				if (rightDistinctFromLeft) {
					break;
				}
			}
			for (Object left : leftValue) {
				leftDistinctFromOrigin = !attributeListContains(ancestorValue, left);
				if (leftDistinctFromOrigin) {
					break;
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
			createNonConflictingAttributeChange(root, attribute, mapping.getLeftElement(), mapping
					.getRightElement());
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
			final List<Object> leftValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getLeftElement(), attribute.getName()));
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(mapping
					.getRightElement(), attribute.getName()));
			final List<Object> ancestorValue = convertFeatureMapList(EFactory.eGetAsList(mapping
					.getOriginElement(), attribute.getName()));

			for (final Object aValue : leftValue) {
				if (!attributeListContains(rightValue, aValue)) {
					final AttributeChangeLeftTarget operation = DiffFactory.eINSTANCE
							.createAttributeChangeLeftTarget();
					if (ancestorValue.contains(aValue)) {
						operation.setRemote(true);
					}
					operation.setAttribute(attribute);
					operation.setRightElement(mapping.getRightElement());
					operation.setLeftElement(mapping.getLeftElement());
					operation.setLeftTarget(aValue);
					root.getSubDiffElements().add(operation);
				}
			}
			for (final Object aValue : rightValue) {
				if (!attributeListContains(leftValue, aValue)) {
					final AttributeChangeRightTarget operation = DiffFactory.eINSTANCE
							.createAttributeChangeRightTarget();
					if (ancestorValue.contains(aValue)) {
						operation.setRemote(true);
					}
					operation.setAttribute(attribute);
					operation.setRightElement(mapping.getRightElement());
					operation.setLeftElement(mapping.getLeftElement());
					operation.setRightTarget(aValue);
					root.getSubDiffElements().add(operation);
				}
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
		createNonConflictingAttributeChange(dummyGroup, attribute, mapping.getLeftElement(), mapping
				.getRightElement());

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
			final List<Object> leftValue = convertFeatureMapList(EFactory.eGetAsList(leftElement, attribute
					.getName()));
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(rightElement, attribute
					.getName()));
			for (final Object aValue : leftValue) {
				if (!attributeListContains(rightValue, aValue)) {
					final AttributeChangeLeftTarget operation = DiffFactory.eINSTANCE
							.createAttributeChangeLeftTarget();
					operation.setAttribute(attribute);
					operation.setRightElement(rightElement);
					operation.setLeftElement(leftElement);
					operation.setLeftTarget(aValue);
					root.getSubDiffElements().add(operation);
				}
			}
			for (final Object aValue : rightValue) {
				if (!attributeListContains(leftValue, aValue)) {
					final AttributeChangeRightTarget operation = DiffFactory.eINSTANCE
							.createAttributeChangeRightTarget();
					operation.setAttribute(attribute);
					operation.setRightElement(rightElement);
					operation.setLeftElement(leftElement);
					operation.setRightTarget(aValue);
					root.getSubDiffElements().add(operation);
				}
			}
		} else {
			final UpdateAttribute operation = DiffFactory.eINSTANCE.createUpdateAttribute();
			operation.setRightElement(rightElement);
			operation.setLeftElement(leftElement);
			operation.setAttribute(attribute);
			root.getSubDiffElements().add(operation);
		}
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
			final List<Object> leftValue = convertFeatureMapList(EFactory.eGetAsList(
					mapping.getLeftElement(), attribute.getName()));
			final List<Object> rightValue = convertFeatureMapList(EFactory.eGetAsList(mapping
					.getRightElement(), attribute.getName()));
			for (final Object aValue : leftValue) {
				// if the value is present in the right (latest) but not in the
				// left (working copy), it's been removed remotely
				if (!attributeListContains(rightValue, aValue)) {
					final AttributeChangeLeftTarget operation = DiffFactory.eINSTANCE
							.createAttributeChangeLeftTarget();
					operation.setRemote(true);
					operation.setAttribute(attribute);
					operation.setRightElement(mapping.getRightElement());
					operation.setLeftElement(mapping.getLeftElement());
					operation.setLeftTarget(aValue);
					root.getSubDiffElements().add(operation);
				}
			}
			for (final Object aValue : rightValue) {
				// if the value is present in the left (working copy) but not
				// in the right (latest), it's been added remotely
				if (!attributeListContains(leftValue, aValue)) {
					final AttributeChangeRightTarget operation = DiffFactory.eINSTANCE
							.createAttributeChangeRightTarget();
					operation.setRemote(true);
					operation.setAttribute(attribute);
					operation.setRightElement(mapping.getRightElement());
					operation.setLeftElement(mapping.getLeftElement());
					operation.setRightTarget(aValue);
					root.getSubDiffElements().add(operation);
				}
			}
		} else {
			final UpdateAttribute operation = DiffFactory.eINSTANCE.createUpdateAttribute();
			operation.setRemote(true);
			operation.setRightElement(mapping.getRightElement());
			operation.setLeftElement(mapping.getLeftElement());
			operation.setAttribute(attribute);
			root.getSubDiffElements().add(operation);
		}
	}
}
