package org.eclipse.emf.compare.uml2.tests.extend.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class ExtendInputData extends AbstractInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml"); //$NON-NLS-1$
	}

}