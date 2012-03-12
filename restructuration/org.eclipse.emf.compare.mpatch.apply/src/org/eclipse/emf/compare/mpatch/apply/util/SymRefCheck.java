/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

/**
 * Wrapper class for Symbolic reference checks.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
class SymRefCheck {

	/**
	 * The symbolic references that was checked. Please see constructor documentation for details.
	 */
	final IElementReference symRef;

	/**
	 * The validation result. Please see constructor documentation for details.
	 */
	final ValidationResult validationResult;

	/**
	 * The list of resolved model elements. Please see constructor documentation for details.
	 */
	final List<EObject> elements;

	/**
	 * Whether this is an internal reference. Please see constructor documentation for details.
	 */
	final boolean internal;

	/**
	 * Check whether the given symbolic references is resolved successfully.
	 * 
	 * The result should check in this order:
	 * <ol>
	 * <li><code>symRef</code> might be <code>null</code>!
	 * <li>if <code>internal</code> is true, ignore <code>elements</code> and the <code>validationResult</code>
	 * <li>then, if the bounds are ok and some elements of the correct type are resolved, <code>validationResult</code>
	 * is <code>null</code>. otherwise, <code>validationResult</code> either results in
	 * {@link ValidationResult#REFERENCE} or {@link ValidationResult#STATE_INVALID}.
	 * </ol>
	 * 
	 * 
	 * @param symRef
	 *            A given symbolic reference.
	 * @param map
	 *            The resolution of it.
	 * @param type
	 *            The type which all resolved elements must have.
	 * @param upperBound
	 *            The maximum number of resolved elements.
	 */
	public SymRefCheck(IElementReference symRef, Map<IElementReference, List<EObject>> map, EClassifier type,
			int upperBound) {
		// default values:
		ValidationResult validation = null;
		elements = new ArrayList<EObject>();
		boolean internal = false;

		// perform checks:
		if (symRef == null) {
			// default values
		} else if (symRef instanceof ModelDescriptorReference) {
			internal = true;
		} else {
			// symref is set, so we need to check the resolution!
			final List<EObject> list = map.get(symRef);
			if (list != null)
				elements.addAll(list);
			if (elements.size() == 0 || (elements.size() > upperBound && upperBound > 0)) {
				validation = ValidationResult.REFERENCE;
			} else {

				// check type
				for (EObject element : elements) {
					if (!type.isInstance(element)) {
						validation = ValidationResult.STATE_INVALID;
						break;
					}
				}
			}
		}
		this.symRef = symRef;
		this.validationResult = validation;
		this.internal = internal;
	}
}