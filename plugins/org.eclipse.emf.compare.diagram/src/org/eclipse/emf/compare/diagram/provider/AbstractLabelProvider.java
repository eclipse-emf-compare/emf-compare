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
package org.eclipse.emf.compare.diagram.provider;

import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Provide the label of a {@link View}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractLabelProvider implements IViewLabelProvider {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.IViewLabelProvider#elementLabel(org.eclipse.gef.GraphicalEditPart)
	 */
	public String elementLabel(View view) {
		if (view == null) {
			throw new IllegalArgumentException("view"); //$NON-NLS-1$
		}
		final ITextAwareEditPart editPart = DiffUtil.getTextEditPart(view);

		final EObject semanticElement = getSemanticElement(editPart);
		final EObjectAdapter semanticAdapter = new EObjectAdapter(semanticElement);

		final IParser parser = DiffUtil.getParser(view);
		final ParserOptions parserOptions = editPart.getParserOptions();

		final String ret = parser.getPrintString(semanticAdapter, parserOptions.intValue());
		return ret;
	}

	/**
	 * Get the semantic element managed by the graphical edit part.
	 * 
	 * @param editPart
	 *            The graphical edit part.
	 * @return The semantic element.
	 */
	protected EObject getSemanticElement(GraphicalEditPart editPart) {
		if (editPart.getModel() instanceof View) {
			final View view = (View)editPart.getModel();
			return view.getElement();
		}
		return null;
	}
}
