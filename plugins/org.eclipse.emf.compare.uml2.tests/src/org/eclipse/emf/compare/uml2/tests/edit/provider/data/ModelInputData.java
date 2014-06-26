/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.edit.provider.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ModelInputData extends AbstractUMLInputData {

	public Resource getStaticProfileModel() throws IOException {
		return loadFromClassLoader("staticProfileModel.uml"); //$NON-NLS-1$
	}

	public Resource getDynamicProfileModelmodel() throws IOException {
		return loadFromClassLoader("dynamicProfileModel.uml"); //$NON-NLS-1$
	}

}
