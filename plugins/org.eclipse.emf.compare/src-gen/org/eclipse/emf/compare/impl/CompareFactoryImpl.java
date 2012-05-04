/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.impl;

import java.lang.Iterable;
import org.eclipse.emf.compare.*;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.spec.ComparisonSpec;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
@SuppressWarnings("unused")
// generated code, removing warnings
public class CompareFactoryImpl extends EFactoryImpl implements CompareFactory {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static CompareFactory init() {
		try {
			CompareFactory theCompareFactory = (CompareFactory)EPackage.Registry.INSTANCE
					.getEFactory("http://www.eclipse.org/emf/compare"); //$NON-NLS-1$ 
			if (theCompareFactory != null) {
				return theCompareFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CompareFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public CompareFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ComparePackage.COMPARISON:
				return createComparison();
			case ComparePackage.MATCH_RESOURCE:
				return createMatchResource();
			case ComparePackage.MATCH:
				return createMatch();
			case ComparePackage.DIFF:
				return createDiff();
			case ComparePackage.RESOURCE_ATTACHMENT_CHANGE:
				return createResourceAttachmentChange();
			case ComparePackage.REFERENCE_CHANGE:
				return createReferenceChange();
			case ComparePackage.ATTRIBUTE_CHANGE:
				return createAttributeChange();
			case ComparePackage.CONFLICT:
				return createConflict();
			case ComparePackage.EQUIVALENCE:
				return createEquivalence();
			default:
				throw new IllegalArgumentException(
						"The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ComparePackage.DIFFERENCE_KIND:
				return createDifferenceKindFromString(eDataType, initialValue);
			case ComparePackage.DIFFERENCE_SOURCE:
				return createDifferenceSourceFromString(eDataType, initialValue);
			case ComparePackage.EITERABLE:
				return createEIterableFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(
						"The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ComparePackage.DIFFERENCE_KIND:
				return convertDifferenceKindToString(eDataType, instanceValue);
			case ComparePackage.DIFFERENCE_SOURCE:
				return convertDifferenceSourceToString(eDataType, instanceValue);
			case ComparePackage.EITERABLE:
				return convertEIterableToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(
						"The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Comparison createComparison() {
		ComparisonImpl comparison = new ComparisonImpl();
		return comparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MatchResource createMatchResource() {
		MatchResourceImpl matchResource = new MatchResourceImpl();
		return matchResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Match createMatch() {
		MatchImpl match = new MatchImpl();
		return match;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Diff createDiff() {
		DiffImpl diff = new DiffImpl();
		return diff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceAttachmentChange createResourceAttachmentChange() {
		ResourceAttachmentChangeImpl resourceAttachmentChange = new ResourceAttachmentChangeImpl();
		return resourceAttachmentChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceChange createReferenceChange() {
		ReferenceChangeImpl referenceChange = new ReferenceChangeImpl();
		return referenceChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeChange createAttributeChange() {
		AttributeChangeImpl attributeChange = new AttributeChangeImpl();
		return attributeChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Conflict createConflict() {
		ConflictImpl conflict = new ConflictImpl();
		return conflict;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Equivalence createEquivalence() {
		EquivalenceImpl equivalence = new EquivalenceImpl();
		return equivalence;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DifferenceKind createDifferenceKindFromString(EDataType eDataType, String initialValue) {
		DifferenceKind result = DifferenceKind.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDifferenceKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DifferenceSource createDifferenceSourceFromString(EDataType eDataType, String initialValue) {
		DifferenceSource result = DifferenceSource.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDifferenceSourceToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Iterable<?> createEIterableFromString(EDataType eDataType, String initialValue) {
		return (Iterable<?>)super.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEIterableToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ComparePackage getComparePackage() {
		return (ComparePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ComparePackage getPackage() {
		return ComparePackage.eINSTANCE;
	}

} // CompareFactoryImpl
