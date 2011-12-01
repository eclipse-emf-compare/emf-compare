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
package org.eclipse.emf.compare.sysml.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.sysml.util.SysmlResource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Utility Class.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 1.3
 */
public final class Utils {

	/**
	 * Empty constructor.
	 */
	private Utils() {
	}

	/**
	 * Test if the EObject belong the SysML meta model.
	 * 
	 * @param eObject
	 *            Any EObject which belong to the model
	 * @return Return true if the EObject belong to a SysML meta model
	 */
	public static boolean belongToSysMLModel(EObject eObject) {
		boolean result = false;
		Element elem = null;
		if (eObject instanceof Element) {
			elem = (Element)eObject;
		} else {
			elem = UMLUtil.getBaseElement(eObject);
		}
		if (elem == null) {
			return result;
		}
		final Model model = elem.getModel();
		if (model != null) {
			final Profile sysml = model.getAppliedProfile(SysmlResource.SYSML_ID);
			if (sysml != null) {
				result = true;
			}
		}
		return result;
	}
}
