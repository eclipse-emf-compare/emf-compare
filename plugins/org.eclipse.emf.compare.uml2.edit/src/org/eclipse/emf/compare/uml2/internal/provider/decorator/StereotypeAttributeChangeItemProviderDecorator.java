/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.decorator;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;

/**
 * Item provider decorator for {@link StereotypeAttributeChange}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypeAttributeChangeItemProviderDecorator extends UMLDiffItemProviderDecorator {

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            .
	 */
	public StereotypeAttributeChangeItemProviderDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Object getImage(Object object) {
		final AttributeChange attributeChange = getAttributeChange(object);
		if (attributeChange != null) {
			return getItemDelegator().getImage(attributeChange);
		}
		return super.getImage(object);
	}

	/**
	 * Retrieves the {@link AttributeChange} which is refined by the {@link StereotypeAttributeChange}.
	 * 
	 * @param diff
	 *            Input object. .
	 * @return an {@link AttributeChange} if the input is a {@link StereotypeAttributeChange} which is refined
	 *         by an {@link AttributeChange} or <code>null</code> otherwise.
	 */
	private AttributeChange getAttributeChange(Object diff) {
		AttributeChange attributeChange = null;
		if (diff instanceof StereotypeAttributeChange) {
			StereotypeAttributeChange stereotypeAttributeChange = (StereotypeAttributeChange)diff;
			Optional<Diff> element = Iterables.tryFind(stereotypeAttributeChange.getRefinedBy(),
					Predicates.instanceOf(AttributeChange.class));
			if (element.isPresent()) {
				attributeChange = (AttributeChange)element.get();
			}
		}
		return attributeChange;
	}

}
