package org.eclipse.emf.compare.uml2.tests.nonreg.bug484576_pseudoconflicts.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class NonReg484576Data extends AbstractUMLInputData {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.uml"); //$NON-NLS-1$
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.uml"); //$NON-NLS-1$
	}

	public Resource getAncestor() throws IOException {
		return loadFromClassLoader("ancestor.uml"); //$NON-NLS-1$
	}
}
