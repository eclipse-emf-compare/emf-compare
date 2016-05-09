/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide;

import org.eclipse.team.core.mapping.IResourceMappingMerger;

/**
 * The use case for this feature is really specific : it is used when a user use git to create branch
 * representing feature releases. Everything not related to a particular feature will be stripped-out of the
 * git repository. These features are used to represent a specific feature in a lightweight model.
 * 
 * <pre>
 * Many architectural features can be created and the AdditiveResourceMappingMerger provide support for the merge of such architectural features.
 * </pre>
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.3
 */
public interface IAdditiveResourceMappingMerger extends IResourceMappingMerger {

}
