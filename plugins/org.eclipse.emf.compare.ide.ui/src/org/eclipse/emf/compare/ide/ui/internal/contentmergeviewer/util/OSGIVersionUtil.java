/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import org.osgi.framework.Version;

/**
 * Avoid using org.osgi.framework.Version.compareTo(Version) method to avoid binary incompatibility with OSGi
 * frameworks before they were compiled with Java 5.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class OSGIVersionUtil {

	/**
	 * Same contract as {@link Version#compareTo(Version)}
	 * 
	 * @param that
	 *            The {@code Version} object to be compared.
	 * @param other
	 *            The {@code Version} object to be compared.
	 * @return A negative integer, zero, or a positive integer if {@code that} version is less than, equal to,
	 *         or greater than the specified {@code other} version.
	 */
	public static int compare(Version that, Version other) {
		if (other == that) {
			return 0;
		} else if (that.getMajor() != other.getMajor()) {
			return that.getMajor() - other.getMajor();
		} else if (that.getMinor() != other.getMinor()) {
			return that.getMinor() - other.getMinor();
		} else if (that.getMicro() != other.getMicro()) {
			return that.getMicro() - other.getMicro();
		} else {
			return that.getQualifier().compareTo(other.getQualifier());
		}
	}
}
