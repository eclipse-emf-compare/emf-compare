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
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffFactory;
import org.eclipse.emf.compare.sysml.utils.Utils;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeUpdateReferenceFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for type SysMLStereotypeUpdateReferenceFactory.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 1.3
 */
public class SysMLStereotypeUpdateReferenceFactory extends UMLStereotypeUpdateReferenceFactory {
	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public SysMLStereotypeUpdateReferenceFactory(UML2DiffEngine engine) {
		super(engine);
	}

	@Override
	public boolean handles(DiffElement input) {
		if (input instanceof UpdateReference) {
			final boolean isSysML = Utils.belongToSysMLModel(((UpdateReference)input).getLeftElement());
			return super.handles(input) && isSysML;
		}
		return false;
	}

	@Override
	public AbstractDiffExtension create(DiffElement input, CrossReferencer crossReferencer) {
		final UpdateReference updateReference = (UpdateReference)input;
		final EObject leftElement = updateReference.getLeftElement();
		final EObject rightElement = updateReference.getRightElement();
		final EObject leftBase = UMLUtil.getBaseElement(leftElement);
		final EObject rightBase = UMLUtil.getBaseElement(rightElement);

		final SysMLStereotypeUpdateReference ret = SysMLdiffFactory.eINSTANCE
				.createSysMLStereotypeUpdateReference();

		ret.setStereotype(UMLUtil.getStereotype(rightElement));
		ret.setRemote(input.isRemote());

		ret.setReference(updateReference.getReference());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		// ret.setRightTarget(updateReference.getRightTarget());
		// ret.setLeftTarget(updateReference.getLeftTarget());

		setTargets(updateReference, ret);
		ret.getHideElements().add(input);
		ret.getRequires().add(input);
		/**
		 * To be sure to hide the corresponding UML element and not others
		 */
		final UMLPredicate<Setting> pre = new UMLPredicate<EStructuralFeature.Setting>() {

			private boolean isEquals(Object origin, Object target) {
				if (origin != null) {
					return origin.equals(target);
				} else {
					return target == null;
				}
			}

			public boolean apply(Setting input) {
				final EObject eObject = input.getEObject();
				if (eObject instanceof UMLStereotypeUpdateReference) {
					final UMLStereotypeUpdateReference diff = (UMLStereotypeUpdateReference)eObject;
					final boolean sameTarget = isEquals(diff.getLeftTarget(), ret.getLeftTarget())
							&& isEquals(diff.getRightTarget(), ret.getRightTarget());
					final boolean sameElements = isEquals(diff.getLeftElement(), ret.getLeftElement())
							&& isEquals(diff.getRightElement(), ret.getRightElement())
							&& isEquals(diff.getStereotype(), ret.getStereotype());
					return sameElements && sameTarget;

				}
				return false;
			}
		};
		/**
		 * Hide the UML contribution
		 */
		hideCrossReferences(leftBase, DiffPackage.Literals.REFERENCE_CHANGE__LEFT_ELEMENT, ret, pre,
				crossReferencer);
		hideCrossReferences(rightBase, DiffPackage.Literals.REFERENCE_CHANGE__RIGHT_ELEMENT, ret, pre,
				crossReferencer);

		return ret;
	}

}
