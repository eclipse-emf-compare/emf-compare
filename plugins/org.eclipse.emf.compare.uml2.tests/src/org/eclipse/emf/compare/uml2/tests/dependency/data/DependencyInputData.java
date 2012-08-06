package org.eclipse.emf.compare.uml2.tests.dependency.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DependencyInputData extends AbstractInputData {
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

	public Resource getA5Left() throws IOException {
		return loadFromClassloader("a5/left.uml"); //$NON-NLS-1$
	}

	public Resource getA5Right() throws IOException {
		return loadFromClassloader("a5/right.uml"); //$NON-NLS-1$
	}

	public Resource getA6Left() throws IOException {
		return loadFromClassloader("a6/left.uml"); //$NON-NLS-1$
	}

	public Resource getA6Right() throws IOException {
		return loadFromClassloader("a6/right.uml"); //$NON-NLS-1$
	}

	public Resource getA7Right() throws IOException {
		return loadFromClassloader("a7/right.uml"); //$NON-NLS-1$
	}

	public Resource getA7Left() throws IOException {
		return loadFromClassloader("a7/left.uml"); //$NON-NLS-1$
	}

	public Resource getA8Right() throws IOException {
		return loadFromClassloader("a8/right.uml"); //$NON-NLS-1$
	}

	public Resource getA8Left() throws IOException {
		return loadFromClassloader("a8/left.uml"); //$NON-NLS-1$
	}
}
