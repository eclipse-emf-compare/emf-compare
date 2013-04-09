/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.usecase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.IncludeChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLExtendChangeLeftTarget.
 */
public class UMLIncludeChangeFactory extends AbstractUMLChangeFactory {

	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return IncludeChange.class;
	}

	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createIncludeChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			result = ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			if (container instanceof Include) {
				result = container;
			}
		}
		return result;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof Include) {
			result.add(discriminant);
			result.add(((Include)discriminant).getAddition());
		}
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof Include && ((Include)input.getValue()).getAddition() != null;
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof Include;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return input.getReference().equals(UMLPackage.Literals.INCLUDE__ADDITION);
	}

}
