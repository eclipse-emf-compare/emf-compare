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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLAssociationChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLDependencyChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLGeneralizationSetChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLInterfaceRealizationChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.clazz.UMLSubstitutionChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.profile.UMLProfileApplicationChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLExecutionSpecificationChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLIntervalConstraintChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.sequence.UMLMessageChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.usecase.UMLExtendChangeFactory;
import org.eclipse.emf.compare.uml2.diff.internal.extension.usecase.UMLIncludeChangeFactory;

/**
 * Registry of all {@link IDiffExtensionFactory}.
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
	public static Map<Class<? extends Diff>, IDiffExtensionFactory> createExtensionFactories() {
		final Map<Class<? extends Diff>, IDiffExtensionFactory> dataset = new HashMap<Class<? extends Diff>, IDiffExtensionFactory>();

		List<IDiffExtensionFactory> factories = new ArrayList<IDiffExtensionFactory>();
		factories.add(new UMLAssociationChangeFactory());
		factories.add(new UMLDependencyChangeFactory());
		factories.add(new UMLInterfaceRealizationChangeFactory());
		factories.add(new UMLSubstitutionChangeFactory());
		factories.add(new UMLGeneralizationSetChangeFactory());
		factories.add(new UMLExtendChangeFactory());
		factories.add(new UMLMessageChangeFactory());
		factories.add(new UMLExecutionSpecificationChangeFactory());
		factories.add(new UMLIncludeChangeFactory());
		factories.add(new UMLIntervalConstraintChangeFactory());
		factories.add(new UMLProfileApplicationChangeFactory());

		for (IDiffExtensionFactory iDiffExtensionFactory : factories) {
			dataset.put(iDiffExtensionFactory.getExtensionKind(), iDiffExtensionFactory);
		}

		return Collections.unmodifiableMap(dataset);
	}

}
