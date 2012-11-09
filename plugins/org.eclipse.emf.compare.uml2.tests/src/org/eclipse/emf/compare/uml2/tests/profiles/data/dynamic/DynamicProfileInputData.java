package org.eclipse.emf.compare.uml2.tests.profiles.data.dynamic;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.ProfilesInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DynamicProfileInputData extends ProfilesInputData {

	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml");
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml");
	}
}
