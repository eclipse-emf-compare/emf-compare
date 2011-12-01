/**
 *  Copyright (c) 2011 Atos.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffFactory;
import org.eclipse.emf.compare.sysml.utils.Utils;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeReferenceOrderChangeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for SysMLStereotypeReferenceOrderChangeFactory.
 *
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 1.3
 */
public class SysMLStereotypeReferenceOrderChangeFactory extends UMLStereotypeReferenceOrderChangeFactory {
	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public SysMLStereotypeReferenceOrderChangeFactory(UML2DiffEngine engine) {
		super(engine);
	}

	@Override
	public boolean handles(DiffElement input) {
		if (input instanceof ReferenceOrderChange) {
			final boolean isSysML = Utils.belongToSysMLModel(((ReferenceOrderChange)input).getLeftElement());
			return super.handles(input) && isSysML;
		}
		return false;
	}

	@Override
	public AbstractDiffExtension create(DiffElement input, CrossReferencer crossReferencer) {
		final ReferenceOrderChange updateReference = (ReferenceOrderChange)input;
		final EObject leftElement = updateReference.getLeftElement();
		final EObject rightElement = updateReference.getRightElement();
		final EObject leftBase = UMLUtil.getBaseElement(leftElement);
		final EObject rightBase = UMLUtil.getBaseElement(rightElement);

		final SysMLStereotypeReferenceOrderChange ret = SysMLdiffFactory.eINSTANCE
				.createSysMLStereotypeReferenceOrderChange();

		/**
		 * Hide the UML contribution
		 */
		hideCrossReferences(leftBase, DiffPackage.Literals.REFERENCE_CHANGE__LEFT_ELEMENT, ret,
				crossReferencer);
		hideCrossReferences(rightBase, DiffPackage.Literals.REFERENCE_CHANGE__RIGHT_ELEMENT, ret,
				crossReferencer);

		ret.setStereotype(UMLUtil.getStereotype(rightElement));
		ret.setRemote(input.isRemote());

		ret.setReference(updateReference.getReference());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		ret.getRightTarget().addAll(updateReference.getRightTarget());
		ret.getLeftTarget().addAll(updateReference.getLeftTarget());

		ret.getHideElements().add(input);
		ret.getRequires().add(input);

		return ret;
	}

}
