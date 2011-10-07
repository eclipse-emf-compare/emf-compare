/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
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
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility Class for differences requests.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public final class DiffUtil {

	/**
	 * Constructor.
	 */
	private DiffUtil() {
	}

	/**
	 * Enumerated type to specify which side from a difference we wish to address.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public enum Side {
		/**
		 * Any side.
		 */
		ANY,
		/**
		 * Left side.
		 */
		LEFT,
		/**
		 * Right side.
		 */
		RIGHT;
	}

	/**
	 * Checks if the view is visible.
	 * 
	 * @param view
	 *            The tested view.
	 * @return True if visible.
	 */
	public static boolean isVisible(View view) {
		boolean result = view.isVisible();
		if (result) {
			final View container = getNextParent(view);
			if (container != null) {
				result = isVisible(container);
			}
		}
		return result;
	}

	/**
	 * Get the closest parent view.
	 * 
	 * @param obj
	 *            The current view.
	 * @return The parent view.
	 */
	private static View getNextParent(EObject obj) {
		View result = null;
		if (obj != null) {
			final EObject parent = obj.eContainer();
			if (parent instanceof View) {
				result = (View)parent;
			} else {
				result = getNextParent(parent);
			}
		}
		return result;
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
	 * Retrieve the GMF label of the specified view.
	 * 
	 * @param view
	 *            The view.
	 * @return The label.
	 */
	public static String getLabel(View view) {
		final LabelRequestor labelRequestor = new LabelRequestor(view);
		Display.getDefault().syncExec(labelRequestor);
		return labelRequestor.getLabel();
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
	 * Retrieve the first ancestor of the specified obj where the type is an instance of clazz.
	 * 
	 * @param obj
	 *            The object from which the scan is processed.
	 * @param clazz
	 *            The type to check.
	 * @param <T>
	 *            The type specified by clazz.
	 * @return The ancestor or null if not found.
	 */
	public static <T> T eContainer(EObject obj, Class<T> clazz) {
		T result = null;
		if (obj != null) {
			if (clazz.isAssignableFrom(obj.getClass())) {
				return (T)obj;
			}
			if (obj.eContainer() != null && clazz.isAssignableFrom(obj.eContainer().getClass()))
				result = (T)obj.eContainer();
			else
				result = eContainer(obj.eContainer(), clazz);
		}
		return result;
	}

	/**
	 * Checks if the specified difference group already contains a difference, instance of clazz, for the
	 * specified elementToCOmpare.
	 * 
	 * @param root
	 *            The difference group.
	 * @param elementToCompare
	 *            The object to check.
	 * @param clazz
	 *            The type of difference elements to consider.
	 * @param <T>
	 *            The type specified by clazz.
	 * @return True if the occurrence exists.
	 */
	public static <T> boolean containsAnInstanceFor(DiffGroup root, EObject elementToCompare, Class<T> clazz) {
		if (elementToCompare != null) {
			final Iterator<EObject> it = root.eAllContents();
			while (it.hasNext()) {
				final EObject obj = it.next();
				if (obj instanceof DiffElement) {
					final DiffElement diff = (DiffElement)obj;
					if (elementToCompare.equals(getElement(diff, Side.ANY, Edge.class))
							&& clazz.isInstance(diff))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the object referenced by the specified difference diff, from the specified side.
	 * 
	 * @param diff
	 *            The difference.
	 * @param side
	 *            The side.
	 * @return The object.
	 */
	public static EObject getProperty(final DiffElement diff, final Side side) {
		final DiffSwitch<EObject> eltRequest = new ElementRequestor(side);
		return eltRequest.doSwitch(diff);
	}

	/**
	 * Get the root container (instance of clazz) of the element referenced by the difference diff, from the
	 * specified side.
	 * 
	 * @param diff
	 *            The difference.
	 * @param side
	 *            The side.
	 * @param clazz
	 *            The type of the element to find.
	 * @param <T>
	 *            The type specified by clazz.
	 * @return The element.
	 */
	public static <T> T getElement(DiffElement diff, Side side, Class<T> clazz) {
		return eContainer(getProperty(diff, side), clazz);
	}

	/**
	 * Get all the differences to hide.
	 * 
	 * @param theDiff
	 *            The reference difference.
	 * @param crossReferencer
	 *            The DiffModel cross referencer.
	 * @return List of differences.
	 */
	public static List<ModelElementChange> getDiffs(ModelElementChange theDiff,
			EcoreUtil.CrossReferencer crossReferencer) {
		final List<ModelElementChange> diffs = new ArrayList<ModelElementChange>();
		final ModelElementChange semanticDiff = getSemanticDiff(theDiff, ModelElementChange.class,
				crossReferencer);
		if (semanticDiff != null) {
			diffs.add(semanticDiff);
		}
		diffs.add(theDiff);
		return diffs;
	}

	/**
	 * Get all the differences to hide.
	 * 
	 * @param theDiff
	 *            The reference difference.
	 * @return List of differences.
	 */
	public static List<ModelElementChange> getDiffs(ModelElementChange theDiff) {
		final List<ModelElementChange> diffs = new ArrayList<ModelElementChange>();
		if (theDiff instanceof DiagramModelElementChange) {
			final ModelElementChange semanticDiff = ((DiagramModelElementChange)theDiff).getSemanticDiff();
			if (semanticDiff != null) {
				diffs.add(semanticDiff);
			}
		}
		diffs.add(theDiff);
		return diffs;
	}

	/**
	 * Get the most relevant semantic difference from the specified difference notationalDiff.
	 * 
	 * @param <T>
	 *            The type specified by type.
	 * @param notationalDiff
	 *            The difference.
	 * @param type
	 *            The instance of the difference which is looked for.
	 * @param crossReferencer
	 *            The DiffModel cross referencer.
	 * @return The semantic difference or null if not found.
	 */
	public static <T> T getSemanticDiff(EObject notationalDiff, Class<T> type, CrossReferencer crossReferencer) {
		if (notationalDiff instanceof DiffElement) {
			final View notationalElement = getElement((DiffElement)notationalDiff, Side.ANY, View.class);
			final EObject semanticElement = notationalElement.getElement();
			if (crossReferencer != null) {
				final Collection<Setting> diffs = crossReferencer.get(semanticElement);
				if (diffs != null) {
					for (Setting setting : diffs) {
						final EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
						if ((eStructuralFeature == DiffPackage.eINSTANCE
								.getModelElementChangeLeftTarget_LeftElement() || eStructuralFeature == DiffPackage.eINSTANCE
								.getModelElementChangeRightTarget_RightElement())
								&& type.isInstance(setting.getEObject())) {
							return (T)setting.getEObject();
						}
					}
				}
			}

		}
		return null;
	}

	/**
	 * Get the sub difference elements of the specified difference, except conflicting groups.
	 * 
	 * @param diffElt
	 *            The difference.
	 * @return The sub difference elements.
	 */
	public static List<DiffElement> getSubDiffElements(DiffElement diffElt) {
		final List<DiffElement> result = new ArrayList<DiffElement>();
		for (DiffElement diff : diffElt.getSubDiffElements()) {
			if (diff instanceof ConflictingDiffElement) {
				result.addAll(getSubDiffElements(diff));
			} else {
				result.add(diff);
			}
		}
		return result;
	}

	/**
	 * Checks if a set of differences is considered as remote.
	 * 
	 * @param hiddenDiffs
	 *            The differences.
	 * @return true if it is remote.
	 */
	public static boolean isRemote(List<DiffElement> hiddenDiffs) {
		for (DiffElement diffElt : hiddenDiffs) {
			if (!diffElt.isRemote()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Utility Class to retrieve right or left element according to the kind of difference element.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public static class ElementRequestor extends DiffSwitch<EObject> {

		/**
		 * Requested side.
		 */
		private Side side;

		/**
		 * Constructor.
		 * 
		 * @param pSide
		 *            requested side to find the element.
		 */
		public ElementRequestor(Side pSide) {
			this.side = pSide;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseAttributeChange(org.eclipse.emf.compare.diff.metamodel.AttributeChange)
		 */
		@Override
		public EObject caseAttributeChange(AttributeChange object) {
			EObject result = null;
			final EObject left = object.getLeftElement();
			final EObject right = object.getRightElement();
			switch (side) {
				case LEFT:
					result = left;
					break;
				case RIGHT:
					result = right;
					break;
				case ANY:
				default:
					if (left == null)
						result = right;
					else
						result = left;
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseAttributeChangeLeftTarget(org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget)
		 */
		@Override
		public EObject caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
			return caseAttributeChange(object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseAttributeChangeRightTarget(org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget)
		 */
		@Override
		public EObject caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
			return caseAttributeChange(object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseUpdateAttribute(org.eclipse.emf.compare.diff.metamodel.UpdateAttribute)
		 */
		@Override
		public EObject caseUpdateAttribute(UpdateAttribute object) {
			return caseAttributeChange(object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseConflictingDiffElement(org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement)
		 */
		@Override
		public EObject caseConflictingDiffElement(ConflictingDiffElement object) {
			return object.getOriginElement();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseModelElementChangeLeftTarget(org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget)
		 */
		@Override
		public EObject caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
			return object.getLeftElement();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseModelElementChangeRightTarget(org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget)
		 */
		@Override
		public EObject caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
			return object.getRightElement();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseUpdateModelElement(org.eclipse.emf.compare.diff.metamodel.UpdateModelElement)
		 */
		@Override
		public EObject caseUpdateModelElement(UpdateModelElement object) {
			EObject result = null;
			final EObject left = object.getLeftElement();
			final EObject right = object.getRightElement();
			switch (side) {
				case LEFT:
					result = left;
					break;
				case RIGHT:
					result = right;
					break;
				case ANY:
				default:
					if (left == null)
						result = right;
					else
						result = left;
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseReferenceChange(org.eclipse.emf.compare.diff.metamodel.ReferenceChange)
		 */
		@Override
		public EObject caseReferenceChange(ReferenceChange object) {
			EObject result = null;
			final EObject left = object.getLeftElement();
			final EObject right = object.getRightElement();
			switch (side) {
				case LEFT:
					result = left;
					break;
				case RIGHT:
					result = right;
					break;
				case ANY:
				default:
					if (left == null)
						result = right;
					else
						result = left;
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseReferenceChangeLeftTarget(org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget)
		 */
		@Override
		public EObject caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
			return caseReferenceChange(object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseReferenceChangeRightTarget(org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget)
		 */
		@Override
		public EObject caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
			return caseReferenceChange(object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseReferenceOrderChange(org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange)
		 */
		@Override
		public EObject caseReferenceOrderChange(ReferenceOrderChange object) {
			return caseReferenceChange(object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch#caseUpdateReference(org.eclipse.emf.compare.diff.metamodel.UpdateReference)
		 */
		@Override
		public EObject caseUpdateReference(UpdateReference object) {
			return caseReferenceChange(object);
		}
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
					.createEditingDomain(resourceSet);

			final DiagramEditPart diagEp = OffscreenEditPartFactory.getInstance().createDiagramEditPart(
					diagram, new Shell());

			if (diagEp != null) {

				final Object viewEp = diagEp.getViewer().getEditPartRegistry().get(view);
				if (viewEp != null) {
					if (viewEp instanceof IGraphicalEditPart) {

						final ITextAwareEditPart textEp = findTextAwareEditPart((IGraphicalEditPart)viewEp);
						if (textEp != null) {
							this.mTextEp = textEp;
							handle(textEp);
							textEp.deactivate();
						}
						((IGraphicalEditPart)viewEp).deactivate();
					}
				}

				diagEp.deactivate();
			}
			ted.dispose();
			resourceSet.eAdapters().remove(ted);
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
		 * @see org.eclipse.emf.compare.diagram.diff.util.DiffUtil.LabelHandling#handle(org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart)
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

}
