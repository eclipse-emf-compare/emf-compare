/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.clazz.UMLAssociationChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.clazz.UMLGeneralizationSetChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.profile.UMLProfileApplicationChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.sequence.UMLExecutionSpecificationChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.sequence.UMLIntervalConstraintChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.sequence.UMLMessageChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype.UMLDanglingStereotypeApplicationFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype.UMLStereotypeApplicationChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype.UMLStereotypeAttributeChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype.UMLStereotypeReferenceChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.usecase.UMLExtendChangeFactory;

/**
 * Registry of all {@link IDiffExtensionFactory} for UML.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class UMLExtensionFactoryRegistry {

	/**
	 * Private Constructor to prevent direct instantiation.
	 */
	private UMLExtensionFactoryRegistry() {
		// Nothing to do
	}

	/**
	 * Creates and returns all {@link IDiffExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 */
	public static Map<Class<? extends Diff>, IChangeFactory> createExtensionFactories() {
		final Map<Class<? extends Diff>, IChangeFactory> dataset = new LinkedHashMap<Class<? extends Diff>, IChangeFactory>();

		List<IChangeFactory> factories = new ArrayList<IChangeFactory>();
		factories.add(new UMLAssociationChangeFactory());
		factories.add(new UMLDirectedRelationshipFactory());
		factories.add(new UMLGeneralizationSetChangeFactory());
		factories.add(new UMLExtendChangeFactory());
		factories.add(new UMLMessageChangeFactory());
		factories.add(new UMLExecutionSpecificationChangeFactory());
		factories.add(new UMLIntervalConstraintChangeFactory());
		factories.add(new UMLProfileApplicationChangeFactory());
		factories.add(new UMLStereotypeAttributeChangeFactory());
		factories.add(new UMLStereotypeReferenceChangeFactory());
		factories.add(new UMLStereotypeApplicationChangeFactory());
		factories.add(new UMLOpaqueElementBodyChangeFactory());
		factories.add(new UMLDanglingStereotypeApplicationFactory());
		factories.add(new MultiplicityElementChangeFactory());

		for (IChangeFactory iDiffExtensionFactory : factories) {
			dataset.put(iDiffExtensionFactory.getExtensionKind(), iDiffExtensionFactory);
		}

		return Collections.unmodifiableMap(dataset);
	}

}
