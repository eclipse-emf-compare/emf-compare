package org.eclipse.emf.compare.uml2.tests.profiles.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.ProfilesInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class ProfileInputData extends ProfilesInputData {

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

	@Override
	protected Resource loadFromClassloader(String string) throws IOException {
		final Resource resource = super.loadFromClassloader(string);
		// FIXME: UML resolves links to profiles itself. Code below is temporary required in order that the
		// comparison also be able to resolve diff values to these profiles.
		EcoreUtil.resolveAll(resource.getResourceSet());
		return resource;
	}

}
