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
package org.eclipse.emf.compare.ide.ui.internal;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class EMFCompareConstants {

	private EMFCompareConstants() {
		// prevents instantiation
	}

	public static final int COMPARE_IMAGE_WIDTH = 22;

	public static final String INCOMING_COLOR = "INCOMING_COLOR"; //$NON-NLS-1$

	public static final String OUTGOING_COLOR = "OUTGOING_COLOR"; //$NON-NLS-1$

	public static final String CONFLICTING_COLOR = "CONFLICTING_COLOR"; //$NON-NLS-1$

	public static final String RESOLVED_COLOR = "RESOLVED_COLOR"; //$NON-NLS-1$

	// CompareConfiguration

	public static final String COMPARE_RESULT = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARE.RESULT"; //$NON-NLS-1$

	public static final String EDITING_DOMAIN = EMFCompareIDEUIPlugin.PLUGIN_ID + ".EDITING_DOMAIN"; //$NON-NLS-1$

	public static final Predicate<? super Diff> IS_CONFLICT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getConflict() != null;
		}
	};

	public static final Predicate<? super Diff> PSEUDO_CONFLICT_OR_NOT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getConflict() == null || input.getConflict().getKind() == ConflictKind.PSEUDO;
		}
	};

	/**
	 * Returns a {@link Predicate} that filter out {@link Diff} not being in the given <code>state</code>.
	 * 
	 * @param state
	 *            The state in which we want to retain {@link Diff}.
	 * @return the created predicate.
	 */
	public static Predicate<? super Diff> diffWithStates(final DifferenceState state) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input.getState() == state;
			}
		};
	}
}
