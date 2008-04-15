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
 * Provides the graphical components of the 
 * {@link org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer}.
 * <p>
 * The content merge viewer is split in three parts, all of which are 
 * {@link ModelContentMergeTabFolder}s. Two tabs are provided by default, one of
 * which displaying the model, the other displaying the selected objects' properties.
 * </p>
 * <p>
 * All the tabs are expected to implement {@link IModelContentMergeViewerTab}.
 * </p>
 */
package org.eclipse.emf.compare.ui.viewer.content.part;