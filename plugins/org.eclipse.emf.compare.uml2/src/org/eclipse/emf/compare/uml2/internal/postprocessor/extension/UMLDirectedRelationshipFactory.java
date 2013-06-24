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
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for DirectedRelationship changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLDirectedRelationshipFactory extends AbstractUMLChangeFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return DirectedRelationshipChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createDirectedRelationshipChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminant(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EObject getDiscriminant(Diff input) {
		return Iterables.find(getDiscriminants(input), instanceOf(DirectedRelationship.class), null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminantsGetter()
	 */
	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new DiscriminantsGetter() {

			@Override
			public Set<EObject> caseDirectedRelationship(DirectedRelationship object) {
				Set<EObject> result = new HashSet<EObject>();
				result.add(object);
				return result;
			}

		};
	}

	/**
	 * It returns the concrete types of dependencies to manage.
	 * 
	 * @return The list of classes to manage.
	 */
	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();

		result.add(UMLPackage.Literals.DEPENDENCY);
		result.add(UMLPackage.Literals.ABSTRACTION);
		result.add(UMLPackage.Literals.USAGE);
		result.add(UMLPackage.Literals.REALIZATION);

		result.add(UMLPackage.Literals.INTERFACE_REALIZATION);

		result.add(UMLPackage.Literals.SUBSTITUTION);

		result.add(UMLPackage.Literals.DIRECTED_RELATIONSHIP);
		result.add(UMLPackage.Literals.GENERALIZATION);
		result.add(UMLPackage.Literals.INCLUDE);
		result.add(UMLPackage.Literals.ELEMENT_IMPORT);
		result.add(UMLPackage.Literals.INFORMATION_FLOW);
		result.add(UMLPackage.Literals.PACKAGE_IMPORT);
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return super.isRelatedToAnExtensionAdd(input)
				&& getManagedConcreteDiscriminantKind().contains(input.getValue().eClass());
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return super.isRelatedToAnExtensionDelete(input)
				&& getManagedConcreteDiscriminantKind().contains(input.getValue().eClass());
	}

}
