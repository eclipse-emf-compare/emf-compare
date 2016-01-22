/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.custom;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.ecore.EObject;

/**
 * Custom {@link UMLDiffCustomItemProvider} for {@link MultiplicityElementChange MultiplicityElementChanges}
 * that handles {@link org.eclipse.emf.compare.provider.IItemStyledLabelProvider} .
 * 
 * @author Alexandra Buzila
 */
public class MultiplicityElementCustomItemProvider extends UMLDiffCustomItemProvider {

	/** Item delegator that reuses the root adapter factory (if any). */
	private ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructs a new instance of the item provider.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public MultiplicityElementCustomItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	@Override
	public IComposedStyledString getStyledText(Object object) {
		MultiplicityElementChange diff = (MultiplicityElementChange)object;
		IComposedStyledString styledText = new ComposedStyledString(getInternalText(diff));
		String changedFeature = getChangedFeatureText(diff);
		styledText.append(" [" + changedFeature, Style.DECORATIONS_STYLER); //$NON-NLS-1$
		switch (diff.getKind()) {
			case ADD:
				styledText.append(" add", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case DELETE:
				styledText.append(" delete", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			case CHANGE:
				styledText.append(" change", Style.DECORATIONS_STYLER); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + diff.getKind()); //$NON-NLS-1$
		}
		styledText.append("]", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		return styledText;

	}

	/**
	 * Provides the name of the MultiplicityElement's structural feature affected by this diff. This will be
	 * either the <code>lowerValue</code> or <code>upperValue</code>.
	 * 
	 * @param diff
	 *            the {@link MultiplicityElementChange}
	 * @return the name of the feature
	 */
	private String getChangedFeatureText(MultiplicityElementChange diff) {
		return diff.getDiscriminant().eContainmentFeature().getName();
	}

	/**
	 * Provides the default label of the given diff.
	 * 
	 * @param diff
	 *            the {@link MultiplicityElementChange} for which the label is returned
	 * @return the label
	 */
	private String getInternalText(MultiplicityElementChange diff) {
		EObject discriminant = diff.getDiscriminant();
		String text = itemDelegator.getText(discriminant);
		if (text == null) {
			text = "<null>"; //$NON-NLS-1$
		}
		return text;
	}
}
