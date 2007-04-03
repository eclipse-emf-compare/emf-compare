/**
 * <copyright>
 * </copyright>
 *
 * $Id: Diff_extensionFactory.java,v 1.1 2007/04/03 06:37:12 cbrun Exp $
 */
package diff_extension;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see diff_extension.Diff_extensionPackage
 * @generated
 */
public interface Diff_extensionFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Diff_extensionFactory eINSTANCE = diff_extension.impl.Diff_extensionFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Rename Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rename Model Element</em>'.
	 * @generated
	 */
	RenameModelElement createRenameModelElement();

	/**
	 * Returns a new object of class '<em>Move Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Move Model Element</em>'.
	 * @generated
	 */
	MoveModelElement createMoveModelElement();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Diff_extensionPackage getDiff_extensionPackage();

} //Diff_extensionFactory
