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
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.swt.graphics.Image;

/**
 * This interface represents an EMF Compare "group" of differences that can be displayed in the structural
 * differences viewer of the UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see DefaultDifferenceGroup
 */
public interface DifferenceGroup {
	/**
	 * Returns all differences that should be considered a part of this group.
	 * 
	 * @return All differences that should be considered a part of this group.
	 */
	Iterable<? extends Diff> getDifferences();

	/**
	 * Returns the {@link Comparison} in which this group is defined.
	 * 
	 * @return The {@link Comparison} in which this group is defined.
	 */
	Comparison getComparison();

	/**
	 * A human-readable label for this group. This will be displayed in the EMF Compare UI.
	 * 
	 * @return A human-readable label for this group that can be displayed to the user.
	 */
	String getName();

	/**
	 * The icon that is to be used for this group in the compare UI.
	 * 
	 * @return Icon that is to be used for this group in the compare UI. If {@code null}, a default image will
	 *         be used instead.
	 */
	Image getImage();
}
