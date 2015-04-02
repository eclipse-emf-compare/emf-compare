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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;

/**
 * Encapsulated a diagnostic to hide multi-threaded details.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DiagnosticSupport {

	/** The wrapped diagnostic, never {@code null}. */
	private final BasicDiagnostic diagnostic;

	/**
	 * Constructor.
	 * 
	 * @param diagnostic
	 *            The diagnostic to wrap, must not be {@code null}.
	 */
	public DiagnosticSupport(BasicDiagnostic diagnostic) {
		this.diagnostic = checkNotNull(diagnostic);
	}

	/**
	 * Constructor, will instantiate a default diagnostic.
	 */
	public DiagnosticSupport() {
		this.diagnostic = createDiagnostic();
	}

	public BasicDiagnostic getDiagnostic() {
		return diagnostic;
	}

	/**
	 * Creates the BasicDiagnostic that will be used by this computation. Can be overridden if necessary.
	 * 
	 * @return A new empty BasicDiagnostic.
	 */
	protected BasicDiagnostic createDiagnostic() {
		return new BasicDiagnostic(EMFCompareIDEUIPlugin.PLUGIN_ID, 0, null, new Object[0]);
	}

	/**
	 * Thread safely merge the given diagnostic to the {@link #diagnostic} field.
	 * 
	 * @param resourceDiagnostic
	 *            the diagnostic to be added to the global diagnostic.
	 */
	public void merge(final Diagnostic resourceDiagnostic) {
		synchronized(diagnostic) {
			diagnostic.merge(resourceDiagnostic);
		}
	}
}
