/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler.util.EMFCompareUIHandlerUtil;

/**
 * Handler that manages a merge from right to left of a difference in case of both sides of the comparison are
 * editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergedToLeft extends AbstractMergedTo {

	@Override
	protected void copyDiff(Diff diff) {
		EMFCompareUIHandlerUtil.copyDiff(diff, false, getConfiguration());
	}
}
