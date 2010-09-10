/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;


/**
 * Specific content provider for symbolic references. The expected input is an {@link MPatchModel} and it shows
 * {@link ChangeGroup}s, {@link IndepChange}s, and all symbolic references which must be resolved for the application of
 * the {@link MPatchModel}. This includes corresponding elements as well as cross-references.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class SymbolicReferenceContentProvider extends AdapterFactoryContentProvider {

	/**
	 * Direction.
	 */
	private final int direction;

	/**
	 * Default constructor.
	 * 
	 * @see SymbolicReferenceContentProvider
	 */
	public SymbolicReferenceContentProvider(int direction, AdapterFactory adapterFactory) {
		super(adapterFactory);
		this.direction = direction;
	}

	@Override
	public Object[] getChildren(Object object) {
		final Object[] result;

		if (object instanceof MPatchModel) {
			result = super.getChildren(object); // root element

		} else if (object instanceof ChangeGroup) {
			result = super.getChildren(object); // groups may only contain other changes

		} else if (object instanceof UnknownChange) {
			result = new Object[0]; // unknown changes are ... unknown ;-)

		} else if (object instanceof IndepChange) {
			final List<Object> resultList = new ArrayList<Object>();
			final IndepChange change = (IndepChange) object;

			// add all relevant references
			final Collection<EReference> relevantRefs = MPatchValidator.getImportantReferencesFor(change.eClass(),
					direction);
			for (EReference eRef : relevantRefs) {
				final Object ref = change.eGet(eRef);
				if (ref != null)
					resultList.add(ref);
			}

			// also: cross references which are not direct children of the change
			final Map<IElementReference, IndepChange> crossRefs = MPatchUtil.collectCrossReferences(Collections
					.singletonList(change));
			for (IElementReference symRef : crossRefs.keySet()) {
				if (!change.equals(symRef.eContainer()) && !resultList.contains(symRef))
					resultList.add(symRef);
			}
			result = resultList.toArray(new Object[resultList.size()]);
		} else {
			result = new Object[0]; // We don't support anything else...
		}

		// check for null objects!
		for (Object object2 : result) {
			if (object2 == null)
				throw new IllegalStateException("A child is null for object: " + object.toString() + " - Children: "
						+ Arrays.asList(result));
		}
		return result;
	}

	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof IElementReference) {
			return false;
		} else
			return super.hasChildren(object);
	}

}