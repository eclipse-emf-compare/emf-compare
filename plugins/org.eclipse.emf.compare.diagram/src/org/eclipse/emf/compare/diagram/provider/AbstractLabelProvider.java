/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Cedric Notot - [374185] Performance issue
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.provider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Provide the label of a {@link View}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractLabelProvider implements IViewLabelProvider {

	/**
	 * Registry which stores text aware edit parts to be re-used if required.
	 * 
	 * @since 1.3
	 */
	protected Map<View, ITextAwareEditPart> registry = new HashMap<View, ITextAwareEditPart>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.IViewLabelProvider#isManaged(org.eclipse.gmf.runtime.notation.View)
	 * @since 1.3
	 */
	public boolean isManaged(View view) {
		boolean result = true;
		EditPart ep = registry.get(view);
		if (ep == null) {
			ep = createEditPart(view);
			if (ep instanceof ITextAwareEditPart) {
				registry.put(view, (ITextAwareEditPart)ep);
			} else {
				if (ep != null)
					ep.deactivate();
				result = false;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.IViewLabelProvider#elementLabel(org.eclipse.gef.GraphicalEditPart)
	 */
	public String elementLabel(View view) {
		if (isManaged(view)) {
			final ITextAwareEditPart ep = registry.get(view);
			return elementLabel(view, ep);
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.IViewLabelProvider#setLabel(org.eclipse.gmf.runtime.notation.View,
	 *      java.lang.String)
	 * @since 1.3
	 */
	public void setLabel(View view, String label) {
		if (isManaged(view)) {
			final ITextAwareEditPart ep = registry.get(view);
			final ICommand iCommand = getDirectEditCommand(ep, label);

			final IWorkbench workbench = PlatformUI.getWorkbench();
			if (workbench != null) {
				final IEditorPart part = workbench.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				final DiagramEditDomain editDomain = new DiagramEditDomain(part);
				editDomain.getCommandStack().execute(new ICommandProxy(iCommand));
			}
		}
	}

	/**
	 * Creates the edit part related to the specified view.
	 * 
	 * @param view
	 *            The view.
	 * @return The edit part.
	 * @since 1.3
	 */
	protected EditPart createEditPart(View view) {
		return null;
	}

	/**
	 * Get the label from the view and its edit part.
	 * 
	 * @param view
	 *            The view.
	 * @param ep
	 *            The edit part.
	 * @return The label.
	 * @since 1.3
	 */
	protected String elementLabel(View view, final ITextAwareEditPart ep) {
		return ""; //$NON-NLS-1$
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.IViewLabelProvider#clear()
	 * @since 1.3
	 */
	public void clear() {
		registry.clear();
	}

	/**
	 * Get the GMF Command from the text edit part. It is inspired from @see
	 * LabelDirectEditPolicy#getDirectEditCommand(DirectEditRequest).
	 * 
	 * @param textEp
	 *            The text edit part.
	 * @param label
	 *            The label to set.
	 * @return the command.
	 */
	private ICommand getDirectEditCommand(ITextAwareEditPart textEp, String label) {
		final EObject model = (EObject)textEp.getModel();
		EObjectAdapter elementAdapter = null;
		if (model instanceof View) {
			final View lview = (View)model;
			elementAdapter = new EObjectAdapterEx(ViewUtil.resolveSemanticElement(lview), lview);
		} else
			elementAdapter = new EObjectAdapterEx(model, null);

		return textEp.getParser().getParseCommand(elementAdapter, label, 0);
	}

	/**
	 * Class inspired from @see LabelDirectEditPolicy.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	class EObjectAdapterEx extends EObjectAdapter {

		/**
		 * The view.
		 */
		private View mView;

		/**
		 * constructor.
		 * 
		 * @param element
		 *            element to be wrapped
		 * @param pView
		 *            view to be wrapped
		 */
		public EObjectAdapterEx(EObject element, View pView) {
			super(element);
			this.mView = pView;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter#getAdapter(java.lang.Class)
		 */
		@Override
		public Object getAdapter(Class adapter) {
			Object result = null;
			final Object o = super.getAdapter(adapter);
			if (o != null)
				result = o;
			else if (adapter.equals(View.class)) {
				result = mView;
			}
			return result;
		}
	}

}
