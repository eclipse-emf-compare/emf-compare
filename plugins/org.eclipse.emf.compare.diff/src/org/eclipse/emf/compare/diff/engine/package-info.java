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
 * Provides a generic diff engine.
 * <p>
 * The provided engine does not require Eclipse to be running and can be 
 * called stand-alone.
 * </p>
 * <p>
 * Clients can extends this diff engine and inherit most of its behavior
 * instead of redefining an IDiffEngine from scratch.
 * </p>
 */
package org.eclipse.emf.compare.diff.engine;