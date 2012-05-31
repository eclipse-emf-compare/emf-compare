/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
class Strings {

	private Strings() {
	}

	final static String elide(String original, int max, String suffix) {
		if (original.length() > max) {
			String elided = original.substring(0, original.length() - suffix.length());
			return elided + suffix;
		}
		return original;
	}
}
