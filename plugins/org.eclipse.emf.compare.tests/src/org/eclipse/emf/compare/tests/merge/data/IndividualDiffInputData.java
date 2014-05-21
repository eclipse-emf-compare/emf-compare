/**
 * Copyright (c) 2012, 2014 Obeo.
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
		return loadFromClassLoader("fullscope/attributemonochange/left.nodes");
	}

	public Resource getAttributeMonoChangeOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemonochange/origin.nodes");
	}

	public Resource getAttributeMonoChangeRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemonochange/right.nodes");
	}

	public Resource getAttributeMonoSetLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemonoset/left.nodes");
	}

	public Resource getAttributeMonoSetOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemonoset/origin.nodes");
	}

	public Resource getAttributeMonoSetRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemonoset/right.nodes");
	}

	public Resource getAttributeMonoUnsetLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemonounset/left.nodes");
	}

	public Resource getAttributeMonoUnsetOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemonounset/origin.nodes");
	}

	public Resource getAttributeMonoUnsetRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemonounset/right.nodes");
	}

	public Resource getAttributeMultiAddLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemultiadd/left.nodes");
	}

	public Resource getAttributeMultiAddOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemultiadd/origin.nodes");
	}

	public Resource getAttributeMultiAddRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemultiadd/right.nodes");
	}

	public Resource getAttributeMultiDelLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemultidel/left.nodes");
	}

	public Resource getAttributeMultiDelOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemultidel/origin.nodes");
	}

	public Resource getAttributeMultiDelRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemultidel/right.nodes");
	}

	public Resource getAttributeMultiMoveLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemultimove/left.nodes");
	}

	public Resource getAttributeMultiMoveOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemultimove/origin.nodes");
	}

	public Resource getAttributeMultiMoveRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemultimove/right.nodes");
	}

	public Resource getReferenceMonoChangeLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencemonochange/left.nodes");
	}

	public Resource getReferenceMonoChangeOrigin() throws IOException {
		return loadFromClassLoader("fullscope/referencemonochange/origin.nodes");
	}

	public Resource getReferenceMonoChangeRight() throws IOException {
		return loadFromClassLoader("fullscope/referencemonochange/right.nodes");
	}

	public Resource getReferenceMonoSetLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencemonoset/left.nodes");
	}

	public Resource getReferenceMonoSetOrigin() throws IOException {
		return loadFromClassLoader("fullscope/referencemonoset/origin.nodes");
	}

	public Resource getReferenceMonoSetRight() throws IOException {
		return loadFromClassLoader("fullscope/referencemonoset/right.nodes");
	}

	public Resource getReferenceMonoUnsetLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencemonounset/left.nodes");
	}

	public Resource getReferenceMonoUnsetOrigin() throws IOException {
		return loadFromClassLoader("fullscope/referencemonounset/origin.nodes");
	}

	public Resource getReferenceMonoUnsetRight() throws IOException {
		return loadFromClassLoader("fullscope/referencemonounset/right.nodes");
	}

	public Resource getReferenceMultiAddLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencemultiadd/left.nodes");
	}

	public Resource getReferenceMultiAddOrigin() throws IOException {
		return loadFromClassLoader("fullscope/referencemultiadd/origin.nodes");
	}

	public Resource getReferenceMultiAddRight() throws IOException {
		return loadFromClassLoader("fullscope/referencemultiadd/right.nodes");
	}

	public Resource getReferenceMultiDelLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencemultidel/left.nodes");
	}

	public Resource getReferenceMultiDelOrigin() throws IOException {
		return loadFromClassLoader("fullscope/referencemultidel/origin.nodes");
	}

	public Resource getReferenceMultiDelRight() throws IOException {
		return loadFromClassLoader("fullscope/referencemultidel/right.nodes");
	}

	public Resource getReferenceMultiMoveLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencemultimove/left.nodes");
	}

	public Resource getReferenceMultiMoveOrigin() throws IOException {
		return loadFromClassLoader("fullscope/referencemultimove/origin.nodes");
	}

	public Resource getReferenceMultiMoveRight() throws IOException {
		return loadFromClassLoader("fullscope/referencemultimove/right.nodes");
	}

	public Resource getReferenceContainmentMultiMoveLeft() throws IOException {
		return loadFromClassLoader("fullscope/referencecontainmentmultimove/left.nodes");
	}

	public Resource getReferenceContainmentMultiMoveRight() throws IOException {
		return loadFromClassLoader("fullscope/referencecontainmentmultimove/right.nodes");
	}

	public Resource getReferenceMonoChangeLeftOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonochange/left.nodes");
	}

	public Resource getReferenceMonoChangeOriginOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonochange/origin.nodes");
	}

	public Resource getReferenceMonoChangeRightOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonochange/right.nodes");
	}

	public Resource getReferenceMonoSetLeftOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonoset/left.nodes");
	}

	public Resource getReferenceMonoSetOriginOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonoset/origin.nodes");
	}

	public Resource getReferenceMonoSetRightOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonoset/right.nodes");
	}

	public Resource getReferenceMonoUnsetLeftOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonounset/left.nodes");
	}

	public Resource getReferenceMonoUnsetOriginOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonounset/origin.nodes");
	}

	public Resource getReferenceMonoUnsetRightOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemonounset/right.nodes");
	}

	public Resource getReferenceMultiAddLeftOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultiadd/left.nodes");
	}

	public Resource getReferenceMultiAddOriginOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultiadd/origin.nodes");
	}

	public Resource getReferenceMultiAddRightOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultiadd/right.nodes");
	}

	public Resource getReferenceMultiDelLeftOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultidel/left.nodes");
	}

	public Resource getReferenceMultiDelOriginOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultidel/origin.nodes");
	}

	public Resource getReferenceMultiDelRightOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultidel/right.nodes");
	}

	public Resource getReferenceMultiMoveLeftOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultimove/left.nodes");
	}

	public Resource getReferenceMultiMoveOriginOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultimove/origin.nodes");
	}

	public Resource getReferenceMultiMoveRightOutOfScope() throws IOException {
		return loadFromClassLoader("outofscope/referencemultimove/right.nodes");
	}

	public Resource getLeftAddRightDeleteLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightdelete/left.ecore");
	}

	public Resource getLeftAddRightDeleteOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightdelete/origin.ecore");
	}

	public Resource getLeftAddRightDeleteRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightdelete/right.ecore");
	}

	public Resource getLeftDeleteRightAddLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightadd/left.ecore");
	}

	public Resource getLeftDeleteRightAddOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightadd/origin.ecore");
	}

	public Resource getLeftDeleteRightAddRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightadd/right.ecore");
	}

	public Resource getLeftDeleteRightSetLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightset/left.ecore");
	}

	public Resource getLeftDeleteRightSetOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightset/origin.ecore");
	}

	public Resource getLeftDeleteRightSetRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightset/right.ecore");
	}

	public Resource getLeftSetRightDeleteLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightdelete/left.ecore");
	}

	public Resource getLeftSetRightDeleteOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightdelete/origin.ecore");
	}

	public Resource getLeftSetRightDeleteRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightdelete/right.ecore");
	}

	public Resource getLeftSetRightUnsetLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightunset/left.ecore");
	}

	public Resource getLeftSetRightUnsetOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightunset/origin.ecore");
	}

	public Resource getLeftSetRightUnsetRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightunset/right.ecore");
	}

	public Resource getLeftUnsetRightSetLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftunset_rightset/left.ecore");
	}

	public Resource getLeftUnsetRightSetOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftunset_rightset/origin.ecore");
	}

	public Resource getLeftUnsetRightSetRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftunset_rightset/right.ecore");
	}

	public Resource getLeftDeleteRightMoveLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightmove/left.ecore");
	}

	public Resource getLeftDeleteRightMoveOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightmove/origin.ecore");
	}

	public Resource getLeftDeleteRightMoveRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftdelete_rightmove/right.ecore");
	}

	public Resource getLeftMoveRightDeleteLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftmove_rightdelete/left.ecore");
	}

	public Resource getLeftMoveRightDeleteOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftmove_rightdelete/origin.ecore");
	}

	public Resource getLeftMoveRightDeleteRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftmove_rightdelete/right.ecore");
	}

	public Resource getLeftPseudoConflictAddScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/add/left.nodes");
	}

	public Resource getOriginPseudoConflictAddScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/add/ancestor.nodes");
	}

	public Resource getRightPseudoConflictAddScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/add/right.nodes");
	}

	public Resource getLeftPseudoConflictChangeScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/change/left.nodes");
	}

	public Resource getOriginPseudoConflictChangeScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/change/ancestor.nodes");
	}

	public Resource getRightPseudoConflictChangeScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/change/right.nodes");
	}

	public Resource getLeftPseudoConflictDeleteScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/delete/left.nodes");
	}

	public Resource getOriginPseudoConflictDeleteScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/delete/ancestor.nodes");
	}

	public Resource getRightPseudoConflictDeleteScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/delete/right.nodes");
	}

	public Resource getLeftPseudoConflictFullScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/fullscope/left.nodes");
	}

	public Resource getOriginPseudoConflictFullScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/fullscope/ancestor.nodes");
	}

	public Resource getRightPseudoConflictFullScope() throws IOException {
		return loadFromClassLoader("pseudoconflictscope/fullscope/right.nodes");
	}
}
