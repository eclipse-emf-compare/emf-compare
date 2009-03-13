/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Import</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.epatch.ResourceImport#getUri <em>Uri</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getResourceImport()
 * @model
 * @generated
 */
public interface ResourceImport extends ModelImport {
	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getResourceImport_Uri()
	 * @model
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.epatch.ResourceImport#getUri <em>Uri</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

} // ResourceImport
