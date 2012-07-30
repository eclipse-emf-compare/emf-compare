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
import org.eclipse.emf.compare.ide.ui.EMFCompareIDEUIPlugin;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IEMFCompareConstants {

	static final String INCOMING_COLOR = "INCOMING_COLOR"; //$NON-NLS-1$

	static final String OUTGOING_COLOR = "OUTGOING_COLOR"; //$NON-NLS-1$

	static final String CONFLICTING_COLOR = "CONFLICTING_COLOR"; //$NON-NLS-1$

	static final String RESOLVED_COLOR = "RESOLVED_COLOR"; //$NON-NLS-1$

	// CompareConfiguration

	static final String MERGE_TIP_RIGHT_TO_LEFT = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".MERGE_TIP_RIGHT_TO_LEFT"; //$NON-NLS-1$

	static final String COMPARE_RESULT = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARE.RESULT"; //$NON-NLS-1$

	static final boolean MERGE_TIP_RIGHT_TO_LEFT_DEFAULT = true;

	static final Predicate<? super Diff> IS_CONFLICT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getConflict() != null;
		}
	};

	static final Predicate<? super Diff> PSEUDO_CONFLICT_OR_NOT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getConflict() == null || input.getConflict().getKind() == ConflictKind.PSEUDO;
		}
	};
}
