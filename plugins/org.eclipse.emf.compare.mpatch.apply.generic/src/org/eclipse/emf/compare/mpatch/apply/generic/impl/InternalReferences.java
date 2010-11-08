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
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.Map;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of the transformation replaces symbolic references within the {@link MPatchModel} with
 * {@link ModelDescriptorReference}s.
 * 
 * These are in particular references to added or removed elements which are described by {@link IModelDescriptor}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class InternalReferences implements IMPatchTransformation {

	/** Label. */
	public static final String LABEL = "Replace Internal " + MPatchConstants.SYMBOLIC_REFERENCES_NAME;

	/** Description for this transformation. */
	private static final String DESCRIPTION = "This transformation replaces internal "
			+ MPatchConstants.SYMBOLIC_REFERENCES_NAME
			+ " with special references (ModelDescriptorReferences). "
			+ "This is required for the default "
			+ MPatchConstants.MPATCH_SHORT_NAME
			+ " application engine to work properly!\n\n"
			+ "During the application of "
			+ MPatchConstants.MPATCH_SHORT_NAME
			+ ", "
			+ MPatchConstants.SYMBOLIC_REFERENCES_NAME
			+ " can be refined by the user. "
			+ "Since internal references refer to model elements that do not yet exist in the model, these elements cannot be refined."
			+ "Hence the need for internal " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + ".";

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return LABEL;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return DESCRIPTION;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPriority() {
		return 10;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOptional() {
		return false;
	}
	
	/**
	 * The transformation.
	 * 
	 * @return Number of created internal references.
	 */
	public int transform(MPatchModel mpatch) {
		return createInternalReferences(mpatch);
	}

	/**
	 * This method iterates over all model descriptors to collect information of all added/removed elements. Then it
	 * collects all cross references in the entire {@link MPatchModel}. Afterwards it replaces all references to those
	 * collected model descriptors with {@link ModelDescriptorReference}s.
	 * 
	 * @param mpatch
	 *            An {@link MPatchModel}.
	 * @return The number of internal symbolic references created.
	 */
	public static int createInternalReferences(MPatchModel mpatch) {
		final Map<String, IModelDescriptor> uriToDescriptorMap = MPatchUtil.collectDescriptors(mpatch.getChanges());
		final Map<IElementReference, IndepChange> crossReferences = MPatchUtil.collectCrossReferences(mpatch
				.getChanges());

		final int replacements = createInternalCrossReferences(crossReferences, uriToDescriptorMap);
		return replacements;
	}

	private static int createInternalCrossReferences(Map<IElementReference, IndepChange> crossReferences,
			Map<String, IModelDescriptor> uriToDescriptorMap) {
		int counter = 0;
		for (IElementReference ref : crossReferences.keySet()) {
			if (!(ref instanceof ModelDescriptorReference) && uriToDescriptorMap.containsKey(ref.getUriReference())) {

				// create internal reference...
				final ModelDescriptorReference newRef = MPatchFactory.eINSTANCE.createModelDescriptorReference();
				final IModelDescriptor descriptor = uriToDescriptorMap.get(ref.getUriReference());
				newRef.setResolvesTo(descriptor);

				// replace the old reference
				EcoreUtil.replace(ref, newRef);
				counter++;
			}
		}
		return counter;
	}
}
