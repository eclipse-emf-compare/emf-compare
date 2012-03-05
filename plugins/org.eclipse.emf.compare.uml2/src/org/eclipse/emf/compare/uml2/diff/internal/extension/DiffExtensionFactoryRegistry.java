/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
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
import org.eclipse.emf.compare.uml2.diff.internal.extension.element.UMLElementChangeLeftTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.element.UMLElementChangeRightTargetFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLProfileApplicationAdditionFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLProfileApplicationRemovalFactory;
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
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference;

/**
 * Registry of all {@link IDiffExtensionFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class DiffExtensionFactoryRegistry {

	/**
	 * Constructor.
	 */
	private DiffExtensionFactoryRegistry() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates and returns all {@link IDiffExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 */
	public static Map<Class<? extends AbstractDiffExtension>, IDiffExtensionFactory> createExtensionFactories(
			UML2DiffEngine engine) {
		final Map<Class<? extends AbstractDiffExtension>, IDiffExtensionFactory> dataset = new HashMap<Class<? extends AbstractDiffExtension>, IDiffExtensionFactory>();

		/* Class diagram */
		dataset.put(UMLAssociationChangeLeftTarget.class, new UMLAssociationChangeLeftTargetFactory(engine));
		dataset.put(UMLAssociationChangeRightTarget.class, new UMLAssociationChangeRightTargetFactory(engine));
		dataset.put(UMLAssociationBranchChangeLeftTarget.class,
				new UMLAssociationBranchChangeLeftTargetFactory(engine));
		dataset.put(UMLAssociationBranchChangeRightTarget.class,
				new UMLAssociationBranchChangeRightTargetFactory(engine));

		dataset.put(UMLGeneralizationSetChangeLeftTarget.class,
				new UMLGeneralizationSetChangeLeftTargetFactory(engine));
		dataset.put(UMLGeneralizationSetChangeRightTarget.class,
				new UMLGeneralizationSetChangeRightTargetFactory(engine));

		dataset.put(UMLDependencyChangeLeftTarget.class, new UMLDependencyChangeLeftTargetFactory(engine));
		dataset.put(UMLDependencyChangeRightTarget.class, new UMLDependencyChangeRightTargetFactory(engine));
		dataset.put(UMLDependencyBranchChangeLeftTarget.class,
				new UMLDependencyBranchChangeLeftTargetFactory(engine));
		dataset.put(UMLDependencyBranchChangeRightTarget.class,
				new UMLDependencyBranchChangeRightTargetFactory(engine));

		/* Use case diagram */
		dataset.put(UMLExtendChangeLeftTarget.class, new UMLExtendChangeLeftTargetFactory(engine));
		dataset.put(UMLExtendChangeRightTarget.class, new UMLExtendChangeRightTargetFactory(engine));

		/* Sequence diagram */
		dataset.put(UMLExecutionSpecificationChangeLeftTarget.class,
				new UMLExecutionSpecificationChangeLeftTargetFactory(engine));
		dataset.put(UMLExecutionSpecificationChangeRightTarget.class,
				new UMLExecutionSpecificationChangeRightTargetFactory(engine));
		dataset.put(UMLIntervalConstraintChangeLeftTarget.class,
				new UMLIntervalConstraintChangeLeftTargetFactory(engine));
		dataset.put(UMLIntervalConstraintChangeRightTarget.class,
				new UMLIntervalConstraintChangeRightTargetFactory(engine));
		dataset.put(UMLDestructionEventChangeLeftTarget.class,
				new UMLDestructionEventChangeLeftTargetFactory(engine));
		dataset.put(UMLDestructionEventChangeRightTarget.class,
				new UMLDestructionEventChangeRightTargetFactory(engine));
		dataset.put(UMLMessageChangeLeftTarget.class, new UMLMessageChangeLeftTargetFactory(engine));
		dataset.put(UMLMessageChangeRightTarget.class, new UMLMessageChangeRightTargetFactory(engine));

		/* Profile support */
		dataset.put(UMLStereotypeUpdateAttribute.class, new UMLStereotypeUpdateAttributeFactory(engine));
		dataset.put(UMLStereotypeAttributeChangeLeftTarget.class,
				new UMLStereotypeAttributeChangeLeftTargetFactory(engine));
		dataset.put(UMLStereotypeAttributeChangeRightTarget.class,
				new UMLStereotypeAttributeChangeRightTargetFactory(engine));

		dataset.put(UMLStereotypeUpdateReference.class, new UMLStereotypeUpdateReferenceFactory(engine));
		dataset.put(UMLStereotypeReferenceOrderChange.class, new UMLStereotypeReferenceOrderChangeFactory(
				engine));
		dataset.put(UMLStereotypeReferenceChangeLeftTarget.class,
				new UMLStereotypeReferenceChangeLeftTargetFactory(engine));
		dataset.put(UMLStereotypeReferenceChangeRightTarget.class,
				new UMLStereotypeReferenceChangeRightTargetFactory(engine));

		dataset.put(UMLStereotypeApplicationAddition.class, new UMLStereotypeApplicationAdditionFactory(
				engine));
		dataset.put(UMLStereotypeApplicationRemoval.class, new UMLStereotypeApplicationRemovalFactory(engine));

		dataset.put(UMLProfileApplicationAddition.class, new UMLProfileApplicationAdditionFactory(engine));
		dataset.put(UMLProfileApplicationRemoval.class, new UMLProfileApplicationRemovalFactory(engine));

		/* Management of model element changes with embedded stereotypes. See Bug 351593. */
		dataset.put(UMLElementChangeLeftTarget.class, new UMLElementChangeLeftTargetFactory(engine));
		dataset.put(UMLElementChangeRightTarget.class, new UMLElementChangeRightTargetFactory(engine));

		return Collections.unmodifiableMap(dataset);
	}

}
