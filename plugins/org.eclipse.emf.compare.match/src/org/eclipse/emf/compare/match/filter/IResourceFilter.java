/*******************************************************************************
 * Copyright (c) 2009, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.filter;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * This interface describes the contract for resource filters that can be provided through the extension point
 * org.eclipse.emf.compare.match.resourcefilters. <b>Note</b> that this filter will only be called when
 * comparing models through the EMF Compare API or the
 * {@link org.eclipse.emf.compare.match.service.MatchService MatchService} doResourceSetMatch() methods.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IResourceFilter {
	/**
	 * This method will be called by the framework with the list of all resources present in the compared
	 * resourceSets for two way comparison. Clients should remove the undesired resources from the parameter
	 * lists.
	 * 
	 * @param leftResources
	 *            Resources contained by the {@link org.eclipse.emf.ecore.resource.ResourceSet} of the left
	 *            resource.
	 * @param rightResources
	 *            Resources contained by the {@link org.eclipse.emf.ecore.resource.ResourceSet} of the right
	 *            resource.
	 */
	void filter(List<Resource> leftResources, List<Resource> rightResources);

	/**
	 * This method will be called by the framework with the list of all resources present in the compared
	 * resourceSets for three way comparison. Clients should remove the undesired resources from the parameter
	 * lists.
	 * 
	 * @param leftResources
	 *            Resources contained by the {@link org.eclipse.emf.ecore.resource.ResourceSet} of the left
	 *            resource.
	 * @param rightResources
	 *            Resources contained by the {@link org.eclipse.emf.ecore.resource.ResourceSet} of the right
	 *            resource.
	 * @param ancestorResources
	 *            Resources contained by the {@link org.eclipse.emf.ecore.resource.ResourceSet} of the origin
	 *            resource.
	 */
	void filter(List<Resource> leftResources, List<Resource> rightResources, List<Resource> ancestorResources);
}
