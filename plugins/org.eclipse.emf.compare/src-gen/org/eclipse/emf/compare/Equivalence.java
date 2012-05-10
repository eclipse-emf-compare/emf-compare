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
package org.eclipse.emf.compare;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Equivalence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Diffs are considered equivalent if merging one is enough to resolve both differences. For example, if a reference has an eOpposite, we will detect one diff for each side of the bidirectional reference, yet merging one of these diffs will automatically update the model in such a way that the second diff is "merged".
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.Equivalence#getDifferences <em>Differences</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.ComparePackage#getEquivalence()
 * @model
 * @generated
 */
public interface Equivalence extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Differences</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.Diff}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.Diff#getEquivalence <em>Equivalence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * References the <i>n</i> diffs composing this equivalence. There are <u>at least</u> two diffs in this list.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Differences</em>' reference list.
	 * @see org.eclipse.emf.compare.ComparePackage#getEquivalence_Differences()
	 * @see org.eclipse.emf.compare.Diff#getEquivalence
	 * @model opposite="equivalence" lower="2"
	 * @generated
	 */
	EList<Diff> getDifferences();

} // Equivalence
