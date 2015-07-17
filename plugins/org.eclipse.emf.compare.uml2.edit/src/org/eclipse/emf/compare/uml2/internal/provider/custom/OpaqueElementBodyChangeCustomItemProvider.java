/*******************************************************************************
 * Copyright (c) 2014, 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.custom;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;

/**
 * A custom {@link org.eclipse.emf.compare.uml2.internal.provider.OpaqueElementBodyChangeItemProvider} that
 * handles {@link org.eclipse.emf.compare.provider.IItemStyledLabelProvider} and
 * {@link org.eclipse.emf.compare.provider.IItemDescriptionProvider}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyChangeCustomItemProvider extends UMLDiffCustomItemProvider {

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * This constructs an instance from a factory and a notifier.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public OpaqueElementBodyChangeCustomItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)object;
		final EObject discriminant = bodyChange.getDiscriminant();

		final ComposedStyledString text = doGetSemanticObjectLabel(bodyChange, discriminant);

		final String opaqueElementType = getOpaqueElementTypeLabel(discriminant);
		return text.append(" [" + opaqueElementType + " body change]", Style.DECORATIONS_STYLER);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider#getSemanticObjectLabel(java.lang.Object)
	 */
	@Override
	public String getSemanticObjectLabel(Object object) {
		final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)object;
		final EObject discriminant = bodyChange.getDiscriminant();
		return doGetSemanticObjectLabel(bodyChange, discriminant).getString();
	}

	/**
	 * Compute the label for the given bodyChange object.
	 * 
	 * @param bodyChange
	 *            The given object
	 * @param discriminant
	 *            The discriminant of the bodyChange
	 * @return the label
	 */
	private ComposedStyledString doGetSemanticObjectLabel(OpaqueElementBodyChange bodyChange,
			EObject discriminant) {
		final ComposedStyledString text = new ComposedStyledString();
		text.append(bodyChange.getLanguage());
		text.append(" body of "); //$NON-NLS-1$
		text.append(itemDelegator.getText(discriminant));
		text.append(" "); //$NON-NLS-1$
		text.append(getChangeKindLabel(bodyChange));

		return text;
	}

	/**
	 * Returns a label text for the change kind of the given {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to get the change kind label for.
	 * @return The text for the label of the change kind of {@code bodyChange}.
	 */
	private String getChangeKindLabel(OpaqueElementBodyChange bodyChange) {
		final String changeKindLabel;
		switch (bodyChange.getKind()) {
			case ADD:
				changeKindLabel = "added";
				break;
			case DELETE:
				changeKindLabel = "deleted";
				break;
			case MOVE:
				changeKindLabel = "moved";
				break;
			case CHANGE: //$FALL-THROUGH$
			default:
				changeKindLabel = "changed";
				break;
		}
		return changeKindLabel;
	}

	/**
	 * Returns the label text for the opaque element type of the given {@code element}.
	 * <p>
	 * If {@code element} is not an {@link OpaqueAction}, an {@link OpaqueBehavior}, or an
	 * {@link OpaqueExpression}, something went wrong before hand. So this method will return an empty string
	 * in this case.
	 * </p>
	 * 
	 * @param element
	 *            The element to get the type label for.
	 * @return The label for the opaque element type.
	 */
	private String getOpaqueElementTypeLabel(EObject element) {
		final String opaqueElementTypeLabel;
		if (element instanceof OpaqueAction) {
			opaqueElementTypeLabel = "opaque action";
		} else if (element instanceof OpaqueBehavior) {
			opaqueElementTypeLabel = "opaque behavior";
		} else if (element instanceof OpaqueExpression) {
			opaqueElementTypeLabel = "opaque expression";
		} else {
			opaqueElementTypeLabel = "";
		}
		return opaqueElementTypeLabel;
	}

}
