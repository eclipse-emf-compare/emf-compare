/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.find;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Factory for diffs affecting {@link MultiplicityElement MultiplicityElements}.
 * 
 * @author Alexandra Buzila <abuzila@eclipsesource.com>
 */
public class MultiplicityElementChangeFactory extends AbstractUMLChangeFactory {

	@Override
	public boolean handles(Diff input) {
		return isChangeOfMultiplicityElement(input) && !refinesMultiplicityChange(input);
	}

	/**
	 * Check whether the given Diff represents a change of a {@link MultiplicityElement}.
	 * 
	 * @param input
	 *            the difference to be checked
	 * @return <code>true</code> if the diff is a change of a {@link MultiplicityElement}, <code>false</code>
	 *         otherwise.
	 */
	private boolean isChangeOfMultiplicityElement(Diff input) {
		if (input instanceof ReferenceChange) {
			EReference reference = ((ReferenceChange)input).getReference();
			return reference == UMLPackage.eINSTANCE.getMultiplicityElement_LowerValue()
					|| reference == UMLPackage.eINSTANCE.getMultiplicityElement_UpperValue();
		} else if (input instanceof AttributeChange) {
			EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			if (container instanceof LiteralInteger || container instanceof LiteralUnlimitedNatural) {
				EStructuralFeature eContainingFeature = container.eContainingFeature();
				return eContainingFeature == UMLPackage.eINSTANCE.getMultiplicityElement_LowerValue()
						|| eContainingFeature == UMLPackage.eINSTANCE.getMultiplicityElement_UpperValue();
			}
		}
		return false;
	}

	/**
	 * Verify if the given diff refines a {@link MultiplicityElementChange} type diff.
	 * 
	 * @param input
	 *            the diff
	 * @return true if the diff refines a {@link MultiplicityElementChange}
	 */
	private boolean refinesMultiplicityChange(Diff input) {
		return any(input.getRefines(), instanceOf(MultiplicityElementChange.class));
	}

	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		// do nothing
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		if (refiningDiff.getSource() == extension.getSource()) {
			addRefiningDiffAndDependencies(extension, refiningDiff);
		}
	}

	/**
	 * Add the extension's refining diff and update the its kind, based on the refining diff's kind.
	 * 
	 * @param extension
	 *            The diff to which the refining diff must be added
	 * @param refiningDiff
	 *            The refining diff to add
	 */
	private void addRefiningDiffAndDependencies(Diff extension, Diff refiningDiff) {
		extension.getRefinedBy().add(refiningDiff);
		extension.setKind(refiningDiff.getKind());
	}

	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new DiscriminantsGetter() {
			@Override
			public Set<EObject> caseValueSpecification(ValueSpecification object) {
				Set<EObject> result = new LinkedHashSet<EObject>();
				result.add(object);
				return result;
			}
		};
	}

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return MultiplicityElementChange.class;
	}

	@Override
	protected EObject getDiscriminant(Diff input) {
		return find(getDiscriminants(input), instanceOf(ValueSpecification.class), null);
	}

	@Override
	public Match getParentMatch(Diff input) {
		EObject multiplicityElement = getDiscriminant(input).eContainer();
		return ComparisonUtil.getComparison(input).getMatch(multiplicityElement);
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getKind() == DifferenceKind.ADD;
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getKind() == DifferenceKind.DELETE;
	}

	@Override
	public Diff createExtension() {
		return UMLCompareFactory.eINSTANCE.createMultiplicityElementChange();
	}

}
