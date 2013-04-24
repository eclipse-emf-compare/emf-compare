/*******************************************************************************
 * Copyright (c)  2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.matchs.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This constructs an instance from a factory and a notifier.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramItemProviderSpec extends ViewItemProviderSpec {

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            The adapter factory.
	 */
	public DiagramItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc} <br>
	 * Add the name of the diagram on the label.
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.matchs.provider.spec.ViewItemProviderSpec#getEClassText(org.eclipse.gmf.runtime.notation.View)
	 */
	@Override
	protected String getEClassText(View obj) {
		return super.getEClassText(obj) + " " + ((Diagram)obj).getName();
	}

}
