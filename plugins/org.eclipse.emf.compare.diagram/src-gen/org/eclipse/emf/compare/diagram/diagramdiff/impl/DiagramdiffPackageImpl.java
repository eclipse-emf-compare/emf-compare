/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff.impl;

import org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DiagramdiffPackageImpl extends EPackageImpl implements DiagramdiffPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramDiffExtensionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramShowElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramHideElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramMoveNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramEdgeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramLabelChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramModelElementChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramModelElementChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramModelElementChangeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DiagramdiffPackageImpl() {
		super(eNS_URI, DiagramdiffFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link DiagramdiffPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DiagramdiffPackage init() {
		if (isInited) return (DiagramdiffPackage)EPackage.Registry.INSTANCE.getEPackage(DiagramdiffPackage.eNS_URI);

		// Obtain or create and register package
		DiagramdiffPackageImpl theDiagramdiffPackage = (DiagramdiffPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DiagramdiffPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DiagramdiffPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DiffPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDiagramdiffPackage.createPackageContents();

		// Initialize created meta-data
		theDiagramdiffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDiagramdiffPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DiagramdiffPackage.eNS_URI, theDiagramdiffPackage);
		return theDiagramdiffPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramDiffExtension() {
		return diagramDiffExtensionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramShowElement() {
		return diagramShowElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramHideElement() {
		return diagramHideElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramMoveNode() {
		return diagramMoveNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramEdgeChange() {
		return diagramEdgeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramLabelChange() {
		return diagramLabelChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramModelElementChangeLeftTarget() {
		return diagramModelElementChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramModelElementChangeRightTarget() {
		return diagramModelElementChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramModelElementChange() {
		return diagramModelElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramModelElementChange_SemanticDiff() {
		return (EReference)diagramModelElementChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramdiffFactory getDiagramdiffFactory() {
		return (DiagramdiffFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		diagramDiffExtensionEClass = createEClass(DIAGRAM_DIFF_EXTENSION);

		diagramShowElementEClass = createEClass(DIAGRAM_SHOW_ELEMENT);

		diagramHideElementEClass = createEClass(DIAGRAM_HIDE_ELEMENT);

		diagramMoveNodeEClass = createEClass(DIAGRAM_MOVE_NODE);

		diagramEdgeChangeEClass = createEClass(DIAGRAM_EDGE_CHANGE);

		diagramLabelChangeEClass = createEClass(DIAGRAM_LABEL_CHANGE);

		diagramModelElementChangeLeftTargetEClass = createEClass(DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET);

		diagramModelElementChangeRightTargetEClass = createEClass(DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET);

		diagramModelElementChangeEClass = createEClass(DIAGRAM_MODEL_ELEMENT_CHANGE);
		createEReference(diagramModelElementChangeEClass, DIAGRAM_MODEL_ELEMENT_CHANGE__SEMANTIC_DIFF);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		diagramDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getDiffElement());
		diagramDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getAbstractDiffExtension());
		diagramShowElementEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		diagramShowElementEClass.getESuperTypes().add(this.getDiagramDiffExtension());
		diagramHideElementEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		diagramHideElementEClass.getESuperTypes().add(this.getDiagramDiffExtension());
		diagramMoveNodeEClass.getESuperTypes().add(theDiffPackage.getMoveModelElement());
		diagramMoveNodeEClass.getESuperTypes().add(this.getDiagramDiffExtension());
		diagramEdgeChangeEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		diagramEdgeChangeEClass.getESuperTypes().add(this.getDiagramDiffExtension());
		diagramLabelChangeEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		diagramLabelChangeEClass.getESuperTypes().add(this.getDiagramDiffExtension());
		diagramModelElementChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		diagramModelElementChangeLeftTargetEClass.getESuperTypes().add(this.getDiagramModelElementChange());
		diagramModelElementChangeLeftTargetEClass.getESuperTypes().add(this.getDiagramDiffExtension());
		diagramModelElementChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		diagramModelElementChangeRightTargetEClass.getESuperTypes().add(this.getDiagramModelElementChange());
		diagramModelElementChangeRightTargetEClass.getESuperTypes().add(this.getDiagramDiffExtension());

		// Initialize classes and features; add operations and parameters
		initEClass(diagramDiffExtensionEClass, DiagramDiffExtension.class, "DiagramDiffExtension", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramShowElementEClass, DiagramShowElement.class, "DiagramShowElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramHideElementEClass, DiagramHideElement.class, "DiagramHideElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramMoveNodeEClass, DiagramMoveNode.class, "DiagramMoveNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramEdgeChangeEClass, DiagramEdgeChange.class, "DiagramEdgeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramLabelChangeEClass, DiagramLabelChange.class, "DiagramLabelChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramModelElementChangeLeftTargetEClass, DiagramModelElementChangeLeftTarget.class, "DiagramModelElementChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramModelElementChangeRightTargetEClass, DiagramModelElementChangeRightTarget.class, "DiagramModelElementChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(diagramModelElementChangeEClass, DiagramModelElementChange.class, "DiagramModelElementChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getDiagramModelElementChange_SemanticDiff(), theDiffPackage.getModelElementChange(), null, "semanticDiff", null, 0, 1, DiagramModelElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} //DiagramdiffPackageImpl
