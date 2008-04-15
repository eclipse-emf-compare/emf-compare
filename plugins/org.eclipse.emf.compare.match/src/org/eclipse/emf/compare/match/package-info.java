/*
 * (non-javadoc)
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
/**
 * This packages backs up EMF Compare's matching functionality.
 * <p>
 * It provides services to call for the matching process on two or three EObjects,
 * allowing for
 * <ul>
 * <li>Content matching on random EObjects</li>
 * <li>Model matching with model roots</li> 
 * <li>Resource matching with model resources</li>
 * <li>ResourceSet matching on whole resource sets</li>
 * </ul>
 * </p>
 * <p>
 * All the features provided by the match service can be called stand-alone.
 * </p>
 */
package org.eclipse.emf.compare.match;