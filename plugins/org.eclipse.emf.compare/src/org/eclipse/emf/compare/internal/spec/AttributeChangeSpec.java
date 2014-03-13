/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import com.google.common.base.Objects;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.impl.AttributeChangeImpl;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This specialization of the {@link AttributeChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class AttributeChangeSpec extends AttributeChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyLeftToRight()
	 * @deprecated Use
	 *             {@link org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff, org.eclipse.emf.common.util.Monitor)}
	 *             instead. See javadoc of IMerger.Registry for usage instructions.
	 */
	@Deprecated
	@Override
	public void copyLeftToRight() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyLeftToRight(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyRightToLeft()
	 * @deprecated Use
	 *             {@link org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff, org.eclipse.emf.common.util.Monitor)}
	 *             instead. See javadoc of IMerger.Registry for usage instructions.
	 */
	@Deprecated
	@Override
	public void copyRightToLeft() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyRightToLeft(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
		// Should we also discard equivalent diffs? And diffs that require this one?
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.AttributeChangeImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		EDataType eAttributeType = getAttribute().getEAttributeType();
		final String valueString;
		if (eAttributeType.isSerializable()) {
			valueString = EcoreUtil.convertToString(eAttributeType, getValue());
		} else {
			valueString = getValue().toString();
		}

		return Objects.toStringHelper(this).add("attribute",
				getAttribute().getEContainingClass().getName() + "." + getAttribute().getName()).add("value",
				valueString).add("kind", getKind()).add("source", getSource()).add("state", getState())
				.toString();
	}
}
