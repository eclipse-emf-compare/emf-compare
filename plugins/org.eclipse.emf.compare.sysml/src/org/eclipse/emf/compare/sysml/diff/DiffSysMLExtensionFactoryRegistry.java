/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */

package org.eclipse.emf.compare.sysml.diff;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypePropertyChangeLeftElementchangefactory;
import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypePropertyChangeRightElementchangefactory;
import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypeReferenceChangeLeftTargetFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypeReferenceChangeRightTargetFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypeReferenceOrderChangeFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypeUpdateAttributeFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.internal.extension.profile.SysMLStereotypeUpdateReferenceFactory;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Registry for Element change factory.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 1.3
 */
public final class DiffSysMLExtensionFactoryRegistry {

	/**
	 * Constructor.
	 */
	private DiffSysMLExtensionFactoryRegistry() {
	}

	/**
	 * Creates and returns all {@link IDiffExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param engine
	 *            {@link UML2DiffEngine}
	 * @param crossReferencer
	 *            {@link CrossReferencer}
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 * @generated
	 */
	public static Set<IDiffExtensionFactory> createExtensionFactories(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		final Set<IDiffExtensionFactory> dataset = new HashSet<IDiffExtensionFactory>();

		/**
		 * Attribute change from SysML Stereotype
		 */

		dataset.add(new SysMLStereotypePropertyChangeRightElementchangefactory(engine));

		dataset.add(new SysMLStereotypePropertyChangeLeftElementchangefactory(engine));
		/**
		 * Reference change from SysML stereotype
		 */

		dataset.add(new SysMLStereotypeReferenceChangeLeftTargetFactory(engine));

		dataset.add(new SysMLStereotypeReferenceChangeRightTargetFactory(engine));

		/**
		 * Change order reference
		 */

		dataset.add(new SysMLStereotypeReferenceOrderChangeFactory(engine));

		/**
		 * Update references
		 */

		dataset.add(new SysMLStereotypeUpdateReferenceFactory(engine));

		/**
		 * Update Attribute
		 */
		dataset.add(new SysMLStereotypeUpdateAttributeFactory(engine));

		return Collections.unmodifiableSet(dataset);
	}

}
