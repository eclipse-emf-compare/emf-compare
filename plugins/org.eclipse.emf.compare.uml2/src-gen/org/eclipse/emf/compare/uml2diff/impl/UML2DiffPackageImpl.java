/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.impl;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChange;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChange;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLElementChange;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChange;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLMessageChange;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class UML2DiffPackageImpl extends EPackageImpl implements UML2DiffPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDiffExtensionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationBranchChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationBranchChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationBranchChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyBranchChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyBranchChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyBranchChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlGeneralizationSetChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlGeneralizationSetChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlGeneralizationSetChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExtendChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExtendChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExtendChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExecutionSpecificationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExecutionSpecificationChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExecutionSpecificationChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDestructionEventChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDestructionEventChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDestructionEventChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlIntervalConstraintChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlIntervalConstraintChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlIntervalConstraintChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlMessageChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlMessageChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlMessageChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypePropertyChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeAttributeChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeAttributeChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeUpdateAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeApplicationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeApplicationAdditionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeApplicationRemovalEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeReferenceChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeReferenceChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeUpdateReferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeReferenceOrderChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlProfileApplicationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlProfileApplicationAdditionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlProfileApplicationRemovalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlElementChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlElementChangeRightTargetEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UML2DiffPackageImpl() {
		super(eNS_URI, UML2DiffFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link UML2DiffPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UML2DiffPackage init() {
		if (isInited) return (UML2DiffPackage)EPackage.Registry.INSTANCE.getEPackage(UML2DiffPackage.eNS_URI);

		// Obtain or create and register package
		UML2DiffPackageImpl theUML2DiffPackage = (UML2DiffPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof UML2DiffPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new UML2DiffPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DiffPackage.eINSTANCE.eClass();
		UMLPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theUML2DiffPackage.createPackageContents();

		// Initialize created meta-data
		theUML2DiffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUML2DiffPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(UML2DiffPackage.eNS_URI, theUML2DiffPackage);
		return theUML2DiffPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDiffExtension() {
		return umlDiffExtensionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationChange() {
		return umlAssociationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationChangeLeftTarget() {
		return umlAssociationChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationChangeRightTarget() {
		return umlAssociationChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationBranchChange() {
		return umlAssociationBranchChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationBranchChangeLeftTarget() {
		return umlAssociationBranchChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationBranchChangeRightTarget() {
		return umlAssociationBranchChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyBranchChange() {
		return umlDependencyBranchChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyBranchChangeLeftTarget() {
		return umlDependencyBranchChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyBranchChangeRightTarget() {
		return umlDependencyBranchChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLGeneralizationSetChange() {
		return umlGeneralizationSetChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLGeneralizationSetChangeLeftTarget() {
		return umlGeneralizationSetChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLGeneralizationSetChangeRightTarget() {
		return umlGeneralizationSetChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyChange() {
		return umlDependencyChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyChangeLeftTarget() {
		return umlDependencyChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyChangeRightTarget() {
		return umlDependencyChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExtendChange() {
		return umlExtendChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExtendChangeLeftTarget() {
		return umlExtendChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExtendChangeRightTarget() {
		return umlExtendChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExecutionSpecificationChange() {
		return umlExecutionSpecificationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExecutionSpecificationChangeLeftTarget() {
		return umlExecutionSpecificationChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExecutionSpecificationChangeRightTarget() {
		return umlExecutionSpecificationChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDestructionEventChange() {
		return umlDestructionEventChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDestructionEventChangeLeftTarget() {
		return umlDestructionEventChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDestructionEventChangeRightTarget() {
		return umlDestructionEventChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLIntervalConstraintChange() {
		return umlIntervalConstraintChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLIntervalConstraintChangeLeftTarget() {
		return umlIntervalConstraintChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLIntervalConstraintChangeRightTarget() {
		return umlIntervalConstraintChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLMessageChange() {
		return umlMessageChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLMessageChangeLeftTarget() {
		return umlMessageChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLMessageChangeRightTarget() {
		return umlMessageChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypePropertyChange() {
		return umlStereotypePropertyChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLStereotypePropertyChange_Stereotype() {
		return (EReference)umlStereotypePropertyChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeAttributeChangeLeftTarget() {
		return umlStereotypeAttributeChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeAttributeChangeRightTarget() {
		return umlStereotypeAttributeChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeUpdateAttribute() {
		return umlStereotypeUpdateAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeApplicationChange() {
		return umlStereotypeApplicationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLStereotypeApplicationChange_Stereotype() {
		return (EReference)umlStereotypeApplicationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeApplicationAddition() {
		return umlStereotypeApplicationAdditionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeApplicationRemoval() {
		return umlStereotypeApplicationRemovalEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeReferenceChangeLeftTarget() {
		return umlStereotypeReferenceChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeReferenceChangeRightTarget() {
		return umlStereotypeReferenceChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeUpdateReference() {
		return umlStereotypeUpdateReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeReferenceOrderChange() {
		return umlStereotypeReferenceOrderChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLProfileApplicationChange() {
		return umlProfileApplicationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLProfileApplicationChange_Profile() {
		return (EReference)umlProfileApplicationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLProfileApplicationAddition() {
		return umlProfileApplicationAdditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLProfileApplicationRemoval() {
		return umlProfileApplicationRemovalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLElementChange() {
		return umlElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLElementChangeLeftTarget() {
		return umlElementChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLElementChangeRightTarget() {
		return umlElementChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffFactory getUML2DiffFactory() {
		return (UML2DiffFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		umlDiffExtensionEClass = createEClass(UML_DIFF_EXTENSION);

		umlAssociationChangeEClass = createEClass(UML_ASSOCIATION_CHANGE);

		umlAssociationChangeLeftTargetEClass = createEClass(UML_ASSOCIATION_CHANGE_LEFT_TARGET);

		umlAssociationChangeRightTargetEClass = createEClass(UML_ASSOCIATION_CHANGE_RIGHT_TARGET);

		umlAssociationBranchChangeEClass = createEClass(UML_ASSOCIATION_BRANCH_CHANGE);

		umlAssociationBranchChangeLeftTargetEClass = createEClass(UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET);

		umlAssociationBranchChangeRightTargetEClass = createEClass(UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET);

		umlDependencyBranchChangeEClass = createEClass(UML_DEPENDENCY_BRANCH_CHANGE);

		umlDependencyBranchChangeLeftTargetEClass = createEClass(UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET);

		umlDependencyBranchChangeRightTargetEClass = createEClass(UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET);

		umlGeneralizationSetChangeEClass = createEClass(UML_GENERALIZATION_SET_CHANGE);

		umlGeneralizationSetChangeLeftTargetEClass = createEClass(UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET);

		umlGeneralizationSetChangeRightTargetEClass = createEClass(UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET);

		umlDependencyChangeEClass = createEClass(UML_DEPENDENCY_CHANGE);

		umlDependencyChangeLeftTargetEClass = createEClass(UML_DEPENDENCY_CHANGE_LEFT_TARGET);

		umlDependencyChangeRightTargetEClass = createEClass(UML_DEPENDENCY_CHANGE_RIGHT_TARGET);

		umlExtendChangeEClass = createEClass(UML_EXTEND_CHANGE);

		umlExtendChangeLeftTargetEClass = createEClass(UML_EXTEND_CHANGE_LEFT_TARGET);

		umlExtendChangeRightTargetEClass = createEClass(UML_EXTEND_CHANGE_RIGHT_TARGET);

		umlExecutionSpecificationChangeEClass = createEClass(UML_EXECUTION_SPECIFICATION_CHANGE);

		umlExecutionSpecificationChangeLeftTargetEClass = createEClass(UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET);

		umlExecutionSpecificationChangeRightTargetEClass = createEClass(UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET);

		umlDestructionEventChangeEClass = createEClass(UML_DESTRUCTION_EVENT_CHANGE);

		umlDestructionEventChangeLeftTargetEClass = createEClass(UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET);

		umlDestructionEventChangeRightTargetEClass = createEClass(UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET);

		umlIntervalConstraintChangeEClass = createEClass(UML_INTERVAL_CONSTRAINT_CHANGE);

		umlIntervalConstraintChangeLeftTargetEClass = createEClass(UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET);

		umlIntervalConstraintChangeRightTargetEClass = createEClass(UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET);

		umlMessageChangeEClass = createEClass(UML_MESSAGE_CHANGE);

		umlMessageChangeLeftTargetEClass = createEClass(UML_MESSAGE_CHANGE_LEFT_TARGET);

		umlMessageChangeRightTargetEClass = createEClass(UML_MESSAGE_CHANGE_RIGHT_TARGET);

		umlStereotypePropertyChangeEClass = createEClass(UML_STEREOTYPE_PROPERTY_CHANGE);
		createEReference(umlStereotypePropertyChangeEClass, UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE);

		umlStereotypeAttributeChangeLeftTargetEClass = createEClass(UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET);

		umlStereotypeAttributeChangeRightTargetEClass = createEClass(UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET);

		umlStereotypeUpdateAttributeEClass = createEClass(UML_STEREOTYPE_UPDATE_ATTRIBUTE);

		umlStereotypeApplicationChangeEClass = createEClass(UML_STEREOTYPE_APPLICATION_CHANGE);
		createEReference(umlStereotypeApplicationChangeEClass, UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE);

		umlStereotypeApplicationAdditionEClass = createEClass(UML_STEREOTYPE_APPLICATION_ADDITION);

		umlStereotypeApplicationRemovalEClass = createEClass(UML_STEREOTYPE_APPLICATION_REMOVAL);

		umlStereotypeReferenceChangeLeftTargetEClass = createEClass(UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET);

		umlStereotypeReferenceChangeRightTargetEClass = createEClass(UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET);

		umlStereotypeUpdateReferenceEClass = createEClass(UML_STEREOTYPE_UPDATE_REFERENCE);

		umlStereotypeReferenceOrderChangeEClass = createEClass(UML_STEREOTYPE_REFERENCE_ORDER_CHANGE);

		umlProfileApplicationChangeEClass = createEClass(UML_PROFILE_APPLICATION_CHANGE);
		createEReference(umlProfileApplicationChangeEClass, UML_PROFILE_APPLICATION_CHANGE__PROFILE);

		umlProfileApplicationAdditionEClass = createEClass(UML_PROFILE_APPLICATION_ADDITION);

		umlProfileApplicationRemovalEClass = createEClass(UML_PROFILE_APPLICATION_REMOVAL);

		umlElementChangeEClass = createEClass(UML_ELEMENT_CHANGE);

		umlElementChangeLeftTargetEClass = createEClass(UML_ELEMENT_CHANGE_LEFT_TARGET);

		umlElementChangeRightTargetEClass = createEClass(UML_ELEMENT_CHANGE_RIGHT_TARGET);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		DiffPackage theDiffPackage = (DiffPackage)EPackage.Registry.INSTANCE.getEPackage(DiffPackage.eNS_URI);
		UMLPackage theUMLPackage = (UMLPackage)EPackage.Registry.INSTANCE.getEPackage(UMLPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		umlDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getDiffElement());
		umlDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getAbstractDiffExtension());
		umlAssociationChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlAssociationChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlAssociationChangeLeftTargetEClass.getESuperTypes().add(this.getUMLAssociationChange());
		umlAssociationChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlAssociationChangeRightTargetEClass.getESuperTypes().add(this.getUMLAssociationChange());
		umlAssociationBranchChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlAssociationBranchChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlAssociationBranchChangeLeftTargetEClass.getESuperTypes().add(this.getUMLAssociationBranchChange());
		umlAssociationBranchChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlAssociationBranchChangeRightTargetEClass.getESuperTypes().add(this.getUMLAssociationBranchChange());
		umlDependencyBranchChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlDependencyBranchChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getReferenceChangeLeftTarget());
		umlDependencyBranchChangeLeftTargetEClass.getESuperTypes().add(this.getUMLDependencyBranchChange());
		umlDependencyBranchChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getReferenceChangeRightTarget());
		umlDependencyBranchChangeRightTargetEClass.getESuperTypes().add(this.getUMLDependencyBranchChange());
		umlGeneralizationSetChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlGeneralizationSetChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlGeneralizationSetChangeLeftTargetEClass.getESuperTypes().add(this.getUMLGeneralizationSetChange());
		umlGeneralizationSetChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlGeneralizationSetChangeRightTargetEClass.getESuperTypes().add(this.getUMLGeneralizationSetChange());
		umlDependencyChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlDependencyChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlDependencyChangeLeftTargetEClass.getESuperTypes().add(this.getUMLDependencyChange());
		umlDependencyChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlDependencyChangeRightTargetEClass.getESuperTypes().add(this.getUMLDependencyChange());
		umlExtendChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlExtendChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlExtendChangeLeftTargetEClass.getESuperTypes().add(this.getUMLExtendChange());
		umlExtendChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlExtendChangeRightTargetEClass.getESuperTypes().add(this.getUMLExtendChange());
		umlExecutionSpecificationChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlExecutionSpecificationChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlExecutionSpecificationChangeLeftTargetEClass.getESuperTypes().add(this.getUMLExecutionSpecificationChange());
		umlExecutionSpecificationChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlExecutionSpecificationChangeRightTargetEClass.getESuperTypes().add(this.getUMLExecutionSpecificationChange());
		umlDestructionEventChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlDestructionEventChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlDestructionEventChangeLeftTargetEClass.getESuperTypes().add(this.getUMLDestructionEventChange());
		umlDestructionEventChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlDestructionEventChangeRightTargetEClass.getESuperTypes().add(this.getUMLDestructionEventChange());
		umlIntervalConstraintChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlIntervalConstraintChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlIntervalConstraintChangeLeftTargetEClass.getESuperTypes().add(this.getUMLIntervalConstraintChange());
		umlIntervalConstraintChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlIntervalConstraintChangeRightTargetEClass.getESuperTypes().add(this.getUMLIntervalConstraintChange());
		umlMessageChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlMessageChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlMessageChangeLeftTargetEClass.getESuperTypes().add(this.getUMLMessageChange());
		umlMessageChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlMessageChangeRightTargetEClass.getESuperTypes().add(this.getUMLMessageChange());
		umlStereotypePropertyChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlStereotypeAttributeChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getAttributeChangeLeftTarget());
		umlStereotypeAttributeChangeLeftTargetEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeAttributeChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getAttributeChangeRightTarget());
		umlStereotypeAttributeChangeRightTargetEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeUpdateAttributeEClass.getESuperTypes().add(theDiffPackage.getUpdateAttribute());
		umlStereotypeUpdateAttributeEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeApplicationChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlStereotypeApplicationAdditionEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		umlStereotypeApplicationAdditionEClass.getESuperTypes().add(this.getUMLStereotypeApplicationChange());
		umlStereotypeApplicationRemovalEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		umlStereotypeApplicationRemovalEClass.getESuperTypes().add(this.getUMLStereotypeApplicationChange());
		umlStereotypeReferenceChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getReferenceChangeLeftTarget());
		umlStereotypeReferenceChangeLeftTargetEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeReferenceChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getReferenceChangeRightTarget());
		umlStereotypeReferenceChangeRightTargetEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeUpdateReferenceEClass.getESuperTypes().add(theDiffPackage.getUpdateReference());
		umlStereotypeUpdateReferenceEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeReferenceOrderChangeEClass.getESuperTypes().add(theDiffPackage.getReferenceOrderChange());
		umlStereotypeReferenceOrderChangeEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlProfileApplicationChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlProfileApplicationAdditionEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		umlProfileApplicationAdditionEClass.getESuperTypes().add(this.getUMLProfileApplicationChange());
		umlProfileApplicationRemovalEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		umlProfileApplicationRemovalEClass.getESuperTypes().add(this.getUMLProfileApplicationChange());
		umlElementChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlElementChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlElementChangeLeftTargetEClass.getESuperTypes().add(this.getUMLElementChange());
		umlElementChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlElementChangeRightTargetEClass.getESuperTypes().add(this.getUMLElementChange());

		// Initialize classes and features; add operations and parameters
		initEClass(umlDiffExtensionEClass, UMLDiffExtension.class, "UMLDiffExtension", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlAssociationChangeEClass, UMLAssociationChange.class, "UMLAssociationChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlAssociationChangeLeftTargetEClass, UMLAssociationChangeLeftTarget.class, "UMLAssociationChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlAssociationChangeRightTargetEClass, UMLAssociationChangeRightTarget.class, "UMLAssociationChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlAssociationBranchChangeEClass, UMLAssociationBranchChange.class, "UMLAssociationBranchChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlAssociationBranchChangeLeftTargetEClass, UMLAssociationBranchChangeLeftTarget.class, "UMLAssociationBranchChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlAssociationBranchChangeRightTargetEClass, UMLAssociationBranchChangeRightTarget.class, "UMLAssociationBranchChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDependencyBranchChangeEClass, UMLDependencyBranchChange.class, "UMLDependencyBranchChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDependencyBranchChangeLeftTargetEClass, UMLDependencyBranchChangeLeftTarget.class, "UMLDependencyBranchChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDependencyBranchChangeRightTargetEClass, UMLDependencyBranchChangeRightTarget.class, "UMLDependencyBranchChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlGeneralizationSetChangeEClass, UMLGeneralizationSetChange.class, "UMLGeneralizationSetChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlGeneralizationSetChangeLeftTargetEClass, UMLGeneralizationSetChangeLeftTarget.class, "UMLGeneralizationSetChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlGeneralizationSetChangeRightTargetEClass, UMLGeneralizationSetChangeRightTarget.class, "UMLGeneralizationSetChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDependencyChangeEClass, UMLDependencyChange.class, "UMLDependencyChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDependencyChangeLeftTargetEClass, UMLDependencyChangeLeftTarget.class, "UMLDependencyChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDependencyChangeRightTargetEClass, UMLDependencyChangeRightTarget.class, "UMLDependencyChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExtendChangeEClass, UMLExtendChange.class, "UMLExtendChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExtendChangeLeftTargetEClass, UMLExtendChangeLeftTarget.class, "UMLExtendChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExtendChangeRightTargetEClass, UMLExtendChangeRightTarget.class, "UMLExtendChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExecutionSpecificationChangeEClass, UMLExecutionSpecificationChange.class, "UMLExecutionSpecificationChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExecutionSpecificationChangeLeftTargetEClass, UMLExecutionSpecificationChangeLeftTarget.class, "UMLExecutionSpecificationChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExecutionSpecificationChangeRightTargetEClass, UMLExecutionSpecificationChangeRightTarget.class, "UMLExecutionSpecificationChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDestructionEventChangeEClass, UMLDestructionEventChange.class, "UMLDestructionEventChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDestructionEventChangeLeftTargetEClass, UMLDestructionEventChangeLeftTarget.class, "UMLDestructionEventChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDestructionEventChangeRightTargetEClass, UMLDestructionEventChangeRightTarget.class, "UMLDestructionEventChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlIntervalConstraintChangeEClass, UMLIntervalConstraintChange.class, "UMLIntervalConstraintChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlIntervalConstraintChangeLeftTargetEClass, UMLIntervalConstraintChangeLeftTarget.class, "UMLIntervalConstraintChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlIntervalConstraintChangeRightTargetEClass, UMLIntervalConstraintChangeRightTarget.class, "UMLIntervalConstraintChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlMessageChangeEClass, UMLMessageChange.class, "UMLMessageChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlMessageChangeLeftTargetEClass, UMLMessageChangeLeftTarget.class, "UMLMessageChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlMessageChangeRightTargetEClass, UMLMessageChangeRightTarget.class, "UMLMessageChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypePropertyChangeEClass, UMLStereotypePropertyChange.class, "UMLStereotypePropertyChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLStereotypePropertyChange_Stereotype(), theUMLPackage.getStereotype(), null, "stereotype", null, 0, 1, UMLStereotypePropertyChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlStereotypeAttributeChangeLeftTargetEClass, UMLStereotypeAttributeChangeLeftTarget.class, "UMLStereotypeAttributeChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeAttributeChangeRightTargetEClass, UMLStereotypeAttributeChangeRightTarget.class, "UMLStereotypeAttributeChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeUpdateAttributeEClass, UMLStereotypeUpdateAttribute.class, "UMLStereotypeUpdateAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeApplicationChangeEClass, UMLStereotypeApplicationChange.class, "UMLStereotypeApplicationChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLStereotypeApplicationChange_Stereotype(), theUMLPackage.getStereotype(), null, "stereotype", null, 0, 1, UMLStereotypeApplicationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlStereotypeApplicationAdditionEClass, UMLStereotypeApplicationAddition.class, "UMLStereotypeApplicationAddition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeApplicationRemovalEClass, UMLStereotypeApplicationRemoval.class, "UMLStereotypeApplicationRemoval", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeReferenceChangeLeftTargetEClass, UMLStereotypeReferenceChangeLeftTarget.class, "UMLStereotypeReferenceChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeReferenceChangeRightTargetEClass, UMLStereotypeReferenceChangeRightTarget.class, "UMLStereotypeReferenceChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeUpdateReferenceEClass, UMLStereotypeUpdateReference.class, "UMLStereotypeUpdateReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypeReferenceOrderChangeEClass, UMLStereotypeReferenceOrderChange.class, "UMLStereotypeReferenceOrderChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlProfileApplicationChangeEClass, UMLProfileApplicationChange.class, "UMLProfileApplicationChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLProfileApplicationChange_Profile(), theUMLPackage.getProfile(), null, "profile", null, 0, 1, UMLProfileApplicationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlProfileApplicationAdditionEClass, UMLProfileApplicationAddition.class, "UMLProfileApplicationAddition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlProfileApplicationRemovalEClass, UMLProfileApplicationRemoval.class, "UMLProfileApplicationRemoval", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlElementChangeEClass, UMLElementChange.class, "UMLElementChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlElementChangeLeftTargetEClass, UMLElementChangeLeftTarget.class, "UMLElementChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlElementChangeRightTargetEClass, UMLElementChangeRightTarget.class, "UMLElementChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} // UML2DiffPackageImpl
