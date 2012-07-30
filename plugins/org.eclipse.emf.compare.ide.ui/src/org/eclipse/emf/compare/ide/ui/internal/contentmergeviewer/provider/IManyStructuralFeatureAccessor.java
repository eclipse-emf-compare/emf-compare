/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IManyStructuralFeatureAccessor<T> {

	List<T> getValues();

	T getValue();

	EObject getValue(ReferenceChange referenceChange);

	ImmutableList<? extends Diff> getDiffFromThisSide();

	ImmutableList<? extends Diff> getDiffFromTheOtherSide();

	ImmutableList<? extends Diff> getDiffFromAncestor();

}
