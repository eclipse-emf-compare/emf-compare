/*******************************************************************************
 * Copyright (c) 2019 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * Specific comparison input that can be used to directly provide a pre-computed Comparison to the EMF Compare
 * UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IComparisonProvider {
	IComparisonScope getComparisonScope(IProgressMonitor monitor);

	Comparison getComparison(IProgressMonitor monitor);
}
