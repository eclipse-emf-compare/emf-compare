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
		return loadFromClassloader("fullscope/attributemonochange/left.nodes");
	}

	public Resource getAttributeMonoChangeOrigin() throws IOException {
		return loadFromClassloader("fullscope/attributemonochange/origin.nodes");
	}

	public Resource getAttributeMonoChangeRight() throws IOException {
		return loadFromClassloader("fullscope/attributemonochange/right.nodes");
	}

	public Resource getAttributeMonoSetLeft() throws IOException {
		return loadFromClassloader("fullscope/attributemonoset/left.nodes");
	}

	public Resource getAttributeMonoSetOrigin() throws IOException {
		return loadFromClassloader("fullscope/attributemonoset/origin.nodes");
	}

	public Resource getAttributeMonoSetRight() throws IOException {
		return loadFromClassloader("fullscope/attributemonoset/right.nodes");
	}

	public Resource getAttributeMonoUnsetLeft() throws IOException {
		return loadFromClassloader("fullscope/attributemonounset/left.nodes");
	}

	public Resource getAttributeMonoUnsetOrigin() throws IOException {
		return loadFromClassloader("fullscope/attributemonounset/origin.nodes");
	}

	public Resource getAttributeMonoUnsetRight() throws IOException {
		return loadFromClassloader("fullscope/attributemonounset/right.nodes");
	}

	public Resource getAttributeMultiAddLeft() throws IOException {
		return loadFromClassloader("fullscope/attributemultiadd/left.nodes");
	}

	public Resource getAttributeMultiAddOrigin() throws IOException {
		return loadFromClassloader("fullscope/attributemultiadd/origin.nodes");
	}

	public Resource getAttributeMultiAddRight() throws IOException {
		return loadFromClassloader("fullscope/attributemultiadd/right.nodes");
	}

	public Resource getAttributeMultiDelLeft() throws IOException {
		return loadFromClassloader("fullscope/attributemultidel/left.nodes");
	}

	public Resource getAttributeMultiDelOrigin() throws IOException {
		return loadFromClassloader("fullscope/attributemultidel/origin.nodes");
	}

	public Resource getAttributeMultiDelRight() throws IOException {
		return loadFromClassloader("fullscope/attributemultidel/right.nodes");
	}

	public Resource getAttributeMultiMoveLeft() throws IOException {
		return loadFromClassloader("fullscope/attributemultimove/left.nodes");
	}

	public Resource getAttributeMultiMoveOrigin() throws IOException {
		return loadFromClassloader("fullscope/attributemultimove/origin.nodes");
	}

	public Resource getAttributeMultiMoveRight() throws IOException {
		return loadFromClassloader("fullscope/attributemultimove/right.nodes");
	}

	public Resource getReferenceMonoChangeLeft() throws IOException {
		return loadFromClassloader("fullscope/referencemonochange/left.nodes");
	}

	public Resource getReferenceMonoChangeOrigin() throws IOException {
		return loadFromClassloader("fullscope/referencemonochange/origin.nodes");
	}

	public Resource getReferenceMonoChangeRight() throws IOException {
		return loadFromClassloader("fullscope/referencemonochange/right.nodes");
	}

	public Resource getReferenceMonoSetLeft() throws IOException {
		return loadFromClassloader("fullscope/referencemonoset/left.nodes");
	}

	public Resource getReferenceMonoSetOrigin() throws IOException {
		return loadFromClassloader("fullscope/referencemonoset/origin.nodes");
	}

	public Resource getReferenceMonoSetRight() throws IOException {
		return loadFromClassloader("fullscope/referencemonoset/right.nodes");
	}

	public Resource getReferenceMonoUnsetLeft() throws IOException {
		return loadFromClassloader("fullscope/referencemonounset/left.nodes");
	}

	public Resource getReferenceMonoUnsetOrigin() throws IOException {
		return loadFromClassloader("fullscope/referencemonounset/origin.nodes");
	}

	public Resource getReferenceMonoUnsetRight() throws IOException {
		return loadFromClassloader("fullscope/referencemonounset/right.nodes");
	}

	public Resource getReferenceMultiAddLeft() throws IOException {
		return loadFromClassloader("fullscope/referencemultiadd/left.nodes");
	}

	public Resource getReferenceMultiAddOrigin() throws IOException {
		return loadFromClassloader("fullscope/referencemultiadd/origin.nodes");
	}

	public Resource getReferenceMultiAddRight() throws IOException {
		return loadFromClassloader("fullscope/referencemultiadd/right.nodes");
	}

	public Resource getReferenceMultiDelLeft() throws IOException {
		return loadFromClassloader("fullscope/referencemultidel/left.nodes");
	}

	public Resource getReferenceMultiDelOrigin() throws IOException {
		return loadFromClassloader("fullscope/referencemultidel/origin.nodes");
	}

	public Resource getReferenceMultiDelRight() throws IOException {
		return loadFromClassloader("fullscope/referencemultidel/right.nodes");
	}

	public Resource getReferenceMultiMoveLeft() throws IOException {
		return loadFromClassloader("fullscope/referencemultimove/left.nodes");
	}

	public Resource getReferenceMultiMoveOrigin() throws IOException {
		return loadFromClassloader("fullscope/referencemultimove/origin.nodes");
	}

	public Resource getReferenceMultiMoveRight() throws IOException {
		return loadFromClassloader("fullscope/referencemultimove/right.nodes");
	}

	public Resource getReferenceMonoChangeLeftOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonochange/left.nodes");
	}

	public Resource getReferenceMonoChangeOriginOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonochange/origin.nodes");
	}

	public Resource getReferenceMonoChangeRightOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonochange/right.nodes");
	}

	public Resource getReferenceMonoSetLeftOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonoset/left.nodes");
	}

	public Resource getReferenceMonoSetOriginOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonoset/origin.nodes");
	}

	public Resource getReferenceMonoSetRightOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonoset/right.nodes");
	}

	public Resource getReferenceMonoUnsetLeftOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonounset/left.nodes");
	}

	public Resource getReferenceMonoUnsetOriginOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonounset/origin.nodes");
	}

	public Resource getReferenceMonoUnsetRightOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemonounset/right.nodes");
	}

	public Resource getReferenceMultiAddLeftOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultiadd/left.nodes");
	}

	public Resource getReferenceMultiAddOriginOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultiadd/origin.nodes");
	}

	public Resource getReferenceMultiAddRightOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultiadd/right.nodes");
	}

	public Resource getReferenceMultiDelLeftOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultidel/left.nodes");
	}

	public Resource getReferenceMultiDelOriginOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultidel/origin.nodes");
	}

	public Resource getReferenceMultiDelRightOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultidel/right.nodes");
	}

	public Resource getReferenceMultiMoveLeftOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultimove/left.nodes");
	}

	public Resource getReferenceMultiMoveOriginOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultimove/origin.nodes");
	}

	public Resource getReferenceMultiMoveRightOutOfScope() throws IOException {
		return loadFromClassloader("outofscope/referencemultimove/right.nodes");
	}
}
