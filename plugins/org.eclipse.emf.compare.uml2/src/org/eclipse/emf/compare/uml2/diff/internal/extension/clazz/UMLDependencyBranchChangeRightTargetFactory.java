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
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Dependency;

public class UMLDependencyBranchChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	public UMLDependencyBranchChangeRightTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ReferenceChangeRightTarget
				&& ((ReferenceChangeRightTarget)input).getRightElement() instanceof Dependency
				&& ((Dependency)((ReferenceChangeRightTarget)input).getRightElement()).getSuppliers().size() > 1;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ReferenceChangeRightTarget changeRightTarget = (ReferenceChangeRightTarget)input;

		UMLDependencyBranchChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLDependencyBranchChangeRightTarget();

		ret.getHideElements().add(changeRightTarget);
		ret.getRequires().add(changeRightTarget);

		ret.setRemote(input.isRemote());
		ret.setReference(changeRightTarget.getReference());
		ret.setLeftElement(changeRightTarget.getLeftElement());
		ret.setLeftTarget(changeRightTarget.getLeftTarget());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setRightTarget(changeRightTarget.getRightTarget());

		return ret;
	}

}
