/**
 * Copyright (c) 2012, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - added testdata for bug 455255
 */
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

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

	public Resource getAttributeMonoEEnumChangeLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemonochange/eenum/left.nodes");
	}

	public Resource getAttributeMonoEEnumChangeOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemonochange/eenum/origin.nodes");
	}

	public Resource getAttributeMonoEEnumChangeRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemonochange/eenum/right.nodes");
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

	public Resource getAttributeEEnumMultiAddLeft() throws IOException {
		return loadFromClassLoader("fullscope/attributemultiadd/eenum/left.nodes");
	}

	public Resource getAttributeEEnumMultiAddOrigin() throws IOException {
		return loadFromClassLoader("fullscope/attributemultiadd/eenum/origin.nodes");
	}

	public Resource getAttributeEEnumMultiAddRight() throws IOException {
		return loadFromClassLoader("fullscope/attributemultiadd/eenum/right.nodes");
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

	public Resource getReferenceDifferentContainmentMoveOrigin() throws IOException {
		return loadFromClassLoader("fullscope/differentcontainmentmove/origin.nodes");
	}

	public Resource getReferenceDifferentContainmentMoveLeft() throws IOException {
		return loadFromClassLoader("fullscope/differentcontainmentmove/left.nodes");
	}

	public Resource getReferenceDifferentContainmentMoveRight() throws IOException {
		return loadFromClassLoader("fullscope/differentcontainmentmove/right.nodes");
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

	public Resource getLeftAddRightAddLeftConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightadd/left.nodes");
	}

	public Resource getLeftAddRightAddOriginConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightadd/origin.nodes");
	}

	public Resource getLeftAddRightAddRightConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightadd/right.nodes");
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

	public Resource getFeatureMapContainmentLeftAddScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/add/left.nodes");
	}

	public Resource getFeatureMapContainmentRightAddScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/add/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginAddScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/add/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/delete/left.nodes");
	}

	public Resource getFeatureMapContainmentRightDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/delete/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/delete/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/keychange/left.nodes");
	}

	public Resource getFeatureMapContainmentRightKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/keychange/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/keychange/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/move/left.nodes");
	}

	public Resource getFeatureMapContainmentRightMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/move/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/move/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/moveorder/left.nodes");
	}

	public Resource getFeatureMapContainmentRightMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/moveorder/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/moveorder/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftMoveKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/movekeychange/left.nodes");
	}

	public Resource getFeatureMapContainmentRightMoveKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/movekeychange/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginMoveKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/movekeychange/ancestor.nodes");
	}

	public Resource getFeatureMapNonContainmentLeftAddScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/add/left.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightAddScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/add/right.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginAddScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/add/ancestor.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentLeftDeleteScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/delete/left.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightDeleteScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/delete/right.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginDeleteScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/delete/ancestor.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentLeftMoveOrderScope(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/moveorder/left.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightMoveOrderScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/moveorder/right.nodes", resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginMoveOrderScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/moveorder/ancestor.nodes", resourceSet);
	}

	public Resource getFeatureMapContainmentLeftConflictLeftAddRightAddWithSameKeyScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftadd_rightadd/left.nodes");
	}

	public Resource getFeatureMapContainmentRightConflictLeftAddRightAddWithSameKeyScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftadd_rightadd/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginConflictLeftAddRightAddWithSameKeyScope()
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftadd_rightadd/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftConflictLeftAddRightAddWithDifferentKeyScope()
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftadd_rightadd_2/left.nodes");
	}

	public Resource getFeatureMapContainmentRightConflictLeftAddRightAddWithDifferentKeyScope()
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftadd_rightadd_2/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginConflictLeftAddRightAddWithDifferentKeyScope()
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftadd_rightadd_2/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftkeychange_rightdelete/left.nodes");
	}

	public Resource getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftkeychange_rightdelete/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftkeychange_rightdelete/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftConflictLeftMoveRightMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftmove_rightmove/left.nodes");
	}

	public Resource getFeatureMapContainmentRightConflictLeftMoveRightMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftmove_rightmove/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginConflictLeftMoveRightMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftmove_rightmove/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftConflictLeftMoveOrderRightMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftmoveorder_rightmoveorder/left.nodes");
	}

	public Resource getFeatureMapContainmentRightConflictLeftMoveOrderRightMoveOrderScope()
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftmoveorder_rightmoveorder/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginConflictLeftMoveOrderRightMoveOrderScope()
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/conflicts/leftmoveorder_rightmoveorder/ancestor.nodes");
	}

	public Resource getFeatureMapNonContainmentLeftConflictLeftMoveOrderRightMoveOrderScope(
			ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader(
				"featuremapscope/noncontainment/conflicts/leftmoveorder_rightmoveorder/left.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightConflictLeftMoveOrderRightMoveOrderScope(
			ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader(
				"featuremapscope/noncontainment/conflicts/leftmoveorder_rightmoveorder/right.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginConflictLeftMoveOrderRightMoveOrderScope(
			ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader(
				"featuremapscope/noncontainment/conflicts/leftmoveorder_rightmoveorder/ancestor.nodes",
				resourceSet);
	}

	public Resource getFeatureMapContainmentLeftPseudoConflictAddScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/add/left.nodes");
	}

	public Resource getFeatureMapContainmentRightPseudoConflictAddScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/add/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginPseudoConflictAddScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/add/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftPseudoConflictDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/delete/left.nodes");
	}

	public Resource getFeatureMapContainmentRightPseudoConflictDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/delete/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginPseudoConflictDeleteScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/delete/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftPseudoConflictKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/keychange/left.nodes");
	}

	public Resource getFeatureMapContainmentRightPseudoConflictKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/keychange/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginPseudoConflictKeyChangeScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/keychange/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftPseudoConflictMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/move/left.nodes");
	}

	public Resource getFeatureMapContainmentRightPseudoConflictMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/move/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginPseudoConflictMoveScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/move/ancestor.nodes");
	}

	public Resource getFeatureMapContainmentLeftPseudoConflictMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/moveorder/left.nodes");
	}

	public Resource getFeatureMapContainmentRightPseudoConflictMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/moveorder/right.nodes");
	}

	public Resource getFeatureMapContainmentOriginPseudoConflictMoveOrderScope() throws IOException {
		return loadFromClassLoader("featuremapscope/containment/pseudoconflicts/moveorder/ancestor.nodes");
	}

	public Resource getFeatureMapNonContainmentLeftPseudoConflictAddScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/add/left.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightPseudoConflictAddScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/add/right.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginPseudoConflictAddScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/add/ancestor.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentLeftPseudoConflictDeleteScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/delete/left.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightPseudoConflictDeleteScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/delete/right.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginPseudoConflictDeleteScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/delete/ancestor.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentLeftPseudoConflictMoveOrderScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/moveorder/left.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentRightPseudoConflictMoveOrderScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/moveorder/right.nodes",
				resourceSet);
	}

	public Resource getFeatureMapNonContainmentOriginPseudoConflictMoveOrderScope(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/noncontainment/pseudoconflicts/moveorder/ancestor.nodes",
				resourceSet);
	}

	public Resource getFeatureMapContainmentMoveInsideOutsideOrigin(ResourceSet resourceSet)
			throws IOException {
		return loadFromClassLoader("featuremapscope/containment/moveinandout/ancestor.nodes", resourceSet);
	}

	public Resource getFeatureMapContainmentMoveOutside(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/containment/moveinandout/outside.nodes", resourceSet);
	}

	public Resource getFeatureMapContainmentMoveInside(ResourceSet resourceSet) throws IOException {
		return loadFromClassLoader("featuremapscope/containment/moveinandout/inside.nodes", resourceSet);
	}

	public Resource getLeftSetRightSetOriginEEnumConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightset/origin.nodes");
	}

	public Resource getLeftSetRightSetLeftEEnumConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightset/left.nodes");
	}

	public Resource getLeftSetRightSetRightEEnumConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftset_rightset/right.nodes");
	}

	public Resource getLeftAddRightDeleteOriginEEnumConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightdelete/eenum/origin.nodes");
	}

	public Resource getLeftAddRightDeleteLeftEEnumConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightdelete/eenum/left.nodes");
	}

	public Resource getLeftAddRightDeleteRightEEnumConflictScope() throws IOException {
		return loadFromClassLoader("conflictscope/leftadd_rightdelete/eenum/right.nodes");
	}
}
