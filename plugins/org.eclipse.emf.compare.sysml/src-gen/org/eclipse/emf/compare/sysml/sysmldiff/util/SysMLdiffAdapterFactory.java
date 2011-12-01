/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage
 * @generated
 */
public class SysMLdiffAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static SysMLdiffPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLdiffAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = SysMLdiffPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This
	 * implementation returns <code>true</code> if the object is either the model's package or is an instance
	 * object of the model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected SysMLdiffSwitch<Adapter> modelSwitch = new SysMLdiffSwitch<Adapter>() {
		@Override
		public Adapter caseSysMLDiffExtension(SysMLDiffExtension object) {
			return createSysMLDiffExtensionAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypeAttributeChange(SysMLStereotypeAttributeChange object) {
			return createSysMLStereotypeAttributeChangeAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypePropertyChangeLeftTarget(
				SysMLStereotypePropertyChangeLeftTarget object) {
			return createSysMLStereotypePropertyChangeLeftTargetAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypePropertyChangeRightTarget(
				SysMLStereotypePropertyChangeRightTarget object) {
			return createSysMLStereotypePropertyChangeRightTargetAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypeReferenceChangeLeftTarget(
				SysMLStereotypeReferenceChangeLeftTarget object) {
			return createSysMLStereotypeReferenceChangeLeftTargetAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypeReferenceChangeRightTarget(
				SysMLStereotypeReferenceChangeRightTarget object) {
			return createSysMLStereotypeReferenceChangeRightTargetAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypeReferenceOrderChange(SysMLStereotypeReferenceOrderChange object) {
			return createSysMLStereotypeReferenceOrderChangeAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypeUpdateAttribute(SysMLStereotypeUpdateAttribute object) {
			return createSysMLStereotypeUpdateAttributeAdapter();
		}

		@Override
		public Adapter caseSysMLStereotypeUpdateReference(SysMLStereotypeUpdateReference object) {
			return createSysMLStereotypeUpdateReferenceAdapter();
		}

		@Override
		public Adapter caseDiffElement(DiffElement object) {
			return createDiffElementAdapter();
		}

		@Override
		public Adapter caseAbstractDiffExtension(AbstractDiffExtension object) {
			return createAbstractDiffExtensionAdapter();
		}

		@Override
		public Adapter caseUMLDiffExtension(UMLDiffExtension object) {
			return createUMLDiffExtensionAdapter();
		}

		@Override
		public Adapter caseUMLStereotypePropertyChange(UMLStereotypePropertyChange object) {
			return createUMLStereotypePropertyChangeAdapter();
		}

		@Override
		public Adapter caseAttributeChange(AttributeChange object) {
			return createAttributeChangeAdapter();
		}

		@Override
		public Adapter caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
			return createAttributeChangeLeftTargetAdapter();
		}

		@Override
		public Adapter caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
			return createAttributeChangeRightTargetAdapter();
		}

		@Override
		public Adapter caseReferenceChange(ReferenceChange object) {
			return createReferenceChangeAdapter();
		}

		@Override
		public Adapter caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
			return createReferenceChangeLeftTargetAdapter();
		}

		@Override
		public Adapter caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
			return createReferenceChangeRightTargetAdapter();
		}

		@Override
		public Adapter caseReferenceOrderChange(ReferenceOrderChange object) {
			return createReferenceOrderChangeAdapter();
		}

		@Override
		public Adapter caseUpdateAttribute(UpdateAttribute object) {
			return createUpdateAttributeAdapter();
		}

		@Override
		public Adapter caseUpdateReference(UpdateReference object) {
			return createUpdateReferenceAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension <em>Sys ML Diff Extension</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
	 * @generated
	 */
	public Adapter createSysMLDiffExtensionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
	 * <em>Sys ML Stereotype Attribute Change</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
	 * @generated
	 */
	public Adapter createSysMLStereotypeAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget
	 * <em>Sys ML Stereotype Property Change Left Target</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget
	 * @generated
	 */
	public Adapter createSysMLStereotypePropertyChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget
	 * <em>Sys ML Stereotype Property Change Right Target</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget
	 * @generated
	 */
	public Adapter createSysMLStereotypePropertyChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget
	 * <em>Sys ML Stereotype Reference Change Left Target</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget
	 * @generated
	 */
	public Adapter createSysMLStereotypeReferenceChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget
	 * <em>Sys ML Stereotype Reference Change Right Target</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget
	 * @generated
	 */
	public Adapter createSysMLStereotypeReferenceChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange
	 * <em>Sys ML Stereotype Reference Order Change</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange
	 * @generated
	 */
	public Adapter createSysMLStereotypeReferenceOrderChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute
	 * <em>Sys ML Stereotype Update Attribute</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute
	 * @generated
	 */
	public Adapter createSysMLStereotypeUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference
	 * <em>Sys ML Stereotype Update Reference</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference
	 * @generated
	 */
	public Adapter createSysMLStereotypeUpdateReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'. <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases; it's useful to ignore a
	 * case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	public Adapter createDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension <em>Abstract Diff Extension</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension
	 * @generated
	 */
	public Adapter createAbstractDiffExtensionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDiffExtension
	 * <em>UML Diff Extension</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
	 * anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDiffExtension
	 * @generated
	 */
	public Adapter createUMLDiffExtensionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * <em>UML Stereotype Property Change</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * @generated
	 */
	public Adapter createUMLStereotypePropertyChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	public Adapter createAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * <em>Attribute Change Left Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * @generated
	 */
	public Adapter createAttributeChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * <em>Attribute Change Right Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * @generated
	 */
	public Adapter createAttributeChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	public Adapter createReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * <em>Reference Change Left Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * @generated
	 */
	public Adapter createReferenceChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * <em>Reference Change Right Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * @generated
	 */
	public Adapter createReferenceChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange <em>Reference Order Change</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange
	 * @generated
	 */
	public Adapter createReferenceOrderChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	public Adapter createUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	public Adapter createUpdateReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns
	 * null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // SysMLdiffAdapterFactory
