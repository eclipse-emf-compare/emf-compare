/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff;

import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch;
import org.eclipse.emf.ecore.EObject;

/**
 * A switch that returns true if left and/or right {@link EObject} given as parameters to the constructor are
 * part of a DiffElement.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DiffOfSwitch extends DiffSwitch<Boolean> {

	/**
	 * Left EObject.
	 */
	private EObject fLeft;

	/**
	 * Right EObject.
	 */
	private EObject fRight;

	/**
	 * Construct a new boolean switch.
	 * 
	 * @param left
	 *            left EObject.
	 * @param right
	 *            right EObject.
	 */
	public DiffOfSwitch(EObject left, EObject right) {
		fLeft = left;
		fRight = right;
	}

	@Override
	public Boolean caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
		return fLeft != null && fLeft.equals(object.getLeftElement());
	}

	@Override
	public Boolean caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
		return fRight != null && fRight.equals(object.getRightElement());
	}

	@Override
	public Boolean caseUpdateAttribute(UpdateAttribute object) {
		return (fLeft != null && fLeft.equals(object.getLeftElement()))
				|| (fRight != null && fRight.equals(object.getRightElement()));
	}

	@Override
	public Boolean caseUpdateReference(UpdateReference object) {
		return (fLeft != null && fLeft.equals(object.getLeftElement()))
				|| (fRight != null && fRight.equals(object.getRightElement()));
	}

	@Override
	public Boolean caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
		return (fLeft != null && fLeft.equals(object.getLeftElement()))
				|| (fRight != null && fRight.equals(object.getRightElement()));
	}

	@Override
	public Boolean caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
		return fLeft != null && fLeft.equals(object.getLeftElement()) || fRight != null
				&& fRight.equals(object.getRightElement());
	}

	@Override
	public Boolean caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
		return (fLeft != null && fLeft.equals(object.getLeftElement()))
				|| (fRight != null && fRight.equals(object.getRightElement()));
	}

	@Override
	public Boolean caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
		return (fLeft != null && fLeft.equals(object.getLeftElement()))
				|| (fRight != null && fRight.equals(object.getRightElement()));
	}

	@Override
	public Boolean caseDiffGroup(DiffGroup object) {
		return fRight != null && fRight.equals(object.getRightParent());
	}

}
