/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * An element that can hold a diagnostic.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IDiagnosable {

	/**
	 * Return the diagnostic associated with this scope. For instance, it may contain errors that occurred
	 * during loading of its notifiers.
	 * 
	 * @return the diagnostic
	 */
	Diagnostic getDiagnostic();

	/**
	 * Set the diagnostic to be associated with this scope.
	 * 
	 * @param diagnostic
	 *            the diagnostic
	 */
	void setDiagnostic(Diagnostic diagnostic);
}
