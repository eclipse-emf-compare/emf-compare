/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Opposite Ref Many To Many</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeOppositeRefManyToManyImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeOppositeRefManyToManyImpl#getDestination <em>Destination</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeOppositeRefManyToManyImpl extends NodeImpl implements NodeOppositeRefManyToMany {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected EList<NodeOppositeRefManyToMany> source;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected EList<NodeOppositeRefManyToMany> destination;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeOppositeRefManyToManyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_OPPOSITE_REF_MANY_TO_MANY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NodeOppositeRefManyToMany> getSource() {
		if (source == null) {
			source = new EObjectWithInverseResolvingEList.ManyInverse<NodeOppositeRefManyToMany>(NodeOppositeRefManyToMany.class, this, NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE, NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION);
		}
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NodeOppositeRefManyToMany> getDestination() {
		if (destination == null) {
			destination = new EObjectWithInverseResolvingEList.ManyInverse<NodeOppositeRefManyToMany>(NodeOppositeRefManyToMany.class, this, NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION, NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE);
		}
		return destination;
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
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSource()).basicAdd(otherEnd, msgs);
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDestination()).basicAdd(otherEnd, msgs);
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
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE:
				return ((InternalEList<?>)getSource()).basicRemove(otherEnd, msgs);
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION:
				return ((InternalEList<?>)getDestination()).basicRemove(otherEnd, msgs);
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
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE:
				return getSource();
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION:
				return getDestination();
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
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE:
				getSource().clear();
				getSource().addAll((Collection<? extends NodeOppositeRefManyToMany>)newValue);
				return;
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION:
				getDestination().clear();
				getDestination().addAll((Collection<? extends NodeOppositeRefManyToMany>)newValue);
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
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE:
				getSource().clear();
				return;
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION:
				getDestination().clear();
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
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE:
				return source != null && !source.isEmpty();
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION:
				return destination != null && !destination.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NodeOppositeRefManyToManyImpl
