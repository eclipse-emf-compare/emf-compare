/**
 * Copyright (c) 2013 Obeo.
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
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Comparison</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.impl.ComparisonImpl#getMatchedResources <em>Matched Resources</em>}</li>
 * <li>{@link org.eclipse.emf.compare.impl.ComparisonImpl#getMatches <em>Matches</em>}</li>
 * <li>{@link org.eclipse.emf.compare.impl.ComparisonImpl#getConflicts <em>Conflicts</em>}</li>
 * <li>{@link org.eclipse.emf.compare.impl.ComparisonImpl#getEquivalences <em>Equivalences</em>}</li>
 * <li>{@link org.eclipse.emf.compare.impl.ComparisonImpl#isThreeWay <em>Three Way</em>}</li>
 * <li>{@link org.eclipse.emf.compare.impl.ComparisonImpl#getDiagnostic <em>Diagnostic</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
@SuppressWarnings("boxing")
public class ComparisonImpl extends MinimalEObjectImpl implements Comparison {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMatchedResources() <em>Matched Resources</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMatchedResources()
	 * @generated
	 * @ordered
	 */
	protected EList<MatchResource> matchedResources;

	/**
	 * The cached value of the '{@link #getMatches() <em>Matches</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMatches()
	 * @generated
	 * @ordered
	 */
	protected EList<Match> matches;

	/**
	 * The cached value of the '{@link #getConflicts() <em>Conflicts</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getConflicts()
	 * @generated
	 * @ordered
	 */
	protected EList<Conflict> conflicts;

	/**
	 * The cached value of the '{@link #getEquivalences() <em>Equivalences</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEquivalences()
	 * @generated
	 * @ordered
	 */
	protected EList<Equivalence> equivalences;

	/**
	 * The default value of the '{@link #isThreeWay() <em>Three Way</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isThreeWay()
	 * @generated
	 * @ordered
	 */
	protected static final boolean THREE_WAY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isThreeWay() <em>Three Way</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isThreeWay()
	 * @generated
	 * @ordered
	 */
	protected boolean threeWay = THREE_WAY_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiagnostic() <em>Diagnostic</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getDiagnostic()
	 * @generated
	 * @ordered
	 */
	protected static final Diagnostic DIAGNOSTIC_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDiagnostic() <em>Diagnostic</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getDiagnostic()
	 * @generated
	 * @ordered
	 */
	protected Diagnostic diagnostic = DIAGNOSTIC_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ComparisonImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComparePackage.Literals.COMPARISON;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<MatchResource> getMatchedResources() {
		if (matchedResources == null) {
			matchedResources = new EObjectContainmentWithInverseEList<MatchResource>(MatchResource.class,
					this, ComparePackage.COMPARISON__MATCHED_RESOURCES,
					ComparePackage.MATCH_RESOURCE__COMPARISON);
		}
		return matchedResources;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Match> getMatches() {
		if (matches == null) {
			matches = new EObjectContainmentEList<Match>(Match.class, this,
					ComparePackage.COMPARISON__MATCHES);
		}
		return matches;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Conflict> getConflicts() {
		if (conflicts == null) {
			conflicts = new EObjectContainmentEList<Conflict>(Conflict.class, this,
					ComparePackage.COMPARISON__CONFLICTS);
		}
		return conflicts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Equivalence> getEquivalences() {
		if (equivalences == null) {
			equivalences = new EObjectContainmentEList<Equivalence>(Equivalence.class, this,
					ComparePackage.COMPARISON__EQUIVALENCES);
		}
		return equivalences;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Diff> getDifferences() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Diff> getDifferences(EObject element) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Match getMatch(EObject element) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IEqualityHelper getEqualityHelper() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ComparePackage.COMPARISON__MATCHED_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getMatchedResources()).basicAdd(
						otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isThreeWay() {
		return threeWay;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setThreeWay(boolean newThreeWay) {
		boolean oldThreeWay = threeWay;
		threeWay = newThreeWay;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.COMPARISON__THREE_WAY,
					oldThreeWay, threeWay));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diagnostic getDiagnostic() {
		return diagnostic;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDiagnostic(Diagnostic newDiagnostic) {
		Diagnostic oldDiagnostic = diagnostic;
		diagnostic = newDiagnostic;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.COMPARISON__DIAGNOSTIC,
					oldDiagnostic, diagnostic));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ComparePackage.COMPARISON__MATCHED_RESOURCES:
				return ((InternalEList<?>)getMatchedResources()).basicRemove(otherEnd, msgs);
			case ComparePackage.COMPARISON__MATCHES:
				return ((InternalEList<?>)getMatches()).basicRemove(otherEnd, msgs);
			case ComparePackage.COMPARISON__CONFLICTS:
				return ((InternalEList<?>)getConflicts()).basicRemove(otherEnd, msgs);
			case ComparePackage.COMPARISON__EQUIVALENCES:
				return ((InternalEList<?>)getEquivalences()).basicRemove(otherEnd, msgs);
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
			case ComparePackage.COMPARISON__MATCHED_RESOURCES:
				return getMatchedResources();
			case ComparePackage.COMPARISON__MATCHES:
				return getMatches();
			case ComparePackage.COMPARISON__CONFLICTS:
				return getConflicts();
			case ComparePackage.COMPARISON__EQUIVALENCES:
				return getEquivalences();
			case ComparePackage.COMPARISON__THREE_WAY:
				return isThreeWay();
			case ComparePackage.COMPARISON__DIAGNOSTIC:
				return getDiagnostic();
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
			case ComparePackage.COMPARISON__MATCHED_RESOURCES:
				getMatchedResources().clear();
				getMatchedResources().addAll((Collection<? extends MatchResource>)newValue);
				return;
			case ComparePackage.COMPARISON__MATCHES:
				getMatches().clear();
				getMatches().addAll((Collection<? extends Match>)newValue);
				return;
			case ComparePackage.COMPARISON__CONFLICTS:
				getConflicts().clear();
				getConflicts().addAll((Collection<? extends Conflict>)newValue);
				return;
			case ComparePackage.COMPARISON__EQUIVALENCES:
				getEquivalences().clear();
				getEquivalences().addAll((Collection<? extends Equivalence>)newValue);
				return;
			case ComparePackage.COMPARISON__THREE_WAY:
				setThreeWay((Boolean)newValue);
				return;
			case ComparePackage.COMPARISON__DIAGNOSTIC:
				setDiagnostic((Diagnostic)newValue);
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
			case ComparePackage.COMPARISON__MATCHED_RESOURCES:
				getMatchedResources().clear();
				return;
			case ComparePackage.COMPARISON__MATCHES:
				getMatches().clear();
				return;
			case ComparePackage.COMPARISON__CONFLICTS:
				getConflicts().clear();
				return;
			case ComparePackage.COMPARISON__EQUIVALENCES:
				getEquivalences().clear();
				return;
			case ComparePackage.COMPARISON__THREE_WAY:
				setThreeWay(THREE_WAY_EDEFAULT);
				return;
			case ComparePackage.COMPARISON__DIAGNOSTIC:
				setDiagnostic(DIAGNOSTIC_EDEFAULT);
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
			case ComparePackage.COMPARISON__MATCHED_RESOURCES:
				return matchedResources != null && !matchedResources.isEmpty();
			case ComparePackage.COMPARISON__MATCHES:
				return matches != null && !matches.isEmpty();
			case ComparePackage.COMPARISON__CONFLICTS:
				return conflicts != null && !conflicts.isEmpty();
			case ComparePackage.COMPARISON__EQUIVALENCES:
				return equivalences != null && !equivalences.isEmpty();
			case ComparePackage.COMPARISON__THREE_WAY:
				return threeWay != THREE_WAY_EDEFAULT;
			case ComparePackage.COMPARISON__DIAGNOSTIC:
				return DIAGNOSTIC_EDEFAULT == null ? diagnostic != null : !DIAGNOSTIC_EDEFAULT
						.equals(diagnostic);
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
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (threeWay: "); //$NON-NLS-1$
		result.append(threeWay);
		result.append(", diagnostic: "); //$NON-NLS-1$
		result.append(diagnostic);
		result.append(')');
		return result.toString();
	}

} // ComparisonImpl
