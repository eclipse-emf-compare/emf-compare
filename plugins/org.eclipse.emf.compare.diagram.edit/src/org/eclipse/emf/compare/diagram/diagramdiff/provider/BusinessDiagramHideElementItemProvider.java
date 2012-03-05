/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.messages.DiagramCompareUIMessages;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Extension of DiagramHideElementItemProvider.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramHideElementItemProvider extends DiagramHideElementItemProvider {
	/**
	 * Constructor.
	 * 
	 * @param pAdapterFactory
	 *            The adapter factory.
	 */
	public BusinessDiagramHideElementItemProvider(AdapterFactory pAdapterFactory) {
		super(pAdapterFactory);
	}

	@Override
	public String getText(Object object) {

		if (object instanceof BusinessDiagramShowHideElement) {
			final BusinessDiagramShowHideElement diff = (BusinessDiagramShowHideElement)object;

			final View view = diff.getLeftView();

			final String label = AdapterUtils.getItemProviderText(view.getElement());

			return buildMessage(diff, label);
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
	private String buildMessage(BusinessDiagramShowHideElement diff, String label) {
		String message;
		if (diff.isRemote()) {
			message = DiagramCompareUIMessages.getString(
					"DiagramCompareDifferences.hideDescription.remote", label); //$NON-NLS-1$
		} else {
			message = DiagramCompareUIMessages.getString("DiagramCompareDifferences.hideDescription", label); //$NON-NLS-1$
		}
		return message;
	}
}
