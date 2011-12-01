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
package org.eclipse.emf.compare.sysml.sysmldiff.provider;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.diff.provider.DiffEditPlugin;
import org.eclipse.emf.compare.match.metamodel.provider.MatchEditPlugin;
import org.eclipse.emf.compare.uml2diff.provider.UML2DiffEditPlugin;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.papyrus.sysml.provider.SysmlEditPlugin;
import org.eclipse.papyrus.uml.standard.provider.standardEditPlugin;
import org.eclipse.uml2.uml.edit.UMLEditPlugin;

/**
 * This is the central singleton for the SysMLdiff edit plugin. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public final class SysMLdiffEditPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final SysMLdiffEditPlugin INSTANCE = new SysMLdiffEditPlugin();

	/**
	 * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * Create the instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLdiffEditPlugin() {
		super(new ResourceLocator[] {DiffEditPlugin.INSTANCE, EcoreEditPlugin.INSTANCE,
				MatchEditPlugin.INSTANCE, standardEditPlugin.INSTANCE, SysmlEditPlugin.INSTANCE,
				UMLEditPlugin.INSTANCE, UML2DiffEditPlugin.INSTANCE,});
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static class Implementation extends EclipsePlugin {
		/**
		 * Creates an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}
	}

}
