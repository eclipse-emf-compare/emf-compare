/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.diagram.ui.decoration.provider;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diagram.ui.decoration.DiffEdgeDecorator;
import org.eclipse.emf.compare.diagram.ui.decoration.DiffLabelDecorator;
import org.eclipse.emf.compare.diagram.ui.decoration.DiffNodeDecorator;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The graphical decorator for comparison.
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public class DiffDecoratorProvider extends AbstractProvider implements IDecoratorProvider {

	/** The decorator marker for diff. used also to annotate the diagrams during comparison. */
	public static final String DIFF = "diff-marker";

	/** Constant for added element. */
	public static final String DIFF_ADDED = "diff-added";

	/** Constant for removed element. */
	public static final String DIFF_REMOVED = "diff-removed";

	/** Constant for moved element. */
	public static final String DIFF_MOVED = "diff-moved";

	/** Constant for hided element. */
	public static final String DIFF_HIDED = "diff-hided";

	/** Constant for showed element. */
	public static final String DIFF_SHOWED = "diff-showed";

	/** Constant for modified element. */
	public static final String DIFF_MODIFIED = "diff-modified";

	/** Constant for modified element. */
	public static final String DIFF_LABEL_MODIFIED = "diff-label-modified";

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof CreateDecoratorsOperation) {
			final IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation)operation)
					.getDecoratorTarget();
			return shouldDecorate(decoratorTarget);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider#createDecorators(org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget)
	 */
	public void createDecorators(IDecoratorTarget decoratorTarget) {
		final View view = (View)decoratorTarget.getAdapter(View.class);
		if (shouldDecorate(view)) {
			final ITextAwareEditPart label = (ITextAwareEditPart)decoratorTarget
					.getAdapter(ITextAwareEditPart.class);

			if (label != null && isNode(view)) {
				// a label = a node representation + a textholder
				decoratorTarget.installDecorator(DIFF, new DiffLabelDecorator(decoratorTarget));
			} else if (isEdge(view)) {
				decoratorTarget.installDecorator(DIFF, new DiffEdgeDecorator(decoratorTarget));
			} else if (isNode(view)) {
				decoratorTarget.installDecorator(DIFF, new DiffNodeDecorator(decoratorTarget));
			} else {
				EMFComparePlugin.log("Unable to decorate target " + view, false);
			}
		}
	}

	/**
	 * Check the EAnnotations list of the view to find the diff one.
	 * 
	 * @param decoratorTarget
	 *            the target to test
	 * @return true if target should be decorated ( diff EAnnotation is set )
	 */
	public static boolean shouldDecorate(IDecoratorTarget decoratorTarget) {
		return shouldDecorate((View)decoratorTarget.getAdapter(View.class));
	}

	/**
	 * Check the EAnnotations list of the view to find the diff one.
	 * 
	 * @param view
	 *            the view element to check
	 * @return true if view should be decorated ( diff EAnnotation is set )
	 */
	public static boolean shouldDecorate(final View view) {
		return !(view instanceof Diagram) && (view.getEAnnotation(DIFF) != null);
	}

	/**
	 * Check if the view is a link representation.
	 * 
	 * @param view
	 *            the gmf view
	 * @return true if the view is an edge
	 */
	private static boolean isEdge(View view) {
		return view != null && (view instanceof Edge) && view.eContainer() instanceof Diagram;
	}

	/**
	 * Check if the view is a node representation.
	 * 
	 * @param view
	 *            the gmf view
	 * @return true if the view is a node
	 */
	private static boolean isNode(View view) {
		return view != null && (view instanceof Node);
	}

}
