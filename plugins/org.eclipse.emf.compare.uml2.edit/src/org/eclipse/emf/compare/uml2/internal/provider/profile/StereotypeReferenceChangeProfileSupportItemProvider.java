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
package org.eclipse.emf.compare.uml2.internal.provider.profile;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.compare.uml2.internal.provider.custom.UMLDiffCustomItemProvider;

/**
 * This item provider displays {@link StereotypeReferenceChange}s like it was basic {@link ReferenceChange}s.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypeReferenceChangeProfileSupportItemProvider extends UMLDiffCustomItemProvider {

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            .
	 */
	public StereotypeReferenceChangeProfileSupportItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public IComposedStyledString getStyledText(Object object) {
		final ReferenceChange referenceChange = getReferenceChange(object);
		if (referenceChange != null) {
			return getItemDelegator().getStyledText(referenceChange);
		}
		return super.getStyledText(object);
	}

	@Override
	public String getDescription(Object object) {
		final ReferenceChange referenceChange = getReferenceChange(object);
		if (referenceChange != null) {
			return getItemDelegator().getDescription(referenceChange);
		}
		return super.getDescription(object);
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
			Optional<Diff> element = Iterables.tryFind(stereotypeReferenceChange.getRefinedBy(),
					Predicates.instanceOf(ReferenceChange.class));
			if (element.isPresent()) {
				referenceChange = (ReferenceChange)element.get();
			}
		}
		return referenceChange;
	}

}
