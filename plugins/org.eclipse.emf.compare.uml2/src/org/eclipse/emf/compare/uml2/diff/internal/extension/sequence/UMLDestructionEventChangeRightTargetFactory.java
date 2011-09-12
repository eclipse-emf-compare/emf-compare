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
package org.eclipse.emf.compare.uml2.diff.internal.extension.sequence;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.DestructionEvent;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLDestructionEventChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	public UMLDestructionEventChangeRightTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof DestructionEvent;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;
		final DestructionEvent destructionEvent = (DestructionEvent)changeRightTarget.getRightElement();

		UMLDestructionEventChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLDestructionEventChangeRightTarget();

		for (EObject occurenceSpecification : getInverseReferences(destructionEvent,
				UMLPackage.Literals.OCCURRENCE_SPECIFICATION__EVENT)) {
			hideCrossReferences(occurenceSpecification,
					DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, ret,
					crossReferencer);
			hideCrossReferences(occurenceSpecification,
					DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, crossReferencer);
		}

		ret.getHideElements().add(changeRightTarget);
		ret.getRequires().add(changeRightTarget);

		ret.setRemote(changeRightTarget.isRemote());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setLeftParent(changeRightTarget.getLeftParent());

		return ret;
	}
}
