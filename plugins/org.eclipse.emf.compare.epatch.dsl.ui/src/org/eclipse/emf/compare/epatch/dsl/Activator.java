package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.emf.compare.epatch.dsl.internal.EpatchActivator;
import org.eclipse.jface.resource.ImageDescriptor;

public class Activator extends EpatchActivator {

	public final static String PLUGIN_ID = "org.eclipse.emf.compare.epatch.dsl.ui";

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
