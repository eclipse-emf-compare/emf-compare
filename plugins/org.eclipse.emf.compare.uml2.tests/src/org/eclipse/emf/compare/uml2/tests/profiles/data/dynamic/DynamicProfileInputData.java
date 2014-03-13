package org.eclipse.emf.compare.uml2.tests.profiles.data.dynamic;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DynamicProfileInputData extends AbstractUMLInputData {

	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml");
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml");
	}

	public Resource getA2Left() throws IOException {
		return loadFromClassLoader("a2/left.uml");
	}

	public Resource getA2Right() throws IOException {
		return loadFromClassLoader("a2/right.uml");
	}
}
