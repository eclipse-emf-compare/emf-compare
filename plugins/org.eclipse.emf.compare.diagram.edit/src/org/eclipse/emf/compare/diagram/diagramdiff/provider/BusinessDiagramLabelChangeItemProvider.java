/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diagramdiff.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.messages.DiagramCompareUIMessages;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Extension of DiagramLabelChangeItemProvider.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
// CHECKSTYLE:OFF
public class BusinessDiagramLabelChangeItemProvider extends DiagramLabelChangeItemProvider {
	// CHECKSTYLE:ON
	/**
	 * Constructor.
	 * 
	 * @param pAdapterFactory
	 *            The adapter factory.
	 */
	public BusinessDiagramLabelChangeItemProvider(AdapterFactory pAdapterFactory) {
		super(pAdapterFactory);
	}

	@Override
	public String getText(Object object) {

		if (object instanceof BusinessDiagramLabelChange) {
			final BusinessDiagramLabelChange diff = (BusinessDiagramLabelChange)object;

			final EObject view = diff.getLeftElement();
			if (view instanceof View) {
				final String label = AdapterUtils.getItemProviderText(((View)view).getElement());
				return buildMessage(diff, label);
			}

		}
		return super.getText(object);

	}

	/**
	 * Build the message of the difference.
	 * 
	 * @param diff
	 *            The difference.
	 * @param label
	 *            The label of the element being compared.
	 * @return The message.
	 */
	private String buildMessage(BusinessDiagramLabelChange diff, String label) {
		String message;
		if (diff.isConflicting()) {
			message = DiagramCompareUIMessages.getString(
					"DiagramCompareDifferences.changeLabelDescription.conflict", label); //$NON-NLS-1$
		} else if (diff.isRemote()) {
			message = DiagramCompareUIMessages.getString(
					"DiagramCompareDifferences.changeLabelDescription.remote", //$NON-NLS-1$
					label, diff.getLeftLabel(), diff.getRightLabel());
		} else {
			message = DiagramCompareUIMessages.getString(
					"DiagramCompareDifferences.changeLabelDescription", label, //$NON-NLS-1$
					diff.getRightLabel(), diff.getLeftLabel());
		}
		return message;
	}
}
