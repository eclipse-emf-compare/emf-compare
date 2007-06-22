/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelInputSnapshot.java,v 1.1 2007/06/22 12:59:46 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel;

import java.util.Date;

import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Input Snapshot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDate <em>Date</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDiff <em>Diff</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getMatch <em>Match</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelInputSnapshot()
 * @model
 * @generated
 */
public interface ModelInputSnapshot extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelInputSnapshot_Date()
	 * @model
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * Returns the value of the '<em><b>Diff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diff</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diff</em>' containment reference.
	 * @see #setDiff(DiffModel)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelInputSnapshot_Diff()
	 * @model containment="true"
	 * @generated
	 */
	DiffModel getDiff();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDiff <em>Diff</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diff</em>' containment reference.
	 * @see #getDiff()
	 * @generated
	 */
	void setDiff(DiffModel value);

	/**
	 * Returns the value of the '<em><b>Match</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Match</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Match</em>' containment reference.
	 * @see #setMatch(MatchModel)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelInputSnapshot_Match()
	 * @model containment="true"
	 * @generated
	 */
	MatchModel getMatch();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getMatch <em>Match</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Match</em>' containment reference.
	 * @see #getMatch()
	 * @generated
	 */
	void setMatch(MatchModel value);

} // ModelInputSnapshot