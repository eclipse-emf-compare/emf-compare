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
package org.eclipse.emf.compare.logical.ui;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * Overrides EMF Compare's {@link ModelCompareInput} in order to return properly typed elements so that
 * platform compare can find the accurate structure and content viewers.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class LogicalModelCompareInput extends ModelCompareInput {
	/**
	 * Instantiates our compare input given its comparison result.
	 * 
	 * @param snapshot
	 *            Result of the comparison we wish to display.
	 */
	public LogicalModelCompareInput(ComparisonResourceSetSnapshot snapshot) {
		super(snapshot);
	}

	/**
	 * Instantiates our compare input given its comparison result.
	 * 
	 * @param snapshot
	 *            Result of the comparison we wish to display.
	 */
	public LogicalModelCompareInput(ComparisonResourceSnapshot snapshot) {
		super(snapshot);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.ModelCompareInput#createTypedElement(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected ITypedElement createTypedElement(EObject eObject) {
		return new EObjectTypedElement(eObject, new AdapterFactoryLabelProvider(
				AdapterUtils.getAdapterFactory()));
	}
}
