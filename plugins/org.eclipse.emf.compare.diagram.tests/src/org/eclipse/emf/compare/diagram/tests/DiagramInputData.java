package org.eclipse.emf.compare.diagram.tests;

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
