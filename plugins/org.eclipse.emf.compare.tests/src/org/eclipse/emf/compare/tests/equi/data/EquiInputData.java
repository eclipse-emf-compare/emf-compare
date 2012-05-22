package org.eclipse.emf.compare.tests.equi.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class EquiInputData extends AbstractInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassloader("a1/left.nodes"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassloader("a1/right.nodes"); //$NON-NLS-1$
	}
}
