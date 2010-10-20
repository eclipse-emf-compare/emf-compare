/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: IndepChangeImpl.java,v 1.2 2010/10/20 09:22:23 pkonemann Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.ChangeKind;
import org.eclipse.emf.compare.mpatch.ChangeType;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepElementChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl#getCorrespondingElement <em>Corresponding Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl#getChangeKind <em>Change Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl#getChangeType <em>Change Type</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl#getDependsOn <em>Depends On</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl#getDependants <em>Dependants</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl#getResultingElement <em>Resulting Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class IndepChangeImpl extends EObjectImpl implements IndepChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * The cached value of the '{@link #getCorrespondingElement() <em>Corresponding Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrespondingElement()
	 * @generated
	 * @ordered
	 */
	protected IElementReference correspondingElement;

	/**
	 * The default value of the '{@link #getChangeKind() <em>Change Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeKind()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeKind CHANGE_KIND_EDEFAULT = ChangeKind.ADDITION;

	/**
	 * The default value of the '{@link #getChangeType() <em>Change Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeType()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeType CHANGE_TYPE_EDEFAULT = ChangeType.ELEMENT;

	/**
	 * The cached value of the '{@link #getDependsOn() <em>Depends On</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependsOn()
	 * @generated
	 * @ordered
	 */
	protected EList<IndepChange> dependsOn;

	/**
	 * The cached value of the '{@link #getDependants() <em>Dependants</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependants()
	 * @generated
	 * @ordered
	 */
	protected EList<IndepChange> dependants;

	/**
	 * The cached value of the '{@link #getResultingElement() <em>Resulting Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultingElement()
	 * @generated
	 * @ordered
	 */
	protected IElementReference resultingElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndepChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.INDEP_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getCorrespondingElement() {
		return correspondingElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCorrespondingElement(IElementReference newCorrespondingElement, NotificationChain msgs) {
		IElementReference oldCorrespondingElement = correspondingElement;
		correspondingElement = newCorrespondingElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT, oldCorrespondingElement, newCorrespondingElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCorrespondingElement(IElementReference newCorrespondingElement) {
		if (newCorrespondingElement != correspondingElement) {
			NotificationChain msgs = null;
			if (correspondingElement != null)
				msgs = ((InternalEObject)correspondingElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT, null, msgs);
			if (newCorrespondingElement != null)
				msgs = ((InternalEObject)newCorrespondingElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT, null, msgs);
			msgs = basicSetCorrespondingElement(newCorrespondingElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT, newCorrespondingElement, newCorrespondingElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ChangeKind getChangeKind() {
		if (this instanceof IndepAddElementChange || this instanceof IndepAddAttributeChange || this instanceof IndepAddReferenceChange) {
			return ChangeKind.ADDITION; 
		} else if (this instanceof IndepRemoveElementChange || this instanceof IndepRemoveAttributeChange || this instanceof IndepRemoveReferenceChange) {
			return ChangeKind.DELETION; 
		} else if (this instanceof IndepUpdateAttributeChange || this instanceof IndepUpdateReferenceChange) {
			return ChangeKind.CHANGE; 
		} else if (this instanceof IndepMoveElementChange) {
			return ChangeKind.MOVE;
		} else if (this instanceof ChangeGroup) {
			return ChangeKind.GROUP;
		} else return ChangeKind.UNKNOWN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ChangeType getChangeType() {
		if (this instanceof IndepElementChange) {
			return ChangeType.ELEMENT;
		} else if (this instanceof IndepAttributeChange) {
			return ChangeType.ATTRIBUTE;
		} else if (this instanceof IndepReferenceChange) {
			return ChangeType.REFERENCE;
		} else if (this instanceof ChangeGroup) {
			return ChangeType.GROUP;
		} else return ChangeType.UNKNOWN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IndepChange> getDependsOn() {
		if (dependsOn == null) {
			dependsOn = new EObjectWithInverseResolvingEList.ManyInverse<IndepChange>(IndepChange.class, this, MPatchPackage.INDEP_CHANGE__DEPENDS_ON, MPatchPackage.INDEP_CHANGE__DEPENDANTS);
		}
		return dependsOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IndepChange> getDependants() {
		if (dependants == null) {
			dependants = new EObjectWithInverseResolvingEList.ManyInverse<IndepChange>(IndepChange.class, this, MPatchPackage.INDEP_CHANGE__DEPENDANTS, MPatchPackage.INDEP_CHANGE__DEPENDS_ON);
		}
		return dependants;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getResultingElement() {
		return resultingElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResultingElement(IElementReference newResultingElement, NotificationChain msgs) {
		IElementReference oldResultingElement = resultingElement;
		resultingElement = newResultingElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT, oldResultingElement, newResultingElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResultingElement(IElementReference newResultingElement) {
		if (newResultingElement != resultingElement) {
			NotificationChain msgs = null;
			if (resultingElement != null)
				msgs = ((InternalEObject)resultingElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT, null, msgs);
			if (newResultingElement != null)
				msgs = ((InternalEObject)newResultingElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT, null, msgs);
			msgs = basicSetResultingElement(newResultingElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT, newResultingElement, newResultingElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.INDEP_CHANGE__DEPENDS_ON:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDependsOn()).basicAdd(otherEnd, msgs);
			case MPatchPackage.INDEP_CHANGE__DEPENDANTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDependants()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT:
				return basicSetCorrespondingElement(null, msgs);
			case MPatchPackage.INDEP_CHANGE__DEPENDS_ON:
				return ((InternalEList<?>)getDependsOn()).basicRemove(otherEnd, msgs);
			case MPatchPackage.INDEP_CHANGE__DEPENDANTS:
				return ((InternalEList<?>)getDependants()).basicRemove(otherEnd, msgs);
			case MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT:
				return basicSetResultingElement(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT:
				return getCorrespondingElement();
			case MPatchPackage.INDEP_CHANGE__CHANGE_KIND:
				return getChangeKind();
			case MPatchPackage.INDEP_CHANGE__CHANGE_TYPE:
				return getChangeType();
			case MPatchPackage.INDEP_CHANGE__DEPENDS_ON:
				return getDependsOn();
			case MPatchPackage.INDEP_CHANGE__DEPENDANTS:
				return getDependants();
			case MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT:
				return getResultingElement();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT:
				setCorrespondingElement((IElementReference)newValue);
				return;
			case MPatchPackage.INDEP_CHANGE__DEPENDS_ON:
				getDependsOn().clear();
				getDependsOn().addAll((Collection<? extends IndepChange>)newValue);
				return;
			case MPatchPackage.INDEP_CHANGE__DEPENDANTS:
				getDependants().clear();
				getDependants().addAll((Collection<? extends IndepChange>)newValue);
				return;
			case MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT:
				setResultingElement((IElementReference)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT:
				setCorrespondingElement((IElementReference)null);
				return;
			case MPatchPackage.INDEP_CHANGE__DEPENDS_ON:
				getDependsOn().clear();
				return;
			case MPatchPackage.INDEP_CHANGE__DEPENDANTS:
				getDependants().clear();
				return;
			case MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT:
				setResultingElement((IElementReference)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MPatchPackage.INDEP_CHANGE__CORRESPONDING_ELEMENT:
				return correspondingElement != null;
			case MPatchPackage.INDEP_CHANGE__CHANGE_KIND:
				return getChangeKind() != CHANGE_KIND_EDEFAULT;
			case MPatchPackage.INDEP_CHANGE__CHANGE_TYPE:
				return getChangeType() != CHANGE_TYPE_EDEFAULT;
			case MPatchPackage.INDEP_CHANGE__DEPENDS_ON:
				return dependsOn != null && !dependsOn.isEmpty();
			case MPatchPackage.INDEP_CHANGE__DEPENDANTS:
				return dependants != null && !dependants.isEmpty();
			case MPatchPackage.INDEP_CHANGE__RESULTING_ELEMENT:
				return resultingElement != null;
		}
		return super.eIsSet(featureID);
	}

} //IndepChangeImpl
