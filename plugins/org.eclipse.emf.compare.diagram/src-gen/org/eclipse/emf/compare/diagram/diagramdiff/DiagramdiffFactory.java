/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage
 * @generated
 */
public interface DiagramdiffFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	DiagramdiffFactory eINSTANCE = org.eclipse.emf.compare.diagram.diagramdiff.impl.BusinessDiagramdiffFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Diagram Show Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Show Element</em>'.
	 * @generated
	 */
	DiagramShowElement createDiagramShowElement();

	/**
	 * Returns a new object of class '<em>Diagram Hide Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Hide Element</em>'.
	 * @generated
	 */
	DiagramHideElement createDiagramHideElement();

	/**
	 * Returns a new object of class '<em>Diagram Move Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Move Node</em>'.
	 * @generated
	 */
	DiagramMoveNode createDiagramMoveNode();

	/**
	 * Returns a new object of class '<em>Diagram Edge Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Edge Change</em>'.
	 * @generated
	 */
	DiagramEdgeChange createDiagramEdgeChange();

	/**
	 * Returns a new object of class '<em>Diagram Label Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Label Change</em>'.
	 * @generated
	 */
	DiagramLabelChange createDiagramLabelChange();

	/**
	 * Returns a new object of class '<em>Diagram Model Element Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Model Element Change Left Target</em>'.
	 * @generated
	 */
	DiagramModelElementChangeLeftTarget createDiagramModelElementChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>Diagram Model Element Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Model Element Change Right Target</em>'.
	 * @generated
	 */
	DiagramModelElementChangeRightTarget createDiagramModelElementChangeRightTarget();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DiagramdiffPackage getDiagramdiffPackage();

} //DiagramdiffFactory
