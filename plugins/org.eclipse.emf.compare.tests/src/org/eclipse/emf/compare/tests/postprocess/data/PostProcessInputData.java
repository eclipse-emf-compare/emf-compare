package org.eclipse.emf.compare.tests.postprocess.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class PostProcessInputData extends AbstractInputData {
	public Resource getLeft() throws IOException {
		return loadFromClassloader("left.nodes"); //$NON-NLS-1$
	}

	public Resource getRight() throws IOException {
		return loadFromClassloader("right.nodes"); //$NON-NLS-1$
	}
}
