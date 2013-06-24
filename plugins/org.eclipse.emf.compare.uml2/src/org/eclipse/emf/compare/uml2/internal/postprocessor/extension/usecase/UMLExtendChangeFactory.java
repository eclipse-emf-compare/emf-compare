/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.usecase;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.uml2.internal.ExtendChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.UMLDirectedRelationshipFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for Extend changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLExtendChangeFactory extends UMLDirectedRelationshipFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.extension.clazz.UMLDependencyChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return ExtendChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.extension.clazz.UMLDependencyChangeFactory#createExtension()
	 */
	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createExtendChange();
	}

	/**
	 * Discriminants getter for the Extend change.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class ExtendDiscriminantsGetter extends DiscriminantsGetter {
		/**
		 * {@inheritDoc}<br>
		 * Discriminants are the extend and extension points.
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseExtend(org.eclipse.uml2.uml.Extend)
		 */
		@Override
		public Set<EObject> caseExtend(Extend object) {
			Set<EObject> result = new HashSet<EObject>();
			result.add(object);
			result.addAll(object.getExtensionLocations());
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseExtensionPoint(org.eclipse.uml2.uml.ExtensionPoint)
		 */
		@Override
		public Set<EObject> caseExtensionPoint(ExtensionPoint object) {
			Set<EObject> result = new HashSet<EObject>();
			final Setting setting = getInverseReferences(object, new Predicate<EStructuralFeature.Setting>() {
				public boolean apply(EStructuralFeature.Setting input) {
					return input.getEStructuralFeature() == UMLPackage.Literals.EXTEND__EXTENSION_LOCATION;
				}
			});
			if (setting != null) {
				EObject extend = setting.getEObject();
				result.addAll(doSwitch(extend));
			}
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminantsGetter()
	 */
	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new ExtendDiscriminantsGetter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.extension.clazz.UMLDependencyChangeFactory#getManagedConcreteDiscriminantKind()
	 */
	@Override
	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.EXTEND);
		return result;
	}

}
