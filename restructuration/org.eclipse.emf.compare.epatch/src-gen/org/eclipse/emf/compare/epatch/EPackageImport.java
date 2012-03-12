/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EPackage Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.epatch.EPackageImport#getNsURI <em>Ns URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getEPackageImport()
 * @model
 * @generated
 */
public interface EPackageImport extends ModelImport
{
  /**
	 * Returns the value of the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ns URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Ns URI</em>' attribute.
	 * @see #setNsURI(String)
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getEPackageImport_NsURI()
	 * @model
	 * @generated
	 */
  String getNsURI();

  /**
	 * Sets the value of the '{@link org.eclipse.emf.compare.epatch.EPackageImport#getNsURI <em>Ns URI</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ns URI</em>' attribute.
	 * @see #getNsURI()
	 * @generated
	 */
  void setNsURI(String value);

} // EPackageImport
