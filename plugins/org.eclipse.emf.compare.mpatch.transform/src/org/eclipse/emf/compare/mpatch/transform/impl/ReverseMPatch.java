/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.transform.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Reversing an {@link MPatchModel}.<br>
 * Please see {@link ReverseMPatch#reverse(MPatchModel)} for details.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class ReverseMPatch implements IMPatchTransformation {

	/** The label. */
	public static final String LABEL = "Reverse MPatch";

	/** Description for this transformation. */
	private static final String DESCRIPTION = "This transformation reverses an " + MPatchConstants.MPATCH_LONG_NAME
			+ ". This is an optional transformation and makes the " + MPatchConstants.MPATCH_LONG_NAME
			+ " applicable in the other direction.\n\n" + "By default, an " + MPatchConstants.MPATCH_SHORT_NAME
			+ " P created from a Model A which is the unchanged version of a Model B "
			+ "can be used to reproduce Model B out of Model A, and not vice versa! "
			+ "So P is directed and cannot be used to create Model A out of Model B.\n"
			+ "This transformation reverses P and creates P_r such that P_r applied to Model B yields Model A.";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return LABEL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPriority() {
		return 80;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOptional() {
		return true;
	}

	/**
	 * See {@link DefaultMPatchGrouping#group(MPatchModel)}.
	 */
	@Override
	public int transform(MPatchModel mpatch) {
		return reverse(mpatch);
	}

	/**
	 * This reverses the direction of a given MPatch. By default, an MPatch P created from a Model A which is the
	 * unchanged version of a Model B can be used to reproduce Model B out of Model A, and not vice versa! So P is
	 * directed and cannot be used to create Model A out of Model B.
	 * 
	 * This transformation reverses P and creates P_r such that P_r applied to Model B yields Model A.
	 * 
	 * @param mpatch
	 *            An MPatch.
	 * @return <code>0</code>, if nothing was changed; the number of reversed changes, otherwise.
	 */
	public static int reverse(MPatchModel mpatch) {
		final List<EObject> allChanges = ExtEcoreUtils.collectTypedElements(mpatch.getChanges(),
				Collections.singleton(MPatchPackage.Literals.INDEP_CHANGE), true);
		final List<EObject> newChanges = new ArrayList<EObject>(allChanges.size());

		// iterate over all changes and reverse them
		int counter = 0;
		for (EObject change : allChanges) {
			final IndepChange oldChange = (IndepChange) change;
			final IndepChange newChange = reverseChangeDispatch(oldChange);
			newChanges.add(newChange == null ? oldChange : newChange);
			if (newChange != null)
				counter++;
		}

		// iterate one more time to record dependency graph
		final Map<IndepChange, List<IndepChange>> deps = new LinkedHashMap<IndepChange, List<IndepChange>>();
		for (EObject change : newChanges) {
			final IndepChange indepChange = (IndepChange) change;
			deps.put(indepChange, new ArrayList<IndepChange>(indepChange.getDependsOn()));
		}

		// iterate over all changes that have dependencies and reverse them
		for (IndepChange change : deps.keySet()) {
			// this works because it is a bidirectional association :-D
			change.getDependants().clear(); // also removes elements from opposite.dependsOn!
			change.getDependants().addAll(deps.get(change));
		}
		return counter;
	}

	protected static IndepChange reverseChangeDispatch(IndepChange change) {
		if (change instanceof IndepAddElementChange) {
			return reverseAddElementChange((IndepAddElementChange) change);
		} else if (change instanceof IndepRemoveElementChange) {
			return reverseRemoveElementChange((IndepRemoveElementChange) change);
		} else if (change instanceof IndepMoveElementChange) {
			return reverseMoveElementChange((IndepMoveElementChange) change);
		} else if (change instanceof IndepAddAttributeChange) {
			return reverseAddAttributeChange((IndepAddAttributeChange) change);
		} else if (change instanceof IndepRemoveAttributeChange) {
			return reverseRemoveAttributeChange((IndepRemoveAttributeChange) change);
		} else if (change instanceof IndepUpdateAttributeChange) {
			return reverseUpdateAttributeChange((IndepUpdateAttributeChange) change);
		} else if (change instanceof IndepAddReferenceChange) {
			return reverseAddReferenceChange((IndepAddReferenceChange) change);
		} else if (change instanceof IndepRemoveReferenceChange) {
			return reverseRemoveReferenceChange((IndepRemoveReferenceChange) change);
		} else if (change instanceof IndepUpdateReferenceChange) {
			return reverseUpdateReferenceChange((IndepUpdateReferenceChange) change);
		} else if (change instanceof UnknownChange || change instanceof ChangeGroup) {
			return null;
		} else {
			throw new IllegalArgumentException("Please implement reversal for unknown change type: "
					+ change.eClass().getName());
		}
	}

	private static IndepChange reverseAddElementChange(IndepAddElementChange change) {
		final IndepRemoveElementChange newChange = MPatchFactory.eINSTANCE.createIndepRemoveElementChange();
		newChange.setContainment(change.getContainment());
		newChange.setSubModel(change.getSubModel());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseRemoveElementChange(IndepRemoveElementChange change) {
		final IndepAddElementChange newChange = MPatchFactory.eINSTANCE.createIndepAddElementChange();
		newChange.setContainment(change.getContainment());
		newChange.setSubModel(change.getSubModel());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseMoveElementChange(IndepMoveElementChange change) {
		final IndepMoveElementChange newChange = MPatchFactory.eINSTANCE.createIndepMoveElementChange();
		newChange.setOldContainment(change.getNewContainment());
		newChange.setNewContainment(change.getOldContainment());
		newChange.setOldParent(change.getNewParent());
		newChange.setNewParent(change.getOldParent());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseAddAttributeChange(IndepAddAttributeChange change) {
		final IndepRemoveAttributeChange newChange = MPatchFactory.eINSTANCE.createIndepRemoveAttributeChange();
		newChange.setChangedAttribute(change.getChangedAttribute());
		newChange.setValue(change.getValue());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseRemoveAttributeChange(IndepRemoveAttributeChange change) {
		final IndepAddAttributeChange newChange = MPatchFactory.eINSTANCE.createIndepAddAttributeChange();
		newChange.setChangedAttribute(change.getChangedAttribute());
		newChange.setValue(change.getValue());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseUpdateAttributeChange(IndepUpdateAttributeChange change) {
		final IndepUpdateAttributeChange newChange = MPatchFactory.eINSTANCE.createIndepUpdateAttributeChange();
		newChange.setChangedAttribute(change.getChangedAttribute());
		newChange.setOldValue(change.getNewValue());
		newChange.setNewValue(change.getOldValue());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseAddReferenceChange(IndepAddReferenceChange change) {
		final IndepRemoveReferenceChange newChange = MPatchFactory.eINSTANCE.createIndepRemoveReferenceChange();
		newChange.setChangedReference(change.getChangedReference());
		newChange.setReference(change.getReference());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseRemoveReferenceChange(IndepRemoveReferenceChange change) {
		final IndepAddReferenceChange newChange = MPatchFactory.eINSTANCE.createIndepAddReferenceChange();
		newChange.setChangedReference(change.getChangedReference());
		newChange.setReference(change.getReference());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseUpdateReferenceChange(IndepUpdateReferenceChange change) {
		final IndepUpdateReferenceChange newChange = MPatchFactory.eINSTANCE.createIndepUpdateReferenceChange();
		newChange.setOldReference(change.getNewReference());
		newChange.setNewReference(change.getOldReference());
		newChange.setReference(change.getReference());
		return reverseIndepChangeAndReplace(change, newChange);
	}

	private static IndepChange reverseIndepChangeAndReplace(IndepChange oldChange, IndepChange newChange) {
		/*
		 * Lets see whether symbolic references of the changed version of the model elements exist. if not, just use the
		 * corresponding element.
		 */
		if (oldChange.getResultingElement() != null) {
			newChange.setCorrespondingElement(oldChange.getResultingElement());
			newChange.setResultingElement(oldChange.getCorrespondingElement());
		} else {
			newChange.setCorrespondingElement(oldChange.getCorrespondingElement());
		}
		newChange.getDependants().addAll(oldChange.getDependants());
		newChange.getDependsOn().addAll(oldChange.getDependsOn());
		oldChange.getDependants().clear();
		oldChange.getDependsOn().clear();
		if (oldChange.eContainer() == null)
			throw new IllegalStateException("The change must be contained somewhere: " + oldChange);
		EcoreUtil.replace(oldChange, newChange);
		return newChange;
	}
}
