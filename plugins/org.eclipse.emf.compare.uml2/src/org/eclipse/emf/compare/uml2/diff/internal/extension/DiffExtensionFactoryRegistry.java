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
	public static Set<IDiffExtensionFactory> createExtensionFactories(UML2DiffEngine engine) {
		Set<IDiffExtensionFactory> dataset = new HashSet<IDiffExtensionFactory>();
		dataset.add(new UMLAssociationChangeLeftTargetFactory(engine));
		dataset.add(new UMLStereotypeUpdateAttributeFactory(engine));
		dataset.add(new UMLStereotypeApplicationRemovalFactory(engine));
		dataset.add(new UMLStereotypeApplicationAdditionFactory(engine));
		return Collections.unmodifiableSet(dataset);
	}

}
