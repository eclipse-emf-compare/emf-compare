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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import java.util.Set;

/**
 * Event indicating that model resources have been removed, which requires updating the graph of dependencies
 * between model resources.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ResourceRemovedEvent<T> {

	private final Set<T> elements;

	public ResourceRemovedEvent(Set<T> elements) {
		this.elements = elements;
	}

	public Set<T> getElements() {
		return elements;
	}
}
