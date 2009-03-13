/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.EpatchPackage;
import org.eclipse.emf.compare.epatch.Import;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectRef;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Epatch</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.EpatchImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.EpatchImpl#getImports <em>Imports</em>}</li>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.EpatchImpl#getResources <em>Resources</em>}</li>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.EpatchImpl#getObjects <em>Objects</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class EpatchImpl extends MinimalEObjectImpl.Container implements Epatch {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getImports() <em>Imports</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
	protected EList<Import> imports;

	/**
	 * The cached value of the '{@link #getResources() <em>Resources</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getResources()
	 * @generated
	 * @ordered
	 */
	protected EList<NamedResource> resources;

	/**
	 * The cached value of the '{@link #getObjects() <em>Objects</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<ObjectRef> objects;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EpatchImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EpatchPackage.Literals.EPATCH;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EpatchPackage.EPATCH__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Import> getImports() {
		if (imports == null) {
			imports = new EObjectContainmentEList<Import>(Import.class, this, EpatchPackage.EPATCH__IMPORTS);
		}
		return imports;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<NamedResource> getResources() {
		if (resources == null) {
			resources = new EObjectContainmentEList<NamedResource>(NamedResource.class, this,
					EpatchPackage.EPATCH__RESOURCES);
		}
		return resources;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ObjectRef> getObjects() {
		if (objects == null) {
			objects = new EObjectContainmentEList<ObjectRef>(ObjectRef.class, this,
					EpatchPackage.EPATCH__OBJECTS);
		}
		return objects;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EpatchPackage.EPATCH__IMPORTS:
				return ((InternalEList<?>)getImports()).basicRemove(otherEnd, msgs);
			case EpatchPackage.EPATCH__RESOURCES:
				return ((InternalEList<?>)getResources()).basicRemove(otherEnd, msgs);
			case EpatchPackage.EPATCH__OBJECTS:
				return ((InternalEList<?>)getObjects()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EpatchPackage.EPATCH__NAME:
				return getName();
			case EpatchPackage.EPATCH__IMPORTS:
				return getImports();
			case EpatchPackage.EPATCH__RESOURCES:
				return getResources();
			case EpatchPackage.EPATCH__OBJECTS:
				return getObjects();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EpatchPackage.EPATCH__NAME:
				setName((String)newValue);
				return;
			case EpatchPackage.EPATCH__IMPORTS:
				getImports().clear();
				getImports().addAll((Collection<? extends Import>)newValue);
				return;
			case EpatchPackage.EPATCH__RESOURCES:
				getResources().clear();
				getResources().addAll((Collection<? extends NamedResource>)newValue);
				return;
			case EpatchPackage.EPATCH__OBJECTS:
				getObjects().clear();
				getObjects().addAll((Collection<? extends ObjectRef>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EpatchPackage.EPATCH__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EpatchPackage.EPATCH__IMPORTS:
				getImports().clear();
				return;
			case EpatchPackage.EPATCH__RESOURCES:
				getResources().clear();
				return;
			case EpatchPackage.EPATCH__OBJECTS:
				getObjects().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EpatchPackage.EPATCH__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EpatchPackage.EPATCH__IMPORTS:
				return imports != null && !imports.isEmpty();
			case EpatchPackage.EPATCH__RESOURCES:
				return resources != null && !resources.isEmpty();
			case EpatchPackage.EPATCH__OBJECTS:
				return objects != null && !objects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // EpatchImpl
