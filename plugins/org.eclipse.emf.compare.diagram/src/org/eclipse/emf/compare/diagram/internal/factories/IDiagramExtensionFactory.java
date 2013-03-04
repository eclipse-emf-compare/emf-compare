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
package org.eclipse.emf.compare.diagram.internal.factories;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;

/**
 * An {@link IDiagramExtensionFactory} is a factory capable to create an {@link Diff extension} from a
 * {@link Diff} if and only if this factory can {@link #handles(Diff) handle} the given {@link Diff}.
 * <p>
 * A factory must be able to say in which parent an {@link Diff extension} must be attached if it handles the
 * {@link Diff} from which it has been {@link #create(Diff) created}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IDiagramExtensionFactory {

	/**
	 * Returns the kind of extension that this factory has to create.
	 * 
	 * @return The kind of extension.
	 */
	Class<? extends Diff> getExtensionKind();

	/**
	 * Returns true if this factory handles the given kind of Diff, i.e., if it can create an {@link Diff
	 * extension}.
	 * <p>
	 * <b>Performance note: </b> this method should return as quickly as possible as it will called on every
	 * {@link Diff} of the Comparison.
	 * 
	 * @param input
	 *            the element to test
	 * @return true if this factory handles the given input, false otherwise.
	 */
	boolean handles(Diff input);

	/**
	 * Creates and returns a {@link Diff extension} from the given {@link Diff}. The returned element MUST NOT
	 * be added to its parent, it will be done by the post processor.
	 * 
	 * @param input
	 *            The input difference element.
	 * @return The difference extension.
	 */
	Diff create(Diff input);

	/**
	 * Returns the match in which the difference will be added.
	 * 
	 * @param input
	 *            The input difference element.
	 * @return The difference extension.
	 */
	Match getParentMatch(Diff input);

	/**
	 * Sets the required link of the difference extension created by the related factory.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param extension
	 *            The difference extension.
	 */
	void fillRequiredDifferences(Comparison comparison, Diff extension);
}
