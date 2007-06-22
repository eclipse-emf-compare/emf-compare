/**
 * <copyright>
 * </copyright>
 *
 * $Id: Diff_extensionFactoryImpl.java,v 1.2 2007/06/22 12:59:54 cbrun Exp $
 */
package diff_extension.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import diff_extension.Diff_extensionFactory;
import diff_extension.Diff_extensionPackage;
import diff_extension.MoveModelElement;
import diff_extension.RenameModelElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Diff_extensionFactoryImpl extends EFactoryImpl implements Diff_extensionFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Diff_extensionFactory init() {
		try {
			Diff_extensionFactory theDiff_extensionFactory = (Diff_extensionFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/sample/diff_extension/1.0"); 
			if (theDiff_extensionFactory != null) {
				return theDiff_extensionFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Diff_extensionFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diff_extensionFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Diff_extensionPackage.RENAME_MODEL_ELEMENT: return createRenameModelElement();
			case Diff_extensionPackage.MOVE_MODEL_ELEMENT: return createMoveModelElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RenameModelElement createRenameModelElement() {
		RenameModelElementImpl renameModelElement = new RenameModelElementImpl();
		return renameModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MoveModelElement createMoveModelElement() {
		MoveModelElementImpl moveModelElement = new MoveModelElementImpl();
		return moveModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diff_extensionPackage getDiff_extensionPackage() {
		return (Diff_extensionPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static Diff_extensionPackage getPackage() {
		return Diff_extensionPackage.eINSTANCE;
	}

} //Diff_extensionFactoryImpl
