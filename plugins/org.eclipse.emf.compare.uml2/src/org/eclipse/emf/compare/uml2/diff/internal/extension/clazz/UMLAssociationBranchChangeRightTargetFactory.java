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
package org.eclipse.emf.compare.uml2.diff.internal.extension.clazz;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

public class UMLAssociationBranchChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	public UMLAssociationBranchChangeRightTargetFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof Property
				&& ((ModelElementChangeRightTarget)input).getRightElement().eContainer() instanceof Association
				&& ((Association)((Property)((ModelElementChangeRightTarget)input).getRightElement())
						.eContainer()).getMemberEnds().size() > 2;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;

		UMLAssociationBranchChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLAssociationBranchChangeRightTarget();

		ret.getHideElements().add(changeRightTarget);

		ret.setRemote(input.isRemote());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setLeftParent(changeRightTarget.getLeftParent());

		return ret;
	}

}
