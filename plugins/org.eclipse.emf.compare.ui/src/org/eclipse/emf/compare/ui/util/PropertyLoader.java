package org.eclipse.emf.compare.ui.util;

import org.eclipse.osgi.util.NLS;

/**
 * Allows retrieval of properties defined in plugin.properties.
 * 
 * @author Laurent Goubet <laurent.goubet@obeo.fr>
 */
public class PropertyLoader extends NLS {
	/** full path of the properties file. */
	public static final String BUNDLE_NAME = "plugin"; //$NON-NLS-1$
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, PropertyLoader.class);
	}
	
	/** key for the file extension of the save delta wizard. */
	public static String UI_SaveDeltaWizard_FileExtension;
	/** key for the warning message of the save delta wizard. */
	public static String WARN_FilenameExtension;
}
