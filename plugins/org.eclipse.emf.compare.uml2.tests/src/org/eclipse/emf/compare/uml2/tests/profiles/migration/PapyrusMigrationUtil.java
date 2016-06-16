/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.profiles.migration;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * This utility provides information about the Papyrus plugin that provides the profile migration. This is
 * important as the profile migration in Eclipse Luna works a bit different than in Mars and upper.
 * Specifically, the IDs of the migrated elements are not kept correctly which in the comparison yields a lot
 * of additions and deletions of elements.
 * 
 * @author Martin Fleck
 */
public class PapyrusMigrationUtil {
	/**
	 * Bundle responsible for the Papyrus profile migration. This may be null if the bundle can not be found.
	 */
	public static Bundle PROFILE_MIGRATION_BUNDLE = Platform.getBundle("org.eclipse.papyrus.uml.modelrepair"); //$NON-NLS-1$

	/**
	 * Latest version number of the profile migration bundle for Eclipse Luna without the qualifier.
	 */
	public static final String LUNA_VERSION = "1.0.2"; //$NON-NLS-1$

	/**
	 * Returns true if the profile migration bundle is not null and the bundle version
	 * (major.minor.micro.qualifier) starts with the given version strings.
	 * 
	 * @param version
	 *            expected version of the profile migration bundle
	 * @return true if the bundle version starts with the given version, false if the version is not a match
	 *         or the bundle could not be found.
	 */
	public static boolean isVersion(String version) {
		return PROFILE_MIGRATION_BUNDLE != null
				&& PROFILE_MIGRATION_BUNDLE.getVersion().toString().startsWith(version);
	}

	/**
	 * Returns true if the profile migration bundle is not null and we loaded the bundle version from Eclipse
	 * Luna.
	 */
	public static boolean isLuna() {
		return isVersion(LUNA_VERSION);
	}
}
