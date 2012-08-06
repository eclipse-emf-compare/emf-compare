package org.eclipse.emf.compare.uml2.tests.association.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class AssociationInputData extends AbstractInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassloader("a1/left.uml"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassloader("a1/right.uml"); //$NON-NLS-1$
	}

	public Resource getA2Left() throws IOException {
		return loadFromClassloader("a2/left.uml"); //$NON-NLS-1$
	}

	public Resource getA2Right() throws IOException {
		return loadFromClassloader("a2/right.uml"); //$NON-NLS-1$
	}

	public Resource getA3Left() throws IOException {
		return loadFromClassloader("a3/left.uml"); //$NON-NLS-1$
	}

	public Resource getA3Right() throws IOException {
		return loadFromClassloader("a3/right.uml"); //$NON-NLS-1$
	}

	public Resource getA4Left() throws IOException {
		return loadFromClassloader("a4/left.uml"); //$NON-NLS-1$
	}

	public Resource getA4Right() throws IOException {
		return loadFromClassloader("a4/right.uml"); //$NON-NLS-1$
	}
}
