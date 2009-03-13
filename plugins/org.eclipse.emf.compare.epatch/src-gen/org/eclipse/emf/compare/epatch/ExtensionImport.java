/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Extension Import</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.epatch.ExtensionImport#getPath <em>Path</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getExtensionImport()
 * @model
 * @generated
 */
public interface ExtensionImport extends Import {
	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute list. The list contents are of type
	 * {@link java.lang.String}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Path</em>' attribute list.
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getExtensionImport_Path()
	 * @model unique="false"
	 * @generated
	 */
	EList<String> getPath();

} // ExtensionImport
