/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypedElementChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.Element;

/**
 * Factory of {@link StereotypeApplicationChange}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class UMLStereotypedElementChangeFactory extends AbstractUMLChangeFactory {

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return StereotypedElementChange.class;
	}

	@Override
	public boolean handles(Diff input) {
		if (input instanceof ReferenceChange) {
			ReferenceChange refChange = (ReferenceChange)input;
			if (refChange.getReference().isContainment()
					&& refChange.getValue() instanceof Element
					&& (refChange.getKind() == DifferenceKind.ADD || refChange.getKind() == DifferenceKind.DELETE)) {
				return !getStereotypeApplicationChanges(refChange).isEmpty();
			}
		}
		return false;
	}

	/**
	 * Gets the {@link StereotypeApplicationChange} link to this difference.
	 * 
	 * @param refChange
	 *            Input differences.
	 * @return The list of {@link StereotypeApplicationChange} linked to the input.
	 */
	private List<StereotypeApplicationChange> getStereotypeApplicationChanges(final ReferenceChange refChange) {
		List<StereotypeApplicationChange> result = Lists.newArrayList();

		final List<Diff> searchScope;
		switch (refChange.getKind()) {
			case ADD:
				searchScope = refChange.getRequiredBy();
				break;
			case DELETE:
				searchScope = refChange.getRequires();
				break;
			default:
				searchScope = Collections.emptyList();
		}
		for (Diff reqBy : searchScope) {
			if (reqBy instanceof StereotypeApplicationChange) {
				StereotypeApplicationChange stereoAppChange = (StereotypeApplicationChange)reqBy;
				if (refChange.getValue().equals(
						UMLCompareUtil.getBaseElement(stereoAppChange.getDiscriminant()))) {
					result.add(stereoAppChange);
				}
			}
		}
		return result;
	}

	@Override
	public Diff createExtension() {
		return UMLCompareFactory.eINSTANCE.createStereotypedElementChange();
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		// super.setRefiningChanges(extension, extensionKind, refiningDiff);
		List<StereotypeApplicationChange> stereotypeApplicationChanges = getStereotypeApplicationChanges((ReferenceChange)refiningDiff);
		if (refiningDiff.getSource() == extension.getSource()) {
			extension.getRefinedBy().add(refiningDiff);
			extension.getRefinedBy().addAll(
					Collections2.filter(stereotypeApplicationChanges, EMFComparePredicates.fromSide(extension
							.getSource())));
		}

	}

	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		// Empty implementation
		return null;
	}

	@Override
	protected EObject getDiscriminant(Diff input) {
		return ((ReferenceChange)input).getValue();
	}

}
