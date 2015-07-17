package org.eclipse.emf.compare.diagram.papyrus.tests.comparison.data;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DiagramTooltipProviderInputData extends DiagramInputData {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.notation");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.notation");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("ancestor.notation");
	}

}
