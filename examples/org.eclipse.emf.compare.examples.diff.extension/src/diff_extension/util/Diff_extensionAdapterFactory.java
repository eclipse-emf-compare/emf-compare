/**
 * <copyright>
 * </copyright>
 *
 * $Id: Diff_extensionAdapterFactory.java,v 1.1 2007/04/03 06:36:51 cbrun Exp $
 */
package diff_extension.util;

import diff_extension.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.compare.diff.AttributeChange;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.ModelElementChange;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.diff.UpdateModelElement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see diff_extension.Diff_extensionPackage
 * @generated
 */
public class Diff_extensionAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Diff_extensionPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diff_extensionAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Diff_extensionPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Diff_extensionSwitch modelSwitch =
		new Diff_extensionSwitch() {
			public Object caseRenameModelElement(RenameModelElement object) {
				return createRenameModelElementAdapter();
			}
			public Object caseMoveModelElement(MoveModelElement object) {
				return createMoveModelElementAdapter();
			}
			public Object caseDiffElement(DiffElement object) {
				return createDiffElementAdapter();
			}
			public Object caseAttributeChange(AttributeChange object) {
				return createAttributeChangeAdapter();
			}
			public Object caseUpdateAttribute(UpdateAttribute object) {
				return createUpdateAttributeAdapter();
			}
			public Object caseModelElementChange(ModelElementChange object) {
				return createModelElementChangeAdapter();
			}
			public Object caseUpdateModelElement(UpdateModelElement object) {
				return createUpdateModelElementAdapter();
			}
			public Object defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link diff_extension.RenameModelElement <em>Rename Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see diff_extension.RenameModelElement
	 * @generated
	 */
	public Adapter createRenameModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link diff_extension.MoveModelElement <em>Move Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see diff_extension.MoveModelElement
	 * @generated
	 */
	public Adapter createMoveModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.DiffElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.DiffElement
	 * @generated
	 */
	public Adapter createDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.AttributeChange
	 * @generated
	 */
	public Adapter createAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.UpdateAttribute <em>Update Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.UpdateAttribute
	 * @generated
	 */
	public Adapter createUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.ModelElementChange <em>Model Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.ModelElementChange
	 * @generated
	 */
	public Adapter createModelElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.UpdateModelElement <em>Update Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.UpdateModelElement
	 * @generated
	 */
	public Adapter createUpdateModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //Diff_extensionAdapterFactory
