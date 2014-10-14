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
package org.eclipse.emf.compare.uml2.ide.hook;

import org.eclipse.emf.compare.ide.hook.AbstractResourceSetHooks;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.resource.UMLResource;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * Hook that unloads {@link UMLResource} from the resource set on dipose to avoid memory leak.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class UnloadingUMLResourceHook extends AbstractResourceSetHooks {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.hook.AbstractResourceSetHooks#onDispose(java.lang.Iterable)
	 */
	@Override
	public void onDispose(Iterable<Resource> resources) {
		for (Resource resource : Iterables.filter(resources, Predicates.instanceOf(UMLResource.class))) {
			resource.unload();
		}
	}

}
