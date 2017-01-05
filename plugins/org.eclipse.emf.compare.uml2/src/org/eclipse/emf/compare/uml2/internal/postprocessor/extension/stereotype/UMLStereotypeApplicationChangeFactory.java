/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreSwitch;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for stereotype application changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLStereotypeApplicationChangeFactory extends AbstractUMLChangeFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return StereotypeApplicationChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createStereotypeApplicationChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminant(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EObject getDiscriminant(Diff input) {
		return Iterables.find(getDiscriminants(input), instanceOf(EObject.class), null);
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
				Set<EObject> result = new LinkedHashSet<EObject>();
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
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#setRefiningChanges(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.compare.DifferenceKind, org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		super.setRefiningChanges(extension, extensionKind, refiningDiff);

		EObject discriminant = getDiscriminant(refiningDiff);

		if (discriminant != null) {
			final Iterator<Diff> changes = ComparisonUtil.getComparison(refiningDiff).getMatch(discriminant)
					.getDifferences().iterator();
			while (changes.hasNext()) {
				final Diff diff = changes.next();
				if ((diff instanceof AttributeChange || diff instanceof ReferenceChange
						|| diff instanceof ResourceAttachmentChange)
						&& diff.getSource() == extension.getSource()) {
					extension.getRefinedBy().add(diff);
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionChange(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return UMLCompareUtil
				.getBaseElement(MatchUtil.getContainer(input.getMatch().getComparison(), input)) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#isRelatedToAnExtensionChange(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return UMLCompareUtil
				.getBaseElement(MatchUtil.getContainer(input.getMatch().getComparison(), input)) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#isRelatedToAnExtensionAdd(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		// do nothing
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#isRelatedToAnExtensionDelete(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		// do nothing
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionAdd(org.eclipse.emf.compare.ResourceAttachmentChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionAdd(ResourceAttachmentChange input) {
		return input.getKind() == DifferenceKind.ADD && UMLCompareUtil
				.getBaseElement(MatchUtil.getContainer(input.getMatch().getComparison(), input)) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionDelete(org.eclipse.emf.compare.ResourceAttachmentChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionDelete(ResourceAttachmentChange input) {
		return input.getKind() == DifferenceKind.DELETE && UMLCompareUtil
				.getBaseElement(MatchUtil.getContainer(input.getMatch().getComparison(), input)) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		super.fillRequiredDifferences(comparison, extension);
		if (!(extension instanceof StereotypeApplicationChange)) {
			return;
		}
		final StereotypeApplicationChange stereotypeApplicationChange = (StereotypeApplicationChange)extension;
		if (stereotypeApplicationChange.getKind() == DifferenceKind.ADD) {
			final Stereotype stereotype = UMLUtil
					.getStereotype(stereotypeApplicationChange.getDiscriminant());
			if (stereotype != null) {
				// FIXME this is not the place to set this, it should be done at creation time.
				stereotypeApplicationChange.setStereotype(stereotype);
				final Profile profile = stereotype.getProfile();
				fillRequirements(comparison, stereotypeApplicationChange, profile);
			}
		}
	}

	/**
	 * It fills the requirements of the given stereotype application change from the profile related to the
	 * stereotype application.
	 * 
	 * @param comparison
	 *            the comparison.
	 * @param stereotypeApplicationChange
	 *            The stereotype application to set.
	 * @param profile
	 *            The profile related to the stereotype application.
	 */
	private void fillRequirements(Comparison comparison,
			final StereotypeApplicationChange stereotypeApplicationChange, final Profile profile) {
		final Iterator<Setting> settings = UML2Util.getInverseReferences(profile).iterator();
		while (settings.hasNext()) {
			final Setting setting = settings.next();
			if (setting.getEStructuralFeature() == UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE) {
				final ProfileApplication profileApplication = (ProfileApplication)setting.getEObject();
				for (Diff diff : comparison.getDifferences(profileApplication)) {
					if (diff instanceof ProfileApplicationChange && diff.getKind() == DifferenceKind.ADD
							&& diff.getSource() == stereotypeApplicationChange.getSource()) {
						stereotypeApplicationChange.getRequires().add(diff);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public Match getParentMatch(Diff input) {
		final EObject discriminant = getDiscriminant(input);
		if (discriminant != null) {
			final Element element = UMLCompareUtil.getBaseElement(discriminant);
			final Match match = ComparisonUtil.getComparison(input).getMatch(element);
			if (match != null) {
				return match;
			}
		}
		return super.getParentMatch(input);
	}

}
