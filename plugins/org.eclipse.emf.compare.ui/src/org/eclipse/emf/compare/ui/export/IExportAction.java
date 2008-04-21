/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.export;

import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.swt.graphics.Image;

/**
 * Describes an action that can be used by EMF Compare "export diff as" menu. EMF Compare "export" extension
 * point requires a class implementing this.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IExportAction {
	/**
	 * This will be called when the described action will be triggered.
	 * <p>
	 * Implementing classes should implement this method as they would implement
	 * {@link org.eclipse.jface.action.Action#run()}.
	 * </p>
	 * 
	 * @param snapshot
	 *            This represents the contents that should be exported.
	 */
	void exportSnapshot(ModelInputSnapshot snapshot);

	/**
	 * Returns the action's display icon when disabled.
	 * 
	 * @return The action's display icon when disabled.
	 * @see org.eclipse.jface.action.Action#setDisabledImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
	 */
	Image getDisabledImage();

	/**
	 * Returns the action's display image while enabled as well as the image that will be displayed when the
	 * mouse hovers over it.
	 * 
	 * @return The action's display image while enabled.
	 * @see org.eclipse.jface.action.Action#setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
	 * @see org.eclipse.jface.action.Action#setHoverImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
	 */
	Image getEnabledImage();

	/**
	 * Defines the action's display text.
	 * 
	 * @return The action's display text.
	 * @see org.eclipse.jface.action.Action#setText(String)
	 */
	String getText();

	/**
	 * Defines the action's tool tip text.
	 * 
	 * @return The action's tool tip text.
	 * @see org.eclipse.jface.action.Action#setToolTipText(String)
	 */
	String getToolTipText();
}
