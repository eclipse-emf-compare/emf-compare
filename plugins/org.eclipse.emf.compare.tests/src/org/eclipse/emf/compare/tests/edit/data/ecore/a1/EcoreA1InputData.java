package org.eclipse.emf.compare.tests.edit.data.ecore.a1;

import java.io.IOException;

import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class EcoreA1InputData extends AbstractInputData implements ResourceScopeProvider {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("extlibraryLeft.ecore");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("extlibraryRight.ecore");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("extlibraryOrigin.ecore");
	}
}
