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
package org.eclipse.emf.compare.diagram.ecoretools;

import org.eclipse.emf.compare.diagram.provider.AbstractLabelProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecoretools.diagram.edit.parts.EcoreEditPartFactory;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The provider for EcoreTools diagram kind.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class EcoreToolsViewLabelProvider extends AbstractLabelProvider {

	/**
	 * EcoreEditPartFactory.
	 */
	private static final EcoreEditPartFactory EDIT_PART_FACTORY = new EcoreEditPartFactory();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.AbstractLabelProvider#createEditPart(org.eclipse.gmf.runtime.notation.View)
	 */
	@Override
	protected EditPart createEditPart(View view) {
		return EDIT_PART_FACTORY.createEditPart(null, view);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.AbstractLabelProvider#elementLabel(org.eclipse.gmf.runtime.notation.View,
	 *      org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart)
	 */
	@Override
	protected String elementLabel(View view, final ITextAwareEditPart ep) {
		final EObject semanticElement = view.getElement();
		final EObjectAdapter semanticAdapter = new EObjectAdapter(semanticElement);

		final IParser parser = ep.getParser();
		final ParserOptions parserOptions = ep.getParserOptions();

		return parser.getPrintString(semanticAdapter, parserOptions.intValue());
	}

}
