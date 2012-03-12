/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.common.util;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * A helper class for loading a particular extension.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ExtensionLoader {

	/**
	 * Ask the extension registry for all extensions with the id <code>extensionId</code>.<br>
	 * 
	 * @throws NullPointerException
	 *             If no extension is found.
	 * @throws ClassCastException
	 *             If the generic type does not match the type of the actual extensions.
	 */ 
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> getAllExtensions(String extensionId, String extensionName)
			throws NullPointerException, ClassCastException {
		try {
			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(extensionId);
			Collection<T> result = new ArrayList<T>();
			for (IConfigurationElement element : elements) {
				result.add((T) element.createExecutableExtension(extensionName));
			}
			return result;
		} catch (CoreException ex) {
			// ex.printStackTrace();
			throw new NullPointerException("Could not find a valid extension for: " + extensionId);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(
					"The generic type probably doesn't fit the actual extension! Please check the types.", e);
		}
	}
}
