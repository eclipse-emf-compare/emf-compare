package org.eclipse.emf.compare.uml2.tests.message.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class MessageInputData extends AbstractUMLInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml"); //$NON-NLS-1$
	}

	public Resource getA2Left() throws IOException {
		return loadFromClassLoader("a2/left.uml"); //$NON-NLS-1$
	}

	public Resource getA2Right() throws IOException {
		return loadFromClassLoader("a2/right.uml"); //$NON-NLS-1$
	}

	public Resource getA3Left() throws IOException {
		return loadFromClassLoader("a3/left.uml"); //$NON-NLS-1$
	}

	public Resource getA3Right() throws IOException {
		return loadFromClassLoader("a3/right.uml"); //$NON-NLS-1$
	}

	public Resource getA4Left() throws IOException {
		return loadFromClassLoader("a4/left.uml"); //$NON-NLS-1$
	}

	public Resource getA4Right() throws IOException {
		return loadFromClassLoader("a4/right.uml"); //$NON-NLS-1$
	}

}
