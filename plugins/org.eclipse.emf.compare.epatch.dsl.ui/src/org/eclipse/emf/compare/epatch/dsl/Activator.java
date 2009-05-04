/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.emf.compare.epatch.dsl.internal.EpatchActivator;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
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
