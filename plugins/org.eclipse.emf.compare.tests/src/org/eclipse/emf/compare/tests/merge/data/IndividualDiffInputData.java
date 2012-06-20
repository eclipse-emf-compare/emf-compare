/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides input models to the unit tests of the matching by id.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class IndividualDiffInputData extends AbstractInputData {
	public Resource getAttributeMonoChangeLeft() throws IOException {
		return loadFromClassloader("attributemonochange/left.nodes");
	}

	public Resource getAttributeMonoChangeOrigin() throws IOException {
		return loadFromClassloader("attributemonochange/origin.nodes");
	}

	public Resource getAttributeMonoChangeRight() throws IOException {
		return loadFromClassloader("attributemonochange/right.nodes");
	}

	public Resource getAttributeMonoSetLeft() throws IOException {
		return loadFromClassloader("attributemonoset/left.nodes");
	}

	public Resource getAttributeMonoSetOrigin() throws IOException {
		return loadFromClassloader("attributemonoset/origin.nodes");
	}

	public Resource getAttributeMonoSetRight() throws IOException {
		return loadFromClassloader("attributemonoset/right.nodes");
	}

	public Resource getAttributeMonoUnsetLeft() throws IOException {
		return loadFromClassloader("attributemonounset/left.nodes");
	}

	public Resource getAttributeMonoUnsetOrigin() throws IOException {
		return loadFromClassloader("attributemonounset/origin.nodes");
	}

	public Resource getAttributeMonoUnsetRight() throws IOException {
		return loadFromClassloader("attributemonounset/right.nodes");
	}

	public Resource getAttributeMultiAddLeft() throws IOException {
		return loadFromClassloader("attributemultiadd/left.nodes");
	}

	public Resource getAttributeMultiAddOrigin() throws IOException {
		return loadFromClassloader("attributemultiadd/origin.nodes");
	}

	public Resource getAttributeMultiAddRight() throws IOException {
		return loadFromClassloader("attributemultiadd/right.nodes");
	}

	public Resource getAttributeMultiDelLeft() throws IOException {
		return loadFromClassloader("attributemultidel/left.nodes");
	}

	public Resource getAttributeMultiDelOrigin() throws IOException {
		return loadFromClassloader("attributemultidel/origin.nodes");
	}

	public Resource getAttributeMultiDelRight() throws IOException {
		return loadFromClassloader("attributemultidel/right.nodes");
	}

	public Resource getAttributeMultiMoveLeft() throws IOException {
		return loadFromClassloader("attributemultimove/left.nodes");
	}

	public Resource getAttributeMultiMoveOrigin() throws IOException {
		return loadFromClassloader("attributemultimove/origin.nodes");
	}

	public Resource getAttributeMultiMoveRight() throws IOException {
		return loadFromClassloader("attributemultimove/right.nodes");
	}

	public Resource getReferenceMonoChangeLeft() throws IOException {
		return loadFromClassloader("referencemonochange/left.nodes");
	}

	public Resource getReferenceMonoChangeOrigin() throws IOException {
		return loadFromClassloader("referencemonochange/origin.nodes");
	}

	public Resource getReferenceMonoChangeRight() throws IOException {
		return loadFromClassloader("referencemonochange/right.nodes");
	}

	public Resource getReferenceMonoSetLeft() throws IOException {
		return loadFromClassloader("referencemonoset/left.nodes");
	}

	public Resource getReferenceMonoSetOrigin() throws IOException {
		return loadFromClassloader("referencemonoset/origin.nodes");
	}

	public Resource getReferenceMonoSetRight() throws IOException {
		return loadFromClassloader("referencemonoset/right.nodes");
	}

	public Resource getReferenceMonoUnsetLeft() throws IOException {
		return loadFromClassloader("referencemonounset/left.nodes");
	}

	public Resource getReferenceMonoUnsetOrigin() throws IOException {
		return loadFromClassloader("referencemonounset/origin.nodes");
	}

	public Resource getReferenceMonoUnsetRight() throws IOException {
		return loadFromClassloader("referencemonounset/right.nodes");
	}

	public Resource getReferenceMultiAddLeft() throws IOException {
		return loadFromClassloader("referencemultiadd/left.nodes");
	}

	public Resource getReferenceMultiAddOrigin() throws IOException {
		return loadFromClassloader("referencemultiadd/origin.nodes");
	}

	public Resource getReferenceMultiAddRight() throws IOException {
		return loadFromClassloader("referencemultiadd/right.nodes");
	}

	public Resource getReferenceMultiDelLeft() throws IOException {
		return loadFromClassloader("referencemultidel/left.nodes");
	}

	public Resource getReferenceMultiDelOrigin() throws IOException {
		return loadFromClassloader("referencemultidel/origin.nodes");
	}

	public Resource getReferenceMultiDelRight() throws IOException {
		return loadFromClassloader("referencemultidel/right.nodes");
	}

	public Resource getReferenceMultiMoveLeft() throws IOException {
		return loadFromClassloader("referencemultimove/left.nodes");
	}

	public Resource getReferenceMultiMoveOrigin() throws IOException {
		return loadFromClassloader("referencemultimove/origin.nodes");
	}

	public Resource getReferenceMultiMoveRight() throws IOException {
		return loadFromClassloader("referencemultimove/right.nodes");
	}
}
