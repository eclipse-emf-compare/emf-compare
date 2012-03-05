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
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.messages.DiagramCompareUIMessages;
import org.eclipse.emf.compare.diff.provider.DiffEditPlugin;

/**
 * Extension of DiagramEdgeChangeItemProvider.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramEdgeChangeItemProvider extends DiagramEdgeChangeItemProvider {
	/**
	 * Constructor.
	 * 
	 * @param pAdapterFactory
	 *            The adapter factory.
	 */
	public BusinessDiagramEdgeChangeItemProvider(AdapterFactory pAdapterFactory) {
		super(pAdapterFactory);
	}

	@Override
	public String getText(Object object) {
		if (object instanceof BusinessDiagramEdgeChange) {
			return buildMessage((BusinessDiagramEdgeChange)object);
		}
		return super.getText(object);
	}

	@Override
	public Object getImage(Object object) {
		return DiffEditPlugin.INSTANCE.getImage("full/obj16/AttributeChange"); //$NON-NLS-1$
	}

	/**
	 * Build the message of the difference.
	 * 
	 * @param diff
	 *            The difference.
	 * @return The message.
	 */
	private String buildMessage(BusinessDiagramEdgeChange diff) {
		String message;
		if (diff.isConflicting()) {
			message = DiagramCompareUIMessages
					.getString("DiagramCompareDifferences.edgeChangeDescription.conflict"); //$NON-NLS-1$
		} else if (diff.isRemote()) {
			message = DiagramCompareUIMessages
					.getString("DiagramCompareDifferences.edgeChangeDescription.remote"); //$NON-NLS-1$
		} else {
			message = DiagramCompareUIMessages.getString("DiagramCompareDifferences.edgeChangeDescription"); //$NON-NLS-1$
		}
		return message;
	}

}
