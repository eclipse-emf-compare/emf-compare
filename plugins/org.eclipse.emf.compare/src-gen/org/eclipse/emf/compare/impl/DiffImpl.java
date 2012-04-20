/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diff</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getMatch <em>Match</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getRequires <em>Requires</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getRequiredBy <em>Required By</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getRefines <em>Refines</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getRefinedBy <em>Refined By</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getEquivalentDiffs <em>Equivalent Diffs</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.DiffImpl#getConflict <em>Conflict</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
// Supressing warnings : generated code
@SuppressWarnings("all")
public class DiffImpl extends MinimalEObjectImpl implements Diff {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected EList<Diff> requires;

	/**
	 * The cached value of the '{@link #getRequiredBy() <em>Required By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredBy()
	 * @generated
	 * @ordered
	 */
	protected EList<Diff> requiredBy;

	/**
	 * The cached value of the '{@link #getRefines() <em>Refines</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefines()
	 * @generated
	 * @ordered
	 */
	protected EList<Diff> refines;

	/**
	 * The cached value of the '{@link #getRefinedBy() <em>Refined By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefinedBy()
	 * @generated
	 * @ordered
	 */
	protected EList<Diff> refinedBy;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final DifferenceKind KIND_EDEFAULT = DifferenceKind.ADD;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected DifferenceKind kind = KIND_EDEFAULT;

	/**
	 * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected static final DifferenceSource SOURCE_EDEFAULT = DifferenceSource.LEFT;

	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected DifferenceSource source = SOURCE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEquivalentDiffs() <em>Equivalent Diffs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEquivalentDiffs()
	 * @generated
	 * @ordered
	 */
	protected Equivalence equivalentDiffs;

	/**
	 * The cached value of the '{@link #getConflict() <em>Conflict</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConflict()
	 * @generated
	 * @ordered
	 */
	protected Conflict conflict;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComparePackage.Literals.DIFF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Match getMatch() {
		if (eContainerFeatureID() != ComparePackage.DIFF__MATCH)
			return null;
		return (Match)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMatch(Match newMatch, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newMatch, ComparePackage.DIFF__MATCH, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMatch(Match newMatch) {
		if (newMatch != eInternalContainer()
				|| (eContainerFeatureID() != ComparePackage.DIFF__MATCH && newMatch != null)) {
			if (EcoreUtil.isAncestor(this, newMatch))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newMatch != null)
				msgs = ((InternalEObject)newMatch).eInverseAdd(this, ComparePackage.MATCH__DIFFERENCES,
						Match.class, msgs);
			msgs = basicSetMatch(newMatch, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__MATCH, newMatch,
					newMatch));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Diff> getRequires() {
		if (requires == null) {
			requires = new EObjectWithInverseResolvingEList.ManyInverse<Diff>(Diff.class, this,
					ComparePackage.DIFF__REQUIRES, ComparePackage.DIFF__REQUIRED_BY);
		}
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Diff> getRequiredBy() {
		if (requiredBy == null) {
			requiredBy = new EObjectWithInverseResolvingEList.ManyInverse<Diff>(Diff.class, this,
					ComparePackage.DIFF__REQUIRED_BY, ComparePackage.DIFF__REQUIRES);
		}
		return requiredBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Diff> getRefines() {
		if (refines == null) {
			refines = new EObjectWithInverseResolvingEList.ManyInverse<Diff>(Diff.class, this,
					ComparePackage.DIFF__REFINES, ComparePackage.DIFF__REFINED_BY);
		}
		return refines;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Diff> getRefinedBy() {
		if (refinedBy == null) {
			refinedBy = new EObjectWithInverseResolvingEList.ManyInverse<Diff>(Diff.class, this,
					ComparePackage.DIFF__REFINED_BY, ComparePackage.DIFF__REFINES);
		}
		return refinedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DifferenceKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(DifferenceKind newKind) {
		DifferenceKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Equivalence getEquivalentDiffs() {
		if (equivalentDiffs != null && equivalentDiffs.eIsProxy()) {
			InternalEObject oldEquivalentDiffs = (InternalEObject)equivalentDiffs;
			equivalentDiffs = (Equivalence)eResolveProxy(oldEquivalentDiffs);
			if (equivalentDiffs != oldEquivalentDiffs) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ComparePackage.DIFF__EQUIVALENT_DIFFS, oldEquivalentDiffs, equivalentDiffs));
			}
		}
		return equivalentDiffs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Equivalence basicGetEquivalentDiffs() {
		return equivalentDiffs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEquivalentDiffs(Equivalence newEquivalentDiffs, NotificationChain msgs) {
		Equivalence oldEquivalentDiffs = equivalentDiffs;
		equivalentDiffs = newEquivalentDiffs;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComparePackage.DIFF__EQUIVALENT_DIFFS, oldEquivalentDiffs, newEquivalentDiffs);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEquivalentDiffs(Equivalence newEquivalentDiffs) {
		if (newEquivalentDiffs != equivalentDiffs) {
			NotificationChain msgs = null;
			if (equivalentDiffs != null)
				msgs = ((InternalEObject)equivalentDiffs).eInverseRemove(this,
						ComparePackage.EQUIVALENCE__DIFFERENCES, Equivalence.class, msgs);
			if (newEquivalentDiffs != null)
				msgs = ((InternalEObject)newEquivalentDiffs).eInverseAdd(this,
						ComparePackage.EQUIVALENCE__DIFFERENCES, Equivalence.class, msgs);
			msgs = basicSetEquivalentDiffs(newEquivalentDiffs, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__EQUIVALENT_DIFFS,
					newEquivalentDiffs, newEquivalentDiffs));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Conflict getConflict() {
		if (conflict != null && conflict.eIsProxy()) {
			InternalEObject oldConflict = (InternalEObject)conflict;
			conflict = (Conflict)eResolveProxy(oldConflict);
			if (conflict != oldConflict) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ComparePackage.DIFF__CONFLICT,
							oldConflict, conflict));
			}
		}
		return conflict;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Conflict basicGetConflict() {
		return conflict;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConflict(Conflict newConflict, NotificationChain msgs) {
		Conflict oldConflict = conflict;
		conflict = newConflict;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComparePackage.DIFF__CONFLICT, oldConflict, newConflict);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConflict(Conflict newConflict) {
		if (newConflict != conflict) {
			NotificationChain msgs = null;
			if (conflict != null)
				msgs = ((InternalEObject)conflict).eInverseRemove(this, ComparePackage.CONFLICT__DIFFERENCES,
						Conflict.class, msgs);
			if (newConflict != null)
				msgs = ((InternalEObject)newConflict).eInverseAdd(this, ComparePackage.CONFLICT__DIFFERENCES,
						Conflict.class, msgs);
			msgs = basicSetConflict(newConflict, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__CONFLICT, newConflict,
					newConflict));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DifferenceSource getSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(DifferenceSource newSource) {
		DifferenceSource oldSource = source;
		source = newSource == null ? SOURCE_EDEFAULT : newSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__SOURCE, oldSource,
					source));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void apply() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void reverse() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void discard() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
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
			case ComparePackage.DIFF__MATCH:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetMatch((Match)otherEnd, msgs);
			case ComparePackage.DIFF__REQUIRES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRequires()).basicAdd(otherEnd,
						msgs);
			case ComparePackage.DIFF__REQUIRED_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRequiredBy()).basicAdd(otherEnd,
						msgs);
			case ComparePackage.DIFF__REFINES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRefines()).basicAdd(otherEnd,
						msgs);
			case ComparePackage.DIFF__REFINED_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRefinedBy()).basicAdd(otherEnd,
						msgs);
			case ComparePackage.DIFF__EQUIVALENT_DIFFS:
				if (equivalentDiffs != null)
					msgs = ((InternalEObject)equivalentDiffs).eInverseRemove(this,
							ComparePackage.EQUIVALENCE__DIFFERENCES, Equivalence.class, msgs);
				return basicSetEquivalentDiffs((Equivalence)otherEnd, msgs);
			case ComparePackage.DIFF__CONFLICT:
				if (conflict != null)
					msgs = ((InternalEObject)conflict).eInverseRemove(this,
							ComparePackage.CONFLICT__DIFFERENCES, Conflict.class, msgs);
				return basicSetConflict((Conflict)otherEnd, msgs);
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
			case ComparePackage.DIFF__MATCH:
				return basicSetMatch(null, msgs);
			case ComparePackage.DIFF__REQUIRES:
				return ((InternalEList<?>)getRequires()).basicRemove(otherEnd, msgs);
			case ComparePackage.DIFF__REQUIRED_BY:
				return ((InternalEList<?>)getRequiredBy()).basicRemove(otherEnd, msgs);
			case ComparePackage.DIFF__REFINES:
				return ((InternalEList<?>)getRefines()).basicRemove(otherEnd, msgs);
			case ComparePackage.DIFF__REFINED_BY:
				return ((InternalEList<?>)getRefinedBy()).basicRemove(otherEnd, msgs);
			case ComparePackage.DIFF__EQUIVALENT_DIFFS:
				return basicSetEquivalentDiffs(null, msgs);
			case ComparePackage.DIFF__CONFLICT:
				return basicSetConflict(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ComparePackage.DIFF__MATCH:
				return eInternalContainer().eInverseRemove(this, ComparePackage.MATCH__DIFFERENCES,
						Match.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ComparePackage.DIFF__MATCH:
				return getMatch();
			case ComparePackage.DIFF__REQUIRES:
				return getRequires();
			case ComparePackage.DIFF__REQUIRED_BY:
				return getRequiredBy();
			case ComparePackage.DIFF__REFINES:
				return getRefines();
			case ComparePackage.DIFF__REFINED_BY:
				return getRefinedBy();
			case ComparePackage.DIFF__KIND:
				return getKind();
			case ComparePackage.DIFF__SOURCE:
				return getSource();
			case ComparePackage.DIFF__EQUIVALENT_DIFFS:
				if (resolve)
					return getEquivalentDiffs();
				return basicGetEquivalentDiffs();
			case ComparePackage.DIFF__CONFLICT:
				if (resolve)
					return getConflict();
				return basicGetConflict();
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
			case ComparePackage.DIFF__MATCH:
				setMatch((Match)newValue);
				return;
			case ComparePackage.DIFF__REQUIRES:
				getRequires().clear();
				getRequires().addAll((Collection<? extends Diff>)newValue);
				return;
			case ComparePackage.DIFF__REQUIRED_BY:
				getRequiredBy().clear();
				getRequiredBy().addAll((Collection<? extends Diff>)newValue);
				return;
			case ComparePackage.DIFF__REFINES:
				getRefines().clear();
				getRefines().addAll((Collection<? extends Diff>)newValue);
				return;
			case ComparePackage.DIFF__REFINED_BY:
				getRefinedBy().clear();
				getRefinedBy().addAll((Collection<? extends Diff>)newValue);
				return;
			case ComparePackage.DIFF__KIND:
				setKind((DifferenceKind)newValue);
				return;
			case ComparePackage.DIFF__SOURCE:
				setSource((DifferenceSource)newValue);
				return;
			case ComparePackage.DIFF__EQUIVALENT_DIFFS:
				setEquivalentDiffs((Equivalence)newValue);
				return;
			case ComparePackage.DIFF__CONFLICT:
				setConflict((Conflict)newValue);
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
			case ComparePackage.DIFF__MATCH:
				setMatch((Match)null);
				return;
			case ComparePackage.DIFF__REQUIRES:
				getRequires().clear();
				return;
			case ComparePackage.DIFF__REQUIRED_BY:
				getRequiredBy().clear();
				return;
			case ComparePackage.DIFF__REFINES:
				getRefines().clear();
				return;
			case ComparePackage.DIFF__REFINED_BY:
				getRefinedBy().clear();
				return;
			case ComparePackage.DIFF__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case ComparePackage.DIFF__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case ComparePackage.DIFF__EQUIVALENT_DIFFS:
				setEquivalentDiffs((Equivalence)null);
				return;
			case ComparePackage.DIFF__CONFLICT:
				setConflict((Conflict)null);
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
			case ComparePackage.DIFF__MATCH:
				return getMatch() != null;
			case ComparePackage.DIFF__REQUIRES:
				return requires != null && !requires.isEmpty();
			case ComparePackage.DIFF__REQUIRED_BY:
				return requiredBy != null && !requiredBy.isEmpty();
			case ComparePackage.DIFF__REFINES:
				return refines != null && !refines.isEmpty();
			case ComparePackage.DIFF__REFINED_BY:
				return refinedBy != null && !refinedBy.isEmpty();
			case ComparePackage.DIFF__KIND:
				return kind != KIND_EDEFAULT;
			case ComparePackage.DIFF__SOURCE:
				return source != SOURCE_EDEFAULT;
			case ComparePackage.DIFF__EQUIVALENT_DIFFS:
				return equivalentDiffs != null;
			case ComparePackage.DIFF__CONFLICT:
				return conflict != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (kind: "); //$NON-NLS-1$
		result.append(kind);
		result.append(", source: "); //$NON-NLS-1$
		result.append(source);
		result.append(')');
		return result.toString();
	}

} //DiffImpl
