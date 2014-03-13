/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreSwitch;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.Element;

/**
 * Factory for stereotype string property changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeAttributeChangeFactory extends AbstractUMLChangeFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#handles(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public boolean handles(Diff input) {
		if (input instanceof AttributeChange) {
			return super.handles(input);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminantsGetter()
	 */
	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new EcoreSwitch<Set<EObject>>() {
			@Override
			public Set<EObject> defaultCase(EObject object) {
				Set<EObject> result = new HashSet<EObject>();
				if (object.eContainer() == null) {
					result.add(object);
				} else {
					return defaultCaseForDiscriminantsGetter(this, object);
				}
				return result;
			}
		};

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminant(org.eclipse
	 *      .emf.compare.Diff)
	 */
	@Override
	protected EObject getDiscriminant(Diff input) {
		final EObject discriminant;
		if (input instanceof AttributeChange) {
			discriminant = ((AttributeChange)input).getAttribute();
		} else {
			discriminant = Iterables.find(getDiscriminants(input), instanceOf(EObject.class), null);
		}
		return discriminant;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public Diff createExtension() {
		return UMLCompareFactory.eINSTANCE.createStereotypeAttributeChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return StereotypeAttributeChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionChange(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return UMLCompareUtil.getBaseElement(MatchUtil.getContainer(input.getMatch().getComparison(), input)) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#setRefiningChanges(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.compare.DifferenceKind, org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		extension.getRefinedBy().add(refiningDiff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public Match getParentMatch(Diff input) {
		Match match = input.getMatch();
		if (match != null) {
			EObject object = match.getLeft();
			if (object == null) {
				object = match.getRight();
			}
			if (object == null) {
				object = match.getOrigin();
			}
			final Element element = UMLCompareUtil.getBaseElement(object);
			match = match.getComparison().getMatch(element);
		}
		return match;
	}
}
