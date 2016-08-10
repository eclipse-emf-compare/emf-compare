/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.provider;

import java.util.Map;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.adapterfactory.context.AbstractContextTester;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.context.PapyrusContextUtil;

/**
 * Indicates whether we are in a Papyrus context.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class PapyrusContextTester extends AbstractContextTester {

	/**
	 * {@inheritDoc}
	 */
	public boolean apply(Map<Object, Object> context) {
		Comparison comparison = getComparison(context);
		if (context != null) {
			return PapyrusContextUtil.isPapyrusContext(comparison);
		}
		return false;
	}

}
