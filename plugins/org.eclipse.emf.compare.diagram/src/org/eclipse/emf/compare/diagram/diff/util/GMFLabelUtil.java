/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.util;

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This utility class will be used to request and set labels on GMF views.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public final class GMFLabelUtil {
	// FIXME
	private static Map<Diagram, DiagramEditPart> DIAGRAM_EDIT_PARTS = Maps.newHashMap();

	/**
	 * Constructor.
	 */
	private GMFLabelUtil() {
		// Hides default constructor
	}

	/**
	 * Retrieve the {@link ITextAwareEditPart} related to a view.
	 * 
	 * @param view
	 *            The view.
	 * @return The text editpart.
	 */
	public static ITextAwareEditPart getTextEditPart(View view) {
		final LabelRequestor labelRequestor = new LabelRequestor(view);
		Display.getDefault().syncExec(labelRequestor);
		return labelRequestor.getTextEditPart();
	}

	/**
	 * Retrieve the GMF parser from the specified view.
	 * 
	 * @param view
	 *            The view.
	 * @return The parser.
	 */
	public static IParser getParser(View view) {
		final ParserRequestor parserRequestor = new ParserRequestor(view);
		Display.getDefault().syncExec(parserRequestor);
		return parserRequestor.getParser();
	}

	/**
	 * Set the GMF label of the specified view with the specified label.
	 * 
	 * @param view
	 *            The view.
	 * @param label
	 *            The label.
	 */
	public static void setLabel(View view, String label) {
		final LabelSetter labelSetter = new LabelSetter(view, label);
		Display.getDefault().syncExec(labelSetter);
	}

	public static void cleanup() {
		for (Map.Entry<Diagram, DiagramEditPart> entry : DIAGRAM_EDIT_PARTS.entrySet()) {
			entry.getValue().deactivate();
		}
		DIAGRAM_EDIT_PARTS.clear();
	}

	/**
	 * Retrieve the {@link ITextAwareEditPart} related to the specified {@link IGraphicalEditPart}.
	 * 
	 * @param parent
	 *            The {@link IGraphicalEditPart}
	 * @return The {@link ITextAwareEditPart}
	 */
	private static ITextAwareEditPart findTextAwareEditPart(IGraphicalEditPart parent) {
		ITextAwareEditPart result = null;
		if (parent instanceof ITextAwareEditPart) {
			result = (ITextAwareEditPart)parent;
		} else {
			final EditPart primaryChildEditPart = parent.getPrimaryChildEditPart();
			if (primaryChildEditPart instanceof ITextAwareEditPart) {
				return (ITextAwareEditPart)primaryChildEditPart;
			}
			for (Object obj : parent.getChildren()) {
				if (obj instanceof ITextAwareEditPart) {
					result = (ITextAwareEditPart)obj;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Utility Class to handle labels on GMF views.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public abstract static class AbstractLabelHandling implements Runnable {

		/**
		 * The view to deal with.
		 */
		protected View view;

		/**
		 * The result of the operation of getting and setting label.
		 */
		protected String label = ""; //$NON-NLS-1$

		/**
		 * The editpart of the label.
		 */
		protected ITextAwareEditPart mTextEp;

		/**
		 * Constructor.
		 * 
		 * @param pView
		 *            The view.
		 */
		public AbstractLabelHandling(View pView) {
			this.view = pView;
		}

		/**
		 * Constructor.
		 * 
		 * @param pView
		 *            The view.
		 * @param pLabel
		 *            The label.
		 */
		public AbstractLabelHandling(View pView, String pLabel) {
			this.view = pView;
			this.label = pLabel;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final Diagram diagram = view.getDiagram();

			final ResourceSet resourceSet = diagram.eResource().getResourceSet();
			final TransactionalEditingDomain ted = TransactionalEditingDomain.Factory.INSTANCE
					.getEditingDomain(resourceSet);
			if (ted == null) {
				TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);
			}

			DiagramEditPart diagramEditPart = DIAGRAM_EDIT_PARTS.get(diagram);
			if (diagramEditPart == null) {
				Shell shell = null;
				try {
					shell = new Shell();
					diagramEditPart = OffscreenEditPartFactory.getInstance().createDiagramEditPart(diagram,
							shell);
					DIAGRAM_EDIT_PARTS.put(diagram, diagramEditPart);
				} finally {
					if (shell != null) {
						shell.dispose();
					}
				}
			}

			if (diagramEditPart != null) {
				final Object viewEp = diagramEditPart.getViewer().getEditPartRegistry().get(view);

				if (viewEp instanceof IGraphicalEditPart) {
					final ITextAwareEditPart textEp = findTextAwareEditPart((IGraphicalEditPart)viewEp);

					if (textEp != null) {
						this.mTextEp = textEp;
						handle(textEp);
					}
				}
			}
		}

		/**
		 * Deals with the specified label editpart.
		 * 
		 * @param editPart
		 *            The editpart.
		 */
		abstract void handle(ITextAwareEditPart editPart);

		public ITextAwareEditPart getTextEditPart() {
			return mTextEp;
		}

	}

	/**
	 * Utility Class to retrieve the label from a GMF view.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public static final class LabelRequestor extends AbstractLabelHandling {

		/**
		 * Constructor.
		 * 
		 * @param pView
		 *            The view.
		 */
		public LabelRequestor(View pView) {
			super(pView);
		}

		/**
		 * Get the label.
		 * 
		 * @return the label;
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diff.util.GMFLabelUtil.LabelHandling#handle(org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart)
		 */
		@Override
		void handle(ITextAwareEditPart editPart) {
			label = editPart.getEditText();
		}

	}

	/**
	 * Utility Class to retrieve the parser from a GMF view.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public static final class ParserRequestor extends AbstractLabelHandling {

		/**
		 * The parser.
		 */
		private IParser parser;

		/**
		 * Constructor.
		 * 
		 * @param pView
		 *            The view.
		 */
		public ParserRequestor(View pView) {
			super(pView);
		}

		/**
		 * Get the parser.
		 * 
		 * @return the parser;
		 */
		public IParser getParser() {
			return parser;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void handle(ITextAwareEditPart editPart) {
			parser = editPart.getParser();
		}

	}

	/**
	 * Utility Class to set the label from a GMF view (and to impact on the semantic objects).
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public static final class LabelSetter extends AbstractLabelHandling {

		/**
		 * Constructor.
		 * 
		 * @param pView
		 *            The view.
		 * @param pLabel
		 *            The label.
		 */
		public LabelSetter(View pView, String pLabel) {
			super(pView, pLabel);
		}

		@Override
		void handle(ITextAwareEditPart editPart) {
			final ICommand iCommand = getDirectEditCommand(editPart);

			final CommandStack stack = editPart.getViewer().getEditDomain().getCommandStack();
			stack.execute(new ICommandProxy(iCommand));
		}

		/**
		 * Get the GMF Command from the text edit part. It is inspired from @see
		 * LabelDirectEditPolicy#getDirectEditCommand(DirectEditRequest).
		 * 
		 * @param textEp
		 *            The text edit part.
		 * @return the command.
		 */
		private ICommand getDirectEditCommand(ITextAwareEditPart textEp) {
			final EObject model = (EObject)textEp.getModel();
			EObjectAdapter elementAdapter = null;
			if (model instanceof View) {
				final View lview = (View)model;
				elementAdapter = new EObjectAdapterEx(ViewUtil.resolveSemanticElement(lview), lview);
			} else {
				elementAdapter = new EObjectAdapterEx(model, null);
			}

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
				if (o != null) {
					result = o;
				} else if (adapter.equals(View.class)) {
					result = mView;
				}
				return result;
			}
		}

	}

}
