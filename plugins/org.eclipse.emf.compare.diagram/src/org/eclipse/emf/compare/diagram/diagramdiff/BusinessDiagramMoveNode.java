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
package org.eclipse.emf.compare.diagram.diagramdiff;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * Overriding of {@link DiagramMoveNode} for specific interface.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public interface BusinessDiagramMoveNode extends DiagramMoveNode, BusinessDiffExtension {

	/**
	 * Get the differences hidden by this extension.
	 * 
	 * @return The list of the hidden differences.
	 */
	List<UpdateAttribute> getUpdateAttributeLocationDiffs();

	/**
	 * Get the left node.
	 * 
	 * @return The node.
	 */
	Node getLeftNode();

	/**
	 * Get the right node.
	 * 
	 * @return The node.
	 */
	Node getRightNode();

	/**
	 * Get the left x coordinates.
	 * 
	 * @return left X.
	 */
	int getLeftLocationX();

	/**
	 * Get the left y coordinates.
	 * 
	 * @return left Y.
	 */
	int getLeftLocationY();

	/**
	 * Get the right x location.
	 * 
	 * @return right X.
	 */
	int getRightLocationX();

	/**
	 * Get the right y location.
	 * 
	 * @return right Y.
	 */
	int getRightLocationY();

}
