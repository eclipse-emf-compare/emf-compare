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
package org.eclipse.emf.compare.ide.ui.tests.git.framework;

/**
 * The different merge strategies. These strategies are provided by EGit or EMFCompare.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public enum GitMergeStrategyID {

	JGIT_DEFAULT("jgit-default-mergeStrategy"), //$NON-NLS-1$

	MODEL_RECURSIVE("model recursive"), //$NON-NLS-1$

	MODEL_ADDITIVE("model additive merge"); //$NON-NLS-1$

	private String value;

	private GitMergeStrategyID(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
