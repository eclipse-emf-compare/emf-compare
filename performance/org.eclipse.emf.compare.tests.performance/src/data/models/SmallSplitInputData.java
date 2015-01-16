/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package data.models;

import java.io.IOException;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class SmallSplitInputData extends Data {
	/**
	 * @return 
	 * @throws IOException 
	 * 
	 */
	public ResourceSet loadLeft() {
		ResourceSet resourceSet = createResourceSet();
		Resource ret = loadFromClassLoader("model_size_small_split/model_size_small_original_model/model.uml", resourceSet);
		EcoreUtil.resolveAll(resourceSet);
		return resourceSet;
	}
	public ResourceSet loadRight() {
		ResourceSet resourceSet = createResourceSet();
		Resource ret = loadFromClassLoader("model_size_small_split/model_size_small_modified_model/model.uml", resourceSet);
		EcoreUtil.resolveAll(resourceSet);		
		return resourceSet;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see data.models.TestMatchUML.Data#loadAncestor()
	 */
	@Override
	public Notifier loadAncestor() {
		return null;
	}
}