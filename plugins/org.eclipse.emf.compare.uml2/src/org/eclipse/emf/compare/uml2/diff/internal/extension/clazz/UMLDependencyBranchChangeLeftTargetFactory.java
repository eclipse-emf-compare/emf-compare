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
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Dependency;

public class UMLDependencyBranchChangeLeftTargetFactory extends AbstractDiffExtensionFactory {

	public UMLDependencyBranchChangeLeftTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ReferenceChangeLeftTarget
				&& ((ReferenceChangeLeftTarget)input).getLeftElement() instanceof Dependency
				&& ((Dependency)((ReferenceChangeLeftTarget)input).getLeftElement()).getSuppliers().size() > 1;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ReferenceChangeLeftTarget changeLeftTarget = (ReferenceChangeLeftTarget)input;

		UMLDependencyBranchChangeLeftTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLDependencyBranchChangeLeftTarget();

		ret.getHideElements().add(changeLeftTarget);
		ret.getRequires().add(changeLeftTarget);

		ret.setRemote(input.isRemote());
		ret.setReference(changeLeftTarget.getReference());
		ret.setLeftElement(changeLeftTarget.getLeftElement());
		ret.setLeftTarget(changeLeftTarget.getLeftTarget());
		ret.setRightElement(changeLeftTarget.getRightElement());
		ret.setRightTarget(changeLeftTarget.getRightTarget());

		return ret;
	}

}
