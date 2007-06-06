/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiffGroupImpl.java,v 1.4 2007/06/06 07:40:51 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.impl;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.diff.DiffGroup;
import org.eclipse.emf.compare.diff.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.DiffGroupImpl#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.DiffGroupImpl#getSubchanges <em>Subchanges</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiffGroupImpl extends DiffElementImpl implements DiffGroup {
	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent = null;

	/**
	 * The default value of the '{@link #getSubchanges() <em>Subchanges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubchanges()
	 * @generated
	 * @ordered
	 */
	protected static final int SUBCHANGES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSubchanges() <em>Subchanges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubchanges()
	 * @generated
	 * @ordered
	 */
	protected int subchanges = SUBCHANGES_EDEFAULT;

	/**
	 * This is true if the Subchanges attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean subchangesESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.DIFF_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject)leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.DIFF_GROUP__LEFT_PARENT, oldLeftParent, leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftParent() {
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftParent(EObject newLeftParent) {
		EObject oldLeftParent = leftParent;
		leftParent = newLeftParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_GROUP__LEFT_PARENT, oldLeftParent, leftParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getSubchanges() {
		Iterator it = getSubDiffElements().iterator();
		int result = 0;
		while (it.hasNext())
		{
			EObject eObj = (EObject)it.next();
			if (eObj instanceof DiffGroup)
			{
				result += ((DiffGroup)eObj).getSubchanges();
			}else
			{
				result += 1;
			}
				
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubchanges(int newSubchanges) {
		int oldSubchanges = subchanges;
		subchanges = newSubchanges;
		boolean oldSubchangesESet = subchangesESet;
		subchangesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_GROUP__SUBCHANGES, oldSubchanges, subchanges, !oldSubchangesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSubchanges() {
		int oldSubchanges = subchanges;
		boolean oldSubchangesESet = subchangesESet;
		subchanges = SUBCHANGES_EDEFAULT;
		subchangesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DiffPackage.DIFF_GROUP__SUBCHANGES, oldSubchanges, SUBCHANGES_EDEFAULT, oldSubchangesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSubchanges() {
		return subchangesESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__LEFT_PARENT:
				if (resolve) return getLeftParent();
				return basicGetLeftParent();
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				return new Integer(getSubchanges());
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__LEFT_PARENT:
				setLeftParent((EObject)newValue);
				return;
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				setSubchanges(((Integer)newValue).intValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__LEFT_PARENT:
				setLeftParent((EObject)null);
				return;
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				unsetSubchanges();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__LEFT_PARENT:
				return leftParent != null;
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				return isSetSubchanges();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (subchanges: ");
		if (subchangesESet) result.append(subchanges); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //DiffGroupImpl