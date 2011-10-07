/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.internal;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;

/**
 * An {@link IDiffExtensionFactory} is a factory capable to create an {@link AbstractDiffExtension} from a
 * {@link DiffElement} if and only if this factory can {@link #handles(DiffElement) handle} the given
 * {@link DiffElement}.
 * <p>
 * A factory must be able to say in which parent a {@link AbstractDiffExtension} must be attached if it
 * handles the {@link DiffElement} from which it has been {@link #create(DiffElement) created}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IDiffExtensionFactory {

	/**
	 * Returns true if this factory handles the given kind of DiffElement, i.e., if it can create an
	 * {@link AbstractDiffExtension}.
	 * <p>
	 * <b>Performance note: </b> this method should return as quickly as possible as it will called on every
	 * {@link DiffElement} of the DiffModel.
	 * 
	 * @param input
	 *            the element to test
	 * @param root
	 *            the diff model root
	 * @return true if this factory handles the given input, false otherwise.
	 */
	boolean handles(DiffElement input, DiffGroup root);

	/**
	 * Creates and returns an {@link AbstractDiffExtension} from the given {@link DiffElement}. The returned
	 * element MUST NOT be added to its parent, it will be done by the {@link UML2DiffEngine}.
	 * 
	 * @param input
	 *            the input difference
	 * @return The extension
	 */
	AbstractDiffExtension create(DiffElement input);

	/**
	 * Returns the {@link DiffElement} in which the {@link #create(DiffElement) created}
	 * {@link AbstractDiffExtension} will be added as a sub diff element. This {@link DiffElement} can be from
	 * the model or newly created.
	 * 
	 * @param input
	 *            the input difference
	 * @return the parent difference
	 */
	DiffElement getParentDiff(DiffElement input);

}
