/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory of UML Dangling Stereotype Application extensions.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLDanglingStereotypeApplicationFactory extends AbstractUMLChangeFactory {

	@Override
	public boolean handles(Diff input) {
		final boolean handles;
		boolean isRAC = input instanceof ResourceAttachmentChange;
		if (!isRAC) {
			handles = false;
		} else {
			EObject stereotypeApplication = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			if (stereotypeApplication != null) {
				handles = UMLUtil.getStereotype(stereotypeApplication) != null
						&& UMLCompareUtil.getBaseElement(stereotypeApplication) == null;
			} else {
				handles = false;
			}
		}
		return handles;
	}

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return DanglingStereotypeApplication.class;
	}

	@Override
	public Diff createExtension() {
		return UMLCompareFactory.eINSTANCE.createDanglingStereotypeApplication();
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ResourceAttachmentChange input) {
		return input.getKind() == DifferenceKind.ADD;
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ResourceAttachmentChange input) {
		return input.getKind() == DifferenceKind.DELETE;
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		if (refiningDiff.getSource() == extension.getSource()) {
			extension.getRefinedBy().add(refiningDiff);
			// Unfortunate, don't know how to set the resourceURI elsewhere than here.
			if (refiningDiff instanceof ResourceAttachmentChange
					&& extension instanceof DanglingStereotypeApplication) {
				((DanglingStereotypeApplication)extension)
						.setResourceURI(((ResourceAttachmentChange)refiningDiff).getResourceURI());
			}
		}
	}

	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return null;
	}

	@Override
	protected EObject getDiscriminant(Diff input) {
		return null;
	}

}
