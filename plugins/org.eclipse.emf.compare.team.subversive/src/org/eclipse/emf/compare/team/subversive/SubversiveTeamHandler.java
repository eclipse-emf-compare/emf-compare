/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.team.subversive;

import java.io.IOException;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ui.internal.AbstractTeamHandler;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.team.svn.core.connector.SVNRevision;
import org.eclipse.team.svn.ui.compare.ResourceCompareInput.ResourceElement;

/**
 * This class will handle the specific parts of loading resources for a comparison via subversive.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class SubversiveTeamHandler extends AbstractTeamHandler {
	/** Indicates that the right resource is remote. */
	private boolean rightIsRemote;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.internal.AbstractTeamHandler#isRightRemote()
	 */
	@Override
	public boolean isRightRemote() {
		return rightIsRemote;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.internal.AbstractTeamHandler#loadResources(org.eclipse.compare.structuremergeviewer.ICompareInput)
	 */
	@Override
	public boolean loadResources(ICompareInput input) throws IOException,
			CoreException {
		final ITypedElement left = input.getLeft();
		final ITypedElement right = input.getRight();
		final ITypedElement ancestor = input.getAncestor();

		if (left instanceof ResourceElement && right instanceof ResourceElement) {
			if (((ResourceElement)left).getRepositoryResource().getSelectedRevision() == SVNRevision.WORKING) {
				rightResource = ModelUtils.load(
						((ResourceElement)left).getLocalResource().getResource().getFullPath(), new ResourceSetImpl())
						.eResource();
			} else {
				rightResource = ModelUtils.load(((ResourceElement)left).getContents(),
						((ResourceElement)left).getName(), new ResourceSetImpl()).eResource();
				rightIsRemote = true;
			}
			leftResource = ModelUtils.load(((ResourceElement)right).getContents(),
					((ResourceElement)right).getName(), new ResourceSetImpl()).eResource();
			if (ancestor != null)
				ancestorResource = ModelUtils.load(((ResourceElement)ancestor).getContents(),
						((ResourceElement)ancestor).getName(), new ResourceSetImpl()).eResource();
			return true;
		}
		return false;
	}
}
