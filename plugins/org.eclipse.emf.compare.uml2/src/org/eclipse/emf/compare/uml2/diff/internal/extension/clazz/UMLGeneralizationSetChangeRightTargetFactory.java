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
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLGeneralizationSetChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	private final UMLPredicate<Setting> hiddingPredicate = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(EStructuralFeature.Setting input) {
			return ((ReferenceChange)input.getEObject()).getReference() == UMLPackage.Literals.GENERALIZATION__GENERALIZATION_SET;
		}
	};

	public UMLGeneralizationSetChangeRightTargetFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof GeneralizationSet;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;
		final GeneralizationSet generalizationSet = (GeneralizationSet)changeRightTarget.getRightElement();

		UMLGeneralizationSetChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLGeneralizationSetChangeRightTarget();

		for (Generalization generalization : generalizationSet.getGeneralizations()) {
			hideCrossReferences(generalization, DiffPackage.Literals.REFERENCE_CHANGE__RIGHT_ELEMENT, ret,
					hiddingPredicate);
		}

		ret.getHideElements().add(changeRightTarget);

		ret.setRemote(changeRightTarget.isRemote());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setLeftParent(changeRightTarget.getLeftParent());

		return ret;
	}
}
