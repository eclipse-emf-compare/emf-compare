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

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;

/**
 * Item provider decorator for {@link StereotypeReferenceChange}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypeReferenceChangeItemProviderDecorator extends UMLDiffItemProviderDecorator {

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            {@link ComposeableAdapterFactory}
	 */
	public StereotypeReferenceChangeItemProviderDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Object getImage(Object object) {
		final ReferenceChange referenceChange = getReferenceChange(object);
		if (referenceChange != null) {
			return getItemDelegator().getImage(referenceChange);
		}
		return super.getImage(object);
	}

	/**
	 * Retrieves the {@link ReferenceChange} which is refined by the {@link StereotypeReferenceChange}.
	 * 
	 * @param diff
	 *            Input object. .
	 * @return an {@link ReferenceChange} if the input is a {@link StereotypeReferenceChange} which is refined
	 *         by an {@link ReferenceChange} or <code>null</code> otherwise.
	 */
	private ReferenceChange getReferenceChange(Object diff) {
		ReferenceChange referenceChange = null;
		if (diff instanceof StereotypeReferenceChange) {
			StereotypeReferenceChange stereotypeReferenceChange = (StereotypeReferenceChange)diff;
			Optional<Diff> element = Iterables.tryFind(stereotypeReferenceChange.getRefinedBy(), Predicates
					.instanceOf(ReferenceChange.class));
			if (element.isPresent()) {
				referenceChange = (ReferenceChange)element.get();
			}
		}
		return referenceChange;
	}

}
