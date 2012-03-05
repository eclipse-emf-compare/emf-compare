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
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramMoveNode;
import org.eclipse.emf.compare.diagram.diagramdiff.messages.DiagramCompareUIMessages;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * Extension of DiagramMoveNodeItemProvider.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramMoveNodeItemProvider extends DiagramMoveNodeItemProvider {
	/**
	 * Constructor.
	 * 
	 * @param pAdapterFactory
	 *            The adapter factory.
	 */
	public BusinessDiagramMoveNodeItemProvider(AdapterFactory pAdapterFactory) {
		super(pAdapterFactory);
	}

	@Override
	public String getText(Object object) {

		if (object instanceof BusinessDiagramMoveNode) {
			final BusinessDiagramMoveNode diff = (BusinessDiagramMoveNode)object;

			final Node node = diff.getLeftNode();

			final String label = AdapterUtils.getItemProviderText(node.getElement());

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
	private String buildMessage(BusinessDiagramMoveNode diff, String label) {
		String message;
		String message2;
		if (diff.isConflicting()) {
			message2 = DiagramCompareUIMessages
					.getString("DiagramCompareDifferences.moveDescription.conflict"); //$NON-NLS-1$
		} else {
			message2 = DiagramCompareUIMessages.getString(
					"DiagramCompareDifferences.moveDescription.noConflict", diff.getRightLocationX(), //$NON-NLS-1$
					diff.getRightLocationY(), diff.getLeftLocationX(), diff.getRightLocationY());
		}
		if (diff.isRemote()) {
			message = DiagramCompareUIMessages.getString("DiagramCompareDifferences.moveDescription.remote", //$NON-NLS-1$
					label, message2);
		} else {
			message = DiagramCompareUIMessages.getString("DiagramCompareDifferences.moveDescription", label, //$NON-NLS-1$
					message2);
		}
		return message;
	}

}
