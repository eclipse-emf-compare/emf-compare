/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * An {@link IDiffProcessor} is meant to be used in order to react to the detection of differences by the diff
 * engine.
 * <p>
 * The default implementation of a Diff engine only detects the changes and sends them over to its
 * {@link IDiffProcessor}. It will then be up to the diff processor to create a
 * {@link org.eclipse.emf.compare.Diff} and attach it to the provided {@link Match}, simply react to the
 * notification... or ignore it altogether.
 * </p>
 * <p>
 * {@link DiffBuilder}, a default implementation of this interface, can be subclassed instead.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see DiffBuilder
 */
public interface IDiffProcessor {
	/**
	 * This will be called whenever the Diff engine detected a difference for a given reference value.
	 * 
	 * @param match
	 *            The match to which this difference should be attached.
	 * @param reference
	 *            The reference on which we detected a difference.
	 * @param value
	 *            The actual value for which we detected a difference.
	 * @param kind
	 *            Kind of the detected difference.
	 * @param source
	 *            Source of the detected difference. For two way comparisons, this will always be
	 *            {@link DifferenceSource#LEFT}. Otherwise, this will indicate the side on which this
	 *            difference has been detected.
	 */
	void referenceChange(Match match, EReference reference, EObject value, DifferenceKind kind,
			DifferenceSource source);
}
