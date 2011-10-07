/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff;

import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Element Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange#getSemanticDiff <em>Semantic Diff</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage#getDiagramModelElementChange()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DiagramModelElementChange extends EObject {
	/**
	 * Returns the value of the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Semantic Diff</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Semantic Diff</em>' reference.
	 * @see #setSemanticDiff(ModelElementChange)
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage#getDiagramModelElementChange_SemanticDiff()
	 * @model
	 * @generated
	 */
	ModelElementChange getSemanticDiff();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange#getSemanticDiff <em>Semantic Diff</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Semantic Diff</em>' reference.
	 * @see #getSemanticDiff()
	 * @generated
	 */
	void setSemanticDiff(ModelElementChange value);

} // DiagramModelElementChange
