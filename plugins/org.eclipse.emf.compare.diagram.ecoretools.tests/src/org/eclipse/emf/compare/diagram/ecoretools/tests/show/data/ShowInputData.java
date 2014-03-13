package org.eclipse.emf.compare.diagram.ecoretools.tests.show.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class ShowInputData extends AbstractInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/TC01.ecorediag"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/TC02.ecorediag"); //$NON-NLS-1$
	}

}
