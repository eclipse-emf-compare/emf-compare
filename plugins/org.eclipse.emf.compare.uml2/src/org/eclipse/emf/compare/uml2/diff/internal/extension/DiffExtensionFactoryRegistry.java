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
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLAssociationBranchChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLAssociationBranchChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLAssociationChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLAssociationChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLDependencyBranchChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLDependencyBranchChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLDependencyChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLDependencyChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLGeneralizationSetChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLGeneralizationSetChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeApplicationAdditionFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeApplicationRemovalFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeAttributeChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeAttributeChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeReferenceChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeReferenceChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeReferenceOrderChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeUpdateAttributeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLStereotypeUpdateReferenceFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLDestructionEventChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLDestructionEventChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLExecutionSpecificationChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLExecutionSpecificationChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLIntervalConstraintChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLIntervalConstraintChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLMessageChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLMessageChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.usecase.UMLExtendChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.usecase.UMLExtendChangeRightTargetFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Registry of all {@link IDiffExtensionFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikaël Barbero</a>
 */
public class DiffExtensionFactoryRegistry {

	/**
	 * Creates and returns all {@link IDiffExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param engine
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 */
	public static Set<IDiffExtensionFactory> createExtensionFactories(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		Set<IDiffExtensionFactory> dataset = new HashSet<IDiffExtensionFactory>();

		/* Class diagram */
		dataset.add(new UMLAssociationChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLAssociationChangeRightTargetFactory(engine, crossReferencer));
		dataset.add(new UMLAssociationBranchChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLAssociationBranchChangeRightTargetFactory(engine, crossReferencer));

		dataset.add(new UMLGeneralizationSetChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLGeneralizationSetChangeRightTargetFactory(engine, crossReferencer));

		dataset.add(new UMLDependencyChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLDependencyChangeRightTargetFactory(engine, crossReferencer));
		dataset.add(new UMLDependencyBranchChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLDependencyBranchChangeRightTargetFactory(engine, crossReferencer));

		/* Use case diagram */
		dataset.add(new UMLExtendChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLExtendChangeRightTargetFactory(engine, crossReferencer));

		/* Sequence diagram */
		dataset.add(new UMLExecutionSpecificationChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLExecutionSpecificationChangeRightTargetFactory(engine, crossReferencer));
		dataset.add(new UMLIntervalConstraintChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLIntervalConstraintChangeRightTargetFactory(engine, crossReferencer));
		dataset.add(new UMLDestructionEventChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLDestructionEventChangeRightTargetFactory(engine, crossReferencer));
		dataset.add(new UMLMessageChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLMessageChangeRightTargetFactory(engine, crossReferencer));

		/* Profile support */
		dataset.add(new UMLStereotypeUpdateAttributeFactory(engine, crossReferencer));
		dataset.add(new UMLStereotypeAttributeChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLStereotypeAttributeChangeRightTargetFactory(engine, crossReferencer));

		dataset.add(new UMLStereotypeUpdateReferenceFactory(engine, crossReferencer));
		dataset.add(new UMLStereotypeReferenceOrderChangeFactory(engine, crossReferencer));
		dataset.add(new UMLStereotypeReferenceChangeLeftTargetFactory(engine, crossReferencer));
		dataset.add(new UMLStereotypeReferenceChangeRightTargetFactory(engine, crossReferencer));

		dataset.add(new UMLStereotypeApplicationAdditionFactory(engine, crossReferencer));
		dataset.add(new UMLStereotypeApplicationRemovalFactory(engine, crossReferencer));

		return Collections.unmodifiableSet(dataset);
	}

}
