/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class DiagramInputData extends AbstractInputData {

	/** Store the set of the resource sets of the input data. */
	private Set<ResourceSet> sets = new LinkedHashSet<ResourceSet>();

	public DiagramInputData() {
	}

	public Set<ResourceSet> getSets() {
		return sets;
	}

}
