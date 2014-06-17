/**
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal.provider.custom;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.uml2.internal.StereotypedElementChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

/*
 * TODO: Improve label and Implement getImage.
 */
/**
 * Custom item provider for {@link StereotypedElementChange}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */

public class StereotypedElementChangeCustomItemProvider extends UMLDiffCustomItemProvider {

	/**
	 * Transform a {@link Stereotype} into its name.
	 */
	private static final Function<Stereotype, String> TO_STEREOTYPE_NAME = new Function<Stereotype, String>() {

		public String apply(Stereotype input) {
			return input.getName();
		}
	};

	/**
	 * Item delegator.
	 */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use.
	 */
	public StereotypedElementChangeCustomItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	@Override
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		final StereotypedElementChange stereotypedElementChange = (StereotypedElementChange)object;

		EObject base = stereotypedElementChange.getDiscriminant();
		if (!(base instanceof Element)) {
			throw new RuntimeException("The discrimant of a StereotypedElementChange should be a uml Element"); //$NON-NLS-1$
		}
		final String sterotypesNames = Joiner.on(", ").join(//$NON-NLS-1$
				Iterables.transform(((Element)base).getAppliedStereotypes(), TO_STEREOTYPE_NAME));
		final ComposedStyledString stereotypeText = new ComposedStyledString();
		stereotypeText.append("<").append(sterotypesNames).append("> "); //$NON-NLS-1$ //$NON-NLS-2$

		if (base instanceof NamedElement) {
			stereotypeText.append(((NamedElement)base).getName());
		} else {
			// Can't really do more
			stereotypeText.append(itemDelegator.getText(base));
		}

		final String action;
		switch (stereotypedElementChange.getKind()) {
			case ADD:
				action = "add"; //$NON-NLS-1$
				break;
			case DELETE:
				action = "remove"; //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + stereotypedElementChange.getKind()); //$NON-NLS-1$
		}

		return stereotypeText.append(" [" + action + "]", //$NON-NLS-1$ //$NON-NLS-2$
				Style.DECORATIONS_STYLER);
	}

}
