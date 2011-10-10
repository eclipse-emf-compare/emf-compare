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

/**
 * Factory for UMLAssociationBranchChangeRightTarget.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLAssociationBranchChangeRightTargetFactory extends AbstractDiffExtensionFactory {
	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLAssociationBranchChangeRightTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof Property
				&& ((ModelElementChangeRightTarget)input).getRightElement().eContainer() instanceof Association
				&& ((Association)((Property)((ModelElementChangeRightTarget)input).getRightElement())
						.eContainer()).getMemberEnds().size() > 2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		final ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;

		final UMLAssociationBranchChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLAssociationBranchChangeRightTarget();

		ret.getHideElements().add(changeRightTarget);
		ret.getRequires().add(changeRightTarget);

		ret.setRemote(input.isRemote());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setLeftParent(changeRightTarget.getLeftParent());

		return ret;
	}

}
